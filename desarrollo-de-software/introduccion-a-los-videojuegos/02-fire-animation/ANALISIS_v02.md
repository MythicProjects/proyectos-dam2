# Análisis del proyecto: Fire Animation (Java Swing + MVP)

> Análisis hecho sobre el código actual de `src/` (paquetes `model`, `view`, `presenter` en minúsculas).
> Compilado con `javac -Xlint:all` (JDK 21) con la salida en una carpeta temporal fuera del proyecto: **compila sin errores**, solo con avisos de `serialVersionUID` y `this-escape`, que no son importantes aquí.
> Las cifras de rendimiento son medidas reales en esta máquina con una pequeña prueba externa (no forma parte del proyecto). Tómalas como orden de magnitud, no como valores exactos.

---

## 1. Resumen

El proyecto está **bien planteado** para ser el primero de este tipo. Las tres capas están separadas en paquetes, cada una tiene su interfaz, la vista **no importa nada del modelo**, `Main` monta las piezas en el EDT, la paleta está precalculada como tabla de consulta y el ciclo de vida del hilo del modelo (play / stop / volver a play) está bien resuelto con `synchronized`.

Los problemas importantes son cuatro:

1. **Hay un caso en que el apagado no termina nunca** (lo he reproducido) y pulsar *Stop* dos veces reaviva el fuego.
2. **La resolución por defecto es 1920×1080**, y a esa resolución un fotograma cuesta unos 48 ms. El objetivo era 30 fps (33 ms), así que no se llega. Además, la ventana sale más grande que la pantalla.
3. **El fotograma se pinta en el hilo de la vista y dentro del cerrojo del modelo**, y la misma imagen se lee a la vez desde el EDT. Esto bloquea al modelo y puede producir *tearing* (imagen mezclada entre dos fotogramas).
4. En MVP, la **vista es la dueña de los valores iniciales** y se los manda al modelo desde `setPresenter()`. Debería ser al revés, y la interfaz `IView` es tan pequeña que el presenter no puede comunicarle nada a la vista.

Todo tiene arreglo sin cambiar la idea general y **manteniendo un hilo en la vista y otro en el modelo**: el modelo simula y genera el fotograma en su hilo, y la vista lo dibuja en el suyo con `BufferStrategy`.

> **Nota sobre git:** has renombrado `Model/`, `View/` y `Presenter/` a minúsculas. Git ve ahora los ficheros antiguos como borrados y los nuevos como "sin seguimiento". Cuando hagas commit, usa `git add -A src` para que git lo registre como un renombrado y no se pierda el historial.

---

## 2. Errores (bugs)

Ordenados por gravedad.

### B1. El apagado puede no terminar nunca · `Model.java:63` y `Model.java:86-91` · **Alta**

**Qué pasa.** Cada fotograma de apagado resta `sparksPercentage / EXTINGUISH_FRAMES` a `currentSparks`. Pero `sparksPercentage` es el valor del slider *en ese momento*. Si después de pulsar *Stop* el usuario mueve el slider de chispas a 0, el paso pasa a ser `0 / 60 = 0`, `currentSparks` deja de bajar, nunca llega a 0 y el hilo no termina. Lo he comprobado: tras 4 s (el apagado debería durar ~2 s) el hilo seguía vivo.

Hay un segundo problema en `stop()`: si se pulsa *Stop* mientras ya se está apagando, `currentSparks` vuelve a valer `sparksPercentage` (por ejemplo 100) y **el fuego se reaviva**.

**Por qué.** El tamaño del paso depende de un valor que puede cambiar durante el proceso. La solución es calcular el paso **una sola vez**, al empezar el apagado, y no volver a empezar si ya se está apagando.

**Antes**
```java
private void extinguishingStep(){
    currentSparks = Math.max(0, currentSparks - sparksPercentage / EXTINGUISH_FRAMES);
    ...
}

@Override
public synchronized void stop(){
    if(thread == null) return ;
    pause = false;
    currentSparks = sparksPercentage;
    extinguishing = true;
}
```

**Después**
```java
private double extinguishDecrement; // cuánto baja cada fotograma; se fija al pulsar Stop

private void extinguishingStep(){
    currentSparks = Math.max(0, currentSparks - extinguishDecrement);
    fire.setSparksPercentage(currentSparks);

    if (currentSparks == 0){
        fire.clearBaseRow();
        if (fire.isCold()) running = false;
    }
}

@Override
public synchronized void stop(){
    if (thread == null || extinguishing) return; // ya parado o ya apagándose
    pause = false;
    currentSparks = sparksPercentage;
    extinguishDecrement = Math.max(sparksPercentage, 1.0) / EXTINGUISH_FRAMES; // nunca 0
    extinguishing = true;
}
```

---

### B2. Resolución inicial de 1920×1080: no llega a 30 fps y la ventana no cabe · `DataPanel.java:27-28`, `View.java:41-42` · **Alta**

**Qué pasa.**
- `Model` define `DEFAULT_WIDTH = 720` y `DEFAULT_HEIGHT = 180`, pero no se llegan a usar: `Presenter` llama a `view.setPresenter(this)` en su constructor, eso ejecuta `DataPanel.setPresenter()`, que a su vez llama a `sendResolution()` y cambia el modelo a **1920×1080** antes de que se muestre la ventana.
- `startView()` usa el tamaño de la imagen como tamaño preferido del `Canvas`, así que el canvas pide 1920 px de ancho **más** el panel izquierdo. En una pantalla Full HD la ventana se sale.
- Coste medido a 1920×1080: **~40 ms para actualizar + ~8 ms para pintar ≈ 48 ms por fotograma**. Para 30 fps solo hay 33 ms, y el máximo del spinner (3840×2160) cuesta ~190 ms por fotograma (≈5 fps).

**Por qué.** La resolución de la **simulación** y el tamaño de la **ventana** son cosas distintas. `drawImage(img, 0, 0, w, h, null)` ya escala la imagen, así que puedes simular a baja resolución (queda incluso más "retro") y mostrarla en grande.

**Antes** (`DataPanel.java`)
```java
widthJSpinner = new JSpinner(new SpinnerNumberModel (1920,16,3840,10));
heightJSpinner = new JSpinner(new SpinnerNumberModel (1080,16,2160,10));
```
**Antes** (`View.java`)
```java
BufferedImage render = presenter.getImage();
viewer.setPreferredSize(new Dimension(render.getWidth(), render.getHeight()));
```

**Después** (`DataPanel.java`: el valor inicial lo pone el presenter, ver sección 3)
```java
// valor inicial provisional; el presenter lo sobrescribe con el del modelo
widthJSpinner  = new JSpinner(new SpinnerNumberModel(720, 300, 1920, 10));
heightJSpinner = new JSpinner(new SpinnerNumberModel(180, 100, 1080, 10));
```
**Después** (`View.java`: tamaño de ventana fijo, independiente de la simulación)
```java
private static final Dimension VIEWER_SIZE = new Dimension(960, 540);
...
viewer.setPreferredSize(VIEWER_SIZE);
```

---

### B3. *Tearing*: la misma imagen se escribe y se lee desde dos hilos a la vez · `Render.java:19-33`, `Viewer.java:10-21` · **Media**

**Qué pasa.** `Render.getImage()` devuelve **siempre el mismo objeto** `BufferedImage` y lo reescribe en cada llamada. El hilo de la vista llama a `getImage()` (escribe), después a `repaint()`, y el **EDT** ejecuta `paint()` más tarde, leyendo esa imagen. Mientras el EDT dibuja, el hilo de la vista ya puede estar escribiendo el fotograma siguiente encima. Resultado: de vez en cuando se ve media imagen de un fotograma y media de otro.

Además, el campo `render` de `Viewer` lo escribe un hilo y lo lee otro sin ser `volatile`, así que no hay garantía de que el EDT vea el valor nuevo.

**Por qué.** Que un hilo escriba en un búfer mientras otro lo lee es una **condición de carrera**. La solución clásica en videojuegos es el **doble búfer**: el modelo escribe en una imagen "trasera" y, al terminar, la intercambia con la "delantera", que es la que se dibuja. La solución completa está en la sección 4.

---

### B4. El fotograma se genera en el hilo de la vista y bloquea al modelo · `Model.java:99-101`, `View.java:56` · **Media**

**Qué pasa.** `Model.getImage()` es `synchronized` y dentro recorre toda la matriz (`render.getImage()`). La llama el hilo de la vista **cada 16 ms (~60 veces/s)**, mientras que el modelo solo genera 30 fotogramas por segundo, así que **cada fotograma se pinta dos veces** para nada. Durante esos ~8 ms (a 1080p) el hilo del modelo no puede entrar en su bloque `synchronized` y espera. El EDT también se bloquea si el usuario cambia la resolución en ese momento (`setResolution` también es `synchronized`).

**Por qué.** Convertir temperaturas en píxeles es trabajo del modelo: debería hacerse **una vez por cada actualización**, en el hilo del modelo. La vista solo debería recoger el último fotograma terminado, que es una operación instantánea.

**Antes**
```java
@Override
public synchronized BufferedImage getImage(){
    return render.getImage(); // recorre W×H píxeles en el hilo que llama
}
```
**Después** (detalle completo en la sección 4)
```java
@Override
public BufferedImage getImage(){
    return render.getFrontImage(); // solo devuelve una referencia volatile, coste O(1)
}
```

---

### B5. Parámetros que cambia el EDT sin sincronización · `FireGenerator.java:11-13`, `Render.java:6`, `Model.java:110-122` · **Media**

**Qué pasa.** `setSparksPercentage`, `setCoolPointsPercentage`, `setRandomization` y `setPalette` se llaman desde el EDT (los sliders) y escriben campos normales (`double`, `Palette`) que luego lee el hilo del modelo. Según el modelo de memoria de Java, **sin `volatile` ni `synchronized` no está garantizado que el otro hilo vea el valor nuevo**. En un PC normal casi siempre funciona, y por eso no lo habrás notado, pero no es correcto. Además, en una JVM de 32 bits escribir un `double` no es atómico (JLS §17.7).

También hay un *check-then-act* en `Model.setSparksPercentage`: `if (!extinguishing) fire.setSparksPercentage(...)` puede leer `extinguishing == false` justo antes de que `stop()` lo ponga a `true`.

**Antes**
```java
@Override
public void setSparksPercentage(double percentage) {
    sparksPercentage = percentage;
    if (!extinguishing) fire.setSparksPercentage(percentage);
}
@Override
public void setCoolPointsPercentage(double percentage){
    fire.setCoolPointsPercentage(percentage);
}
```
**Después** (con el mismo cerrojo que ya usa el bucle; el coste es despreciable)
```java
@Override
public synchronized void setSparksPercentage(double percentage) {
    sparksPercentage = percentage;
    if (!extinguishing) fire.setSparksPercentage(percentage);
}
@Override
public synchronized void setCoolPointsPercentage(double percentage){
    fire.setCoolPointsPercentage(percentage);
}
@Override
public synchronized void setRandomization(int randomization){
    fire.setRandomization(randomization);
}
@Override
public synchronized void togglePause(){
    pause = !pause; // leer y escribir no es atómico: mejor dentro del cerrojo
}
```
En `Render`, la paleta puede ser `private volatile Palette palette;`.

---

### B6. Una resolución no válida se ignora sin avisar · `Presenter.java:33-36`, `DataPanel.java:27-28` · **Baja**

**Qué pasa.** El spinner deja elegir desde 16 px, pero el presenter descarta todo lo que sea menor de 300×100 **sin decir nada**: el usuario pulsa *Apply* y no ocurre nada.

**Por qué.** Validar en el presenter está bien, pero tiene que informar a la vista. Además, los límites de la vista deberían coincidir con los del presenter.

**Antes**
```java
@Override public void setResolution(int w, int h) {
    if (w < MIN_W_SIZE || h < MIN_H_SIZE) return;
    model.setResolution(w, h);
}
```
**Después**
```java
@Override public void setResolution(int w, int h) {
    if (w < MIN_WIDTH || h < MIN_HEIGHT) {
        view.showMessage("Resolución mínima: " + MIN_WIDTH + "×" + MIN_HEIGHT);
        return;
    }
    model.setResolution(w, h);
}
```
(Para esto hace falta añadir `showMessage` a `IView`, ver sección 3.)

---

### B7. La paleta acepta dos puntos con la misma temperatura · `Palette.java:21-29` · **Baja**

**Qué pasa.** La comprobación es `to < from`, así que se admite `to == from`. En ese caso `range = 0`, `ratio = 0/0 = NaN`, `Math.round(NaN) = 0` y el color queda en `0x00000000` (negro **transparente**). Si ese tramo es el último (por ejemplo, dos puntos con temperatura 255), la temperatura máxima se pinta transparente. Además, el mensaje de error ("must be between 0 and 255") no explica el problema real.

**Antes**
```java
if (to.getTemperature() < from.getTemperature()){
    throw new IllegalArgumentException("Palette must be between 0 and " + MAX_TEMPERATURE);
}
```
**Después**
```java
if (to.getTemperature() <= from.getTemperature()){
    throw new IllegalArgumentException(
        "Las temperaturas de la paleta deben ser estrictamente crecientes: "
        + from.getTemperature() + " -> " + to.getTemperature());
}
```

---

### B8. `InterruptedException` se trata a medias · `Model.java:50-52`, `View.java:59-61` · **Baja**

**Qué pasa.** Se captura la excepción y se para el bucle, que está bien, pero se pierde el *flag* de interrupción. Es una buena costumbre restaurarlo para que el código que esté por encima sepa que el hilo fue interrumpido.

**Antes**
```java
} catch(InterruptedException e){
    running = false;
}
```
**Después**
```java
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // restaura el flag
    running = false;
}
```

---

### B9. El hilo de la vista sigue trabajando aunque el fuego esté parado · `View.java:54-63` · **Baja**

**Qué pasa.** Tras *Stop* (o en pausa), el hilo de la vista sigue llamando a `getImage()` 60 veces por segundo, y cada llamada repinta toda la matriz (ver B4). El modelo está parado, pero la CPU sigue ocupada. Además, el hilo no tiene nombre ni es *daemon*. Ahora no importa porque `EXIT_ON_CLOSE` mata el proceso, pero si algún día cambias eso, el programa no terminaría al cerrar la ventana.

**Solución:** cuando se aplique B4, `getImage()` pasa a costar casi nada y el problema desaparece. Además, llama al hilo `"fire-view"` y márcalo con `setDaemon(true)` (código en la sección 4).

---

### Observación (no es un error): la actualización es "in-place" · `FireGenerator.java:59-72`

El bucle recorre `y` de abajo arriba y lee `temperatures[y + 1]`, **que ya se ha actualizado en este mismo fotograma**. Por eso el calor de la fila base sube por toda la columna en un único fotograma. En el algoritmo clásico (el fuego del *Doom* de PSX) se lee del fotograma anterior, y el fuego "sube" una fila por fotograma. No es incorrecto, es otro comportamiento, pero conviene que sepas que lo estás haciendo así. Si algún día quieres el comportamiento clásico, necesitas dos matrices (anterior y nueva) e intercambiarlas.

---

## 3. Arquitectura MVP

### Lo que está bien (mantenlo)

- **Paquetes separados** `model`, `view`, `presenter`, y una interfaz por capa.
- **La vista no importa nada de `model`.** Lo he comprobado: `view.*` solo importa `presenter.IPresenter` y clases de Java (`BufferedImage` es de `java.awt`, no de tu modelo). Esta es la regla más importante del MVP y la cumples.
- **El presenter depende de interfaces** (`IModel`, `IView`) y no de las clases concretas.
- **`Main` es la "raíz de composición"**: es el único sitio que conoce las clases concretas y las conecta. Lo creas todo dentro de `SwingUtilities.invokeLater`, que es lo correcto en Swing.
- **El modelo no sabe nada de Swing**: no tiene `JFrame`, `JPanel` ni listeners.
- Los paneles (`ControlPanel`, `DataPanel`) delegan en el presenter en lugar de tocar el modelo.

### Quién conoce a quién (estado actual)

```
Main ──crea──> Model, View, Presenter

View ─────> IPresenter          (llama a play/pause/stop/set*, getImage)
Presenter ─> IModel, IView      (pero de IView solo usa setPresenter y startView)
Model ────> (nadie)             ✔ correcto
```

El esquema es correcto. Lo que falla es **el sentido del flujo de datos**:

### Problema A: la vista es la dueña de los valores iniciales

Hoy los valores por defecto están **repetidos en tres sitios y no coinciden**:

| Valor          | `Model`   | `DataPanel` | `Presenter` |
|----------------|-----------|-------------|-------------|
| Resolución     | 720×180   | 1920×1080   | mín. 300×100 |
| Chispas %      | 100       | 100         | —           |
| Puntos fríos % | 70        | 70          | —           |
| Randomización  | 50        | 20          | —           |

Y quien manda es la vista, porque `DataPanel.setPresenter()` envía sus valores al modelo. Esto tiene dos problemas:

1. **Efecto secundario oculto**: un método que se llama `setPresenter` modifica el modelo. Quien lea `Presenter` no se lo espera.
2. En MVP, **el modelo es la fuente de verdad** y la vista solo muestra lo que el presenter le pasa.

**Antes** (`DataPanel.java:57-64`)
```java
public void setPresenter(IPresenter presenter)
{
    this.presenter = presenter;
    sendResolution();
    presenter.setSparksPercentage(sparkSlider.getValue());
    presenter.setCoolPointsPrecentage(coolPointsSlider.getValue());
    presenter.setRandomization(randomizeSlider.getValue());
}
```
**Después** (`DataPanel.java`: `setPresenter` solo guarda la referencia, y hay un método para recibir valores)
```java
public void setPresenter(IPresenter presenter) {
    this.presenter = presenter;
}

/** La llama el presenter para mostrar el estado actual del modelo. */
public void showValues(int width, int height, int sparks, int coolPoints, int randomization) {
    widthSpinner.setValue(width);
    heightSpinner.setValue(height);
    sparkSlider.setValue(sparks);
    coolPointsSlider.setValue(coolPoints);
    randomizeSlider.setValue(randomization);
}
```
Ojo: `setValue` en un `JSlider` dispara el `ChangeListener`, así que cada valor volverá al modelo una vez. Es inofensivo porque es el mismo valor, pero si quieres evitarlo, asigna los listeners **después** de llamar a `showValues`, o usa un flag `updatingFromPresenter`.

### Problema B: `IView` es demasiado pequeña

Ahora mismo el presenter **no puede decirle nada a la vista** (ni mostrar un error, ni pasarle los valores iniciales). En MVP la comunicación va en los dos sentidos: la vista avisa al presenter de lo que hace el usuario, y el presenter le dice a la vista qué mostrar.

**Antes** (`IView.java`)
```java
public interface IView {
    void setPresenter(IPresenter presenter);
    void startView();
}
```
**Después**
```java
public interface IView {
    void setPresenter(IPresenter presenter);
    void startView();
    void showValues(int width, int height, int sparks, int coolPoints, int randomization);
    void showMessage(String message);
}
```
Y en `View`, como el presenter podría llamar algún día desde otro hilo, conviene protegerse:
```java
@Override
public void showMessage(String message) {
    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, message));
}
```

### Problema C: `IModel` expone de más y de menos

- **De más:** `setPalette(Palette palette)` obliga a quien use la interfaz a conocer la clase concreta `Palette` del modelo, y nadie la llama. Si el presenter la usara, tendría que importar `model.Palette`, cosa que es aceptable en el presenter, pero si la vista quisiera elegir paleta no podría sin romper MVP. Tu propio comentario lo apunta bien: usa un **enum** con los tipos de paleta, o quítala hasta que la necesites.
- **De menos:** no hay getters para leer la configuración actual, así que el presenter no puede inicializar la vista (Problema A).

**Antes** (`IModel.java`)
```java
void setPalette(Palette palette); // Convertirlo en enum: ...
```
**Después**
```java
// model/PaletteType.java
public enum PaletteType { FIRE, BLUE_FIRE, GREEN_FIRE }

// IModel.java
void setPalette(PaletteType type);
int getWidth();
int getHeight();
double getSparksPercentage();
double getCoolPointsPercentage();
int getRandomization();
```

### Problema D: nombres que no coinciden entre capas

`IPresenter.pause()` llama a `IModel.togglePause()`. El presenter se llama `pause` pero en realidad alterna (pausa y reanuda). Usa el mismo nombre en las dos capas (`togglePause`) para que se entienda qué hace. También hay una errata en `setCoolPointsPrecentage` (debería ser *Percentage*) en `IPresenter.java:12`.

### Presenter propuesto

```java
public class Presenter implements IPresenter {
    private static final int MIN_WIDTH = 300;
    private static final int MIN_HEIGHT = 100;

    private final IModel model;
    private final IView view;

    public Presenter(IModel model, IView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void start() {
        view.setPresenter(this); // fuera del constructor: evita el aviso "this-escape"
        view.showValues(model.getWidth(), model.getHeight(),
                (int) model.getSparksPercentage(),
                (int) model.getCoolPointsPercentage(),
                model.getRandomization());
        view.startView();
        model.play();
    }

    @Override public void play()        { model.play(); }
    @Override public void togglePause() { model.togglePause(); }
    @Override public void stop()        { model.stop(); }

    @Override public BufferedImage getImage() { return model.getImage(); }

    @Override
    public void setResolution(int w, int h) {
        if (w < MIN_WIDTH || h < MIN_HEIGHT) {
            view.showMessage("Resolución mínima: " + MIN_WIDTH + "×" + MIN_HEIGHT);
            return;
        }
        model.setResolution(w, h);
    }

    @Override
    public void setSparksPercentage(double p) {
        model.setSparksPercentage(clamp(p, 0, 100));
    }

    @Override
    public void setCoolPointsPercentage(double p) {
        model.setCoolPointsPercentage(clamp(p, 0, 100));
    }

    @Override public void setRandomization(int r) { model.setRandomization(r); }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
```

Sobre tu pregunta del comentario `//clamp(p, 0, 100)) ??`: **sí, tiene sentido validar**. Lo ideal es hacerlo en dos sitios. El **presenter** valida la entrada del usuario para poder avisarle, y el **modelo** se protege a sí mismo con su propio `clamp`, porque nunca debería poder quedar en un estado inválido aunque lo use otro presenter.

### ¿Está bien que `Render` esté en el modelo?

Sí. Convertir temperaturas en colores es **lógica** (forma parte de "qué es el fuego"), y el resultado (`BufferedImage`) es un dato. **Dibujarlo en pantalla** sí es responsabilidad de la vista, y eso ya lo hace `Viewer`. El reparto es correcto. Lo único que falla es **en qué hilo** se ejecuta `Render` (B4).

### ¿Está bien que la vista "pida" la imagen (pull)?

Con un hilo propio en la vista, sí: es el patrón habitual del *game loop*. El hilo de la vista pide al presenter el último fotograma (`presenter.getImage()`) a su propio ritmo. La única condición es que esa llamada sea **barata y segura entre hilos**, y eso es justo lo que resuelve la sección 4.

---

## 4. Hilos y concurrencia

### Hilos que hay ahora

| Hilo | Quién lo crea | Qué hace |
|------|---------------|----------|
| **EDT** (Swing) | Swing | eventos de botones/sliders, `paint()` del `Viewer` |
| **fire-model** | `Model.play()` | `setSparkPoints` + `setCoolPoints` + `updateTemperatures`, 30 veces/s |
| **Thread-N** (sin nombre) | `View.startView()` | `presenter.getImage()` (¡genera el fotograma!) + `repaint()`, cada 16 ms |

### Lo que está bien

- **`running`, `pause`, `extinguishing` y `thread` son `volatile`.** Es correcto para *flags* que un hilo escribe y otro lee.
- **El ciclo de vida del hilo del modelo es correcto, y no es fácil hacerlo bien.** `run()` pone `thread = null` **dentro** del `synchronized`, y `play()` también es `synchronized`. Por eso, si alguien pulsa *Play* justo cuando el apagado acaba, solo pueden pasar dos cosas: o `play()` llega antes, pone `running = true` y el bucle continúa, o llega después, ve `thread == null` y crea un hilo nuevo. No se pueden quedar dos hilos vivos ni ninguno. Muy bien.
- Toda la actualización de la matriz está protegida con el mismo cerrojo que `setResolution`, así que **cambiar la resolución en mitad de un fotograma no rompe nada**.
- `repaint()` se puede llamar desde cualquier hilo (es de los pocos métodos de Swing/AWT que lo permiten).
- No hay riesgo de *deadlock*: solo existe un cerrojo (el del `Model`) y nunca se adquieren dos a la vez.

### Lo que falla

1. **El hilo de la vista hace trabajo del modelo** (B4) y tiene el cerrojo ocupado unos 8 ms × 60 veces/s.
2. **El EDT lee una imagen que el hilo de la vista está escribiendo** (B3).
3. **Los parámetros se escriben sin sincronización** (B5).
4. **`Thread.sleep(FRAME_MS)` fijo**: el tiempo real de cada fotograma es *trabajo + 33 ms*, no 33 ms. Si actualizar cuesta 20 ms, vas a ~19 fps. Hay que restar el tiempo que ha costado el trabajo.
5. **`Model` y `View` implementan `Runnable` con un `run()` público**, así que cualquiera podría llamar a `model.run()` desde otro hilo y saltarse todo el control. Es mejor un método privado y `new Thread(this::loop, ...)`.

### Diseño propuesto (con un hilo en el modelo y otro en la vista)

```
 hilo fire-model (30 Hz)                     hilo fire-view (~60 Hz)
 ───────────────────────                     ───────────────────────
 update temperatures                         img = presenter.getImage()   // O(1)
 synchronized(back) { pintar píxeles }       synchronized(img) { dibujar con BufferStrategy }
 front <-> back (swap, front es volatile)
```

- El **modelo** simula y pinta en la imagen **trasera**. Cuando termina, la intercambia con la **delantera**.
- La **vista** pide la delantera y la dibuja ella misma en su hilo con `BufferStrategy`, que es la forma habitual de hacer *active rendering* con un `Canvas`. Así el EDT no interviene en el dibujado y desaparece la carrera.
- Para que el modelo no reescriba una imagen que la vista todavía está dibujando, **los dos bloquean la propia imagen** mientras la usan. Ese bloqueo dura muy poco (pintar ~1 ms, dibujar ~1-2 ms).

#### `Render` (doble búfer)

**Antes** (`Render.java`)
```java
public BufferedImage getImage(){
    if(image.getWidth() != fire.getWidth() || image.getHeight() != fire.getHeight()){
            createImage();
    }
    int[][] temperatures = fire.getTemperatures();
    for (int y = 0; y < fire.getHeight(); y++) {
        for(int x = 0; x < fire.getWidth(); x++){
            image.setRGB(x, y, palette.getColor(temperatures[y][x]));
        }
    }
    return image;
}

private void createImage(){
    image = new BufferedImage(fire.getWidth(), fire.getHeight(), BufferedImage.TYPE_INT_ARGB);
}
```

**Después**
```java
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

class Render {
    private final FireGenerator fire;
    private volatile Palette palette;
    private volatile BufferedImage front; // la que lee la vista
    private BufferedImage back;           // solo la toca el hilo del modelo

    Render(FireGenerator fire, Palette palette) {
        this.fire = fire;
        this.palette = palette;
        createImages();
    }

    void setPalette(Palette palette) { this.palette = palette; }

    /** Solo desde el hilo del modelo. Pinta en la trasera y la publica. */
    void renderFrame() {
        if (back.getWidth() != fire.getWidth() || back.getHeight() != fire.getHeight()) {
            createImages();
        }
        int[][] temperatures = fire.getTemperatures();
        Palette p = palette; // copia local: el mismo valor durante todo el fotograma
        int width = fire.getWidth();

        synchronized (back) {
            // acceso directo al array de píxeles: muchísimo más rápido que setRGB
            int[] pixels = ((DataBufferInt) back.getRaster().getDataBuffer()).getData();
            for (int y = 0; y < fire.getHeight(); y++) {
                int[] row = temperatures[y];
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = p.getColor(row[x]);
                }
            }
        }
        BufferedImage tmp = front; // swap
        front = back;
        back = tmp;
    }

    /** Cualquier hilo. Coste O(1). */
    BufferedImage getFrontImage() { return front; }

    private void createImages() {
        front = new BufferedImage(fire.getWidth(), fire.getHeight(), BufferedImage.TYPE_INT_RGB);
        back  = new BufferedImage(fire.getWidth(), fire.getHeight(), BufferedImage.TYPE_INT_RGB);
    }
}
```
`TYPE_INT_RGB` en vez de `ARGB`: tu fuego no usa transparencia, y sin canal alfa el escalado al dibujar es más rápido. Los colores de la paleta ya llevan `0xFF` en el alfa y simplemente se ignora.

#### Bucle del modelo (con tiempo de fotograma real)

**Antes** (`Model.java:35-54`)
```java
@Override
public void run() {
    while (true) {
        synchronized (this) {
            if(!running){
                thread = null;
                extinguishing = false;
                return;
            }
            if(!pause) {
                if (extinguishing) extinguishingStep();
                updateModel();
            }
        }
        try{
            Thread.sleep(FRAME_MS); // 60fps
        } catch(InterruptedException e){
            running = false;
        }
    }
}
```

**Después**
```java
private static final long FRAME_NS = 1_000_000_000L / TARGET_FPS;

private void loop() {
    long next = System.nanoTime();
    while (true) {
        synchronized (this) {
            if (!running) {
                thread = null;
                extinguishing = false;
                return;
            }
            if (!pause) {
                if (extinguishing) extinguishingStep();
                updateModel();
                render.renderFrame(); // el fotograma se genera aquí, una vez por actualización
            }
        }
        next += FRAME_NS;
        long sleepNs = next - System.nanoTime();
        if (sleepNs > 0) {
            try {
                Thread.sleep(sleepNs / 1_000_000, (int) (sleepNs % 1_000_000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        } else {
            next = System.nanoTime(); // vamos tarde: no intentes "recuperar" fotogramas
        }
    }
}

@Override
public synchronized void play() {
    ...
    if (thread == null) {
        thread = new Thread(this::loop, "fire-model");
        thread.start();
    }
}

@Override
public BufferedImage getImage() {
    return render.getFrontImage(); // ya no necesita synchronized
}
```
(`Model` ya no implementa `Runnable`.)

Con esto el modelo ocupa el cerrojo un poco más (simulación + pintado), así que conviene aplicar también las optimizaciones de la sección 5. Con ellas, un fotograma a 1080p baja de ~48 ms a ~8 ms y el EDT apenas espera.

#### Hilo de la vista con `BufferStrategy`

**Antes** (`Viewer.java`)
```java
public class Viewer extends Canvas{
    private BufferedImage render;

    public void updateRender(BufferedImage render){
        this.render = render;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (render != null){
            g.drawImage(render, 0, 0, getWidth(), getHeight(), null);
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}
```

**Después**
```java
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Viewer extends Canvas {
    private BufferStrategy strategy;

    public Viewer() {
        setIgnoreRepaint(true); // pintamos nosotros desde el hilo de la vista, no el EDT
    }

    /** Solo desde el hilo de la vista. */
    public void paintFrame(BufferedImage frame) {
        if (frame == null || !isDisplayable()) return;
        if (strategy == null) {
            createBufferStrategy(2);
            strategy = getBufferStrategy();
        }
        do {
            do {
                Graphics g = strategy.getDrawGraphics();
                try {
                    synchronized (frame) { // el modelo no puede reescribirla mientras dibujamos
                        g.drawImage(frame, 0, 0, getWidth(), getHeight(), null);
                    }
                } finally {
                    g.dispose();
                }
            } while (strategy.contentsRestored());
            strategy.show();
        } while (strategy.contentsLost());
    }
}
```
Los dos `do/while` vienen del patrón que recomienda la documentación de `BufferStrategy`: el sistema puede "perder" el búfer de vídeo (por ejemplo, al minimizar la ventana) y hay que volver a pintarlo.

**Antes** (`View.java:39-63`)
```java
@Override
public void startView(){
    ...
    running = true;
    if (thread != null) return; // ya arrancada
    thread = new Thread(this);
    thread.start();
}

@Override
public void run() {
    while (running) {
        viewer.updateRender(presenter.getImage());
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            running = false;
        }
    }
}
```

**Después**
```java
private static final long VIEW_FRAME_MS = 16; // ~60 fps de refresco de pantalla

@Override
public void startView() {
    viewer.setPreferredSize(VIEWER_SIZE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    if (thread != null) return; // ya arrancada
    running = true;
    thread = new Thread(this::renderLoop, "fire-view");
    thread.setDaemon(true); // no impide que la JVM termine
    thread.start();
}

private void renderLoop() {
    while (running) {
        viewer.paintFrame(presenter.getImage());
        try {
            Thread.sleep(VIEW_FRAME_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            running = false;
        }
    }
}
```
(`View` ya no implementa `Runnable`.)

### Regla de oro de Swing

> Los componentes Swing (`JSlider`, `JSpinner`, `JLabel`...) **solo se tocan desde el EDT**.

Tu código ya la cumple: el hilo de la vista solo toca el `Canvas`, que es AWT y se dibuja con `BufferStrategy`, y eso está permitido. Si en el futuro el modelo tiene que avisar a la vista desde su hilo (por ejemplo, "el fuego se ha apagado" para cambiar el texto de un botón), esa llamada debe pasar por `SwingUtilities.invokeLater(...)`.

---

## 5. Rendimiento

### Coste por fotograma (medido)

Coste de lo que hace ahora cada fotograma (actualizar la matriz + `Render.getImage()`):

| Resolución  | Celdas | Actualizar | Pintar (`setRGB`) | Total  | fps máx. |
|-------------|--------|------------|-------------------|--------|----------|
| 720×180     | 130 k  | 2,5 ms     | 0,5 ms            | 3 ms   | 30 ✔     |
| 1920×1080   | 2,1 M  | 40 ms      | 8 ms              | 48 ms  | ~20 ✘    |
| 3840×2160   | 8,3 M  | 161 ms     | 32 ms             | 193 ms | ~5 ✘     |

Y además, ahora mismo el pintado se hace **dos veces por fotograma del modelo** (B4).

El coste es **O(ancho × alto)**: duplicar la resolución en cada eje multiplica el trabajo por 4.

### Dónde se va el tiempo y cómo arreglarlo

| Causa | Por qué es lento | Solución |
|-------|------------------|----------|
| `java.util.Random` en cada celda | Es *thread-safe*: cada llamada hace una operación atómica (CAS) sobre un `AtomicLong` | `SplittableRandom` (no sincronizado), creado una vez |
| `random.nextDouble(bound)` + aritmética `double` | Conversiones `double` ↔ `int` en cada celda | `nextInt(maxNoise + 1)` y aritmética entera |
| `image.setRGB(x, y, c)` por píxel | Cada llamada pasa por el `ColorModel` y comprueba límites | Escribir directamente en el `int[]` de `DataBufferInt` |
| `int[][]` | Cada fila es un objeto aparte, y hay una indirección más por acceso | `int[]` de una dimensión con índice `y * width + x` (opcional) |
| `isCold()` en cada fotograma de apagado | Recorre toda la matriz | Aceptable. Si quieres, compruébalo solo cada N fotogramas |
| Simular a la resolución de pantalla | El fuego no necesita tanta resolución | **Simular a ~480×270 o 720×180 y escalar al dibujar** |

He probado una versión de `updateTemperatures` + pintado con `SplittableRandom`, aritmética entera, matriz 1D y escritura directa de píxeles, a **1920×1080**:

| | Actualizar | Pintar |
|-|------------|--------|
| Actual | 40 ms | 8 ms |
| Optimizado | **7,3 ms** | **0,6 ms** |

Es unas **6 veces más rápido** sin cambiar el algoritmo.

#### `updateTemperatures` optimizado (manteniendo `int[][]`)

**Antes** (`FireGenerator.java:56-73`)
```java
private final Random random = new Random();
...
public void updateTemperatures(){
    double maxNoise = Math.max(0, randomization - 10);

    for (int y = height - 2; y >= 0; y--){
        for(int x = 0; x < width; x++){
            double left = temperatures[y + 1][Math.max(x - 1, 0)];
            double center = temperatures[y + 1][x];
            double right = temperatures[y + 1][Math.min(x + 1, width - 1)];
            double self = temperatures[y][x];

            double t = (self + left + center + right) / 3.9 - BASE_COOLING - random.nextDouble(maxNoise + 1);
            t = Math.min(Palette.MAX_TEMPERATURE, t);

            temperatures[y][x] = (int) Math.max(t, 0);
        }
    }
}
```

**Después**
```java
private static final double SPREAD_DIVISOR = 3.9; // < 4: el fuego gana un poco de calor al subir
private static final int BASE_COOLING = 2;
private static final int NOISE_OFFSET = 10;

private final SplittableRandom random = new SplittableRandom();
...
public void updateTemperatures() {
    int noiseBound = Math.max(0, randomization - NOISE_OFFSET) + 1;
    int lastX = width - 1;

    for (int y = height - 2; y >= 0; y--) {
        int[] row = temperatures[y];
        int[] below = temperatures[y + 1];
        for (int x = 0; x <= lastX; x++) {
            int sum = row[x] + below[x == 0 ? 0 : x - 1] + below[x] + below[x == lastX ? lastX : x + 1];
            int t = (int) (sum / SPREAD_DIVISOR) - BASE_COOLING - random.nextInt(noiseBound);
            row[x] = t < 0 ? 0 : Math.min(t, Palette.MAX_TEMPERATURE);
        }
    }
}
```
(`randomization` pasaría a ser `int`, que es lo que recibe su setter.) Guardar `temperatures[y]` y `temperatures[y + 1]` en variables locales evita buscar la fila en cada celda.

#### Ritmo del bucle

Con `Thread.sleep(FRAME_MS)` fijo, el fotograma dura *trabajo + 33 ms*. La versión con `System.nanoTime()` de la sección 4 corrige esto.

---

## 6. Calidad del código

### Lo que está bien

- **La paleta como tabla de consulta (`int[256]`)** es la decisión más acertada del proyecto: la interpolación se hace una vez y luego cada píxel cuesta un acceso a array.
- **Los comentarios de `Palette` sobre las máscaras de bits y la interpolación** son muy buenos para estudiar. Mantenlos.
- `ColorTargetDTO` tiene los campos `final`: es un objeto inmutable, bien hecho.
- Las constantes `TARGET_FPS`, `FRAME_MS` y `EXTINGUISH_FRAMES` se calculan unas a partir de otras. Así, si cambias los fps, todo se ajusta.
- Has llamado al hilo `"fire-model"`, lo que facilita la depuración (aparece en `jstack` y en el depurador).
- Has sobrescrito `update()` en `Viewer` para evitar el parpadeo del borrado de AWT. Muestra que entiendes cómo pinta AWT.

### Nombres

| Dónde | Ahora | Propuesta | Por qué |
|-------|-------|-----------|---------|
| `View.java:28` | `lefJPanel` | `leftPanel` | Errata. Tampoco hace falta poner el tipo en el nombre |
| `DataPanel.java:37` | `"Widht"` | `"Width"` | Errata visible para el usuario |
| `IPresenter.java:12` | `setCoolPointsPrecentage` | `setCoolPointsPercentage` | Errata, y no coincide con `IModel` |
| `DataPanel` | `widthJSpinner` | `widthSpinner` | Igual que `sparkSlider`: sé consistente |
| `FireGenerator` | `setSparkPoints()` / `setCoolPoints()` | `seedSparks()` / `seedCoolPoints()` | En Java, `setX` significa "setter de X". Estos métodos no reciben valor: generan puntos |
| `Viewer.java:8` | `render` | `frame` o `image` | Es una imagen, no un `Render`. Confunde con la clase `Render` |
| `Render` | `Render` | `FireRenderer` | Las clases suelen ser sustantivos ("el que pinta") |
| `IPresenter` / `IModel` | `pause()` / `togglePause()` | `togglePause()` en las dos | Mismo concepto, mismo nombre |
| `Presenter.java:11-12` | `MIN_W_SIZE` | `MIN_WIDTH` | Más claro |

### Convenciones de Java

**Constantes que no son `static`** · `Presenter.java:11-12`
```java
// Antes
private final int MIN_W_SIZE = 300;
private final int MIN_H_SIZE = 100;
```
```java
// Después
private static final int MIN_WIDTH = 300;
private static final int MIN_HEIGHT = 100;
```
Una constante en MAYÚSCULAS debe ser `static final`: hay una sola copia por clase y no una por objeto.

**Falta `@Override`** · `Presenter.java:28-29`
```java
// Antes
@Override
public void play() { model.play(); }
public void pause() { model.togglePause(); }
public void stop() { model.stop(); }
```
```java
// Después
@Override public void play()        { model.play(); }
@Override public void togglePause() { model.togglePause(); }
@Override public void stop()        { model.stop(); }
```
`@Override` hace que el compilador te avise si el método no coincide con el de la interfaz (por ejemplo, si cambias un nombre en un lado y no en el otro).

**Formato** · `FireGenerator.java:82-84`, mal indentado:
```java
// Antes
    public void clearTemperatures() {
    temperatures = new int[height][width];
}
```
```java
// Después
    public void clearTemperatures() {
        temperatures = new int[height][width];
    }
```
También hay espacios irregulares (`implements IModel, Runnable{`, `return ;`, `SpinnerNumberModel (1920,...)`). Usa el formateador del IDE (en VS Code, `Shift+Alt+F`).

**`ColorTargetDTO` puede ser un `record`** (Java 16+). Ya necesitas Java 17+ por `Random.nextDouble(bound)`:
```java
// Antes: 19 líneas con constructor y getters
public class ColorTargetDTO {
    private final int temperature;
    private final int color;
    ...
}
```
```java
// Después
public record ColorTarget(int temperature, int color) { }
// uso: target.temperature(), target.color()
```
Además, "DTO" (*Data Transfer Object*) se usa para datos que viajan entre capas o sistemas. Esto es un **valor** interno de la paleta, así que `ColorTarget` o `ColorStop` describe mejor lo que es.

### Números mágicos

Un número mágico es una cifra en el código sin nombre que explique qué significa:

| Dónde | Número | Constante sugerida |
|-------|--------|-------------------|
| `FireGenerator.java:67` | `3.9` | `SPREAD_DIVISOR` |
| `FireGenerator.java:57` | `10` | `NOISE_OFFSET` |
| `FireGenerator.java:32,41` | `100` | `PERCENT` |
| `View.java:58` | `16` | `VIEW_FRAME_MS` |
| `Model.java:26-28` | `100`, `70`, `50` | `DEFAULT_SPARKS`, `DEFAULT_COOL_POINTS`, `DEFAULT_RANDOMIZATION` |

### Comentarios desactualizados o que sobran

- `Model.java:11`: `// ~2 s a 60 fps`. Son 30 fps.
- `Model.java:49`: `// 60fps`. Son 30.
- `Palette.java:68`: el punto 255 dice "blanco cálido" igual que el 150, pero es blanco puro.
- Código comentado: `FireGenerator.java:14,66`, `Palette.java:31,63,65`, `View.java:72-85`, `Presenter.java:39`. Si ya no lo usas, bórralo: está guardado en el historial de git. El código comentado envejece y confunde.
- `FireGenerator.java:74-77`: el comentario que explica el algoritmo está **debajo** del método. Muévelo encima como Javadoc:
  ```java
  /**
   * Cada celda pasa a ser la media de sí misma y de sus tres vecinas de la fila inferior
   * (izquierda, centro, derecha), menos un enfriamiento base y un ruido aleatorio.
   */
  public void updateTemperatures() { ... }
  ```

### Encapsulación

- **`getTemperatures()` devuelve el array interno** (`FireGenerator.java:79-81`): quien lo reciba puede modificar la simulación desde fuera. Ahora solo lo usa `Render`, que está en el mismo paquete, así que basta con hacerlo **package-private** (sin `public`).
- **`FireGenerator` y `Render` pueden ser package-private** (`class FireGenerator`): fuera de `model` nadie debería conocerlas. Si `IModel` deja de exponer `Palette` (sección 3), `Palette` también puede serlo. Así el compilador te **impide** saltarte el MVP sin querer.
- **`public void run()`** en `Model` y `View`: cualquiera puede llamarlo. Con `new Thread(this::loop, ...)` el método puede ser `private` (sección 4).
- **El estado del modelo son tres booleanos** (`running`, `pause`, `extinguishing`) que admiten combinaciones sin sentido (por ejemplo, `pause && extinguishing && !running`). Un `enum` lo deja claro:
  ```java
  // Antes
  private volatile boolean running;
  private volatile boolean pause;
  private volatile boolean extinguishing = false;
  ```
  ```java
  // Después
  private enum State { STOPPED, RUNNING, PAUSED, EXTINGUISHING }
  private volatile State state = State.STOPPED;
  ```
  Con un `switch (state)` en el bucle, el flujo de play/pause/stop se entiende de un vistazo. Es una mejora opcional: con los arreglos de B1 los booleanos también funcionan.

---

## 7. Lista de cambios priorizada

### Prioridad 1: lo que rompe el programa o su comportamiento

1. **B1**: calcular el paso de apagado al pulsar *Stop* e ignorar un segundo *Stop* (`Model.java:63`, `86-91`).
2. **B2**: resolución inicial razonable (720×180) y tamaño de ventana fijo e independiente de la simulación (`DataPanel.java:27-28`, `View.java:41-42`).
3. **B3 + B4**: doble búfer en `Render`, generar el fotograma en el hilo del modelo y dibujar con `BufferStrategy` en el hilo de la vista (sección 4).
4. **B5**: `synchronized` en los setters de `Model` y `volatile` en `Render.palette`.
5. **Rendimiento**: `SplittableRandom` + escritura directa de píxeles (sección 5). Es poco código y multiplica la velocidad por 6.

### Prioridad 2: arquitectura MVP

6. El **modelo es la fuente de los valores iniciales**: getters en `IModel`, `showValues(...)` en `IView`, y quitar los efectos secundarios de `DataPanel.setPresenter()`.
7. Añadir `showMessage(...)` a `IView` y usarlo al validar en el presenter (**B6**). Igualar los límites del spinner con los del presenter.
8. Quitar `setPalette(Palette)` de `IModel` o cambiarlo por un `enum PaletteType`.
9. Unificar `pause` / `togglePause` y corregir `setCoolPointsPrecentage`.
10. Llamar a `view.setPresenter(this)` desde `start()` en lugar del constructor.
11. Ritmo del bucle con `System.nanoTime()` en lugar de `sleep` fijo.
12. `Model` y `View` sin `implements Runnable`, con hilos con nombre y el de la vista como *daemon* (**B9**).

### Prioridad 3: calidad y estética

13. Paleta estrictamente creciente con un mensaje de error claro (**B7**).
14. Restaurar el flag de interrupción en los `catch` (**B8**).
15. Corregir los nombres de la tabla de la sección 6 (`lefJPanel`, `"Widht"`, `setSparkPoints`...).
16. Constantes `static final` y nombres para los números mágicos.
17. Añadir `@Override` donde falta y formatear el código con el IDE.
18. Borrar el código comentado y corregir los comentarios desactualizados ("60 fps").
19. `ColorTargetDTO` como `record` y clases internas del modelo package-private.
20. (Opcional) `enum State` en lugar de tres booleanos.
21. (Opcional) Decidir si quieres la actualización *in-place* o la clásica con dos matrices.

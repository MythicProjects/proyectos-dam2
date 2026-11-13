# Resumen de la práctica MyPlaces

---

## 1. Objetivo y estructura

### Objetivo

Crear una app móvil con React Native + Expo + TypeScript para ver, añadir, guardar y borrar lugares, usando las piezas típicas de una app real: navegación, formularios, almacenamiento local, una API externa, ubicación, conectividad y sensores.

### Por qué `src/` está organizado así

| Carpeta / archivo | Qué contiene | Por qué está separado |
|---|---|---|
| `src/app/` | Solo pantallas | Expo Router convierte cada archivo de esta carpeta en una ruta, así que si metes otra cosa aquí se convierte en pantalla. |
| `src/app/_layout.tsx` | El `Stack` y el `PlacesProvider` | Es el "marco" que envuelve todas las pantallas: define la navegación y comparte los datos con todas. |
| `src/app/index.tsx` | Lista de lugares (ruta `/`) | `index` es siempre la pantalla inicial. |
| `src/app/add.tsx` | Pantalla de añadir (ruta `/add`) | Valida los datos y decide qué hacer con ellos. |
| `src/app/discover.tsx` | Resultados de la API + favoritos (ruta `/discover`) | Pantalla que consume la API de Wikipedia. |
| `src/app/sensors.tsx` | Acelerómetro (ruta `/sensors`) | Pantalla del sensor. |
| `src/app/detail/[id].tsx` | Detalle de un lugar (ruta `/detail/123`) | Los corchetes crean una **ruta dinámica**: `[id]` es una parte variable de la URL. |
| `src/components/` | `PlaceCard`, `PlaceForm`, `LocationButton`, `ConnectionStatus` | Piezas de interfaz reutilizables que reciben datos por *props* y no dependen de ninguna pantalla. |
| `src/context/PlacesContext.tsx` | Estado compartido + AsyncStorage | Un único sitio guarda la lista de lugares, así todas las pantallas ven los mismos datos sin pasárselos de una a otra. |
| `src/hooks/useLocation.ts` | Lógica de permiso + ubicación | Un hook propio permite reutilizar la misma lógica en varios componentes (`LocationButton`, `PlaceForm`, `discover`). |
| `src/api/discover.ts` | La petición `fetch` | Separa "cómo se piden los datos" de "cómo se muestran". |
| `src/data/places.ts` | El tipo `Place` y datos de ejemplo | Define la forma de un lugar para que todo el proyecto la comparta. |

La idea general es **separar responsabilidades**: las pantallas muestran, los componentes se reutilizan, el contexto guarda, el hook encapsula lógica y la carpeta `api` habla con el exterior.

---

## 2. Qué se ha importado y para qué

### React (`react`)

| Import | Para qué |
|---|---|
| `useState` | Crea una variable de estado que, al cambiar, vuelve a dibujar el componente. Ej: `const [error, setError] = useState<string \| null>(null);` |
| `useEffect` | Ejecuta código *después* de dibujar, y otra vez cuando cambian sus dependencias. Ej: `useEffect(() => { load(coords); }, [isConnected, coords]);` |
| `createContext` | Crea un "canal" de datos compartidos para todo el árbol de componentes. Ej: `const PlacesContext = createContext<PlacesContextValue \| null>(null);` |
| `useContext` | Lee el valor de un contexto desde cualquier componente que esté dentro de su Provider. Ej: `const context = useContext(PlacesContext);` |
| `ReactNode` | Tipo de TypeScript que significa "cualquier cosa que React pueda dibujar" (se usa para `children`). |

### React Native (`react-native`)

| Import | Para qué |
|---|---|
| `View` | Contenedor genérico, equivalente a un `<div>`. |
| `Text` | Único componente que puede mostrar texto en pantalla. |
| `Image` | Muestra una imagen; con URL se usa `source={{ uri: "https://..." }}`. |
| `Pressable` | Cualquier zona pulsable; su `style` puede ser una función que recibe `{ pressed }`. |
| `TextInput` | Campo de texto; se controla con `value` y `onChangeText`. |
| `FlatList` | Lista eficiente que solo dibuja los elementos visibles; necesita `data`, `renderItem` y `keyExtractor`. |
| `ActivityIndicator` | La ruedecita de "cargando". |
| `StyleSheet.create` | Define los estilos en un objeto (validado y optimizado). Ej: `const styles = StyleSheet.create({ title: { fontSize: 24 } });` |
| `Alert.alert` | Muestra un diálogo nativo con título, mensaje y botones. Ej: `Alert.alert("Lugar guardado", "Se ha añadido");` |
| `Platform.OS` | Dice en qué plataforma corre la app (`"android"`, `"ios"` o `"web"`). |

### Expo Router (`expo-router`)

| Import | Para qué |
|---|---|
| `Stack` | Navegador en pila: cada pantalla nueva se pone encima y "Volver" la quita. |
| `Stack.Screen` | Configura una pantalla (título de la cabecera, etc.); se usa en el layout y también dentro de `detail/[id]` para cambiar el título dinámicamente. |
| `router.push(ruta)` | Abre una pantalla nueva encima. Ej: `router.push("/add")` |
| `router.push({ pathname, params })` | Abre una ruta dinámica pasándole parámetros. Ej: `router.push({ pathname: "/detail/[id]", params: { id: item.id } })` |
| `router.back()` | Vuelve a la pantalla anterior. |
| `router.canGoBack()` | Devuelve `true` si hay una pantalla a la que volver. |
| `router.replace("/")` | Cambia la pantalla actual por otra sin dejar historial. |
| `useLocalSearchParams` | Lee los parámetros de la ruta (el `id` de `detail/[id]`). Ej: `const { id } = useLocalSearchParams<{ id: string }>();` |

### AsyncStorage (`@react-native-async-storage/async-storage`)

| Función | Para qué |
|---|---|
| `AsyncStorage.getItem(clave)` | Lee un texto guardado en el móvil (devuelve `null` si no existe). |
| `AsyncStorage.setItem(clave, texto)` | Guarda un texto en el móvil de forma persistente (sobrevive a reiniciar la app). |

### NetInfo (`@react-native-community/netinfo`)

| Función | Para qué |
|---|---|
| `useNetInfo()` | Hook que devuelve el estado de la red; `isConnected` vale `true`, `false` o `null` (aún no se sabe) y `type` el tipo (`wifi`, `cellular`…). |

### Expo Location (`expo-location`)

| Función | Para qué |
|---|---|
| `import * as Location` | Importa todo el módulo bajo el nombre `Location`. |
| `Location.requestForegroundPermissionsAsync()` | Pide el permiso de ubicación y devuelve `{ status }` (`"granted"` si se concede). |
| `Location.getCurrentPositionAsync({ accuracy })` | Obtiene la posición actual; las coordenadas están en `position.coords.latitude/longitude`. |
| `Location.Accuracy.Balanced` | Precisión media: equilibrio entre exactitud y batería. |

### Expo Sensors (`expo-sensors`)

| Función | Para qué |
|---|---|
| `Accelerometer.isAvailableAsync()` | Comprueba si el dispositivo tiene acelerómetro (en web suele ser `false`). |
| `Accelerometer.setUpdateInterval(ms)` | Cada cuántos milisegundos se recibe una lectura. |
| `Accelerometer.addListener(fn)` | Llama a `fn` con `{ x, y, z }` en cada lectura y devuelve una suscripción. |
| `subscription.remove()` | Cancela la suscripción para no gastar batería al salir de la pantalla. |

### Imports propios (alias `@/`)

`@/` es un atajo configurado en `tsconfig.json` que apunta a `src/`, así se evita escribir `../../`. Ej: `import { usePlaces } from "@/context/PlacesContext";`

| Import | Para qué |
|---|---|
| `PlacesProvider` | Envuelve la app para que todas las pantallas compartan la lista de lugares. |
| `usePlaces()` | Devuelve `{ places, addPlace, removePlace }` desde cualquier pantalla. |
| `useLocation()` | Devuelve `{ coords, message, loading, getLocation }` para pedir y mostrar la ubicación. |
| `fetchNearbyPlaces(lat, lon)` | Pide a la API de Wikipedia lugares cercanos a unas coordenadas (por defecto Barcelona). |
| `Place`, `Coords`, `PlaceFormData`, `WikiPlace` | Tipos que describen la forma de cada dato. |
| `PlaceCard`, `PlaceForm`, `LocationButton` | Componentes reutilizables de la interfaz. |

### Funciones propias definidas en el proyecto

| Función | Dónde | Qué hace |
|---|---|---|
| `loadPlaces()` | `PlacesContext` | Lee la clave `places` de AsyncStorage y la convierte en array (o `[]` si falla). |
| `savePlaces(places)` | `PlacesContext` | Convierte el array a texto y lo guarda en AsyncStorage. |
| `update(lista)` | `PlacesContext` | Cambia el estado y guarda en disco a la vez, para que nunca se desincronicen. |
| `addPlace(place)` | `PlacesContext` | Crea una lista nueva con el lugar añadido al final. |
| `removePlace(id)` | `PlacesContext` | Crea una lista nueva sin el lugar con ese id. |
| `getLocation()` | `useLocation` | Pide permiso, obtiene la posición y gestiona carga y errores. |
| `save(data, coords)` | `add.tsx` | Limpia y valida el formulario y, si es correcto, añade el lugar y vuelve atrás. |
| `change(key, value)` | `PlaceForm` | Actualiza un único campo del formulario manteniendo los demás. |
| `goBack()` | `detail/[id]` | Vuelve atrás si se puede, o va al inicio si no. |
| `confirmDelete()` | `detail/[id]` | Pide confirmación (`window.confirm` en web, `Alert` en móvil) y borra. |
| `load(center)` | `discover.tsx` | Consulta la API gestionando los estados de carga, error y sin conexión. |
| `favoriteId(item)` | `discover.tsx` | Genera el id `wiki-<pageid>` para un resultado de la API. |
| `isSaved(item)` | `discover.tsx` | Comprueba si ese resultado ya está guardado. |
| `saveFavorite(item)` | `discover.tsx` | Convierte un resultado de la API en `Place` y lo añade si no estaba ya. |

---

## 3. Funciones que vienen de base (JavaScript / navegador)

Estas no se importan: forman parte del lenguaje o del entorno.

### Texto

| Función | Qué hace |
|---|---|
| `texto.trim()` | Quita los espacios del principio y del final. Ej: `"  Roma ".trim()` → `"Roma"` |
| `texto.startsWith(x)` | Devuelve `true` si el texto empieza por `x`. Ej: `url.startsWith("https://")` |
| `numero.toString()` | Convierte un número en texto. Ej: `(42).toString()` → `"42"` |
| `numero.toFixed(n)` | Convierte un número en texto con `n` decimales. Ej: `(2.123456).toFixed(2)` → `"2.12"` |

### Arrays

| Función | Qué hace |
|---|---|
| `array.find(fn)` | Devuelve el **primer** elemento que cumple la condición, o `undefined`. Ej: `places.find((p) => p.id === id)` |
| `array.filter(fn)` | Devuelve un **array nuevo** solo con los elementos que cumplen la condición. Ej: `places.filter((p) => p.id !== id)` |
| `array.some(fn)` | Devuelve `true` si **al menos uno** cumple la condición. Ej: `places.some((p) => p.id === "wiki-5")` |
| `array.length` | Número de elementos del array. |

### Datos y JSON

| Función | Qué hace |
|---|---|
| `JSON.stringify(obj)` | Convierte un objeto/array en texto (para poder guardarlo). Ej: `JSON.stringify([{ id: "1" }])` → `'[{"id":"1"}]'` |
| `JSON.parse(texto)` | Convierte ese texto de vuelta en objeto/array. |
| `Date.now()` | Devuelve los milisegundos desde 1970; se usa como id único. Ej: `Date.now().toString()` → `"1762950000000"` |
| `Math.sqrt(x)` | Raíz cuadrada; se usa para calcular la fuerza total del acelerómetro. |

### Red y entorno

| Función | Qué hace |
|---|---|
| `fetch(url)` | Hace una petición HTTP y devuelve una promesa con la respuesta. |
| `response.ok` | `true` si el código HTTP está entre 200 y 299 (`fetch` **no** lanza error con un 404 o 500, por eso se comprueba). |
| `response.status` | El código HTTP (200, 404, 500…). |
| `response.json()` | Lee el cuerpo de la respuesta y lo convierte en objeto. |
| `new URLSearchParams({...})` | Construye la parte `?a=1&b=2` de una URL a partir de un objeto, escapando los caracteres especiales. |
| `promesa.then(fn)` | Ejecuta `fn` con el resultado cuando la promesa termina. Ej: `loadPlaces().then(setPlaces);` |
| `window.confirm(texto)` | Diálogo del navegador con Aceptar/Cancelar; devuelve `true` o `false` (solo web). |
| `console.log` / `console.warn` | Escriben mensajes en la consola para depurar. |
| `throw new Error(msg)` | Lanza un error que corta la ejecución hasta el `catch` más cercano. |

---

## 4. Teoría de TypeScript / JavaScript / React

### Variables y tipos

| Concepto | Explicación | Ejemplo |
|---|---|---|
| `const` | Declara una variable que no se puede reasignar. | `const name = "Roma";` |
| `let` | Declara una variable que sí se puede reasignar. | `let cancelled = false; cancelled = true;` |
| `type` | Define la "forma" que debe tener un dato. | `type Coords = { latitude: number; longitude: number };` |
| `: tipo` | Anotación que indica el tipo de una variable o parámetro. | `(id: string) => ...` |
| `string`, `number`, `boolean` | Tipos básicos: texto, número y verdadero/falso. | `let n: number = 5;` |
| `Tipo[]` | Un array de elementos de ese tipo. | `places: Place[]` |
| `campo?:` | Campo **opcional**: puede no existir (vale `undefined`). | `image?: string;` |
| `A \| B` (unión) | El valor puede ser de un tipo **o** de otro. | `string \| null` |
| `null` vs `undefined` | `null` es "vacío a propósito"; `undefined` es "no se ha asignado / no existe". | `useState<string \| null>(null)` |
| Tipos literales | El tipo es un valor exacto en lugar de un tipo general. | `status === "granted"` |
| `string` vs `String` | Siempre se usa `string` en minúscula; `String` (mayúscula) es el objeto envoltorio de JS y no debe usarse como tipo. | `(id: string) => ...` |
| `keyof Tipo` | Unión de los nombres de los campos de un tipo. | `keyof PlaceFormData` → `"name" \| "description" \| "image"` |
| `typeof x` (en tipos) | Obtiene el tipo de una variable existente. | `typeof Accelerometer.addListener` |
| `ReturnType<F>` | Tipo de lo que devuelve la función `F`. | `ReturnType<typeof Accelerometer.addListener>` |
| Genéricos `<T>` | Parámetro de tipo que concreta qué tipo maneja una función. | `useState<Place[]>([])` |
| `Promise<T>` | Un valor de tipo `T` que llegará en el futuro. | `async function f(): Promise<Place[]>` |

### Funciones

| Concepto | Explicación | Ejemplo |
|---|---|---|
| `function nombre() {}` | Declaración clásica de función. | `function savePlaces(places: Place[]) { ... }` |
| Función flecha `() =>` | Forma corta de escribir una función; se guarda en una variable o se pasa como argumento. | `const goBack = () => { router.back(); };` |
| Flecha con retorno implícito | Sin llaves, devuelve directamente la expresión. | `const doble = (n: number) => n * 2;` |
| `() => void` | Tipo de una función que no recibe nada y no devuelve nada. | `onPress: () => void;` |
| Callback | Función que se pasa a otra para que la llame más tarde. | `onPress={() => router.push("/add")}` |
| ¿Por qué `onPress={() => f(x)}` y no `onPress={f(x)}`? | `f(x)` se ejecutaría **al dibujar**; la flecha crea una función que solo se ejecuta **al pulsar**. | `onPress={() => saveFavorite(item)}` |
| ¿Por qué `onPress={getLocation}` sin paréntesis? | Se pasa la función en sí (sin llamarla) porque no necesita argumentos. | `onPress={getLocation}` |
| Parámetros por defecto | Valor que se usa si el argumento no se pasa o es `undefined`. | `function f(lat = 41.38) { ... }` |
| `return;` vacío | Sale de la función en ese punto (se usa para cortar si algo falla). | `if (name === "") { setError("..."); return; }` |
| `export` | Hace algo accesible desde otros archivos. | `export type Place = { ... };` |
| `export default` | Exportación principal del archivo; se importa sin llaves. | `import PlaceCard from "@/components/PlaceCard";` |
| `import { x }` | Importa una exportación con nombre (con llaves). | `import { usePlaces } from "@/context/PlacesContext";` |

### Asincronía

| Concepto | Explicación | Ejemplo |
|---|---|---|
| `async` | Marca una función que devuelve una promesa y permite usar `await` dentro. | `const getLocation = async () => { ... };` |
| `await` | Espera a que una promesa termine y devuelve su resultado. | `const response = await fetch(url);` |
| `try / catch / finally` | `try` intenta, `catch` captura el error y `finally` se ejecuta siempre (ideal para quitar el "cargando"). | `try { ... } catch { ... } finally { setLoading(false); }` |
| `catch` sin `(error)` | Si no necesitas el error, puedes omitir el paréntesis. | `catch { return []; }` |

### Objetos y arrays

| Concepto | Explicación | Ejemplo |
|---|---|---|
| Desestructuración de objeto | Saca campos de un objeto en variables. | `const { places, addPlace } = usePlaces();` |
| Desestructuración con renombre | Saca un campo y le pone otro nombre. | `const { loading: locating } = useLocation();` |
| Desestructuración de array | Saca elementos por posición (así funciona `useState`). | `const [error, setError] = useState(null);` |
| Desestructuración en parámetros | Saca las props directamente en la firma del componente. | `function PlaceCard({ place, onPress }: PlaceCardProps)` |
| Spread `...` en arrays | Copia los elementos de un array dentro de otro nuevo. | `[...places, place]` |
| Spread `...` en objetos | Copia los campos de un objeto y permite sobrescribir algunos. | `{ ...form, name: "Roma" }` |
| Clave computada `[key]` | Usa el valor de una variable como nombre del campo. | `{ ...form, [key]: value }` |
| Propiedad abreviada | Si la variable se llama igual que el campo, basta con escribirla una vez. | `{ name }` equivale a `{ name: name }` |
| Inmutabilidad | En React no se modifica el array/objeto del estado: se crea uno nuevo para que React detecte el cambio. | `setPlaces([...places, nuevo])` en vez de `places.push(nuevo)` |

### Operadores

| Concepto | Explicación | Ejemplo |
|---|---|---|
| `===` / `!==` | Igualdad/desigualdad estricta (compara también el tipo); se usa siempre en lugar de `==`. | `place.id === id` |
| `!` | Niega un valor (`!true` → `false`). | `if (!place) return;` |
| `&&` y `\|\|` | "Y" y "O" lógicos. | `if (lat !== undefined && lon !== undefined)` |
| Ternario `c ? a : b` | Un `if/else` en una sola expresión. | `saved ? "✓ Guardado" : "★ Guardar"` |
| `?.` (optional chaining) | Accede a un campo solo si lo anterior no es `null`/`undefined`; si lo es, devuelve `undefined` sin error. | `coords?.latitude` |
| `?.[0]` | Lo mismo pero para acceder a una posición de un array. | `item.coordinates?.[0]?.lat` |
| `??` (nullish coalescing) | Usa el valor de la derecha solo si el de la izquierda es `null` o `undefined`. | `item.extract ?? ""` |
| `**` | Potencia. | `x ** 2` |
| Template literals `` `${}` `` | Texto con variables dentro, entre comillas invertidas. | `` `Foto de ${place.name}` `` |

### React y JSX

| Concepto | Explicación | Ejemplo |
|---|---|---|
| Componente | Función que devuelve JSX (lo que se dibuja). | `export default function Index() { return <Text>Hola</Text>; }` |
| JSX | Sintaxis tipo HTML dentro de TypeScript (por eso los archivos son `.tsx`). | `<Text style={styles.title}>Hola</Text>` |
| `{ }` en JSX | Inserta una expresión de JavaScript dentro del JSX. | `<Text>{place.name}</Text>` |
| `{{ }}` doble llave | Las de fuera son "aquí va JS" y las de dentro son un objeto literal. | `source={{ uri: place.image }}` |
| Props | Datos que un componente padre pasa al hijo, como atributos. | `<PlaceCard place={item} onPress={...} />` |
| `children` | Prop especial con lo que se pone **entre** las etiquetas de un componente. | `<PlacesProvider><Stack /></PlacesProvider>` |
| Fragment `<> </>` | Agrupa varios elementos sin añadir un contenedor real. | `<><Text>A</Text><Text>B</Text></>` |
| Renderizado condicional `&&` | Dibuja el elemento solo si la condición es verdadera. | `{place.image && <Image ... />}` |
| Renderizado condicional `? : null` | Dibuja una cosa u otra; `null` significa "no dibujar nada" (más seguro que `&&` con números o textos vacíos). | `{error ? <Text>{error}</Text> : null}` |
| Return temprano | Un componente puede devolver distinto JSX según el estado. | `if (loading) return <ActivityIndicator />;` |
| Array de estilos | Combina varios estilos; los valores `false` se ignoran. | `style={[styles.button, pressed && styles.pressed]}` |
| Estilo como función | `Pressable` llama a la función con `{ pressed }` para cambiar el estilo al pulsar. | `style={({ pressed }) => [styles.card, pressed && styles.pressed]}` |
| `renderItem={({ item }) => ...}` | `FlatList` llama a esta función por cada elemento, pasándole un objeto del que se saca `item`. | `renderItem={({ item }) => <PlaceCard place={item} />}` |
| `keyExtractor` | Indica a la lista qué campo identifica cada elemento de forma única. | `keyExtractor={(place) => place.id}` |
| Componente controlado | El valor del `TextInput` sale del estado y cada cambio actualiza el estado. | `value={form.name} onChangeText={(t) => change("name", t)}` |
| Hook | Función que empieza por `use` y solo se puede llamar en el nivel superior de un componente u otro hook. | `const { coords } = useLocation();` |
| Hook propio | Una función `useAlgo` que agrupa estado y lógica para reutilizarla. | `export function useLocation() { const [c, setC] = useState(null); ... }` |
| Provider | Componente que pone un valor a disposición de todos sus hijos. | `<PlacesContext.Provider value={{ places }}>` |

### `useEffect` y el array de dependencias

| Forma | Cuándo se ejecuta | Ejemplo |
|---|---|---|
| `useEffect(fn, [])` | **Una sola vez**, al montar el componente (el `[]` vacío significa "no depende de nada"). | `useEffect(() => { loadPlaces().then(setPlaces); }, []);` |
| `useEffect(fn, [a, b])` | Al montar y cada vez que cambie `a` o `b`. | `useEffect(() => { load(coords); }, [isConnected, coords]);` |
| `useEffect(fn)` sin array | Después de **cada** renderizado (no se usa en el proyecto, suele ser un error). | — |
| `return () => {...}` dentro del efecto | Función de **limpieza**: se ejecuta al salir de la pantalla (o antes de repetir el efecto). | `return () => { subscription?.remove(); };` |
| Bandera `cancelled` | Evita actualizar el estado si la pantalla ya se cerró antes de que terminara una promesa. | `if (cancelled) return;` |

### Otros `[]` y `{}` que aparecen

| Forma | Significado | Ejemplo |
|---|---|---|
| `useState<Place[]>([])` | Estado inicial: un array vacío (todavía no hay lugares). | `const [places, setPlaces] = useState<Place[]>([]);` |
| `return [];` | Devuelve una lista vacía como valor seguro cuando algo falla. | `catch { return []; }` |
| `?? []` | Si la API no trae resultados, usa una lista vacía. | `data.query?.pages ?? []` |
| `[id].tsx` | En el **nombre de archivo**, los corchetes indican un parámetro de ruta de Expo Router (no es TypeScript). | `detail/[id].tsx` ↔ `/detail/42` |
| `useLocalSearchParams<{ id: string }>()` | El `{ id: string }` entre `< >` es un tipo genérico que dice qué parámetros esperas. | `const { id } = useLocalSearchParams<{ id: string }>();` |

---

## 5. Observaciones encontradas al revisar `src/`

- El `README.md` dice que **Descubre** usa JSONPlaceholder, pero `src/api/discover.ts` usa la API de **Wikipedia (geosearch)**.
- El `README.md` dice que la lista muestra la barra de conexión, pero `ConnectionStatus` no se usa en ninguna pantalla.
- En `PlacesContext.tsx`, `removePlace` usa `id: String` (mayúscula); lo correcto es `string`.
- `LocationButton.tsx` declara un `type Coords` propio que no se usa (ya existe en `useLocation`).
- `samplePlaces` (en `data/places.ts`) ya no se usa, y en `index.tsx` queda un `console.log` de depuración.

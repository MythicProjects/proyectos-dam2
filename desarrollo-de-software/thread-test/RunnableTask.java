/**
 * Forma 2: implementar la interfaz Runnable.
 *
 * TareaRunnable NO es un Thread: solo cumple el "contrato" de la interfaz
 * (implementar run()). Por sí sola no puede arrancar; hay que entregársela
 * a un objeto Thread, que es quien realmente crea el hilo.
 * Ventaja: la clase queda libre para extender otra (aquí, Animal).
 */
public class RunnableTask extends Animal implements Runnable {

    public RunnableTask(String especie) {
        super(especie);
    }

    @Override
    public void run() {
        // Como no heredamos de Thread, accedemos al hilo actual con Thread.currentThread()
        String nombreHilo = Thread.currentThread().getName();
        for (int i = 1; i <= 5; i++) {
            System.out.println("[implements Runnable] " + nombreHilo
                    + " (" + getEspecie() + ") -> paso " + i);
            try {
                Thread.sleep(300); // sleep es estático de Thread, no heredado
            } catch (InterruptedException e) {
                System.out.println(nombreHilo + " interrumpido");
                return;
            }
        }
        System.out.println("[implements Runnable] " + nombreHilo + " ha terminado");
    }
}

/** Clase padre cualquiera, para demostrar que con Runnable seguimos pudiendo heredar. */
class Animal {
    private final String especie;

    Animal(String especie) {
        this.especie = especie;
    }

    String getEspecie() {
        return especie;
    }
}

/**
 * Forma 1: extender la clase Thread.
 *
 * HiloExtendido ES un Thread (relación de herencia "es-un"),
 * así que hereda directamente start(), getName(), sleep(), join()...
 * Inconveniente: Java no tiene herencia múltiple, por lo que esta clase
 * ya no puede extender ninguna otra.
 */
public class ExtendedThread extends Thread {

    public ExtendedThread(String nombre) {
        super(nombre); // le damos nombre al hilo usando el constructor de Thread
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            // getName() y sleep() se usan directamente: son métodos heredados
            System.out.println("[extends Thread] " + getName() + " -> paso " + i);
            try {
                sleep(300);
            } catch (InterruptedException e) {
                System.out.println(getName() + " interrumpido");
                return;
            }
        }
        System.out.println("[extends Thread] " + getName() + " ha terminado");
    }
}

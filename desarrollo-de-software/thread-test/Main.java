/**
 * Experimento: comparar las dos formas clásicas de crear hilos en Java.
 *
 * Compilar y ejecutar:
 *   javac *.java && java Main
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hilo principal: " + Thread.currentThread().getName());
        System.out.println("=============================================");

        // 1) extends Thread: el objeto YA es un hilo, se arranca directamente
        ExtendedThread h1 = new ExtendedThread("Hilo-A");
        ExtendedThread h2 = new ExtendedThread("Hilo-B");

        // 2) implements Runnable: el objeto es solo la tarea; hace falta un Thread que la ejecute
        RunnableTask tarea = new RunnableTask("Gato");
        Thread h3 = new Thread(tarea, "Hilo-C");
        Thread h4 = new Thread(tarea, "Hilo-D"); // la MISMA tarea puede compartirse entre varios hilos

        // 3) Extra: como Runnable es una interfaz funcional, se puede usar una lambda
        Thread h5 = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("[lambda Runnable] " + Thread.currentThread().getName() + " -> paso " + i);
                try { Thread.sleep(400); } catch (InterruptedException e) { return; }
            }
        }, "Hilo-Lambda");

        // Comprobación de tipos: ¿quién "es" un Thread?
        System.out.println("h1 instanceof Thread?    " + (h1 instanceof Thread));
        System.out.println("tarea instanceof Thread? " + ((Object) tarea instanceof Thread));
        System.out.println("tarea instanceof Runnable? " + (tarea instanceof Runnable));
        System.out.println("tarea instanceof Animal?   " + (tarea instanceof Animal));
        System.out.println("=============================================");

        // OJO: start() crea un hilo nuevo; llamar a run() lo ejecutaría en el hilo main
        long inicio = System.currentTimeMillis();
        h1.start();
        h2.start();
        h3.start();
        h4.start();
        h5.start();

        // join(): el hilo main espera a que terminen todos antes de seguir
        h1.join();
        h2.join();
        h3.join();
        h4.join();
        h5.join();

        long total = System.currentTimeMillis() - inicio;
        System.out.println("=============================================");
        System.out.println("Todos los hilos han terminado en " + total + " ms");
        System.out.println("(En secuencia habrían tardado unos " + (4 * 5 * 300 + 3 * 400) + " ms)");

        // Demostración de run() vs start()
        System.out.println("=============================================");
        System.out.println("Llamando a run() directamente (¡sin hilo nuevo!):");
        new Thread(() -> System.out.println("  Ejecutado en: " + Thread.currentThread().getName()), "Hilo-X").run();
        System.out.println("Llamando a start():");
        Thread hx = new Thread(() -> System.out.println("  Ejecutado en: " + Thread.currentThread().getName()), "Hilo-X");
        hx.start();
        hx.join();
    }
}

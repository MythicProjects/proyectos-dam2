import java.time.Year;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String FILE_XML = "films.xml";
    private static final int MIN_YEAR = 1888; // first film ever recorded
    private static final int MAX_YEAR = Year.now().getValue() + 5;

    private static final Scanner sc = new Scanner(System.in);
    private static final FilmRepository repository = new FilmRepository(FILE_XML);

    public static void main(String[] args) {
        repository.load();

        int option;
        do {
            showMenu();
            option = readInt("Selecciona una opción: ", 0, 5, "Opción no válida. Introduce un número del 0 al 5.");
            switch (option) {
                case 1 -> showAllFilms();
                case 2 -> searchById();
                case 3 -> addFilm();
                case 4 -> updateFilm();
                case 5 -> deleteFilm();
                case 0 -> System.out.println("¡Hasta luego!");
            }
        } while (option != 0);

        sc.close();
    }

    // ----------------------------------------------------------------------
    // Console menu
    // ----------------------------------------------------------------------
    private static void showMenu() {
        System.out.println();
        System.out.println("========== CATÁLOGO DE PELÍCULAS ==========");
        System.out.println("1. Mostrar todas las películas");
        System.out.println("2. Buscar una película por ID");
        System.out.println("3. Añadir una película");
        System.out.println("4. Modificar una película por ID");
        System.out.println("5. Eliminar una película por ID");
        System.out.println("0. Salir");
        System.out.println("==========================================");
    }

    // ----------------------------------------------------------------------
    // Menu options
    // ----------------------------------------------------------------------
    // ------------------------ Show all films
    private static void showAllFilms() {
        List<Film> films = repository.findAll();
        if (films.isEmpty()) {
            System.out.println("El catálogo está vacío.");
            return;
        }
        for (Film film : films) {
            System.out.println(film);
        }
        System.out.println("Total: " + films.size() + " películas.");
    }

    // ------------------------ Search by id
    private static void searchById() {
        int id = 0;
        System.out.print("ID de la película: ");
        // Repeat until the user types a positive whole number
        while (id < 1) {
            // Input ended: exit instead of crashing
            if (!sc.hasNext()) {
                System.out.println();
                System.out.println("No hay más entrada. Saliendo.");
                System.exit(0);
            }
            if (sc.hasNextInt()) {
                id = sc.nextInt();
            }
            // Clear the rest of the line (also the line break left by nextInt)
            sc.nextLine();
            if (id < 1) {
                System.out.print("ID no válido. Introduce un número entero positivo: ");
            }
        }
        Film film = repository.findById(id);
        if (film == null) {
            System.out.println("No existe ninguna película con ID " + id + ".");
        } else {
            System.out.println(film);
        }
    }

    // ------------------------ Add film
    private static void addFilm() {
        if (!canWrite()) {
            return;
        }

        String title = readRequired("Título: ");
        String director = readRequired("Director: ");
        int year = readYear("Año (" + MIN_YEAR + "-" + MAX_YEAR + "): ", -1);
        String genre = readRequired("Género: ");

        Film film = new Film(repository.nextId(), title, director, year, genre);
        if (repository.add(film)) {
            System.out.println("Película añadida y guardada en " + FILE_XML + ":");
            System.out.println(film);
        }
    }

    // ------------------------ Modify film
    private static void updateFilm() {
        if (!canWrite()) {
            return;
        }

        int id = readId();
        Film film = repository.findById(id);
        if (film == null) {
            System.out.println("No existe ninguna película con ID " + id + ". No se ha modificado el fichero.");
            return;
        }

        System.out.println("Datos actuales:");
        System.out.println(film);
        System.out.println("Introduce los nuevos datos (Enter para mantener el valor actual):");

        String title = readOptional("Título [" + film.getTitle() + "]: ", film.getTitle());
        String director = readOptional("Director [" + film.getDirector() + "]: ", film.getDirector());
        int year = readYear("Año [" + film.getYear() + "]: ", film.getYear());
        String genre = readOptional("Género [" + film.getGenre() + "]: ", film.getGenre());

        boolean changed = !title.equals(film.getTitle())
                || !director.equals(film.getDirector())
                || year != film.getYear()
                || !genre.equals(film.getGenre());
        if (!changed) {
            System.out.println("No hay cambios. No se ha modificado el fichero.");
            return;
        }

        film.setTitle(title);
        film.setDirector(director);
        film.setYear(year);
        film.setGenre(genre);
        if (repository.update(film)) {
            System.out.println("Película modificada y guardada en " + FILE_XML + ":");
            System.out.println(film);
        }
    }

    // ------------------------ Delete film
    private static void deleteFilm() {
        if (!canWrite()) {
            return;
        }

        int id = readId();
        Film film = repository.findById(id);
        if (film == null) {
            System.out.println("No existe ninguna película con ID " + id + ". No se ha modificado el fichero.");
            return;
        }

        System.out.println(film);
        if (!readYesNo("¿Seguro que quieres eliminarla? (s/n): ")) {
            System.out.println("Eliminación cancelada. No se ha modificado el fichero.");
            return;
        }

        if (repository.delete(id)) {
            System.out.println("Película con ID " + id + " eliminada (borrado físico: se ha quitado de "
                    + FILE_XML + ").");
        }
    }

    // ----------------------------------------------------------------------
    // Tools
    // ----------------------------------------------------------------------
    // ------------------------ Write check
    // Write operations are not allowed if the XML file could not be loaded
    private static boolean canWrite() {
        if (repository.isWriteBlocked()) {
            System.out.println("Operación no disponible: " + FILE_XML + " está vacío o mal formado."
                    + " Revisa el fichero y vuelve a iniciar el programa.");
            return false;
        }
        return true;
    }

    // ------------------------ Read line
    private static String readLine(String prompt) {
        System.out.print(prompt);
        // Input ended (e.g. piped input without a final 0): exit instead of crashing
        if (!sc.hasNextLine()) {
            System.out.println();
            System.out.println("No hay más entrada. Saliendo.");
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    // ------------------------ Read number
    private static int readInt(String prompt, int min, int max, String error) {
        while (true) {
            String line = readLine(prompt);
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Not a number: the error message below is shown
            }
            System.out.println(error);
        }
    }

    // ------------------------ Read id
    private static int readId() {
        return readInt("ID de la película: ", 1, Integer.MAX_VALUE, "ID no válido. Introduce un número entero positivo.");
    }

    // ------------------------ Required text
    private static String readRequired(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Este campo no puede estar vacío.");
        }
    }

    // ------------------------ Optional text
    private static String readOptional(String prompt, String current) {
        String line = readLine(prompt);
        if (line.isEmpty()) {
            return current;
        }
        return line;
    }

    // ------------------------ Read year
    private static int readYear(String prompt, int current) {
        while (true) {
            String line = readLine(prompt);
            if (line.isEmpty()) {
                if (current != -1) {
                    return current;
                }
                System.out.println("El año no puede estar vacío.");
                continue;
            }
            try {
                int year = Integer.parseInt(line);
                if (year >= MIN_YEAR && year <= MAX_YEAR) {
                    return year;
                }
            } catch (NumberFormatException e) {
                // Not a number: the error message below is shown
            }
            System.out.println("Año no válido. Introduce un número entre " + MIN_YEAR + " y " + MAX_YEAR + ".");
        }
    }

    // ------------------------ Yes or no
    private static boolean readYesNo(String prompt) {
        while (true) {
            String answer = readLine(prompt).toLowerCase();
            if (answer.equals("s") || answer.equals("si") || answer.equals("sí")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("Responde 's' o 'n'.");
        }
    }
}

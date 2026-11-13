import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.List;
import java.util.Scanner;

import org.jdom2.JDOMException;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Main {

    private static final String FILE_XML = "films.xml";
    private static final String FILE_JSON = "peliculas.json";
    private static final int MIN_YEAR = 1888;
    private static final int MAX_YEAR = Year.now().getValue() + 5;

    private static final Scanner sc = new Scanner(System.in);
    private static final JsonRepository repository = new JsonRepository(FILE_JSON);
    // null while the JSON has not been loaded
    private static Catalogo catalogo;

    public static void main(String[] args) {
        loadJson();

        int option;
        do {
            showMenu();
            option = readInt("Selecciona una opción: ");
            switch (option) {
                case 1 -> convertXmlToJson();
                case 2 -> showAll();
                case 3 -> searchById();
                case 4 -> addPelicula();
                case 5 -> updatePelicula();
                case 6 -> deletePelicula();
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción no válida.");
            }
        } while (option != 0);
        sc.close();
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("========== CATÁLOGO DE PELÍCULAS ==========");
        System.out.println("1. Convertir XML a JSON");
        System.out.println("2. Mostrar todas las películas");
        System.out.println("3. Buscar una película por ID");
        System.out.println("4. Añadir una película");
        System.out.println("5. Modificar una película por ID");
        System.out.println("6. Eliminar una película por ID");
        System.out.println("0. Salir");
        System.out.println("==========================================");
    }

    // ------------------------ Load JSON
    private static boolean loadJson() {
        if (!repository.exists()) {
            System.out.println("No existe " + FILE_JSON + ". Usa primero la opción 1 (Convertir XML a JSON).");
            catalogo = null;
            return false;
        }
        try {
            catalogo = repository.load();
            System.out.println("Catálogo cargado: " + catalogo.getPeliculas().size() + " películas.");
            return true;
        } catch (JsonProcessingException e) {
            // Must go before IOException: it is a subclass of it
            System.out.println("Error: " + FILE_JSON + " no tiene un formato JSON válido (" + e.getOriginalMessage() + ").");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error al leer " + FILE_JSON + " (" + e.getMessage() + ").");
            e.printStackTrace();
        }
        catalogo = null;
        return false;
    }

    // ------------------------ Save JSON
    private static boolean saveJson() {
        try {
            repository.save(catalogo);
            return true;
        } catch (IOException e) {
            System.out.println("Error al guardar " + FILE_JSON + " (" + e.getMessage() + ").");
            e.printStackTrace();
            // Discard the change in memory so it matches the file again
            System.out.println("El cambio se ha descartado.");
            loadJson();
            return false;
        }
    }

    // Options 2-6 need the JSON loaded
    private static boolean catalogReady() {
        if (catalogo == null) {
            System.out.println("No hay catálogo JSON cargado. Usa primero la opción 1 (Convertir XML a JSON).");
            return false;
        }
        return true;
    }

    // ------------------------ 1. XML -> JSON
    private static void convertXmlToJson() {
        File xml = new File(FILE_XML);
        if (!xml.exists()) {
            System.out.println("Error: no existe " + FILE_XML + ". El programa se detiene.");
            System.exit(1);
        }
        if (repository.exists() && !readYesNo(FILE_JSON + " ya existe. ¿Sobrescribirlo con los datos del XML? (s/n): ")) {
            System.out.println("Conversión cancelada.");
            return;
        }
        try {
            List<Pelicula> peliculas = XmlReader.read(xml);
            repository.save(new Catalogo(peliculas));
            System.out.println("Migradas " + peliculas.size() + " películas a " + FILE_JSON + ".");
            loadJson();
        } catch (JDOMException e) {
            System.out.println("Error: " + FILE_XML + " está vacío o mal formado (" + e.getMessage() + ").");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error de lectura/escritura (" + e.getMessage() + ").");
            e.printStackTrace();
        }
    }

    // ------------------------ 2. Show all
    private static void showAll() {
        if (!catalogReady()) return;
        if (catalogo.getPeliculas().isEmpty()) {
            System.out.println("El catálogo está vacío.");
            return;
        }
        for (Pelicula p : catalogo.getPeliculas()) {
            System.out.println(p);
        }
        System.out.println("Total: " + catalogo.getPeliculas().size() + " películas.");
    }

    // ------------------------ 3. Search
    private static void searchById() {
        if (!catalogReady()) return;
        Pelicula p = findById(readInt("ID de la película: "));
        System.out.println(p == null ? "No existe ninguna película con ese ID." : p);
    }

    // ------------------------ 4. Add
    // Invalid data: the question is repeated until the value is valid
    private static void addPelicula() {
        if (!catalogReady()) return;
        String titulo = readRequired("Título: ");
        String director = readRequired("Director: ");
        int año = readYear("Año (" + MIN_YEAR + "-" + MAX_YEAR + "): ", -1);
        String genero = readRequired("Género: ");

        Pelicula p = new Pelicula(nextId(), titulo, director, año, genero);
        catalogo.getPeliculas().add(p);
        if (saveJson()) {
            System.out.println("Película añadida con ID " + p.getId() + ".");
        }
    }

    // ------------------------ 5. Update
    private static void updatePelicula() {
        if (!catalogReady()) return;
        Pelicula p = findById(readInt("ID de la película: "));
        if (p == null) {
            System.out.println("No existe ninguna película con ese ID. No se ha modificado el fichero.");
            return;
        }
        System.out.println("Datos actuales: " + p);
        System.out.println("Introduce los nuevos datos (Enter para mantener el valor actual):");
        p.setTitulo(readOptional("Título [" + p.getTitulo() + "]: ", p.getTitulo()));
        p.setDirector(readOptional("Director [" + p.getDirector() + "]: ", p.getDirector()));
        p.setAño(readYear("Año [" + p.getAño() + "]: ", p.getAño()));
        p.setGenero(readOptional("Género [" + p.getGenero() + "]: ", p.getGenero()));

        if (saveJson()) {
            System.out.println("Película modificada: " + p);
        }
    }

    // ------------------------ 6. Delete
    // Physical delete: the film is removed from the "peliculas" array
    private static void deletePelicula() {
        if (!catalogReady()) return;
        Pelicula p = findById(readInt("ID de la película: "));
        if (p == null) {
            System.out.println("No existe ninguna película con ese ID. No se ha modificado el fichero.");
            return;
        }
        System.out.println("Se eliminará: " + p);
        if (!readYesNo("¿Confirmas? (s/n): ")) {
            System.out.println("Eliminación cancelada.");
            return;
        }
        catalogo.getPeliculas().remove(p);
        if (saveJson()) {
            System.out.println("Película eliminada (borrado físico).");
        }
    }

    // ------------------------ Catalog helpers
    private static Pelicula findById(int id) {
        for (Pelicula p : catalogo.getPeliculas()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    // Max id + 1, or 1 if the catalog is empty
    private static int nextId() {
        int max = 0;
        for (Pelicula p : catalogo.getPeliculas()) {
            if (p.getId() > max) max = p.getId();
        }
        return max + 1;
    }

    // ------------------------ Input helpers
    private static String readLine(String prompt) {
        System.out.print(prompt);
        // Input ended (e.g. piped input): exit instead of crashing
        if (!sc.hasNextLine()) {
            System.out.println("\nNo hay más entrada. Saliendo.");
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    // Returns -1 if the text is not a number (never a valid option or id)
    private static int readInt(String prompt) {
        try {
            return Integer.parseInt(readLine(prompt));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String readRequired(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) return line;
            System.out.println("Este campo no puede estar vacío.");
        }
    }

    private static String readOptional(String prompt, String current) {
        String line = readLine(prompt);
        return line.isEmpty() ? current : line;
    }

    // current = -1 means the year is required
    private static int readYear(String prompt, int current) {
        while (true) {
            String line = readLine(prompt);
            if (line.isEmpty() && current != -1) return current;
            try {
                int year = Integer.parseInt(line);
                if (year >= MIN_YEAR && year <= MAX_YEAR) return year;
            } catch (NumberFormatException e) {
                // Falls through to the error message
            }
            System.out.println("Año no válido. Introduce un número entre " + MIN_YEAR + " y " + MAX_YEAR + ".");
        }
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            String answer = readLine(prompt).toLowerCase();
            if (answer.equals("s") || answer.equals("si") || answer.equals("sí")) return true;
            if (answer.equals("n") || answer.equals("no")) return false;
            System.out.println("Responde 's' o 'n'.");
        }
    }
}
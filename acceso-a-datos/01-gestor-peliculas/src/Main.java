import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class Main {

    private static final String FILE_TXT = "./data/films.txt";
    private static final String FILE_XML = "./films.xml";

    public static void main(String[] args){
        List<Film> filmsList = readFilmList(FILE_TXT);
        Scanner sc = new Scanner(System.in);

        int option;
        do{
            System.out.println("========= CATALOGO =========");
            System.out.println("1 - Show all films");
            System.out.println("2 - Search by gender");
            System.out.println("3 - Search by title");
            System.out.println("4 - Export XML");
            System.out.println("5 - Import XML");
            System.out.println("Option: ");
            option = readInt(sc);

            switch (option) {
                case 1 -> showAllFilms(filmsList);
                case 2 -> searchByGender(filmsList, sc);
                case 3 -> searchByTitle(filmsList, sc);
                case 4 -> exportarXML(filmsList, FILE_XML);
                case 5 -> {
                    List<Film> imported = importXML(FILE_XML);
                    showFullInfo(imported);
                    }
                case 0 -> System.out.println("Bye!");
                default -> System.out.println("Not valid option");
        }
        }
        while(option != 0);
        sc.close();
    }
    // ----------------------------------- Part 2
    private static List<Film> readFilmList(String fileName){
        List<Film> list = new ArrayList<>();
        try (BufferedReader br = 
            new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName), StandardCharsets.UTF_8))
        ){
            String line;
            while ((line = br.readLine()) != null) {
                if(line.isBlank()) continue;

                String[] c = line.split("\\|");
                if(c.length != 5) {
                    System.err.println("Incorrect format: " + line);
                    continue;
                }

                list.add(new Film(
                    Integer.parseInt(c[0].trim()), 
                    c[1].trim(), 
                    c[2].trim(), 
                    Integer.parseInt(c[3].trim()), 
                    c[4].trim()));
            }
        } catch (IOException e){
            System.err.println("Error: cannot open file " + fileName);
        }

        return list;
    }

    // ----------------------------------- Part 3
    private static int readInt(Scanner sc){
        try{
            return  Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e){
            return -1; // user
        }
    }

    private static void showAllFilms(List<Film> film){
        for(Film f : film) System.out.println("|| ---- " + f.getTitle());
    }

    private static void searchByGender(List<Film>films, Scanner sc){
        String text = sc.nextLine().trim().toLowerCase();

        boolean finded = false;
        for(Film f : films){
            if(f.getGender().trim().equalsIgnoreCase(text)){
                System.out.println("|| ---- " + f.getGender() + " - " + f.getTitle());

                finded = true;
            }
        }

        if(!finded) System.out.println("There is any film of that gender");
    }

    private static void searchByTitle(List<Film>films, Scanner sc){
        String text = sc.nextLine().trim().toLowerCase();

        boolean finded = false;
        for(Film f : films){
            if(f.getTitle().toLowerCase().trim().contains(text)){
                System.out.println("|| ---- " 
                    + f.getId() + " - " 
                    + f.getTitle() +  " - " 
                    + f.getDirector() + " - "
                    + f.getYear() + " - "
                    + f.getGender());

                finded = true;
            }
        }

        if(!finded) System.out.println("Film not finded");
    }

    // ----------------------------------- Part 4
    private  static void exportarXML(List<Film> films, String nombreFichero) {
        try {
            // Crea documento
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            // Crea primera etiqueta (no se añade)
            Element root = doc.createElement("films");
            doc.appendChild(root); // La añade al documento

            // Por cada película -> añade sus datos
            for (Film f : films) {
                Element film = doc.createElement("film");
                film.setAttribute("id", String.valueOf(f.getId()));
                addInfo(doc, film, "title", f.getTitle());
                addInfo(doc, film, "director", f.getDirector());
                addInfo(doc, film, "year", String.valueOf(f.getYear()));
                addInfo(doc, film, "gender", f.getGender());
                root.appendChild(film); // añade todo a la raíz
            }

            // Formato para el documento
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            t.transform(new DOMSource(doc), new StreamResult(new File(nombreFichero)));

            System.out.println("Exported at " + nombreFichero);
        } catch (Exception e) {
            System.err.println("Error al exportar XML: " + e.getMessage());
        }
    }

    private static void addInfo(Document doc, Element parent, String name, String value) {
            Element e = doc.createElement(name);
            e.setTextContent(value);
            parent.appendChild(e);
    }

    // ----------------------------------- Part 5
    private static List<Film> importXML(String fileName) {
    List<Film> films = new ArrayList<Film>();

    try {
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new File(fileName));

        doc.getDocumentElement().normalize();
        NodeList nodes = doc.getElementsByTagName("film");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);

            int id = Integer.parseInt(e.getAttribute("id"));
            String title = getInfo(e, "title");
            String director = getInfo(e, "director");
            int year = Integer.parseInt(getInfo(e, "year"));
            String gender = getInfo(e, "gender");

            films.add(new Film(id, title, director, year, gender));
        }
        System.out.println("Imported " + films.size() + " films from " + fileName);
        } catch (Exception e) {
            System.err.println("Error al importar XML: " + e.getMessage());
        }

        return films;
    }

    // get for "importXML" function
    private static String getInfo(Element parent, String name) {
    NodeList nl = parent.getElementsByTagName(name);
    return nl.getLength() > 0 ? nl.item(0).getTextContent().trim() : "";
    }

    // show in main
    private static void showFullInfo(List<Film> films) {
    if (films.isEmpty()) {
        System.out.println("No films to show");
        return;
    }
    for (Film f : films) {
        System.out.println("|| ---- " + f.getId() + " - " + f.getTitle() + " - "
            + f.getDirector() + " - " + f.getYear() + " - " + f.getGender());
    }
    }
}
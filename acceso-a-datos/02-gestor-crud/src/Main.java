import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Main {

    private static final String FILE_XML = "./films.xml";

    public static void main(String[] args){
        List<Film> filmsList = importXML(FILE_XML);
        Scanner sc = new Scanner(System.in);

        int option;
        do{
            System.out.println("========= CATALOGO =========");
            System.out.println("1 - Show all films");
            System.out.println("2 - Search film by ID");
            System.out.println("3 - Add new film");
            System.out.println("4 - Edit film by ID");
            System.out.println("5 - Delete film by ID");
            System.out.println("Option: ");
            option = readInt(sc);

            switch (option) {
                case 1 -> showFullInfo(filmsList); //showAllFilms(filmsList);
                case 2 -> searchByID(filmsList, sc);
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
    // ----------------------------------- Ex 01 Part 3
    private static int readInt(Scanner sc){
        try{
            return  Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e){
            return -1; // user
        }
    }

    private static void searchByID(List<Film>films, Scanner sc){
        String id = sc.nextLine().trim().toLowerCase();

        boolean finded = false;
        for(Film f : films){
            if(f.getId() == Integer.parseInt(id)){
                System.out.println("|| ---- " + f.getId() + " - " + f.getTitle());

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

    // ----------------------------------- Ex 01 Part 4
    private  static void exportarXML(List<Film> films, String fileName) {
        if (films.isEmpty()) {
            System.err.println("Nothing to export, file not overwritten");
            return;
        }

        Element root = new Element("films");

        for (Film f : films){
            Element film =  new Element("film");

            film.setAttribute("id", String.valueOf(f.getId()));
            film.addContent(new Element("title").setText(f.getTitle()));
            film.addContent(new Element("director").setText(f.getDirector()));
            film.addContent(new Element("year").setText(String.valueOf(f.getYear())));
            film.addContent(new Element("gender").setText(f.getGender()));  
            root.addContent(film);
        }


        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        try (Writer w = Files.newBufferedWriter(Path.of(fileName), StandardCharsets.UTF_8)){
            outputter.output(new Document(root), w);
            System.out.println("Exported at " + fileName);
        }catch(IOException e){
            System.err.println("Cannot write " + fileName + ": " + e.getMessage());
        }
    }

    // ----------------------------------- Ex 02 Part 1
    private static List<Film> importXML(String fileName) {
        List<Film> films = new ArrayList<Film>();

        SAXBuilder builder = new SAXBuilder();
        // Reject any DOCTYPE to prevent XXE (external entity) attacks
        builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        try {
            Document doc = builder.build(new File(fileName));
            Element root = doc.getRootElement();

            for(Element e : root.getChildren("film")){
                try{
                    int id = Integer.parseInt(e.getAttributeValue("id"));
                    String title = e.getChildTextTrim("title");
                    String director = e.getChildTextTrim("director");
                    int year = Integer.parseInt(e.getChildTextTrim("year"));
                    String gender = e.getChildTextTrim("gender");


                    films.add(new Film(id, title, director, year, gender));
                }catch(NumberFormatException ex){
                    System.err.println("Skipping invalid film: " + ex.getMessage());
                }
            }
            System.out.println("Imported " + films.size() + " films from " + fileName);
        } catch (JDOMException e) {
            System.err.println("Invalid XML in " + fileName + ": " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Cannot read " + fileName + ": " + e.getMessage());
        }

        return films;
    }


    private static void showAllFilms(List<Film> film){
        for(Film f : film) System.out.println("|| ---- " + f.getTitle());
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
    // ----------------------------------- Ex 02 Part 4

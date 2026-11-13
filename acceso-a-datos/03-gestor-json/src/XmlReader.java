import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

// Reads the XML catalog with JDOM2 (only used for the migration)
public class XmlReader {

    public static List<Pelicula> read(File file) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        // Reject any DOCTYPE to prevent XXE attacks
        builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document document = builder.build(file);

        List<Pelicula> peliculas = new ArrayList<>();
        for (Element film : document.getRootElement().getChildren("film")) {
            try {
                int id = Integer.parseInt(film.getAttributeValue("id").trim());
                int year = Integer.parseInt(film.getChildTextTrim("year"));
                peliculas.add(new Pelicula(id, film.getChildTextTrim("title"),
                        film.getChildTextTrim("director"), year, film.getChildTextTrim("genre")));
            } catch (NumberFormatException | NullPointerException e) {
                // Missing or non-numeric id/year: skip only this film
                System.out.println("Aviso: se ignora una película con id o año no válidos.");
            }
        }
        return peliculas;
    }
}
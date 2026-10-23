import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class FilmRepository {

    private File file;
    private Document document;
    // True when the XML file exists but could not be read: writing would destroy its data
    private boolean writeBlocked;

    public FilmRepository(String fileName) {
        file = new File(fileName);
        document = new Document(new Element("films"));
    }

    // ----------------------------------------------------------------------
    // Load
    // ----------------------------------------------------------------------
    public void load() {
        document = new Document(new Element("films"));
        writeBlocked = false;

        if (!file.exists()) {
            System.out.println("Aviso: no existe " + file.getName()
                    + ". Se empieza con un catálogo vacío; el fichero se creará al guardar.");
            return;
        }

        SAXBuilder builder = new SAXBuilder();
        builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        try (FileInputStream in = new FileInputStream(file)) {
            Document loaded = builder.build(in);

            if (!loaded.getRootElement().getName().equals("films")) {
                writeBlocked = true;
                System.out.println("Aviso: la raíz de " + file.getName() + " no es <films>."
                        + " Revisa el fichero; no se permitirá guardar cambios.");
                return;
            }

            document = loaded;
            System.out.println("Catálogo cargado: " + findAll().size() + " películas desde " + file.getName() + ".");
        } catch (FileNotFoundException e) {
            writeBlocked = true;
            System.out.println("Aviso: no se pudo abrir " + file.getName() + " (" + e.getMessage() + ")."
                    + " Revisa el fichero; no se permitirá guardar cambios.");
            e.printStackTrace();
        } catch (JDOMException e) {
            writeBlocked = true;
            System.out.println("Aviso: " + file.getName() + " está vacío o mal formado (" + e.getMessage() + ")."
                    + " Revisa el fichero; no se permitirá guardar cambios.");
            e.printStackTrace();
        } catch (IOException e) {
            writeBlocked = true;
            System.out.println("Aviso: error al leer " + file.getName() + " (" + e.getMessage() + ")."
                    + " Revisa el fichero; no se permitirá guardar cambios.");
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------------------
    // Find films
    // ----------------------------------------------------------------------
    // ------------------------ Find all
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        for (Element element : document.getRootElement().getChildren("film")) {
            Film film = toFilm(element);
            if (film != null) {
                films.add(film);
            }
        }
        return films;
    }

    // ------------------------ Find by id
    public Film findById(int id) {
        Element element = findElement(id);
        if (element == null) {
            return null;
        }
        return toFilm(element);
    }

    // ------------------------ Next id
    public int nextId() {
        int max = 0;
        for (Element element : document.getRootElement().getChildren("film")) {
            try {
                int id = Integer.parseInt(element.getAttributeValue("id", "").trim());
                if (id > max) {
                    max = id;
                }
            } catch (NumberFormatException e) {
                // A film without a valid id does not count for the numbering
            }
        }
        return max + 1;
    }

    // ----------------------------------------------------------------------
    // Changes
    // ----------------------------------------------------------------------
    // ------------------------ Add
    public boolean add(Film film) {
        Element element = new Element("film");
        element.setAttribute("id", String.valueOf(film.getId()));
        element.addContent(new Element("title").setText(film.getTitle()));
        element.addContent(new Element("director").setText(film.getDirector()));
        element.addContent(new Element("year").setText(String.valueOf(film.getYear())));
        element.addContent(new Element("genre").setText(film.getGenre()));

        document.getRootElement().addContent(element);
        return save();
    }

    // ------------------------ Update
    public boolean update(Film film) {
        Element element = findElement(film.getId());
        if (element == null) {
            return false;
        }

        setChildText(element, "title", film.getTitle());
        setChildText(element, "director", film.getDirector());
        setChildText(element, "year", String.valueOf(film.getYear()));
        setChildText(element, "genre", film.getGenre());
        return save();
    }

    // ------------------------ Delete
    public boolean delete(int id) {
        Element element = findElement(id);
        if (element == null) {
            return false;
        }

        document.getRootElement().removeContent(element);
        return save();
    }

    // ------------------------ Save
    public boolean save() {
        if (writeBlocked) {
            System.out.println("No se puede guardar: " + file.getName() + " no se pudo cargar. Revisa el fichero.");
            return false;
        }

        // Pretty format: indentation and line breaks
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

        // Write in UTF-8 so the accents match the encoding="UTF-8" header
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            outputter.output(document, writer);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error: no se puede escribir en " + file.getName() + " (" + e.getMessage() + ").");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error al guardar " + file.getName() + " (" + e.getMessage() + ").");
            e.printStackTrace();
        }

        // Saving failed: reload from disk to discard the change made in memory
        System.out.println("El cambio se ha descartado. Se recarga el catálogo desde el fichero.");
        load();
        return false;
    }

    // ----------------------------------------------------------------------
    // Tools
    // ----------------------------------------------------------------------
    // ------------------------ Write state
    public boolean isWriteBlocked() {
        return writeBlocked;
    }

    // ------------------------ XML elements
    private Element findElement(int id) {
        String wanted = String.valueOf(id);
        for (Element element : document.getRootElement().getChildren("film")) {
            String elementId = element.getAttributeValue("id");
            if (elementId != null && elementId.trim().equals(wanted)) {
                return element;
            }
        }
        return null;
    }

    // ------------------------ To film
    private Film toFilm(Element element) {
        try {
            int id = Integer.parseInt(element.getAttributeValue("id", "").trim());
            int year = Integer.parseInt(childText(element, "year"));
            return new Film(id, childText(element, "title"), childText(element, "director"),
                    year, childText(element, "genre"));
        } catch (NumberFormatException e) {
            System.out.println("Aviso: se ignora una película con id o año no válidos (id=\""
                    + element.getAttributeValue("id") + "\").");
            return null;
        }
    }

    // ------------------------ Child text
    // Returns "" if the child does not exist
    private String childText(Element parent, String name) {
        String text = parent.getChildTextTrim(name);
        if (text == null) {
            return "";
        }
        return text;
    }

    // ------------------------ Set child text
    // Creates the child if it does not exist
    private void setChildText(Element parent, String name, String value) {
        Element child = parent.getChild(name);
        if (child == null) {
            child = new Element(name);
            parent.addContent(child);
        }
        child.setText(value);
    }
}

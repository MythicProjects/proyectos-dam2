// POJO: field names must match the JSON keys, because annotations are not allowed
public class Pelicula {

    private int id;
    private String titulo;
    private String director;
    private int año;
    private String genero;

    // Empty constructor: Jackson needs it to create the object before calling the setters
    public Pelicula() {
    }

    public Pelicula(int id, String titulo, String director, int año, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.año = año;
        this.genero = genero;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public int getAño() { return año; }
    public void setAño(int año) { this.año = año; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    @Override
    public String toString() {
        return "[" + id + "] " + titulo + " | Director: " + director
                + " | Año: " + año + " | Género: " + genero;
    }
}
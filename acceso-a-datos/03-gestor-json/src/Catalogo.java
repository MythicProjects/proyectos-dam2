import java.util.ArrayList;
import java.util.List;

// Root object of the JSON: { "peliculas": [ ... ] }
public class Catalogo {

    private List<Pelicula> peliculas = new ArrayList<>();

    public Catalogo() {
    }

    public Catalogo(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    public List<Pelicula> getPeliculas() { return peliculas; }
    public void setPeliculas(List<Pelicula> peliculas) { this.peliculas = peliculas; }
}
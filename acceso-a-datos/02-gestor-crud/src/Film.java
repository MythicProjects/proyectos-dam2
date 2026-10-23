public class Film {

    private int id;
    private String title;
    private String director;
    private int year;
    private String genre;

    public Film(int id, String title, String director, int year, String genre) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
        this.genre = genre;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }

    public void setDirector(String director) { this.director = director; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }

    // ------------------------ To string
    // One-line format used everywhere a film is printed
    @Override
    public String toString() {
        return "[" + id + "] " + title + " | Director: " + director
                + " | Año: " + year + " | Género: " + genre;
    }
}

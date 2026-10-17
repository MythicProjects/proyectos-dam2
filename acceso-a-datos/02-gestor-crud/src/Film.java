public class Film {
    private int id;
    private String title;
    private String director;
    private int year;
    private String gender;

    public Film(int id, String title, String director, int year, String gender){
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
        this.gender = gender;        
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDirector() {
        return director;
    }
    public int getYear() {
        return year;
    }
    public String getGender() {
        return gender;
    }
}
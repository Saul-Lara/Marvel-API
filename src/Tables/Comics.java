package Tables;

public class Comics {
    private String title;
    private String pathImage;
    private int comicId;
    private String description;

    public Comics(String title, String path, int comicId) { // Basic Information of Comic
        this.title = title;
        this.pathImage = path;
        this.comicId = comicId;
    }

    public Comics(String title, String path, String description) {    // Detalled information of Comic
        this.title = title;
        this.pathImage = path;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getPathImage() {
        return pathImage;
    }

    public int getComicId() {
        return comicId;
    }

    public String getDescription() {
        return description;
    }
}

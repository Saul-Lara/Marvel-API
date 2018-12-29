package Tables;

public class Comics {
    private String title;
    private String pathImage;
    private int comicId;

    public Comics(String title, String path, int comicId) {
        this.title = title;
        this.pathImage = path;
        this.comicId = comicId;
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
}

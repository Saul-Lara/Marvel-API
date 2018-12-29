package Tables;

public class Comics {
    private String title;
    private String pathImage;

    public Comics(String title, String path){
        this.title = title;
        this.pathImage = path;
    }

    public String getTitle() {
        return title;
    }

    public String getPathImage() {
        return pathImage;
    }
}

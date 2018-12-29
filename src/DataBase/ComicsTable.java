package DataBase;

public class ComicsTable {
    public String comics(String Title){
        String URL;

        URL = "/v1/public/comics?format=comic&formatType=comic&noVariants=false&title=" + Title + "&limit=5&";

        return URL;
    }

    public String comicId(int comicId) {
        String URL;

        URL = "/v1/public/comics/" + comicId + "?";

        return URL;
    }
}

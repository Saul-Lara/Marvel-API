package sample;

import Tables.Comics;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class comicInfoController {

    @FXML
    private Hyperlink linkMarvel;
    @FXML
    private ImageView comicImage;
    @FXML
    private Label comicTitle;
    @FXML
    private Label comicDescription;

    public String comicURL;
    public double ELEMENT_SIZE_WIDTH = 168;
    public double ELEMENT_SIZE_HEIGHT = 252;

    public JSONObject marvelJson;
    public JSONObject dataObject;
    public JSONArray resultsArray;
    private List<Comics> comics = new ArrayList<>();

    public void initData(String URL) throws Exception {
        comicURL = URL;
        callServer(comicURL);
        linkMarvel.setText(marvelJson.getString("attributionText"));
        createComponents();
    }

    public void onExitButtonClicked(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void onAboutButtonClicked(MouseEvent event) throws IOException {
        Parent about = FXMLLoader.load(getClass().getResource("about.fxml"));
        Scene aboutScene = new Scene(about);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(aboutScene);
        window.show();
    }

    public void onBackButtonClicked(MouseEvent event) throws IOException {
        Parent about = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene aboutScene = new Scene(about);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(aboutScene);
        window.show();
    }

    public void goMarvel(MouseEvent event) {
        goLink("http://marvel.com");
    }

    public void goLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createComponents() {
        Image image = new Image(comics.get(0).getPathImage());
        comicImage.setImage(image);
        comicImage.setFitWidth(ELEMENT_SIZE_WIDTH);
        comicImage.setFitHeight(ELEMENT_SIZE_HEIGHT);

        comicTitle.setText(comics.get(0).getTitle());
        comicDescription.setText(comics.get(0).getDescription());
    }

    public void callServer(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        marvelJson = new JSONObject(response.toString());

        getData();
    }

    public void getData() throws Exception {
        String title;
        String path;
        String description;

        dataObject = new JSONObject(marvelJson.getJSONObject("data").toString());
        resultsArray = dataObject.getJSONArray("results");

        JSONObject data = resultsArray.getJSONObject(0);
        JSONObject img = data.getJSONObject("thumbnail");
        title = data.getString("title");
        description = data.getString("description");
        path = img.getString("path") + "/portrait_fantastic." + img.getString("extension");
        this.comics.add(new Comics(title, path, description));

    }
}

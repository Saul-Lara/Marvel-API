package sample;

import DataBase.ComicsTable;
import DataBase.Connection;
import Tables.Comics;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
import java.util.ResourceBundle;

public class comicsController implements Initializable{

    @FXML private TilePane tilepane;
    @FXML private ScrollPane myScrollPane;
    @FXML private Hyperlink linkMarvel;

    public String searchURL;
    int count = 0;
    private int nRows = 3;
    private int nCols = 2;
    public double ELEMENT_SIZE_WIDTH = 168;
    public double ELEMENT_SIZE_HEIGHT = 252;
    public double GAP = ELEMENT_SIZE_HEIGHT / 10;
    public JSONObject marvelJson;
    public JSONObject dataObject;
    public JSONArray resultsArray;
    private List<Comics> comics = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tilepane.setPrefColumns(nCols);
        tilepane.setPrefRows(nRows);
        tilepane.setHgap(GAP);
        tilepane.setVgap(GAP);
    }

    public void initData(String URL) throws Exception {
        searchURL = URL;
        callServer(searchURL);
        linkMarvel.setText(marvelJson.getString("attributionText"));
        createComponents();
    }

    public void onExitButtonClicked(MouseEvent event){
        Platform.exit();
        System.exit(0);
    }

    public void onAboutButtonClicked(MouseEvent event) throws IOException {
        Parent about = FXMLLoader.load(getClass().getResource("about.fxml"));
        Scene aboutScene = new Scene(about);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(aboutScene);
        window.show();
    }

    public void onBackButtonClicked(MouseEvent event) throws IOException{
        Parent about = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene aboutScene = new Scene(about);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(aboutScene);
        window.show();
    }

    public void goMarvel(MouseEvent event){
        goLink("http://marvel.com");
    }

    public void goLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createComponents(){
        tilepane.getChildren().clear();

        for (int i = 0; i < nCols; i++){
            for (int j = 0; j < nRows; j++){
                if(count < resultsArray.length()){
                    tilepane.getChildren().add(createPage(count));
                    count++;
                }
            }
        }
    }

    public VBox createPage(int count){
        ImageView imageview = new ImageView();
        Image image = new Image(comics.get(count).getPathImage());
        imageview.setImage(image);
        imageview.setFitWidth(ELEMENT_SIZE_WIDTH);
        imageview.setFitHeight(ELEMENT_SIZE_HEIGHT);

        Font font = new Font("Amble CN", 17);

        Label lbl = new Label(comics.get(count).getTitle());
        lbl.setFont(font);

        JFXButton button = new JFXButton("More Information");
        button.setStyle("-fx-background-color: #1dacf4; -fx-text-fill: white; -fx-font-size: 14px;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Connection cnx = new Connection();
                String URL = "https://gateway.marvel.com";
                String apiKey = cnx.getapiKey();
                String comicInfo;

                ComicsTable comics_table = new ComicsTable();
                comicInfo = URL + comics_table.comicId(comics.get(count).getComicId()) + apiKey;

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("comicInfo.fxml"));
                Parent comic = null;
                try {
                    comic = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Scene comicScene = new Scene(comic);

                // Access the controller and call a method
                comicInfoController controller = loader.getController();
                try {
                    controller.initData(comicInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(comicScene);
                window.show();
            }
        });


        VBox pageBox = new VBox();

        pageBox.setPadding(new Insets(10, 30, 10, 30));
        pageBox.setSpacing(10);
        pageBox.setAlignment(Pos.CENTER);
        pageBox.getChildren().add(imageview);
        pageBox.getChildren().add(lbl);
        pageBox.getChildren().add(button);

        return pageBox;
    }

    public void callServer(String url) throws Exception{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        //int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        marvelJson = new JSONObject(response.toString());
        //marvelJson = myResponse;

        getData();

    }

    public void getData() throws Exception{
        String title;
        String path;
        int id;

        dataObject = new JSONObject(marvelJson.getJSONObject("data").toString());
        resultsArray = dataObject.getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++){
            JSONObject data = resultsArray.getJSONObject(i);
            JSONObject img = data.getJSONObject("thumbnail");
            title = data.getString("title");
            id = data.getInt("id");
            path = img.getString("path") + "/portrait_fantastic." + img.getString("extension");
            this.comics.add(new Comics(title, path, id));
        }
    }
}

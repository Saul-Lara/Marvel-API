package sample;

import Tables.Comics;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.IOException;
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
    public double ELEMENT_SIZE = 100;
    public double GAP = ELEMENT_SIZE/10;
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

        System.out.println("Status: "+ marvelJson.getString("status"));
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
        //Image image = new Image(path);
        //imageview.setImage(image);
        imageview.setFitWidth(ELEMENT_SIZE);
        imageview.setFitHeight(ELEMENT_SIZE);

        Font font = new Font("Amble CN", 15);

        Label lbl = new Label(comics.get(count).getTitle());
        lbl.setFont(font);

        VBox pageBox = new VBox();

        pageBox.setPadding(new Insets(10, 50, 50, 50));
        pageBox.setSpacing(10);
        pageBox.getChildren().add(lbl);

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

        dataObject = new JSONObject(marvelJson.getJSONObject("data").toString());
        System.out.println("Limit" + dataObject.getInt("limit"));

        resultsArray = dataObject.getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++){
            JSONObject data = resultsArray.getJSONObject(i);
            JSONObject img = data.getJSONObject("thumbnail");
            System.out.println(img.toString());
            title = data.getString("title");
            path = img.getString("path") + "/portrait_fantastic." + img.getString("extension");
            System.out.println(path);
            this.comics.add(new Comics(title, path));
        }
    }
}

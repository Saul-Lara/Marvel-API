package sample;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class comicInfoController {

    public String comicURL;

    public void initData(String URL) {
        comicURL = URL;
        System.out.println(comicURL);

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
}

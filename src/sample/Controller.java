package sample;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import DataBase.*;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private JFXComboBox<String> comboboxSearch;
    @FXML private JFXTextField TextfieldSearch;

    ObservableList<String> comboSearchContent =
            FXCollections.observableArrayList(
                    "Character",
                    "Comics",
                    "Series"
            );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboboxSearch.setItems(comboSearchContent);
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

    public void onSearchButtonClicked(MouseEvent event) throws Exception {
        String opc = comboboxSearch.getValue();
        String searchText = TextfieldSearch.getText();
        Connection cnx = new Connection();

        String URL = "https://gateway.marvel.com";
        String apiKey = cnx.getapiKey();
        String Search;

        if (opc == "Comics" && !searchText.isEmpty()){
            ComicsTable comics_table = new ComicsTable();
            Search = URL + comics_table.comics(searchText) + apiKey;

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("comics.fxml"));
            Parent comics = loader.load();
            Scene comicsScene = new Scene(comics);

            // Access the controller and call a method
            comicsController controller = loader.getController();
            controller.initData(Search);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(comicsScene);
            window.show();



        }else {
            System.out.println("error");
        }


    }
}

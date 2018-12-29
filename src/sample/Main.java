package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Marvel DataBase");
        Scene Scene = new Scene(root);
        Scene.setFill(Color.TRANSPARENT);       // Transparencia
        primaryStage.setScene(Scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

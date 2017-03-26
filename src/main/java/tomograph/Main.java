package tomograph;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {

    @FXML
    GridPane root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(Main.class.getResource("/tomograph.fxml"));

        Scene scene = new Scene(root, 1376, 510);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tomograph");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


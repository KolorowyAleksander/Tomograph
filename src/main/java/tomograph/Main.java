package tomograph;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Image image = new Image(getClass().getResourceAsStream("/Kolo.jpg"));
        Slider slider = new Slider(0, 100, 40);
        Label label = new Label("Label:");
        Label sliderValue = new Label(Double.toString(slider.getValue()));
        Color textColor = Color.BLACK;

        StackPane root = new StackPane();


        Scene scene = new Scene(root, 400, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Slider Sample");
        scene.setFill(Color.BLACK);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(70);

        ImageView sample = new ImageView(image);
        GridPane.setConstraints(sample, 0, 0);
        GridPane.setColumnSpan(sample, 3);
        grid.getChildren().add(sample);
        scene.setRoot(grid);

        label.setTextFill(textColor);
        GridPane.setConstraints(label, 0, 1);
        grid.getChildren().add(label);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                sample.setOpacity(new_val.doubleValue());
                sliderValue.setText(String.format("%.2f", new_val));
            }
        });
        GridPane.setConstraints(slider, 1, 1);
        grid.getChildren().add(slider);

        sliderValue.setTextFill(textColor);
        GridPane.setConstraints(sliderValue, 2, 1);
        grid.getChildren().add(sliderValue);

        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}


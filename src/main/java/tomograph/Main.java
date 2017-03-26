package tomograph;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting calculations");

        Image image = new Image(getClass().getResourceAsStream("/Shepp_logan.png"));
        Slider slider = new Slider(0, 100, 40);
        Label label = new Label("Alpha?:");
        Label sliderValue = new Label(Double.toString(slider.getValue()));
        double alpha = 250;
        double phi = 270;
        int n = 180;
        double deltaAlpha = 0.5;

        WritableImage sinogram = new WritableImage(n, (int)(180/deltaAlpha));

        new Sinogram(image, sinogram, alpha, phi, n, deltaAlpha).averagePoints();
        Image endImage = new InverseTransform(image, sinogram, alpha, phi, n, deltaAlpha).calculate();

        StackPane root = new StackPane();

        Scene scene = new Scene(root, 1250, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tomograph");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(70);

        ImageView sample = new ImageView(image);
        ImageView sinogramView = new ImageView(sinogram);
        ImageView endView = new ImageView(endImage);
        sample.setFitHeight(400);
        sample.setFitWidth(400);
        endView.setFitHeight(400);
        endView.setFitWidth(400);
        GridPane.setConstraints(sample, 0, 0);
        GridPane.setConstraints(sinogramView, 3, 0);
        GridPane.setConstraints(endView, 6, 0);
        GridPane.setColumnSpan(sample, 3);
        GridPane.setColumnSpan(sinogramView, 3);
        GridPane.setColumnSpan(endView, 3);
        grid.getChildren().add(sample);
        grid.getChildren().add(sinogramView);
        grid.getChildren().add(endView);
        scene.setRoot(grid);

        label.setTextFill(Color.BLACK);
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

        sliderValue.setTextFill(Color.BLACK);
        GridPane.setConstraints(sliderValue, 2, 1);
        grid.getChildren().add(sliderValue);

        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}


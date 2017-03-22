package tomograph;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Image image = new Image(getClass().getResourceAsStream("/Shepp_logan.png"));
        Slider slider = new Slider(0, 100, 40);
        Label label = new Label("Label:");
        Label sliderValue = new Label(Double.toString(slider.getValue()));
        double alpha = 250;
        double phi = 180;
        int r = 200;
        int n = 180;
        double deltaAlpha = 1;
        int numberOfSteps = (int)(360/deltaAlpha);


        Point position = Position.findEmmiterPosition(alpha, r);
        List<Point> detectorspositon = Position.findDetectorsPositions(alpha, phi, r, n);

        Canvas canvas = new Canvas(2 * r, 2 * r);
        Canvas sinogram = new Canvas(n, numberOfSteps);

        //drawing middle
        canvas.getGraphicsContext2D().getPixelWriter().setColor(200, 200, Color.GREEN);

        //drawing emmiter
        canvas.getGraphicsContext2D()
                .getPixelWriter()
                .setColor((int) position.x, (int) position.y, Color.RED);

        //drawing detectors
        for (Point detector : detectorspositon) {
            canvas.getGraphicsContext2D()
                    .getPixelWriter()
                    .setColor((int) detector.x, (int) detector.y, Color.BLACK);

            //drawing lines from detectors
            ArrayList<Point> line = Lines.arrayLine(position, detector);
            for (Point point : line) {
//                System.out.println(point.x + " :x; " + point.y + " :y ");
                canvas.getGraphicsContext2D()
                        .getPixelWriter()
                        .setColor((int) point.x, (int) point.y, Color.BLACK);
            }
        }

        Sinogram.averagePoints(image, sinogram, alpha, phi, n, deltaAlpha);


        StackPane root = new StackPane();

        Scene scene = new Scene(root, 1250, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Slider Sample");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(70);

        ImageView sample = new ImageView(image);
        sample.setFitHeight(400);
        sample.setFitWidth(400);
        GridPane.setConstraints(sample, 0, 0);
        GridPane.setConstraints(canvas, 3, 0);
        GridPane.setConstraints(sinogram, 3, 0);
        GridPane.setColumnSpan(sample, 3);
        grid.getChildren().add(sample);
        //grid.getChildren().add(canvas);
        grid.getChildren().add(sinogram);
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


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

import static oracle.jrockit.jfr.events.Bits.intValue;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Image image = new Image(getClass().getResourceAsStream("/Shepp_logan.png"));
        Slider slider = new Slider(0, 100, 40);
        Label label = new Label("Label:");
        Label sliderValue = new Label(Double.toString(slider.getValue()));
        double alpha = 90;
        double phi = 90;
        int r = 200;
        int n = 80;
        Point position = new Position().emiterPosition(alpha, r);
        Point[] detectorspositon = new Position().detectorPosition(alpha, phi, r, n);

        Canvas canvas = new Canvas(400,400);
        canvas.getGraphicsContext2D().setFill(Color.RED);
        canvas.getGraphicsContext2D().getPixelWriter().setColor(200,200,Color.GREEN);
        //rysowanie emitera
        canvas.getGraphicsContext2D().getPixelWriter().setColor(intValue(position.x), intValue(position.y), Color.RED);
        //rysowanie detektorow
        for(int i = 0; i < n; i++) {
            canvas.getGraphicsContext2D().getPixelWriter().setColor(intValue(detectorspositon[i].x),
                                                                    intValue(detectorspositon[i].y),
                                                                    Color.BLACK);
            //rysowanie linii
            ArrayList<Point> line = new Lines().arrayLine(position, detectorspositon[i]);
            for(Point point: line) {
                System.out.println(point.x + " :x; " + point.y + " :y ");
                canvas.getGraphicsContext2D().getPixelWriter().setColor(intValue(point.x),
                                                                        intValue(point.y),
                                                                        Color.BLACK);
            }
        }



        StackPane root = new StackPane();

        Scene scene = new Scene(root, 850, 500);
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
        GridPane.setColumnSpan(sample, 3);
        grid.getChildren().add(sample);
        grid.getChildren().add(canvas);
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


package tomograph;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    double deltaAlpha;
    double phi;
    int n;
    Image endImage;
    private Image image;
    private WritableImage sinogram;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting calculations");

        image = new Image(getClass().getResourceAsStream("/Shepp_logan.png"));
        Button button = new Button("calculate");
        Slider alphaSlider = new Slider(0, 5, 0.2);
        alphaSlider.setSnapToTicks(true);
        alphaSlider.setMajorTickUnit(1);
        alphaSlider.setMinorTickCount(4);
        alphaSlider.setBlockIncrement(0.2);
        Slider phiSlider = new Slider(0, 360, 270);
        phiSlider.setSnapToTicks(true);
        phiSlider.setMajorTickUnit(1);
        phiSlider.setMinorTickCount(0);
        Slider nSlider = new Slider(0, 360, 180);
        nSlider.setSnapToTicks(true);
        nSlider.setMajorTickUnit(1);
        nSlider.setMinorTickCount(0);
        Label label = new Label("delta Alpha:");
        Label sliderValue = new Label(Double.toString(alphaSlider.getValue()));
        Label label2 = new Label("Phi:");
        Label sliderValue2 = new Label(Double.toString(phiSlider.getValue()));
        Label label3 = new Label("n:");
        Label sliderValue3 = new Label(Double.toString(nSlider.getValue()));
        this.phi = 90.0;
        this.n = 90;
        this.deltaAlpha = 2.0;

        StackPane root = new StackPane();

        Scene scene = new Scene(root, 1250, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tomograph");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(70);

        GridPane.setConstraints(button, 3,2);
        grid.getChildren().add(button);

        ImageView sample = new ImageView(image);
        ImageView sinogramView = new ImageView(this.sinogram);
        ImageView endView = new ImageView(this.endImage);


        button.setOnMouseClicked(o -> {
            System.out.println("button");
            this.sinogram = new Sinogram(image, phi, n, deltaAlpha).averagePoints();
            this.endImage = new InverseTransform(image, this.sinogram, phi, n, deltaAlpha).calculate();
            sinogramView.setImage(this.sinogram);
            endView.setImage(this.endImage);
        });


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



        //----------------------------deltaAlpha customize
        label.setTextFill(Color.BLACK);
        GridPane.setConstraints(label, 0, 1);
        grid.getChildren().add(label);

        alphaSlider.valueProperty().addListener(o -> {
            double value = alphaSlider.getValue();
            deltaAlpha = value;
            sliderValue.setText(String.format("%.2f", value));
        });

        GridPane.setConstraints(alphaSlider, 1, 1);
        grid.getChildren().add(alphaSlider);

        sliderValue.setTextFill(Color.BLACK);
        GridPane.setConstraints(sliderValue, 2, 1);
        grid.getChildren().add(sliderValue);

        //-----------------------------Phi customize
        label2.setTextFill(Color.BLACK);
        GridPane.setConstraints(label2, 0, 2);
        grid.getChildren().add(label2);

        phiSlider.valueProperty().addListener(o -> {
            double value = phiSlider.getValue();
            phi = value;
            sliderValue2.setText(String.format("%.2f", value));
        });

        GridPane.setConstraints(phiSlider, 1, 2);
        grid.getChildren().add(phiSlider);

        sliderValue2.setTextFill(Color.BLACK);
        GridPane.setConstraints(sliderValue2, 2, 2);
        grid.getChildren().add(sliderValue2);

        //n customize
        label3.setTextFill(Color.BLACK);
        GridPane.setConstraints(label3, 0, 3);
        grid.getChildren().add(label3);

        nSlider.valueProperty().addListener(o -> {
            double value = nSlider.getValue();
            n = (int) value;
            System.out.println(n);
            sliderValue3.setText(String.format("%.2f", value));
        });

        GridPane.setConstraints(nSlider, 1, 3);
        grid.getChildren().add(nSlider);

        sliderValue3.setTextFill(Color.BLACK);
        GridPane.setConstraints(sliderValue3, 2, 3);
        grid.getChildren().add(sliderValue3);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


package tomograph;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private double deltaAlpha;
    private double phi;
    private int n;
    private Image endImage;
    private Image image;
    private WritableImage sinogram;

    @FXML
    private GridPane grid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        image = new Image(getClass().getResourceAsStream("/imageSamples/Shepp_logan.png"));
        Button button = new Button("calculate");
        Slider alphaSlider = new Slider(0, 5, 1);
        alphaSlider.setSnapToTicks(true);
        alphaSlider.setMajorTickUnit(0.1);
        alphaSlider.setMinorTickCount(0);
        alphaSlider.setBlockIncrement(0.1);
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

//        this.phi = 90.0;
//        this.n = 90;
//        this.deltaAlpha = 2.0;


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

        this.phi = phiSlider.getValue();
        this.deltaAlpha = alphaSlider.getValue();
        this.n = (int)nSlider.getValue();
    }
}

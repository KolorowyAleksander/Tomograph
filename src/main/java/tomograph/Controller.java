package tomograph;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private double numberOfSteps;
    private double deltaAlpha;
    private double phi;
    private int n;
    private int step;

    private Sinogram sinogram;
    private InverseTransform endImage;

    @FXML
    Image image;
    @FXML
    private Button calculateButton;
    @FXML
    private Slider alphaSlider;
    @FXML
    private Slider phiSlider;
    @FXML
    private Slider nSlider;
    @FXML
    private Slider stepSlider;
    @FXML
    private ImageView sample;
    @FXML
    private ImageView sinogramView;
    @FXML
    private ImageView endView;
    @FXML
    private Label alphaSliderValue;
    @FXML
    private Label phiSliderValue;
    @FXML
    private Label nSliderValue;
    @FXML
    private Label stepSliderValue;
    @FXML
    private GridPane firstColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        alphaSlider.setValue(1.0);
        phiSlider.setValue(270.0);
        nSlider.setValue(180);
        stepSlider.setValue(360 / alphaSlider.getValue());

        this.alphaSliderValue.setText(String.valueOf(alphaSlider.getValue()));
        this.phiSliderValue.setText(Double.toString(phiSlider.getValue()));
        this.nSliderValue.setText(Double.toString(nSlider.getValue()));
        this.stepSliderValue.setText(Double.toString(stepSlider.getValue()));

        this.phi = phiSlider.getValue();
        this.deltaAlpha = alphaSlider.getValue();
        this.n = (int) nSlider.getValue();
        this.step = (int) stepSlider.getValue();


        calculateButton.setOnMouseClicked(o -> {
            firstColumn.setDisable(true);

            if (sinogram == null
                    || sinogram.phi != phi
                    || sinogram.n != n
                    || sinogram.deltaAlpha != deltaAlpha
                    || sinogram.sourceImage != image) {
                this.sinogram = new Sinogram(image, phi, n, deltaAlpha);
            }

            if (endImage == null
                    || endImage.phi != phi
                    || endImage.deltaAlpha != deltaAlpha
                    || endImage.sourceImage != sinogram) {
                this.endImage =
                        new InverseTransform((int) this.image.getWidth(),
                                (int) this.image.getHeight(),
                                this.sinogram,
                                phi,
                                n,
                                deltaAlpha);
            }

            this.endImage.calculate(step);
            sinogramView.setImage(this.sinogram);
            endView.setImage(this.endImage);
            firstColumn.setDisable(false);
        });

        alphaSlider.valueProperty().addListener(o -> {
            double value = alphaSlider.getValue();
            this.deltaAlpha = value;
            this.numberOfSteps = (int) (360 / deltaAlpha);

            stepSlider.setMax(numberOfSteps);
            stepSlider.setValue(numberOfSteps);
            alphaSliderValue.setText(String.format("%.2f", value));
        });

        phiSlider.valueProperty().addListener(o -> {
            double value = phiSlider.getValue();
            this.phi = value;
            phiSliderValue.setText(String.format("%.2f", value));
        });

        nSlider.valueProperty().addListener(o -> {
            int value = (int) nSlider.getValue();
            this.n = value;
            nSliderValue.setText(String.format("%d", value));
        });

        stepSlider.valueProperty().addListener(o -> {
            int value = (int) stepSlider.getValue();
            this.step = value;
            stepSliderValue.setText(String.format("%d", value));
        });
    }
}

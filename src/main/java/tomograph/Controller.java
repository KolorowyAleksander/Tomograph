package tomograph;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private double deltaAlpha;
    private double phi;
    private int n;
    private int step;
    private int numberOfSteps;

    private Sinogram sinogram;
    private InverseTransform endImage;
    private Image image;

    @FXML
    private ChoiceBox imageChoice;
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
        setInitialValues();

        calculateButton.setOnMouseClicked(o -> {
            firstColumn.setDisable(true);

            if (sinogram.phi != phi || sinogram.n != n
                    || sinogram.deltaAlpha != deltaAlpha
                    || sinogram.sourceImage != image
                    || endImage.numberOfSteps != step) {

                this.sinogram = new Sinogram(image, phi, n, deltaAlpha);

                this.endImage = new InverseTransform(
                        (int) this.image.getWidth(),
                        (int) this.image.getHeight(),
                        this.sinogram,
                        phi,
                        n,
                        deltaAlpha,
                        step
                );
            }

            sinogramView.setImage(this.sinogram);
            endView.setImage(this.endImage);

            firstColumn.setDisable(false);
        });

        alphaSlider.valueProperty().addListener(o -> {
            double value = alphaSlider.getValue();
            this.deltaAlpha = value;
            this.numberOfSteps = (int) (360 / deltaAlpha);

            alphaSliderValue.setText(String.format("%.1f", value));


            stepSlider.setMax(numberOfSteps);
            stepSlider.setValue(numberOfSteps);
            this.step = numberOfSteps;

        });

        phiSlider.valueProperty().addListener(o -> {
            double value = phiSlider.getValue();
            this.phi = value;
            phiSliderValue.setText(String.format("%d", (int) value));
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

//            if (!stepSlider.isValueChanging()) {
//                calculateButton.fireEvent(generateEmptyMouseEvent());
//            }
        });

        imageChoice.valueProperty().addListener(o -> {
            this.image = new Image("imageSamples/" + imageChoice.getValue());
            sample.setImage(this.image);
        });
    }

    private void setInitialValues() {
        this.deltaAlpha = 1.0;
        this.phi = 270.0;
        this.n = 180;
        this.numberOfSteps = (int) (360 / deltaAlpha);
        this.step = numberOfSteps;

        alphaSlider.setValue(deltaAlpha);
        phiSlider.setValue(phi);
        nSlider.setValue(n);
        stepSlider.setValue(step);

        this.alphaSliderValue.setText(String.valueOf(deltaAlpha));
        this.phiSliderValue.setText(String.valueOf(phi));
        this.nSliderValue.setText(String.valueOf(n));
        this.stepSliderValue.setText(String.valueOf(step));

        this.image = new Image("imageSamples/" + imageChoice.getValue());
        this.sinogram = new Sinogram(image, phi, n, deltaAlpha);
        this.endImage = new InverseTransform((int) image.getWidth(), (int) image.getHeight(),
                sinogram, phi, n, deltaAlpha, numberOfSteps);

        this.sample.setImage(image);
        this.sinogramView.setImage(sinogram);
        this.endView.setImage(endImage);
    }

    private MouseEvent generateEmptyMouseEvent() {
        return new MouseEvent(MouseEvent.MOUSE_CLICKED,
                0,
                0,
                0,
                0,
                MouseButton.PRIMARY,
                1,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                null);
    }
}

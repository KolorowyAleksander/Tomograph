package tomograph;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class CalculationRepresentation extends WritableImage {

    protected Image sourceImage;
    protected double phi;
    protected int n;
    protected double deltaAlpha;
    protected double radius;

    protected CalculationRepresentation(int width, int height, Image sourceImage, double phi, int n, double deltaAlpha) {
        super(width, height);
        this.sourceImage = sourceImage;
        this.phi = phi;
        this.n = n;
        this.deltaAlpha = deltaAlpha;
    }

    protected void normalize() {
        double max = 0.0, min = 1.0;
        PixelReader pixelReader = this.getPixelReader();

        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                double brightness = pixelReader.getColor(i, j).getBrightness();
                max = Math.max(brightness, max);
                min = Math.min(brightness, min);
            }
        }

        PixelWriter pixelWriter = this.getPixelWriter();
        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                Color c = pixelReader.getColor(i, j);
                pixelWriter.setColor(i, j, Color.gray((c.getBrightness() - min) / (max - min)));
            }
        }
    }
}

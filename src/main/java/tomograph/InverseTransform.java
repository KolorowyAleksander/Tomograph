package tomograph;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class InverseTransform {

    private Image image;
    private Image sinogram;
    private double phi;
    private int n;
    private double deltaAlpha;
    private double[][] tab;

    public InverseTransform(Image image, Image sinogram, double phi, int n, double deltaAlpha) {
        this.image = image;
        this.sinogram = sinogram;
        this.phi = phi;
        this.n = n;
        this.deltaAlpha = deltaAlpha;
        this.tab = new double[(int) image.getHeight()][(int) image.getWidth()];


        for (int i = 0; i < (int) image.getHeight(); i++) {
            for (int j = 0; j < (int) image.getHeight(); j++) {
                tab[i][j] = 1.0;
            }
        }
    }

    public Image calculate() {
        System.out.println("calculating reverse transform");
        WritableImage endImage = new WritableImage((int) this.image.getHeight(), (int) this.image.getHeight());
        int numberOfSteps = (int) (180 / this.deltaAlpha);
        PixelReader reader = this.sinogram.getPixelReader();
        for (int i = 0; i < numberOfSteps; i++) {
            Point emitter = Position.findEmmiterPosition(90 + i * deltaAlpha, (int) this.image.getHeight() / 2);
            List<Point> detectors = Position.findDetectorsPositions(90 + i * deltaAlpha, this.phi, (int) this.image.getHeight() / 2, this.n);

            for (int j = 0; j < n; j++) {
                ArrayList<Point> line = Lines.arrayLine(emitter, detectors.get(j));
                for (Point point : line) {
                    tab[point.x][point.y] += reader.getColor(j, i).getBrightness();
                }
            }
        }

        PixelWriter writer = endImage.getPixelWriter();
        for (int i = 0; i < (int) this.image.getHeight(); i++) {
            for (int j = 0; j < (int) this.image.getHeight(); j++) {
                writer.setColor(i, j, Color.hsb(0.0, 0.0, tab[i][j]/numberOfSteps));
            }
        }

        return endImage;
    }
}

package tomograph;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class InverseTransform {

    public Image image;
    public Image sinogram;
    public double alpha;
    public double phi;
    public int n;
    public double deltaAlpha;
    public List[][] drawing;
    public double[][][] averages;

    public InverseTransform(Image image, Image sinogram, double alpha, double phi, int n, double deltaAlpha) {
        this.image = image;
        this.sinogram = sinogram;
        this.alpha = alpha;
        this.phi = phi;
        this.n = n;
        this.deltaAlpha = deltaAlpha;
        this.drawing = new ArrayList[(int) image.getHeight()][(int) image.getHeight()];
//        this.averages = new double[(int) image.getHeight()][(int) image.getWidth()][n *(int)(360/deltaAlpha)];

        for (int i = 0; i < (int) image.getHeight(); i++) {
            for (int j = 0; j < (int) image.getHeight(); j++) {
                drawing[i][j] = new ArrayList<Double>();
            }
        }
    }

    public Image calculate() {
        WritableImage endImage = new WritableImage((int) this.image.getHeight(), (int) this.image.getHeight());
        int numberOfSteps = (int) (360 / this.deltaAlpha);
        PixelReader reader = this.sinogram.getPixelReader();
        for (int i = 0; i < numberOfSteps; i++) {
            Point emiter = Position.findEmmiterPosition(this.alpha + i * deltaAlpha, (int) this.image.getHeight() / 2);
            List<Point> detectors = Position.findDetectorsPositions(this.alpha + i * deltaAlpha, this.phi, (int) this.image.getHeight() / 2, this.n);

            for (int j = 0; j < n; j++) {
                ArrayList<Point> line = Lines.arrayLine(emiter, detectors.get(j));
                for (Point point : line) {
                    drawing[point.x][point.y].add(reader.getColor(j, i).getBrightness());
                }
            }
        }

        PixelWriter writer = endImage.getPixelWriter();
        for (int i = 0; i < (int) this.image.getHeight(); i++) {
            for (int j = 0; j < (int) this.image.getHeight(); j++) {
                double average = drawing[i][j].stream().mapToDouble(a -> (Double) a).average().orElseGet(() -> 0.0);
                writer.setColor(i, j, Color.hsb(0.0, 0.0, average));
            }
        }

        return endImage;
    }
}

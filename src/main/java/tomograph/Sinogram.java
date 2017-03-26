package tomograph;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Sinogram extends WritableImage {
    private Image sourceImage;
    private double phi;
    private int n;
    private double deltaAlpha;

    public Sinogram(Image image, double phi, int n, double deltaAlpha) {
        super(n, (int) (180 / deltaAlpha));
        this.sourceImage = image;
        this.phi = phi;
        this.n = n;
        this.deltaAlpha = deltaAlpha;
        this.calculate();
    }

    public void calculate() {
        System.out.println("calculating sinogram");

        int numberOfSteps = (int) (180 / deltaAlpha);

        PixelWriter writer = this.getPixelWriter();
        for (int i = 0; i < numberOfSteps; i++) {
            Point emitter = Position.findEmmiterPosition(90 + i * deltaAlpha, (int) sourceImage.getHeight() / 2);
            List<Point> detectors =
                    Position.findDetectorsPositions(90 + i * deltaAlpha, phi, (int) sourceImage.getHeight() / 2, n);
            for (int j = 0; j < n; j++) {
                ArrayList<Point> line = Lines.arrayLine(emitter, detectors.get(j));
                writer.setColor(j, i, Color.hsb(0.0, 0.0, averageLine(line)));
            }
        }

        normalize();
    }

    private void normalize() {
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

    private double averageLine(ArrayList<Point> line) {
        PixelReader reader = this.sourceImage.getPixelReader();
        return line.stream()
                .mapToDouble(point -> reader.getColor(point.x, point.y).getBrightness())
                .average()
                .orElseThrow(RuntimeException::new);
    }
}

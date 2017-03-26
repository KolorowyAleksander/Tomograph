package tomograph;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Sinogram {
    private Image image;
    private WritableImage sinogram;
    private double alpha;
    private double phi;
    private int n;
    private double deltaAlpha;

    public Sinogram(Image image, double phi, int n, double deltaAlpha){
        this.image = image;
        this.sinogram =  new WritableImage(n, (int)(180/deltaAlpha));
        this.phi = phi;
        this.n = n;
        this.deltaAlpha = deltaAlpha;
    }

    public WritableImage averagePoints() {
        System.out.println("calculating sinogram");

        int numberOfSteps = (int) (180 / deltaAlpha);

        PixelWriter writer = sinogram.getPixelWriter();
        for (int i = 0; i < numberOfSteps; i++) {
            Point emitter = Position.findEmmiterPosition(i * deltaAlpha, (int) image.getHeight() / 2);
            List<Point> detectors = Position.findDetectorsPositions(i * deltaAlpha, phi, (int) image.getHeight() / 2, n);
            for (int j = 0; j < n; j++) {
                ArrayList<Point> line = Lines.arrayLine(emitter, detectors.get(j));
                writer.setColor(j, i, Color.hsb(0.0, 0.0, averageLine(line)));
            }
        }

        return this.sinogram;
    }

    public double averageLine(ArrayList<Point> line) {
        PixelReader reader = this.image.getPixelReader();
        return line.stream()
                .mapToDouble(point -> reader.getColor(point.x, point.y).getBrightness())
                .average()
                .orElseThrow(RuntimeException::new);
    }
}

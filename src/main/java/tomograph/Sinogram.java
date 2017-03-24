package tomograph;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Sinogram {
    public static void averagePoints(Image image, WritableImage sinogram, double alpha, double phi, int n, double deltaAlpha) {
        int numberOfSteps = (int) (360 / deltaAlpha);

        PixelWriter writer = sinogram.getPixelWriter();
        for (int i = 0; i < numberOfSteps; i++) {
            Point emitter = Position.findEmmiterPosition(alpha + i * deltaAlpha, (int) image.getHeight() / 2);
            List<Point> detectors = Position.findDetectorsPositions(alpha + i * deltaAlpha, phi, (int) image.getHeight() / 2, n);
            for (int j = 0; j < n; j++) {
                ArrayList<Point> line = Lines.arrayLine(emitter, detectors.get(j));
                writer.setColor(j, i, Color.hsb(0.0, 0.0, averageLine(line, image)));
            }
        }
    }

    public static double averageLine(ArrayList<Point> line, Image image) {
        PixelReader reader = image.getPixelReader();
        return line.stream()
                .mapToDouble(point -> reader.getColor(point.x, point.y).getBrightness())
                .average()
                .orElseThrow(RuntimeException::new);
    }
}

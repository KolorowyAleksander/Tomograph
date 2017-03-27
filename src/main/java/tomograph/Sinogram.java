package tomograph;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Sinogram extends CalculationRepresentation {

    public Sinogram(Image image, double phi, int n, double deltaAlpha) {
        super(n, (int) (360 / deltaAlpha), image, phi, n, deltaAlpha);

        this.radius = image.getHeight() / 2;
        this.calculate();
    }

    private void calculate() {
        int numberOfSteps = (int) (360 / this.deltaAlpha);

        PixelWriter writer = this.getPixelWriter();
        for (int step = 0; step < numberOfSteps; step++) {
            Point emitter = Position.findEmmiterPosition(step * deltaAlpha, (int) this.radius);
            List<Point> detectors =
                    Position.findDetectorsPositions(step * deltaAlpha, phi, (int) this.radius, n);

            for (int j = 0; j < n; j++) {
                ArrayList<Point> line = Lines.arrayLine(emitter, detectors.get(j));
                writer.setColor(j, step, Color.hsb(0.0, 0.0, averageLine(line)));
            }
        }

        normalize();
    }

    private double averageLine(ArrayList<Point> line) {
        PixelReader reader = this.sourceImage.getPixelReader();
        return line.stream()
                .mapToDouble(point -> reader.getColor(point.x, point.y).getBrightness())
                .average()
                .orElseThrow(RuntimeException::new);
    }
}

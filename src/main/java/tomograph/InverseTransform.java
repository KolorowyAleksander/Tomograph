package tomograph;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class InverseTransform extends CalculationRepresentation {

    private double lMin = Double.MAX_VALUE, lMax = 0.0;
    protected int numberOfSteps;
    private double[][] tab;

    public InverseTransform(int width,
                            int height,
                            Image sinogram,
                            double phi,
                            int n,
                            double deltaAlpha,
                            int numberOfSteps) {
        super(width, height, sinogram, phi, n, deltaAlpha);

        this.radius = (double) width / 2;
        this.tab = new double[width][height];
        this.numberOfSteps = numberOfSteps;

        calculate();
    }

    private void calculate() {

        PixelReader reader = this.sourceImage.getPixelReader();
        for (int step = 0; step < numberOfSteps; step++) {
            Point emitter = Position.findEmmiterPosition(step * deltaAlpha, (int) radius);
            List<Point> detectors =
                    Position.findDetectorsPositions(step * deltaAlpha, this.phi, (int) radius, this.n);

            for (int j = 0; j < n; j++) {
                ArrayList<Point> line = Lines.arrayLine(emitter, detectors.get(j));
                for (Point point : line) {
                    double brightness = reader.getColor(j, step).getBrightness();
                    tab[point.x][point.y] += brightness;
                    lMax = Math.max(tab[point.x][point.y], lMax);
                    lMin = Math.min(tab[point.x][point.y], lMin);
                }
            }
        }

        PixelWriter writer = this.getPixelWriter();
        for (int i = 0; i < (int) this.getWidth(); i++) {
            for (int j = 0; j < (int) this.getHeight(); j++) {
                writer.setColor(i, j, Color.hsb(0.0, 0.0, (tab[i][j] - lMin) / (lMax - lMin)));
            }
        }

        normalize();
    }
}

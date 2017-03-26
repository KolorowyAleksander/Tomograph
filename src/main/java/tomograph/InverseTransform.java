package tomograph;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class InverseTransform extends WritableImage {

    private Image sourceSinogram;
    private double phi;
    private int n;
    private double deltaAlpha;
    private double radius;
    private double[][] tab;

    public InverseTransform(int width, int height, Image sinogram, double phi, int n, double deltaAlpha) {
        super(width, height);
        this.sourceSinogram = sinogram;
        this.phi = phi;
        this.n = n;
        this.deltaAlpha = deltaAlpha;
        this.radius = (double)width/2;
        this.tab = new double[width][height];

        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                tab[i][j] = 1.0;
            }
        }
    }

    public void calculate() {
        System.out.println("calculating reverse transform");

        int numberOfSteps = (int) (180 / this.deltaAlpha);

        PixelReader reader = this.sourceSinogram.getPixelReader();
        for (int i = 0; i < numberOfSteps; i++) {
            Point emitter = Position.findEmmiterPosition(90 + i * deltaAlpha, (int) radius);
            List<Point> detectors =
                    Position.findDetectorsPositions(90 + i * deltaAlpha, this.phi, (int) radius, this.n);

            for (int j = 0; j < n; j++) {
                ArrayList<Point> line = Lines.arrayLine(emitter, detectors.get(j));
                for (Point point : line) {
                    tab[point.x][point.y] += reader.getColor(j, i).getBrightness();
                }
            }
        }

        PixelWriter writer = this.getPixelWriter();
        for (int i = 0; i < (int) this.getWidth(); i++) {
            for (int j = 0; j < (int) this.getHeight(); j++) {
                writer.setColor(i, j, Color.hsb(0.0, 0.0, tab[i][j]/numberOfSteps));
            }
        }
    }


    public void calculateStepByStep() {
        throw new NotImplementedException();
    }
}

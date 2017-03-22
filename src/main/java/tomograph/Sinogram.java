package tomograph;


import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Sinogram {
    public static void averagePoints(Image image, Canvas sinogram, double alpha, double phi, int n, double deltaAlpha){
        int numberOfSteps = (int)(360/deltaAlpha);
        for(int i = 0; i < numberOfSteps; i++){
            Point emiter = Position.findEmmiterPosition(alpha+i*deltaAlpha, (int)image.getHeight()/2);
            List<Point> detectors = Position.findDetectorsPositions(alpha+i*deltaAlpha, phi, (int)image.getHeight()/2, n);
            for(int j = 0; j < n; j++){
                ArrayList<Point> line = Lines.arrayLine(emiter, detectors.get(j));
                sinogram.getGraphicsContext2D()
                        .getPixelWriter()
                        .setArgb(j, i, averageLine(line, image));

            }
        }
    }

    private static int averageLine(ArrayList<Point> line, Image image){
        int sum = 0;
        for(Point point:line) {
            sum += image.getPixelReader().getArgb((Math.round(point.x) < 0 ? 0 : Math.round(point.x)),
                                                  (Math.round(point.y) < 0 ? 0 : Math.round(point.y)));
        }

        return Math.round(sum/line.size());
    }
}

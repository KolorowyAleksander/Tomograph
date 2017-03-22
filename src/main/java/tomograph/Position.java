package tomograph;


import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class Position {

    public static Point findEmmiterPosition(double alpha, int r) {
        alpha += 90;
        double x = r * cos(toRadians(alpha));
        double y = r * sin(toRadians(alpha));

        return new Point(positionCheck((int)(x+r), 2*r),
                         positionCheck((int)(y+r), 2*r));
    }

    public static List<Point> findDetectorsPositions(double alpha, double phi, int r, int n) {
        List<Point> detectorsPositions = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double x, y;
            if (i == 0) {
                x = r * cos(toRadians(alpha + 90) + PI - (toRadians(phi) / 2));
                y = r * sin(toRadians(alpha + 90) + PI - (toRadians(phi) / 2));
            } else if (i > 0 && i < n - 1) {
                x = r * cos(toRadians(alpha + 90) + PI - (toRadians(phi) / 2) + (i * toRadians(phi) / (n - 1)));
                y = r * sin(toRadians(alpha + 90) + PI - (toRadians(phi) / 2) + (i * toRadians(phi) / (n - 1)));
            } else {
                x = r * cos(toRadians(alpha + 90) + PI + (toRadians(phi) / 2));
                y = r * sin(toRadians(alpha + 90) + PI + (toRadians(phi) / 2));
            }

            detectorsPositions.add(new Point(positionCheck((int)(x+r), 2*r),
                                             positionCheck((int)(y+r), 2*r)));
        }

        return detectorsPositions;
    }

    private static int positionCheck(int x, int height){
        if(x < 0){
            return 0;
        } else if( x > height-1) {
            return height-1;
        } else {
            return x;
        }
    }
}



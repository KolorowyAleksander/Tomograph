package tomograph;


import static java.lang.Math.*;

public class Position {

    public static Point emiterPosition(double alpha, double r){
        alpha += 90;
        double x = r * cos(toRadians(alpha));
        double y = r * sin(toRadians(alpha));

        return new Point(x + r, y + r);
    }

    public Point[] detectorPosition(double alpha, double phi, int r, int n){
        double x = 0, y = 0;
        Point detectorsposition[] = new Point[n];
        for(int i = 0; i < n; i++){
            Point position = new Point(x, y);
            if (i == 0) {
                x = r * cos(toRadians(alpha+90) + PI - (toRadians(phi) / 2));
                y = r * sin(toRadians(alpha+90) + PI - (toRadians(phi) / 2));
            } else if (i > 0 && i < n - 1) {
                x = r * cos(toRadians(alpha+90) + PI - (toRadians(phi) / 2) + (i * toRadians(phi) / (n - 1)));
                y = r * sin(toRadians(alpha+90) + PI - (toRadians(phi) / 2) + (i * toRadians(phi) / (n - 1)));
            } else {
                x = r * cos(toRadians(alpha+90) + PI + (toRadians(phi) / 2));
                y = r * sin(toRadians(alpha+90) + PI + (toRadians(phi) / 2));
            }
            position.x = x+200; //+200 bo javafx
            position.y = y+200; //+200 bo javafx
            detectorsposition[i] =  position;
        }

        return detectorsposition;
    }
}

package tomograph;


import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;


import static oracle.jrockit.jfr.events.Bits.intValue;

public class Position {

    public Point emiterPosition(double alpha, float r){
        double x, y;
        x = r * Math.cos(Math.toRadians(alpha));
        y = r * Math.sin(Math.toRadians(alpha));

        Point position = new Point(x, y);

        return position;
    }

    public Point[] detectorPosition(double alpha, double phi, int r, int n){
        double x = 0, y = 0;
        Point detectorsposition[] = new Point[n];
        for(int i = 0; i < n; i++){
            Point position = new Point(x, y);
            if (i == 0) {
                x = r * Math.cos(Math.toRadians(alpha+90) + Math.PI - (Math.toRadians(phi) / 2));
                y = r * Math.sin(Math.toRadians(alpha+90) + Math.PI - (Math.toRadians(phi) / 2));
            } else if (i > 0 && i < n - 1) {
                x = r * Math.cos(Math.toRadians(alpha+90) + Math.PI - (Math.toRadians(phi) / 2) + (i * Math.toRadians(phi) / (n - 1)));
                y = r * Math.sin(Math.toRadians(alpha+90) + Math.PI - (Math.toRadians(phi) / 2) + (i * Math.toRadians(phi) / (n - 1)));
            } else {
                x = r * Math.cos(Math.toRadians(alpha+90) + Math.PI + (Math.toRadians(phi) / 2));
                y = r * Math.sin(Math.toRadians(alpha+90) + Math.PI + (Math.toRadians(phi) / 2));
            }
            position.x = x;
            position.y = y;
            detectorsposition[i] =  position;
        }

        return detectorsposition;
    }
}

package tomograph;


import java.util.ArrayList;

import static java.lang.Math.abs;
import static oracle.jrockit.jfr.events.Bits.intValue;

public class Lines {

    public ArrayList<Point> arrayLine(Point a, Point b){
        ArrayList<Point> result = new ArrayList<Point>();
        if(b.x < a.x){
            Point temp = a;
            a = b;
            b = temp;
        }
        double deltax = abs(b.x - a.x);
        double deltay = abs(b.y - a.y);
        if(deltax == 0){
            deltax = 1;
        }
        double deltaerr = abs(deltay/deltax);
        double error = deltaerr - 0.5;
        int y = intValue(a.y);
        for(int x = intValue(a.x); x < b.x; x++){
            Point position = new Point(x, y);
            position.x = x;
            position.y = y;
            result.add(position);
            error += deltaerr;
            if(error >= 0.5){
                y += 1;
                error -= 1.0;
            }
        }
        return result;
    };
}

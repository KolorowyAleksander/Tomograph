package tomograph;


import java.util.ArrayList;

import static java.lang.Math.abs;

public class Lines {

    public static ArrayList<Point> arrayLine(Point a, Point b) {
        ArrayList<Point> result = new ArrayList<Point>();
        int octant = octant2(a, b);
        a = switchToOctantZeroFrom(octant, a.x, a.y);
        b = switchToOctantZeroFrom(octant, b.x, b.y);
        double deltax = b.x - a.x;
        double deltay = b.y - a.y;
        if (deltax == 0) {
            deltax = 1;
        }
        double deltaerr = abs(deltay / deltax);
        double error = deltaerr - 0.5;
        int y = a.y;
        for (int x = a.x; x < b.x; x++) {
            Point position = switchFromOctantZeroTo(octant, x, y);
            result.add(position);
            error += deltaerr;
            if (error >= 0.5) {
                y += 1;
                error -= 1.0;
            }
        }

        return result;
    }

    private static int octant(double dx, double dy) {

        if (dx == 0.0 && dy == 0.0)
            throw new IllegalArgumentException("Cannot compute the octant for point ( " + dx + ", " + dy + " )");

        double adx = abs(dx);
        double ady = abs(dy);

        if (dx >= 0) {
            if (dy >= 0) {
                if (adx >= ady)
                    return 7;
                else
                    return 6;
            } else { // dy < 0
                if (adx >= ady)
                    return 0;
                else
                    return 1;
            }
        } else { // dx < 0
            if (dy >= 0) {
                if (adx >= ady)
                    return 4;
                else
                    return 5;
            } else { // dy < 0
                if (adx >= ady)
                    return 3;
                else
                    return 2;
            }
        }
    }

    private static int octant2(Point p0, Point p1) {
        double dx = p1.x - p0.x;
        double dy = p1.y - p0.y;
        if (dx == 0.0 && dy == 0.0)
            throw new IllegalArgumentException("Cannot compute the octant for two identical points " + p0);
        return octant(dx, dy);
    }

    private static Point switchToOctantZeroFrom(int octant, int x, int y) {
        switch (octant) {
            case 7:
                return new Point(x, y);
            case 6:
                return new Point(y, x);
            case 5:
                return new Point(y, -x);
            case 4:
                return new Point(-x, y);
            case 3:
                return new Point(-x, -y);
            case 2:
                return new Point(-y, -x);
            case 1:
                return new Point(-y, x);
            case 0:
                return new Point(x, -y);
        }
        return null;
    }

    public static Point switchFromOctantZeroTo(int octant, int x, int y) {
        switch (octant) {
            case 7:
                return new Point(x, y);
            case 6:
                return new Point(y, x);
            case 5:
                return new Point(-y, x);
            case 4:
                return new Point(-x, y);
            case 3:
                return new Point(-x, -y);
            case 2:
                return new Point(-y, -x);
            case 1:
                return new Point(y, -x);
            case 0:
                return new Point(x, -y);
        }
        return null;
    }
}



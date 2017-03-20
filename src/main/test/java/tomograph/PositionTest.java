package tomograph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {

    public static final int R = 200;

    @Test
    public void alphaZero() {
        //when
        Point position = Position.emiterPosition(0, R);

        //then
        assertEquals(R, position.x);
        assertEquals(0, position.y);
    }

    @Test
    public void alpha90Deg() {
        //when
        Point position = Position.emiterPosition(90, R);

        //then
        assertEquals(0, position.x);
        assertEquals(R, position.y);
    }

    @Test
    public void alpha180Deg() {
        //when
        Point position = Position.emiterPosition(180, R);

        //then
        assertEquals(R, position.x);
        assertEquals(2*R, position.y);
    }

    @Test
    public void alpha270Deg() {
        //when
        Point position = Position.emiterPosition(270, R);

        //then
        assertEquals(2*R, position.x);
        assertEquals(R, position.y);
    }
}

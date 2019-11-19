package amazons;

import org.junit.Test;

import static org.junit.Assert.*;
import static amazons.Square.*;
import ucb.junit.textui;

/** The suite of all JUnit tests for the Square Class.
 *  @author Michael Chang
 */
public class SquareTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(SquareTest.class);
    }

    @Test
    public void testSquare() {
        assertTrue(sq(0, 0) == sq(0));
        assertTrue(sq(0) == sq("a", "1"));
        assertTrue(sq(9, 9) == sq(99));
        assertTrue(sq(99) == sq("j", "10"));
        assertTrue(sq(3, 3) == sq(33));
        assertTrue(sq(33) == sq("d", "4"));
        assertTrue(sq(6, 6) == sq(66));
        assertTrue(sq(66) == sq("g", "7"));
        assertTrue(sq(0, 1) == sq(1));
        assertTrue(sq(1) == sq("a", "2"));
        assertTrue(sq(8, 4) == sq(84));
        assertTrue(sq(84) == sq("i", "5"));
        assertTrue(sq("a", "1") == sq("a1"));
        assertTrue(sq("j", "9") == sq("j9"));
        assertTrue(sq("e", "4") == sq("e4"));
        assertEquals("i5", sq(84).toString());
        assertEquals("a10", sq(9).toString());
        assertEquals("j10", sq(99).toString());
        assertEquals("e8", sq(47).toString());
        assertEquals("a1", sq(0).toString());
    }
    @Test
    public void testisQueenMove() {
        assertFalse(sq(1, 5).isQueenMove(sq(1, 5)));
        assertFalse(sq(1, 5).isQueenMove(sq(2, 7)));
        assertFalse(sq(0, 0).isQueenMove(sq(5, 1)));
        assertTrue(sq(1, 1).isQueenMove(sq(9, 9)));
        assertTrue(sq(2, 7).isQueenMove(sq(8, 7)));
        assertTrue(sq(3, 0).isQueenMove(sq(3, 4)));
        assertTrue(sq(7, 9).isQueenMove(sq(0, 2)));
        assertTrue(sq(0, 0).isQueenMove(sq(9, 9)));
        assertFalse(sq(0, 0).isQueenMove(sq(9, 8)));
        assertTrue(sq(1, 1).isQueenMove(sq(4, 4)));
        assertTrue(sq(1, 1).isQueenMove(sq(1, 3)));
        assertFalse(sq(1, 1).isQueenMove(sq(2, 3)));

    }

    @Test
    public void testQueenMove() {
        assertEquals(sq(0, 0).queenMove(0, 6), sq(0, 6));
        assertEquals(sq(0, 0).queenMove(1, 6), sq(6, 6));
        assertEquals(sq(1, 1).queenMove(2, 3), sq(4, 1));
        assertEquals(sq(5, 5).queenMove(3, 1), sq(6, 4));
        assertEquals(sq(5, 5).queenMove(4, 2), sq(5, 3));
        assertEquals(sq(5, 5).queenMove(5, 1), sq(4, 4));
        assertEquals(sq(5, 5).queenMove(6, 4), sq(1, 5));
        assertEquals(sq(5, 5).queenMove(7, 2), sq(3, 7));
        assertEquals(null, sq(5, 5).queenMove(0, 10));
        assertEquals(null, sq(5, 5).queenMove(8, 2));

    }

    @Test
    public void testDirection() {
        assertEquals(0, sq(0, 0).direction(sq(0, 6)));
        assertEquals(1, sq(1, 1).direction(sq(4, 4)));
        assertEquals(2, sq(1, 1).direction(sq(5, 1)));
        assertEquals(3, sq(5, 5).direction(sq(7, 3)));
        assertEquals(4, sq(5, 5).direction(sq(5, 3)));
        assertEquals(5, sq(7, 7).direction(sq(5, 5)));
        assertEquals(6, sq(6, 4).direction(sq(4, 4)));
        assertEquals(7, sq(5, 6).direction(sq(3, 8)));

    }

    @Test
    public void testtoDirection() {
        assertEquals(0, todirection(0));
        assertEquals(1, todirection(20));
        assertEquals(-1, todirection(-4));
        assertEquals(1, todirection(2));
        assertEquals(1, todirection(50));
        assertEquals(-1, todirection(-1));


    }
}



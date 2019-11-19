package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static amazons.Piece.SPEAR;
import static org.junit.Assert.*;
import static amazons.Square.*;
import ucb.junit.textui;

import java.util.ArrayList;
import java.util.Iterator;

/** The suite of all JUnit tests for the Board Class.
 *  @author Michael Chang
 */
public class BoardTest {

    /**
     * Run the JUnit tests in this package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(amazons.BoardTest.class);
    }

    @Test
    /** Tests basic correctness of put and get on the initialized board. */
    public void testPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(BLACK, b.get(Square.sq(3, 5)));
        b.put(WHITE, 2, 3);
        assertEquals(WHITE, b.get(2, 3));
        b.put(BLACK, 'e', '6');
        assertEquals(b.get('e', '6'), BLACK);
        b.put(WHITE, Square.sq("a", "6"));
        assertEquals(WHITE, b.get(Square.sq(0, 5)));
        b.put(EMPTY, 3, 5);
        assertEquals(EMPTY, b.get(Square.sq(3, 5)));
    }
    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
        Board test = new Board();
        test.makeMove(sq(6, 0), sq(4, 2), sq(7, 5));
        assertEquals(BOARD_STATE_1, test.toString());
        test.makeMove(sq(3, 9), sq(5, 7), sq(2, 4));
        assertEquals(BOARD_STATE_2, test.toString());
        test.makeMove(sq(4, 2), sq(3, 1), sq(4, 2));
        assertEquals(BOARD_STATE_3, test.toString());
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
                    "   - - - B - - B - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   B - - - - - - - - B\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   W - - - - - - - - W\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - W - - W - - -\n";

    static final String SMILE =
                    "   - - - - - - - - - -\n"
                            + "   - S S S - - S S S -\n"
                            + "   - S - S - - S - S -\n"
                            + "   - S S S - - S S S -\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - W - - - - W - -\n"
                            + "   - - - W W W W - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - - - - - - - -\n";

    static final String BOARD_STATE_1 =
                    "   - - - B - - B - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   B - - - - - - - - B\n"
                            + "   - - - - - - - S - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   W - - - - - - - - W\n"
                            + "   - - - - W - - - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - W - - - - - -\n";

    static final String BOARD_STATE_2 =
                    "   - - - - - - B - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - - - B - - - -\n"
                            + "   B - - - - - - - - B\n"
                            + "   - - - - - - - S - -\n"
                            + "   - - S - - - - - - -\n"
                            + "   W - - - - - - - - W\n"
                            + "   - - - - W - - - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - W - - - - - -\n";

    static final String BOARD_STATE_3 =
                    "   - - - - - - B - - -\n"
                            + "   - - - - - - - - - -\n"
                            + "   - - - - - B - - - -\n"
                            + "   B - - - - - - - - B\n"
                            + "   - - - - - - - S - -\n"
                            + "   - - S - - - - - - -\n"
                            + "   W - - - - - - - - W\n"
                            + "   - - - - S - - - - -\n"
                            + "   - - - W - - - - - -\n"
                            + "   - - - W - - - - - -\n";


    @Test
    public void testMoveandUndo() {
        Board test = new Board();
        test.makeMove(sq(6, 0), sq(4, 2), sq(7, 5));
        assertEquals(BOARD_STATE_1, test.toString());
        test.makeMove(sq(3, 9), sq(5, 7), sq(2, 4));
        assertEquals(BOARD_STATE_2, test.toString());
        test.makeMove(sq(4, 2), sq(3, 1), sq(4, 2));
        assertEquals(BOARD_STATE_3, test.toString());
        test.undo();
        assertEquals(BOARD_STATE_2, test.toString());
        test.undo();
        assertEquals(BOARD_STATE_1, test.toString());
        test.undo();
        assertEquals(INIT_BOARD_STATE, test.toString());
    }

    @Test
    public void testReachableFromIterator() {
        Board test = new Board();
        Iterator<Square> iter = test.reachableFrom(sq(0, 3), null);
        int counter = 0;
        java.util.ArrayList<Square> list = new java.util.ArrayList<>();
        while (iter.hasNext()) {
            list.add(iter.next());
            counter += 1;
        }
        assertEquals(20, counter);
        makeSmile(test);
        Iterator iter2 = test.reachableFrom(Square.sq(23), null);
        int counter1 = 0;
        while (iter2.hasNext()) {
            counter1 += 1;
            iter2.next();
        }
        assertEquals(18, counter1);
        counter1 = 0;
        iter2 = test.reachableFrom(Square.sq(27), null);
        while (iter2.hasNext()) {
            counter1 += 1;
            iter2.next();
        }
        assertEquals(0, counter1);

    }
    @Test
    public void testLegalMoveIterator() {
        Board test1 = new Board();
        Iterator<Move> iter = test1.legalMoves();
        java.util.ArrayList<Move> list = new java.util.ArrayList<>();
        int counter = 0;
        while (iter.hasNext()) {
            counter += 1;
            iter.next();
        }
        assertEquals(2176, counter);
        makeSmile(test1);
        test1.put(EMPTY, Square.sq(32));
        test1.put(EMPTY, Square.sq(42));
        test1.put(EMPTY, Square.sq(52));
        test1.put(EMPTY, Square.sq(62));
        test1.put(EMPTY, Square.sq(23));
        test1.put(WHITE, Square.sq(27));
        test1.put(WHITE, Square.sq(77));
        test1.put(EMPTY, Square.sq(73));
        counter = 0;
        for (Iterator<Square> itermoves = test1.reachableFrom(Square.sq(73),
                null); itermoves.hasNext(); ) {
            for (Iterator<Square> iterspears = test1.reachableFrom(
                    itermoves.next(), Square.sq(73)); iterspears.hasNext(); ) {
                iterspears.next();
                counter += 1;
            }
        }
        int counter1 = 0;
        for (Iterator<Move> itermove = test1.legalMoves(WHITE);
             itermove.hasNext();) {
            itermove.next();
            counter1 += 1;
        }
        assertEquals(0, counter1);
    }

    @Test
    public void testisUnblockedMove() {
        Board t = new Board();
        assertFalse(t.isUnblockedMove(sq(3, 0), sq(3, 9), null));
        assertFalse(t.isUnblockedMove(sq(3, 0), sq(3, 0), null));
        assertTrue(t.isUnblockedMove(sq(3, 0), sq(3, 3), null));
        assertTrue(t.isUnblockedMove(sq(3, 0), sq(3, 9), sq(3, 9)));
        assertTrue(t.isUnblockedMove(sq(3, 0), sq(3, 9), sq(3, 9)));
        assertFalse(t.isUnblockedMove(sq(0, 3), sq(0, 3), null));
        assertTrue(t.isUnblockedMove(sq(0, 3), sq(9, 3), sq(9, 3)));
        assertFalse(t.isUnblockedMove(sq(3, 0), sq(3, 9), null));
        assertTrue(t.isUnblockedMove(sq(3, 0), sq(4, 1), null));
    }

    @Test
    public void testWinner() {
        Board test = new Board();
        test.makeMove(sq("d1"), sq("a1"), sq("d1"));
        test.makeMove(sq("d10"), sq("d9"), sq("d10"));
        test.makeMove(sq("a4"), sq("a2"), sq("a4"));
        test.makeMove(sq("d9"), sq("d8"), sq("d9"));
        test.makeMove(sq("j4"), sq("b4"), sq("j4"));
        test.makeMove(sq("g10"), sq("g9"), sq("g10"));
        test.makeMove(sq("b4"), sq("b1"), sq("b4"));
        test.makeMove(sq("j7"), sq("b7"), sq("b6"));
        test.makeMove(sq("g1"), sq("g2"), sq("g1"));
        test.makeMove(sq("g9"), sq("j9"), sq("g9"));
        test.makeMove(sq("g2"), sq("b2"), sq("g2"));
        test.makeMove(sq("d8"), sq("d3"), sq("a3"));
        test.makeMove(sq("b1"), sq("c1"), sq("c10"));
        test.makeMove(sq("d3"), sq("c3"), sq("c2"));
        test.makeMove(sq("c1"), sq("b1"), sq("c1"));
        test.makeMove(sq("c3"), sq("b3"), sq("c3"));
        assertEquals(BLACK, test.winner());

    }


    @Test
    public void testCopy() {
        Board old = new Board();
        Board cpy = new Board(old);
        cpy.makeMove(sq("d1"), sq("a1"), sq("d1"));
        cpy.makeMove(sq("d10"), sq("d9"), sq("d10"));
        assertNotEquals(old.toString(), cpy.toString());
        Board cpy2 = new Board(cpy);
        cpy2.undo();
        assertNotEquals(cpy.toString(), cpy2.toString());
    }

    @Test
    public void test12() {
        Board brd = new Board();
        brd.makeMove(sq("a4"), sq("a5"), sq("a6"));
        brd.makeMove(sq("j7"), sq("j6"), sq("j7"));
        brd.makeMove(sq("a5"), sq("b6"), sq("b8"));
        brd.makeMove(sq("j6"), sq("j5"), sq("j6"));
        brd.makeMove(sq("b6"), sq("b7"), sq("c8"));
        brd.makeMove(sq("j5"), sq("i4"), sq("i5"));
        brd.makeMove(sq("b7"), sq("c7"), sq("e9"));
        brd.makeMove(sq("i4"), sq("j5"), sq("i4"));
        brd.makeMove(sq("c7"), sq("d8"), sq("d9"));
        brd.makeMove(sq("j5"), sq("i6"), sq("i7"));
        brd.makeMove(sq("d1"), sq("d2"), sq("d3"));
        brd.makeMove(sq("g10"), sq("f10"), sq("g10"));
        brd.makeMove(sq("d2"), sq("e3"), sq("e5"));
        brd.makeMove(sq("f10"), sq("e10"), sq("f10"));
        brd.makeMove(sq("d8"), sq("e8"), sq("f9"));
        brd.makeMove(sq("d10"), sq("c9"), sq("d10"));
        brd.makeMove(sq("e3"), sq("e4"), sq("f5"));
        brd.makeMove(sq("a7"), sq("c5"), sq("a7"));
        brd.makeMove(sq("e4"), sq("f4"), sq("g5"));
        brd.makeMove(sq("c9"), sq("b9"), sq("a9"));
        brd.makeMove(sq("e8"), sq("f8"), sq("h10"));
        brd.makeMove(sq("c5"), sq("a3"), sq("a1"));
        brd.makeMove(sq("f4"), sq("g4"), sq("h3"));
        brd.makeMove(sq("b9"), sq("a8"), sq("b7"));
        brd.makeMove(sq("f8"), sq("g9"), sq("i9"));
        Iterator<Move> mv = brd.legalMoves(WHITE);
        int counter = 0;
        while (mv.hasNext()) {
            mv.next();
            counter += 1;
        }
        assertEquals(672, counter);

    }

    @Test
    public void testGame() {
        Board test = new Board();
        makeBoard(test);
        test.makeMove(sq("d3"), sq("c4"), sq("c3"));
        test.makeMove(sq("i2"), sq("j2"), sq("i2"));
        test.makeMove(sq("c4"), sq("b3"), sq("a3"));
        test.makeMove(sq("b9"), sq("a9"), sq("c9"));
        test.makeMove(sq("b3"), sq("b1"), sq("a1"));
        test.makeMove(sq("b6"), sq("a7"), sq("a5"));
        test.makeMove(sq("c7"), sq("b6"), sq("a6"));
        Iterator<Move> moves = test.legalMoves(BLACK);
        int counter = 0;
        while (moves.hasNext()) {
            moves.next();
            counter += 1;
        }
        assertEquals(2, counter);
        test.makeMove(sq("a10"), sq("b9"), sq("a10"));
        Iterator<Move> moves2 = test.legalMoves(BLACK);
        int counter2 = 0;
        while (moves.hasNext()) {
            moves.next();
            counter2 += 1;
        }
        assertEquals(0, counter2);
        assertEquals(null, test.getWinner());
        test.makeMove(sq("b1"), sq("c1"), sq("b1"));
        assertEquals(WHITE, test.getWinner());

    }

    public void makeBoard(Board b) {
        b.makeMove(sq("g1"), sq("g5"), sq("j8"));
        b.makeMove(sq("j7"), sq("g4"), sq("d4"));
        b.makeMove(sq("a4"), sq("c6"), sq("a8"));
        b.makeMove(sq("d10"), sq("h6"), sq("f6"));
        b.makeMove(sq("d1"), sq("b3"), sq("b10"));
        b.makeMove(sq("a7"), sq("c5"), sq("d5"));
        b.makeMove(sq("c6"), sq("e8"), sq("c10"));
        b.makeMove(sq("c5"), sq("e7"), sq("e3"));
        b.makeMove(sq("b3"), sq("c2"), sq("a2"));
        b.makeMove(sq("g10"), sq("g6"), sq("g8"));
        b.makeMove(sq("e8"), sq("b8"), sq("f8"));
        b.makeMove(sq("e7"), sq("c7"), sq("h2"));
        b.makeMove(sq("g5"), sq("i5"), sq("i10"));
        b.makeMove(sq("g6"), sq("d3"), sq("b5"));
        b.makeMove(sq("b8"), sq("e8"), sq("b8"));
        b.makeMove(sq("h6"), sq("h5"), sq("i4"));
        b.makeMove(sq("j4"), sq("j1"), sq("h3"));
        b.makeMove(sq("g4"), sq("g1"), sq("f2"));
        b.makeMove(sq("j1"), sq("j7"), sq("j1"));
        b.makeMove(sq("g1"), sq("g7"), sq("i7"));
        b.makeMove(sq("j7"), sq("g10"), sq("f9"));
        b.makeMove(sq("h5"), sq("h8"), sq("h5"));
        b.makeMove(sq("i5"), sq("h6"), sq("i6"));
        b.makeMove(sq("h8"), sq("g9"), sq("h8"));
        b.makeMove(sq("h6"), sq("f4"), sq("g5"));
        b.makeMove(sq("g9"), sq("f10"), sq("d8"));
        b.makeMove(sq("g10"), sq("h10"), sq("h9"));
        b.makeMove(sq("g7"), sq("d7"), sq("e7"));
        b.makeMove(sq("h10"), sq("g10"), sq("h10"));
        b.makeMove(sq("f10"), sq("d10"), sq("e9"));
        b.makeMove(sq("g10"), sq("e10"), sq("b7"));
        b.makeMove(sq("d10"), sq("c9"), sq("d10"));
        b.makeMove(sq("f4"), sq("d6"), sq("e5"));
        b.makeMove(sq("d7"), sq("c8"), sq("d9"));
        b.makeMove(sq("e8"), sq("c6"), sq("e8"));
        b.makeMove(sq("c7"), sq("b6"), sq("c5"));
        b.makeMove(sq("d6"), sq("c7"), sq("d6"));
        b.makeMove(sq("d3"), sq("c4"), sq("a4"));
        b.makeMove(sq("c2"), sq("b3"), sq("b4"));
        b.makeMove(sq("c4"), sq("c1"), sq("i1"));
        b.makeMove(sq("b3"), sq("b1"), sq("f5"));
        b.makeMove(sq("c8"), sq("a10"), sq("d7"));
        b.makeMove(sq("b1"), sq("e4"), sq("c2"));
        b.makeMove(sq("c1"), sq("h1"), sq("f3"));
        b.makeMove(sq("e4"), sq("d3"), sq("f1"));
        b.makeMove(sq("c9"), sq("b9"), sq("c8"));
        b.makeMove(sq("d3"), sq("e4"), sq("h4"));
        b.makeMove(sq("h1"), sq("g2"), sq("g1"));
        b.makeMove(sq("e4"), sq("d3"), sq("e4"));
        b.makeMove(sq("g2"), sq("h1"), sq("g2"));
        b.makeMove(sq("e10"), sq("g10"), sq("e10"));
        b.makeMove(sq("h1"), sq("j3"), sq("i3"));
        b.makeMove(sq("g10"), sq("f10"), sq("g9"));
        b.makeMove(sq("j3"), sq("h1"), sq("j3"));
        b.makeMove(sq("f10"), sq("g10"), sq("f10"));
        b.makeMove(sq("h1"), sq("i2"), sq("h1"));
    }


    @Test
    public void findWin() {
        Board f = new Board();
        f.makeMove(sq("d1"), sq("a1"), sq("b1"));
        f.makeMove(sq("d10"), sq("d4"), sq("i4"));
        f.makeMove(sq("a4"), sq("a2"), sq("a3"));
        f.makeMove(sq("g10"), sq("g3"), sq("b3"));
        f.makeMove(sq("g1"), sq("j1"), sq("i1"));
        f.makeMove(sq("a7"), sq("a10"), sq("i2"));
        f.makeMove(sq("j4"), sq("j2"), sq("j3"));
        f.makeMove(sq("d4"), sq("f6"), sq("b2"));
        f.makeMove(sq("j2"), sq("i3"), sq("j4"));
        f.makeMove(sq("a10"), sq("c8"), sq("a10"));
        f.makeMove(sq("i3"), sq("j2"), sq("g5"));
        Iterator<Move> moves = f.legalMoves(BLACK);
        ArrayList<Move> list = new ArrayList<>();
        while (moves.hasNext()) {
            list.add(moves.next());
        }
        assertTrue(list.contains(Move.mv(sq("g3"), sq("h3"), sq("i3"))));
    }
}

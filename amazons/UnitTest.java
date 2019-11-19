package amazons;

import ucb.junit.textui;

/** The suite of all JUnit tests for the Amazons package.
 *  @author Michael Chang
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(BoardTest.class);
        textui.runClasses(SquareTest.class);
    }
}



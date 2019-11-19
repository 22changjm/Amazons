package amazons;

import java.util.regex.Pattern;

import static amazons.Move.mv;

/** A Player that takes input as text commands from the standard input.
 *  @author Michael Chang
 */
class TextPlayer extends Player {

    /** Char A integer value. */
    static final int A = 97;

    /** Char j integer value. */
    static final int J = 106;

    /** A new TextPlayer with no piece or controller (intended to produce
     *  a template). */
    TextPlayer() {
        this(null, null);
    }

    /** A new TextPlayer playing PIECE under control of CONTROLLER. */
    private TextPlayer(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new TextPlayer(piece, controller);
    }

    @Override
    String myMove() {
        while (true) {
            String line = _controller.readLine();
            if (line == null) {
                return "quit";
            } else if (Pattern.matches("([a-z]\\d+ [a-z]\\d+ [a-z]\\d+$)|"
                            + "([a-z]\\d+\\-[a-z]\\d+\\([a-z]\\d+\\)$)",
                    line) && myPiece() == Piece.EMPTY) {
                _controller.reportError("misplaced move");
            } else if (Pattern.matches("([a-z]\\d+ [a-z]\\d+ [a-z]\\d+$)|"
                    + "([a-z]\\d+\\-[a-z]\\d+\\([a-z]\\d+\\)$)", line)) {
                String from = line.split(" |-|\\(")[0].
                        replaceAll(" |-|\\(|\\)", "");
                String to = line.split(" |-|\\(")[1].
                        replaceAll(" |-|\\(|\\)", "");
                String spear = line.split(" |-|\\(")[2]
                        .replaceAll(" |-|\\(|\\)", "");
                if (from.charAt(0) > J
                        || to.charAt(0) > J || spear.charAt(0) > J
                        || from.charAt(0) < A || to.charAt(0) < A
                        || spear.charAt(0) < A) {
                    _controller.reportError("Invalid move. "
                            + "Please try again.");
                    continue;
                }
                if (Integer.parseInt(from.substring(1)) > 10
                        || Integer.parseInt(to.substring(1)) > 10
                        || Integer.parseInt(spear.substring(1)) > 10
                        || Integer.parseInt(from.substring(1)) < 1
                        || Integer.parseInt(to.substring(1)) < 1
                        || Integer.parseInt(spear.substring(1)) < 1) {
                    _controller.reportError("Invalid move. "
                            + "Please try again.");
                    continue;
                }
                Move move = mv(Square.sq(from),
                        Square.sq(to), Square.sq(spear));
                if (!_controller.board().isLegal(move)) {
                    _controller.reportError("Invalid move. "
                            + "Please try again.");
                    continue;
                }
            }
            return line;
        }
    }
}


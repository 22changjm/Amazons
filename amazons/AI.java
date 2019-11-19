package amazons;



import java.util.Iterator;

import static java.lang.Math.*;

import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author Michael Chang
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board);
        }
        if (sense == 1) {
            int bestvalue = -INFTY;
            for (Iterator<Move> i = board.legalMoves(WHITE); i.hasNext();) {
                Move move = i.next();
                board.makeMove(move);
                int value = findMove(board, depth - 1, false, -1, alpha, beta);
                board.undo();
                if (max(bestvalue, value) == value) {
                    bestvalue = value;
                    if (saveMove) {
                        _lastFoundMove = move;
                    }
                }
                if (bestvalue == WINNING_VALUE) {
                    return bestvalue;
                }
                alpha = max(alpha, bestvalue);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestvalue;
        } else {
            int bestvalue = INFTY;
            for (Iterator<Move> i = board.legalMoves(BLACK); i.hasNext();) {
                Move move = i.next();
                board.makeMove(move);
                int value = findMove(board, depth - 1, false, 1, alpha, beta);
                board.undo();
                if (min(bestvalue, value) == value) {
                    bestvalue = value;
                    if (saveMove) {
                        _lastFoundMove = move;
                    }
                }
                if (bestvalue == -WINNING_VALUE) {
                    return bestvalue;
                }
                beta = min(beta, bestvalue);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestvalue;
        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        int N = board.numMoves();
        final int moves = 20;
        if (N < moves + 15) {
            return 1;
        }
        if (N < 2 * moves + 15) {
            return 2;
        }
        if (N < 3 * moves + 10) {
            return 3;
        }
        if (N < 4 * moves) {
            return 4;
        }
        return 5;

    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        Piece winner = board.getWinner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        }
        int scorewhite = 0;
        Iterator<Move> whitemoves = board.legalMoves(WHITE);
        while (whitemoves.hasNext()) {
            whitemoves.next();
            scorewhite += 1;
        }
        int scoreblack = 0;
        Iterator<Move> blackmoves = board.legalMoves(BLACK);
        while (blackmoves.hasNext()) {
            blackmoves.next();
            scoreblack += 1;
        }
        return scorewhite - scoreblack;
    }


}

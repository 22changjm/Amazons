package amazons;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import java.util.HashMap;

import static amazons.Piece.*;
import static amazons.Move.mv;


/** The state of an Amazons Game.
 *  @author Michael Chang
 */
class Board {

    /** The previous board after a move was made. */
    private Stack<Move> _moveStack;

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** The number of moves on a given board. */
    private int _numMoves;

    /** HashMap that represents the board. */
    private HashMap<Square, Piece> _map;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        this._turn = model._turn;
        this._winner = model._winner;
        this._numMoves = model._numMoves;
        this._map = new HashMap<>(model._map);
        this._moveStack = new Stack<>();
        this._moveStack.addAll(model._moveStack);
    }

    /** Clears the board to the initial position. */
    void init() {
        _turn = WHITE;
        _winner = null;
        _numMoves = 0;
        _moveStack = new Stack<>();
        _map = new HashMap<>();
        final int whitepos1 = 3;
        final int whitepos2 = 30;
        final int whitepos3 = 60;
        final int whitepos4 = 93;
        final int blackpos1 = 6;
        final int blackpos2 = 39;
        final int blackpos3 = 69;
        final int blackpos4 = 96;
        for (int i = 0; i < 100; i += 1) {
            _map.put(Square.sq(i), EMPTY);
            if (i == whitepos1 || i == whitepos2
                    || i == whitepos3 || i == whitepos4) {
                _map.put(Square.sq(i), WHITE);
            } else if (i == blackpos1 || i == blackpos2
                    || i == blackpos3 || i == blackpos4) {
                _map.put(Square.sq(i), BLACK);
            }
        }
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return _numMoves; }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        if (_winner == null) {
            Iterator<Move> iter = legalMoves(_turn.opponent());
            if (iter.hasNext()) {
                return null;
            } else {
                return _turn;
            }
        } else {
            return _winner;
        }
    }

    /** Retrieve winner on board.
     * @return winning side */
    Piece getWinner() {
        return _winner;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return _map.get(s); }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return get(Square.sq(col, row));
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        _map.put(s, p); }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        put(p, Square.sq(col, row));
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (!from.isQueenMove(to)) {
            return false;
        }
        int dir = from.direction(to);
        for (Square check = from.queenMove(dir, 1);
             check != to; check = check.queenMove(dir, 1)) {
            if (get(check) != EMPTY && check != asEmpty) {
                return false;
            }
        }
        if (get(to) != EMPTY && to != asEmpty) {
            return false;
        }
        return true;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from) == turn();
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        return isUnblockedMove(from, to, null);
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        return isUnblockedMove(to, spear, from);

    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        if (!isLegal(move.from())) {
            return false;
        } else if (!isLegal(move.from(), move.to())) {
            return false;
        }
        return isLegal(move.from(), move.to(), move.spear());

    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        makeMove(Move.mv(from, to, spear));
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        assert isLegal(move);
        _moveStack.push(move);
        put(get(move.from()), move.to());
        put(EMPTY, move.from());
        put(SPEAR, move.spear());
        _numMoves += 1;
        _winner = winner();
        _turn = _turn.opponent();

    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (numMoves() == 0) {
            return;
        } else {
            Move prev = _moveStack.pop();
            put(EMPTY, prev.spear());
            put(get(prev.to()), prev.from());
            put(EMPTY, prev.to());
            _numMoves -= 1;
            _turn = _turn.opponent();
            _winner = null;
        }
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = -1;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            Square result = _from.queenMove(_dir, _steps);
            toNext();
            return result;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            while (_dir < 8
                    && !isUnblockedMove(_from,
                    _from.queenMove(_dir, _steps + 1), _asEmpty)) {
                _steps = 0;
                _dir += 1;
            }
            _steps += 1;
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _startingSquares.hasNext() || _pieceMoves.hasNext();
        }

        @Override
        public Move next() {
            Move result = mv(_start, _nextSquare, _spearThrows.next());
            toNext();
            return result;
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (!_spearThrows.hasNext()) {
                if (!_pieceMoves.hasNext()) {
                    while (_startingSquares.hasNext()) {
                        _start = _startingSquares.next();
                        if (get(_start) == _fromPiece
                                && new ReachableFromIterator(
                                        _start, null).hasNext()) {
                            break;
                        }
                    }
                    if (get(_start) == _fromPiece
                            && new ReachableFromIterator(
                                    _start, null).hasNext()) {
                        _pieceMoves = new ReachableFromIterator(_start, null);
                        _nextSquare = _pieceMoves.next();
                        _spearThrows = new ReachableFromIterator(
                                _nextSquare, _start);
                        if (!_spearThrows.hasNext()) {
                            toNext();
                        }
                    }
                } else {
                    while (_pieceMoves.hasNext()) {
                        _nextSquare = _pieceMoves.next();
                        if (new ReachableFromIterator(
                                _nextSquare, _start).hasNext()) {
                            _spearThrows = new
                                    ReachableFromIterator(_nextSquare, _start);
                            return;
                        }
                    }
                    if (!_pieceMoves.hasNext() && _startingSquares.hasNext()) {
                        toNext();
                    }
                }
            } else {
                return;
            }
        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
    }

    @Override
    public String toString() {
        String str = "  ";
        for (int row = 9; row >= 0; row -= 1) {
            for (int col = 0; col < 10; col += 1) {
                str += " " + get(Square.sq(col * 10 + row)).toString();
                if (col == 9 && row != 0) {
                    str += "\n  ";
                }
            }
        }
        str += "\n";
        return str;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
}

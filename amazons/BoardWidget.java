package amazons;

import ucb.gui2.Pad;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import static amazons.Piece.*;
import static amazons.Square.sq;

/** A widget that displays an Amazons game.
 *  @author Michael Chang
 */
class BoardWidget extends Pad {

    /* Parameters controlling sizes, speeds, colors, and fonts. */

    /** Colors of empty squares and grid lines. */
    static final Color
        SPEAR_COLOR = new Color(64, 64, 64),
        LIGHT_SQUARE_COLOR = new Color(238, 207, 161),
        DARK_SQUARE_COLOR = new Color(205, 133, 63);

    /** Locations of images of white and black queens. */
    private static final String
        WHITE_QUEEN_IMAGE = "wq4.png",
        BLACK_QUEEN_IMAGE = "bq4.png",
        SELECTED_QUEEN_IMAGE = "selected.png",
        DOT = "dot.png";



    /** Size parameters. */
    private static final int
        SQUARE_SIDE = 30,
        BOARD_SIDE = SQUARE_SIDE * 10;

    /** A graphical representation of an Amazons board that sends commands
     *  derived from mouse clicks to COMMANDS.  */
    BoardWidget(ArrayBlockingQueue<String> commands) {
        _commands = commands;
        setMouseHandler("click", this::mouseClicked);
        setPreferredSize(BOARD_SIDE, BOARD_SIDE);

        try {
            _whiteQueen = ImageIO.read(Utils.getResource(WHITE_QUEEN_IMAGE));
            _blackQueen = ImageIO.read(Utils.getResource(BLACK_QUEEN_IMAGE));
            _selectedQueen = ImageIO.read(
                    Utils.getResource(SELECTED_QUEEN_IMAGE));
            _dot = ImageIO.read(Utils.getResource(DOT));

        } catch (IOException excp) {
            System.err.println("Could not read queen images.");
            System.exit(1);
        }
        _acceptingMoves = false;
    }

    /** Draw the bare board G.  */
    private void drawGrid(Graphics2D g) {
        g.setColor(LIGHT_SQUARE_COLOR);
        g.fillRect(0, 0, BOARD_SIDE, BOARD_SIDE);
        g.setColor(DARK_SQUARE_COLOR);
        for (int y = 0; y <= BOARD_SIDE; y += 2 * SQUARE_SIDE) {
            for (int x = SQUARE_SIDE; x <= BOARD_SIDE; x += 2 * SQUARE_SIDE) {
                g.fillRect(x,  y, SQUARE_SIDE, SQUARE_SIDE);
            }
        }
        for (int y = SQUARE_SIDE; y <= BOARD_SIDE; y += 2 * SQUARE_SIDE) {
            for (int x = 0; x <= BOARD_SIDE; x += 2 * SQUARE_SIDE) {
                g.fillRect(x,  y, SQUARE_SIDE, SQUARE_SIDE);
            }
        }
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        drawGrid(g);
        for (int i = 0; i < BOARD_SIDE / 3; i += 1) {
            if (_board.get(sq(i)) != EMPTY) {
                if (list.size() == 1
                        && list.get(0).equals(sq(i).toString())) {
                    drawSelectedQueen(g, sq(i));
                    drawPossibleSpots(g, sq(i), null);
                }
                if (list.size() == 2) {
                    drawSelectedQueen(g, sq(list.get(1)));
                    drawPossibleSpots(g, sq(list.get(1)), sq(list.get(0)));
                }
                if (_board.get(sq(i)) == WHITE
                        || _board.get(sq(i)) == BLACK) {
                    if ((list.size() == 2 || list.size() == 1)
                            && sq(i) == sq(list.get(0))) {
                        continue;
                    }
                    drawQueen(g, sq(i), _board.get(sq(i)));
                } else {
                    drawSpear(g, sq(i));
                }
            }
        }
    }

    /** Draw all the possible moves on the clicked queen.
     * @param g graphics.
     * @param s square.
     * @param asEmpty asEmpty. */
    private void drawPossibleSpots(Graphics2D g, Square s, Square asEmpty) {
        Iterator<Square> iter = _board.reachableFrom(s, asEmpty);
        while (iter.hasNext()) {
            Square d = iter.next();
            g.drawImage(_dot, cx(d.col()) + 8, cy(d.row()) + 8, null);
        }
    }
    /** Draw a queen for side PIECE at square S on G.  */
    private void drawQueen(Graphics2D g, Square s, Piece piece) {
        g.drawImage(piece == WHITE ? _whiteQueen : _blackQueen,
                    cx(s.col()) + 2, cy(s.row()) + 4, null);
    }

    /** Draw a selected queen for side PIECE at square S on G.  */
    private void drawSelectedQueen(Graphics2D g, Square s) {
        g.drawImage(_selectedQueen,
                cx(s.col()) + 2, cy(s.row()) + 4, null);
    }


    /** Draw a spear PIECE at square S on G. */
    private void drawSpear(Graphics2D g, Square s) {
        g.setColor(SPEAR_COLOR);
        g.fillRect(cx(s.col()), cy(s.row()), SQUARE_SIDE, SQUARE_SIDE);
    }

    /** Handle a click on S. */
    private void click(Square s) {
        if (_board.getWinner() == null) {
            if (list.size() == 0 && _board.isLegal(s)) {
                list.add(s.toString());
            } else if (list.size() == 1 && sq(list.get(0)) == s) {
                list.remove(0);
            } else if (list.size() == 1
                    && _board.get(s) == _board.turn()
                    && s != sq(list.get(0))) {
                list.remove(0);
                list.add(s.toString());
            }
            if (list.size() == 1
                    && _board.isLegal(sq(list.get(0)), s)) {
                list.add(s.toString());
            }
            if (list.size() == 2
                    && _board.isLegal(sq(list.get(0)), sq(list.get(1)), s)) {
                list.add(s.toString());
            }
            if (list.size() == 3) {
                String cmd = list.get(0)
                        + " " + list.get(1) + " " + list.get(2);
                list.clear();
                _commands.add(cmd);
            }
            repaint();
        }
    }

    /** Handle mouse click event E. */
    private synchronized void mouseClicked(String unused, MouseEvent e) {
        int xpos = e.getX(), ypos = e.getY();
        int x = xpos / SQUARE_SIDE,
            y = (BOARD_SIDE - ypos) / SQUARE_SIDE;
        if (_acceptingMoves
            && x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE) {
            click(sq(x, y));
        }
    }

    /** Revise the displayed board according to BOARD. */
    synchronized void update(Board board) {
        _board.copy(board);
        repaint();
    }

    /** Turn on move collection iff COLLECTING, and clear any current
     *  partial selection.   When move collection is off, ignore clicks on
     *  the board. */
    void setMoveCollection(boolean collecting) {
        _acceptingMoves = collecting;
        repaint();
    }

    /** Return x-pixel coordinate of the left corners of column X
     *  relative to the upper-left corner of the board. */
    private int cx(int x) {
        return x * SQUARE_SIDE;
    }

    /** Return y-pixel coordinate of the upper corners of row Y
     *  relative to the upper-left corner of the board. */
    private int cy(int y) {
        return (Board.SIZE - y - 1) * SQUARE_SIDE;
    }

    /** Return x-pixel coordinate of the left corner of S
     *  relative to the upper-left corner of the board. */
    private int cx(Square s) {
        return cx(s.col());
    }

    /** Return y-pixel coordinate of the upper corner of S
     *  relative to the upper-left corner of the board. */
    private int cy(Square s) {
        return cy(s.row());
    }

    /** Queue on which to post move commands (from mouse clicks). */
    private ArrayBlockingQueue<String> _commands;
    /** Board being displayed. */
    private final Board _board = new Board();

    /** Image of white queen. */
    private BufferedImage _whiteQueen;
    /** Image of black queen. */
    private BufferedImage _blackQueen;

    /** True iff accepting moves from user. */
    private boolean _acceptingMoves;

    /** Image of selected queen. */
    private BufferedImage _selectedQueen;

    /** Image of possible moves. */
    private BufferedImage _dot;

    /** List of clicks. */
    private ArrayList<String> list = new ArrayList<>();
}

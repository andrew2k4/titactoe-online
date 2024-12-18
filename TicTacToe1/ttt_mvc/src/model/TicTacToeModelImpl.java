package model;

/**
 * This class provides an implementation of the TicTacToeModelInterface.
 * It represents our model with respect to the MVC pattern.
 */
public class TicTacToeModelImpl implements TicTacToeModelInterface, Cloneable {
    /**
     * Constant representing an empty cell on the board.
     */
    final static byte NONE = 0;

    /**
     * Constant representing player one.
     */
    final static byte ONE = 1;

    /**
     * Constant representing player two.
     */
    final static byte TWO = 22;

    /**
     * The current player.
     */
    byte player = ONE;

    /**
     * Number of rows in our game.
     */
    final byte rows = 3;
    /**
     * Number of columns in our game.
     */
    final byte columns = 3;

    @Override
    public byte[][] getB() {
        return b;
    }

    @Override
    public void setB(byte[][] b) {
        this.b = b;
    }

    /**
     * Game board represented as 2D array.
     */
    byte[][] b;

    /**
     * Track numer of moves made in game.
     */
    int movesDone = 0;

    /**
     * Flag for checking last win.
     */
    Boolean winsLast = null;

    /**
     * Returns the opposite player.
     *
     * @param p the player byte.
     * @return the byte representing the other player.
     */
    @Override
    public byte otherPlayer(byte p) {
        return (p==ONE)? TWO: ONE;
    }

    /**
     * Checks if the game has ended either by a win or draw.
     *
     * @return true if the game has ended, false otherwise.
     */
    @Override
    public boolean ended() {
        return noMoreMove() || wins();
    }

    /**
     * Checks if the last move resulted in a win.
     *
     * @return true if the last player won, false otherwise.
     */
    public boolean wins() {
        if (winsLast == null) {
            winsLast = wins(lastPlayer());
        }
        return winsLast;
    }

    /**
     * Gets the number of rows in the board.
     *
     * @return the number of rows.
     */
    @Override
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the board.
     *
     * @return the number of columns.
     */
    @Override
    public int getColumns() {
        return columns;
    }

    /**
     * Gets the byte representing player one.
     *
     * @return the byte representing player one.
     */
    @Override
    public byte getPlayerOne() {
        return ONE;
    }

    /**
     * Gets the byte representing player two.
     *
     * @return the byte representing player two.
     */
    @Override
    public byte getPlayerTwo() {
        return TWO;
    }

    /**
     * Gets the byte representing an empty cell.
     *
     * @return the byte representing an empty cell.
     */
    @Override
    public byte getPlayerNone() {
        return NONE;
    }

    /**
     * Gets the player at a specific board position.
     *
     * @param column the column index.
     * @param row the row index.
     * @return the player byte at the given position.
     */
    @Override
    public byte getAtPosition(byte column, byte row) {
        return b[column][row];
    }

    /**
     * Determines the next player to move.
     *
     * @return the byte representing the next player.
     */
    public byte nextPlayer() {
        return otherPlayer(player);
    }

    /**
     * Determines the last player who moved.
     *
     * @return the byte representing the last player.
     */
    public byte lastPlayer() {
        return otherPlayer(player);
    }

    /**
     * Constructor, initalizes the board.
     */
    public TicTacToeModelImpl() {
        b = new byte[columns][];
        for (int i = 0; i < columns; i++) {
            b[i] = new byte[rows];
        }
    }

    /**
     * Sets a move at a given position and returns a new game state.
     *
     * @param column the column index.
     * @param row the row index.
     * @return a new TicTacToeModelImpl object with the updated move.
     */
    public TicTacToeModelImpl setAtPosition(byte column, byte row, byte player) {
        return doMove(new Pair(column, row), player);
    }

    /**
     * Executes a move and returns a new game state.
     *
     * @param m the move represented as a Pair.
     * @return a new TicTacToeModelImpl reflecting the move.
     */
    @Override
    public TicTacToeModelImpl doMove(Pair m,byte player) {
        TicTacToeModelImpl result = clone();
        result.player = player;
        result.b[m.fst][m.snd] = player;
        result.movesDone = (byte) (movesDone + 1);
        return result;
    }

    /**
     * Checks if there are no more moves possible.
     *
     * @return true if there are no more moves possible, false otherwise.
     */
    @Override
    public boolean noMoreMove() {
        return this.getRows() * this.getColumns() == movesDone;
    }

    /**
     * Checks if the specified player has won the game.
     *
     * @param p the player byte to check for a win.
     * @return true if the player has won, false otherwise.
     */
    @Override
    public boolean wins(byte p) {
        return checkRows(p) || checkColums(p) || checkDiagonal1(p) || checkDiagonal2(p);
    }

    /**
     * Checks for a win in any row for the specified player.
     *
     * @param p the player byte to check for a row win.
     * @return true if the player has a winning row, false otherwise.
     */
    private boolean checkRows(byte p) {
        for (byte r = 0; r < getRows(); r++) {
            for (byte c = 0; c < getColumns(); c++) {
                if (getAtPosition(c, r) != p) {
                    break;
                }
                if (c == getColumns() - 1 ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks for a win in any column for the specified player.
     *
     * @param p the player byte to check for a column win.
     * @return true if the player has a winning column, false otherwise.
     */
    private boolean checkColums(byte p) {
        for (byte c = 0; c < getColumns(); c++) {
            for (byte r = 0; r < getRows(); r++) {
                if (getAtPosition(c, r) != p) {
                    break;
                }
                if (r == getRows() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks for a win in the first diagonal for the specified player.
     *
     * @param p the player byte to check for a diagonal win.
     * @return true if the player has a winning first diagonal, false otherwise.
     */
    private boolean checkDiagonal1(byte p) {
        for (byte r = 0; r < getRows(); r++) {
            if (getAtPosition(r, r) != p) {
                break;
            }
            if (r == getRows() - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for a win in the second diagonal for the specified player.
     *
     * @param p the player byte to check for a diagonal win.
     * @return true if the player has a winning second diagonal, false otherwise.
     */
    private boolean checkDiagonal2(byte p) {
        for (byte r = 0; r < getRows(); r++) {
            if (getAtPosition(r, (byte) (getRows() - r - 1)) != p) {
                break;
            }
            if (r == getRows() - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates and returns a copy of TicTacToeModelImpl.
     *
     * @return a new TicTacToeModelImpl object that is a copy of this instance.
     */
    @Override public TicTacToeModelImpl clone() {
        TicTacToeModelImpl result = null;
        try {
            result = (TicTacToeModelImpl) super.clone();
            result.b = b.clone();
            result.winsLast = null;
            for (int i = 0; i < result.b.length; i++) {
                result.b[i].clone();
            }
        } catch (CloneNotSupportedException ignored) {}
        return result;
    }
}

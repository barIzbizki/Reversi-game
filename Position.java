/**
 * Represents a position on the Reversi board.
 * A position is defined by a row and column on the board grid.
 */

public class Position {
    private final int row;
    private final int col;

    /**
     * Constructs a new Position instance.
     *
     * @param row the row index of the position on the board (0-7)
     * @param col the column index of the position on the board (0-7)
     */
    public Position(int row, int col) {
        isWithinBounds();
        this.row = row;
        this.col = col;
    }

    /**
     * Retrieves the row index of this position.
     *
     * @return the row index of the position
     */
    public int row() {
        return row;
    }

    /**
     * Retrieves the column index of this position.
     *
     * @return the column index of the position
     */
    public int col() {
        return col;
    }

    /**
     * Checks whether this position is equal to another position.
     * Two positions are considered equal if they have the same row and column.
     *
     * @param obj the object to compare with this position
     * @return true if the positions are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }

    /**
     * Checks if this position is within the valid bounds of the board (0-7 for both row and column).
     *
     * @return true if the position is within bounds, false otherwise
     */
    public boolean isWithinBounds() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    /**
     * Returns a string representation of this position.
     * This can be useful for debugging or logging.
     *
     * @return a string representing the position
     */
    @Override
    public String toString() {
        return "Position{" + "row=" + row + ", col=" + col + '}';
    }



}

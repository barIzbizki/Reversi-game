
public class Position {
    private final int row;
    private final int col;

    // Constructor
    public Position(int row, int col) {
        isWithinBounds();
        this.row = row;
        this.col = col;
    }

    // Getters for row and column
    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    // equals to compare positions based on row and col values
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }

    public boolean isWithinBounds() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    // Override toString for easier debugging
    @Override
    public String toString() {
        return "Position{" + "row=" + row + ", col=" + col + '}';
    }



}

/**
 * Represents a move in the Reversi game.
 * A move consists of a position on the board and the type of disc placed at that position.
 */

public class Move {
    private final Position position;
    private final Disc disc;

    /**
     * Constructs a new Move instance.
     *
     * @param position the position on the board where the move is made
     * @param disc     the disc placed at the specified position
     */
    public Move(Position position, Disc disc) {
        this.position = position;
        this.disc = disc;
    }

    /**
     * Retrieves the position of this move.
     *
     * @return the position associated with this move
     */
    public Position position() {
        return position;
    }

    /**
     * Retrieves the disc associated with this move.
     *
     * @return the disc placed as part of this move
     */
    public Disc disc() {
        return disc;
    }

    /**
     * Checks whether this move is equal to another object.
     * Two moves are considered equal if they have the same position and disc.
     *
     * @param obj the object to compare with this move
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return position.equals(move.position) && disc.equals(move.disc);
    }


    /**
     * Returns a string representation of the move.
     * Includes the position and the type of disc used in the move.
     *
     * @return a string describing the move
     */
    @Override
    public String toString() {
        return "Move{" + "position=" + position + ", disc=" + disc + '}';
    }
}

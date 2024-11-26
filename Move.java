
public class Move {
    private final Position position;
    private final Disc disc;


    public Move(Position position, Disc disc) {
        this.position = position;
        this.disc = disc;
    }

    // return the position of the move
    public Position position() {
        return position;
    }

    // return the disc of the move
    public Disc disc() {
        return disc;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return position.equals(move.position) && disc.equals(move.disc);
    }

    @Override
    public String toString() {
        return "Move{" + "position=" + position + ", disc=" + disc + '}';
    }
}

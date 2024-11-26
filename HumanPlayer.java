/**
 * HumanPlayer represents a human player in the game.
 */
public class HumanPlayer extends Player {

    /**
     * Creates a new HumanPlayer with the specified player type (Player 1 or Player 2).
     */
    public HumanPlayer(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * return true if the player is human.
     */
    @Override
    public boolean isHuman() {
        return true;
    }

    /**
     * return string of the player
     */
    @Override
    public String toString() {
        return "HumanPlayer{" +
                "isPlayerOne=" + isPlayerOne +
                ", wins=" + wins +
                ", bombs=" + number_of_bombs +
                ", unflippable=" + number_of_unflippedable +
                '}';
    }
}


/**
 * Represents a human player in the Reversi game.
 * This class extends the Player class and defines specific behavior for human-controlled players.
 */
public class HumanPlayer extends Player {

    /**
     * Constructs a new HumanPlayer instance.
     *
     * @param isPlayerOne a boolean indicating whether this player is Player One (true) or Player Two (false)
     */
    public HumanPlayer(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * Determines whether this player is a human player.
     *
     * @return true, as this class represents a human player
     */
    @Override
    public boolean isHuman() {
        return true;
    }

    /**
     * Returns a string representation of the HumanPlayer, including player details.
     *
     * @return a string describing the player, including their type (Player One or Player Two),
     *         number of wins, remaining BombDiscs, and UnflippableDiscs
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


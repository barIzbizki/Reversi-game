import java.util.List;
import java.util.Random;

/**
 * RandomAI represents an artificial intelligence player that makes random moves.
 * The AI randomly chooses from valid moves and may select different types of discs,
 * including BombDisc and UnflippableDisc, based on availability.
 */

public class RandomAI extends AIPlayer {

    /**
     * Creates a new RandomAI player with the specified player type (Player 1 or Player 2).
     *
     * @param isPlayerOne a boolean indicating whether the player is Player 1
     */
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * Makes a move by selecting a random valid move from the available moves.
     * It may also randomly choose to use a BombDisc or UnflippableDisc, if available.
     *
     * @param gameStatus the current game state, which includes the valid moves and other game information
     * @return a Move object representing the randomly chosen move, or null if no valid moves are available
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> validMoves = gameStatus.ValidMoves();
        if (validMoves.isEmpty()) {
            return null; // No valid moves, return null
        }

        Random random = new Random();
        Position randomMove = validMoves.get(random.nextInt(validMoves.size())); // Pick a random valid move

        // Randomly decide the type of disc to use
        Disc chosenDisc;
        if (getNumber_of_bombs() > 0 && random.nextBoolean()) {
            // Use a BombDisc if available and randomly chosen
            chosenDisc = new BombDisc(this);
            //reduce_bomb(); // Reduce the number of available bombs
        } else if (getNumber_of_unflippedable() > 0 && random.nextBoolean()) {
            // Use an UnflippableDisc if available and randomly chosen
            chosenDisc = new UnflippableDisc(this);
           // reduce_unflippedable(); // Reduce the number of available unflippable discs
        } else {
            chosenDisc = new SimpleDisc(this); // Default to a SimpleDisc
        }

        return new Move(randomMove, chosenDisc); // Return the random move with the selected disc
    }

}

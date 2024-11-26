import java.util.List;

/**
 * Represents a GreedyAI player in the Reversi game.
 * This AI player makes moves based on a greedy strategy, prioritizing moves that maximize the number of flipped opponent discs.
 * It can also use special discs like BombDisc and UnflippableDisc when applicable.
 */
public class GreedyAI extends AIPlayer {


    /**
     * Constructs a GreedyAI player.
     *
     * @param isPlayerOne a boolean indicating whether this player is Player One (true) or Player Two (false)
     */
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * Determines the best move for the GreedyAI player based on the current game state.
     * The move is selected by evaluating all valid moves and choosing the one that flips the most opponent discs.
     * The AI may choose to use a BombDisc or an UnflippableDisc if certain conditions are met.
     *
     * @param gameStatus the current game state, represented by an implementation of the PlayableLogic interface
     * @return a Move object representing the chosen move and the type of disc to be placed,
     *         or null if no valid moves are available
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> validMoves = gameStatus.ValidMoves();
        if (validMoves.isEmpty()) {
            return null; // No valid moves, return null
        }

        int maxFlips = -1;
        Position bestMove = null;
        boolean useBomb = false;

        // Iterate through all valid moves to find the one with the maximum flips
        for (Position move : validMoves) {
            int flips = gameStatus.countFlips(move); // Count flips for the current move
            if (flips > maxFlips) {
                maxFlips = flips; // Update the maximum flips
                bestMove = move;  // Set the best move
            }

            // Decide to use a BombDisc if surrounded by many opponent discs
            if (gameStatus.countFlips(move) > 3 && getNumber_of_bombs() > 0) {
                useBomb = true;
            }
        }

        // Choose the type of disc for the move
        Disc chosenDisc;
        if (useBomb) {
            chosenDisc = new BombDisc(this); // Use a BombDisc
            //reduce_bomb(); // Reduce the number of available bombs
        } else if (getNumber_of_unflippedable() > 0) {
            chosenDisc = new UnflippableDisc(this); // Use an UnflippableDisc
            //reduce_unflippedable(); // Reduce the number of available unflippable discs
        } else {
            chosenDisc = new SimpleDisc(this); // Default to a SimpleDisc
        }

        return new Move(bestMove, chosenDisc); // Return the best move with the selected disc
    }

}

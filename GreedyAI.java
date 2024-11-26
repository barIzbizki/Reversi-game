import java.util.List;

public class GreedyAI extends AIPlayer {


    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

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

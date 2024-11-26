import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer {


    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

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

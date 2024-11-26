import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class GameLogic implements PlayableLogic {
    private Player player1;
    private Player player2;
    private Disc[][] board; // the board of the game
    private final List<Move> moveHistory; // list of game moves
    private static final int BOARD_SIZE = 8; // the normal size of the board
    private boolean isFirst = true; // Indicates if it's the first player's turn
    private final Stack<List<Disc>> FlipsHistory;// Stack of lists to store flipped discs due to bomb discs
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};



    public GameLogic() {
        moveHistory = new ArrayList<>();
        board = new Disc[BOARD_SIZE][BOARD_SIZE];
        FlipsHistory = new Stack<>();
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        Player currentPlayer = isFirstPlayerTurn() ? player1 : player2;
        if (isMoveValid(a)) {
            placeDisc(a, disc);
            if (getDiscAtPosition(a) == null) { // if place a disc wasn't successful
                return false;
            }

         //print
            System.out.println("Player " + (currentPlayer.isPlayerOne() ? "1" : "2") +
                    " placed a " + disc.getType() +
                    " in (" + a.row() + ", " + a.col() + ")");


            flipDiscs(a);
            System.out.println("");
            moveHistory.add(new Move(a, disc));
            isFirst = !isFirst;

            return true; // if the move was successful
        }
        return false; // if not
    }

    /**
     * Return the disc at the given position
     */
    @Override
    public Disc getDiscAtPosition(Position position) {
        return board[position.row()][position.col()];
    }

    /**
     * Return the size of the board (8x8)
     */
    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * @param
     * @return
     */
    @Override
    public List<Position> ValidMoves() {
        List<Position> validMoves = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Position pos = new Position(row, col);
                if (isMoveValid(pos)) {
                    validMoves.add(pos);// Add the valid position to the list
                }
            }
        }
        return validMoves;
    }

    /**
     * Count the number of discs that would be flipped for the given position
     */
    @Override
    public int countFlips(Position a) {
        return countOrFlip(a.row(), a.col(),false);
    }
    /**
     * Return the first player based on their 'isPlayerOne' status
     */
    @Override
    public Player getFirstPlayer() {
        if(player1.isPlayerOne()){
            return player1;
        }
        return player2;
    }

    /**
     * Return the second player based on their 'isPlayerOne' status
     */
    @Override
    public Player getSecondPlayer() {
        if(player1.isPlayerOne()){
            return player2;
        }
        return player1;
    }

    /**
     * Set the players for the game
     */
    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

    }

    /**
     * Return true if it's the first player's turn
     */
    @Override
    public boolean isFirstPlayerTurn() {
        return isFirst;
    }

    /**
     * The game is finished if neither player has valid moves - and update the winner
     */
    @Override
    public boolean isGameFinished() {
        // if one of the players has  more legal moves the game is not finish
        if (hasValidMove(player1) || hasValidMove(player2)) {
            return false;
        }

        // counting the number of discs for each player
        int player_1_discs = 0;
        int player_2_discs = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null) {
                    if (board[i][j].getOwner() == player1) {
                        player_1_discs++;
                    } else {
                        player_2_discs++;
                    }
                }
            }
        }

        // check who won
        String winner = player_1_discs >= player_2_discs ? "1" : "2";

        // adding the win to the winning payer
        if (winner.equals("1")) {
            player1.addWin();
        } else {
            player2.addWin();
        }
        // print
        System.out.printf("Player %s wins with %d discs! Player %s had %d discs.%n",
                winner,
                winner.equals("1") ? player_1_discs : player_2_discs,
                winner.equals("1") ? "2" : "1",
                winner.equals("1") ? player_2_discs : player_1_discs);
        return true; // the game is finished
    }


    /**
     * Reset the game state to its initial configuration
     */
    @Override
    public void reset() {
        board = new Disc[BOARD_SIZE][BOARD_SIZE]; // create new bord
        moveHistory.clear(); // Clear the move history
        FlipsHistory.clear(); // Clear the stack of bomb flips
        player1.reset_bombs_and_unflippedable();
        player2.reset_bombs_and_unflippedable();
        initializeBoard(); // Re-initialize the board with the starting position
        isFirst =true;// the first turn is player1

    }

    /**
     * Undo the last move and restore the board to its previous state
     */
    @Override
    public void undoLastMove() {
        if ((player1 instanceof HumanPlayer) && (player2 instanceof HumanPlayer)) {
            if (!moveHistory.isEmpty()) {
                System.out.println("Undoing last move:");

                // removing the last move from the history of moves
                Move lastMove = moveHistory.remove(moveHistory.size() - 1);
                Position lastPosition = lastMove.position();

                // removing the last disc placed from the board
                Disc removedDisc = board[lastPosition.row()][lastPosition.col()];

                //print
                String removedDiscType = removedDisc.getType();
                System.out.printf("\tUndo: removing '%s' from (%d, %d)%n",
                        removedDiscType, lastPosition.row(), lastPosition.col());
                board[lastPosition.row()][lastPosition.col()] = null;

                // recovering discs that were overturned in the last move
                if (!FlipsHistory.isEmpty()) {
                    List<Disc> flippedDiscs = FlipsHistory.pop();
                    for (Disc flippedDisc : flippedDiscs) {
                        Player currentOwner = flippedDisc.getOwner();
                        Player newOwner = currentOwner == player1 ? player2 : player1;
                        flippedDisc.setOwner(newOwner);

                        // prnit
                        Position flippedPosition = find_disc(flippedDisc);
                        String flippedDiscType = flippedDisc.getType();
                        System.out.printf("\tUndo: flipping back '%s' in (%d, %d)%n",
                                flippedDiscType, flippedPosition.row(), flippedPosition.col());
                    }
                    System.out.println();
                }

                // if the disc we removed from the board was a bomb - update the number of bombs for the current player
                if (lastMove.disc() instanceof BombDisc) {
                    lastMove.disc().getOwner().increase_bomb();
                }
                // if the disc we removed from the board was a UnflippableDisc - update the number of UnflippableDisc for the current player
                if (lastMove.disc() instanceof UnflippableDisc) {
                    lastMove.disc().getOwner().increase_Unflippedable();
                }

                // pass the turn
                isFirst = !isFirst;
            } else {
                System.out.println("\tNo previous move available to undo.");
                System.out.println();
            }
        }
    }


    /**
     * Initialize the board to its starting state with two discs for each player
     */
    private void initializeBoard() {
        if (player1 == null || player2 == null) {
            throw new IllegalStateException("Players must be set before initializing the board.");
        }
        int mid = BOARD_SIZE / 2;
        board[mid - 1][mid - 1] = new SimpleDisc(player1); // First player's disc at (3,3)
        board[mid][mid] = new SimpleDisc(player1); // First player's disc at (4,4)
        board[mid - 1][mid] = new SimpleDisc(player2); // Second player's disc at (3,4)
        board[mid][mid - 1] = new SimpleDisc(player2);  // Second player's disc at (4,3)
    }

    /**
     * Check if the move is valid: position must be empty, within bounds, and flip at least one disc
     */
    private boolean isMoveValid(Position pos) {
        return isInBounds(pos.row(), pos.col()) && board[pos.row()][pos.col()] == null && countFlips(pos) > 0;
    }

    // Place a disc at the specified position
    private void placeDisc(Position pos, Disc disc) {
        if (disc == null || disc.getOwner() == null) {
            return; // Exit the method if the disc or owner is null
        }

        Player currentPlayer = isFirstPlayerTurn() ? player1 : player2;

        if (disc instanceof BombDisc && currentPlayer.getNumber_of_bombs() > 0) {
            currentPlayer.reduce_bomb();
            board[pos.row()][pos.col()] = disc;
        } else if (disc instanceof UnflippableDisc && currentPlayer.getNumber_of_unflippedable() > 0) {
            currentPlayer.reduce_unflippedable();
            board[pos.row()][pos.col()] = disc;
        } else if (!(disc instanceof BombDisc) && !(disc instanceof UnflippableDisc)) {
            // Place a simple disc on the board
            board[pos.row()][pos.col()] = disc;
        }
    }

    /**
     * Flip the discs surrounding the placed disc and record the flips
     */
    private void flipDiscs(Position pos) {
        ArrayList<Disc> flippedDiscs = new ArrayList<>();
        countOrFlip(pos.row(), pos.col(), true);

        // Print the flipped discs
        for (Disc flippedDisc : flippedDiscs) {
            Position flippedPosition = find_disc(flippedDisc);
            String discType = flippedDisc instanceof BombDisc ? "bomb disc"
                    : flippedDisc instanceof UnflippableDisc ? "unflippable disc"
                    : "simple disc";
            int playerNumber = flippedDisc.getOwner() == player1 ? 1 : 2;
            System.out.printf("Player %d flipped the %s in (%d, %d)%n",
                    playerNumber, discType, flippedPosition.row(), flippedPosition.col());
        }
    }

    /**
     * the function receives a boolean value and position on the board.
     * if the boolean value is true-turns the discs according to the position that the function received
     * if the boolean value is false-counts and return the number of flips according the position
     */
    private int countOrFlip(int dx, int dy, boolean toFlip) {
        ArrayList<Disc> needToFlip = new ArrayList<>();
        ArrayList<Disc> allFlippedDiscs = new ArrayList<>(); // the finel list of discs that fliped in this turn

        for (int[] direction : DIRECTIONS) {
            Player currentPlayer = isFirstPlayerTurn() ? player1 : player2;
            int x = dx + direction[0];
            int y = dy + direction[1];
            ArrayList<Disc> flipD = new ArrayList<>();// a list of the discs that will fliped in the dirction

            while (isInBounds(x, y) && board[x][y] != null && board[x][y].getOwner() != currentPlayer) {
                Disc d = board[x][y];
                // if the disc is not an unflipeddisc
                if (!(d instanceof UnflippableDisc)) {
                    flipD.add(board[x][y]);
                }
                x += direction[0];
                y += direction[1];
            }

            // Validate the sequence to ensure it ends with the current player's piece
            if (!isInBounds(x, y) || board[x][y] == null || board[x][y].getOwner() != currentPlayer) {
                flipD.clear();  // Reset flips if not bounded by the player's piece
            }

            if (!flipD.isEmpty()) {
                for (int i = 0; i < flipD.size(); i++) {
                    Position a = find_disc(flipD.get(i));

                    // if the disc that we want to flip is a bomb - flip all the discs around
                    if (flipD.get(i) instanceof BombDisc) {
                        countOrFlipSurroundFlips(a.row(), a.col(), currentPlayer, flipD);
                    }
                }
            }

            // if we want to flip ,and we have discs to flip
            if (toFlip && !flipD.isEmpty()) {
                for (Disc disc : flipD) {
                    disc.setOwner(currentPlayer); // flip the disc
                }
                allFlippedDiscs.addAll(flipD); // add the disc that we fliped to the list of all the discs that we fliped in this turn

                //print
                for (Disc disc : flipD) {
                    Position pos = find_disc(disc); // find the position of the disc
                    String discType = disc instanceof BombDisc ? "BombDisc" :
                            disc instanceof UnflippableDisc ? "UnflippableDisc" :
                                    "SimpleDisc";
                    System.out.printf("Player %d flipped the %s in (%d, %d)%n",
                            currentPlayer == player1 ? 1 : 2,
                            disc.getType(),
                            pos.row(), pos.col());
                    System.out.printf("");
                }

            }

            for (Disc disc : flipD) {
                // add to the list that count how many will be flip
                if (!needToFlip.contains(disc)) {
                    needToFlip.add(disc);
                }
            }
        }

        // insert the list of all the discs that fliped in this turn to the history
        if (toFlip && !allFlippedDiscs.isEmpty()) {
            FlipsHistory.push(allFlippedDiscs); // add the list of all the discs that fliped to the history
        }
        return needToFlip.size();
    }

    /**
     *the function receives the position of the bomb and count or adding the 8 disc around the bomb to the list to flip
     */
    private int countOrFlipSurroundFlips(int bombX, int bombY, Player currentPlayer, ArrayList<Disc> flipped_disc) {
        int flipCount = 0;

        for (int[] direction : DIRECTIONS) {
            int x = bombX + direction[0];
            int y = bombY + direction[1];

            if (isInBounds(x, y) && board[x][y] != null && board[x][y].getOwner() != currentPlayer && !(board[x][y] instanceof UnflippableDisc) && !flipped_disc.contains(board[x][y])) {
                if (board[x][y] instanceof BombDisc) {
                    flipped_disc.add(board[x][y]);
                    flipCount += countOrFlipSurroundFlips(x, y, currentPlayer, flipped_disc) + 1;
                } else {
                    flipped_disc.add(board[x][y]);
                    flipCount++;
                }
            }
        }

        return flipCount;
    }

    /**
     * the function receives a disc and return the position of the disc
     */
    private Position find_disc(Disc disc){
        Position pos = null;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == disc){
                    pos = new Position(i,j);
                }
            }
        }
        return pos;
    }


    /**
     * Check if the given coordinates are within the board's bounds
     */
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    /**
     * ?
     */
    private boolean hasValidMove(Player player) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (isMoveValid(new Position(row, col))) {
                    return true;
                }
            }
        }
        return false;
    }
}
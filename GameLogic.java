import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * The GameLogic class implements the PlayableLogic interface and handles the rules and logic of the Reversi game.
 * It manages players, the game board, moves, and other core functionalities.
 */

public class GameLogic implements PlayableLogic {
    private Player player1;
    private Player player2;
    private Disc[][] board; // the board of the game
    private final List<Move> moveHistory; // list of game moves
    private static final int BOARD_SIZE = 8; // the normal size of the board
    private boolean isFirst = true; // Indicates if it's the first player's turn
    private final Stack<List<Disc>> FlipsHistory;// Stack of lists to store flipped discs due to bomb discs
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    /**
     * Constructs a new GameLogic instance with an empty board and history.
     */
    public GameLogic() {
        moveHistory = new ArrayList<>();
        board = new Disc[BOARD_SIZE][BOARD_SIZE];
        FlipsHistory = new Stack<>();
    }

    /**
     * Places a disc at the specified position if the move is valid.
     *
     * @param a the position to place the disc.
     * @param disc the disc to be placed.
     * @return true if the move is successful, false otherwise.
     */
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
     * Returns the disc located at the specified position.
     *
     * @param position the position to check.
     * @return the disc at the given position or null if the position is empty.
     */
    @Override
    public Disc getDiscAtPosition(Position position) {
        return board[position.row()][position.col()];
    }

    /**
     * Returns the size of the board.
     *
     * @return the size of the board (e.g., 8 for a 8x8 board).
     */
    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * Returns a list of all valid moves for the current player.
     *
     * @return a list of valid positions where the current player can place a disc.
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
     * Counts the number of discs that would be flipped if a disc is placed at the given position.
     *
     * @param a the position to check.
     * @return the number of discs that would be flipped.
     */
    @Override
    public int countFlips(Position a) {
        return countOrFlip(a.row(), a.col(),false);
    }

    /**
     * Returns the first player in the game.
     *
     * @return the first player.
     */
    @Override
    public Player getFirstPlayer() {
        if(player1.isPlayerOne()){
            return player1;
        }
        return player2;
    }

    /**
     * Returns the second player in the game.
     *
     * @return the second player.
     */
    @Override
    public Player getSecondPlayer() {
        if(player1.isPlayerOne()){
            return player2;
        }
        return player1;
    }

    /**
     * Sets the two players for the game.
     *
     * @param player1 the first player.
     * @param player2 the second player.
     */
    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

    }

    /**
     * Checks if it's the first player's turn.
     *
     * @return true if it's the first player's turn, false otherwise.
     */
    @Override
    public boolean isFirstPlayerTurn() {
        return isFirst;
    }

    /**
     * Checks if the game is finished.
     * The game ends if neither player has a valid move.
     *
     * @return true if the game is finished, false otherwise.
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
     * Resets the game state to its initial configuration.
     * This method clears the board, move history, and any special disc effects,
     * resets player-specific attributes (such as BombDisc and UnflippableDisc counts),
     * and re-initializes the board to the starting position of the game.
     * The first turn is assigned to Player 1.
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
     * Undoes the last move in the game if both players are human.
     * Removes the last placed disc, restores flipped discs to their original owners,
     * and updates the BombDisc or UnflippableDisc counts if applicable.
     * Switches the turn back to the previous player.
     *
     * @return true if a move was undone successfully, false if no moves are available to undo
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
     * Initializes the board to the starting state with two discs for each player.
     * Ensures the board is ready for a new game and that both players are properly set.
     *
     */
    private void initializeBoard() {
        if (player1 == null || player2 == null) {
            return;
        }
        int mid = BOARD_SIZE / 2;
        board[mid - 1][mid - 1] = new SimpleDisc(player1); // First player's disc at (3,3)
        board[mid][mid] = new SimpleDisc(player1); // First player's disc at (4,4)
        board[mid - 1][mid] = new SimpleDisc(player2); // Second player's disc at (3,4)
        board[mid][mid - 1] = new SimpleDisc(player2);  // Second player's disc at (4,3)
    }

    /**
     * Checks if a move at the specified position is valid.
     * A move is valid if the position is empty, within bounds, and flips at least one opponent disc.
     *
     * @param pos the position to check
     * @return true if the move is valid, false otherwise
     */
    private boolean isMoveValid(Position pos) {
        return isInBounds(pos.row(), pos.col()) && board[pos.row()][pos.col()] == null && countFlips(pos) > 0;
    }

    /**
     * Places a disc on the board at the specified position if the move is valid.
     * Updates the player's BombDisc or UnflippableDisc count if such a disc is placed.
     *
     * @param pos  the position on the board to place the disc
     * @param disc the disc to be placed
     * @return true if the disc was successfully placed, false otherwise
     */
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
     * Flips opponent discs affected by the most recent move, following the rules of Reversi.
     * Records flipped discs for potential undo operations.
     *
     * @param pos the position of the newly placed disc
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
     * Counts or flips discs in all valid directions from the given position.
     * Traverses the board to determine which discs should be flipped or calculates their count.
     *
     * @param dx      the row coordinate of the starting position
     * @param dy      the column coordinate of the starting position
     * @param toFlip  true to flip discs, false to count flips
     * @return the number of discs flipped or that could be flipped
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
     * Counts or flips discs surrounding a BombDisc at the specified position.
     * Applies cascading effects if adjacent BombDiscs are triggered.
     *
     * @param bombX         the row coordinate of the BombDisc
     * @param bombY         the column coordinate of the BombDisc
     * @param currentPlayer the current player
     * @param flipped_disc  the list of discs already flipped
     * @return the total number of discs flipped by the BombDisc effect
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
     * Finds the position of a specific disc on the board.
     *
     * @param disc the disc to locate
     * @return the position of the disc, or null if the disc is not found
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
     * Checks if the specified coordinates are within the board's bounds.
     *
     * @param x the row coordinate
     * @param y the column coordinate
     * @return true if the coordinates are within bounds, false otherwise
     */
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    /**
     * Determines if the specified player has at least one valid move on the board.
     *
     * @param player the player to check for valid moves
     * @return true if the player has a valid move, false otherwise
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
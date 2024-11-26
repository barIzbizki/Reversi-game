/**
 * Represents a BombDisc in the Reversi game.
 * A BombDisc has a specific owner and a unique type symbol ("💣").
 * When placed, it can trigger special flipping mechanics affecting adjacent discs.
 */

public class BombDisc implements Disc {
    private Player owner;

    /**
     * Constructs a BombDisc with the specified owner.
     *
     * @param owner the player who owns this BombDisc
     */

    public BombDisc(Player owner) {
        this.owner = owner;
    }

    /**
     * Retrieves the owner of this BombDisc.
     *
     * @return the player who owns this BombDisc
     */
    @Override
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this BombDisc to the specified player.
     *
     * @param player the new owner of this BombDisc
     */
    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    /**
     * Retrieves the type symbol of this BombDisc.
     * The type symbol for a BombDisc is "💣".
     *
     * @return a string representing the type of this disc
     */
    @Override
    public String getType() {
        return "💣"; // Represents a Bomb Disc
    }
}
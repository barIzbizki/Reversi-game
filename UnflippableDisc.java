/**
 * UnflippableDisc represents a disc that cannot be flipped, owned by a specific player.
 * This disc is not affected by the normal flipping rules in the game.
 */

public class UnflippableDisc implements Disc {
    private Player owner;

    /**
     * Creates a new UnflippableDisc with the specified owner.
     *
     * @param owner the player who owns the disc
     */
    public UnflippableDisc(Player owner) {
        this.owner = owner;
    }

    /**
     * Returns the owner of the disc.
     *
     * @return the player who owns the disc
     */
    @Override
    public Player getOwner() {
        return owner;
    }

    /**
     * Changes the owner of the disc to the specified player.
     *
     * @param player the player who will own the disc
     */
    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    /**
     * Returns a string representation of the UnflippableDisc.
     *
     * @return the symbol for the UnflippableDisc
     */
    @Override
    public String getType() {
        return "⭕"; // Represents an Unflippable Disc
    }
}
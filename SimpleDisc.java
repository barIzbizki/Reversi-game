/**
 * SimpleDisc represents a basic disc in the game, owned by a specific player.
 * This disc can be flipped to represent ownership changes between players.
 */
public class SimpleDisc implements Disc {
    private Player owner; // return the owner of the disc

    /**
     * Creates a new SimpleDisc with the specified owner.
     *
     * @param owner the player who owns the disc
     */
    public SimpleDisc(Player owner) {
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
     * Returns a string representation of the SimpleDisc.
     *
     * @return the symbol for the SimpleDisc
     */
    @Override
    public String getType() {
        return "⬤";
    }

}


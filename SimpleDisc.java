/**
 * SimpleDisc represents a basic disc in the game, owned by a specific player.
 * This disc can be flipped to represent ownership changes between players.
 */
public class SimpleDisc implements Disc {
    private Player owner; // return the owner of the disc


    public SimpleDisc(Player owner) {
        this.owner = owner;
    }

    /**
     *return the owner of the disc
     */
    @Override
    public Player getOwner() {
        return owner;
    }

    /**
     * change the owner if the disc
     */
    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    /**
     * return the string of the simple disc.
     */
    @Override
    public String getType() {
        return "⬤";
    }

}


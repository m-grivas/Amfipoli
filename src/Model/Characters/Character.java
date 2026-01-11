package Model.Characters;

import Model.*;

/**
 * Character is the abstract class for all characters
 *
 */
public abstract class Character implements Colored {

    /**
     * The color matching the players colors for dealing
     */
    private final Color color;

    private final Player player;

    /**
     * Whether the character ability can be used or not
     */
    private boolean usable;

    /**
     * Create a new character with a usable ability
     */
    public Character(Color c, Player p) {
        this.usable = true;
        this.color = c;
        this.player = p;
    }

    /**
     *
     * @return the color of the character card
     */
    public Color getColor() {
        return this.color;
    }

    public Player getPlayer() {
        return this.player;
    }

    /**
     *
     * @return ability status
     */
    public boolean getAvailability() {
        return this.usable;
    }

    public void setAvailability(boolean a) {
        this.usable = a;
    }

    /**
     * Implemented by each character subclass
     */
    public abstract int useAbility(AreaType previousArea, Board board, Player player);
}

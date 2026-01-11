package Model.Characters;

import Model.AreaType;
import Model.Board;
import Model.Color;
import Model.Player;

public class Coder extends Character {

    /**
     * Create a new character with a usable ability
     *
     * @param c the color of the character
     * @param p the player owner of the character
     */
    public Coder(Color c, Player p) {
        super(c, p);
    }

    /**
     * Chooses an area and at his next turn he picks 2 tiles from that area
     */
    @Override
    public int useAbility(AreaType previousArea, Board board, Player player) {

        return 0;
    }

    @Override
    public void setColor(Color c) {

    }
}

package Model.Characters;

import Model.AreaType;
import Model.Board;
import Model.Color;
import Model.Player;


public class Assistant extends Character {


    /**
     * Create a new character with a usable ability
     *
     * @param c the color of the character
     */
    public Assistant(Color c, Player p) {
        super(c, p);

    }

    /**
     * Picks 1 tile form whatever area
     * @return the number of tiles the user is allowed to pick
     */
    @Override
    public int useAbility(AreaType previousArea, Board board, Player player) {
        board.setAllAreasAvailable(true);
        return 1;
    }

    @Override
    public void setColor(Color c) {

    }
}

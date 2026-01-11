package Model.Characters;

import Model.AreaType;
import Model.Board;
import Model.Color;
import Model.Player;

public class Digger extends Character {

    /**
     * Create a new character with a usable ability
     *
     * @param c the color of the player
     * @param p the player owner of the player
     */
    public Digger(Color c, Player p) {
        super(c, p);
    }

    /**
     * Picks at most 2 tiles from the area that the player choose earlier at his turn
     */
    @Override
    public int useAbility(AreaType previousArea, Board board, Player player) {
        board.setAllAreasAvailable(false);
        board.setAreaAvailability(previousArea, true);

        return 2;
    }


    @Override
    public void setColor(Color c) {

    }
}

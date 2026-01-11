package Model.Characters;

import Model.AreaType;
import Model.Board;
import Model.Color;
import Model.AreaType.*;
import Model.Player;

public class Archeologist extends Character {

    /**
     * Create a new character with a usable ability
     *
     * @param c the color of the player who has this character
     */
    public Archeologist(Color c, Player p) {
        super(c, p);
    }

    /**
     * Picks 2 tiles from whatever area except the one the player choose earlier at his turn
     */
    @Override
    public int useAbility(AreaType previousArea, Board board, Player player) {
        board.setAllAreasAvailable(true);
        board.setAreaAvailability(previousArea, false);

        return 2;
    }

    @Override
    public void setColor(Color c) {

    }
}

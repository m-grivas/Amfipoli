package Model.Characters;

import Model.AreaType;
import Model.Board;
import Model.Color;
import Model.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Professor extends Character implements ActionListener {


    /**
     * Create a new character with a usable ability
     *
     * @param c the color of the character
     * @param p the player owner of the character
     */
    public Professor(Color c, Player p) {
        super(c, p);
    }

    /**
     * Picks a tile from each area except the one the player choose earlier at his turn
     */
    @Override
    public int useAbility(AreaType previousArea, Board board, Player player) {
        board.setAllAreasAvailable(true);
        board.setAreaAvailability(previousArea, false);



        return 3;
    }

    @Override
    public void setColor(Color c) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

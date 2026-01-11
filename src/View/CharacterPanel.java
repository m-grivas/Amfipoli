package View;

import Model.Characters.Character;
import Model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CharacterPanel extends JPanel {

    private Player player;
    private final List<CharacterView> characters;

    /**
     * Constructor
     * @param p the player that owns these characters
     */
    public CharacterPanel(Player p, ActionListener l) {
        this.player = p;
        this.characters = new ArrayList<>();


        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));


        setLayout(new GridLayout(0, 2, 10, 10));


        for (Character c : player.getCharacters()) {
            CharacterView view = new CharacterView(c, l);


            view.setFocusPainted(false);
            view.setBorderPainted(true);
            view.setContentAreaFilled(false);
            view.setOpaque(false);

            characters.add(view);
            add(view);
        }
    }

    /**
     * Set all characters enabled or disabled.
     * When disabled, the disabled icon remains the same as the default (handled in CharacterView).
     */
    public void enableCharacters(boolean status) {
        for (CharacterView c : characters) {
            c.setEnabled(status);
        }
    }

    /**
     * Set character disabled, changes the disabled icon as well
     * @param character the character to disable
     */
    public void setUsedCharacter(Character character) {
        for (CharacterView c : characters) {
            if (character == c.getCorrespondingCharacter()) {
                c.setUsedCharacter();
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Update the character availability based on the current player.
     * (Keeps your original logic: if not available, mark used.)
     */
    public void update(Player p) {
        for (CharacterView c : characters) {
            if (!c.getCorrespondingCharacter().getAvailability()) {
                c.setUsedCharacter();
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Setter for player
     */
    public void setPlayer(Player p) {
        player = p;
    }

    public Player getPlayer() {
        return player;
    }
}

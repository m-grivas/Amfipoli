package View;

import javax.swing.*;
import Model.Characters.*;
import Model.Characters.Character;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class CharacterView extends JButton {

    private final int CHARACTER_X = 170;
    private final int CHARACTER_Y = 250;


    private boolean available;
    private final Character correspondingCharacter;
    private final ImageIcon icon;

    /**
     * Loads the character image according to character type
     * @param c the character to generate the image for
     * @param l the listener of the object (controller class)
     */
    public CharacterView(Character c, ActionListener l) {
        available = true;

        correspondingCharacter = c;

        setPreferredSize(new Dimension(CHARACTER_X, CHARACTER_Y));

        URL imageURL = null;
        if (c instanceof Archeologist) {
            imageURL = TileView.class.getResource("/project_assets/images/archaeologist.png");
        }
        else if (c instanceof Assistant) {
            imageURL = TileView.class.getResource("/project_assets/images/assistant.png");
        }
        else if (c instanceof Coder) {
            imageURL = TileView.class.getResource("/project_assets/images/coder.PNG");
        }
        else if (c instanceof Digger) {
            imageURL = TileView.class.getResource("/project_assets/images/digger.png");
        }
        else if (c instanceof Professor) {
            imageURL = TileView.class.getResource("/project_assets/images/professor.png");
        }

        if (imageURL != null) {
            icon = new ImageIcon(imageURL);
            setIcon(icon);
            setDisabledIcon(icon);
        } else {
            icon = null;
            System.err.println("Image for tile" + c.toString() + " not found.");
        }

        addActionListener(l);
        setActionCommand("CHARACTER_PRESSED");

    }

    /**
     * Setter for available
     * @param a new available value
     */
    public void setAvailable(boolean a) {
        available = a;
    }

    /**
     * Set the current character disabled, and changes the disabled icon as well
     */
    protected void setUsedCharacter() {
        setEnabled(false);
        setDisabledIcon(null);
        revalidate();
        repaint();
    }

    public Character getCorrespondingCharacter() {
        return this.correspondingCharacter;
    }

    /**
     * getter for available
     * @return available
     */
    public boolean isAvailable() {
        return available;
    }
}

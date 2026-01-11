package View;

import Model.AreaType;
import Model.Tiles.*;
import static Model.AreaType.*;

import static Model.Color.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TileView extends JButton {

    private final int TILE_X = 50;
    private final int TILE_Y = 50;

    private Tile source;
    private final ImageIcon icon;
    private final AreaType correspondingArea;

    /**
     * Choose the correct icon for the tile button
     * @param t the tile to create the image
     * @param l the action listener of the object (controller)
     */
    protected TileView(Tile t, ActionListener l) {

        source = t;


        URL imageURL = null;
        if (t instanceof LandslideTile) {
            this.correspondingArea = null;
            imageURL = TileView.class.getResource("/project_assets/images/landslide.png");
        }
        else if (t instanceof AmphoraTile) {
            this.correspondingArea = AMPHORA_AREA;

            switch (((AmphoraTile) t).getColor()) {
                case BLUE:
                    imageURL = TileView.class.getResource("/project_assets/images/amphora_blue.png");
                    break;
                case BROWN:
                    imageURL = TileView.class.getResource("/project_assets/images/amphora_brown.png");
                    break;
                case GREEN:
                    imageURL = TileView.class.getResource("/project_assets/images/amphora_green.png");
                    break;
                case PURPLE:
                    imageURL = TileView.class.getResource("/project_assets/images/amphora_purple.png");
                    break;
                case RED:
                    imageURL = TileView.class.getResource("/project_assets/images/amphora_red.png");
                    break;
                case YELLOW:
                    imageURL = TileView.class.getResource("/project_assets/images/amphora_yellow.png");
                    break;
            }
        }
        else if (t instanceof MosaicTile) {
            this.correspondingArea = MOSAIC_AREA;

            switch (((MosaicTile) t).getColor()) {
                case GREEN:
                    imageURL = TileView.class.getResource("/project_assets/images/mosaic_green.png");
                    break;
                case RED:
                    imageURL = TileView.class.getResource("/project_assets/images/mosaic_red.png");
                    break;
                case YELLOW:
                    imageURL = TileView.class.getResource("/project_assets/images/mosaic_yellow.png");
                    break;
            }
        }
        else if (t instanceof StatueTile) {
            this.correspondingArea = STATUE_AREA;

            switch (((StatueTile) t).getType()) {
                case SPHINX:
                    imageURL = TileView.class.getResource("/project_assets/images/sphinx.png");
                    break;
                case CARYATID:
                    imageURL = TileView.class.getResource("/project_assets/images/caryatid.png");
                    break;
            }
        }
        else if (t instanceof SkeletonTile) {
            this.correspondingArea = SKELETON_AREA;

            switch (((SkeletonTile) t).getType()) {
                case ADULT_LOWER:
                    imageURL = TileView.class.getResource("/project_assets/images/skeleton_big_bottom.png");
                    break;
                case ADULT_UPPER:
                    imageURL = TileView.class.getResource("/project_assets/images/skeleton_big_top.png");
                    break;
                case CHILDREN_LOWER:
                    imageURL = TileView.class.getResource("/project_assets/images/skeleton_small_bottom.png");
                    break;
                case CHILDREN_UPPER:
                    imageURL = TileView.class.getResource("/project_assets/images/skeleton_small_top.png");
                    break;
            }
        }
        else this.correspondingArea = null;

        if (imageURL != null) {
            icon = new ImageIcon(imageURL);
            setIcon(icon);

            if (t instanceof LandslideTile) {
                setDisabledIcon(icon);
                setEnabled(false);
            }
            else {
                setEnabled(true);
            }
        } else {
            icon = null;
            System.err.println("Image for tile" + t.toString() + " not found.");
        }

        setActionCommand("TILE_PRESSED");
        addActionListener(l);
        setPreferredSize(new Dimension(TILE_X, TILE_Y));
    }

    //getters:
    public Tile getTileSource() {
        return source;
    }

    public AreaType getCorrespondingArea() {
        return this.correspondingArea;
    }

    public ImageIcon getIcon() {
        return icon;
    }
}

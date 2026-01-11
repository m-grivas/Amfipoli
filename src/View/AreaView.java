package View;

import Model.Tiles.LandslideTile;
import Model.Tiles.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AreaView extends JPanel {

    private final Color fillColor;
    private List<TileView> tiles;
    private final JPanel grid;
    private final ActionListener listener;

    protected AreaView(ActionListener l) {
        fillColor = new Color(80, 180, 255, 180);
        setPreferredSize(new Dimension(275, 275));
        grid = new JPanel(new GridLayout(5, 5, 5, 5));
        add(grid);
        tiles = new ArrayList<>();
        listener = l;
    }

    /**
     * Add a tile to the area
     * @param t the tile to add
     */
    protected void addTile(Tile t) {
        TileView newTile = new TileView(t, listener);
        grid.add(newTile);
        revalidate();
        repaint();
    }

    /**
     * Remove a tileView object from the area
     * @param tlv the tile to remove
     */
    protected void removeTile(TileView tlv) {
        grid.remove(tlv);
        revalidate();
        repaint();
    }

    /**
     * Enable each tile at the area so that the player can or can't pick them up
     * @param status the status of the area
     */
    protected void enabledTiles(boolean status) {
        for (Component tlv : grid.getComponents()) {
            tlv.setEnabled(status);
        }
    }
}


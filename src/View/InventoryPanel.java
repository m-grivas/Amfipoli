package View;

import Model.Player;
import Model.Tiles.*;
import Model.Color;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventoryPanel extends JPanel {

    private final int INVENTORY_X = 1500;
    private final int INVENTORY_Y = 150;

    private Player player;


    private final List<TileView> tiles;


    private JPanel tilesRow;
    private JScrollPane scroll;

    /**
     * Constructor that initializes the player to show his inventory
     * @param p the player object
     */
    protected InventoryPanel(Player p, ActionListener listener) {
        this.player = p;
        this.tiles = new ArrayList<>();


        Dimension size = new Dimension(INVENTORY_X, INVENTORY_Y);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        setLayout(new BorderLayout(0, 0));
        setBackground(new java.awt.Color(245, 246, 250));


        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(java.awt.Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(java.awt.Color.BLACK, 2),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JLabel title = new JLabel("Inventory");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setBorder(new EmptyBorder(0, 2, 0, 0));


        tilesRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        tilesRow.setOpaque(false);

        scroll = new JScrollPane(
                tilesRow,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getHorizontalScrollBar().setUnitIncrement(18);

        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210)),
                "Tiles",
                TitledBorder.LEFT,
                TitledBorder.TOP
        );
        tb.setTitleFont(getFont().deriveFont(Font.BOLD, 12f));
        scroll.setBorder(tb);

        card.add(title, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);

        loadFromPlayer();
    }

    /**
     * Load all inventory tiles from current player into the UI
     */
    private void loadFromPlayer() {
        for (Map.Entry<Color, List<MosaicTile>> e : player.getMosaics().entrySet()) {
            List<MosaicTile> list = e.getValue();
            if (list == null) continue;
            for (MosaicTile tile : list) addTile(tile);
        }

        for (StatueTile tile : player.getStatues()) addTile(tile);

        for (AmphoraTile tile : player.getAmphoras()) addTile(tile);

        for (Map.Entry<SkeletonTile.SkeletonType, List<SkeletonTile>> e : player.getSkeletons().entrySet()) {
            List<SkeletonTile> list = e.getValue();
            if (list == null) continue;
            for (SkeletonTile tile : list) addTile(tile);
        }
    }

    /**
     * Function to add a tile to the list of TileView objects
     * @param t the tile to add
     */
    protected void addTile(Tile t) {
        TileView newTile = new TileView(t, null);
        newTile.setDisabledIcon(newTile.getIcon());
        newTile.setEnabled(false);

        tiles.add(newTile);
        tilesRow.add(newTile);

        tilesRow.revalidate();
        tilesRow.repaint();
    }

    /**
     * Useful when switching players: clears and reloads the UI from the new player.
     * (Safe to keep even if you don’t call it yet.)
     */
    public void refresh() {
        tiles.clear();
        tilesRow.removeAll();
        loadFromPlayer();
        tilesRow.revalidate();
        tilesRow.repaint();
    }

    /**
     * Setter for the player
     * @param p the new player
     */
    public void setPlayer(Player p) {
        this.player = p;
        refresh();
    }

    public Player getPlayer() {
        return this.player;
    }
}

package View;

import Model.Board;
import Model.Model;
import Model.Tiles.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class BoardPanel extends JPanel {

    private final int BOARD_X = 1500;
    private final int BOARD_Y = 1000;


    private Board board;
    private BufferedImage backgroundImage;
    private final AreaView entranceArea;
    private final AreaView mosaicArea;
    private final AreaView amphoraArea;
    private final AreaView skeletonArea;
    private final AreaView statueArea;



    /**
     * Initialize BoardPanel with background image and area bounds
     */
    public BoardPanel(Board board, ActionListener listener) {
        this.board = board;

        setPreferredSize(new Dimension(BOARD_X, BOARD_Y));

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/project_assets/images/background1.jpg"));
        } catch (IOException e) {
            System.err.println("Background image not found.");
        }



        entranceArea = new AreaView(listener);
        mosaicArea = new AreaView(listener);
        amphoraArea = new AreaView(listener);
        skeletonArea = new AreaView(listener);
        statueArea = new AreaView(listener);

        layoutAreas();

        styleArea(entranceArea, "Entrance");
        styleArea(mosaicArea, "Mosaics");
        styleArea(amphoraArea, "Amphoras");
        styleArea(skeletonArea, "Skeletons");
        styleArea(statueArea, "Statues");

        add(entranceArea);
        add(mosaicArea);
        add(amphoraArea);
        add(skeletonArea);
        add(statueArea);
    }

    /**
     * For updating from model after load
     * @param model the model
     * @param listener the controller class
     */
    public BoardPanel(Model model, ActionListener listener) {
        this.board = model.getBoard();

        setPreferredSize(new Dimension(BOARD_X, BOARD_Y));

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/project_assets/images/background1.jpg"));
        } catch (IOException e) {
            System.err.println("Background image not found.");
        }

        entranceArea = new AreaView(listener);
        for (LandslideTile tile : model.getBoard().getEntrance()) {
            if (tile == null) break;
            entranceArea.addTile(tile);
        }

        mosaicArea = new AreaView(listener);
        for (MosaicTile tile : model.getBoard().getMosaicArea()) {
            if (tile == null) break;
            mosaicArea.addTile(tile);
        }

        amphoraArea = new AreaView(listener);
        for (AmphoraTile tile : model.getBoard().getAmphoraArea()) {
            if (tile == null) break;
            amphoraArea.addTile(tile);
        }

        skeletonArea = new AreaView(listener);
        for (SkeletonTile tile : model.getBoard().getSkeletonArea()) {
            if (tile == null) break;
            skeletonArea.addTile(tile);
        }

        statueArea = new AreaView(listener);
        for (StatueTile tile : model.getBoard().getStatueArea()) {
            if (tile == null) break;
            statueArea.addTile(tile);
        }

        layoutAreas();

        styleArea(entranceArea, "Entrance");
        styleArea(mosaicArea, "Mosaics");
        styleArea(amphoraArea, "Amphoras");
        styleArea(skeletonArea, "Skeletons");
        styleArea(statueArea, "Statues");

        updateBoard();


        add(entranceArea);
        add(mosaicArea);
        add(amphoraArea);
        add(skeletonArea);
        add(statueArea);
    }

    /**
     * Helper function to style better the area
     * @param area area to style
     * @param title the title to add to the area
     */
    private void styleArea(AreaView area, String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP
        );
        area.setBorder(tb);
    }

    /**
     * Setter for the board
     * @param b: the new board
     */
    public void setBoard(Board b) {
        board = b;
    }

    /**
     * Add a tile to the corresponding area of the board
     * @param t the tile to add
     */
    public void addTile(Tile t) {

        if (t instanceof LandslideTile) {
            entranceArea.addTile(t);
        }
        else if (t instanceof MosaicTile) {
            mosaicArea.addTile(t);
        }
        else if (t instanceof AmphoraTile) {
            amphoraArea.addTile(t);
        }
        else if (t instanceof SkeletonTile) {
            skeletonArea.addTile(t);
        }
        else if (t instanceof StatueTile) {
            statueArea.addTile(t);
        }
    }

    /**
     * remove a tile from the board when the player picks it up
     * @param tlv the tileView to remove
     */
    protected void removeTile(TileView tlv) {
        Tile source = tlv.getTileSource();
        if (source instanceof LandslideTile) {
            entranceArea.removeTile(tlv);
        }
        else if (source instanceof MosaicTile) {
            mosaicArea.removeTile(tlv);
        }
        else if (source instanceof AmphoraTile) {
            amphoraArea.removeTile(tlv);
        }
        else if (source instanceof SkeletonTile) {
            skeletonArea.removeTile(tlv);
        }
        else if (source instanceof StatueTile) {
            statueArea.removeTile(tlv);
        }
    }

    /**
     * Updates areas availability based on Board areas availability from model
     */
    protected void updateBoard() {
        mosaicArea.enabledTiles(board.getMosaicAvailability());
        amphoraArea.enabledTiles(board.getAmphoraAvailability());
        statueArea.enabledTiles(board.getStatueAvailability());
        skeletonArea.enabledTiles(board.getSkeletonAvailability());

        revalidate();
        repaint();
    }

    /**
     * Disable all board areas
     */
    protected void disableAreas() {
        mosaicArea.enabledTiles(false);
        amphoraArea.enabledTiles(false);
        statueArea.enabledTiles(false);
        skeletonArea.enabledTiles(false);
    }

    /**
     * Enable all board areas
     */
    protected void enableAreas() {
        mosaicArea.enabledTiles(true);
        amphoraArea.enabledTiles(true);
        statueArea.enabledTiles(true);
        skeletonArea.enabledTiles(true);
    }


    /**
     * Set the layout for the areas in the board
     */
    private void layoutAreas() {
        setLayout(null);

        int w = 360;
        int h = 220;

        int padX = 140;
        int padY = 120;

        int leftX   = padX;
        int rightX  = BOARD_X - padX - w;
        int topY    = padY;
        int bottomY = BOARD_Y - padY - h;

        int centerX = (BOARD_X - w) / 2;
        int centerY = (BOARD_Y - h) / 2;

        mosaicArea.setBounds(leftX,  topY,    w, h);
        amphoraArea.setBounds(rightX, topY,   w, h);
        entranceArea.setBounds(centerX, centerY, w, h);
        skeletonArea.setBounds(leftX, bottomY, w, h);
        statueArea.setBounds(rightX, bottomY, w, h);
    }




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }
}

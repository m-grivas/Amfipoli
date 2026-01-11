package View;

import Model.Board;
import Model.Model;
import Model.Player;
import Model.Tiles.*;
import Model.Characters.Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class View extends JFrame {

    private final Board board;
    private final BoardPanel boardPanel;
    private final PlayerPanel playerPanel;
    private final JPanel inventoryCards;
    private List<InventoryPanel> inventoryPanels;

    private JPanel horizontalContainer;




    public View(Board b, List<Player> p, ActionListener listener) {
        super("Amphipoli");

        this.board = b;

        inventoryPanels = new ArrayList<>();
        inventoryCards = new JPanel();
        inventoryCards.setLayout(new CardLayout());

        playerPanel = new PlayerPanel(p, listener);

        for (Player pl : p) {
            InventoryPanel newPanel = new InventoryPanel(pl, listener);
            inventoryPanels.add(newPanel);
            inventoryCards.add(newPanel, pl.getName());
        }


        boardPanel = new BoardPanel(board, listener);

        JPanel root = new JPanel(new BorderLayout());

        horizontalContainer = new JPanel();
        horizontalContainer.setLayout(new BoxLayout(horizontalContainer, BoxLayout.X_AXIS));
        horizontalContainer.add(boardPanel);
        horizontalContainer.add(playerPanel);

        inventoryCards.setPreferredSize(new Dimension(1500, 150));
        inventoryCards.setMinimumSize(new Dimension(1500, 150));
        inventoryCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        root.add(horizontalContainer, BorderLayout.CENTER);
        root.add(inventoryCards, BorderLayout.SOUTH);

        setContentPane(root);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * For load, updates the ui according to model
     * @param model the model
     * @param listener the controller class
     */
    public View(Model model, ActionListener listener) {
        super("Amphipoli");

        this.board = model.getBoard();

        inventoryPanels = new ArrayList<>();
        inventoryCards = new JPanel();
        inventoryCards.setLayout(new CardLayout());

        playerPanel = new PlayerPanel(model, listener);

        for (Player pl : model.getPlayersList()) {
            InventoryPanel newPanel = new InventoryPanel(pl, listener);
            inventoryPanels.add(newPanel);
            inventoryCards.add(newPanel, pl.getName());
        }


        boardPanel = new BoardPanel(model, listener);


        JPanel root = new JPanel(new BorderLayout());

        horizontalContainer = new JPanel();
        horizontalContainer.setLayout(new BoxLayout(horizontalContainer, BoxLayout.X_AXIS));
        horizontalContainer.add(boardPanel);
        horizontalContainer.add(playerPanel);

        inventoryCards.setPreferredSize(new Dimension(1500, 150));
        inventoryCards.setMinimumSize(new Dimension(1500, 150));
        inventoryCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        root.add(horizontalContainer, BorderLayout.CENTER);
        root.add(inventoryCards, BorderLayout.SOUTH);

        setContentPane(root);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Function to add the players panel buttons an action listener(the controller class)
     * @param l the listener object( the controller class)
     */
    public void addButtonsListener(ActionListener l) {
        playerPanel.getDrawTilesButton().addActionListener(l);
        playerPanel.getEndTurnButton().addActionListener(l);
        playerPanel.getSaveButton().addActionListener(l);
    }


    /**
     * Function to change the inventory and the player panel accordingly so that they match the player who plays
     * @param player the new player
     */
    public void showPlayer(Player player) {
        CardLayout playerCardLayout = (CardLayout) inventoryCards.getLayout();
        playerCardLayout.show(inventoryCards, player.getName());
        inventoryCards.revalidate();
        inventoryCards.repaint();

        playerPanel.showPlayer(player);
    }

    /**
     * Function to add a tile to the board when a player picks it from the bag
     * @param t the tile to add
     */
    public void drawTilesFromBag(Tile t) {
        boardPanel.addTile(t);
        boardPanel.revalidate();
        boardPanel.repaint();
        playerPanel.disableDrawTiles();
    }

    /**
     * Function to remove a tile from the board when a player picks it
     * @param tlv the TileView object to remove
     */
    public void removeTileFromBoard(TileView tlv) {
        boardPanel.removeTile(tlv);
    }

    /**
     * Function to add a tile to the inventory of the current player
     * @param t the tile to add
     * @param currentPlayer the current player
     */
    public void addTileToInventory(Tile t, Player currentPlayer) {
        for (InventoryPanel panel : inventoryPanels) {
            if (panel.getPlayer() == currentPlayer) {
                panel.addTile(t);
            }
        }
        inventoryCards.revalidate();
        inventoryCards.repaint();

    }

    /**
     * Function to disable all areas of the board
     */
    public void disableAreas() {
        boardPanel.disableAreas();
    }

    /**
     * Function to enable all areas of the board
     */
    public void enableAreas() {
        boardPanel.enableAreas();
    }

    /**
     * Function to disable all characters of the current player
     */
    public void disableCharacters() {
        playerPanel.enableCharacters(false);
    }

    /**
     * Function to enable all the characters of the current player
     */
    public void enableCharacters() {
        playerPanel.enableCharacters(true);
    }

    /**
     * When a player uses a character sets it unusable
     * @param p the current player
     * @param c the used character
     */
    public void setUsedCharacter(Player p, Character c) {
        playerPanel.setUseCharacter(p, c);
    }

    /**
     * Updates the character panel
     */
    public void updateCharacters() {
        playerPanel.updateCharacters();
    }


    /**
     * Function to update board
     */
    public void updateBoard() {
        boardPanel.updateBoard();
    }

    /**
     * Updates the time left for the current player
     * @param seconds the seconds
     */
    public void setDisplayTime(int seconds) {
        playerPanel.setDisplayTime(seconds);
    }

    /**
     * Updates the points of the current player
     * @param points points
     */
    public void setPointsLabel(int points) {
        playerPanel.setPointsLabel(points);
    }

}
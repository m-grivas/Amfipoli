package View;

import Model.Model;
import Model.Player;
import Model.Characters.Character;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel extends JPanel {

    private final int PLAYER_X = 400;
    private final int PLAYER_Y = 1000;

    private List<Player> players;

    private JLabel nameLabel;
    private JLabel pointsLabel;
    private JLabel timerLabel;
    private JLabel useCharacterLabel;

    private JPanel characterPanels;

    private JButton drawTilesButton;
    private JButton endTurnButton;
    private JButton saveButton;

    private List<CharacterPanel> characterCards;


    /**
     * Constructor for PlayerPanel
     * @param p Players
     * @param l Controller listener
     */
    protected PlayerPanel(List<Player> p, ActionListener l) {
        this.players = p;
        buildUI(l);
    }

    public PlayerPanel(Model model, ActionListener listener) {
        this.players = model.getPlayersList();
        buildUI(listener);
        updateCharacters();
    }


    private void buildUI(ActionListener listener) {
        characterCards = new ArrayList<>();

        setPreferredSize(new Dimension(PLAYER_X, PLAYER_Y));
        setMinimumSize(new Dimension(PLAYER_X, PLAYER_Y));
        setMaximumSize(new Dimension(PLAYER_X, PLAYER_Y));

        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(12, 12, 12, 12));
        setBackground(new Color(245, 246, 250));

        JPanel headerCard = createCardPanel();
        headerCard.setLayout(new BorderLayout(10, 10));

        nameLabel = new JLabel("Player");
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 20f));

        JPanel rightBadges = new JPanel(new GridLayout(1, 2, 10, 0));
        rightBadges.setOpaque(false);

        pointsLabel = createBadgeLabel("0", "Points");
        timerLabel  = createBadgeLabel("30", "Time");

        rightBadges.add(wrapBadge(pointsLabel));
        rightBadges.add(wrapBadge(timerLabel));

        headerCard.add(nameLabel, BorderLayout.CENTER);
        headerCard.add(rightBadges, BorderLayout.EAST);

        JPanel charactersCard = createCardPanel();
        charactersCard.setLayout(new BorderLayout(0, 8));

        useCharacterLabel = new JLabel("Use Character");
        useCharacterLabel.setFont(useCharacterLabel.getFont().deriveFont(Font.BOLD, 14f));
        useCharacterLabel.setBorder(new EmptyBorder(0, 2, 0, 0));

        characterPanels = new JPanel(new CardLayout());
        characterPanels.setOpaque(false);

        for (Player pl : players) {
            CharacterPanel newCharacterPanel = new CharacterPanel(pl, listener);
            characterCards.add(newCharacterPanel);
            characterPanels.add(newCharacterPanel, pl.getName());
        }

        JScrollPane scroll = new JScrollPane(characterPanels);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                "Characters",
                TitledBorder.LEFT,
                TitledBorder.TOP
        );
        tb.setTitleFont(getFont().deriveFont(Font.BOLD, 12f));
        charactersCard.setBorder(tb);

        charactersCard.add(useCharacterLabel, BorderLayout.NORTH);
        charactersCard.add(scroll, BorderLayout.CENTER);

        JPanel actionsCard = createCardPanel();
        actionsCard.setLayout(new GridLayout(3, 1, 0, 10));

        drawTilesButton = createPrimaryButton("Draw Tiles", "DRAW_TILES");
        endTurnButton   = createPrimaryButton("End turn", "END_TURN");
        saveButton      = createPrimaryButton("Save", "SAVE");

        actionsCard.add(drawTilesButton);
        actionsCard.add(endTurnButton);
        actionsCard.add(saveButton);

        add(headerCard, BorderLayout.NORTH);
        add(charactersCard, BorderLayout.CENTER);
        add(actionsCard, BorderLayout.SOUTH);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                new EmptyBorder(12, 12, 12, 12)
        ));

        enableDrawTiles();
    }

    private JPanel createCardPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(12, 12, 12, 12)
        ));
        return p;
    }

    private JButton createPrimaryButton(String text, String actionCommand) {
        JButton b = new JButton(text);
        b.setActionCommand(actionCommand);

        b.setPreferredSize(new Dimension(200, 65));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 14f));
        b.setFocusPainted(false);

        return b;
    }

    private JLabel createBadgeLabel(String value, String label) {
        JLabel l = new JLabel(value, SwingConstants.CENTER);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 16f));
        l.setOpaque(true);
        l.setBackground(new Color(240, 240, 240));
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(6, 10, 6, 10)
        ));
        l.setToolTipText(label);
        return l;
    }

    private JPanel wrapBadge(JLabel badge) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(badge, BorderLayout.CENTER);
        return wrap;
    }


    protected JButton getDrawTilesButton() {
        return drawTilesButton;
    }

    protected JButton getEndTurnButton() {
        return endTurnButton;
    }

    protected JButton getSaveButton() {
        return saveButton;
    }

    protected void disableDrawTiles() {
        drawTilesButton.setEnabled(false);
    }

    protected void enableDrawTiles() {
        drawTilesButton.setEnabled(true);
    }

    protected void showPlayer(Player p) {
        nameLabel.setText(p.getName());

        CardLayout cl = (CardLayout) characterPanels.getLayout();
        cl.show(characterPanels, p.getName());

        enableDrawTiles();
        characterPanels.revalidate();
        characterPanels.repaint();
    }

    protected void enableCharacters(boolean status) {
        for (CharacterPanel charCard : characterCards) {
            charCard.enableCharacters(status);
        }
    }

    protected void updateCharacters() {
        for (CharacterPanel charPanel : characterCards) {
            charPanel.update(charPanel.getPlayer());
        }
    }

    protected void setUseCharacter(Player p, Character c) {
        for (CharacterPanel cp : characterCards) {
            if (cp.getPlayer() == p) {
                cp.setUsedCharacter(c);
            }
        }
    }

    protected void setDisplayTime(int seconds) {
        timerLabel.setText(String.valueOf(seconds));
    }

    protected void setPointsLabel(int points) {
        pointsLabel.setText(String.valueOf(points));
    }
}

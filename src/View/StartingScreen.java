package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StartingScreen extends JFrame {

    public StartingScreen(ActionListener listener) {
        setTitle("Amphipoli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel background = new GradientPanel();
        background.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 32, 24, 32));
        card.setBackground(new Color(0, 0, 0, 160)); // translucent dark
        card.setOpaque(true);

        JLabel title = new JLabel("Amphipoli");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 44f));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("New game or load a save");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 14f));
        subtitle.setForeground(new Color(230, 230, 230));

        JButton newGameButton = makePrimaryButton("New Game");
        newGameButton.setActionCommand("NEW_GAME");
        newGameButton.addActionListener(listener);
        newGameButton.addActionListener(e -> dispose());

        JButton loadButton = makeSecondaryButton("Load Game");
        loadButton.setActionCommand("LOAD_GAME");
        loadButton.addActionListener(listener);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 0, 10));
        buttons.setOpaque(false);
        buttons.setMaximumSize(new Dimension(260, 200));
        buttons.add(newGameButton);
        buttons.add(loadButton);

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(22));
        card.add(buttons);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        background.add(card, gbc);

        setContentPane(background);

        getRootPane().setDefaultButton(newGameButton);

        setMinimumSize(new Dimension(520, 360));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton makePrimaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 16f));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(46, 125, 50)); // green
        b.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        return b;
    }

    private JButton makeSecondaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(b.getFont().deriveFont(Font.PLAIN, 16f));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(60, 60, 60)); // dark gray
        b.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        return b;
    }

    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(25, 25, 35),
                    0, h, new Color(10, 10, 15)
            );
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
            g2.dispose();
        }
    }


    public static int askNumberOfPlayers() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(2, 1, 4, 1));
        spinner.setPreferredSize(new Dimension(60, spinner.getPreferredSize().height));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.add(new JLabel("Number of players:"));
        panel.add(spinner);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Number of Players",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return -1;
        return (Integer) spinner.getValue();
    }

    public static List<String> askPlayerNames(int n) {
        while (true) {
            JPanel panel = new JPanel(new GridLayout(n, 2, 10, 10));
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));

            List<JTextField> fields = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                panel.add(new JLabel("Player " + i + ":"));
                JTextField tf = new JTextField(12);
                fields.add(tf);
                panel.add(tf);
            }

            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Player Names",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return null;

            List<String> names = new ArrayList<>();
            boolean allValid = true;

            for (JTextField tf : fields) {
                String name = tf.getText().trim();
                if (name.isEmpty()) {
                    allValid = false;
                    break;
                }
                names.add(name);
            }

            if (allValid) return names;

            JOptionPane.showMessageDialog(
                    null,
                    "All names must be filled in.",
                    "Invalid name",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

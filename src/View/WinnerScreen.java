package View;

import Model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map;

public class WinnerScreen extends JFrame {

    public WinnerScreen(List<Map.Entry<Player, Integer>> map, int playerCount) {
        setTitle("Game Over");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);

        JLabel title = new JLabel("Game is Over", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));

        JLabel subtitle = new JLabel("Final Scoreboard", SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 14f));

        top.add(title);
        top.add(Box.createVerticalStrut(4));
        top.add(subtitle);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(8, 8, 8, 8));
        list.setBackground(Color.WHITE);

        list.add(ScoreRow.header());
        list.add(Box.createVerticalStrut(6));


        int rank = 1;
        for (int i = 0; i < map.size(); i++) {
            Map.Entry<Player, Integer> entry = map.get(i);

            Player player = entry.getKey();
            int score = entry.getValue();

            list.add(new ScoreRow(rank, player.getName(), score, i));
            list.add(Box.createVerticalStrut(6));
            rank++;
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bottom.setOpaque(false);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottom.add(closeBtn);

        root.add(top, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);
        setMinimumSize(new Dimension(440, 320));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static class ScoreRow extends JPanel {
        private ScoreRow(String c1, String c2, String c3, boolean header, int index) {
            super(new GridBagLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setOpaque(true);

            if (!header && (index % 2 == 0)) setBackground(new Color(245, 245, 245));
            else setBackground(Color.WHITE);

            Font font = getFont().deriveFont(header ? Font.BOLD : Font.PLAIN, 14f);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel rank = new JLabel(c1);
            rank.setFont(font);

            JLabel name = new JLabel(c2);
            name.setFont(font);

            JLabel score = new JLabel(c3);
            score.setFont(font);
            score.setHorizontalAlignment(SwingConstants.RIGHT);

            gbc.gridx = 0; gbc.weightx = 0; gbc.anchor = GridBagConstraints.WEST; gbc.ipadx = 12;
            add(rank, gbc);

            gbc.gridx = 1; gbc.weightx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.ipadx = 0;
            add(name, gbc);

            gbc.gridx = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
            add(score, gbc);

            if (header) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                        new EmptyBorder(10, 10, 10, 10)
                ));
            }
        }

        static ScoreRow header() {
            return new ScoreRow("Rank", "Player", "Score", true, -1);
        }

        ScoreRow(int rank, String name, int score, int index) {
            this("#" + rank, name, String.valueOf(score), false, index);
        }
    }
}

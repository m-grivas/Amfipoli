package Model;

import Model.Characters.Character;
import Model.Tiles.*;

import static Model.Tiles.StatueTile.StatueType.*;
import static Model.Tiles.SkeletonTile.SkeletonType.*;
import Model.Characters.*;
import static Model.AreaType.*;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import static Model.Color.*;


public class Model {

    private final int playerCount;
    private final Player[] players;
    private final Board board;
    private final Bag bag;
    private int currentPlayer;

    private static final String SAVE_MAGIC = "AMPHIPOLI_SAVE_V1";


    /**
     * Function to initialize Game<br>
     * - Initializes Bag and the tiles in it by calling Bag()<br>
     * - Initializes Board<br>
     * - Initializes players with colors<br>
     * - Gives character cards to players according to their color<br>
     * - Places a tile from each category to the corresponding board area<br>
     * - Chooses randomly the player to play first<br>
     * @param p_count: the number of the players
     * @param names: the list with the names the users gave to assign them to players
     */
    public Model(int p_count, List<String> names) {

        playerCount = p_count;

        //Initialize players
        players = new Player[playerCount];

        int i = 0;
        for (String name : names) {
            if (i == 0) {
                players[i] = new Player(YELLOW, name);
            }
            if (i == 1) {
                players[i] = new Player(RED, name);
            }
            if (i == 2) {
                players[i] = new Player(BLUE, name);
            }
            if (i == 3) {
                players[i] = new Player(BLACK, name);
            }

            i++;
        }

        //Initialize board
        board = new Board();

        //Initialize bag
        bag = new Bag();


        currentPlayer = 0;
    }


    //getters:

    public Board getBoard() {
        return board;
    }
    public List<Player> getPlayersList() {

        return new ArrayList<Player>(Arrays.asList(players));
    }
    public Bag getBag() {
        return bag;
    }
    public Player currentPlayer() {
        return players[currentPlayer];
    }

    /**
     * Calculates who has the turn to play
     * @return the player who plays in this turn
     */
    public Player nextPlayer() {
        currentPlayer = (currentPlayer + 1)%playerCount;
        return players[currentPlayer];
    }


    /**
     * Calculate the statue points of all players:
     * For each type of statue(Caryatid or Sphinx):
     * - The player with the most statues: +6
     * - The player with the least statues: +0
     * - Every other player: +3
     * @return the total statue points
     */
    public int statuePoints(Player player) {
        int[] points = new int[playerCount];

        //Caryatids
        int cMin = Integer.MAX_VALUE;
        int cMax = Integer.MIN_VALUE;
        for (int i = 0; i < playerCount; i++) {
            int c = players[i].caryatidCount();
            cMin = Math.min(cMin, c);
            cMax = Math.max(cMax, c);
        }

        for (int i = 0; i < playerCount; i++) {
            int c = players[i].caryatidCount();
            if (c == cMin) points[i] += 0;
            else if (c == cMax) points[i] += 6;
            else points[i] += 3;
        }

        //Sphinx
        int sMin = Integer.MAX_VALUE;
        int sMax = Integer.MIN_VALUE;
        for (int i = 0; i < playerCount; i++) {
            int s = players[i].sphinxCount();
            sMin = Math.min(sMin, s);
            sMax = Math.max(sMax, s);
        }

        for (int i = 0; i < playerCount; i++) {
            int s = players[i].sphinxCount();
            if (s == sMin) points[i] += 0;
            else if (s == sMax) points[i] += 6;
            else points[i] += 3;
        }

        for (int i = 0; i < playerCount; i++) {
            if (players[i] == player) return points[i];
        }
        return 0;
    }


    /**
     * Calculates the total points of a player
     * @param player the player whose points to calculate
     * @return the total points
     */
    public int calculatePlayerPoints(Player player) {
        int totalPoints = 0;
        totalPoints += player.mosaicPoints();
        totalPoints += player.amphoraPoints();
        totalPoints += player.skeletonPoints();
        totalPoints += statuePoints(player);

        return totalPoints;
    }


    /**
     * Calculates who is the winner. The entry must be blocked.
     *
     * @return a sorted map with the points of each player mapped with the corresponding player object
     * Else returns null
     */
    public List<Map.Entry<Player, Integer>> calculateScoreboard() {
        if (board.blockedEntrance()) {

            Map<Player, Integer> playerMap = new HashMap<Player, Integer>();

            for (Player p : players) {
                playerMap.put(p, calculatePlayerPoints(p));
            }

            List<Map.Entry<Player, Integer>> sorted = new ArrayList<>(playerMap.entrySet());
            sorted.sort(Map.Entry.<Player, Integer>comparingByValue(Comparator.reverseOrder()));

            return sorted;
        }
        return null;
    }

    /**
     * Checks if the entry is blocked and so the game ends
     * @return boolean value if the entry is blocked
     */
    public boolean gameOver() {
        return board.blockedEntrance();
    }


    /**
     * Chooses the file to save via JFileChooser and cals saveToFile to save the game
     */
    public void saveGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save game");
        int result = chooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        try {
            saveToFile(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to save file:\n" + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves the game
     * @param file the file to save the game
     */
    public void saveToFile(File file) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.write("AMPHIPOLI_SAVE_V1");
            out.newLine();

            out.write(Integer.toString(playerCount));
            out.newLine();
            out.write(Integer.toString(currentPlayer));
            out.newLine();


            out.write("PLAYERS " + playerCount);
            out.newLine();
            for (int i = 0; i < playerCount; i++) {
                writePlayer(out, players[i]);
            }

            writeBag(out);

            writeBoard(out);
        }
    }

    /**
     * Chooses a file to load game from and calls loadFromFile(file)
     * @return the new model from the saved file
     */
    public static Model loadGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load game");
        int result = chooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) return null;

        File file = chooser.getSelectedFile();
        try {
            return loadFromFile(file);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to load file:\n" + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Loads the game from a saved file
     * @param file the file to load the game from
     * @return the model generated from loading the game
     */
    public static Model loadFromFile(File file) throws Exception {
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String header = requireLine(in, "Missing save header");
            if (!"AMPHIPOLI_SAVE_V1".equals(header)) {
                throw new IOException("Unsupported save format: " + header);
            }

            int pCount = parseInt(requireLine(in, "Missing player count"));
            int currentPlayerIndex = parseInt(requireLine(in, "Missing current player index"));

            String playersLine = requireLine(in, "Missing PLAYERS line");
            String[] pl = playersLine.trim().split("\\s+");
            if (pl.length != 2 || !"PLAYERS".equals(pl[0])) {
                throw new IOException("Invalid PLAYERS line: " + playersLine);
            }
            int declaredPlayers = parseInt(pl[1]);
            if (declaredPlayers != pCount) {
                throw new IOException("Player count mismatch (expected " + pCount + ", found " + declaredPlayers + ")");
            }

            List<String> names = new ArrayList<>();
            List<PlayerSnapshot> snapshots = new ArrayList<>();
            for (int i = 0; i < pCount; i++) {
                PlayerSnapshot snap = readPlayerSnapshot(in);
                names.add(snap.name);
                snapshots.add(snap);
            }

            Model model = new Model(pCount, names);
            model.currentPlayer = clamp(currentPlayerIndex, 0, pCount - 1);

            for (int i = 0; i < pCount; i++) {
                applyPlayerSnapshot(model.players[i], snapshots.get(i));
            }

            // Bag
            readBagInto(model.bag, in);

            // Board
            readBoardInto(model.board, in);

            return model;
        }
    }

    /**
     * Save helper function to save the player to the file
     * @param out the buffered writer
     * @param player the player to save
     * @throws IOException for error when writing to the file
     */
    private void writePlayer(BufferedWriter out, Player player) throws IOException {
        out.write("PLAYER " + escape(player.getName()) + " " + player.getColor().name());
        out.newLine();

        out.write("MOSAICS " + player.getMosaics().get(RED).size() + " "
                + player.getMosaics().get(GREEN).size() + " "
                + player.getMosaics().get(YELLOW).size());
        out.newLine();

        int caryatid = 0, sphinx = 0;
        for (StatueTile s : player.getStatues()) {
            if (s.getType() == CARYATID) caryatid++;
            else sphinx++;
        }
        out.write("STATUES " + caryatid + " " + sphinx);
        out.newLine();

        int[] amph = new int[Color.values().length];
        for (AmphoraTile a : player.getAmphoras()) amph[a.getColor().ordinal()]++;
        out.write("AMPHORAS");
        for (Color c : Color.values()) out.write(" " + amph[c.ordinal()]);
        out.newLine();

        out.write("SKELETONS "
                + player.getSkeletons().get(ADULT_UPPER).size() + " "
                + player.getSkeletons().get(ADULT_LOWER).size() + " "
                + player.getSkeletons().get(CHILDREN_UPPER).size() + " "
                + player.getSkeletons().get(CHILDREN_LOWER).size());
        out.newLine();

        out.write("CHARS");
        for (Character c : player.getCharacters()) {
            out.write(" " + (c.getAvailability() ? 1 : 0));
        }
        out.newLine();

        out.write("CODER "
                + (player.getUseCoderNextTurn() ? 1 : 0) + " "
                + player.getCoderTiles() + " "
                + (player.getCoderArea() == null ? "NONE" : player.getCoderArea().name()));
        out.newLine();
    }

    /**
     * Helper function to write the bag to the file
     * @param out the file
     * @throws IOException for errors
     */
    private void writeBag(BufferedWriter out) throws IOException {
        out.write("BAG " + bag.getRemainingTiles());
        out.newLine();

        int last = bag.getRemainingTiles();
        Tile[] tiles = bag.getTiles();
        out.write("BAG_TILES");
        for (int i = 0; i <= last; i++) {
            out.write(" " + tileToCode(tiles[i]));
        }
        out.newLine();
    }

    /**
     * Helper function to write the board to the file
     * @param out the file
     * @throws IOException for errors
     */
    private void writeBoard(BufferedWriter out) throws IOException {
        out.write("BOARD_AVAIL "
                + (board.getMosaicAvailability() ? 1 : 0) + " "
                + (board.getStatueAvailability() ? 1 : 0) + " "
                + (board.getAmphoraAvailability() ? 1 : 0) + " "
                + (board.getSkeletonAvailability() ? 1 : 0));
        out.newLine();

        writeBoardArea(out, "MOSAIC", board.getMosaicIndex(), board.getMosaicArea());
        writeBoardArea(out, "STATUE", board.getStatueIndex(), board.getStatueArea());
        writeBoardArea(out, "AMPHORA", board.getAmphoraIndex(), board.getAmphoraArea());
        writeBoardArea(out, "SKELETON", board.getSkeletonIndex(), board.getSkeletonArea());
        writeBoardArea(out, "ENTRANCE", board.getEntranceIndex(), board.getEntrance());
    }

    /**
     * Helper function to write a board area to the file
     * @param out the file
     * @param label the label of the area
     * @param index the number of the objects
     * @param area the area
     * @throws IOException for errors
     */
    private void writeBoardArea(BufferedWriter out, String label, int index, Object[] area) throws IOException {
        out.write("AREA " + label + " " + index);
        for (int i = 0; i < index; i++) {
            out.write(" " + tileToCode((Tile) area[i]));
        }
        out.newLine();
    }

    /**
     * Return a letter corresponding to the tile type
     * @param tile the tile to get its code
     * @return the corresponding code
     */
    private String tileToCode(Tile tile) {
        if (tile == null) return "NULL";
        if (tile instanceof LandslideTile) return "L";
        if (tile instanceof MosaicTile) return "M:" + ((MosaicTile) tile).getColor().name();
        if (tile instanceof AmphoraTile) return "A:" + ((AmphoraTile) tile).getColor().name();
        if (tile instanceof SkeletonTile) return "K:" + ((SkeletonTile) tile).getType().name();
        if (tile instanceof StatueTile) return "S:" + ((StatueTile) tile).getType().name();
        return "UNKNOWN";
    }


    /**
     * Class for all the player information that needs to be saved
     */
    private static class PlayerSnapshot {
        String name;
        Color color;
        int mosaicRed, mosaicGreen, mosaicYellow;
        int caryatid, sphinx;
        int[] amphorasByColor;
        int adultUpper, adultLower, childUpper, childLower;
        int[] charAvailability;
        boolean useCoder;
        int coderTiles;
        AreaType coderArea;
    }


    private static PlayerSnapshot readPlayerSnapshot(BufferedReader in) throws IOException {
        PlayerSnapshot s = new PlayerSnapshot();

        String line = requireLine(in, "Missing PLAYER line");
        String[] p = splitPreserveEscapes(line);
        if (p.length != 3 || !"PLAYER".equals(p[0])) throw new IOException("Invalid PLAYER line: " + line);
        s.name = unescape(p[1]);
        s.color = Color.valueOf(p[2]);

        line = requireLine(in, "Missing MOSAICS line");
        String[] m = line.trim().split("\\s+");
        if (m.length != 4 || !"MOSAICS".equals(m[0])) throw new IOException("Invalid MOSAICS line: " + line);
        s.mosaicRed = parseInt(m[1]);
        s.mosaicGreen = parseInt(m[2]);
        s.mosaicYellow = parseInt(m[3]);

        line = requireLine(in, "Missing STATUES line");
        String[] st = line.trim().split("\\s+");
        if (st.length != 3 || !"STATUES".equals(st[0])) throw new IOException("Invalid STATUES line: " + line);
        s.caryatid = parseInt(st[1]);
        s.sphinx = parseInt(st[2]);

        line = requireLine(in, "Missing AMPHORAS line");
        String[] a = line.trim().split("\\s+");
        if (a.length != 1 + Color.values().length || !"AMPHORAS".equals(a[0])) {
            throw new IOException("Invalid AMPHORAS line: " + line);
        }
        s.amphorasByColor = new int[Color.values().length];
        for (int i = 0; i < Color.values().length; i++) {
            s.amphorasByColor[i] = parseInt(a[i + 1]);
        }

        line = requireLine(in, "Missing SKELETONS line");
        String[] sk = line.trim().split("\\s+");
        if (sk.length != 5 || !"SKELETONS".equals(sk[0])) throw new IOException("Invalid SKELETONS line: " + line);
        s.adultUpper = parseInt(sk[1]);
        s.adultLower = parseInt(sk[2]);
        s.childUpper = parseInt(sk[3]);
        s.childLower = parseInt(sk[4]);

        line = requireLine(in, "Missing CHARS line");
        String[] ch = line.trim().split("\\s+");
        if (ch.length < 2 || !"CHARS".equals(ch[0])) throw new IOException("Invalid CHARS line: " + line);
        s.charAvailability = new int[ch.length - 1];
        for (int i = 1; i < ch.length; i++) s.charAvailability[i - 1] = parseInt(ch[i]);

        line = requireLine(in, "Missing CODER line");
        String[] co = line.trim().split("\\s+");
        if (co.length != 4 || !"CODER".equals(co[0])) throw new IOException("Invalid CODER line: " + line);
        s.useCoder = parseInt(co[1]) == 1;
        s.coderTiles = parseInt(co[2]);
        s.coderArea = "NONE".equals(co[3]) ? null : AreaType.valueOf(co[3]);

        return s;
    }

    private static void applyPlayerSnapshot(Player player, PlayerSnapshot s) {
        if (s.color != null) {
            player.setColor(s.color);
        }
        // Inventories
        player.getMosaics().get(RED).clear();
        player.getMosaics().get(GREEN).clear();
        player.getMosaics().get(YELLOW).clear();
        for (int i = 0; i < s.mosaicRed; i++) player.getMosaics().get(RED).add(new MosaicTile(RED));
        for (int i = 0; i < s.mosaicGreen; i++) player.getMosaics().get(GREEN).add(new MosaicTile(GREEN));
        for (int i = 0; i < s.mosaicYellow; i++) player.getMosaics().get(YELLOW).add(new MosaicTile(YELLOW));

        player.getStatues().clear();
        for (int i = 0; i < s.caryatid; i++) player.getStatues().add(new StatueTile(CARYATID));
        for (int i = 0; i < s.sphinx; i++) player.getStatues().add(new StatueTile(SPHINX));

        player.getAmphoras().clear();
        if (s.amphorasByColor != null) {
            for (Color c : Color.values()) {
                int n = s.amphorasByColor[c.ordinal()];
                for (int i = 0; i < n; i++) player.getAmphoras().add(new AmphoraTile(c));
            }
        }

        player.getSkeletons().get(ADULT_UPPER).clear();
        player.getSkeletons().get(ADULT_LOWER).clear();
        player.getSkeletons().get(CHILDREN_UPPER).clear();
        player.getSkeletons().get(CHILDREN_LOWER).clear();
        for (int i = 0; i < s.adultUpper; i++) player.getSkeletons().get(ADULT_UPPER).add(new SkeletonTile(ADULT_UPPER));
        for (int i = 0; i < s.adultLower; i++) player.getSkeletons().get(ADULT_LOWER).add(new SkeletonTile(ADULT_LOWER));
        for (int i = 0; i < s.childUpper; i++) player.getSkeletons().get(CHILDREN_UPPER).add(new SkeletonTile(CHILDREN_UPPER));
        for (int i = 0; i < s.childLower; i++) player.getSkeletons().get(CHILDREN_LOWER).add(new SkeletonTile(CHILDREN_LOWER));

        if (s.charAvailability != null) {
            List<Character> chars = player.getCharacters();
            for (int i = 0; i < Math.min(chars.size(), s.charAvailability.length); i++) {
                chars.get(i).setAvailability(s.charAvailability[i] == 1);
            }
        }

        player.setUseCoderNextTurn(s.useCoder);
        player.setCoderTiles(s.coderTiles);
        player.setCoderArea(s.coderArea);
    }

    private static void readBagInto(Bag bag, BufferedReader in) throws Exception {
        String line = requireLine(in, "Missing BAG line");
        String[] b = line.trim().split("\\s+");
        if (b.length != 2 || !"BAG".equals(b[0])) throw new IOException("Invalid BAG line: " + line);
        int remaining = parseInt(b[1]);

        line = requireLine(in, "Missing BAG_TILES line");
        String[] bt = line.trim().split("\\s+");
        if (bt.length < 1 || !"BAG_TILES".equals(bt[0])) throw new IOException("Invalid BAG_TILES line: " + line);

        Tile[] tiles = bag.getTiles();
        Arrays.fill(tiles, null);
        for (int i = 0; i <= remaining; i++) {
            int tokenIndex = i + 1;
            if (tokenIndex >= bt.length) throw new IOException("Not enough BAG_TILES entries for remaining=" + remaining);
            tiles[i] = codeToTile(bt[tokenIndex]);
        }

        setPrivateInt(bag, "remainingTiles", remaining);
    }

    private static void readBoardInto(Board board, BufferedReader in) throws Exception {
        String line = requireLine(in, "Missing BOARD_AVAIL line");
        String[] av = line.trim().split("\\s+");
        if (av.length != 5 || !"BOARD_AVAIL".equals(av[0])) throw new IOException("Invalid BOARD_AVAIL line: " + line);
        board.setMosaicAvailable(parseInt(av[1]) == 1);
        board.setStatueAvailable(parseInt(av[2]) == 1);
        board.setAmphoraAvailable(parseInt(av[3]) == 1);
        board.setSkeletonAvailable(parseInt(av[4]) == 1);

        readBoardAreaInto(board.getMosaicArea(), "MOSAIC", in);
        int mi = lastReadAreaIndex;
        readBoardAreaInto(board.getStatueArea(), "STATUE", in);
        int si = lastReadAreaIndex;
        readBoardAreaInto(board.getAmphoraArea(), "AMPHORA", in);
        int ai = lastReadAreaIndex;
        readBoardAreaInto(board.getSkeletonArea(), "SKELETON", in);
        int ski = lastReadAreaIndex;
        readBoardAreaInto(board.getEntrance(), "ENTRANCE", in);
        int ei = lastReadAreaIndex;

        setPrivateInt(board, "mosaicIndex", mi);
        setPrivateInt(board, "statueIndex", si);
        setPrivateInt(board, "amphoraIndex", ai);
        setPrivateInt(board, "skeletonIndex", ski);
        setPrivateInt(board, "entranceIndex", ei);
    }

    private static int lastReadAreaIndex = 0;

    private static void readBoardAreaInto(Object[] targetArea, String expectedLabel, BufferedReader in) throws Exception {
        String line = requireLine(in, "Missing AREA line for " + expectedLabel);
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 3 || !"AREA".equals(parts[0]) || !expectedLabel.equals(parts[1])) {
            throw new IOException("Invalid AREA line (expected " + expectedLabel + "): " + line);
        }
        int index = parseInt(parts[2]);
        Arrays.fill(targetArea, null);
        for (int i = 0; i < index; i++) {
            int tokenIndex = 3 + i;
            if (tokenIndex >= parts.length) throw new IOException("Not enough tiles listed for AREA " + expectedLabel);
            targetArea[i] = codeToTile(parts[tokenIndex]);
        }
        lastReadAreaIndex = index;
    }

    private static Tile codeToTile(String code) throws IOException {
        if (code == null || code.isEmpty() || "NULL".equals(code)) return null;
        if ("L".equals(code)) return new LandslideTile();

        String[] parts = code.split(":");
        if (parts.length != 2) throw new IOException("Invalid tile code: " + code);
        String kind = parts[0];
        String value = parts[1];

        switch (kind) {
            case "M":
                return new MosaicTile(Color.valueOf(value));
            case "A":
                return new AmphoraTile(Color.valueOf(value));
            case "S":
                return new StatueTile(StatueTile.StatueType.valueOf(value));
            case "K":
                return new SkeletonTile(SkeletonTile.SkeletonType.valueOf(value));
            default:
                throw new IOException("Unknown tile kind: " + kind);
        }
    }

    private static void setPrivateInt(Object obj, String fieldName, int value) throws Exception {
        Field f = obj.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.setInt(obj, value);
    }

    private static String requireLine(BufferedReader in, String errorMsg) throws IOException {
        String line = in.readLine();
        if (line == null) throw new IOException(errorMsg);
        return line;
    }

    private static int parseInt(String s) throws IOException {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IOException("Invalid number: " + s);
        }
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    // Simple escaping so names with spaces are safe.
    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace(" ", "\\s");
    }

    private static String unescape(String s) {
        // reverse of escape (only used on single token)
        return s.replace("\\s", " ").replace("\\\\", "\\");
    }

    private static String[] splitPreserveEscapes(String line) {
        // We only need 3 tokens for PLAYER. Names were escaped to contain no spaces.
        return line.trim().split("\\s+");
    }

}

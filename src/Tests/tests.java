package Tests;

import Model.Board;
import Model.Color;
import Model.Player;
import Model.Tiles.AmphoraTile;
import Model.Tiles.SkeletonTile;
import Model.Tiles.StatueTile;
import org.junit.Test;
import Model.Model;

import java.util.*;

import static Model.Color.*;
import static Model.Tiles.StatueTile.StatueType.CARYATID;
import static Model.Tiles.SkeletonTile.SkeletonType.*;
import static Model.Tiles.StatueTile.StatueType.SPHINX;
import static org.junit.Assert.assertEquals;

public class tests {


    //Amphora points tests
    @Test
    public void amphoraPointsTest() {
        Player player = new Player(BLUE, "mike");
        Board board = new Board();

        AmphoraTile a1 = new AmphoraTile(BLUE);
        AmphoraTile a2 = new AmphoraTile(YELLOW);
        AmphoraTile a3 = new AmphoraTile(BLACK);
        AmphoraTile a4 = new AmphoraTile(BROWN);


        board.appendTileToArea(a1);
        board.appendTileToArea(a2);
        board.appendTileToArea(a3);
        board.appendTileToArea(a4);

        player.pickTileFromBoard(board, a1);
        player.pickTileFromBoard(board, a2);
        player.pickTileFromBoard(board, a3);
        player.pickTileFromBoard(board, a4);

        assertEquals(2, player.amphoraPoints());


        AmphoraTile a5 = new AmphoraTile(RED);
        board.appendTileToArea(a5);
        player.pickTileFromBoard(board, a5);

        assertEquals(4, player.amphoraPoints());

    }


    //Mosaic points test
    @Test
    public void completeSameColor() {
        Player p = new Player(RED, "mike");

        addMosaics(p, RED, 4);

        assertEquals(4, p.mosaicPoints());
    }

    @Test
    public void completeDifferentColor() {
        Player p = new Player(RED, "mike");

        addMosaics(p, RED, 2);
        addMosaics(p, YELLOW, 1);
        addMosaics(p, GREEN, 1);

        assertEquals(2, p.mosaicPoints());
    }

    @Test
    public void incomplete() {
        Player p = new Player(RED, "mike");

        addMosaics(p, RED, 2);
        addMosaics(p, GREEN, 1);

        assertEquals(0, p.mosaicPoints());
    }

    @Test
    public void mixedCaseCountsCorrectly() {
        Player p = new Player(RED, "mike");

        addMosaics(p, RED, 4);

        addMosaics(p, GREEN, 4);

        addMosaics(p, RED, 2);
        addMosaics(p, YELLOW, 2);

        assertEquals(10, p.mosaicPoints());
    }

    private void addMosaics(Player p, Color color, int n) {
        for (int i = 0; i < n; i++) {
            p.getMosaics().get(color).add(null);
        }
    }


    //Skeleton Tiles test
    @Test
    public void family() {
        Player p = new Player(RED, "mike");
        
        addSkeletonParts(p, ADULT_UPPER, 2);
        addSkeletonParts(p, ADULT_LOWER, 2);
        
        addSkeletonParts(p, CHILDREN_UPPER, 1);
        addSkeletonParts(p, CHILDREN_LOWER, 1);
        
        assertEquals(6, p.skeletonPoints());
    }

    @Test
    public void completeAdult() {
        Player p = new Player(RED, "mike");

        addSkeletonParts(p, ADULT_UPPER, 1);
        addSkeletonParts(p, ADULT_LOWER, 1);

        assertEquals(1, p.skeletonPoints());
    }

    @Test
    public void completeChild() {
        Player p = new Player(RED, "mike");

        addSkeletonParts(p, CHILDREN_UPPER, 1);
        addSkeletonParts(p, CHILDREN_LOWER, 1);

        assertEquals(1, p.skeletonPoints());
    }

    @Test
    public void incompleteSkeleton() {
        Player p = new Player(RED, "mike");
        
        addSkeletonParts(p, ADULT_UPPER, 1);
        addSkeletonParts(p, CHILDREN_LOWER, 1);

        assertEquals(0, p.skeletonPoints());
    }

    @Test
    public void twoFamilies() {
        Player p = new Player(RED, "mike");

        addSkeletonParts(p, ADULT_UPPER, 4);
        addSkeletonParts(p, ADULT_LOWER, 4);

        addSkeletonParts(p, CHILDREN_UPPER, 2);
        addSkeletonParts(p, CHILDREN_LOWER, 2);

        assertEquals(12, p.skeletonPoints());
    }

    @Test
    public void familyAndOneExtra() {
        Player p = new Player(RED, "mike");

        addSkeletonParts(p, ADULT_UPPER, 3);
        addSkeletonParts(p, ADULT_LOWER, 3);

        addSkeletonParts(p, CHILDREN_UPPER, 1);
        addSkeletonParts(p, CHILDREN_LOWER, 1);

        assertEquals(7, p.skeletonPoints());
    }


    private void addSkeletonParts(Player p, SkeletonTile.SkeletonType type, int n) {
        for (int i = 0; i < n; i++) {
            p.getSkeletons().get(type).add(null);
        }
    }


    //Statue points tests:
    @Test
    public void statuePoints_3players_uniqueMaxAndMin() {
        Model m = new Model(3, Arrays.asList("A", "B", "C"));
        List<Player> ps = m.getPlayersList();
        Player p1 = ps.get(0);
        Player p2 = ps.get(1);
        Player p3 = ps.get(2);

        addStatues(p1, CARYATID, 3);
        addStatues(p2, CARYATID, 1);
        addStatues(p3, CARYATID, 2);

        addStatues(p1, SPHINX, 0);
        addStatues(p2, SPHINX, 2);
        addStatues(p3, SPHINX, 1);

        assertEquals(6, m.statuePoints(p1));
        assertEquals(6, m.statuePoints(p2));
        assertEquals(6, m.statuePoints(p3));
    }

    @Test
    public void statuePoints_4players_tieForMax() {
        Model m = new Model(4, Arrays.asList("A", "B", "C", "D"));
        List<Player> ps = m.getPlayersList();
        Player p1 = ps.get(0);
        Player p2 = ps.get(1);
        Player p3 = ps.get(2);
        Player p4 = ps.get(3);

        addStatues(p1, CARYATID, 3);
        addStatues(p2, CARYATID, 3);
        addStatues(p3, CARYATID, 1);
        addStatues(p4, CARYATID, 2);

        addStatues(p1, SPHINX, 0);
        addStatues(p2, SPHINX, 1);
        addStatues(p3, SPHINX, 2);
        addStatues(p4, SPHINX, 3);

        assertEquals(6, m.statuePoints(p1));
        assertEquals(9, m.statuePoints(p2));
        assertEquals(3, m.statuePoints(p3));
        assertEquals(9, m.statuePoints(p4));
    }

    @Test
    public void statuePoints_minPlayerGetsZero() {

        Model m = new Model(3, Arrays.asList("A", "B", "C"));
        List<Player> ps = m.getPlayersList();
        Player p1 = ps.get(0);
        Player p2 = ps.get(1);
        Player p3 = ps.get(2);

        addStatues(p1, CARYATID, 2);
        addStatues(p2, CARYATID, 0);
        addStatues(p3, CARYATID, 1);

        addStatues(p1, SPHINX, 0);
        addStatues(p2, SPHINX, 1);
        addStatues(p3, SPHINX, 2);


        assertEquals(6, m.statuePoints(p1));
        assertEquals(3, m.statuePoints(p2));
        assertEquals(9, m.statuePoints(p3));
    }

    private void addStatues(Player p, StatueTile.StatueType type, int n) {
        for (int i = 0; i < n; i++) {
            p.getStatues().add(new StatueTile(type));
        }
    }

}
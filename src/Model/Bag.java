package Model;

import java.util.Random;
import Model.Tiles.*;
import static Model.Color.*;
import static Model.Tiles.StatueTile.StatueType.*;
import static Model.Tiles.SkeletonTile.SkeletonType.*;

public class Bag {
    private final Tile[] tiles = new Tile[135];
    private int remainingTiles;


    public Tile[] getTiles () {
        return tiles;
    }
    public int getRemainingTiles() {
        return remainingTiles;
    }


    /**
     * Fill the bag with the corresponding tiles from each category
     */
    public Bag() {
        int index = 0;
        remainingTiles = 125;

        //27 Mosaic Tiles
        for (int i = 0; i < 9; i++) {
            tiles[index] = new MosaicTile(RED);
            index ++;
        }
        for (int i = 0; i < 9; i++) {
            tiles[index] = new MosaicTile(GREEN);
            index ++;
        }
        for (int i = 0; i < 9; i++) {
            tiles[index] = new MosaicTile(YELLOW);
            index ++;
        }

        //24 Statue tiles
        for (int i = 0; i < 12; i ++) {
            tiles[index] = new StatueTile(CARYATID);
            index ++;
        }
        for (int i = 0; i < 12; i++ ) {
            tiles[index] = new StatueTile(SPHINX);
            index++;
        }

        //24 Landslide tiles
        for (int i = 0; i < 24; i++) {
            tiles[index] = new LandslideTile();
            index++;
        }

        //30 Skeleton tiles
        for (int i = 0; i < 10; i++) {
            tiles[index] = new SkeletonTile(ADULT_UPPER);
            index++;
        }
        for (int i = 0; i < 10; i++) {
            tiles[index] = new SkeletonTile(ADULT_LOWER);
            index++;
        }
        for (int i = 0; i < 5; i++) {
            tiles[index] = new SkeletonTile(CHILDREN_UPPER);
            index++;
        }
        for (int i = 0; i < 5; i++) {
            tiles[index] = new SkeletonTile(CHILDREN_LOWER);
            index++;
        }

        //30 Amphora tiles
        for (int i = 0; i < 30; i++) {
            tiles[index] = new AmphoraTile(Color.values()[i/5]);
            index++;
        }
    }

    /**
     * Function to pick a random tile from the bag.
     * Selects a random tile to give to the player, and then gets the last tile in the bag and places it to the position of the tile we gave to the player
     * @return returns a random tile
     */
    public Tile pickTile() throws EmptyBagException {
        if (remainingTiles == -2)
            throw new EmptyBagException();


        int value;
        if (remainingTiles == -1) {
            value = 0;
        }
        else {
            Random rand = new Random();
            value = rand.nextInt(remainingTiles+1);
        }


        Tile tileToReturn = tiles[value];

        tiles[value] = tiles[remainingTiles];
        tiles[remainingTiles] = null;
        remainingTiles --;

        return tileToReturn;
    }


    public static class EmptyBagException extends Exception {}
}

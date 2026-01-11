package Model;

import Model.Characters.Character;
import Model.Tiles.*;
import Model.Tiles.Tile;
import Model.Characters.*;
import static Model.Tiles.StatueTile.StatueType.*;
import static Model.Tiles.SkeletonTile.SkeletonType.*;
import Model.Tiles.SkeletonTile.SkeletonType;
import static Model.Color.*;
import static java.lang.Math.min;


import java.util.*;
import java.util.HashSet;

public class Player implements Colored {
    private final String name;
    private Color color;

    private final HashMap<Color, List<MosaicTile>> mosaics;
    private final List<StatueTile> statues;
    private final List<AmphoraTile> amphoras;
    private final HashMap<SkeletonType, List<SkeletonTile>> skeletons;
    private final List<Character> characters;

    private boolean useCoderNextTurn;
    private int coderTiles;
    private AreaType coderArea;


    /**
     * Initialize player with empty inventory and all 5 characters
     * @param c: the color of the new player
     * @param n: the name of the new player
     */
    public Player(Color c, String n) {
        color = c;
        name = n;
        useCoderNextTurn = false;
        coderTiles = 0;
        coderArea = null;

        characters = new ArrayList<Character>();

        characters.add(new Assistant(c, this));
        characters.add(new Archeologist(c, this));
        characters.add(new Digger(c, this));
        characters.add(new Professor(c, this));
        characters.add(new Coder(c, this));


        mosaics = new HashMap<>();
        mosaics.put(RED, new ArrayList<MosaicTile>());
        mosaics.put(GREEN, new ArrayList<MosaicTile>());
        mosaics.put(YELLOW, new ArrayList<MosaicTile>());

        statues = new ArrayList<>();
        amphoras = new ArrayList<>();

        skeletons = new HashMap<>();
        skeletons.put(ADULT_UPPER, new ArrayList<SkeletonTile>());
        skeletons.put(ADULT_LOWER, new ArrayList<SkeletonTile>());
        skeletons.put(CHILDREN_UPPER, new ArrayList<SkeletonTile>());
        skeletons.put(CHILDREN_LOWER, new ArrayList<SkeletonTile>());

    }



    public boolean getUseCoderNextTurn() {
        return useCoderNextTurn;
    }

    public void setUseCoderNextTurn(boolean a) {
        useCoderNextTurn = a;
    }

    public int getCoderTiles() {
        return coderTiles;
    }

    public void setCoderTiles(int c) {
        coderTiles = c;
    }

    public AreaType getCoderArea() {
        return coderArea;
    }

    public void setCoderArea(AreaType t) {
        coderArea = t;
    }

    /**
     * Getter for the name
     * @return player name
     */
    public String getName() {
        return this.name;
    }

    public HashMap<Color, List<MosaicTile>> getMosaics() {
        return mosaics;
    }

    public List<StatueTile> getStatues() {
        return statues;
    }

    public List<AmphoraTile> getAmphoras() {
        return amphoras;
    }

    public HashMap<SkeletonType, List<SkeletonTile>> getSkeletons() {
        return skeletons;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    /**
     * Function to pick a tile from the bag and append it to the board
     * @param bag the bag
     * @param board the board
     * @return the tile picked from the bag (for display)
     */
    public Tile pickTileFromBag(Bag bag, Board board) {
        try {
            Tile newTile = bag.pickTile();
            board.appendTileToArea(newTile);
            return newTile;
        } catch (Bag.EmptyBagException e) {
            return null;
        }


    }


    /**
     * Pick tile from board and add it to the corresponding structure
     * @param board the board
     * @param tile the tile picked
     */
    public void pickTileFromBoard(Board board, Tile tile) {

        Tile newTile = board.pickTile(tile);

        if (newTile instanceof MosaicTile) {
            mosaics.get(((MosaicTile) newTile).getColor()).add((MosaicTile) newTile);
        }
        else if (newTile instanceof AmphoraTile) {
            amphoras.add((AmphoraTile) newTile);
        }
        else if (newTile instanceof StatueTile) {
            statues.add((StatueTile) newTile);
        }
        else if (newTile instanceof SkeletonTile) {
            skeletons.get(((SkeletonTile) newTile).getType()).add((SkeletonTile) newTile);
        }
    }


    /**
     * Function to use the ability of an available character
     * @param character: the character to be used
     * @param previousArea the area from which the player picked the last tile from
     * @param board the board
     * @return : the tiles the user is able to pick from the board
     */
    public int useCharacter(Character character, AreaType previousArea, Board board) {
        int tilesToPick = -1;

        if (character.getAvailability()) {
            if (character instanceof Coder) {
                coderTiles = 2;
                useCoderNextTurn = true;
                coderArea = previousArea;
            }
            tilesToPick = character.useAbility(previousArea, board, this);
            character.setAvailability(false);
        }
        else {
            System.err.println("Error: character is not available");
        }

        if (tilesToPick == -1) System.err.println("Error(wrong tiles from useAbility");
        return tilesToPick;
    }



    /**
     * Function to calculate points from mosaic tiles:
     * Complete mosaic = 4 mosaic tiles.
     * For one color complete mosaic : 4 points
     * For different color complete mosaic : 2 points
     * For not completed mosaic : 0 points
     * @return the total points coming from mosaic tiles
     */
    public int mosaicPoints() {

        int points = 0;

        points += (mosaics.get(RED).size()/4 + mosaics.get(YELLOW).size()/4 + mosaics.get(GREEN).size()/4) * 4;

        int remainingTiles = mosaics.get(RED).size()%4 + mosaics.get(YELLOW).size()%4 + mosaics.get(GREEN).size()%4;

        points += (remainingTiles/4) * 2;

        return points;
    }

    /**
     * Function to calculate the number of sphinx statues the player has
     * @return the number of sphinx statues
     */
    public int sphinxCount() {
        int c = 0;
        for (StatueTile tile : statues) {
            if (tile.getType() == SPHINX) {
                c++;
            }
        }
        return c;
    }

    /**
     * Function to calculate the number of caryatid statues the player has
     * @return the number of caryatid statues
     */
    public int caryatidCount() {
        int c = 0;
        for (StatueTile tile : statues) {
            if (tile.getType() == CARYATID) {
                c++;
            }
        }
        return c;
    }


    /**
     * Calculates the points the user has based on the different amphora colors he has in his inventory
     * @return the number of points coming from amphora tiles
     */
    public int amphoraPoints() {
        HashSet<Color> foundColors = new HashSet<>();

        for (AmphoraTile tile : amphoras) {
            foundColors.add(tile.getColor());
        }

        int n = foundColors.size();

        switch (n) {
            case 6:
                return 6;
            case 5:
                return 4;
            case 4:
                return 2;
            case 3:
                return 1;
            default:
                return 0;
        }


    }

    /**
     * Calculate points coming from skeleton tiles:
     * A family(2 complete adults + 1 complete child) gives 6 points
     * A complete skeleton that is not in a family gives 1 point
     * Every other skeleton part gives 0 points
     * @return the total skeleton points
     */
    public int skeletonPoints() {
        int completeAdults = min(skeletons.get(ADULT_UPPER).size(), skeletons.get(ADULT_LOWER).size());

        int completeChildren = min(skeletons.get(CHILDREN_UPPER).size(), skeletons.get(CHILDREN_LOWER).size());

        int points = 0;

        while (completeAdults >= 2 && completeChildren >= 1) {
            points += 6;
            completeAdults -= 2;
            completeChildren -= 1;
        }


        points += completeChildren + completeAdults;

        return points;
    }



    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color c) {
        this.color = c;
    }
}

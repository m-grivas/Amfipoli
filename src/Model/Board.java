package Model;

import Model.Tiles.*;
import static Model.AreaType.*;

public class Board {
    private final MosaicTile[] mosaicArea;
    private int mosaicIndex;
    private boolean mosaicAvailable;

    private final StatueTile[] statueArea;
    private int statueIndex;
    private boolean statueAvailable;

    private final AmphoraTile[] amphoraArea;
    private int amphoraIndex;
    private boolean amphoraAvailable;

    private final SkeletonTile[] skeletonArea;
    private int skeletonIndex;
    private boolean skeletonAvailable;

    private final LandslideTile[] entrance;
    private int entranceIndex;

    public int getMosaicIndex() {
        return mosaicIndex;
    }
    public int getStatueIndex() {
        return statueIndex;
    }
    public int getAmphoraIndex() {
        return amphoraIndex;
    }
    public int getSkeletonIndex() {
        return skeletonIndex;
    }
    public int getEntranceIndex() {
        return entranceIndex;
    }

    /**
     * Initializes Board
     */
    public Board() {
        mosaicArea = new MosaicTile[27];
        mosaicIndex = 0;
        mosaicAvailable = true;
        statueArea = new StatueTile[24];
        statueIndex = 0;
        statueAvailable = true;
        amphoraArea = new AmphoraTile[30];
        amphoraIndex = 0;
        amphoraAvailable = true;
        skeletonArea = new SkeletonTile[30];
        skeletonIndex = 0;
        skeletonAvailable = true;
        entrance = new LandslideTile[24];
        entranceIndex = 0;
    }

    /**
     * Appends a tile to the corresponding area
     * @param t: the tile to append
     */
    public void appendTileToArea(Tile t) {
        if (t instanceof MosaicTile) {
            mosaicArea[mosaicIndex] = (MosaicTile) t;
            mosaicIndex ++;
        }
        if (t instanceof StatueTile) {
            statueArea[statueIndex] = (StatueTile) t;
            statueIndex ++;
        }
        if (t instanceof AmphoraTile) {
            amphoraArea[amphoraIndex] = (AmphoraTile) t;
            amphoraIndex ++;
        }
        if (t instanceof SkeletonTile) {
            skeletonArea[skeletonIndex] = (SkeletonTile) t;
            skeletonIndex ++;
        }
        if (t instanceof LandslideTile) {
            entrance[entranceIndex] = (LandslideTile) t;
            entranceIndex ++;
        }
    }

    /**
     * Function to remove a tile from an area and give it to the user
     * @param tile: the tile to remove
     */
    public Tile pickTile(Tile tile) {
        Tile tileToReturn = null;

        if (tile instanceof MosaicTile) {
            tile = (MosaicTile) tile;
            for (int i = 0; i < mosaicIndex; i ++) {
                if (tile == mosaicArea[i]) {
                    mosaicArea[i] = mosaicArea[mosaicIndex-1];
                    mosaicArea[mosaicIndex-1] = null;
                    mosaicIndex --;
                    return tile;
                }
            }
        }
        else if (tile instanceof StatueTile) {
            tile = (StatueTile) tile;
            for (int i = 0; i < statueIndex; i ++) {
                if (tile == statueArea[i]) {
                    statueArea[i] = statueArea[statueIndex-1];
                    statueArea[statueIndex-1] = null;
                    statueIndex --;
                    return tile;
                }
            }
        }
        else if (tile instanceof AmphoraTile) {
            tile = (AmphoraTile) tile;
            for (int i = 0; i < amphoraIndex; i ++) {
                if (tile == amphoraArea[i]) {
                    amphoraArea[i] = amphoraArea[amphoraIndex-1];
                    amphoraArea[amphoraIndex-1] = null;
                    amphoraIndex --;
                    return tile;
                }
            }
        }
        else if (tile instanceof SkeletonTile) {
            tile = (SkeletonTile) tile;
            for (int i = 0; i < skeletonIndex; i ++) {
                if (tile == skeletonArea[i]) {
                    skeletonArea[i] = skeletonArea[skeletonIndex-1];
                    skeletonArea[skeletonIndex-1] = null;
                    skeletonIndex --;
                    return tile;
                }
            }
        }

        return null;
    }


    //Getters for areas
    public MosaicTile[] getMosaicArea() {
        return mosaicArea;
    }

    public StatueTile[] getStatueArea() {
        return statueArea;
    }

    public AmphoraTile[] getAmphoraArea() {
        return amphoraArea;
    }

    public SkeletonTile[] getSkeletonArea() {
        return skeletonArea;
    }

    public LandslideTile[] getEntrance() {
        return entrance;
    }

    //getters for area availability
    public boolean getMosaicAvailability() {
        return mosaicAvailable;
    }
    public boolean getAmphoraAvailability() {
        return amphoraAvailable;
    }
    public boolean getStatueAvailability() {
        return statueAvailable;
    }
    public boolean getSkeletonAvailability() {
            return skeletonAvailable;
    }

    //setters for area availability
    public void setMosaicAvailable(boolean mosaicAvailable) {
        this.mosaicAvailable = mosaicAvailable;
    }
    public void setAmphoraAvailable(boolean amphoraAvailable) {
        this.amphoraAvailable = amphoraAvailable;
    }
    public void setSkeletonAvailable(boolean skeletonAvailable) {
        this.skeletonAvailable = skeletonAvailable;
    }
    public void setStatueAvailable(boolean statueAvailable) {
        this.statueAvailable = statueAvailable;
    }

    /**
     * Set the availability of a certain area
     * @param type the area to set
     * @param status the availability of the area
     */
    public void setAreaAvailability(AreaType type, boolean status) {
        switch (type) {
            case MOSAIC_AREA:
                setMosaicAvailable(status);
                break;
            case STATUE_AREA:
                setStatueAvailable(status);
                break;
            case SKELETON_AREA:
                setSkeletonAvailable(status);
                break;
            case AMPHORA_AREA:
                setAmphoraAvailable(status);
                break;
        }
    }

    /**
     * Set all areas disabled or enabled
     * @param status the status to set all areas
     */
    public void setAllAreasAvailable(boolean status) {
        setMosaicAvailable(status);
        setAmphoraAvailable(status);
        setSkeletonAvailable(status);
        setStatueAvailable(status);
    }

    /**
     * Function to check if the entrance has been blocked by landslide tiles
     * @return true if the entrance is blocked, false it's not blocked
     */
    public boolean blockedEntrance() {
        return entranceIndex == 23;
    }

}



package Model.Tiles;

import Model.AreaType;

import static Model.AreaType.SKELETON_AREA;
import static Model.AreaType.STATUE_AREA;

public class SkeletonTile extends FindingTile {

    /**
     * The type of the Skeleton Tile
     */
    private SkeletonType type;

    public AreaType correspondingArea;

    /**
     * Construct a new skeleton tile with type t
     * @param t: the type of the new skeleton
     */
    public SkeletonTile(SkeletonType t) {
        this.type = t;
        this.correspondingArea = SKELETON_AREA;
    }

    public SkeletonType getType() {
        return this.type;
    }


    /**
     * Enumeration for the different SkeletonTile types
     */
    public enum SkeletonType {
        ADULT_UPPER,
        ADULT_LOWER,
        CHILDREN_UPPER,
        CHILDREN_LOWER
    }
}

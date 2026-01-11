package Model.Tiles;

import Model.AreaType;
import static Model.AreaType.*;

public class StatueTile extends FindingTile {

    private StatueType type;
    public AreaType correspondingArea;

    /**
     * Constructor for new StatueTile
     * @param t: type of the Statue Tile (SPHINX, CARYATID)
     */
    public StatueTile(StatueType t) {
        this.type = t;
        this.correspondingArea = STATUE_AREA;
    }

    /**
     * Get Statue type
     * @return Statue type (SPHINX, CARYATID)
     */
    public StatueType getType() {
        return this.type;
    }

    /**
     * Set Statue type
     * @param t: statue type (SPHINX, CARYATID)
     */
    public void setType(StatueType t) {
        this.type = t;
    }


    /**
     * Enumeration for all statue types(SPHINX, CARYATID)
     */
    public enum StatueType {
        SPHINX,
        CARYATID
    }
}



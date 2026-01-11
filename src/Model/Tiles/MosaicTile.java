package Model.Tiles;

import Model.AreaType;
import Model.Color;
import Model.Colored;

import static Model.AreaType.MOSAIC_AREA;
import static Model.AreaType.STATUE_AREA;

public class MosaicTile extends FindingTile implements Colored {
    /**
     * The color of the Mosaic Tile
     */
    private Color color;

    public AreaType correspondingArea;

    public MosaicTile(Color c) {
        color = c;
        this.correspondingArea = MOSAIC_AREA;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color c) {
        this.color = c;
    }
}

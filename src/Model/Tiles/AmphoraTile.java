package Model.Tiles;
import Model.AreaType;
import Model.Color;
import Model.Colored;

import static Model.AreaType.AMPHORA_AREA;
import static Model.AreaType.STATUE_AREA;

public class AmphoraTile extends FindingTile implements Colored {
    private Color color;

    private final AreaType correspondingArea;

    public AmphoraTile(Color c) {
        this.color = c;
        this.correspondingArea = AMPHORA_AREA;
    }

    public AreaType getCorrespondingArea() {
        return correspondingArea;
    }

    @Override
    public Color getColor() {return this.color;}

    @Override
    public void setColor(Color c) {this.color = c;}

}

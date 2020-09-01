package runner.generator;

public class Box {
    public static final float BASIC_LENGTH = 0.2f;

    public final Pos xPos;
    public final float yPos;
    public final float length;

    public Box(Pos xPos, float yPos, float length) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.length = length;
    }
}

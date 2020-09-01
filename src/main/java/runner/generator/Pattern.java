package runner.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jme3.math.FastMath;

public class Pattern {
    public final int score;
    private final float length;
    public final List<Box> boxes;

    public Pattern(int score, Box... boxes) {
        this(score, 0, boxes);
    }

    public Pattern(int score, float length, Box... boxes) {
        this.score = score;
        this.length = length;
        this.boxes = new ArrayList<>(Arrays.asList(boxes));
    }

    public void addBoxes(float offset, List<Box> boxes) {
        for (var b : boxes) {
            this.boxes.add(new Box(b.xPos, b.yPos + offset, b.length));
        }
    }

    public float getLength() {
        if (length != 0)
            return length; // if set use it

        float max = 0;
        for (Box b : this.boxes) {
            max = Math.max(max, b.yPos + b.length);
        }
        return FastMath.ceil(max);
    }

    public Pattern mergePatterns(Pattern... patterns) {
        float pos = 0;
        for (Pattern p : patterns) {
            addBoxes(pos, p.boxes);
            pos += p.getLength();
        }

        return this;
    }
}
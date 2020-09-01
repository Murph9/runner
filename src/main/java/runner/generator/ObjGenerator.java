package runner.generator;

import java.util.LinkedList;
import java.util.List;

import com.jme3.math.FastMath;

public class ObjGenerator {

    private static final Pattern Nothing = new Pattern(0, 3);
    private static final Pattern BasicL = new Pattern(1, 3, new Box(Pos.Left, 2, Box.BASIC_LENGTH));
    private static final Pattern BasicM = new Pattern(1, 3, new Box(Pos.Middle, 2, Box.BASIC_LENGTH));
    private static final Pattern BasicR = new Pattern(1, 3, new Box(Pos.Right, 2, Box.BASIC_LENGTH));

    private static final Pattern LR = new Pattern(1, 5, new Box(Pos.Left, 4, Box.BASIC_LENGTH),
            new Box(Pos.Right, 2, Box.BASIC_LENGTH));
    private static final Pattern RL = new Pattern(1, 5, new Box(Pos.Right, 4, Box.BASIC_LENGTH),
            new Box(Pos.Left, 2, Box.BASIC_LENGTH));

    private static final Pattern LeftBlock = new Pattern(1, 3, new Box(Pos.Left, 2, Box.BASIC_LENGTH),
            new Box(Pos.Middle, 2, Box.BASIC_LENGTH));
    private static final Pattern RightBlock = new Pattern(1, 3, new Box(Pos.Right, 2, Box.BASIC_LENGTH),
            new Box(Pos.Middle, 2, Box.BASIC_LENGTH));

    private static final Pattern[] PATTERNS = new Pattern[] { Nothing, BasicL, BasicM, BasicR, LR, RL, LeftBlock, RightBlock};
    
    public ObjGenerator() {
    }

    public Pattern getNext() {
        //TODO some kind of difficulty pattern
        return rand(PATTERNS);
    }

	public Pattern getStart(float startSize) {
        // random easy
        var patternList = new Pattern[] { Nothing, BasicL, BasicM, BasicR };
        List<Pattern> pos = new LinkedList<>();
        float length = 0;
        while (length < startSize) {
            var p = rand(patternList);
            pos.add(p);
            length += p.getLength();
        }
        
		return new Pattern(0,0).mergePatterns(pos.toArray(new Pattern[pos.size()]));
    }
        
    private static <T> T rand(T[] list) {
        return list[FastMath.nextRandomInt(0, list.length -1 )];
    }
}

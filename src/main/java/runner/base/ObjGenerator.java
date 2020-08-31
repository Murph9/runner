package runner.base;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.jme3.math.FastMath;

public class ObjGenerator {

    public enum Pos {
        None(false, false, false),
        Left(true, false, false),
        Middle(false, true, false),
        Right(false, false, true),
        LeftMiddle(true, true, false),
        MiddleRight(false, true, true),
        LeftRight(true, false, true),
        All(true, true, true); // useful?

        private boolean left;
        private boolean middle;
        private boolean right;
        Pos(boolean left, boolean middle, boolean right) {
            this.left = left;
            this.middle = middle;
            this.right = right;
        }
        public boolean ifLeft() {
            return left;
        }
        
        public boolean ifMiddle() {
            return middle;
        }
        
        public boolean ifRight() {
            return right;
        }

        public boolean ifEmpty() {
            return !left && !middle && !right;
        }
    }

    enum Pattern {
        Nothing(Pos.None, Pos.None, Pos.None),
        BasicL(Pos.None, Pos.None, Pos.Left),
        BasicM(Pos.None, Pos.None, Pos.Middle),
        BasicR(Pos.None, Pos.None, Pos.Right),
        ;

        private Pos[] pos;
        Pattern(Pos... pos) {
            this.pos = pos;
        }

        public Pos[] get() {
            return pos;
        }
    }


    private final Queue<Pos> buffer;
    public ObjGenerator() {
        buffer = new LinkedList<>();
    }

    public Pos getNextRow() {
        if (buffer.isEmpty()) {
            appendPattern(rand(Pattern.values()));
        }
        
        return buffer.poll();
    }

	public Pos[] initalRows() {
        // random easy
        var patternList = new Pattern[] { Pattern.BasicL, Pattern.BasicM, Pattern.BasicR};
        List<Pos> pos = new LinkedList<>();
        pos.add(Pos.None);
        pos.add(Pos.None);
        while (pos.size() < 20) {
            pos.addAll(Arrays.asList(rand(patternList).pos));
        }
        
		return pos.toArray(new Pos[pos.size()]);
    }
    
    private void appendPattern(Pattern pattern) {
        appendPos(pattern.pos);
    }
    private void appendPos(Pos ...pos) {
        buffer.addAll(Arrays.asList(pos));
    }

    private static <T> T rand(T[] list) {
        return list[FastMath.nextRandomInt(0, list.length -1 )];
    }
}

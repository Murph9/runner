package runner.base;

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

    public Pos getNextRow() {
        // TODO random
        return Pattern.BasicL.get()[2];
    }

	public Pos[] initalRows() {
        // TODO random
		return new Pos[] { Pos.None, Pos.None, Pos.None, Pos.None, Pos.Left, Pos.Right };
	}
}

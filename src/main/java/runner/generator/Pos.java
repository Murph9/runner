package runner.generator;

public enum Pos {
    Left(-1), Middle(0), Right(1);

    public final int pos;
    Pos(int pos) {
        this.pos = pos;
    }
}
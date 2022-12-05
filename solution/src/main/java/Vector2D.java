public class Vector2D {
    private int[][] vector;
    private int inner;
    private int outer;

    public Vector2D(int[][] v) {
        vector = v;
        inner = 0;
        outer = 0;
    }

    private void advanceToNext() {
        while (outer < vector.length && inner == vector[outer].length) {
            inner = 0;
            outer++;
        }
    }

    public int next() {
        if (!hasNext()) return -1;
        return vector[outer][inner++];
    }

    public boolean hasNext() {
        advanceToNext();
        return outer < vector.length;
    }
}

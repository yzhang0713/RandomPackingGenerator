public class Key {
    // Key object to hold pair of grid values for the grid map
    private int xGrid;
    private int yGrid;

    public Key(int x, int y) {
        xGrid = x;
        yGrid = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Key key = (Key) o;
        return xGrid == key.xGrid && yGrid == key.yGrid;
    }

    @Override
    public int hashCode() {
        int result = xGrid;
        result = 31 * result + yGrid;
        return result;
    }
}

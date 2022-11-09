public class CoordinateSystem {
    final private int xmax;
    final private int ymax;
    final private int zmax;
    private int [][][] matrix;

    public CoordinateSystem(int xmax, int ymax, int zmax) {
        this.xmax = xmax;
        this.ymax = ymax;
        this.zmax = zmax;

        this.matrix = new int[xmax][ymax][zmax];
    }

    public int getXmax() { return xmax; }
    public int getYmax() {
        return ymax;
    }
    public int getZmax() {
        return zmax;
    }
    public int[][][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][][] matrix) {
        this.matrix = matrix;
    }
}

public class Position {
    private double x;
    private double y;
    private double z;
    private double t; //time

    public Position(double x, double y, double z, double t) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getZ() {return z;}
    public double getT() {return t;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setZ(double z) {this.z = z;}
    public void setT(double t) {this.t = t;}

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", t=" + t +
                '}';
    }
}

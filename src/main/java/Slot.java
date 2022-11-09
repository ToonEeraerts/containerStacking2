public class Slot {
    private int id;
    private int x;
    private int y;
    private Container c;

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                "} \n";
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Container getC() {
        return c;
    }

    public void setContainer(Container c){
        this.c = c;
    }
}

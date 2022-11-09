import java.util.Stack;

public class Slot {
    private int id;
    private int x;
    private int y;
    private Stack<Container> containers = new Stack<>();

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", containers=" + containers +
                '}';
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

    public Stack<Container> getContainers() {
        return containers;
    }

    public void addContainer(Container c){
        this.containers.add(c);
    }
}

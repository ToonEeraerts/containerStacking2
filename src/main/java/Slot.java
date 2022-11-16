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

    public Stack<Container> getContainers() {
        return containers;
    }

    public void popContainer(Container c) {
        Container oldContainer = containers.pop();
        if (!c.equals(oldContainer)) throw new IllegalStateException("Container "+c+" was not on top of the stack.");
    }

    public void addContainer(Container c) {
        containers.add(c);
    }

    public int getHeight() {
        return containers.size();
    }

    public boolean hasHeightLeft(int maxHeight) {
        return (containers.size()-maxHeight>0); //Returns true if there's space left
    }

    public boolean stackIsEmpty() {
        return containers.isEmpty();
    }

    public boolean hasSmallContainerOnTop() {
        return containers.peek().getLength()==1;
    }
}

import java.util.Stack;

public class Slot {
    private int id;
    private int x;
    private int y;
    private boolean[] reserved = new boolean[3];
    private Stack<Container> containers;

    public Slot(Slot slot) {
        this.id = slot.id;
        this.x = slot.x;
        this.y = slot.y;
        this.containers = slot.containers;
        for( int i = 0; i < reserved.length; i++){
            reserved[i] = false;
        }
    }

    public Slot() {
        this.id = 0;
        this.x = 0;
        this.y = 0;
        this.containers = new Stack<>();
        for( int i = 0; i < reserved.length; i++){
            reserved[i] = false;
        }
    }

    public boolean[] getReserved() {
        return reserved;
    }

    public void setReserved(int height, boolean reserved) {
        this.reserved[height-1] = reserved;
    }

    public boolean isReserved(int height){
        return reserved[height-1];
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

    public Container peekTop() {
        return containers.peek();
    }

    public void popContainer(Container c) {
        Container oldContainer = containers.peek();
        if (!c.equals(oldContainer)) throw new IllegalStateException("Container "+c+" was not on top of the stack.");
        containers.pop();
    }

    public void addContainer(Container c) {
        containers.push(c);
    }

    public int getHeight() {
        return containers.size();
    }
    public int getContainerHeight(Container c){
        return containers.indexOf(c)+1; //why +1? Imagine no containers, this would return height -1
    }

    public boolean hasHeightLeft(int maxHeight) {
        return maxHeight-containers.size()>=1;
    }

    public boolean stackIsEmpty() {
        return containers.isEmpty();
    }

    public boolean isBeginSlot() {
        if (containers.isEmpty()) return true;
        else {
            Container c = containers.peek();
            return c.isBeginSlot(this);
        }
    }

    public boolean isEndSlot() {
        Container c = peekTop();
        if (c == null) return true;
        else return c.isEndSlot(this);
    }

    @Override
    public String toString() {
        return "Slot{" +
//                "id=" + id +
                "x=" + x +
                ", y=" + y +
//                ", containers=" + containers +
                '}';
    }
}

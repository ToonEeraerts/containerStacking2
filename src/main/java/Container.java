import java.util.ArrayList;
import java.util.Arrays;

public class Container {
    private int id;
    private int length;
    private ArrayList<Slot> slots = null;

    @Override
    public String toString() {
        return "Container{" +
                "id=" + id +
                ", length=" + length +
                ", slots=" + slots +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public void addSlots(Slot slot) {
        this.slots.add(slot);
    }
}

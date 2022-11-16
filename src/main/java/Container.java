import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Container {
    private int id;
    private int length;
    private List<Slot> slots = new ArrayList<>();

    @Override
    public String toString() {
        return "Container{" +
                "id=" + id +
                ", length=" + length +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void addSlot(Slot slot) {
        slots.add(slot);
    }

    public void clearSlots() {
        slots.clear();
    }
}
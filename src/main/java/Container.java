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
                ", slots=" + slots +
                "} \n";
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



    public void move(List<Slot> slotList) {
        // Remove old data
        List<Slot> previousSlots = getSlots();
        for (Slot s : previousSlots) {
            s.popContainer(this);
        }
        clearSlots();

        // Set new data
        for (Slot s : slotList){
            s.addContainer(this);
            addSlot(s);
        }
    }

    /** Constraints for moving **/

    // Slots 1 and 2 are the future slots where we want to place the container
    // Slot 2 can be null for small containers
    public boolean checkConstraints(Slot s1, Slot s2, int maxHeight) {
        // large container
        if (length==2) {
            if (!checkHeight(maxHeight, s1, s2)) return false;
            return checkSupported(s1, s2);
        }
        // small container
        else {
            if (!checkHeight(maxHeight, s1)) return false;
            return checkTopDown(s1);
        }
    }

    public static boolean checkHeight(int maxHeight, Slot s1 , Slot s2) {
        if (s2 == null) return s1.hasHeightLeft(maxHeight);
        return s1.hasHeightLeft(maxHeight) && s2.hasHeightLeft(maxHeight);
    }

    public static boolean checkHeight(int maxHeight, Slot s1){
        return s1.hasHeightLeft(maxHeight);
    }

    // Only necessary for large containers
    public static boolean checkSupported(Slot s1, Slot s2) {
        return s1.getHeight()==s2.getHeight();
    }

    // Only necessary for small containers
    public static boolean checkTopDown(Slot s1) {
        if (s1.stackIsEmpty()) return true;
        return s1.hasSmallContainerOnTop();
    }
}
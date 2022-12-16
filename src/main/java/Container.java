import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Container {
    private int id;
    private int length;
    private int x;
    private int y;
    private List<Slot> slots = new ArrayList<>();
    private Position position;

    public int getId() {
        return id;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getLength() {
        return length;
    }
    public List<Slot> getSlots() {
        return slots;
    }
    public void clearSlots() {
        slots.clear();
    }

    public void updatePosition(){
        int x = slots.get(0).getX();
        int y = slots.get(0).getY();
        double temp = (double) (length-1)/2;
        position = new Position(x+temp, y+0.5, 0, 0);
    }

    public Position getPosition() {
        updatePosition();
        return position;
    }

    public void moveTo(List<Slot> slotList) {
        // Remove old data
        for (Slot s : slots) {
            s.popContainer(this);
        }
        clearSlots();

        // Set new data
        for (Slot s : slotList){
            s.addContainer(this);
            slots.add(s);
        }
    }

    public boolean isBeginSlot(Slot s) {
        Slot beginSlot = slots.get(0);
        return s.equals(beginSlot);
    }

    public boolean isEndSlot(Slot s) {
        Slot endSlot = slots.get(slots.size()-1);
        return s.equals(endSlot);
    }


    /********************************************* Constraints for moving **********************************************/
    public boolean isFeasibleContainerPlacement(List<Slot> slotList, int maxHeight) {
        if (!checkOnTop()) {
            System.out.println("ILLEGAL PLACEMENT: The container is not on top of his current stack.");
            return false;
        }
        if (!checkHeight(slotList, maxHeight)) {
            System.out.println("ILLEGAL PLACEMENT: "+slotList+" The destination stack has no room to add another container.");
            return false;
        }
        if (!checkSupported(slotList)) {
            System.out.println("ILLEGAL PLACEMENT: "+slotList+" The large container is not properly supported: not all slots underneath are occupied.");
            return false;
        }
        if (!checkTopDown(slotList)) {
            System.out.println("ILLEGAL PLACEMENT: "+slotList+" Top down constraint not satisfied.");
            return false;
        }
        return true;
    }

    public boolean checkOnTop() {
        for (Slot s : slots)
            if (s.peekTop()!=this) return false;
        return true;
    }

    public boolean checkHeight(List<Slot> slotList, int maxHeight) {
        for (Slot s : slotList) {
            int tempMaxHeight = maxHeight;
            if (!s.stackIsEmpty() && s.peekTop().equals(this)) tempMaxHeight++;
            if (!s.hasHeightLeft(tempMaxHeight)) return false;
        }
        return true;
    }

    public boolean checkSupported(List<Slot> slotList) {
        // Only necessary for large containers
        if (slotList.size() == 1) return true;

        int[] height = new int[slotList.size()];
        for (int i = 0; i < height.length; i++) {
            Slot s = slotList.get(i);
            height[i] = s.getHeight();
            // If this container is already in one of these slots
            // We simulate removing it by lowering the height
            if (!s.stackIsEmpty() && s.peekTop().equals(this)) height[i]--;
        }

        // Check if al the elements of the array have the same value
        int[] temp = new int[slotList.size()];
        Arrays.fill(temp, height[0]);
        return Arrays.equals(height, temp);
    }

    public boolean checkTopDown(List<Slot> slotList) {
        boolean res = false;

        // If this container is already in this slot
        // We simulate removing it before checking the constraint
        boolean[] isInSlot = new boolean[slotList.size()];
        for (int i = 0; i < isInSlot.length; i++) {
            Slot s = slots.get(i);
            isInSlot[i] = s.peekTop().equals(this);
            if (isInSlot[i]) s.popContainer(this);
        }

        // No problem if all stacks are empty
        boolean allEmpty = true;
        for (Slot s : slotList) {
            if (!s.stackIsEmpty()) allEmpty=false;
        }
        if (allEmpty) res = true;

        // No problem if both edge slots contain the end/beginning of a container
        // No need to check if res is already true
        if (!res) {
            Slot beginSlot = slotList.get(0);
            Slot endSlot = slotList.get(slotList.size()-1);
            res = beginSlot.isBeginSlot() && endSlot.isEndSlot();
        }

        // Putting the container back in place
        for (int i = 0; i < isInSlot.length; i++) {
            if (isInSlot[i]) {
                Slot s = slots.get(i);
                s.addContainer(this);
            }
        }

        return res;
    }
    /********************************************* Constraints for moving **********************************************/


    @Override
    public String toString() {
        return "Container{" +
                "id=" + id +
                ", length=" + length +
                ", slots=" + slots +
                "} \n";
    }
}
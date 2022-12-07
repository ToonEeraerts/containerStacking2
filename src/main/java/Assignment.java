import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Map;

public class Assignment {

    @SerializedName("container_id")
    private int containerId;
    @SerializedName("slot_id")
    private int slotId;

    private Container container;
    private Slot slot;
    private Position slotPosition;

    @Override
    public String toString() {
        return "Assignment{" +
                "containerId=" + containerId +
                ", slotId=" + slotId +
                "} \n";
    }

    public int getContainerId() {
        return containerId;
    }

    public int getSlotId() {
        return slotId;
    }

    public Container getContainer() { return container; }
    public Slot getSlot() { return slot; }
    public Position getSlotPosition() { return slotPosition; }

    public void setContainer(Map<Integer, Container> containersMap) {
        container = containersMap.get(containerId);
    }

    public void setSlot(Map<Integer, Slot> slotsMap) {
        slot = slotsMap.get(slotId);
    }

    public void generateSlotPosition() {
        assert container != null : "Container not yet generated";
        assert slot != null : "Slot not yet generated";
        switch (container.getLength()) {
            case 1: slotPosition = new Position(slot.getX(), slot.getY(), 0, 0); break;
            case 2: slotPosition = new Position(slot.getX()+0.5, slot.getY(), 0, 0); break;
            case 3: slotPosition = new Position(slot.getX()+1, slot.getY(), 0, 0); break;
            default: throw new IllegalStateException("Length not specified: "+container.getLength());
        }
    }

}

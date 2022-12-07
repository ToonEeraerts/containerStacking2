import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Assignment {

    @SerializedName("container_id")
    private int containerId;
    @SerializedName("slot_id")
    private int slotId;

    private Container container;
    private Slot slot;

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

    public void setContainer(Container container) {
        this.container = container;
    }

}

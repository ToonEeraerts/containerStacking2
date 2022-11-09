import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Assignment {
    @SerializedName("container_id")
    private int containerId;
    @SerializedName("slot_id")
    private int[] slotId;

    @Override
    public String toString() {
        return "Assignment{" +
                "containerId=" + containerId +
                ", slotId=" + Arrays.toString(slotId) +
                "} \n";
    }
}

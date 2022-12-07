import com.google.gson.annotations.SerializedName;

import java.util.*;

public class InputData {
    private String name;
    private int length;
    private int width;
    @SerializedName("maxheight")
    private int maxHeight;
    private List<Slot> slots;
    private List<Crane> cranes;
    private List<Container> containers;

    private List<Assignment> assignments;

    public String getName() { return name; }
    public int getLength() {
        return length;
    }
    public int getWidth() {
        return width;
    }
    public int getMaxHeight() {
        return maxHeight;
    }
    public List<Slot> getSlots() {
        return slots;
    }
    public List<Crane> getCranes() {
        return cranes;
    }
    public List<Container> getContainers() {
        return containers;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setName(String name) { this.name = name; }
    public void setLength(int length) {
        this.length = length;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }
    public void setCranes(List<Crane> cranes) {
        this.cranes = cranes;
    }
    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }
    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

//    public void linkAssignments() {
//        Map<Integer,Container> containersMap = new HashMap<>();
//        for(Container c: containers){
//            containersMap.put(c.getId(),c);
//        }
//
//        for (Assignment a : assignments) {
//            a.setContainer(containersMap);
//            a.setSlot(slotMap);
//        }
//    }


    public void initialAssignments() {
        // Initial assignments
        for(Assignment a: assignments){

            // Search the container with id from assignment
            int conID = a.getContainerId();
            Container c = null;
            for(Container container: containers)
                if(conID == container.getId())
                    c=container;

            // Search the slots with the ids from assignment
            List<Slot> slotList = new ArrayList<>();
            int slotId = a.getSlotId();
            for(Slot slot: slots)
                if(slotId == slot.getId())
                    slotList.add(slot);

            //Add a list with all the slots the container stands on to the container
            c.move(slotList);

            // Search the slot with id from assignment + the slot to the right of it
            int slotID = a.getSlotId();
            int slotID2 = slotID + 1;
            Slot s = null;
            Slot s2 = null;
            for(Slot slot: slots){
                if(slotID == slot.getId())
                    s = slot;
                else if(slotID2 == slot.getId()){
                    s2 = slot;
                }
            }

            //Search the containers with the ids from assignment
            int containerID = a.getContainerId();
            for(Container container : containers){
                if(containerID == container.getId()){
                    s.addContainer(container);
                    s2.addContainer(container);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "InputData{" +
                "name='" + name + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", maxHeight=" + maxHeight +
                ", slots=" + slots +
                ", cranes=" + cranes +
                ", containers=" + containers +
                ", assignments=" + assignments +
                '}';
    }
}

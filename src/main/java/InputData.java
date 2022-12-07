import com.google.gson.annotations.SerializedName;

import java.util.*;

public class InputData {
    // Data from json file
    private String name;
    private int length;
    private int width;
    @SerializedName("maxheight")
    private int maxHeight;
    private List<Slot> slots;
    private List<Crane> cranes;
    private List<Container> containers;
    private List<Assignment> assignments;

    // Generated data
    private Map <Integer, Container> containersMap;
    private Map <Integer, Slot> slotsMap;

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
    public List<Crane> getCranes() {
        return cranes;
    }
    public List<Slot> getSlots() { return slots; }
    public List<Assignment> getAssignments() {
        return assignments;
    }

    public Map<Integer, Container> getContainersMap() {
        return containersMap;
    }
    public Map<Integer, Slot> getSlotsMap() {
        return slotsMap;
    }

    public void generateInput() {
        // Create maps
        containersMap = new HashMap<>();
        slotsMap = new HashMap<>();
        for (Container c : containers) containersMap.put(c.getId(), c);
        for (Slot s : slots) slotsMap.put(s.getId(), s);
        initialAssignments();

        generateAssignments(containersMap, slotsMap);
    }

    public void generateAssignments(Map<Integer, Container> containersMap, Map<Integer, Slot> slotsMap) {
        // Put objects in the assignments
        for (Assignment a : assignments) {
            a.setContainer(containersMap);
            a.setSlot(slotsMap);
            a.generateSlotPosition();
        }
    }



    public void initialAssignments() {
        System.out.println(slots.get(0).getContainers());
        // Initial assignments
        for(Assignment a: assignments){
            System.out.println("1" + slots.get(0).getContainers());
            // Search the container with id from assignment
            int conID = a.getContainerId();
            Container c = null;
            for(Container container: containers)
                if(conID == container.getId())
                    c=container;

            // Search the slots with the ids from assignment
            List<Slot> slotList = new ArrayList<>();
            int[] slotIDs = new int[c.getLength()];
            slotIDs[0] = a.getSlotId();
            for(int i = 0; i < slotIDs.length; i++){
                slotIDs[i] = slotIDs[0] + i;
                for(Slot slot: slots){
                    if(slotIDs[i] == slot.getId()){
                        slotList.add(slot);
                    }
                }
            }
            //Add a list with all the slots the container stands on to the container
            c.move(slotList);
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

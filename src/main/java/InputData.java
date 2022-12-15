import com.google.gson.annotations.SerializedName;

import java.util.*;

public class InputData {
    // Data from json file
    private String name;
    private int length;
    private int width;
    @SerializedName("maxheight")
    private int maxHeight;
    @SerializedName("targetheight")
    private int targetHeight;
    private List<Slot> slots;
    private List<Crane> cranes;
    private List<Container> containers;
    private List<Assignment> assignments;

    // Generated data
    private Map <Integer, Container> containersMap;
    private Map <Integer, Slot> slotsMap;

    public String getName() {
        return name;
    }
    public int getLength() {
        return length;
    }
    public int getWidth() {
        return width;
    }
    public int getMaxHeight() {
        return maxHeight;
    }
    public int getTargetHeight() {
        return targetHeight;
    }
    public List<Crane> getCranes() {
        return cranes;
    }
    public List<Slot> getSlots() {
        return slots;
    }
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
        generateAssignments(containersMap, slotsMap);
        for (Assignment a : assignments) a.generateSlotList(slots);
        initialAssignments();
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
        for(Assignment a: assignments){
            Container container = a.getContainer();

            List<Slot> slotList = new ArrayList<>();
            Slot assignmentSlot = a.getSlot();
            slotList.add(assignmentSlot);

            int index = slots.indexOf(assignmentSlot);
            for (int i = 1; i < container.getLength(); i++) {
                slotList.add(slots.get(index+i));
            }

            //Add a list with all the slots the container stands on to the container
            container.moveTo(slotList);
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

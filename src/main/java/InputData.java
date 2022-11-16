import java.util.ArrayList;
import java.util.List;

public class InputData {
    private String name;
    private List<Container> containers;
    private List<Slot> slots;
    private List<Assignment> assignments;

    public String getName() { return name; }
    public List<Container> getContainers() {
        return containers;
    }
    public List<Slot> getSlots() {
        return slots;
    }
    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setName(String name) { this.name = name; }
    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }
    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }
    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void initialAssignments() {
        // Initial assignments
        // For future replacements, use stack
        for(Assignment a: assignments){
            int conID = a.getContainerId();

            // Search the container with id from assignment
            Container c = null;
            for(Container container: containers)
                if(conID == container.getId())
                    c=container;

            // Search the slots with the ids from assignment
            List<Slot> slotList = new ArrayList<>();
            int[] slotIds = a.getSlotId();
            for(int slotId : slotIds)
                for(Slot slot: slots)
                    if(slotId == slot.getId())
                        slotList.add(slot);

            c.move(slotList);
        }
    }

    @Override
    public String toString() {
        return "InputData{" +
                "name='" + name +
                ", \n containers= \n" + containers +
                ", \n slots= \n" + slots +
                ", \n assignments= \n" + assignments +
                '}';
    }
}

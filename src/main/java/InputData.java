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

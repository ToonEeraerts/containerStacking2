import java.util.List;

public class InputData {
    private String name;
    private List<Container> containers;
    private List<Slot> slots;
    private List<Assignment> assignments;

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


import java.util.ArrayList;
import java.util.List;

public class UltimateTrajectory {

    // This list contains alle the trajectories needed to move 1 container to its target position
    // More than one container can be moved in order to make way
    private List<Trajectory> trajectories;

    public UltimateTrajectory(Container container) {
        trajectories = new ArrayList<>();
    }

    public void addTrajectory(Trajectory trajectory) {
        trajectories.add(trajectory);
    }

    // Totale tijd teruggeven afh van de beginpositie van de kraan



}

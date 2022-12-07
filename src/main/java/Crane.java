import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Crane {
    private int id;
    private float x;
    private float y;
    private float xmin;
    private float xmax;
    private float ymin;
    private float ymax;
    private float xspeed;
    private float yspeed;

    private List<UltimateTrajectory> ultimateTrajectories;
    private List<Crane> otherCranes;

    public Crane(int id, float x, float y, float xmin, float xmax, float ymin, float ymax, float xspeed, float yspeed) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.xspeed = xspeed;
        this.yspeed = yspeed;
        ultimateTrajectories = new ArrayList<>();
    }

    public void addOtherCranes(List<Crane> cranes) {
        otherCranes = new ArrayList<>();
        for (Crane c : cranes) {
            if (!c.equals(this)) otherCranes.add(c);
        }
    }

    // Calculate all the possible UltimateTrajectories
    public void generateUltimateTrajectories(List<Assignment> currentAssignments, List<Assignment> targetAssignments) {
        for (Assignment a : currentAssignments) {
            // todo wat als er container onderaan stack
            // Move to the container
            Position cranePosition = new Position(x, y, 0, 0);
            Container container = a.getContainer();
            Position containerPosition = container.getPosition();

            Movement moveToContainer = new Movement(0, cranePosition, containerPosition, xspeed, yspeed, container);
            Trajectory trajectoryToContainer = moveToContainer.calculateTrajectory();

            // Move the container to his destination
            Position targetPosition = null;
            for (Assignment ta : targetAssignments) {
                if (ta.getContainer().equals(container)) { // We found the matching target Assignment
                    targetPosition = ta.getSlotPosition(); break;
                }
            }
            assert targetPosition != null : "Matching target assignment not found";

            Movement moveToTargetLocation = new Movement(0, containerPosition, targetPosition, xspeed, yspeed, container);
            Trajectory trajectoryToTargetLocation = moveToTargetLocation.calculateTrajectory();

            UltimateTrajectory ultimateTrajectory = new UltimateTrajectory(container);
            ultimateTrajectory.addTrajectory(trajectoryToContainer);
            ultimateTrajectory.addTrajectory(trajectoryToTargetLocation);

            ultimateTrajectories.add(ultimateTrajectory);
        }
    }


    // If idle: Neem beste UltimateTrajectory die niet botst met andere kranen en nog niet uitgevoerd!
    public void executeUltimateTrajectory() {


    }



    @Override
    public String toString() {
        return "Crane{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", xmin=" + xmin +
                ", xmax=" + xmax +
                ", ymin=" + ymin +
                ", ymax=" + ymax +
                ", xspeed=" + xspeed +
                ", yspeed=" + yspeed +
                '}';
    }
}

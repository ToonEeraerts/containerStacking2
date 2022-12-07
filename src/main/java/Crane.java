import java.util.ArrayList;
import java.util.List;

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
    private UltimateTrajectory currentUT;

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

    public UltimateTrajectory getCurrentTrajectory(int timer) {
        if (currentUT.isBusy(timer)) return currentUT;
        else return null;
    }

    public void addOtherCranes(List<Crane> cranes) {
        otherCranes = new ArrayList<>();
        for (Crane c : cranes) {
            if (!c.equals(this)) otherCranes.add(c);
        }
    }

    // Returns th assignment it will complete
    public Assignment executeNextMove(int timer, List<Assignment> todoAssignments, List<Assignment> targetAssignments) {
        generateAllUltimateTrajectories(todoAssignments, targetAssignments);
        UltimateTrajectory toExecute = null;


        // Check if safety distance is respected
        // Keep looking for other toExecute until a safe one is found;
        boolean safe = false;
        while (!safe) {
            toExecute = selectBestUltimateTrajectory();
            List<UltimateTrajectory> otherTrajectories = new ArrayList<>();
            for (Crane c : otherCranes) otherTrajectories.add(c.getCurrentTrajectory(timer));
            for (UltimateTrajectory ut : otherTrajectories) {
                safe = true;
                if (ut != null) {
                    if (isNotSafe(1, toExecute, ut)) {
                        ultimateTrajectories.remove(toExecute);
                        safe = false;
                        break;
                    }
                }
            }
        }

        // execute toExecute
        currentUT = toExecute;



        // Return the completed assignment
//        for (Assignment a : todoAssignments) {
//            if (toExecute.getContainer().equals(a.getContainer()))
//                return a;
//        }

        return null;
    }

    // Calculate all the possible UltimateTrajectories
    public void generateAllUltimateTrajectories(List<Assignment> todoAssignments, List<Assignment> targetAssignments) {
        for (Assignment a : todoAssignments) {
            // todo wat als er container onderaan stack
            // Move to the container
            Position cranePosition = new Position(x, y, 0, 0);
            Container container = a.getContainer();
            Position containerPosition = container.getPosition();

            Movement moveToContainer = new Movement(0, cranePosition, containerPosition, xspeed, yspeed, container);

            // Move the container to his destination
            Position targetPosition = null;
            for (Assignment ta : targetAssignments) {
                if (ta.getContainer().equals(container)) { // We found the matching target Assignment
                    targetPosition = ta.getSlotPosition(); break;
                }
            }
            assert targetPosition != null : "Matching target assignment not found";

            Movement moveToTargetLocation = new Movement(0, containerPosition, targetPosition, xspeed, yspeed, container);

            UltimateTrajectory ultimateTrajectory = new UltimateTrajectory(container);
            ultimateTrajectory.addMovement(moveToContainer);
            ultimateTrajectory.addMovement(moveToTargetLocation);

            ultimateTrajectories.add(ultimateTrajectory);
        }
    }

    public UltimateTrajectory selectBestUltimateTrajectory() {
        double minimumTime = Double.MAX_VALUE;
        UltimateTrajectory best = null;
        for (UltimateTrajectory ut : ultimateTrajectories) {
//            if (ut.getTime() < minimumTime) {
//                minimumTime = ut.getTime();
//                best = ut;
//            }
        }
        return best;
    }

    // Checks if two trajectories won't collide
    // True in case the trajectories come closer than margin
    public boolean isNotSafe(double margin, UltimateTrajectory t1, UltimateTrajectory t2) {
        // If the cranes don't cross we skip the heavy calculations
        // t1 is on the left-hand side
        if (t1.getLeftBound() < t2.getLeftBound())
            if (t1.getRightBound() <= t2.getLeftBound()+margin)
                return true;

        // t2 is on the left-hand side
        if (t2.getLeftBound() < t1.getLeftBound())
            if (t2.getRightBound() <= t1.getLeftBound()+margin)
                return true;

        return false;
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

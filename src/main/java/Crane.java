import java.util.ArrayList;
import java.util.List;

public class Crane {
    private int id;
    private double x;
    private double y;
    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;
    private double xspeed;
    private double yspeed;
    private int maxHeight;

    private List<Trajectory> trajectories;
    private List<Crane> otherCranes;
    private Trajectory currentTrajectory;

    public int getId() {
        return id;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getXmin() {
        return xmin;
    }
    public double getXmax() {
        return xmax;
    }
    public double getYmin() {
        return ymin;
    }
    public double getYmax() {
        return ymax;
    }
    public double getXspeed() {
        return xspeed;
    }
    public double getYspeed() {
        return yspeed;
    }

    public void setCurrentPosition(Position position) {
        x = position.getX();
        y = position.getY();
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Trajectory getCurrentTrajectory(int timer) {
        if (currentTrajectory == null) return null;
        else if (currentTrajectory.isBusy(timer)) return currentTrajectory;
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
        generateAllTrajectories(todoAssignments, targetAssignments);
        Trajectory toExecute = null;

        // Check if safety distance is respected
        // Keep looking for other toExecute until a safe one is found;
        boolean safe = false;
        while (!safe) {
            toExecute = selectBestTrajectory();
            List<Trajectory> otherTrajectories = new ArrayList<>();
            for (Crane c : otherCranes) otherTrajectories.add(c.getCurrentTrajectory(timer));
            for (Trajectory t : otherTrajectories) {
                safe = true;
                if (t != null) {
                    if (isNotSafe(1, toExecute, t)) {
                        trajectories.remove(toExecute);
                        safe = false;
                        break;
                    }
                }
            }
        }

        if (toExecute != null) {
            // execute toExecute
            currentTrajectory = toExecute;
            toExecute.execute(this, timer);

            // Return the completed assignment
            Assignment done = null;
            for (Assignment a: todoAssignments)
                if (a.getContainer() == toExecute.getContainer())
                    done = a;
            todoAssignments.remove(done);

            return done;
        }
        else return null;

    }

    // Calculate all the possible UltimateTrajectories
    public void generateAllTrajectories(List<Assignment> todoAssignments, List<Assignment> targetAssignments) {
        trajectories = new ArrayList<>();
        for (Assignment a : todoAssignments) {

            // todo wat als er container onderaan stack

            // Move to the container is added with beginPosition 0
            // We will later edit this if we know the real beginPosition
            Position beginPosition = new Position(0, 0, 0, 0);
            Container container = a.getContainer();
            Position containerPosition = container.getPosition();
            Movement moveToContainer = new Movement(0, beginPosition, containerPosition, xspeed, yspeed, container);

            // Move the container to his destination
            Position targetPosition = null;
            Assignment targetAssignment = null;
            for (Assignment ta : targetAssignments) {
                if (ta.getContainer().equals(container)) { // We found the matching target Assignment
                    targetAssignment = ta;
                    targetPosition = ta.getSlotPosition();
                    break;
                }
            }
            assert targetPosition != null : "Matching target assignment not found";

            Movement moveToTargetLocation = new Movement(0, containerPosition, targetPosition, xspeed, yspeed, container);

            if (moveToTargetLocation.isFeasibleContainerPlacement(targetAssignment.getSlotList(), maxHeight)) {
                Trajectory trajectory = new Trajectory(container);
                trajectory.addMovement(moveToContainer);
                trajectory.addMovement(moveToTargetLocation);

                trajectories.add(trajectory);
            }

        }
    }

    public Trajectory selectBestTrajectory() {
        double minimumTime = Double.MAX_VALUE;
        Trajectory best = null;
        for (Trajectory t : trajectories) {
            if (t.getExecutionTime(this) < minimumTime) {
                if (t.compatibleWithCrane(this)) {
                    minimumTime = t.getExecutionTime(this);
                    best = t;
                }
            }
        }
        return best;
    }

    // Checks if two trajectories won't collide
    // True in case the trajectories come closer than margin
    public boolean isNotSafe(double margin, Trajectory t1, Trajectory t2) {
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

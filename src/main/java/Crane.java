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


        toExecute = trajectories.get(0); // voorlopig pakken we gewoon de eerste

        // todo met iets dat lijkt op hieronder blijven zoeken tot we een safe trajectory vinden
        // Check if safety distance is respected
        // Keep looking for other toExecute until a safe one is found;
//        boolean safe = false;
//        while (!safe) {
//            toExecute = selectBestTrajectory();
//            List<Trajectory> otherTrajectories = new ArrayList<>();
//            for (Crane c : otherCranes) otherTrajectories.add(c.getCurrentTrajectory(timer));
//            for (Trajectory ut : otherTrajectories) {
//                safe = true;
//                if (ut != null) {
//                    if (isNotSafe(1, toExecute, ut)) {
//                        trajectories.remove(toExecute);
//                        safe = false;
//                        break;
//                    }
//                }
//            }
//        }

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

    // Calculate all the possible UltimateTrajectories
    public void generateAllTrajectories(List<Assignment> todoAssignments, List<Assignment> targetAssignments) {
        trajectories = new ArrayList<>();
        for (Assignment a : todoAssignments) {
            // todo wat als er container onderaan stack

            // Move to the container is added in the Trajectory when we know where the beginPosition of the crane is
            // Move the container to his destination
            Container container = a.getContainer();
            Position containerPosition = container.getPosition();
            Position targetPosition = null;
            for (Assignment ta : targetAssignments) {
                if (ta.getContainer().equals(container)) { // We found the matching target Assignment
                    targetPosition = ta.getSlotPosition();
                    break;
                }
            }
            assert targetPosition != null : "Matching target assignment not found";

            Movement moveToTargetLocation = new Movement(0, containerPosition, targetPosition, xspeed, yspeed, container);

            Trajectory trajectory = new Trajectory(container);
            trajectory.addMovement(moveToTargetLocation);

            trajectories.add(trajectory);
        }
    }

    public Trajectory selectBestTrajectory() {
        double minimumTime = Double.MAX_VALUE;
        Trajectory best = null;
        for (Trajectory t : trajectories) {
            if (t.getExecutionTime(this) < minimumTime) {
                minimumTime = t.getExecutionTime(this);
                best = t;
            }
        }
        System.out.println("Best trajectory: "+best);
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

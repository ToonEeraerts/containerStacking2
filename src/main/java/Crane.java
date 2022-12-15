import java.util.ArrayList;
import java.util.List;

public class Crane implements Comparable<Crane> {
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
    private int margin;
    private double finishTime = 0;

    private List<Trajectory> trajectories;
    private List<Crane> otherCranes;
    private List<Slot> allSlots;
    private Trajectory currentTrajectory;
    private Trajectory makeWayTrajectory;
    private boolean passAlongExecuted;

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
    public double getFinishTime() {
        return finishTime;
    }

    public int compareTo(Crane otherCrane) {
        return Double.compare(getFinishTime(), otherCrane.getFinishTime());
    }

    public void setCurrentPosition(Position position) {
        x = position.getX();
        y = position.getY();
    }
    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }
    public void setMargin(int margin) {
        this.margin = margin;
    }
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
    public void setAllSlots(List<Slot> allSlots) {
        this.allSlots = allSlots;
    }

    public boolean isInCraneReach(Position p) {
        boolean xOk = xmin<=p.getX() && p.getX()<=xmax;
        boolean yOk = ymin<=p.getY() && p.getY()<=ymax;
        return xOk && yOk;
    }

    public Trajectory getCurrentTrajectory(double timer) {
        // If currentTrajectory is finished we return
        // our finishPosition in the format of a trajectory
        if (timer >= finishTime) {
            Position p = new Position(x, y, 0, 0);
            Movement m = new Movement(0, p, p, xspeed, yspeed, null);
            Trajectory t = new Trajectory(null);
            t.addMovement(m);
            return t;
        }
        else {
            return currentTrajectory;
        }
    }

    public void makeWayFor(Trajectory trajectory, Crane otherCrane, int margin) {
        Position target = null;
        if (otherCrane.getX() < x) { // The crane is on our left-hand side
            double rightBound = trajectory.getRightBound();
            target = new Position(rightBound+margin, y, 0, 0);
        }
        else { // The crane is on our right-hand side
            double leftBound = trajectory.getLeftBound();
            target = new Position(leftBound-margin, y, 0, 0);
        }

        Position current = new Position(x, y, 0, 0);
        Movement m = new Movement(0, current, target, xspeed, yspeed, null);
        Trajectory t = new Trajectory(null);
        t.addMovement(m);
        makeWayTrajectory = t;
    }

    public void addOtherCranes(List<Crane> cranes) {
        otherCranes = new ArrayList<>();
        for (Crane c : cranes) {
            if (!c.equals(this)) otherCranes.add(c);
        }
    }

    // Returns th assignment it will complete
    public Assignment executeNextMove(double timer, List<Assignment> todoAssignments) {
//        System.out.println("Kraan "+id+" aan de beurt.");
        if (passAlongExecuted) {
            // We already did a pass along, so now we return to give the other cranes a chance
            passAlongExecuted = false;
            return null;
        }

        // todo optioneel: kijken of we met een gewone trajectory ook niet genoeg aan de kant gaan
        // If there is a makeWayTrajectory it is always handled of first
        Trajectory toExecute = null;
        if (makeWayTrajectory != null) {
            toExecute = makeWayTrajectory;
            currentTrajectory = toExecute;
            finishTime = toExecute.execute(this, timer);
            makeWayTrajectory = null;
            return null;
        }


        else {
            generateAllTrajectories(todoAssignments, timer);
            int numberOfTrajectories = trajectories.size();
            List<Trajectory> otherTrajectories = new ArrayList<>();


            // Check if safety distance is respected
            // Keep looking for other toExecute until a safe one is found;
            boolean safe = false;
            while (!safe) {
                toExecute = selectBestTrajectory();
                for (Crane c : otherCranes) otherTrajectories.add(c.getCurrentTrajectory(timer));
                for (Trajectory t : otherTrajectories) {
                    safe = true;
                    if (t != null && toExecute != null) {
                        if (!isSafe(margin, toExecute, t)) {
                            System.out.println("ILLEGAL MOVEMENT: The cranes would not respect the safety margin");
                            trajectories.remove(toExecute);
                            safe = false;
                            break;
                        }
                    }
                }
            }
            // If no trajectory was safe and there are still trajectories
            // We ask the other cranes to make room
            if (toExecute == null && numberOfTrajectories>0) {
                generateAllTrajectories(todoAssignments, timer);
                Trajectory trajectory = selectBestTrajectory(); // trajectory that is being blocked
                // Find the first blocking crane
                for (Crane c : otherCranes) {
                    if (c.getCurrentTrajectory(timer) != null) {
                        c.makeWayFor(trajectory, this, margin);
                    }
                }
            }


            Assignment done = null;
            if (toExecute != null) {
                // execute toExecute
                currentTrajectory = toExecute;
                finishTime = toExecute.execute(this, timer);

                // Return the completed assignment
                for (Assignment a: todoAssignments)
                    if (a.getContainer() == toExecute.getContainer())
                        done = a;
//            System.out.println("finishtime na uitvoeren: "+finishTime);
            }
            return done;
        }
    }

    // Calculate all the possible UltimateTrajectories
    public void generateAllTrajectories(List<Assignment> todoAssignments, double timer) {

        trajectories = new ArrayList<>();
        for (Assignment a : todoAssignments) {

            // todo wat als er container onderaan stack staat

            // Move to the container is added with a compatible beginPosition
            Position cranePosition = new Position(x, y, 0, 0);
            Container container = a.getContainer();
            Position containerPosition = container.getPosition();
            Movement moveToContainer = new Movement(0, cranePosition, containerPosition, xspeed, yspeed, container);

            // Move the container to his destination
            Position targetPosition = a.getContainerCenter();
            Movement moveToTargetLocation = new Movement(0, containerPosition, targetPosition, xspeed, yspeed, container);

            if (moveToTargetLocation.isFeasibleContainerPlacement(a.getSlotList(), maxHeight)) {
                Trajectory trajectory = new Trajectory(container);
                trajectory.addMovement(moveToContainer);
                trajectory.addMovement(moveToTargetLocation);

                if (trajectory.compatibleWithCrane(this)) trajectories.add(trajectory);
                else checkIfPassAlongNeeded(trajectory, a, timer);
            }

        }
//        System.out.println(trajectories);
    }

    public Trajectory selectBestTrajectory() {
        double minimumTime = Double.MAX_VALUE;
        Trajectory best = null;
        for (Trajectory t : trajectories) {
            if (t.getTimeToContainer() < minimumTime) {
                minimumTime = t.getTimeToContainer();
                best = t;
            }
        }
//        System.out.println("best: "+best);
        return best;
    }

    // Checks if two trajectories won't collide
    // True in case the trajectories come closer than margin
    public boolean isSafe(double margin, Trajectory t1, Trajectory t2) {

        // If the cranes don't cross we skip the heavy calculations
        // t1 is on the left-hand side
        if (t1.getLeftBound() < t2.getLeftBound())
            if (t1.getRightBound() <= t2.getLeftBound()+margin)
                return true;

        // t2 is on the left-hand side
        if (t2.getLeftBound() < t1.getLeftBound())
            if (t2.getRightBound() <= t1.getLeftBound()-margin)
                return true;

        return false;
    }

    // If not compatible check if it is compatible with another crane
    // If yes, do nothing and leave it for the others
    // If not move it so another crane can pick it up
    public void checkIfPassAlongNeeded(Trajectory fullTrajectory, Assignment a, double timer) {

        boolean compatibleWithOtherCrane = false;
        for (Crane c : otherCranes) {
            if (fullTrajectory.compatibleWithCrane(c)) {
                compatibleWithOtherCrane = true;
                break;
            }
        }
        if (!compatibleWithOtherCrane && isInCraneReach(fullTrajectory.getPickUpPosition())) {
            // Move as far as possible towards destination
            Position destination = fullTrajectory.getDestination();

            double left, right;
            Assignment assignment = new Assignment(a);
            if (destination.getX()<=fullTrajectory.getLeftBound()) { // Destination is to our left
                left = xmin;
                right = xmin+4; //todo niet goed, moet afhangen van bereik andere kraan
            }
            else { // Destination is to our right
                left = xmax-4; // todo niet goed
                right = xmax;
            }
            Position tempDestination = assignment.getFeasiblePlacementPosition(left, right, ymin, ymax, allSlots, maxHeight);
            System.out.println("CRANE PASS ALONG TO: "+tempDestination);

            // Creating the pass along trajectory
            Trajectory t = new Trajectory(fullTrajectory.getContainer());
            Movement moveToContainer = fullTrajectory.getFirstMovement();
            Movement moveTowardOtherCrane = new Movement(0, moveToContainer.getP2(), tempDestination, xspeed, yspeed, fullTrajectory.getContainer());
            t.addMovement(moveToContainer);
            t.addMovement(moveTowardOtherCrane);

            // We execute pass along immediately
            // If not it is possible that the slot to place the container is no longer available
            currentTrajectory = t;
            finishTime = t.execute(this, timer);
            Container container = a.getContainer();
            container.moveTo(assignment.getSlotList());
            passAlongExecuted = true;

            // todo kraan terug aan de kant zetten

        }
    }


    @Override
    public String toString() {
        return "Crane{" +
                "id=" + id +
//                ", x=" + x +
//                ", y=" + y +
//                ", xmin=" + xmin +
//                ", xmax=" + xmax +
//                ", ymin=" + ymin +
//                ", ymax=" + ymax +
//                ", xspeed=" + xspeed +
//                ", yspeed=" + yspeed +
//                ", maxHeight=" + maxHeight +
                ", finishTime=" + finishTime +
//                ", trajectories=" + trajectories +
//                ", currentTrajectory=" + currentTrajectory +
                '}';
    }
}

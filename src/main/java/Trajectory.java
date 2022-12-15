
import java.util.ArrayList;
import java.util.List;


public class Trajectory {

    // This list contains alle the movements needed to move 1 container to its target position
    // More than one container can be moved in order to make way
    private List<Movement> movements;
    private Container container;

    public Trajectory(Container container) {
        this.container = container; // The main container we put in his target position
        movements = new ArrayList<>();
    }
    public Container getContainer() {
        return container;
    }

    public void addMovement(Movement movement) {
        movements.add(movement);
    }



    // Totale tijd teruggeven afh van de beginpositie van de kraan
    public double getExecutionTime(Crane c) {
        // Edit the movement towards the container
        Position cranePosition = new Position(c.getX(), c.getY(), 0, 0);
        movements.get(0).setP1(cranePosition);

        double duration = 0;
        for (Movement m : movements) {
            m.setSpeed(c);
            duration += m.getDuration();
        }
        return duration;
    }

    public double getTimeToContainer() {
        // Edit the movement towards the container
//        Position cranePosition = new Position(c.getX(), c.getY(), 0, 0);
//        movements.get(0).setP1(cranePosition);
//        System.out.println("time to container "+container.getId()+", met kraan "+c.getId()+" : "+movements.get(0).getDuration());
        return movements.get(0).getDuration();
    }


    // Returns furthest x-coordinate
    public double getLeftBound() {
        double res = Integer.MAX_VALUE;
        for (Movement m : movements)
            if (m.getLeftBound()<res) res = m.getLeftBound();
        return res;
    }
    // Returns furthest x-coordinate
    public double getRightBound() {
        double res = 0;
        for (Movement m : movements)
            if (m.getRightBound()>res) res = m.getRightBound();
        return res;
    }
    public double getTopBound() {
        double res = Integer.MAX_VALUE;
        for (Movement m : movements)
            if (m.getTopBound()<res) res = m.getTopBound();
        return res;
    }
    public double getBottomBound() {
        double res = 0;
        for(Movement m : movements)
            if (m.getBottomBound()>res) res = m.getBottomBound();
        return res;
    }

    public boolean compatibleWithCrane(Crane c) {
        double left = getLeftBound();
        double right = getRightBound();
        boolean xOk = c.getXmin() <= left && right <= c.getXmax();

        double top = getTopBound();
        double bottom = getBottomBound();
        boolean yOk = c.getYmin() <= top && bottom <= c.getYmax();

        return xOk && yOk;
    }

    // returns the finishTime
    public double execute(Crane crane, double timer) {
        // Only for empty trajectories is the first move printed as this is the only move
        if (container == null) {
            Movement m = movements.get(0);
            m.executeMovement(crane.getId(), timer);
            timer += m.getDuration();
            crane.setCurrentPosition(m.getP2());
        }
        else { // In all other normal cases:
            // The first movement (moveToContainer) is not printed
            // We only need its duration for the startTime of the next movement
            timer += movements.get(0).getDuration();

            for (int i = 1; i < movements.size(); i++) {
                Movement m = movements.get(i);
                m.executeMovement(crane.getId(), timer);
                timer += m.getDuration();
                crane.setCurrentPosition(m.getP2());
            }
        }
        return timer;
    }


    @Override
    public String toString() {
        return "Trajectory{" +
                "movements=" + movements +
                '}';
    }
}

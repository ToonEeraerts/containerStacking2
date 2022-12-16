
import java.util.ArrayList;
import java.util.List;


public class Trajectory {

    private List<Movement> movements;
    private Container container;

    public Trajectory(Container container) {
        this.container = container; // The main container we put in his target position
        movements = new ArrayList<>();
    }
    // Attention no deep copy of movements
    public Trajectory TrajectoryCopy() {
        Trajectory res = new Trajectory(container);
        for (Movement m : movements)
            res.addMovement(m);
        return res;
    }
    public Container getContainer() {
        return container;
    }
    public void addMovement(Movement movement) {
        movements.add(movement);
    }
    public void removeMovement(int i) {
        movements.remove(i);
    }
    public Movement getFirstMovement() {
        return movements.get(0);
    }
    public Position getDestination() {
        Movement m = movements.get(movements.size()-1);
        return m.getP2();
    }
    public Position getPickUpPosition() {
        Movement m = movements.get(0);
        return m.getP2();
    }
    public List<Movement> getMovements() {
        return movements;
    }
    public void setStartCranePosition(Position p) {
        Movement m = movements.get(0);
        m.setP1(p);
    }
    public double getTimeToContainer() {
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

    public void setDestination(Position position) {
        Movement m = movements.get(movements.size()-1);
        m.setP2(position);
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

    public void execute(Crane crane, double timer) {
        // Only for empty trajectories is the first move printed as this is the only move
        if (container == null) {
            Movement m = movements.get(0);
            m.executeMovement(crane.getId(), timer);
            timer += m.getDuration();
            crane.setCurrentPosition(m.getP2());
            crane.setFinishTime(timer);
            crane.setTimer(timer);
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
                crane.setFinishTime(timer);
                crane.setTimer(timer);
            }
        }
    }


    @Override
    public String toString() {
        return "Trajectory{" +
                "movements=" + movements +
                '}';
    }
}

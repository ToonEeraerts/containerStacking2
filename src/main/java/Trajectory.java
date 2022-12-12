
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

    public boolean isBusy(int timer) {
//        return timer<=movements.getLast().getEndTime();
        return false;
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

    public void execute(Crane crane, int timer) {
        for (Movement m : movements) {
            m.executeMovement(crane.getId(), timer);
            timer += m.getDuration();
            crane.setCurrentPosition(m.getP2());
        }
    }


    @Override
    public String toString() {
        return "Trajectory{" +
                "movements=" + movements +
                '}';
    }
}

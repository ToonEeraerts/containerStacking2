
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

        // Add the movement towards the container
        Position cranePosition = new Position(c.getX(), c.getY(), 0, 0);
        Position containerPosition = movements.get(0).getP1();
        Movement moveToContainer = new Movement(0, cranePosition, containerPosition, c.getXspeed(), c.getYspeed(), null);
        movements.add(0, moveToContainer);

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

    public void execute(Crane crane, int timer) {
//        System.out.println("Executing: "+this);
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

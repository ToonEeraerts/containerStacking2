
import java.util.ArrayList;
import java.util.List;


public class UltimateTrajectory {

    // This list contains alle the trajectories needed to move 1 container to its target position
    // More than one container can be moved in order to make way
    private List<Movement> movements;

    public UltimateTrajectory(Container container) {
        movements = new ArrayList<>();
    }


    public void addMovement(Movement movement) {
        movements.add(movement);
    }

    public boolean isBusy(int timer) {
//        return timer<=movements.getLast().getEndTime();
        return false;
    }

    // Totale tijd teruggeven afh van de beginpositie van de kraan




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

    public void execute() {
        for (Movement m : movements) {
//            m.printMovement();
        }
    }


}

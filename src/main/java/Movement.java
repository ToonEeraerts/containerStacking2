import java.util.ArrayList;
import java.util.List;

public class Movement {
    private int tbegin;
    private double tend;
    private double vx;
    private double vy;


    private Container container;
    private List<Position> positions;


    public Movement(int tbegin, Position p1, Position p2, double vx, double vy, Container container) {
        positions = new ArrayList<>();
        positions.add(p1);
        positions.add(p2);
        this.tbegin = tbegin;
        this.vx = vx;
        this.vy = vy;
        this.container = container;
    }

    public int getTbegin() { return tbegin; }
    public double getVx() {
        return vx;
    }
    public double getVy() {
        return vy;
    }
    public double getTend() {
        return tend;
    }

    public void setTbegin(int tbegin) {
        this.tbegin = tbegin;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }
    public void setTend(int tend) {
        this.tend = tend;
    }


    public void addPosition(Position p) {
        positions.add(p);
    }

    public double calculateDurationBetweenTwoPoints(Position p1, Position p2) {
        double dx = Math.abs(p1.getX()-p2.getX());
        double dy = Math.abs(p1.getY()-p2.getY());
        return (Math.max(dx/vx, dy/vy));
    }


    public double getLeftBound() {
        double res = Integer.MAX_VALUE;
        for (Position p : positions)
            if (p.getX()<res) res = p.getX();
        return res;
    }

    // Returns furthest x-coordinate
    public double getRightBound() {
        double res = 0;
        for (Position p : positions)
            if (p.getX()>res) res = p.getX();
        return res;
    }

    public void printMovement(int craneId, int containerId,int pickupTime, int endTime, float pickupPosX, float pickupPosY, float endPosX, float endPosY){
        System.out.println(craneId + ";" + containerId + ";" + pickupTime + ";" + endTime + ";" + pickupPosX + ";" + pickupPosY + ";" + endPosX + ";" + endPosX + ";");
    }




    // The time is relative to the start of the movement
//    public Trajectory calculateTrajectory() {
//        Trajectory trajectory = new Trajectory(container); //Trajectory of the crane for this movement
//
//        //Start position
//        p1.setT(0);
//        trajectory.addPosition(p1);
//
//        double dx = Math.abs(p1.getX()-p2.getX()); //X-distance
//        double dy = Math.abs(p1.getY()-p2.getY()); //Y-distance
//        double tx = dx/vx; //How much time the crane moves in the x-direction
//        double ty = dy/vy; //How much time the crane moves in the y-direction
//
//        //Position where we change direction
//        double x2, y2, t2;
//        if(tx>ty) {
//            if(p2.getX() > p1.getX()) //move to the right
//                x2 = p1.getX() + ty*vx;
//            else x2 = p1.getX() - ty*vx;
//            y2 = p2.getY();
//            t2 = ty;
//        }
//        else {
//            if(p2.getY() > p1.getY()) //move to the left
//                y2 = p1.getY() + tx*vy;
//            else y2 = p1.getY() - tx*vy;
//            x2 = p2.getX();
//            t2 = tx;
//        }
//        Position pt = new Position(x2,y2,0,t2);
//        trajectory.addPosition(pt);
//
//        //Finish position
//        double tfinish = Math.max(tx, ty);
//        p2.setT(tfinish);
//        trajectory.addPosition(p2);
//
//        return trajectory;
//    }

}

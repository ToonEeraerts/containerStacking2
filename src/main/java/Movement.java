public class Movement {
    private int tbegin;
    private double tend;
    private Position p1;
    private Position p2;
    private double vx;
    private double vy;

    private Container container;


    public Movement(int tbegin, Position p1, Position p2, double vx, double vy, Container container) {
        this.p1 = p1;
        this.p2 = p2;
        this.tbegin = tbegin;
        this.vx = vx;
        this.vy = vy;
        this.container = container;
    }

    public int getTbegin() {
        return tbegin;
    }
    public double getTend() {
        return tend;
    }
    public Position getP1() {
        return p1;
    }
    public Position getP2() {
        return p2;
    }
    public double getVx() {
        return vx;
    }
    public double getVy() {
        return vy;
    }


    public void setTbegin(int tbegin) {
        this.tbegin = tbegin;
    }
    public void setTend(int tend) {
        this.tend = tend;
    }
    public void setP1(Position p1) {
        this.p1 = p1;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }


    public void setSpeed(Crane crane) {
        vx = crane.getXspeed();
        vy = crane.getYspeed();
    }
    public void setTimes(int timer) {
        tbegin = timer;
        tend = timer+getDuration();
    }

    public double getDuration() {
        assert vx != 0 : "vx not set";
        assert vy != 0 : "vy not set";
        double dx = Math.abs(p1.getX()-p2.getX());
        double dy = Math.abs(p1.getY()-p2.getY());
        return (Math.max(dx/vx, dy/vy));
    }


    public double getLeftBound() {
        return Math.min(p1.getX(), p2.getX());
    }
    public double getRightBound() {
        return Math.max(p1.getX(), p2.getX());
    }

    public void executeMovement(int craneId, int timer) {
        setTimes(timer);
        printMovement(craneId);
//        container.moveTo(p2);
    }

    public void printMovement(int craneId){
        String containerId = "";
        if (container != null) containerId = Integer.toString(container.getId());
        System.out.println(
                craneId+";"+
                containerId+";"+
                tbegin+";"+
                tend+";"+
                p1.getX()+";"+
                p1.getY()+";"+
                p2.getX()+";"+
                p2.getY()+";");
    }

    @Override
    public String toString() {
        return "Movement{" +
                "tbegin=" + tbegin +
                ", tend=" + tend +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", vx=" + vx +
                ", vy=" + vy +
                ", container=" + container +
                '}';
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

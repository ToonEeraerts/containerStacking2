import java.util.List;

public class Movement {
    private double tbegin;
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

    public double getTbegin() {
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
    public void setP2(Position p2) {
        this.p2 = p2;
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
    public void setTimes(double timer) {
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
    public double getTopBound() {
        return Math.min(p1.getY(), p2.getY());
    }
    public double getBottomBound() {
        return Math.max(p1.getY(), p2.getY());
    }

    public boolean isFeasibleContainerPlacement(List<Slot> slotList, int maxHeight) {
        return container.isFeasibleContainerPlacement(slotList, maxHeight);
    }

    public void executeMovement(int craneId, double timer) {
        setTimes(timer);
        printMovement(craneId);
        // No moveTo here, the correct moveTo is executed in main.java with the correct targetAssignment
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


}

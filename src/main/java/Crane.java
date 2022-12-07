import java.util.List;
import java.util.Map;

public class Crane {
    private int id;
    private float x;
    private float y;
    private float xmin;
    private float xmax;
    private float ymin;
    private float ymax;
    private float xspeed;
    private float yspeed;

    public Crane(int id, float x, float y, float xmin, float xmax, float ymin, float ymax, float xspeed, float yspeed) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.xspeed = xspeed;
        this.yspeed = yspeed;
    }

    // Calculate all the possible UltimateTrajectories
    public void generateUltimateTrajectories(List<Assignment> currentAssignments, Map<Integer,Container> containersMap) {
        for (Assignment a : currentAssignments) {



            // Move to the container
            Position cranePosition = new Position(x, y, 0, 0);
            Container container = containersMap.get(a.getContainerId());
            Position containerPosition = container.getPosition();

            // todo wat als er container onderaan stack

             = new Position(container.getX(), container.getY(),container.getSlots().get(0).getHeight(),0);
                //Replace above containers

            Movement m = new Movement(0,cranePosition, containerPosition,c.getXspeed(),c.getYspeed(), container);
            m.calculateTrajectory();
        }
    }


    // If idle: Neem beste UltimateTrajectory die niet botst met andere kranen en nog niet uitgevoerd!
    public void executeUltimateTrajectory() {

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getXmin() {
        return xmin;
    }

    public void setXmin(float xmin) {
        this.xmin = xmin;
    }

    public float getXmax() {
        return xmax;
    }

    public void setXmax(float xmax) {
        this.xmax = xmax;
    }

    public float getYmin() {
        return ymin;
    }

    public void setYmin(float ymin) {
        this.ymin = ymin;
    }

    public float getYmax() {
        return ymax;
    }

    public void setYmax(float ymax) {
        this.ymax = ymax;
    }

    public float getXspeed() {
        return xspeed;
    }

    public void setXspeed(float xspeed) {
        this.xspeed = xspeed;
    }

    public float getYspeed() {
        return yspeed;
    }

    public void setYspeed(float yspeed) {
        this.yspeed = yspeed;
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

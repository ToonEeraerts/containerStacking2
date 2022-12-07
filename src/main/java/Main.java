import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ///////////
        //Dataset//
        ///////////
        //Reading dataset for initial situation
        InputData inputData = readFile("datasets/terminal22_1_100_1_10.json");
        inputData.initialAssignments();

        //Processing the initial dataset
        List<Slot> slots = inputData.getSlots();
        List<Crane> cranes = inputData.getCranes();
        List<Container> containers = inputData.getContainers();


        Map<Integer,Container> containersMap = new HashMap<>();
        for(Container c: containers){
            containersMap.put(c.getId(),c);
        }
        List<Assignment> currentAssignments = inputData.getAssignments(); // This list contains all the changes we still have to make
        System.out.println("Dataset initialized");

        //Reading dataset for end situation
        InputData targetData = readFile("datasets/terminal22_1_100_1_10target.json");
        List<Assignment> targetAssignments = targetData.getAssignments();

        ///////
        //GUI//
        ///////
        //use "grid.updateGrid(slots);" to visualize movements
        System.out.println("Press next to view further movements");
        Grid grid = new Grid(inputData.getLength(),inputData.getWidth(), inputData.getMaxHeight(), slots);
        System.out.println("GUI initialized");


        /////////////
        //Algorithm//
        /////////////
        // 1: Calculate the best trajectory for this crane
        // 2:
        // 3: Process first set of trajectories
        // 4:
        currentAssignments = filterAssignments(currentAssignments,targetAssignments);
        //Calculate trajectory for every crane for every assignment
        for(Crane c: cranes){
            c.generateUltimateTrajectories(currentAssignments, containersMap);

            for(Assignment a: currentAssignments){
                int t = 0;
                float craneX = c.getX();
                float craneY = c.getY();
                Position cranePosition = new Position(craneX,craneY,0,0);
                Container container = containersMap.get(a.getContainerId());
                container.updatePosition();
                Position containerPosition;

                // Containers on top of the requested container need to be replaced first
                if(container.getSlots().get(0).peekTop() == container){
                    containerPosition = new Position(container.getX(), container.getY(),container.getSlots().get(0).getHeight(),0);
                }
                else{
                    containerPosition = new Position(container.getX(), container.getY(),container.getSlots().get(0).getHeight(),0);
                    //Replace above containers
                }
                Movement m = new Movement(0,cranePosition, containerPosition,c.getXspeed(),c.getYspeed(), container);
                m.calculateTrajectory();

            }
        }

    }
    // Remove all the assignments from currentAssignments where the container is already in place
    public static List<Assignment> filterAssignments (List<Assignment> currentAssignments, List<Assignment> targetAssignments){
        List<Assignment> toRemove = new ArrayList<>(currentAssignments);
        for(Assignment ca: currentAssignments){
            for(Assignment ta: targetAssignments){
                if(ca.getContainerId() == ta.getContainerId() && ca.getSlotId() == ta.getSlotId())
                    toRemove.remove(ca);
            }
        }
        return toRemove;
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
            if (t2.getRightBound() <= t1.getLeftBound()+margin)
                return true;

        return false;
    }

    public static InputData readFile(String path) {
        InputData inputData = null;
        try {
            String jsonString = Files.readString(Paths.get(path));
            Gson gson = new Gson();
            inputData = gson.fromJson(jsonString, InputData.class);
        }
        catch (IOException e) {e.printStackTrace();}
        return inputData;
    }

}
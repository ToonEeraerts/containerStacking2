import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        Position p1 = new Position(1,2,3,0);
//        Position p2 = new Position(4,5,6,0);
//        Movement m = new Movement(1, p1, p2, 3, 4);

        InputData inputData = readFile("datasets/terminal_4_3.json");

        List<Container> containers = inputData.getContainers();
        List<Slot> slots = inputData.getSlots();
        List<Assignment> assignments = inputData.getAssignments();

        //Initial assignments
        //For future replacements, use stack
        for(Assignment a: assignments){
            int conID = a.getContainerId();

            // Search the container with id from assignment
            Container c = null;
            for(Container container: containers)
                if(conID == container.getId())
                    c=container;

            // Search the slots with the ids from assignment
            List<Slot> slotList = new ArrayList<>();
            int[] slotIds = a.getSlotId();
            for(int slotId : slotIds)
                for(Slot slot: slots)
                    if(slotId == slot.getId())
                        slotList.add(slot);

            updateContainer(c, slotList);
        }
        System.out.println(slots);

    }

    public static void updateContainer(Container container, List<Slot> slotList) {
        // Remove old data
        List<Slot> previousSlots = container.getSlots();
        for (Slot s : previousSlots) {
            s.popContainer(container);
        }
        container.clearSlots();

        // Set new data
        for (Slot s : slotList){
            s.addContainer(container);
            container.addSlot(s);
        }
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

    /*
    public static void writeFile(String path, OutputData outputData) {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String jsonString = gson.toJson(outputData);
            PrintWriter printer = new PrintWriter(new FileWriter(path));
            printer.write(jsonString);
            printer.close();
        }
        catch (IOException e) {e.printStackTrace();}
    }

     */

    public static boolean checkHeight(int maxHeight, Slot s1 , Slot s2) {
        if (s2 == null) return s1.hasHeightLeft(maxHeight);
        return s1.hasHeightLeft(maxHeight) && s2.hasHeightLeft(maxHeight);
    }

    public static boolean checkHeight(int maxHeight, Slot s1){
        return s1.hasHeightLeft(maxHeight);
    }

    public static boolean checkSupported(Slot s1, Slot s2) {
        return s1.getHeight()==s2.getHeight();
    }

    public static boolean checkTopDownSmallAdded(Slot s1) {
        if (s1.stackIsEmpty()) return true;
        return s1.hasSmallContainerOnTop();
    }

}
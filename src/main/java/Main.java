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

        InputData inputData = readFile("datasets/terminal_4_3.json");
        inputData.initialAssignments();

        //GUI
        List<Slot> slots = new ArrayList<>(inputData.getSlots());
        //use grid.updateGrid(slots); to visualize movements
        System.out.println("Empty stack?" + inputData.getSlots());

        /** Testing of container movement **/
        List<Container> containers = inputData.getContainers();
        slots = inputData.getSlots();
        List<Slot> list = new ArrayList<>();
        Container container;
        boolean constraints;
        Grid grid = new Grid(1,3, 3, slots);

        Slot s1 = slots.get(0);
        Slot s2 = slots.get(1);
        Slot s3 = slots.get(2);

        System.out.println("initial situation: \n"+containers);

        // move 1
        System.out.println("MOVE 1:");
        container = containers.get(2);
        constraints = container.checkConstraints(s2, s3, 2);
        System.out.println("constraints: "+constraints);

        if(constraints) {
            list.add(s2);
            list.add(s3);
            container.move(list);
        }
        grid.updateGrid(slots);
        list.clear();
        System.out.println(containers);

        // move 2
        System.out.println("MOVE 2: should not be possible, container can't be grabbed");
        container = containers.get(3);
        constraints = container.checkConstraints(s1, null, 2);
        System.out.println("constraints: "+constraints);
        grid.updateGrid(slots);

        // move 3
        System.out.println("MOVE 2: should not be possible, not properly supported");
        container = containers.get(0);
        constraints = container.checkConstraints(s3, null, 3);
        System.out.println("constraints: "+constraints);
        grid.updateGrid(slots);




        OutputData outputData = generateOutputData();
        writeFile("solutions/solution1", outputData);
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

    public static OutputData generateOutputData() {
        return new OutputData("output");
    }

    public static void writeFile(String path, OutputData outputData) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String jsonString = gson.toJson(outputData);
            PrintWriter printer = new PrintWriter(new FileWriter(path));
            printer.write(jsonString);
            printer.close();
        }
        catch (IOException e) {e.printStackTrace();}
    }
}
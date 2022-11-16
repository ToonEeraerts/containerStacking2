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
        Grid grid = new Grid(1,3, 3, (ArrayList<Slot>) inputData.getSlots());
        //use grid.updateGrid(slots); to visualize movements
        /** Testing of container movement **/
        List<Container> containers = inputData.getContainers();
        List<Slot> slots = inputData.getSlots();
        Slot s1 = slots.get(0);
        Slot s2 = slots.get(1);
        Slot s3 = slots.get(2);

        System.out.println("initial situation: \n"+containers);
        Container container = containers.get(2);
        System.out.println(container);
        System.out.println(s2);
        System.out.println(s3);
        boolean constraints = container.checkConstraints(s2, s3, 2);
        System.out.println("constraints: "+constraints);

        if(constraints){
            List<Slot> list = new ArrayList<>();
            list.add(s2);
            list.add(s3);
            container.move(list);
        }
        System.out.println("final situation: \n"+containers);


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
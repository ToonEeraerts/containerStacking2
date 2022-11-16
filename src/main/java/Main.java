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
        new Grid(3,1);//makes new ButtonGrid with 2 parameters
        InputData inputData = readFile("datasets/terminal_4_3.json");
        inputData.initialAssignments();

        /** testing **/

//        Position p1 = new Position(1,2,3,0);
//        Position p2 = new Position(4,5,6,0);
//        Movement m = new Movement(1, p1, p2, 3, 4);

        CoordinateSystem cs = new CoordinateSystem(0,2,2);


    }



    // todo call checkConstraints before


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

    // Only necessary for large containers
    public static boolean checkSupported(Slot s1, Slot s2) {
        return s1.getHeight()==s2.getHeight();
    }

    // Only necessary for small containers
    public static boolean checkTopDown(Slot s1) {
        if (s1.stackIsEmpty()) return true;
        return s1.hasSmallContainerOnTop();
    }

    // slot 2 can be null for small containers
    public boolean checkConstraints(Container container, Slot s1, Slot s2, int maxHeight) {
        // large container
        if (container.getLength()==2) {
            if (!checkHeight(maxHeight, s1, s2)) return false;
            return checkSupported(s1, s2);
        }
        // small container
        else {
            if (!checkHeight(maxHeight, s1)) return false;
            return checkTopDown(s1);
        }
    }



}
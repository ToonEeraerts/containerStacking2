import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        /////////
        //Input//
        /////////
        InputData inputData = readFile("datasets/Terminal_20_10_3_2_100.json");
        InputData targetData = readFile("datasets/terminal22_1_100_1_10target.json");
        inputData.generateInput();

        List <Crane> cranes = inputData.getCranes();
        Map <Integer, Slot> slots = inputData.getSlotsMap();
        Map <Integer, Container> containers = inputData.getContainersMap();

        List <Assignment> initialAssignments = inputData.getAssignments();
        targetData.generateAssignments(containers, slots);
        List <Assignment> targetAssignments = targetData.getAssignments();
        List <Assignment> todoAssignments = filterAssignments(initialAssignments,targetAssignments);

        System.out.println("Data initialized");


        ///////
        //GUI//
        ///////
        //use "grid.updateGrid(slots);" to visualize movements
        System.out.println("Press next to view further movements");
        Grid grid = new Grid(inputData.getLength(),inputData.getWidth(), inputData.getMaxHeight(), inputData.getSlots());
        System.out.println("GUI initialized");


        /////////////
        //Algorithm//
        /////////////
        // Tell the cranes which other cranes they are competing with
        for (Crane c : cranes) c.addOtherCranes(cranes);


        int timer = 0; // todo gebruiken! onder andere voor isSafe() nodig
        while (!todoAssignments.isEmpty()) {
            //todo nu 1 voor 1: willen dat kraan direct kan uitvoeren als hij klaar is voor een andere kraan
            //todo slots blokkeren waar 1 kraan mee bezig is

            for (Crane c : cranes) {
                Assignment executedAssignment = c.executeNextMove(timer, todoAssignments, targetAssignments);
                todoAssignments.remove(executedAssignment);
            }

        }





    }

    // Remove all the assignments from currentAssignments where the container is already in place
    public static List<Assignment> filterAssignments (List<Assignment> currentAssignments, List<Assignment> targetAssignments){
        List<Assignment> todoAssignments = new ArrayList<>(currentAssignments);
        for(Assignment ca: currentAssignments){
            for(Assignment ta: targetAssignments){
                if(ca.getContainerId() == ta.getContainerId() && ca.getSlotId() == ta.getSlotId())
                    todoAssignments.remove(ca);
            }
        }
        return todoAssignments;
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
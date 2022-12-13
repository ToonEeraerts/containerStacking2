import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        List<Assignment> initialAssignments;
        List<Assignment> targetAssignments;
        List<Assignment> todoAssignments;


        /////////
        //Input//
        /////////
        String instance = "ConstraintsTesting";
//        String instance = "A_22_1_100_1_10";
//        String instance = "A_20_10_3_2_160";
        InputData inputData = readFile("datasets/Terminal"+instance+".json");
        inputData.generateInput();
        List <Crane> cranes = inputData.getCranes();
        List <Slot> slotList = inputData.getSlots();
        Map <Integer, Slot> slots = inputData.getSlotsMap();
        Map <Integer, Container> containers = inputData.getContainersMap();
        initialAssignments = inputData.getAssignments();
        int targetHeight = inputData.getTargetHeight();

        if (targetHeight == 0) {
            InputData targetData = readFile("datasets/targetTerminal"+instance+".json");
            targetData.generateAssignments(containers, slots);
            targetAssignments = targetData.getAssignments();
            for (Assignment a : targetAssignments) a.generateSlotList(slotList);
        }
        else {
            //todo calculate our own targetAssignments for a given max height
            targetAssignments = null;
        }



        todoAssignments = filterAssignments(initialAssignments,targetAssignments);

        System.out.println("Data initialized, todo: "+todoAssignments);


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
        for (Crane c : cranes) {
            c.addOtherCranes(cranes);
            c.setMaxHeight(grid.maxHeight);
        }


        int timer = 0; // todo gebruiken! onder andere voor isSafe() nodig
        int craneIndex = 0;
        while (!todoAssignments.isEmpty()) {
            //todo nu 1 voor 1: willen dat kraan direct kan uitvoeren als hij klaar is voor een andere kraan
            //todo slots blokkeren waar 1 kraan mee bezig is



            Crane crane = cranes.get(craneIndex);
            Assignment executedAssignment = crane.executeNextMove(timer, todoAssignments, targetAssignments);

            if (executedAssignment != null) {
                executedAssignment.updateContainerObject(slotList);

                // Update container object via assignment
                for (Assignment ta: targetAssignments)
                    if (ta.getContainer().equals(executedAssignment.getContainer()))
                        ta.updateContainerObject(slotList);

                todoAssignments.remove(executedAssignment);

                grid.updateGrid(slotList);
            }


            if (craneIndex < cranes.size()-1) craneIndex++;
            else craneIndex=0;
        }

        System.out.println("Klaar!");
    }

    // Remove all the assignments from currentAssignments where the container is already in place
    public static List<Assignment> filterAssignments (List<Assignment> currentAssignments, List<Assignment> targetAssignments){
        List<Assignment> todoAssignments = new ArrayList<>(currentAssignments);
        for(Assignment ca: currentAssignments){
            for(Assignment ta: targetAssignments){
                if(ca.getContainer() == ta.getContainer() && ca.getSlotId() == ta.getSlotId())
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
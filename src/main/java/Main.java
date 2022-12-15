import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        List<Assignment> initialAssignments;
        List<Assignment> targetAssignments;
        List<Assignment> todoAssignments;
        int margin = 1;


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
        // todoAssignments = initialAssignments - targetAssignments
        todoAssignments = filterAssignments(initialAssignments,targetAssignments);
//        System.out.println("Data initialized, todo: "+todoAssignments);


        ///////
        //GUI//
        ///////
        Grid grid = new Grid(inputData.getLength(),inputData.getWidth(), inputData.getMaxHeight(), inputData.getSlots());


        /////////////
        //Algorithm//
        /////////////
        // Tell the cranes which other cranes they are competing with
        for (Crane c : cranes) {
            c.addOtherCranes(cranes);
            c.setMaxHeight(grid.maxHeight);
            c.setMargin(margin);
        }

        // Crane queue sorted on who is ready first
        PriorityQueue<Crane> craneQueue = new PriorityQueue<>(cranes);

        double timer = 0;
        double maxFinishTime = 0;
        int x = 0;
//        while (!todoAssignments.isEmpty()) {
        while (x<6) {
//            System.out.println(craneQueue);
            Crane crane = craneQueue.poll();
            // Set the timer to the time when this crane was finished
            timer = crane.getFinishTime();

            Assignment executedAssignment = crane.executeNextMove(timer, todoAssignments);

            if (executedAssignment != null) {
                // Update container object via assignment
                executedAssignment.updateContainerObject(slotList);
                todoAssignments.remove(executedAssignment);
                grid.updateGrid(slotList);
                maxFinishTime = Math.max(crane.getFinishTime(), maxFinishTime);
            }
            else {
                // crane could not execute a move
                // increase its finishTime, so it gets to the back of the queue
                crane.setFinishTime(maxFinishTime+1);
            }



            craneQueue.add(crane);

            x++;
        }
        System.out.println("Klaar!");
    }


    // Remove all the assignments from currentAssignments where the container is already in place
    public static List<Assignment> filterAssignments(List<Assignment> initialAssignments, List<Assignment> targetAssignments){
        List<Assignment> todoAssignments = new ArrayList<>(targetAssignments);
        for(Assignment ta: targetAssignments){
            for(Assignment ia: initialAssignments){
                if(ia.getContainer().equals(ta.getContainer()) && ia.getSlotId() == ta.getSlotId())
                    todoAssignments.remove(ta);
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
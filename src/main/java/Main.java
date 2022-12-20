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


        /************************************************** Input **************************************************/
//        String instance = "ConstraintsTesting";
        //String instance = "TerminalA_22_1_100_1_10";
        String instance = "MH2Terminal_20_10_3_2_160";

        InputData inputData = readFile("datasets/"+instance+".json");
        inputData.generateInput();
        int maxHeight = inputData.getMaxHeight();
        int width = inputData.getWidth();
        int length = inputData.getLength();
        List <Crane> cranes = inputData.getCranes();
        List <Slot> slotList = inputData.getSlots();
        Map <Integer, Slot> slots = inputData.getSlotsMap();
        Map <Integer, Container> containers = inputData.getContainersMap();
        initialAssignments = inputData.getAssignments();
        int targetHeight = inputData.getTargetHeight();
        int makeRoomCounter = 0;

        if (targetHeight == 0) {
            InputData targetData = readFile("datasets/target"+instance+".json");
            targetData.generateAssignments(containers, slots);
            targetAssignments = targetData.getAssignments();
            for (Assignment a : targetAssignments) a.generateSlotList(slotList);
        }
        else {
            targetAssignments = putLower(slotList, maxHeight, targetHeight, length);
        }
        System.out.println(slotList.get(3).getHeight());
        // todoAssignments = initialAssignments - targetAssignments
        todoAssignments = filterAssignments(initialAssignments,targetAssignments);

        Grid grid = new Grid(inputData.getLength(),inputData.getWidth(), inputData.getMaxHeight(), inputData.getSlots());

        // Tell the cranes which other cranes they are competing with
        double maxFinishTime = 0;
        for (Crane c : cranes) {
            c.addOtherCranes(cranes);
            c.setMaxHeight(grid.maxHeight);
            c.setMargin(margin);
            c.setAllSlots(slotList);
            c.generateAllTrajectories(todoAssignments);

            // Er gebeuren al pass alongs bij generateAllTrajectories()
            // Die passeren dus niet in onze while loop
            // We moeten de maxFinishTime dus handmatig zetten
            if (c.getFinishTime()>maxFinishTime)
                maxFinishTime = c.getFinishTime();
        }
        /************************************************** Input **************************************************/



        /********************************************* Core algorithm **********************************************/
        int x = 0;
        double timer = 0;
        timer = assignAssignments(todoAssignments, cranes, timer, slotList, grid, maxFinishTime);
        while(!validate(targetHeight, slotList)) {
            todoAssignments = putLower(slotList, maxHeight, targetHeight, length);
            if(todoAssignments.isEmpty())todoAssignments = makeRoom(slotList, makeRoomCounter);
            timer = assignAssignments(todoAssignments, cranes, timer, slotList, grid, maxFinishTime);
        }
        System.out.println("Klaar!");
        /********************************************* Core algorithm **********************************************/
    }

    private static List<Assignment> putLower(List<Slot> slotList, int maxHeight, int targetHeight, int length){
        //Get all containers on the top level
        ArrayList<Container> topLevelContainers = getTopContainers(slotList,maxHeight);

        List<Assignment> targetAssignments = new ArrayList<>();
        for(Container c : topLevelContainers){
            Assignment feasibleAssignment = new Assignment(c);
            Position feasiblePlacementPosition = feasibleAssignment.getLowerPosition(slotList, targetHeight, length);
            if(feasiblePlacementPosition!=null){
                feasibleAssignment.setContainerCenter(feasiblePlacementPosition);
                targetAssignments.add(feasibleAssignment);
            }
        }
        return targetAssignments;
    }
    private static List<Assignment> makeRoom(List<Slot> slotList, int makeRoomCounter) {
        List<Assignment> todoAssignments = new ArrayList<>();

        //Shuffle all containers to the left
        for(int i = 1; i < slotList.size(); i++){
            if(i%20==0)i++;
            Slot previousSlot = slotList.get(i-1);
            Slot currentSlot = slotList.get(i);
            if(currentSlot.getHeight()==1 && previousSlot.stackIsEmpty()){
                /*Stack<Container> containerStack= currentSlot.getContainers();
                while(!containerStack.isEmpty()){
                    Container c = containerStack.pop();
                    Assignment tempMove = new Assignment(c);
                    Position feasiblePlacementPosition = tempMove.getFeasiblePosition(slotList, targetHeight, length);
                }*/
                Assignment leftAssignment = new Assignment(currentSlot.peekTop());
                leftAssignment.moveToTheLeft(previousSlot, slotList);
                todoAssignments.add(leftAssignment);
            }
        }
        return todoAssignments;
    }

    public static boolean validate(int targetHeight, List<Slot> slotList){
        if(targetHeight==0)return true;
        int height = 0;
        for( Slot s : slotList){
            if(height < s.getHeight())height = s.getHeight();
        }
        return height <= targetHeight;
    }
    // Remove all the assignments from currentAssignments where the container is already in place
    public static List<Assignment> filterAssignments(List<Assignment> initialAssignments, List<Assignment> targetAssignments){
        List<Assignment> todoAssignments = new ArrayList<>(targetAssignments);
        for(Assignment ta: targetAssignments){
            for(Assignment ia: initialAssignments){
                if(ia.getContainer().equals(ta.getContainer()) && ia.getSlot().equals(ta.getSlot()))
                    todoAssignments.remove(ta);
            }
        }
        return todoAssignments;
    }

    public static ArrayList<Container> getTopContainers(List<Slot> slots, int maxHeight){
        ArrayList<Container> containersOnTop = new ArrayList<>();
        for(Slot s : slots){
            if(!s.hasHeightLeft(maxHeight) && !containersOnTop.contains(s.peekTop()))containersOnTop.add(s.peekTop());
        }
        return containersOnTop;
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
    public static double assignAssignments(List<Assignment> todoAssignments, List<Crane> cranes, double timer, List<Slot> slotList, Grid grid, double maxFinishTime){
        // Crane queue sorted on who is ready first
        PriorityQueue<Crane> craneQueue = new PriorityQueue<>(cranes);
        while (!todoAssignments.isEmpty()) {
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
                for (Crane c : cranes) {
                    c.removeTrajectory(executedAssignment);
                }
            }
            else {
                // crane could not execute a move
                // increase its finishTime, so it gets to the back of the queue
                maxFinishTime = Math.max(crane.getFinishTime(), maxFinishTime);
                crane.setFinishTime(maxFinishTime+1);
            }
            craneQueue.add(crane);
        }
        return timer;
    }
}
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Assignment {

    @SerializedName("container_id")
    private int containerId;
    @SerializedName("slot_id")
    private int slotId;

    private Container container;
    private Slot slot;
    private Position containerCenter;
    private List<Slot> slotList;

    public Assignment(Container c){
        this.container = c;
        this.containerId = c.getId();
        this.containerCenter = c.getPosition();
    }

    public Assignment(Assignment other) {
        container = other.getContainer();
        slot = other.getSlot();
        containerCenter = other.getContainerCenter();
        slotList = new ArrayList<>(other.getSlotList());
    }

    public Container getContainer() {
        return container;
    }
    public Slot getSlot() {
        return slot;
    }
    public Position getContainerCenter() {
        return containerCenter;
    }
    public List<Slot> getSlotList() {
        return slotList;
    }

    public void setContainerCenter(Position containerCenter) {
        this.containerCenter = containerCenter;
    }

    public void setContainer(Map<Integer, Container> containersMap) {
        container = containersMap.get(containerId);
    }
    public void setSlot(Map<Integer, Slot> slotsMap) {
        slot = slotsMap.get(slotId);
    }

    public void generateSlotPosition() {
        assert container != null : "Container not yet generated";
        assert slot != null : "Slot not yet generated";
        double temp = (double)(container.getLength()-1)/2;
        containerCenter = new Position(slot.getX()+temp, slot.getY()+0.5, 0, 0);
    }

    public void updateContainerObject(List<Slot> allSlots) {
        generateSlotList(allSlots);
        container.moveTo(slotList);
    }

    public void generateSlotList(List<Slot> allSlots) {
        assert container != null : "Container not yet generated";
        assert slot != null : "Slot not yet generated";
        slotList = new ArrayList<>();

        int index = allSlots.indexOf(slot);
        for (int i = 0; i < container.getLength(); i++) {
            slotList.add(allSlots.get(index+i));
        }
    }

    // todo uitbreiden met een voorkeurspositie
    // Returns a position where the container is allowed to stand between the boundaries
    // Position -> Slot -> SlotList -> feasible?
    public Position getFeasiblePlacementPosition(double leftBound, double rightBound, double topBound, double bottomBound, List<Slot> allSlots, int maxHeight) {
        for (double x = leftBound+0.5; x <= rightBound; x++) {
            for (double y = topBound+0.5; y <= bottomBound; y++) {
                Position p = new Position(x, y, 0, 0);
                for (Slot s : allSlots) {
                    if (s.getX()==x-0.5 && s.getY()==y-0.5) {
                        slot = s;
                    }
                }
                generateSlotList(allSlots);
                if (container.isFeasibleContainerPlacement(slotList, maxHeight)) {
                    return p;
                }
            }
        }
        return null;
    }

    public void moveToTheLeft(Slot targetSlot , List<Slot> allSlots){
        slot = targetSlot;
        generateSlotList(allSlots);
        containerCenter.setX(containerCenter.getX()-1);
    }

    //Returns the closest available position where the container can sit under targetHeight
    public Position getLowerPosition( List<Slot> allSlots, int targetHeight, int length){
        Position p = null;
        double minimalDistance = Integer.MAX_VALUE;

        for(int i =0; i < allSlots.size()-container.getLength(); i++){
            if(((i + container.getLength()-1) % (length))==0)i += container.getLength()-1;
            Slot s = allSlots.get(i);
            boolean available = true;
            List<Slot> targetSlots = new ArrayList<>();
            for(int j = 0; j < container.getLength(); j++) {
                targetSlots.add(allSlots.get(i + j));
            }
            for(int j = 0; j < container.getLength(); j++){
                if(available){

                    if(!container.isFeasibleContainerPlacement(targetSlots, targetHeight))available = false;
                    else if(allSlots.get(i+j).isReserved(targetHeight))available = false;
                }
            }
            if(available){
                double slotX = s.getX();
                double slotY = s.getY();
                double currentDistance = calculateDistance(s,container);
                if(currentDistance < minimalDistance){
                    minimalDistance = currentDistance;
                    p = new Position(slotX, slotY, s.getHeight(), 0);
                    slot = s;
                    slotId = s.getId();
                    for(int j = 0; j < container.getLength(); j++) {
                        allSlots.get(i+j).setReserved(s.getHeight(), true);
                    }
                    generateSlotList(allSlots);
                }
            }
        }
        return p;
    }

    public double calculateDistance (Slot s, Container c){
        return Math.sqrt(Math.pow(s.getY()+0.5-(c.getPosition().getY()+0.5),2) + Math.pow(s.getX() + (float) c.getLength()/2 - (c.getPosition().getX()+ (float) c.getLength()/2),2));
    }





    @Override
    public String toString() {
        return "Assignment{" +
                "containerId=" + containerId +
                ", slotId=" + slotId +
                ", container=" + container +
                ", slot=" + slot +
                ", slotPosition=" + containerCenter +
                ", slotList=" + slotList +
                "}\n";
    }
}

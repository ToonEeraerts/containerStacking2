import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Grid extends JFrame{

    Random rand = new Random();
    int maxHeight;
    int width;
    int length;
    HashMap<Integer,Color> colors = new HashMap<>();
    JFrame frame=new JFrame("Container Stacking"); //creates frame
    ArrayList<JTextArea[][]> levels = new ArrayList<>(); //The grid of containers
    ArrayList<JPanel> panels = new ArrayList<>();
    JPanel flowPanel = new JPanel(new FlowLayout());
    public Grid(int width, int length, int maxHeight, ArrayList<Slot> slots){ //constructor
        this.maxHeight = maxHeight;
        this.width = width;
        this.length = length;
        updateGrid(slots);
    }
    public void updateGrid(ArrayList<Slot> slots){
        for(int i = 0; i < maxHeight; i++){
            levels.add(new JTextArea[width][length]);
            panels.add(new JPanel());
            panels.get(i).add(new JLabel("Level " + i));
            for(int y=0; y<length; y++){
                for(int x=0; x<width; x++){
                    levels.get(i)[x][y] = new JTextArea(); //creates new text Area
                }
            }
        }

        for(Slot s : slots){
            Stack<Container> containers = s.getContainers();
            int x = s.getX();
            int y = s.getY();
            for(int i = s.getHeight()-1; i >= 0; i--){
                int containerID = containers.peek().getId();
                containers.pop();
                System.out.println(levels.size());
                levels.get(i)[x][y].setText(String.valueOf(containerID));
                Color c;
                if(colors.get(containerID)==null){

                    // Java 'Color' class takes 3 floats, from 0 to 1.
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    c = new Color(r, g, b);
                    colors.put(containerID,c);
                }
                else{
                    c = colors.get(containerID);
                }
                levels.get(i)[x][y].setBackground(c);
                panels.get(i).add(levels.get(i)[x][y]); //adds button to grid
            }
        }
        for (JPanel panel : panels) {
            flowPanel.add(panel);
        }
        frame.add(flowPanel);
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); //makes frame visible
    }
}
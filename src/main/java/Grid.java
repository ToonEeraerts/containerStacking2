import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

// todo optioneel:
//  - bovenaan slot ids zetten zou makkelijker zijn om te tellen
//  - posities van kranen weergeven
//  - terugknop

public class Grid extends JFrame implements ActionListener {

    Random rand = new Random();
    int maxHeight;
    int width;
    int length;
    HashMap<Integer,Color> colors = new HashMap<>();
    JFrame frame = new JFrame("Container Stacking"); //creates frame
    private JScrollPane scroll;

    ArrayList<JTextPane[][]> levels = new ArrayList<>(); //The grid of containers
    ArrayList<JPanel> panels = new ArrayList<>();

    JPanel flowPanel = new JPanel();
    JButton button = new JButton("Next");
    volatile Boolean pass = false;

    public Grid(int width, int length, int maxHeight, List<Slot> slots){ //constructor
        this.maxHeight = maxHeight;
        this.width = width;
        this.length = length;

        button.addActionListener(this);
        scroll = new JScrollPane();
        updateGrid(slots);
    }
    public void updateGrid(List<Slot> slots){
        levels = new ArrayList<>();
        panels = new ArrayList<>();
        flowPanel = new JPanel();
        for(int i = 0; i < maxHeight; i++){
            levels.add(new JTextPane[width][length]);
            panels.add(new JPanel());
            panels.get(i).setLayout(new GridLayout(length,width));
            for(int y=0; y<length; y++){
                for(int x=0; x<width; x++){
                    levels.get(i)[x][y] = new JTextPane(); //creates new text Area
                }
            }
        }

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        for(Slot s : slots){
            Stack<Container> containers = (Stack<Container>) s.getContainers().clone();
            int x = s.getX();
            int y = s.getY();
            for(int i = maxHeight-1; i >= 0; i--){
                Color c;
                if(i>(s.getHeight()-1)){
                    levels.get(i)[x][y].setText("/");
                    c = new Color(255,255,255);
                }
                else{
                    int containerId = containers.pop().getId();
                    levels.get(i)[x][y].setText(String.valueOf(containerId));
                    if(colors.get(containerId)==null){
                        // Java 'Color' class takes 3 floats, from 0 to 1.
                        float r = rand.nextFloat();
                        float g = rand.nextFloat();
                        float b = rand.nextFloat();
                        c = new Color(r, g, b);
                        colors.put(containerId,c);
                    }
                    else{
                        c = colors.get(containerId);
                    }
                }
                StyledDocument doc = levels.get(i)[x][y].getStyledDocument();
                doc.setParagraphAttributes(0,10,center,false);
                levels.get(i)[x][y].setBackground(c);
                panels.get(i).add(levels.get(i)[x][y]);
            }
        }
        frame.setLayout(new BorderLayout());
        flowPanel.setLayout(new BoxLayout(flowPanel,BoxLayout.Y_AXIS));
        int i = 0;
        for (JPanel panel : panels) {
            flowPanel.add(new JLabel("Level " + i));
            flowPanel.add(panel);
            i++;
        }
        frame.remove(scroll);
        scroll = new JScrollPane(flowPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scroll);
        frame.add(button,  BorderLayout.SOUTH);
        frame.setSize(1200,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); //makes frame visible
        while (!pass);
        pass = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pass = true;
    }
}
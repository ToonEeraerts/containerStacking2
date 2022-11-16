import javax.swing.*;
import java.awt.*;

public class Grid {
    JFrame frame=new JFrame(); //creates frame
    JLabel[][] containers; //The grid of containers

    public Grid(int width, int length){ //constructor
        frame.setLayout(new GridLayout(width,length)); //set layout
        containers=new JLabel[width][length]; //allocate the size of grid
        for(int y=0; y<length; y++){
            for(int x=0; x<width; x++){
                containers[x][y]=new JLabel("id"); //creates new label
                containers[x][y].setBackground(Color.RED);
                frame.add(containers[x][y]); //adds button to grid
            }
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack(); //sets appropriate size for frame
        frame.setVisible(true); //makes frame visible
    }
}
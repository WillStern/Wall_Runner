import javax.swing.*;
import java.awt.*;

public class Main{

    public static void main(String[] args){

	    JFrame myFrame = new JFrame();
        myFrame.setPreferredSize(new Dimension(1200,600));
		myFrame.setTitle("Game");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameCanvas gc = new GameCanvas();
		Container cp = myFrame.getContentPane();

        cp.setLayout(new BorderLayout());
        cp.add(gc, BorderLayout.CENTER);
 	    myFrame.pack();
 		myFrame.setVisible(true);

    }
}

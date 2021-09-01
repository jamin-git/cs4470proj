import javax.swing.*;
import java.awt.*;

public class Calendar {

    JFrame frame;
    JMenuBar menubar;
    JMenuItem i1;
    JMenuItem i2;
    JTextArea text;

    Calendar() {
        // Setting Up Frame Functionality
        frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Setting Up Menu Bar & Items
        menubar = new JMenuBar();
        i1 = new JMenuItem("Month View");
        i2 = new JMenuItem("Day View");
        menubar.add(i1);
        menubar.add(i2);
        frame.getContentPane().add(menubar, BorderLayout.NORTH);

        // Setting Up Text Area
        text = new JTextArea(50,100);
        text.setBackground(Color.gray);
        frame.getContentPane().add(text, BorderLayout.CENTER);


        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Calendar cal = new Calendar();
            }
        });
    }
}

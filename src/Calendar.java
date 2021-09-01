import javax.swing.*;
import java.awt.*;

public class Calendar {

    JFrame frame;
    JMenuBar menubar;
    JMenu file;
    JMenu view;
    JMenuItem exit;
    JMenuItem dayView;
    JMenuItem monthView;

    JTextArea text;

    Calendar() {
        // Setting Up Frame Functionality
        frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Setting Up Menu Bar & Items
        menubar = new JMenuBar();
        file = new JMenu("File");
        view = new JMenu("View");
        exit = new JMenuItem("Exit");
        dayView = new JMenuItem("Day View");
        monthView = new JMenuItem("Month View");
        file.add(exit);
        view.add(dayView);
        view.add(monthView);
        menubar.add(file);
        menubar.add(view);
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

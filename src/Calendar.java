import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Calendar {

    JFrame frame;
    JMenuBar menuBar;
    JMenu file;
    JMenu view;
    JMenuItem exit;
    JMenuItem dayView;
    JMenuItem monthView;
    JLabel statusBar;

    JPanel westButtons;
    JButton today;
    JButton next;
    JButton prev;
    JButton appointment;

    JPanel mainArea;
    JLabel placeHolder;

    boolean dayViewBool = true;

    Calendar() {
        // Setting Up Frame Functionality
        frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Setting Up Menu Bar & Items
        initMenu();

        // Initializing the Status Bar
        statusBar = new JLabel("This is a status bar!");
        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);

        // Setting Up Buttons
        today = new JButton("Today");
        today.setAlignmentX(Component.CENTER_ALIGNMENT);
        next = new JButton("Next");
        next.setAlignmentX(Component.CENTER_ALIGNMENT);
        prev = new JButton("Prev");
        prev.setAlignmentX(Component.CENTER_ALIGNMENT);
        appointment = new JButton("New Appointment");
        appointment.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Configuring the JPanel Frame
        westButtons = new JPanel();
        westButtons.setBackground(new Color(157,209,166));
        westButtons.setPreferredSize(new Dimension(200, 0));
        westButtons.setLayout(new BoxLayout(westButtons, BoxLayout.Y_AXIS));

        westButtons.add(today);
        westButtons.add(next);
        westButtons.add(prev);
        westButtons.add(appointment);

        frame.getContentPane().add(westButtons, BorderLayout.WEST);

        // Configuring Main Area (Defaulted to Day View)
        mainArea = new JPanel();
        mainArea.setPreferredSize(new Dimension(800, 600));
        mainArea.setLayout(new BorderLayout());
        mainArea.setBackground(Color.lightGray);
        LocalDate today = LocalDate.now();
        placeHolder = new JLabel();
        changeDay();
        mainArea.add(placeHolder);

        frame.getContentPane().add(mainArea);

        frame.pack();
        frame.setVisible(true);
    }

    private void initMenu() {
        // Setting Up Menu Bar & Items
        menuBar = new JMenuBar();
        file = new JMenu("File");
        view = new JMenu("View");
        exit = new JMenuItem("Exit");
        dayView = new JMenuItem("Day View");
        monthView = new JMenuItem("Month View");
        file.add(exit);
        view.add(dayView);
        view.add(monthView);
        menuBar.add(file);
        menuBar.add(view);

        exit.addActionListener(e -> System.exit(0));
        monthView.addActionListener(e -> changeMonth());
        dayView.addActionListener(e -> changeDay());
        frame.getContentPane().add(menuBar, BorderLayout.NORTH);
    }
    private void changeMonth() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM-yyyy");
        String month = date.format(monthFormatter);
        placeHolder.setText("Month View: " + month);
    }
    private void changeDay() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String day = date.format(monthFormatter);
        placeHolder.setText("Day View: " + day);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Calendar cal = new Calendar();
            }
        });
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Calendar {

    // Initializing Main Frame
    JFrame frame;

    // Initializing MenuBar
    JMenuBar menuBar;
    JMenu file;
    JMenu view;
    JMenu theme;
    JMenuItem exit;
    JMenuItem dayView;
    JMenuItem monthView;
    JMenuItem sky;
    JMenuItem forest;
    JMenuItem lavender;

    // Initializing Status Bar
    JLabel statusBar;

    // Initializing Control Buttons
    JPanel westButtons;
    JPanel westFlow;
    JButton today;
    JButton next;
    JButton prev;
    JButton appointment;


    JPanel mainArea;
    JLabel placeHolder;

    JPanel appointmentBox;

    // Misc Variables
    int temp = 0;
    boolean isDay = true;


    // Styling Components

    // Fonts
    Font verdana = new Font("Verdana", Font.BOLD, 28);
    Font franklinGothic = new Font("Franklin Gothic", Font.BOLD, 14);

    // Colors
    Color lightGreen = new Color(157,209,166);
    Color lightBlue = new Color(157,189,209);
    Color lightPurple = new Color(186,157,209);
    Color gray = new Color(209,209,209);

    Calendar() {
        // Setting Up Frame Functionality
        frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(1000,700));
        frame.setMaximumSize(new Dimension(1400,1100));

        // Setting Up Menu Bar & Items
        initMenu();

        // Initializing the Status Bar
        initStatusBar();

        // Setting Up West Buttons & Containers
        initControlPanel();

        // Configuring Main Area (Defaulted to Day View)
        initMainArea();

        // Basic Styling
        changeTheme(forest);


        frame.pack();
        frame.setVisible(true);
    }

    private void initMenu() {
        // Setting Up Menu Bar & Items
        menuBar = new JMenuBar();
        file = new JMenu("File");
        view = new JMenu("View");
        theme = new JMenu("Themes");
        exit = new JMenuItem("Exit");
        dayView = new JMenuItem("Day View");
        monthView = new JMenuItem("Month View");
        sky = new JMenuItem("Sky Theme");
        forest = new JMenuItem("Forest Theme");
        lavender = new JMenuItem("Lavender Theme");
        file.add(exit);
        view.add(dayView);
        view.add(monthView);
        theme.add(sky);
        theme.add(forest);
        theme.add(lavender);
        menuBar.add(file);
        menuBar.add(view);
        menuBar.add(theme);

        exit.addActionListener(e -> System.exit(0));
        monthView.addActionListener(e -> changeMonth());
        dayView.addActionListener(e -> changeDay());
        sky.addActionListener(e -> changeTheme(sky));
        forest.addActionListener(e -> changeTheme(forest));
        lavender.addActionListener(e -> changeTheme(lavender));

        frame.getContentPane().add(menuBar, BorderLayout.NORTH);
    }
    private void initStatusBar() {
        statusBar = new JLabel("This is a status bar!");
        statusBar.setFont(franklinGothic);
        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
    }
    private void initControlPanel() {
        westButtons = new JPanel();
        westButtons.setLayout(new BoxLayout(westButtons, BoxLayout.Y_AXIS));
        westButtons.setPreferredSize(new Dimension(200, 0));

        westFlow = new JPanel();
        westFlow.setMaximumSize(new Dimension(300, 36));

        today = new JButton("Today");
        today.setAlignmentX(Component.CENTER_ALIGNMENT);
        westButtons.add(today);
        next = new JButton("Next");
        prev = new JButton("Prev");

        westFlow.add(prev);
        westFlow.add(next);
        westButtons.add(westFlow);

        appointment = new JButton("New Appointment");
        appointment.setAlignmentX(Component.CENTER_ALIGNMENT);
        westButtons.add(appointment);


        today.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isDay) {
                    changeDay();
                    statusBar.setText("Status: Updated System to Present Day");
                } else {
                    changeMonth();
                    statusBar.setText("Status: Updated System to Present Month");
                }
            }
        });
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isDay) {
                    nextDay();
                } else {
                    nextMonth();
                }
            }
        });
        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isDay) {
                    prevDay();
                } else {
                    prevMonth();
                }
            }
        });

        appointment.addActionListener(e -> appointment());

        frame.getContentPane().add(westButtons, BorderLayout.WEST);

        // Styling Buttons
        today.setFont(franklinGothic);
        next.setFont(franklinGothic);
        prev.setFont(franklinGothic);
        appointment.setFont(franklinGothic);
    }
    private void initMainArea() {
        mainArea = new JPanel();
        mainArea.setPreferredSize(new Dimension(1200, 800));
        mainArea.setLayout(new BorderLayout());
        placeHolder = new JLabel();
        placeHolder.setFont(verdana);
        placeHolder.setHorizontalAlignment(JLabel.CENTER);
        placeHolder.setVerticalAlignment(JLabel.CENTER);
        changeDay();
        mainArea.add(placeHolder, BorderLayout.CENTER);

        frame.getContentPane().add(mainArea);
    }
    private void changeMonth() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
        String month = date.format(monthFormatter);
        placeHolder.setText("Month View: " + month);
        isDay = false;
        temp = 0;
        statusBar.setText("Status: System changed to Month View");
    }
    private void changeDay() {
        LocalDate date = LocalDate.now();
        String day = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date);
        placeHolder.setText("Day View: " + day);
        isDay = true;
        temp = 0;
        statusBar.setText("Status: System changed to Day View");
    }
    private void nextDay() {
        temp++;
        LocalDate date = LocalDate.now();
        date = date.plusDays(temp);
        String day = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date);
        placeHolder.setText("Day View: " + day);
        statusBar.setText("Status: Moved Forward 1 Day");
    }
    private void prevDay() {
        temp--;
        LocalDate date = LocalDate.now();
        date = date.plusDays(temp);
        String day = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date);
        placeHolder.setText("Day View: " + day);
        statusBar.setText("Status: Moved Backward 1 Day");
    }

    private void nextMonth() {
        temp++;
        LocalDate date = LocalDate.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
        date = date.plusMonths(temp);
        String month = date.format(monthFormatter);
        placeHolder.setText("Month View: " + month);
        statusBar.setText("Status: Moved Forward 1 Month");
    }
    private void prevMonth() {
        temp--;
        LocalDate date = LocalDate.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
        date = date.plusMonths(temp);
        String month = date.format(monthFormatter);
        placeHolder.setText("Month View: " + month);
        statusBar.setText("Status: Moved Backward 1 Month");
    }

    private void appointment() {
        appointmentBox = new JPanel();
        appointmentBox.setLayout(new BoxLayout(appointmentBox, BoxLayout.Y_AXIS));

        LocalDate currDate = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String day = currDate.format(dayFormatter);

        ImageIcon cal = new ImageIcon("graphics/cal.png");
        JTextField name = new JTextField(5);
        JTextField date = new JTextField(day.toString(),5);
        JSpinner start = new JSpinner();
        JSpinner end = new JSpinner();
        JCheckBox vacation = new JCheckBox("Vacation");
        JCheckBox work = new JCheckBox("Work");
        JCheckBox school = new JCheckBox("School");
        JCheckBox family = new JCheckBox("Family");

        appointmentBox.add(new JLabel("Enter Appointment Name: "));
        appointmentBox.add(name);
        appointmentBox.add(new JLabel("Enter Date: "));
        appointmentBox.add(date);
        appointmentBox.add(new JLabel("Enter Start Time: "));
        appointmentBox.add(start);
        appointmentBox.add(new JLabel("Enter End Time: "));
        appointmentBox.add(end);
        appointmentBox.add(vacation);
        appointmentBox.add(work);
        appointmentBox.add(school);
        appointmentBox.add(family);


        int n = JOptionPane.showConfirmDialog(frame, appointmentBox, "Appointment Creation", JOptionPane.OK_CANCEL_OPTION, 0 , cal);
        if (n != 0) {
            statusBar.setText("Status: Appointment Creation Cancelled");
        } else {
            String tags = "";
            if (vacation.isSelected()) {
                tags = tags + "Vacation ";
            }
            if (work.isSelected()) {
                tags = tags + "Work ";
            }
            if (school.isSelected()) {
                tags = tags + "School ";
            }
            if (family.isSelected()) {
                tags = tags + "Family";
            }
            String statusReport = "Name: " +  name.getText() + ", Date: " + date.getText() + ", From " + start.getValue()
                    + " to " + end.getValue() + ", Tags: " + tags;
            statusBar.setText("Status: Appointment Created - " + statusReport);
        }
    }

    private void changeTheme(JMenuItem curr) {
        if (curr == sky) {
            westFlow.setBackground(lightBlue);
            westButtons.setBackground(lightBlue);
            mainArea.setBackground(gray);
        } else if (curr == forest) {
            westFlow.setBackground(lightGreen);
            westButtons.setBackground(lightGreen);
            mainArea.setBackground(gray);
        } else {
            westFlow.setBackground(lightPurple);
            westButtons.setBackground(lightPurple);
            mainArea.setBackground(gray);
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Calendar cal = new Calendar();
            }
        });
    }
}

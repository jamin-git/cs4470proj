import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
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

    // Initializing Main Area Panels
    JLabel placeHolder;
    JScrollPane mainSection;

    // Initializing the appointment dialog box
    JPanel appointmentBox;


    // HashMap to Store EventDetails
    //Hashmap<LocalDate d, EventDetails e>

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
        // These dimension work on my computer screen, these are untested on other monitors / OS
        frame.setMinimumSize(new Dimension(1200,700));
        frame.setMaximumSize(new Dimension(1600,1100));

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

        // Adding ActionListeners for each item
        exit.addActionListener(e -> System.exit(0));
        monthView.addActionListener(e -> changeMonth());
        dayView.addActionListener(e -> changeDay());
        sky.addActionListener(e -> changeTheme(sky));
        forest.addActionListener(e -> changeTheme(forest));
        lavender.addActionListener(e -> changeTheme(lavender));

        frame.getContentPane().add(menuBar, BorderLayout.NORTH);
    }
    private void initStatusBar() {
        // Initializing the Status Bar
        statusBar = new JLabel("This is a status bar!");
        statusBar.setFont(franklinGothic);
        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
    }
    private void initControlPanel() {
        // Initializing the westButtons (Control Buttons)
        westButtons = new JPanel();
        westButtons.setLayout(new BoxLayout(westButtons, BoxLayout.Y_AXIS));
        westButtons.setPreferredSize(new Dimension(200, 0));

        // Initializing Button Icons
        URL rArrow = getClass().getResource("ArrowR.png");
        ImageIcon rightArrow = new ImageIcon(rArrow);

        URL lArrow = getClass().getResource("ArrowL.png");
        ImageIcon leftArrow = new ImageIcon(lArrow);



        // Adding Vertical Glue to Beginning of the BoxLayout
        westButtons.add(Box.createVerticalGlue());

        // Configuring Flow Layout for Prev & Next Buttons
        westFlow = new JPanel();
        westFlow.setMaximumSize(new Dimension(300, 40));

        // Adding the Today Button
        today = new JButton("Today");
        today.setAlignmentX(Component.CENTER_ALIGNMENT);

        westButtons.add(today);
        westButtons.add(Box.createRigidArea(new Dimension(0,10)));

        // Adding the Prev / Next Button
        next = new JButton(rightArrow);
        next.setText("Next ");
        next.setHorizontalTextPosition(SwingConstants.LEFT);

        prev = new JButton(leftArrow);
        prev.setText(" Prev");
        prev.setHorizontalTextPosition(SwingConstants.RIGHT);

        westFlow.add(prev);
        westFlow.add(next);

        westButtons.add(westFlow);
        westButtons.add(Box.createRigidArea(new Dimension(0,10)));

        // Adding the Appointment Button
        appointment = new JButton("New Appointment");
        appointment.setAlignmentX(Component.CENTER_ALIGNMENT);

        westButtons.add(appointment);


        // Action Listeners for Buttons
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


        westButtons.add(Box.createVerticalGlue());

        // Adding Buttons to the Frame
        frame.getContentPane().add(westButtons, BorderLayout.WEST);


        // Styling Buttons
        today.setFont(franklinGothic);
        next.setFont(franklinGothic);
        prev.setFont(franklinGothic);
        appointment.setFont(franklinGothic);
    }
    private void initMainArea() {
        DayView dV = new DayView();
        mainSection = new JScrollPane(dV);
        mainSection.setPreferredSize(new Dimension(600, 700));
        frame.getContentPane().add(mainSection, BorderLayout.CENTER);

























        // Initializing the mainArea Panel and the PlaceHolder view
//        mainArea = new JPanel();
//        mainArea.setLayout(new BorderLayout());
//
//        DayView dV = new DayView();
//        mainArea.add(dV);
//
//        JScrollPane s = new JScrollPane(mainArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        s.setViewportView(dV);
//        frame.getContentPane().add(s);
//        placeHolder = new JLabel();
//        placeHolder.setFont(verdana);
//        placeHolder.setHorizontalAlignment(JLabel.CENTER);
//        placeHolder.setVerticalAlignment(JLabel.CENTER);
//
//        // Defaults to DayView
//        changeDay();
//
//        // Adding to the Frame
//        mainArea.add(placeHolder, BorderLayout.CENTER);
//        frame.getContentPane().add(mainArea);
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
        // Configuring the Appointment Panel
        appointmentBox = new JPanel();
        appointmentBox.setLayout(new BoxLayout(appointmentBox, BoxLayout.Y_AXIS));

        // Determining the Current Day
        LocalDate currDate = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String day = currDate.format(dayFormatter);

        // Calendar Icon Initialization
        URL calURL = getClass().getResource("cal.png");
        ImageIcon cal = new ImageIcon(calURL);

        // TextField Initialization
        JTextField name = new JTextField(5);
        JTextField date = new JTextField(day,5);


        // Used a ComboBox for Time Picking
        String[] times = {"12am", "1am", "2am", "3am", "4am", "5am", "6am", "7am", "8am", "9am", "10am", "11am", "12pm",
                "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm", "11pm"};
        JComboBox start = new JComboBox(times);
        JComboBox end = new JComboBox(times);


        // JCheckBox Initialization
        JCheckBox vacation = new JCheckBox("Vacation");
        JCheckBox work = new JCheckBox("Work");
        JCheckBox school = new JCheckBox("School");
        JCheckBox family = new JCheckBox("Family");
        JCheckBox other = new JCheckBox("Other");

        // Adding JLabels & Inputs
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
        appointmentBox.add(other);

        // Creating Dialog Box
        int n = JOptionPane.showConfirmDialog(frame, appointmentBox, "Appointment Creation", JOptionPane.OK_CANCEL_OPTION, 0 , cal);

        // Creating StatusBar String
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
                tags = tags + "Family ";
            }
            if (other.isSelected()) {
                tags = tags + "Other";
            }
            String statusReport = "Name: " +  name.getText() + ", Date: " + date.getText() + ", From " + start.getSelectedItem()
                    + " to " + end.getSelectedItem() + ", Tags: " + tags;
            statusBar.setText("Status: Appointment Created - " + statusReport);
        }
    }

    private void changeTheme(JMenuItem curr) {
        if (curr == sky) {
            westFlow.setBackground(lightBlue);
            westButtons.setBackground(lightBlue);
            statusBar.setText("Status: Changed to Sky Theme");
        } else if (curr == forest) {
            westFlow.setBackground(lightGreen);
            westButtons.setBackground(lightGreen);
            statusBar.setText("Status: Changed to Forest Theme");
        } else {
            westFlow.setBackground(lightPurple);
            westButtons.setBackground(lightPurple);
            statusBar.setText("Status: Changed to Lavender Theme");
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

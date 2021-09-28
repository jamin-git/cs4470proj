import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Calendar extends JFrame {

    // Initializing Main Frame
    private static JFrame frame;

    // Initializing MenuBar
    private JMenuBar menuBar;
    private JMenu file;
    private JMenu view;
    private JMenu theme;
    private JMenuItem exit;
    private JMenuItem dayView;
    private JMenuItem monthView;
    private JMenuItem sky;
    private JMenuItem forest;
    private JMenuItem lavender;

    // Initializing Status Bar
    private static JLabel statusBar;

    // Initializing Control Buttons
    private JPanel westButtons;
    private JPanel westFlow;
    private JButton today;
    private JButton next;
    private JButton prev;
    private JButton appointment;

    // Initializing Main Area Panels
    private JLabel placeHolder;
    private JScrollPane mainSection;

    // Initializing the appointment dialog box
    private static JPanel appointmentBox;

    // DayView Component Initialization
    private DayView dV = new DayView();


    // HashMap to Store EventDetails, Event Details are within an array to support multiple events
    private static HashMap<LocalDate, ArrayList<EventDetails>> eventDetails = new HashMap<>();

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


    // Time Key
    private String[] timeKey = {"0000", "0015", "0030", "0045", "0100", "0115", "0130", "0145",
            "0200", "0200", "0200", "0200", "0300", "0300", "0300", "0300",
            "0400", "0400", "0400", "0400", "0500", "0500", "0500", "0500",
            "0600", "0600", "0600", "0600", "0700", "0700", "0700", "0700",
            "0800", "0800", "0800", "0800", "0900", "0900", "9:30am", "9:45am",
            "10:00am", "8:15am", "8:30am", "8:45am", "9:00am", "9:15am", "9:30am", "9:45am",
            "12:00pm", "12:15pm", "12:30pm", "12:45pm", "1:00pm", "1:15pm", "1:30pm", "1:45pm",
            "2:00pm", "2:15pm", "2:30pm", "2:45pm", "3:00pm", "3:15pm", "3:30pm", "3:45pm",
            "4:00pm", "4:15pm", "4:30pm", "4:45pm", "5:00pm", "5:15pm", "5:30pm", "5:45pm",
            "6:00pm", "6:15pm", "6:30pm", "6:45pm", "7:00pm", "7:15pm", "7:30pm", "7:45pm",
            "8:00pm", "8:15pm", "8:30pm", "8:45pm", "9:00pm", "9:15pm", "9:30pm", "9:45pm",
            "10:00pm", "10:15pm", "10:30pm", "10:45pm", "11:00pm", "11:15pm", "11:30pm", "11:45pm"};

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
        mainSection = new JScrollPane(dV);
        mainSection.setPreferredSize(new Dimension(600, 700));
        frame.getContentPane().add(mainSection, BorderLayout.CENTER);


        // PlaceHolder code
        placeHolder = new JLabel();
        placeHolder.setFont(verdana);
        placeHolder.setHorizontalAlignment(JLabel.CENTER);
        placeHolder.setVerticalAlignment(JLabel.CENTER);





















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
        mainSection.setViewportView(placeHolder);
        isDay = false;
        temp = 0;
        statusBar.setText("Status: System changed to Month View");
    }
    private void changeDay() {
//        LocalDate date = LocalDate.now();
//        String day = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date);
//        placeHolder.setText("Day View: " + day);
        dV.setDate(LocalDate.now());
        mainSection.setViewportView(dV);
        isDay = true;
        temp = 0;
        statusBar.setText("Status: System changed to Day View");
    }
    private void nextDay() {
        temp++;
        LocalDate date = LocalDate.now();
        date = date.plusDays(temp);
        dV.setDate(date);
        mainSection.setViewportView(dV);
//        String day = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date);
//        placeHolder.setText("Day View: " + day);
        statusBar.setText("Status: Moved Forward 1 Day");
    }
    private void prevDay() {
        temp--;
        LocalDate date = LocalDate.now();
        date = date.plusDays(temp);
        dV.setDate(date);
        mainSection.setViewportView(dV);
//        String day = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date);
//        placeHolder.setText("Day View: " + day);
        statusBar.setText("Status: Moved Backward 1 Day");
    }
    private void nextMonth() {
        temp++;
        LocalDate date = LocalDate.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
        date = date.plusMonths(temp);
        String month = date.format(monthFormatter);
        placeHolder.setText("Month View: " + month);
        mainSection.setViewportView(placeHolder);
        statusBar.setText("Status: Moved Forward 1 Month");
    }
    private void prevMonth() {
        temp--;
        LocalDate date = LocalDate.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
        date = date.plusMonths(temp);
        String month = date.format(monthFormatter);
        placeHolder.setText("Month View: " + month);
        mainSection.setViewportView(placeHolder);
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
        String[] times = {"12:00am", "12:15am", "12:30am", "12:45am", "1:00am", "1:15am", "1:30am", "1:45am",
                "2:00am", "2:15am", "2:30am", "2:45am", "3:00am", "3:15am", "3:30am", "3:45am",
                "4:00am", "4:15am", "4:30am", "4:45am", "5:00am", "5:15am", "5:30am", "5:45am",
                "6:00am", "6:15am", "6:30am", "6:45am", "7:00am", "7:15am", "7:30am", "7:45am",
                "8:00am", "8:15am", "8:30am", "8:45am", "9:00am", "9:15am", "9:30am", "9:45am",
                "10:00am", "8:15am", "8:30am", "8:45am", "9:00am", "9:15am", "9:30am", "9:45am",
                "12:00pm", "12:15pm", "12:30pm", "12:45pm", "1:00pm", "1:15pm", "1:30pm", "1:45pm",
                "2:00pm", "2:15pm", "2:30pm", "2:45pm", "3:00pm", "3:15pm", "3:30pm", "3:45pm",
                "4:00pm", "4:15pm", "4:30pm", "4:45pm", "5:00pm", "5:15pm", "5:30pm", "5:45pm",
                "6:00pm", "6:15pm", "6:30pm", "6:45pm", "7:00pm", "7:15pm", "7:30pm", "7:45pm",
                "8:00pm", "8:15pm", "8:30pm", "8:45pm", "9:00pm", "9:15pm", "9:30pm", "9:45pm",
                "10:00pm", "10:15pm", "10:30pm", "10:45pm", "11:00pm", "11:15pm", "11:30pm", "11:45pm"};
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
            ArrayList<String> tag = new ArrayList<>();
            if (vacation.isSelected()) {
                tags = tags + "Vacation ";
                tag.add("Vacation");
            }
            if (work.isSelected()) {
                tags = tags + "Work ";
                tag.add("Work");
            }
            if (school.isSelected()) {
                tags = tags + "School ";
                tag.add("School");
            }
            if (family.isSelected()) {
                tags = tags + "Family ";
                tag.add("Family");
            }
            if (other.isSelected()) {
                tags = tags + "Other";
                tag.add("Other");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");



            EventDetails e = new EventDetails(name.getText(), LocalDate.parse(date.getText(), formatter),
                    convertTime(start.getSelectedItem().toString()), convertTime(end.getSelectedItem().toString()),
                    start.getSelectedIndex(), end.getSelectedIndex(),
                    tag, Math.abs(start.getSelectedIndex() - end.getSelectedIndex()));

            // Adding event details to the hashmap
            if (eventDetails.containsKey(e.getDate())) {
                eventDetails.get(e.getDate()).add(e);
            } else {
                ArrayList<EventDetails> list = new ArrayList<>();
                list.add(e);
                eventDetails.put(e.getDate(), list);
            }
            statusBar.setText("Status: Appointment Created - " + e);
        }
    }

    public static void appointmentFilled(EventDetails event) {
        // Configuring the Appointment Panel
        JPanel newBox = new JPanel();
        newBox.setLayout(new BoxLayout(newBox, BoxLayout.Y_AXIS));

        // Determining the Current Day
        LocalDate currDate = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String day = currDate.format(dayFormatter);

        // Calendar Icon Initialization
        URL calURL = Calendar.class.getResource("cal.png");
        ImageIcon cal = new ImageIcon(calURL);

        // TextField Initialization
        JTextField name = new JTextField(5);
        name.setText(event.getName());
        JTextField date = new JTextField(day,5);
        date.setText(event.getDate().format(dayFormatter));


        // Used a ComboBox for Time Picking
        String[] times = {"12:00am", "12:15am", "12:30am", "12:45am", "1:00am", "1:15am", "1:30am", "1:45am",
                "2:00am", "2:15am", "2:30am", "2:45am", "3:00am", "3:15am", "3:30am", "3:45am",
                "4:00am", "4:15am", "4:30am", "4:45am", "5:00am", "5:15am", "5:30am", "5:45am",
                "6:00am", "6:15am", "6:30am", "6:45am", "7:00am", "7:15am", "7:30am", "7:45am",
                "8:00am", "8:15am", "8:30am", "8:45am", "9:00am", "9:15am", "9:30am", "9:45am",
                "10:00am", "8:15am", "8:30am", "8:45am", "9:00am", "9:15am", "9:30am", "9:45am",
                "12:00pm", "12:15pm", "12:30pm", "12:45pm", "1:00pm", "1:15pm", "1:30pm", "1:45pm",
                "2:00pm", "2:15pm", "2:30pm", "2:45pm", "3:00pm", "3:15pm", "3:30pm", "3:45pm",
                "4:00pm", "4:15pm", "4:30pm", "4:45pm", "5:00pm", "5:15pm", "5:30pm", "5:45pm",
                "6:00pm", "6:15pm", "6:30pm", "6:45pm", "7:00pm", "7:15pm", "7:30pm", "7:45pm",
                "8:00pm", "8:15pm", "8:30pm", "8:45pm", "9:00pm", "9:15pm", "9:30pm", "9:45pm",
                "10:00pm", "10:15pm", "10:30pm", "10:45pm", "11:00pm", "11:15pm", "11:30pm", "11:45pm"};
        JComboBox start = new JComboBox(times);
        start.setSelectedIndex(event.getStartIndex());
        JComboBox end = new JComboBox(times);
        end.setSelectedIndex(event.getEndIndex());


        // JCheckBox Initialization
        JCheckBox vacation = new JCheckBox("Vacation");
        JCheckBox work = new JCheckBox("Work");
        JCheckBox school = new JCheckBox("School");
        JCheckBox family = new JCheckBox("Family");
        JCheckBox other = new JCheckBox("Other");
        if (event.getTags() != null) {
            for (String string : event.getTags()) {
                if (string.equals("Vacation")) {
                    vacation.setSelected(true);
                } else if (string.equals("Work")) {
                    work.setSelected(true);
                } else if (string.equals("School")) {
                    school.setSelected(true);
                } else if (string.equals("Family")) {
                    family.setSelected(true);
                } else if (string.equals("Other")) {
                    other.setSelected(true);
                }
            }
        }

        // Adding JLabels & Inputs
        newBox.add(new JLabel("Enter Appointment Name: "));
        newBox.add(name);
        newBox.add(new JLabel("Enter Date: "));
        newBox.add(date);
        newBox.add(new JLabel("Enter Start Time: "));
        newBox.add(start);
        newBox.add(new JLabel("Enter End Time: "));
        newBox.add(end);
        newBox.add(vacation);
        newBox.add(work);
        newBox.add(school);
        newBox.add(family);
        newBox.add(other);

        // Creating Dialog Box
        int pane = JOptionPane.showConfirmDialog(frame, newBox, "Appointment Creation", JOptionPane.OK_CANCEL_OPTION, 0 , cal);

        // Creating StatusBar String
        if (pane != 0) {
            statusBar.setText("Status: Appointment Creation Cancelled");
            if (!(event.getTime() == 0)) {
                if (eventDetails.containsKey(event.getDate())) {
                    eventDetails.get(event.getDate()).add(event);
                } else {
                    ArrayList<EventDetails> list = new ArrayList<>();
                    list.add(event);
                    eventDetails.put(event.getDate(), list);
                }
            }
        } else {
            String tags = "";
            ArrayList<String> tag = new ArrayList<>();
            if (vacation.isSelected()) {
                tags = tags + "Vacation ";
                tag.add("Vacation");
            }
            if (work.isSelected()) {
                tags = tags + "Work ";
                tag.add("Work");
            }
            if (school.isSelected()) {
                tags = tags + "School ";
                tag.add("School");
            }
            if (family.isSelected()) {
                tags = tags + "Family ";
                tag.add("Family");
            }
            if (other.isSelected()) {
                tags = tags + "Other";
                tag.add("Other");
            }



            EventDetails e = new EventDetails(name.getText(), LocalDate.parse(date.getText(), dayFormatter),
                     convertTime(start.getSelectedItem().toString()), convertTime(end.getSelectedItem().toString()),
                     start.getSelectedIndex(), end.getSelectedIndex(),
                     tag, Math.abs(start.getSelectedIndex() - end.getSelectedIndex()));
            System.out.println(e.getBoundingRectangle());

            // Adding event details to the hashmap
            if (eventDetails.containsKey(e.getDate())) {
                eventDetails.get(e.getDate()).add(e);
            } else {
                ArrayList<EventDetails> list = new ArrayList<>();
                list.add(e);
                eventDetails.put(e.getDate(), list);
            }

            statusBar.setText("Status: Appointment Created - " + event);
        }
    }


    private static int convertTime(String time) {
        String am = time.substring(time.length() - 2, time.length());
        time = time.substring(0, time.length() - 2);
        String temp = time.substring(0, time.length() - 3);
        String temp2 = time.substring(time.length() - 2, time.length());
        time = temp + temp2;
        int ret = Integer.parseInt(time);
        if (am.equals("pm")) {
            ret = ret + 1200;
        } else if (ret == 1200 || ret == 1215 || ret == 1230 || ret == 1245) {
            ret = 0;
        }
        return ret;
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

    public static HashMap<LocalDate, ArrayList<EventDetails>> getEventDetails() {
        return eventDetails;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Calendar cal = new Calendar();
            }
        });
    }
}

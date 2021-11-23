import dollar.DollarRecognizer;
import dollar.Result;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MonthView extends JComponent {
    private LocalDate date = LocalDate.now();
    private int xSize = 1065;
    private int ySize = 800;
    private Color gray = new Color(209,209,209);

    private int rowCount = 6;
    private int columnCount = 7;
    private ArrayList<Rectangle> rectList = new ArrayList<>();
    private String[] arrDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    private Font franklinGothic = new Font("Franklin Gothic", Font.BOLD, 14);
    private Font sfranklinGothic = new Font("Franklin Gothic", Font.BOLD, 10);

    private MonthView.Handlerclass handler = new MonthView.Handlerclass();

    // Variables for Layout
    private int initGrayBoxes = 0;
    private int countDays = 0;

    // Gesture Variables
    private ArrayList<Point2D> strokes = null;
    private DollarRecognizer dr = new DollarRecognizer();
    private EventDetails strokeEvent = null;

    // Map Variables
    private HashMap<LocalDate, ArrayList<EventDetails>> map = Calendar.getEventDetails();
    private ArrayList<EventDetails> list = map.get(date);


    // Animation Variables
    private Timer timerR = new Timer(25, new ActionListener() {
        public void actionPerformed(ActionEvent event){
            x += xVel;
            count++;
            repaint();
            System.out.println("In Action Performed");
        }
    });
    private Timer timerL = new Timer(25, new ActionListener() {
        public void actionPerformed(ActionEvent event){
            x += xVel;
            count++;
            repaint();
            System.out.println("In Action Performed");
        }
    });
    int x = 0;
    int xVel = 40;
    int count = 0;
    boolean animationEnd = false;

    public MonthView() {
        date = LocalDate.now();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Resetting the list of Rectangles to resize them
        rectList.clear();

        // Updating Window Size
        xSize = Calendar.getScrollPaneWidth();
        ySize = Calendar.getScrollPaneHeight();


        if (Calendar.animateNext) {
            if (!animationEnd) {
                System.out.println("Timer started");
                timerL.restart();
                animationEnd = !animationEnd;
            }

            g.drawImage(Calendar.nextImage, 0, 0, this);


            if (count > 30) {
                timerL.stop();
                x = 0;
                count = 0;
                Calendar.animateNext = false;
                repaint();
                animationEnd = !animationEnd;
                System.out.println("Timer Ended");
            }

            int width = (xSize) - (xSize * count / 31);
            BufferedImage portion = Calendar.currImage.getSubimage(0, 0, width, ySize);
            g.drawImage(portion, 0, 0, this);
            g.setColor(Color.WHITE);
            g.fillRect(x, 0, 100, ySize);
        } else if (Calendar.animatePrev) {

            if (!animationEnd) {
                System.out.println("Timer started");
                timerR.restart();
                animationEnd = !animationEnd;
            }

            g.drawImage(Calendar.nextImage, 0, 0, this);

            if (count > 30) {
                timerR.stop();
                x = 0;
                count = 0;
                Calendar.animatePrev = false;
                repaint();
                animationEnd = !animationEnd;
                System.out.println("Timer Ended");
            }


            int width = (xSize) - (xSize * count / 31);
            BufferedImage portion = Calendar.currImage.getSubimage(0, 0, width, ySize);
            g.drawImage(portion, 0, 0, this);
            g.setColor(Color.WHITE);
            g.fillRect(xSize - x, 0, 100, ySize);
        } else {
            // Creating Rectangle
            g.setColor(gray);
            if (Calendar.getTheme().equals("Sky")) {
                g.setColor(new Color(157,189,209));
            } else if (Calendar.getTheme().equals("Forest")) {
                g.setColor(new Color(157,209,166));
            } else if (Calendar.getTheme().equals("Lavender")) {
                g.setColor(new Color(186,157,209) );
            }
            g.drawRect(0,0, xSize, ySize);
            g.fillRect(0,0, xSize, ySize);

            // Creating Date String
            g.setColor(Color.BLACK);
            g.setFont(franklinGothic);
            FontMetrics fm = g.getFontMetrics();
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM, YYYY");
            String dateString = date.format(monthFormatter);
            int x = (xSize / 2) - fm.stringWidth(dateString) / 2;
            int y = fm.getHeight();
            g.drawString(dateString, x, y + 5);


            // Painting 6x7 Grid with resizability
            int width = xSize - 25;
            int height = ySize - 100;

            int rectWidth = width / columnCount;
            int rectHeight = height / rowCount;

            int xOffset = (width - (columnCount * rectWidth)) / 2;
            int yOffset = (height + 150 - (rowCount * rectHeight)) / 2;

            boolean drawnDays = false;

            // Drawing Grid + Labels
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    if (rectList.size() < 42) {
                        rectList.add(new Rectangle(xOffset + (col * rectWidth), yOffset + (row * rectHeight),
                                rectWidth, rectHeight));
                    }
                    g.drawRect(xOffset + (col * rectWidth), yOffset + (row * rectHeight),
                            rectWidth, rectHeight);
                    if (!drawnDays) {
                        for (int i = 0; i < arrDays.length; i++) {
                            String day = arrDays[i];
                            int xPos = i * rectWidth + rectWidth / 2 - fm.stringWidth(day) / 2;
                            int yPos = fm.getHeight() + 50;
                            g.drawString(day, xPos, yPos);
                        }
                        drawnDays = true;
                    }
                }
            }

            // Data Initialization
            LocalDate start = date.withDayOfMonth(1);
            DateTimeFormatter day = DateTimeFormatter.ofPattern("EEEE");
            DateTimeFormatter currDay = DateTimeFormatter.ofPattern("d");
            DateTimeFormatter currMonth = DateTimeFormatter.ofPattern("MMMM");
            String dayString = day.format(start);
            boolean monthStart = false;
            countDays = 0;
            initGrayBoxes = 0;

            // Filling Inactive Cells and Numbering
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    g.setColor(new Color(0, 0, 0, 130));
                    if (!monthStart) {
                        if (arrDays[col].equals(dayString)) {
                            col--;
                            monthStart = true;
                        } else {
                            g.fillRect(xOffset + (col * rectWidth), yOffset + (row * rectHeight),
                                    rectWidth, rectHeight);
                            initGrayBoxes++;
                        }
                    } else {
                        countDays++;
                        if (countDays == Integer.parseInt(currDay.format(date)) && currMonth.format(date).equals(currMonth.format(LocalDate.now()))) {
                            g.setColor(new Color(255, 255, 255, 100));
                            g.fillRect(xOffset + (col * rectWidth), yOffset + (row * rectHeight),
                                    rectWidth, rectHeight);
                        }
                        if (countDays > date.lengthOfMonth()) {
                            g.setColor(new Color(0, 0, 0, 130));
                            g.fillRect(xOffset + (col * rectWidth), yOffset + (row * rectHeight),
                                    rectWidth, rectHeight);
                            countDays--;
                        } else {
                            g.setColor(Color.black);
                            g.drawString(String.valueOf(countDays), xOffset + (col * rectWidth) + 3 , yOffset + (row * rectHeight) + 13);
                        }
                    }
                }
            }

            // Painting Events
            map = Calendar.getEventDetails();
            DateTimeFormatter month = DateTimeFormatter.ofPattern("MMMM");
            String monthString = month.format(date);

            DateTimeFormatter dayInt = DateTimeFormatter.ofPattern("d");
            int eventHeight = 15;
            int topDist = 15;
            g.setFont(sfranklinGothic);
            fm = g.getFontMetrics();

            for (LocalDate temp : map.keySet()) {
                if (month.format(temp).equals(monthString)) {
                    list = map.get(temp);
                    // Sorting the ArrayList so events are displayed in order
                    Collections.sort(list);
                    int size = (rectHeight - 20) / eventHeight;
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (i >= size) {
                                break;
                            }
                            // Storing event and other vars
                            EventDetails e = list.get(i);
                            Rectangle curr = rectList.get(Integer.parseInt(dayInt.format(e.getDate())) + initGrayBoxes - 1);
                            int vertPadding = (i * eventHeight + 2);

                            // Drawing event box

                            // Setting Color of Event Box
                            if (e.getTags().contains("Other")) {
                                g.setColor(new Color(66, 142, 89, 200));
                            } else if (e.getTags().contains("Family")) {
                                g.setColor(new Color(243, 166, 14, 200));
                            } else if (e.getTags().contains("School")) {
                                g.setColor(new Color(12, 85, 109, 200));
                            } else if (e.getTags().contains("Work")) {
                                g.setColor(new Color(142, 70, 66, 200));
                            } else if (e.getTags().contains("Vacation")) {
                                g.setColor(new Color(87, 213, 203, 200));
                            } else {
                                g.setColor(new Color(141, 135, 145, 200));
                            }
                            // Drawing Event Rectangle + Setting Bounding Box of Event
                            Rectangle rect = new Rectangle((int) curr.getX() + 5, (int) curr.getY() + topDist + vertPadding, (int) curr.getWidth() - 10, eventHeight);
                            g.fillRoundRect((int) curr.getX() + 5, (int) curr.getY() + topDist + vertPadding, (int) curr.getWidth() - 10, eventHeight, 10, 10);
                            e.setBoundingRectangle(rect);

                            // Resizing Name
                            String tempName = e.getName();
                            String ellipse = "...";
                            if (fm.stringWidth(tempName) > (int) curr.getWidth() - 10) {
                                for (int j = tempName.length(); j > 0; j--) {
                                    tempName = tempName.substring(0, j);
                                    if (fm.stringWidth(tempName) + fm.stringWidth(ellipse) < (int) curr.getWidth() - 10) {
                                        tempName = tempName + ellipse;
                                        break;
                                    }
                                }
                            }
                            // Drawing Name
                            g.setColor(Color.BLACK);
                            g.drawString(tempName, (int) curr.getX() + 7, (int) curr.getY() + 11 + vertPadding + fm.getHeight());
                        }
                    }
                }
            }
            // Drawing the Strokes
            g.setColor(Color.BLUE);
            if (strokes != null) {
                Point2D temp = null;
                for (Point2D stroke : strokes) {
                    if (temp != null) {
                        g.drawLine((int)temp.getX(), (int)temp.getY(), (int)stroke.getX(), (int)stroke.getY());
                    }
                    temp = stroke;
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(xSize, ySize);
    }

    private class Handlerclass extends MouseInputAdapter {

        EventDetails newEvent = new EventDetails("New Event", date, 0, 0, 0, 0, new ArrayList<String>(), 1);
        HashMap<LocalDate, ArrayList<EventDetails>> map = Calendar.getEventDetails();
        ArrayList<EventDetails> list = map.get(date);
        DateTimeFormatter month = DateTimeFormatter.ofPattern("MMMM");
        String monthString = month.format(date);
        boolean selectedEvent = false;
        EventDetails curr = null;

        // Left and Right Mouse click variables
        boolean left = false;
        boolean right = false;
        boolean startDrag = false;

        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (e.getButton() == MouseEvent.BUTTON1) {
                left = true;
                right = false;

                boolean isEvent = false;
                if (map != null || map.isEmpty()) {
                    for (LocalDate temp : map.keySet()) {
                        if (month.format(temp).equals(monthString)) {
                            list = map.get(temp);
                            for (EventDetails event : list) {
                                if (e.getClickCount() == 2 && !e.isConsumed() && event.getBoundingRectangle().contains(x, y)) {
                                    Calendar.appointmentFilled(event, false);
                                    list.remove(event);
                                    e.consume();
                                    isEvent = true;
                                    repaint();
                                    break;
                                }
                            }
                            if (isEvent) {
                                break;
                            }
                        }
                    }
                }
                if (!isEvent) {
                    for (Rectangle rect : rectList) {
                        if (e.getClickCount() == 2 && !e.isConsumed() && rect.contains(x, y)) {
                            int t = rectList.indexOf(rect);
                            if (t > initGrayBoxes - 1 && t < countDays + initGrayBoxes) {
                                LocalDate d = LocalDate.of(date.getYear(), date.getMonth(),t - initGrayBoxes + 1);
                                newEvent = new EventDetails("New Event", d, 0, 1, 0, 4, new ArrayList<String>(), 1);
                                Calendar.appointmentFilled(newEvent, true);
                                e.consume();
                                repaint();
                                break;
                            }
                        }
                    }
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                right = true;
                left = false;
                strokes = new ArrayList<>();
            }
        }
        public void mouseReleased(MouseEvent e) {
            selectedEvent = false;
            list = null;

            // Functionality for strokes
            if (strokes != null && strokes.size() > 0) {
                Result r = dr.recognize(strokes);
                if (map != null || map.isEmpty()) {
                    for (LocalDate temp : map.keySet()) {
                        if (month.format(temp).equals(monthString)) {
                            list = map.get(temp);
                            for (EventDetails event : list) {
                                if (event.getBoundingRectangle().contains(strokes.get(0))) {
                                    strokeEvent = event;
                                }
                            }
                        }
                    }
                }
                if (r != null) {
                    if (r.getName().equals("delete")) {
                        if (strokeEvent != null) {
                            Calendar.removeMap(strokeEvent.getDate(), strokeEvent);
                            Calendar.setStatusBar("Event deleted via delete gesture with an accuracy score of: " + round(r.getScore()));
                        }
                    } else if (r.getName().equals("right square bracket")) {
                        Calendar.nextMonth();
                        Calendar.setStatusBar("Moved to next month via right bracket gesture with an accuracy score of: " +
                                Math.round(r.getScore() * Math.pow(10, 2)) / Math.pow(10, 2));
                    } else if (r.getName().equals("left square bracket")) {
                        Calendar.prevMonth();
                        Calendar.setStatusBar("Moved to prev month via left bracket gesture with an accuracy score of: " + round(r.getScore()));
                    }
                    // Checks for Vacation Tag
                    else if (r.getName().equals("star")) {
                        if (strokeEvent != null) {
                            strokeEvent.toggleTag("Vacation");
                            Calendar.setStatusBar("Toggled Vacation Tag via star gesture with an accuracy score of: " + round(r.getScore()));
                        }
                    }
                    // Checks for Work Tag
                    else if (r.getName().equals("check")) {
                        if (strokeEvent != null) {
                            strokeEvent.toggleTag("Work");
                            Calendar.setStatusBar("Toggled Work Tag via check gesture with an accuracy score of: " + round(r.getScore()));
                        }
                    }
                    // Checks for School Tag
                    else if (r.getName().equals("x")) {
                        if (strokeEvent != null) {
                            strokeEvent.toggleTag("School");
                            Calendar.setStatusBar("Toggled School Tag via x gesture with an accuracy score of: " + round(r.getScore()));
                        }
                    }
                    // Checks for Family Tag
                    else if (r.getName().equals("pigtail")) {
                        if (strokeEvent != null) {
                            System.out.println(strokeEvent.getBoundingRectangle());
                            strokeEvent.toggleTag("Family");
                            Calendar.setStatusBar("Toggled Family Tag via pigtail gesture with an accuracy score of: " + round(r.getScore()));
                        }
                    }
                    // Checks for Other Tag
                    else if (r.getName().equals("triangle")) {
                        if (strokeEvent != null) {
                            strokeEvent.toggleTag("Other");
                            Calendar.setStatusBar("Toggled Other Tag via triangle gesture with an accuracy score of: " + round(r.getScore()));
                        }
                    }
                    // Checks Zig-Zag
                    else if (r.getName().equals("zig-zag")) {
                        if (strokeEvent != null) {
                            if (r.getCandidate().getRadians() < -1) {
                                addVerticalEvents(strokeEvent);
                                Calendar.setStatusBar("Added events vertically via zig-zag gesture with an accuracy score of: " + round(r.getScore()));
                            } else {
                                addHorizontalEvents(strokeEvent);
                                Calendar.setStatusBar("Added events horizontally via zig-zag gesture with an accuracy score of: " + round(r.getScore()));
                            }
                        }
                    }
                    // For Gestures that don't match
                    else {
                        Calendar.setStatusBar("Gesture did not match");
                    }
                }
            }

            left = false;
            right = false;
            strokes = null;
            strokeEvent = null;
            startDrag = false;
            repaint();
        }
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (!startDrag) {
                strokes = new ArrayList<>();
                startDrag = true;
            }

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (map != null && !selectedEvent) {
                    // Iterating through the map keys
                    for (LocalDate tempDate : map.keySet()) {
                        // Checking if the current date is in the month
                        if (month.format(tempDate).equals(monthString)) {
                            list = map.get(tempDate);
                            if (list != null) {
                                for (EventDetails event : list) {
                                    if (event.getBoundingRectangle().contains(x, y)) {
                                        curr = event;
                                        selectedEvent = true;
                                        break;
                                    }
                                }
                                if (selectedEvent) {
                                    break;
                                }
                            }
                        }
                    }
                }
                if (selectedEvent) {
                    for (Rectangle rect : rectList) {
                        if (rect.contains(x, y)) {
                            int t = rectList.indexOf(rect);
                            if (t > initGrayBoxes - 1 && t < countDays + initGrayBoxes) {
                                list.remove(curr);
                                curr.setDate(LocalDate.of(date.getYear(), date.getMonth(),t - initGrayBoxes + 1));
                                Calendar.addMap(curr);
                                list = map.get(LocalDate.of(date.getYear(), date.getMonth(),t - initGrayBoxes + 1));
                            }
                            repaint();
                        }
                    }
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                strokes.add(new Point(x, y));
                repaint();
            }
        }
    }
    // Updates the mouseListeners when a new date is needed
    private void updateListener() {
        handler = new MonthView.Handlerclass();
    }
    public void setDate(LocalDate n) {
        date = n;
        removeMouseListener(handler);
        removeMouseMotionListener(handler);
        updateListener();
        addMouseMotionListener(handler);
        addMouseListener(handler);
    }

    private void addVerticalEvents(EventDetails e) {
        DateTimeFormatter day = DateTimeFormatter.ofPattern("d");
        int totalDays = e.getDate().lengthOfMonth();
        int dayOfMonth = Integer.parseInt(day.format(e.getDate()));

        int c1 = dayOfMonth - 7;
        int c2 = dayOfMonth + 7;
        int count1 = 0;
        int count2 = 0;

        // Days before selected date
        while (c1 >= 1) {
            count1++;
            EventDetails newEvent = e.copyEvent(e.getDate().minusDays(7 * count1));
            Calendar.addMap(newEvent);
            c1 -= 7;
        }
        // Days after selected date
        while (c2 <= totalDays) {
            count2++;
            EventDetails newEvent = e.copyEvent(e.getDate().plusDays(7 * count2));
            Calendar.addMap(newEvent);
            c2 += 7;
        }
        repaint();
    }
    private void addHorizontalEvents(EventDetails e) {
        DateTimeFormatter day = DateTimeFormatter.ofPattern("d");
        DateTimeFormatter dayString = DateTimeFormatter.ofPattern("EEEE");
        int totalDays = e.getDate().lengthOfMonth();
        int dayOfMonth = Integer.parseInt(day.format(e.getDate()));
        String currDay = dayString.format(e.getDate());

        int place = 0;
        for (String s : arrDays) {
            if (s.equals(currDay)) {
                break;
            }
            place++;
        }

        int count = 0;
        while (count < 7) {
            if (dayOfMonth - place + count >= 1 && dayOfMonth + count - place <= totalDays) {
                if (count - place != 0) {
                    EventDetails newEvent = e.copyEvent(e.getDate().plusDays(count - place));
                    Calendar.addMap(newEvent);
                }
            }
            count++;
        }
        repaint();
    }

    private double round(double v) {
        return Math.round(v * Math.pow(10, 2)) / Math.pow(10, 2);
    }
}

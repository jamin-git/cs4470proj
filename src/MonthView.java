import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;

public class MonthView extends JComponent {
    private LocalDate date = LocalDate.now();
    private int xSize = 1065;
    private int ySize = 800;
    private Color gray = new Color(209,209,209);

    private int rowCount = 6;
    private int columnCount = 7;
    private ArrayList<Rectangle> rectList = new ArrayList<>();
    private HashMap<LocalDate, ArrayList<EventDetails>> monthMap = new HashMap<>();
    private String[] arrDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    private Font franklinGothic = new Font("Franklin Gothic", Font.BOLD, 14);
    private Font sfranklinGothic = new Font("Franklin Gothic", Font.BOLD, 10);

    private MonthView.Handlerclass handler = new MonthView.Handlerclass();

    // Variables for Layout
    private int initGrayBoxes = 0;
    private int countDays = 0;

    public MonthView() {
        date = LocalDate.now();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Resetting the list of Rectangles to resize them, also resetting monthMap
        rectList.clear();
        //monthMap.clear();

        // Updating Window Size
        xSize = Calendar.getScrollPaneWidth();
        ySize = Calendar.getScrollPaneHeight();

        // System.out.println("Width: " + xSize);
        // System.out.println("Height: " + ySize);


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
        String dayString = day.format(start);
        boolean monthStart = false;
        countDays = 0;
        initGrayBoxes = 0;
        g.setColor(gray);

        // Filling Inactive Cells and Numbering
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                g.setColor(new Color(255, 255, 255, 130));
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
                    if (countDays > date.lengthOfMonth()) {
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
        HashMap<LocalDate, ArrayList<EventDetails>> map = Calendar.getEventDetails();
        DateTimeFormatter month = DateTimeFormatter.ofPattern("MMMM");
        String monthString = month.format(date);

        // Creating Temporary Map to hold all Months Values
//        for (LocalDate temp : map.keySet()) {
//            if (month.format(temp).equals(monthString)) {
//                //monthMap.put(temp, map.get(temp));
//            }
//        }

        DateTimeFormatter dayInt = DateTimeFormatter.ofPattern("d");
        int eventHeight = 15;
        int topDist = 15;
        g.setFont(sfranklinGothic);
        fm = g.getFontMetrics();

        for (LocalDate temp : map.keySet()) {
            if (month.format(temp).equals(monthString)) {
                ArrayList<EventDetails> list = map.get(temp);
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
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(xSize, ySize);
    }

    private class Handlerclass extends MouseInputAdapter {
        EventDetails newEvent = new EventDetails("New Event", date, 0, 0, 0, 0, new ArrayList<String>(), 1);
        boolean mouseDragged = false;
        int count = 0;
        HashMap<LocalDate, ArrayList<EventDetails>> map = Calendar.getEventDetails();
        ArrayList<EventDetails> list = monthMap.get(date);
        DateTimeFormatter month = DateTimeFormatter.ofPattern("MMMM");
        String monthString = month.format(date);

        boolean inEvent = false;
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            boolean isEvent = false;
            if (map != null || map.isEmpty()) {
                for (LocalDate temp : map.keySet()) {
                    if (month.format(temp).equals(monthString)) {
                        ArrayList<EventDetails> list = monthMap.get(temp);
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
        }
        public void mouseReleased(MouseEvent e) {
            mouseDragged = false;
            inEvent = false;
            count = 0;
        }
        public void mouseDragged(MouseEvent e) {
            int currentDragX = e.getX();
            int currentDragY = e.getY();

            if (monthMap != null || monthMap.isEmpty()) {
                for (LocalDate temp : monthMap.keySet()) {
                    list = monthMap.get(temp);
                    if (list != null && !inEvent) {
                        for (EventDetails event : list) {
                            if (event.getBoundingRectangle().contains(currentDragX, currentDragY)) {
                                inEvent = true;
                                break;
                            }
                            count++;
                        }
                        if (inEvent) {
                            break;
                        }
                    }
                }
                if (inEvent) {
                    for (Rectangle rect : rectList) {
                        if (rect.contains(currentDragX, currentDragY)) {
                            int t = rectList.indexOf(rect);
                            if (t > initGrayBoxes - 1 && t < countDays + initGrayBoxes) {
                                list.get(count).setDate(LocalDate.of(date.getYear(), date.getMonth(),t - initGrayBoxes + 1));
                            }
                            repaint();
                        }
                    }
                }
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
}

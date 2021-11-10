import dollar.DollarRecognizer;
import dollar.Result;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;

public class DayView extends JComponent {

    private LocalDate date;
    private int xSize = 800;
    private int ySize = 1250;
    private ArrayList<Rectangle> rectList = new ArrayList<>();
    private int yLine = -1;
    private Handlerclass handler = new Handlerclass();

    private Color gray = new Color(209,209,209);

    // Gesture Variables
    private ArrayList<Point2D> strokes = null;
    private DollarRecognizer dr = new DollarRecognizer();
    private EventDetails strokeEvent = null;


    private Font franklinGothic = new Font("Franklin Gothic", Font.BOLD, 14);
    private Font sfranklinGothic = new Font("Franklin Gothic", Font.BOLD, 10);


    // Map Variables
    private HashMap<LocalDate, ArrayList<EventDetails>> map = Calendar.getEventDetails();
    private ArrayList<EventDetails> list = map.get(date);

    public DayView() {
        setDate(LocalDate.now());
    }

    // Painting the Application
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

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
        String dateString = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date);
        int x = (xSize / 2) - fm.stringWidth(dateString) / 2;
        int y = fm.getHeight();
        g.drawString(dateString, x, y + 5);

        // Creating Rectangles
        g.setColor(Color.BLACK);
        g.setFont(sfranklinGothic);
        FontMetrics sfm = g.getFontMetrics();
        int count = 0;
        int ypos = 50;
        int i = 44;
        while (count < 24) {
            g.drawString(count + ":00", 5, ypos + sfm.getAscent() / 2);
            if (rectList.size() < 97) {
                rectList.add(new Rectangle(35, ypos + 0 * i / 4, xSize - 70, i / 4));
                rectList.add(new Rectangle(35, ypos + 1 * i / 4, xSize - 70, i / 4));
                rectList.add(new Rectangle(35, ypos + 2 * i / 4, xSize - 70, i / 4));
                rectList.add(new Rectangle(35, ypos + 3 * i / 4, xSize - 70, i / 4));
            }
            g.drawRect(35, ypos, xSize - 70, i);
            count++;
            ypos += i;
        }

        // Painting Events
        list = map.get(date);
        if (list != null) {
            for (EventDetails e : list) {

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

                int numStart = e.getStart();

                // Temp is the initial offset of the time based on 15 min increments
                int temp = numStart % 100;
                temp = (temp / 15) * 11;

                // Gets the Total Distance Between the two times
                int height = e.getTime() * 11;

                // First and Third inputs are the horizontal padding, 2nd and 4th get the starting pos and the ending pos
                int yEventPosition = (numStart / 100) * i + 50 + temp;
                Rectangle rect = new Rectangle(40, yEventPosition, xSize - 80, height);
                e.setBoundingRectangle(rect);
                g.fillRoundRect(40, yEventPosition, xSize - 80, height, 25, 25);
                g.setColor(Color.BLACK);
                g.drawString(e.getName(), 50, yEventPosition + sfm.getAscent());
            }
        }

        // Painting Time-Of-Day Line
        g.setColor(Color.RED);
        g.drawLine(35, yLine, xSize - 35, yLine);

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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(xSize, ySize);
    }

    private class Handlerclass extends MouseInputAdapter {

        // Placeholder EventDetails object
        EventDetails newEvent = new EventDetails("New Event", date, 0, 1, 0, 0, new ArrayList<String>(), 1);

        //
        boolean singleEvent = false;

        // Used to set the time of a dragged event
        boolean start = false;

        // This is used to detect if an the mouse click or drag is on an event
        boolean isEvent = false;

        // This is used to detect if a mouseDrag has started within the mouseDragged method
        boolean mouseDragged = false;

        // Used to keep track of events when dragging already made events
        int count = -1;

        // Left and Right Mouse click variables
        boolean left = false;
        boolean right = false;


        // Methods / Code for Time-Of-Day Line
        public void mouseMoved(MouseEvent e) {
            yLine = e.getY();
            repaint();
        }
        public void mouseExited(MouseEvent e) {
            yLine = -1;
            repaint();
        }

        // Methods for the Double Click Functionality
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (e.getButton() == MouseEvent.BUTTON1) {
                left = true;
                right = false;
                boolean isEvent = false;
                ArrayList<EventDetails> list = Calendar.getEventDetails().get(date);
                if (list != null) {
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
                }
                if (!isEvent) {
                    for (Rectangle rect : rectList) {
                        if (e.getClickCount() == 2 && !e.isConsumed() && rect.contains(x, y)) {
                            int t = rectList.indexOf(rect);
                            newEvent = new EventDetails("New Event", date, 0, 0, t, t + 4, new ArrayList<String>(), 1);
                            if (t == 92) {
                                newEvent.setEndIndex(t + 3);
                            } else if (t == 93) {
                                newEvent.setEndIndex(t + 2);
                            } else if (t == 94) {
                                newEvent.setEndIndex(t + 1);
                            }
                            Calendar.appointmentFilled(newEvent, true);
                            e.consume();
                            repaint();
                            break;
                        }
                    }
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                right = true;
                left = false;
                strokes = new ArrayList<>();
            }
        }

        // This method is utilized to reset mouseDrag events when they are over
        public void mouseReleased(MouseEvent e) {
            singleEvent = false;
            start = false;
            isEvent = false;
            mouseDragged = false;
            count = -1;
            newEvent = new EventDetails("New Event", date, 0, 0, 0, 0, new ArrayList<String>(), 1);
            list = map.get(date);

            // Functionality for Strokes
            if (strokes != null && strokes.size() > 0) {
                Result r = dr.recognize(strokes);
                if (list != null) {
                    for (EventDetails event : list) {
                        if (event.getBoundingRectangle().contains(strokes.get(0))) {
                            strokeEvent = event;
                        }
                    }
                }

                if (r.getName().equals("delete")) {
                    if (strokeEvent != null) {
                        Calendar.removeMap(date ,strokeEvent);
                        Calendar.setStatusBar("Event deleted via delete gesture with an accuracy score of: " + round(r.getScore()));
                    }
                }

                else if (r.getName().equals("right square bracket")) {
                    Calendar.nextDay();
                    Calendar.setStatusBar("Moved to next day via right bracket gesture with an accuracy score of: " + round(r.getScore()));
                }

                else if (r.getName().equals("left square bracket")) {
                    Calendar.prevDay();
                    Calendar.setStatusBar("Moved to prev day via left bracket gesture with an accuracy score of: " + round(r.getScore()));
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
                        strokeEvent.toggleTag("Family");
                        Calendar.setStatusBar("Toggled Family Tag via pigtail gesture with an accuracy score of: " + round(r.getScore()));
                    }
                }
                // Checks for Other tag
                else if (r.getName().equals("triangle")) {
                    if (strokeEvent != null) {
                        strokeEvent.toggleTag("Other");
                        Calendar.setStatusBar("Toggled Other Tag via triangle gesture with an accuracy score of: " + round(r.getScore()));
                    }
                }

                else if (r.getName().equals("caret")) {
                    if (strokeEvent != null) {
                        int end = strokeEvent.getEndIndex();
                        int start = strokeEvent.getStartIndex();
                        if (start - 4 >= 0) {
                            Calendar.updateEventStart(strokeEvent, start - 4);
                            Calendar.updateEventEnd(strokeEvent, end - 4);
                        } else {
                            int temp = 0 - start;
                            Calendar.updateEventStart(strokeEvent, start + temp);
                            Calendar.updateEventEnd(strokeEvent, end + temp);
                        }
                        Calendar.setStatusBar("Moved time forward 1 hour via caret gesture with an accuracy score of: " + round(r.getScore()));
                    }
                }

                else if (r.getName().equals("v")) {
                    if (strokeEvent != null) {
                        int end = strokeEvent.getEndIndex();
                        int start = strokeEvent.getStartIndex();
                        if (end + 4 < 96) {
                            Calendar.updateEventStart(strokeEvent, start + 4);
                            Calendar.updateEventEnd(strokeEvent, end + 4);
                        } else {
                            int temp = 95 - end;
                            Calendar.updateEventStart(strokeEvent, start + temp);
                            Calendar.updateEventEnd(strokeEvent, end + temp);
                        }
                        Calendar.setStatusBar("Moved time backward 1 hour via v gesture with an accuracy score of: " + round(r.getScore()));
                    }
                }
                else {
                    Calendar.setStatusBar("Gesture did not match");
                }
            }

            left = false;
            right = false;
            strokes = null;
            strokeEvent = null;
            repaint();
        }

        // Dragging functionality
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (left) {
                // Code for Dragging Already Created Event
                ArrayList<EventDetails> list = Calendar.getEventDetails().get(date);
                if (list != null && !singleEvent) {
                    if (!mouseDragged) {
                        mouseDragged = true;
                        for (EventDetails event : list) {
                            count++;
                            if (event.getBoundingRectangle().contains(x, y)) {
                                isEvent = true;
                                break;
                            }
                        }
                    }
                    if (isEvent) {
                        for (Rectangle rect : rectList) {
                            if (rect.contains(x, y)) {
                                int t = rectList.indexOf(rect);
                                Calendar.updatePrevEvent(list.get(count), t);
                            }
                            repaint();
                        }
                    }
                }

                // Code for Drag Creating a new event
                if (!isEvent) {
                    if (!singleEvent) {
                        Calendar.addMap(newEvent);
                        singleEvent = true;
                    }
                    if (Calendar.getEventDetails().get(date) != null) {
                        for (Rectangle rect : rectList) {
                            if (rect.contains(x, y)) {
                                int t = rectList.indexOf(rect);
                                ArrayList<EventDetails> l = Calendar.getEventDetails().get(date);
                                if (!start) {
                                    Calendar.updateEventStart(l.get(l.indexOf(newEvent)), t);
                                    start = true;
                                } else if (t > l.get(l.indexOf(newEvent)).getStartIndex()) {
                                    Calendar.updateEventEnd(l.get(l.indexOf(newEvent)), t);
                                } else if (t <= l.get(l.indexOf(newEvent)).getStartIndex()) {
                                    int index = l.indexOf(newEvent);
                                    if (l.get(index).getStartIndex() < 95) {
                                        Calendar.updateEventEnd(l.get(index), l.get(index).getStartIndex() + 1);
                                    } else {
                                        Calendar.updateEventEnd(l.get(index), l.get(index).getStartIndex());
                                    }
                                }
                                repaint();
                            }
                        }
                    }
                }
            } else if (right) {
                strokes.add(new Point(x, y));
                repaint();
            }
        }
    }


    // Updates the mouseListeners when a new date is needed
    private void updateListener() {
        handler = new Handlerclass();
    }
    // Getters / Setters
    public LocalDate getDate() {
        return date;
    }
    // Setting the date and updating the mouseListeners
    public void setDate(LocalDate n) {
        date = n;
        removeMouseListener(handler);
        removeMouseMotionListener(handler);
        updateListener();
        addMouseMotionListener(handler);
        addMouseListener(handler);
    }
    public void setSizex(int x) {
        xSize = x;
    }
    public void setSizey(int y) {
        ySize = y;
    }

    private double round(double v) {
        return Math.round(v * Math.pow(10, 2)) / Math.pow(10, 2);
    }
}

import jdk.jfr.Event;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DayView extends JComponent {

    private LocalDate date;
    int xSize = 800;
    int ySize = 1250;
    private ArrayList<Rectangle> rectList = new ArrayList<>();


    private Color gray = new Color(209,209,209);

    private Font franklinGothic = new Font("Franklin Gothic", Font.BOLD, 14);
    private Font sfranklinGothic = new Font("Franklin Gothic", Font.BOLD, 10);

    public DayView() {
        date = LocalDate.now();
        Handlerclass handler = new Handlerclass();
        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);



        //addDoubleClick();
    }
    public DayView(LocalDate date) {
        this.date = date;
        addDoubleClick();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Creating Rectangle
        g.setColor(gray);
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
            if (rectList.size() < 25) {
                rectList.add(new Rectangle(35, ypos, xSize - 70, i));
            }
            g.drawRect(35, ypos, xSize - 70, i);
            count++;
            ypos += i;
        }

        // Painting Events
        HashMap<LocalDate, ArrayList<EventDetails>> map = Calendar.getEventDetails();
        ArrayList<EventDetails> list = map.get(date);
        if (list != null) {
            for (EventDetails e : list) {

                // Setting Color of Event Box
                if (e.getTags().contains("Other")) {
                    g.setColor(Color.CYAN);
                } else if (e.getTags().contains("Family")) {
                    g.setColor(Color.GRAY);
                } else if (e.getTags().contains("School")) {
                    g.setColor(Color.MAGENTA);
                } else if (e.getTags().contains("Work")) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(Color.RED);
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
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(xSize, ySize);
    }

    private class Handlerclass extends MouseInputAdapter {

        EventDetails newEvent = new EventDetails("New Event", date, 0, 0, 0, 0, new ArrayList<String>(), 0);
        boolean temp = false;
        boolean start = false;
        boolean inEvent = false;

        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            boolean isEvent = false;
            ArrayList<EventDetails> list = Calendar.getEventDetails().get(date);
            if (list != null) {
                for (EventDetails event : list) {
                    if (e.getClickCount() == 2 && !e.isConsumed() && event.getBoundingRectangle().contains(x, y)) {
                        Calendar.appointmentFilled(event);
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
                        int t = rectList.indexOf(rect) * 4;
                        newEvent = new EventDetails("New Event", date, 0, 0, t, t + 4, new ArrayList<String>(), 0);
                        if (t == 92) {
                            newEvent.setEndIndex(t + 3);
                        }
                        Calendar.appointmentFilled(newEvent);
                        e.consume();
                        repaint();
                        break;
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            temp = false;
            start = false;
            inEvent = false;
            newEvent = new EventDetails("New Event", date, 0, 0, 0, 0, new ArrayList<String>(), 0);
        }

        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            boolean isEvent = false;


            ArrayList<EventDetails> list = Calendar.getEventDetails().get(date);
            if (list != null) {
                for (EventDetails event : list) {
                    if (event.getBoundingRectangle().contains(x, y)) {
                        for (Rectangle rect : rectList) {
                            if (rect.contains(x, y)) {
                                int t = rectList.indexOf(rect) * 4;
                                Calendar.updatePrevEvent(event, t);
                                repaint();
                            }
                        }
                        isEvent = true;
                    }
                }
            }

            if (!isEvent) {
                if (!temp) {
                    Calendar.addMap(newEvent);
                    temp = true;
                }
                if (Calendar.getEventDetails().get(date) != null) {
                    for (Rectangle rect : rectList) {
                        if (rect.contains(x, y)) {
                            int t = rectList.indexOf(rect) * 4;
                            ArrayList<EventDetails> l = Calendar.getEventDetails().get(date);
                            if (!start) {
                                Calendar.updateEventStart(l.get(l.indexOf(newEvent)), t);
                                start = true;
                            } else if (t > l.get(l.indexOf(newEvent)).getStartIndex()) {
                                Calendar.updateEventEnd(l.get(l.indexOf(newEvent)), t);
                            }
                            repaint();
                        }
                    }
                }
            }
        }
    }
    private void addDoubleClick() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                boolean isEvent = false;
                ArrayList<EventDetails> list = Calendar.getEventDetails().get(date);
                if (list != null) {
                    for (EventDetails event : list) {
                        if (e.getClickCount() == 2 && !e.isConsumed() && event.getBoundingRectangle().contains(x, y)) {
                            Calendar.appointmentFilled(event);
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
                            int t = rectList.indexOf(rect) * 4;
                            EventDetails newEvent = new EventDetails("New Event", date, 0, 0, t, t + 4, new ArrayList<String>(), 0);
                            if (t == 92) {
                                newEvent.setEndIndex(t + 3);
                            }
                            Calendar.appointmentFilled(newEvent);
                            e.consume();
                            repaint();
                            break;
                        }
                    }
                }
            }
        });
    }

    // Getters / Setters
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate n) {
        date = n;
    }
    public void setSizex(int x) {
        xSize = x;
    }
    public void setSizey(int y) {
        xSize = y;
    }


    public static void main(String [] args) {
        DayView go = new DayView();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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


    private Color gray = new Color(209,209,209);

    private Font franklinGothic = new Font("Franklin Gothic", Font.BOLD, 14);
    private Font sfranklinGothic = new Font("Franklin Gothic", Font.BOLD, 10);

    public DayView() {
        date = LocalDate.now();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                ArrayList<EventDetails> list = Calendar.getEventDetails().get(date);
                if (list != null) {
                    for (EventDetails event : list) {
                        if (e.getClickCount() == 2 && !e.isConsumed() && event.getBoundingRectangle().contains(x, y)) {
                            System.out.println("Its Working!");
                            Calendar.appointmentFilled(event);
                            e.consume();
                        }
                    }
                }
            }
        });
    }
    public DayView(LocalDate date) {
        this.date = date;
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


        /*
        // Creating Equally Spaced Lines
        g.setColor(Color.BLACK);
        g.setFont(sfranklinGothic);
        FontMetrics sfm = g.getFontMetrics();
        int c = y + 50;
        int count = 0;
        while (count < 24) {
            g.drawString(count + ": 00", 5, c + sfm.getAscent() / 2);
            g.drawLine(50, c, xSize - 30, c);
            c = c + 50;
            count++;
        } */
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(xSize, ySize);
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

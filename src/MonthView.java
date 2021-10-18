import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class MonthView extends JComponent {
    private LocalDate date;
    int xSize = 1065;
    int ySize = 800;
    private Color gray = new Color(209,209,209);


    public MonthView() {
        date = LocalDate.now();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Updating Window Size
        xSize = Calendar.getScrollPaneWidth();
        ySize = Calendar.getScrollPaneHeight();
        System.out.println("Width: " + xSize);
        System.out.println("Height: " + ySize);


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
        FontMetrics fm = g.getFontMetrics();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM, YYYY");
        String dateString = date.format(monthFormatter);
        int x = (xSize / 2) - fm.stringWidth(dateString) / 2;
        int y = fm.getHeight();
        g.drawString(dateString, x, y + 5);


        // Painting 6x7 Grid with resizability
        int width = xSize;
        int height = ySize;
        int viewWidth = xSize - 200;
        int viewHeight = ySize - 200;
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 8; j++) {
                g.drawRect(j * viewWidth / 7,i * viewHeight / 6, viewWidth / 7, viewHeight / 6);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(xSize, ySize);
    }

    public void setDate(LocalDate n) {
        date = n;
    }
}

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class MonthView extends JComponent {
    private LocalDate date;
    int xSize = 800;
    int ySize = 1250;
    private Color gray = new Color(209,209,209);


    public MonthView() {
        date = LocalDate.now();
    }

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
        FontMetrics fm = g.getFontMetrics();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM, YYYY");
        String dateString = date.format(monthFormatter);
        int x = (xSize / 2) - fm.stringWidth(dateString) / 2;
        int y = fm.getHeight();
        g.drawString(dateString, x, y + 5);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(xSize, ySize);
    }

    public void setDate(LocalDate n) {
        date = n;
    }
}

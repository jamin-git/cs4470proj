import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class MonthView extends JComponent {
    private LocalDate date;
    private int xSize = 1065;
    private int ySize = 800;
    private Color gray = new Color(209,209,209);

    private int rowCount = 6;
    private int columnCount = 7;
    private ArrayList<Rectangle> rectList = new ArrayList<>();
    private String[] arrDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};


    public MonthView() {
        date = LocalDate.now();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

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
        int count = 0;

        // Drawing Grid + Labels
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                rectList.add(new Rectangle(xOffset + (col * rectWidth), yOffset + (row * rectHeight),
                        rectWidth, rectHeight));
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
        LocalDate start = date.withDayOfMonth(1);
        DateTimeFormatter month = DateTimeFormatter.ofPattern("EEEE");
        String dayString = month.format(start);
        boolean monthStart = false;
        int countDays = 0;
        g.setColor(gray);

        // Filling Inactive Cells and Numbering
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                g.setColor(gray);
                if (!monthStart) {
                    if (arrDays[col].equals(dayString)) {
                        col--;
                        monthStart = true;
                    } else {
                        g.fillRect(xOffset + (col * rectWidth) + 5, yOffset + (row * rectHeight) + 5,
                                rectWidth - 10, rectHeight - 10);
                    }
                } else {
                    countDays++;
                    if (countDays > date.lengthOfMonth()) {
                        g.fillRect(xOffset + (col * rectWidth) + 5, yOffset + (row * rectHeight) + 5,
                                rectWidth - 10, rectHeight - 10);
                    } else {
                        g.setColor(Color.black);
                        g.drawString(String.valueOf(countDays), xOffset + (col * rectWidth) + 3 , yOffset + (row * rectHeight) + rectHeight - 3);
                    }
                }
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

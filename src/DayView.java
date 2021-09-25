import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DayView extends JComponent {

    private LocalDate date;
    int xSize = 600;
    int ySize = 600;
    int wxSize = 800;
    int wySize = 800;
    JFrame t = new JFrame();

    private Color gray = new Color(209,209,209);

    private Font franklinGothic = new Font("Franklin Gothic", Font.BOLD, 14);
    private Font sfranklinGothic = new Font("Franklin Gothic", Font.BOLD, 10);

    public DayView() {
        date = LocalDate.now();
        //initFrame();
    }
    public DayView(LocalDate d, int x, int y) {
        date = d;
        xSize = x;
        ySize = y;
    }
    private void initFrame() {
        t.setVisible(true);
        t.setSize(wxSize, wySize);
        t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        t.add(this);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Creating Rectangle
        g.setColor(gray);
        g.drawRect(0,0, xSize, ySize);
        g.fillRect(0,0, xSize, ySize);

        // Creating Date String
        g.setColor(Color.blue);
        g.setFont(franklinGothic);
        FontMetrics fm = g.getFontMetrics();
        int x = (xSize / 2) - fm.stringWidth(date.toString()) / 2;
        int y = fm.getHeight();
        g.drawString(date.toString(), x, y);

        // Creating Equally Spaced Lines
        g.setColor(Color.BLACK);
        g.setFont(sfranklinGothic);
        FontMetrics sfm = g.getFontMetrics();
        int c = 2 * y;
        int count = 0;
        while (count < 25) {
            g.drawString(count + ": 00", 5, c + sfm.getAscent() / 2);
            g.drawLine(50, c, ySize - 30, c);
            c = c + 50;
            count++;
        }
    }


    // Getters / Setters
    private LocalDate getDate() {
        return date;
    }
    private void setDate(LocalDate n) {
        date = n;
    }
    private void setSizex(int x) {
        xSize = x;
    }
    private void setSizey(int y) {
        xSize = y;
    }


    public static void main(String [] args) {
        DayView go = new DayView();
    }
}

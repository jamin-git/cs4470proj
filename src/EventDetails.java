import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EventDetails {
    private String name;
    private LocalDate date;
    private int start;
    private int end;
    private ArrayList<String> tags;
    private int time;
    private Rectangle boundingRectangle;

    public EventDetails(String name, LocalDate date, int start, int end, ArrayList<String> tags, int time) {
        this.name = name;
        this.date = date;
        this.start = start;
        this.end = end;
        this.tags = tags;
        this.time = time;
    }



    public String toString() {
        return "Name: " +  name + ", Date: " + date + ", From " + start
                + " to " + end + ", Tags: " + tags;
    }

    public int getEnd() {
        return end;
    }
    public void setEnd(int end) {
        this.end = end;
    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<String> getTags() {
        return tags;
    }
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }

    public void setBoundingRectangle(Rectangle rect) {
        boundingRectangle = rect;
    }
    public Rectangle getBoundingRectangle() {
        return boundingRectangle;
    }


    public static void main(String[] args) {

    }
}

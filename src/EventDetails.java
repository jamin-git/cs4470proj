import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EventDetails implements Comparable {
    private String name;
    private LocalDate date;
    private int start;
    private int end;
    private int startIndex;
    private int endIndex;
    private ArrayList<String> tags;
    private int time;
    private Rectangle boundingRectangle;

    public EventDetails(String name, LocalDate date, int start, int end, int startIndex, int endIndex, ArrayList<String> tags, int time) {
        this.name = name;
        this.date = date;
        this.start = start;
        this.end = end;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.tags = tags;
        this.time = time;
    }
    @Override
    public int compareTo(Object event) {
        int compareTime = ((EventDetails) event).startIndex;
        return this.startIndex - compareTime;
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
    public void toggleTag(String s) {
        if (tags.contains(s)) {
            tags.remove(s);
        } else {
            tags.add(s);
        }
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

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}

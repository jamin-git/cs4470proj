import java.time.LocalDate;

public class EventDetails {
    private String name;
    private LocalDate date;
    private int start;
    private int end;

    public EventDetails(String name, LocalDate date, int start, int end) {
        this.name = name;
        this.date = date;
        this.start = start;
        this.end = end;
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
}

package domain;

public class Homework implements HasID<Integer>{
    private int id;
    private String description;
    private int deadline;
    private int received;

    public Homework(int id, String description, int deadline, int received) {
        this.id = id;
        this.description = description;
        this.deadline = deadline;
        this.received = received;
    }
    public Homework(int id, String description, int deadline) {
        this.id = id;
        this.description = description;
        this.deadline = deadline;
        this.received = 0;
    }

    @Override
    public String toString() {
        return  id +
                "`" + description+
                "`" + deadline +
                "`" + received;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void setID(Integer integer) {
        id=integer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }
}


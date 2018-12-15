package domain;

public class GradeDTO {
    private String name;
    private String homework;
    private float value;
    private int date;

    public GradeDTO(String name, String homework, float value, int date) {
        this.name = name;
        this.homework = homework;
        this.value = value;
        this.date = date;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

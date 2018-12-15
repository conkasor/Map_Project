package domain;

import domain.Validator.GradeHomeworkException;

public class Grade implements  HasID<Integer>{
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getWeekGraded() {
        return weekGraded;
    }

    public void setWeekGraded(int weekGraded) {
        this.weekGraded = weekGraded;
    }

    public Grade(int id, Student student, Homework homework) {
        this.student = student;
        this.homework = homework;
        this.id = id;

    }

    public Grade(int id, Student student, Homework homework, float value, int weekGraded) {
        this.student = student;
        this.homework = homework;
        this.value = value;
        this.id = id;
        this.weekGraded = weekGraded;
    }

    @Override
    public String toString() {
        return  id  +
                "`" + student.getID() +
                "`" + homework.getID() +
                "`" + value +
                "`" + weekGraded+
                "`" + feedback;
    }

    private Student student;
    private Homework homework;
    private float value;
    private int id;
    private int weekGraded;

    public Grade(Student student, Homework homework, float value, int id, int weekGraded, String feedback) {
        this.student = student;
        this.homework = homework;
        this.value = value;
        this.id = id;
        this.weekGraded = weekGraded;
        this.feedback = feedback;
    }
    public void calculate() throws GradeHomeworkException {
        int deadline =homework.getDeadline();
        float minus = 0;
        if (deadline < weekGraded - 2)
            throw new GradeHomeworkException("Homework can't be received");
        if (deadline - weekGraded < 0)
            minus = (float)2.5 * (weekGraded - deadline);
        this.value=value-minus;
    }

    private String feedback="";

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void setID(Integer o) {
        this.id=o;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
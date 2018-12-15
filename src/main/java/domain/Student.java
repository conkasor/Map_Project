package domain;

public class
Student implements HasID<Integer>{
    private int id;
    private String name;
    private int group;
    private String email;
    private String labTeacher;

    public Student(int id, String name, int group, String email, String labTeacher) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.email = email;
        this.labTeacher = labTeacher;

    }




    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLabTeacher(String labTeacher) {
        this.labTeacher = labTeacher;
    }

    public String getName() {
        return name;
    }

    public int getGroup() {
        return group;
    }

    public String getEmail() {
        return email;
    }

    public String getLabTeacher() {
        return labTeacher;
    }

    @Override
public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(Integer integer) {
        this.id=integer;
    }

    @Override
    public String toString() {
        return id +
                "`" + name +
                "`" + group +
                "`" + email +
                "`" + labTeacher;
    }
}

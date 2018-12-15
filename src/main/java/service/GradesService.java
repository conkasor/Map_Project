package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import domain.Validator.GradeHomeworkException;
import domain.Validator.ValidationException;
import repository.CrudRepository;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class GradesService extends CrudServiceIdInteger<Grade> {
    public GradesService(CrudRepository<Integer, Grade> repo, CrudServiceIdInteger<Student> ctrlS, CrudServiceIdInteger<Homework> ctrlH) {
        super(repo);
        this.ctrlS = ctrlS;
        this.ctrlH = ctrlH;
    }

    private CrudServiceIdInteger<Student> ctrlS;
    private CrudServiceIdInteger<Homework> ctrlH;

    @Override
    protected Grade ParseEntityFromString(String string) {
        String[] args = string.split("`");
        Student S;
        Homework H;
        int id = Integer.parseInt(args[0]);
        int idS = Integer.parseInt(args[1]);
        int idH = Integer.parseInt(args[2]);
        S = (Student) ctrlS.findOne(idS);
        H = (Homework) ctrlH.findOne(idH);

        if (S == null || H == null) return null;
        if (args.length <= 3)
            return new Grade(id, S, H);

        float value = Float.parseFloat(args[3]);
        int week = Integer.parseInt(args[4]);
        try {
            String feedback =args[5];
            return new Grade(S,H,value,id,week,feedback);
        }  catch (ArrayIndexOutOfBoundsException e){}
        return new Grade(id, S, H, value, week);

    }
    public void updateEntity(Grade gr)throws ValidationException{
        try {
            repo.update(gr);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }
    public void GradeHomework(int Gid, float value,int week, String feedback) throws GradeHomeworkException, IOException {
        Grade g = (Grade) findOne(Gid);
        if (g==null)
            throw  new GradeHomeworkException("Grade is inexistent");
        int deadline = g.getHomework().getDeadline();
        double minus = 0;
        if (deadline < week - 2)
            throw new GradeHomeworkException("Homework can't be received");
        if (deadline - week < 0)
            minus = 2.5 * (week - deadline);
        if (g.getValue() != 0)
            throw new GradeHomeworkException("Homework already graded for this student");
        g.setValue(value - (float) minus);
        g.setWeekGraded(week);
        try {
            super.update(g.toString());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        writeGradesOfStudent(g, feedback);
    }

    public void writeGradesOfStudent(Grade g, String feedback) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(g.getStudent().getName() + ".txt", true));
        bw.write("Tema: " + g.getHomework().getID());
        bw.newLine();
        bw.write("Nota" + g.getValue());
        bw.newLine();
        bw.write("Predata in saptamana: " + g.getWeekGraded());
        bw.newLine();
        bw.write("Deadline: " + g.getHomework().getDeadline());
        bw.newLine();
        bw.write("Feedbkack: " + feedback);
        bw.newLine();
        bw.newLine();
        bw.close();
    }
}

//package repository;
//
//import domain.Grade;
//import domain.Homework;
//import domain.Student;
//import domain.Validator.GradeValidator;
//import domain.Validator.HomeworkValidator;
//import domain.Validator.StudentValidator;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.Test;
//
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//class GradesRepoInFileTest2 {
//    StudentsRepoInFile repoSt=new StudentsRepoInFile(new StudentValidator(),"repoStudentsT.txt");
//    HomeworkRepoInFile repoHo=new HomeworkRepoInFile(new HomeworkValidator(),"repoHomeworkT.txt");
//    GradesRepoInFile repoGr=new GradesRepoInFile(new GradeValidator(),"repoGradesT.txt",repoSt,repoHo);
//    @AfterAll
//    static void tearDown() {
//        PrintWriter writer = null;
//        try {
//            writer = new PrintWriter("repoStudentsT.txt");
//            writer.print("");
//            writer.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            writer = new PrintWriter("repoHomeworkT.txt");
//            writer.print("");
//            writer.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            writer = new PrintWriter("repoGradesT.txt");
//            writer.print("");
//            writer.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    void update() {
//        Student st=new Student(1,"Bob",222,"bob@gmail.com","Camelia");
//        Homework h=new Homework(1,"lab1",5);
//        Homework h2=new Homework(2,"lab1",5);
//        repoSt.save(st);
//        repoHo.save(h);
//        repoHo.save(h2);
//        Grade gr=new Grade(st,h,1,1,4);
//        Grade gr2=new Grade(st,h2,1,2,5);
//        repoGr.save(gr);
//        repoGr.update(gr2);
//        assertEquals(h2,repoGr.findOne(1).getHomework());
//
//    }
//
//    @Test
//    void delete() {
//        Student st=new Student(1,"Bob",222,"bob@gmail.com","Camelia");
//        Homework h=new Homework(1,"lab1",5);
//        repoSt.save(st);
//        repoHo.save(h);
//        Grade gr=new Grade(st,h,1,1,5);
//        repoGr.save(gr);
//        repoGr.delete(gr.getID());
//        assertNull(repoGr.findOne(1));
//    }
//
//    @Test
//    void save() {
//        Student st=new Student(1,"Bob",222,"bob@gmail.com","Camelia");
//        Homework h=new Homework(1,"lab1",5);
//        repoSt.save(st);
//        repoHo.save(h);
//        Grade gr=new Grade(st,h,1,1,5);
//        repoGr.save(gr);
//        assertEquals(gr,repoGr.findOne(1));
//        try{repoGr.save(null);
//            assertEquals(1,2);
//        }
//        catch (IllegalArgumentException e){
//        }
//        repoGr.delete(1);
//        repoHo.delete(1);
//        repoSt.delete(1);
//
//
//
//
//    }
//
//    @Test
//    void findOne() {
//        Student st=new Student(1,"Bob",222,"bob@gmail.com","Camelia");
//        Homework h=new Homework(1,"lab1",5);
//        repoSt.save(st);
//        repoHo.save(h);
//        Grade gr=new Grade(st,h,1,1,5);
//        repoGr.save(gr);
//        assertEquals(gr,repoGr.findOne(1));
//        Grade g=repoGr.findOne(2);
//        assertNull(g);
//
//        repoGr.delete(1);
//        repoHo.delete(1);
//        repoSt.delete(1);
//    }
//
//    @Test
//    void findAll() {
//        Student st=new Student(1,"Bob",222,"bob@gmail.com","Camelia");
//        Homework h=new Homework(1,"lab1",5);
//        Student st1=new Student(2,"Bob",222,"bob@gmail.com","Camelia");
//        Homework h1=new Homework(2,"lab1",5);
//        repoSt.save(st);
//        repoSt.save(st1);
//        repoHo.save(h);
//        repoHo.save(h1);
//        Grade gr=new Grade(st,h,1,1,5);
//        Grade gr1=new Grade(st,h1,2,2,5);
//        Grade gr2=new Grade(st1,h1,3,3,5);
//        repoGr.save(gr);
//        repoGr.save(gr2);
//        repoGr.save(gr1);
//        int nr=0;
//        for (Grade grr:repoGr.findAll())nr++;
//        assertEquals(3,nr);
//
//        repoGr.delete(1);
//        repoGr.delete(2);
//        repoGr.delete(3);
//        repoSt.delete(2);
//        repoHo.delete(2);
//        repoHo.delete(1);
//        repoSt.delete(1);
//    }
//}
//package service;
//
//import domain.Grade;
//import domain.Homework;
//import domain.Student;
//import domain.Validator.GradeValidator;
//import domain.Validator.HomeworkValidator;
//import domain.Validator.StudentValidator;
//import domain.Validator.ValidationException;
//import repository.GradesRepoInFile;
//import repository.HomeworkRepoInFile;
//import repository.StudentsRepoInFile;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//
//class ControllerTest {
//
//    private StudentsRepoInFile repoSt=new StudentsRepoInFile(new StudentValidator(),"repoStudentsT.txt");
//    private HomeworkRepoInFile repoHo=new HomeworkRepoInFile(new HomeworkValidator(),"repoHomeworkT.txt");
//    private GradesRepoInFile repoGr=new GradesRepoInFile(new GradeValidator(),"repoGradesT.txt",repoSt,repoHo);
//    private service ctrl=new service(repoSt,repoHo,repoGr);
//    @Test
//    void getCurrentWeek() {
//        assertEquals(6, ctrl.getCurrentWeek());
//    }
//
//    @Test
//    void AssignHomework() {
//        int nr=0;
//        for (Grade gr:repoGr.findAll())nr++;
//        assertEquals(1,nr);
//    }
//
//    @Test
//    void addHomework()  {
//        try {
//            ctrl.addHomework(new Homework(-1,"lab1",6));
//            assert (false);
//        } catch (ValidationException e) {
//            assert(true);
//        }
//        try {
//            ctrl.addHomework(new Homework(1,"lab1",6));
//            assert(true);
//        } catch (ValidationException e) {
//            assert(false);
//        };
//    }
//
//    @Test
//    void extendDeadline() {
//        try {
//            ctrl.addHomework(new Homework(1,"lab1",6));
//            assert(true);
//        } catch (ValidationException e) {
//            assert(false);
//        };
//        ctrl.extendDeadline(1);
//        assertEquals(5,repoHo.findOne(1).getDeadline());
//
//
//    }
//
//    @Test
//    void addStudent() {
//        ctrl.addStudent(new Student(1,"Bob",222,"bob@gmail.com","Camelia"));
//        int r=0;
//        for (Student st:repoSt.findAll())r++;
//        assertEquals(1,r);
//    }
//
//    @Test
//    void findStudent() {
//        Student st=new Student(1,"Bob",222,"bob@gmail.com","Camelia");
//        ctrl.deleteStudent(st.getID());
//        ctrl.addStudent(st);
//        assertEquals(st,ctrl.findStudent(1));
//
//    }
//
//    @Test
//    void findAllStudents() {
//        Student st=new Student(1,"Bob",222,"bob@gmail.com","Camelia");
//        Student s1=new Student(2,"Bob",222,"bob@gmail.com","Camelia");
//        ctrl.addStudent(st);
//        ctrl.addStudent(s1);
//        int r=0;
//        for (Student stt:repoSt.findAll())r++;
//        assertEquals(2,r);
//
//    }
//
//    @Test
//    void deleteStudent() {
//        Student st=new Student(1,"Bob",222,"bob@gmail.com","Camelia");
//        Student s1=new Student(2,"Bob",222,"bob@gmail.com","Camelia");
//        ctrl.addStudent(st);
//        ctrl.addStudent(s1);
//        int r=0;
//        ctrl.deleteStudent(1);
//        for (Student stt:repoSt.findAll())r++;
//        assertEquals(1,r);
//    }
//
//    @Test
//    void updateStudent() {
//        Student st=new Student(1,"Bob",222,"bob@gmail.com","Camelia");
//        Student s1=new Student(1,"Bob Bob",222,"bob@gmail.com","Camelia");
//        ctrl.addStudent(st);
//        ctrl.updateStudent(s1);
//        assertEquals(s1,ctrl.findStudent(1));
//    }
//}
//
//import domain.Student;
//import service.GradesService;
//import service.HomeworkService;
//import service.StudentsService;
//import domain.Grade;
//import domain.Homework;
//import domain.Validator.*;
//import repository.*;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Scanner;
//
//public class Ui {
//
//    private CrudRepository<Integer, Student> repoSt;
//    private CrudRepository<Integer,Homework> repoHo;
//    private CrudRepository<Integer, Grade> repoGr;
//    private StudentsService ctrlS;
//    private HomeworkService ctrlH;
//    private GradesService ctrlG;
//    private Scanner scanner;
//
//    public Ui() {
//        scanner = new Scanner( System.in );
//        repoHo = new HomeworkRepoXML(new HomeworkValidator(),"repoHomeworkXML.xml");
//        ctrlH = new HomeworkService(repoHo);
//        repoSt = new StudentsRepoXML(new StudentValidator(),"repoStudentsXML.xml");
//        repoGr = new GradesRepoXML(new GradeValidator(),"repoGradesXML.xml",repoSt,repoHo);
//        ctrlS = new StudentsService(repoSt);
//        ctrlG = new GradesService(repoGr,ctrlS,ctrlH);
//    }
//
//    public void mainMenu(){
//        printMenu();
//        while (true) {
//            printEntities();
//            String input = scanner.nextLine();
//            switch (input) {
//                case "0":
//                    return;
//                case "1":
//                    addStudent();
//                    break;
//                case "2":
//                    getStudent();
//                    break;
//                case "3":
//                    updateStudent();
//                    break;
//                case "4":
//                    removeStudent();
//                    break;
//                case "5":
//                    addHomework();
//                    break;
//                case"6":
//                    assignHomework();
//                    break;
//                case"7":
//                    ExtendDeadline();
//                    break;
//                case"8":
//                    GradeHomework();
//                    break;
//            }
//
//            printMenu();
//
//        }
//    }
//
//    private void GradeHomework() {
//        try{
//            System.out.println("Grade Id");
//            String input = scanner.nextLine();
//            int Gid = Integer.parseInt(input);
//            System.out.println("Grade value");
//            input = scanner.nextLine();
//            float value = Integer.parseInt(input);
//            System.out.println("feedback");
//            input = scanner.nextLine();
////            ctrlG.GradeHomework(Gid,value,input);
////            ctrlG.GradeHomework(Gid,10,input);
//        }
//            catch (NumberFormatException e)
//            {
//                System.out.println(e.getMessage());
//            } catch (GradeHomeworkException e) {
//            System.out.println(e.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**Print all the entities existing in the app at this moment  */
//    private void printEntities() {
//        System.out.println("Students\n");
//        ctrlS.findAll().forEach(System.out::println);
//        System.out.println("Homework\n");
//        ctrlH.findAll().forEach(System.out::println);
//        System.out.println("Grades\n");
//        ctrlG.findAll().forEach(System.out::println);
//    }
///**Prints the menu*/
//    public void printMenu(){
//        System.out.println("0:Exit");
//        System.out.println("1:Add Student");
//        System.out.println("2:Get Student");
//        System.out.println("3:Update Student");
//        System.out.println("4:Remove Student");
//        System.out.println("5:Add Homework");
//        System.out.println("6:Assign Homework");
//        System.out.println("7:Extend Deadline Homework");
//        System.out.println("8:Grade a Homework");
//    }
//    /**Adds a student read from keyboard in the repo of students*/
//    void addStudent(){
//        try {
//            System.out.println("'Student Name'`'Group'`'Email'`'LabTeacher'");
//            String input = scanner.nextLine();
//            ctrlS.save(input);
//        }
//        catch (NumberFormatException e){
//            System.out.println("Last field completed must be integer");} catch (ValidationException e) {
//            e.printStackTrace();
//        }
//    }
//    /**Show the student with the id read from keyboard*/
//    void getStudent(){
//        int id;
//        System.out.println("Student Id");
//        String input = scanner.nextLine();
//        id=Integer.parseInt(input);
//        System.out.println(ctrlS.findOne(id));
//
//    }
//    /**Update the student that is read from keyboard and with the same id*/
//    void updateStudent(){
//
//        try {
//            System.out.println("'Student Id'`'NEW Student Name'`'NEW Group'`'NEW Email'`'NEW LabTeacher'");
//            String input = scanner.nextLine();
//            ctrlS.update(input);}
//            catch   (IllegalArgumentException e)
//            {
//                System.out.println("Nu se poate sterge acest student");
//            } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (ValidationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**Extends the deadline of a homework, if the current week isn't past deadline*/
//    private void ExtendDeadline() {
//        int id;
//        try{
//        System.out.println("Homework Id");
//        String input = scanner.nextLine();
//        id = Integer.parseInt(input);
//        if(ctrlH.extendDeadline(id))
//            System.out.println("Deadline Extended 1 week");
//        else System.out.println("It's too late to extend the deadline");
//        }catch (NumberFormatException e){
//            System.out.println("Last field completed must be integer");
//        } catch (ValidationException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * Reads an id for student, on for a homework and creates a grade that assigns the homework to the student
//     */
//    private void assignHomework() {
//        try {
//            System.out.println("'Student Id'`'Homework Id");
//            String input = scanner.nextLine();
//            ctrlG.save(input);
//        }
//            catch (NumberFormatException e) {
//            System.out.println("Last field completed must be integer");
//        }
//        catch (IllegalArgumentException e){
//            System.out.println(e.getMessage());
//        } catch (ValidationException e) {
//            e.printStackTrace();
//        }
//    }
///**Adds a homework read from keyboard to the homework repo*/
//    private void addHomework() {
//        try{ System.out.println("'Homework Description'`'Homework deadline'");
//        String input = scanner.nextLine();
//        ctrlH.save(input);
//
//
//        }
//        catch (ValidationException e){
//            System.out.println(e.getMessage());
//        }
//        catch (NumberFormatException e){
//            System.out.println("Last field completed must be integer");
//        }
//    }
///**delete a student with the id read from keyboard*/
//    private void removeStudent() {
//        try{
//        System.out.println("Student Id");
//        String input = scanner.nextLine();
//        int id = Integer.parseInt(input);
//        ctrlS.delete(id);
//        }
//        catch (NumberFormatException e){
//            System.out.println("Last field completed must be integer");
//        }
//        catch (IllegalArgumentException e) {
//            System.out.println("This student doesn't exists");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    }
//

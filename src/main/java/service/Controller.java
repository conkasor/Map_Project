//package service;
//
//import domain.Grade;
//import domain.Homework;
//import domain.Student;
//import domain.Validator.ValidationException;
//import repository.CrudRepository;
//
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//
//import static java.lang.Math.toIntExact;
//
//public class service {
//    private int currentWeek;
//    private CrudRepository<Integer, Student> repoStudents;
//    private CrudRepository<Integer, Homework> repoHomework;
//    private CrudRepository<Integer, Grade> repoGrades;
//
//    /**
//     *
//     * @return current week of the semester;
//     */
//    public int getCurrentWeek() {
//        return currentWeek;
//    }
//
//    public service(CrudRepository<Integer, Student> repoStudents, CrudRepository<Integer, Homework> repoHomework
//            ,CrudRepository<Integer,Grade>repoGrades) {
//        this.repoStudents = repoStudents;
//        this.repoHomework = repoHomework;
//        this.repoGrades=repoGrades;
//        LocalDate now = LocalDate.now();
//        LocalDate start=LocalDate.of(2018,10,1);
//        currentWeek= toIntExact(ChronoUnit.WEEKS.between(start, now))+1;
//    }
//
//    /**
//     *
//     * @param homework the homework entity that'll be added
//     * @throws ValidationException in case that the entity is wrong
//     */
//    public void addHomework(Homework homework) throws ValidationException {
//        repoHomework.save(homework);
//    }
//
//    /**
//     *
//     * @param Sid the id of the student that will receive the homeowrok
//     * @param Hid the id of the homework that will be assigned
//     * @param id the id of the newly created grade
//     */
//    public void assignHomework(int Sid, int Hid,int id) {
//        Student student = repoStudents.findOne(Sid);
//        Homework homework= repoHomework.findOne(Hid);
//        if (homework != null && student != null) {
//            if (repoGrades.save(new Grade(student, homework, id))==null)
//                throw new IllegalArgumentException("Grade id already exists");
//
//        }
//        else if (student == null) throw new IllegalArgumentException("Student doesn't exist");
//        else throw new IllegalArgumentException("Homework Doesn't Exist");
//    }
//
//    /**
//     *Extends the deadline of a given homework by id
//     * @param Hid the homework deadline that you want to extend
//     * @return true if succeds, false otherwise
//     */
//    public boolean extendDeadline(int Hid) {
//        Homework n=repoHomework.findOne(Hid);
//        try{if (currentWeek <= n.getDeadline())
//        {n.setDeadline(n.getDeadline()+1);
//        repoHomework.update(n);
//        return true;}
//        return false;}
//        catch (NullPointerException e){
//            System.out.println("This homework doesn't exist");
//        }
//        return false;
//    }
//
//    /**
//     *
//     * @param st the student that will be added if it passes the checks
//     * @return null if is added, the student if it fails to add the student in repo
//     * @throws IllegalArgumentException
//     * @throws ValidationException
//     */
//    public Student addStudent(Student st) throws IllegalArgumentException, ValidationException {
//
//
//        return repoStudents.save(st);
//    }
//
//    /**
//     *
//     * @param Sid the Student id that we want to search
//     * @return the student
//     * @throws IllegalArgumentException
//     */
//    public Student findStudent(int Sid)throws IllegalArgumentException {
//        return repoStudents.findOne(Sid);
//    }
//
//    /**
//     *
//     * @return a list with all students in find all students
//     */
//    public Iterable<Student> findAllStudents(){
//        return repoStudents.findAll();
//    }
//
//    /**
//     *
//     * @param Sid the id of the student that will be removed
//     * @return student if succedes, exception otherwise
//     * @throws IllegalArgumentException
//     */
//    public Student deleteStudent(int Sid)throws  IllegalArgumentException{
//        return repoStudents.delete(Sid);
//    }
//
//    /**
//     *
//     * @param st the new student that will change the old one
//     * @return the old student
//     * @throws ValidationException if fails
//     */
//    public Student updateStudent(Student st) throws ValidationException {
//        return repoStudents.update(st);
//    }
//    public Iterable<Student> findAllS(){
//        return repoStudents.findAll();
//    }
//    public Iterable<Homework> findAllH() {
//        return repoHomework.findAll();
//    }
//    public Iterable<Grade> findAllG(){
//        return repoGrades.findAll();
//
//    }
//
//
//}
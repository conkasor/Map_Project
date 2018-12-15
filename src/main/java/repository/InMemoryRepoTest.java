//package repository;
//
//import domain.Student;
//import domain.Validator.StudentValidator;
//import repository.InMemoryRepo;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//class InMemoryRepoTest {
//    private InMemoryRepo<Integer,Student> repo=new InMemoryRepo<Integer,Student>(new StudentValidator());
//    private Student st1=new Student(10,"Bob",223,"bob@gmail.com","Camelia");
//    private Student st2=new Student(3,"Bob Bobescu",233,"bob22@gmail.com","Came");
//    @Test
//    void save() {
//        try{
//            repo.save(null);
//        }
//        catch (IllegalArgumentException e){
//            assertEquals("Can't add null", e.getMessage());
//        }
//        assertNull(repo.save(st1));
//        assertNull(repo.save(st2));
//        assertEquals(st2,repo.save(st2));
//    }
//    @Test
//    void findOne() {
//        repo.save(st2);
//        assertEquals(st2,repo.findOne(3));
//        assertNull(repo.findOne(2));
//    }
//
//    @Test
//    void findAll() {
//        repo.save(st1);
//        repo.save(st2);
//        int nr=0;
//        for (Student st:repo.findAll())nr++;
//        assertEquals(2,nr);
//    }
//
//
//
//    @Test
//    void delete() {
//        try {
//            repo.delete(null);
//
//        }
//        catch (IllegalArgumentException e) {
//            assertEquals("The entitity that you try to delete doesn't exist bro",e.getMessage());
//        }
//        repo.save(st1);
//        assertEquals(st1,repo.delete(10));
//    }
//    @Test
//    void update() {
//        try{repo.update(null);}
//        catch(IllegalArgumentException e){
//            assertEquals("The new entity can't be null",e.getMessage());
//        }
//        Student st22=new Student(3,"Bob Bobescu",234,"bob22@gmail.com","Came");
//        try{repo.update(st22);}
//        catch (IllegalArgumentException e){
//            assertEquals("The entity you want to update doesn't exist",e.getMessage());
//        }
//        repo.save(st2);
//        repo.update(st22);
//        assertEquals(234,repo.findOne(3).getGroup());
//    }
//}
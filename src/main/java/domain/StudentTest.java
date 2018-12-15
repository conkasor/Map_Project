//package domain;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class StudentTest {
//    static Student s=new Student(10,"Bob",223,"filipoiuBarosanu6969XXX@gmail.com","Cami");
//    @org.junit.jupiter.api.Test
//    void setName(){
//        s.setName("Bobus");
//        assertEquals("Bobus",s.getName());
//    }
//    @org.junit.jupiter.api.Test
//    void setGroup(){
//        s.setGroup(100);
//        assertEquals(100,s.getGroup());
//    }
//    @org.junit.jupiter.api.Test
//    void setEmail() {
//        s.setEmail("filipoiu@gmail.com");
//        assertEquals("filipoiu@gmail.com",s.getEmail());
//    }
//
//    @org.junit.jupiter.api.Test
//    void setLabTeacher() {
//        s.setLabTeacher("Camella");
//        assertEquals("Camella",s.getLabTeacher());
//        s.setLabTeacher("Cami");
//    }
//
//    @org.junit.jupiter.api.Test
//    void getName() {
//        assertEquals("Bob",s.getName());
//    }
//
//
//    @org.junit.jupiter.api.Test
//    void getGroup() {
//        assertEquals(100,s.getGroup());
//    }
//
//    @org.junit.jupiter.api.Test
//    void getEmail() {
//        assertEquals(s.getEmail(),"filipoiu@gmail.com");
//    }
//
//    @org.junit.jupiter.api.Test
//    void getLabTeacher() {
//        assertEquals("Cami",s.getLabTeacher());
//    }
//
//    @org.junit.jupiter.api.Test
//    void getID() {
//        assertEquals((Integer)10,s.getID());
//    }
//
//
//    @org.junit.jupiter.api.Test
//    void setID() {
//        s.setID(1);
//        assertEquals((Integer)1,s.getID());
//    }
//}
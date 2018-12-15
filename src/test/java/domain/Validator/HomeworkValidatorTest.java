package domain.Validator;

import domain.Homework;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeworkValidatorTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void validate() {
        HomeworkValidator v=new HomeworkValidator();
        Homework h=new Homework(1,"aaks",1,1);
        try{v.validate(h);
            assertTrue(true);}
            catch (ValidationException e){
                fail("asdkdsk");}
    }
}
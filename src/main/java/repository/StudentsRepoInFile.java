package repository;

import domain.Student;
import domain.Validator.Validator;

import java.util.Arrays;
import java.util.List;

public class StudentsRepoInFile extends InFileRepo<Integer, Student> {
    public StudentsRepoInFile(Validator validator, String file) {
        super(validator, file);
        loadData();
    }

    /**
     *
     * @param line a string with a line from txt file
     * @returns a student entity
     */
    @Override
    protected Student extractEntity(String line) {
        List<String> strings=Arrays.asList(line.split("`"));
        int id=Integer.parseInt(strings.get(0));
        String name=strings.get(1);
        int group=Integer.parseInt(strings.get(2));
        String email=strings.get(3);
        String labTeacher=strings.get(4);
        Student st=new Student(id,name,group,email,labTeacher);
      return st;
    }
}
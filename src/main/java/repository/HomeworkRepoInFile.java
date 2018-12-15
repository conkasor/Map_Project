package repository;

import domain.Homework;
import domain.Validator.Validator;

import java.util.Arrays;
import java.util.List;

public class HomeworkRepoInFile extends InFileRepo<Integer,Homework> {
    public HomeworkRepoInFile(Validator validator, String file) {
        super(validator, file);
        loadData();
    }

    @Override
    protected Homework extractEntity(String line) {
        List<String> strings= Arrays.asList(line.split("`"));
        int id=Integer.parseInt(strings.get(0));
        String desc=strings.get(1);
        int deadline=Integer.parseInt(strings.get(2));
        int received=Integer.parseInt(strings.get(3));
        return new Homework(id,desc,deadline,received);
    }
}

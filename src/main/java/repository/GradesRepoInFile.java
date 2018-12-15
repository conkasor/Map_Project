package repository;

import domain.Grade;
import domain.Homework;
import domain.Student;
import domain.Validator.Validator;

import java.util.Arrays;
import java.util.List;

public class GradesRepoInFile extends InFileRepo<Integer,Grade> {

    private CrudRepository repoS;
    private CrudRepository repoH;


    public GradesRepoInFile(Validator validator, String file, CrudRepository repoS, CrudRepository repoH) {
        super(validator, file);
        this.repoS = repoS;
        this.repoH = repoH;
        loadData();


    }
    @Override
    protected Grade extractEntity(String line) {
        List<String> strings=Arrays.asList(line.split("`"));
        int id=Integer.parseInt(strings.get(0));
        Student S;
        Homework H;

            S = (Student) repoS.findOne(Integer.parseInt(strings.get(1)));
            H = (Homework) repoH.findOne(Integer.parseInt(strings.get(2)));

        if (S==null || H==null)
        return null;


        float val=Float.parseFloat(strings.get(3));
        int weekGraded=Integer.parseInt(strings.get(4));
        return new Grade(id,S,H,val,weekGraded);
    }


}

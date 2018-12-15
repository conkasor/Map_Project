package service;

import domain.Student;
import repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentsService extends CrudServiceIdInteger<Student> {
    public StudentsService(CrudRepository<Integer, Student> repo) {
        super(repo);
    }

    @Override
    protected Student ParseEntityFromString(String string) {
        int id = -10,group;
        String[] args=string.split("`");
        try{id = Integer.parseInt(args[0]);
        group=Integer.parseInt(args[2]);
        return new Student(id,args[1],group,args[3],args[4]);
    }
    catch (NumberFormatException e) {
        return new Student(id, args[1], -1, args[3], args[4]);
    }
    }
    public List<Student> filterByName(String filterArgument){
        List<Student> list=(List<Student>)(List)findAll();
        List<Student> result=list.stream().filter(x->x.getName().toLowerCase().contains(filterArgument.toLowerCase())).collect(Collectors.toList());
        return result;
    }

}

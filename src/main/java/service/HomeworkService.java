package service;

import domain.HasID;
import domain.Homework;
import domain.Validator.ValidationException;
import repository.CrudRepository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

public class HomeworkService extends CrudServiceIdInteger<Homework> {
    public HomeworkService(CrudRepository<Integer, Homework> repo) {
        super(repo);

    }

    @Override
    protected Homework ParseEntityFromString(String string) throws  NumberFormatException{
        String[] args=string.split("`");
            int id = Integer.parseInt(args[0]);
            int deadline = Integer.parseInt(args[2]);
            int received= Integer.parseInt(args[3]);
        return new Homework(id,args[1],deadline,received);
    }

    public boolean extendDeadline(int id) throws ValidationException{
        Homework n=(Homework)findOne(id);
        try{if (getCurrentWeek() <= n.getDeadline())
        {n.setDeadline(n.getDeadline()+1);
            repo.update(n);
            return true;}
            return false;}
        catch (NullPointerException e){
            System.out.println("This homework doesn't exist");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public HasID<Integer> save(String string) throws ValidationException {
        return super.save(string);
    }
    public Optional<Homework> getByName (String name){
        List<Homework> mythings = (List<Homework>) (Object) findAll();
        Optional<Homework> h= Optional.of(mythings.stream()
                .filter(x -> x.getDescription()==name)
                .findFirst()).get();
        return h;
    }



}

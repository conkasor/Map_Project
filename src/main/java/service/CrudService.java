package service;

import domain.HasID;
import domain.Validator.ValidationException;
import repository.CrudRepository;
import util.Observable;
import util.Observer;
import util.OperationType;
import util.ChangeEvent;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public abstract class CrudService<ID,E extends HasID<ID>> implements Observable<ChangeEvent> {
    private List<Observer<ChangeEvent>> observers=new ArrayList<>();
protected CrudRepository<ID,E> repo;
    public CrudService(CrudRepository<ID, E> repo) {
        this.repo = repo;

    }

    /**
     *
     * @return the number of weeks that paste from first of october
     */
    public int getCurrentWeek(){
        LocalDate now = LocalDate.now();
        LocalDate start=LocalDate.of(2018,10,1);
        return toIntExact(ChronoUnit.WEEKS.between(start, now))+1;
    }

    /**
     *
     * @return a list with all entities from repo
     */
    public List<E> findAll(){
        List<E> l=new ArrayList<E>();
        repo.findAll().forEach(l::add);
        return l;
    }

    /**
     *
     * @param string the string that will be converted to entity
     * @return null if succesfull, the entity if fails
     * @throws ValidationException
     */
    E save(String string) throws ValidationException,IllegalArgumentException{
        E entity=ParseEntityFromString(string);
        E v= repo.save(entity);
        notifyObservers(new ChangeEvent(OperationType.ADD,v));
        return v;
    }

    /**
     *
     * @param string the entity taken as string
     * @return the enetity as object
     */
    protected abstract E ParseEntityFromString(String string);

    /**
     *
     * @param id the id of the entity that will be searched
     * @return the entity if succesful/ null if fails
     */
    public E findOne(ID id) {
        return repo.findOne(id);
    }

    /**
     *
     * @param id the id of the entity that will be deleted
     * @return the entity if succesful/null if fails
     * @throws FileNotFoundException
     */
    public E delete(ID id) throws FileNotFoundException {
            E entity= repo.delete(id);
            if (entity!=null)
                notifyObservers(new ChangeEvent(OperationType.DELETE,entity));
            return entity;
    }

    /**
     *
     * @param string the new entity that is readed as a string
     * @return the old entity
     * updates the enetity from repo wihth the one that is received as string
     * @throws ValidationException
     * @throws IllegalArgumentException
     * @throws FileNotFoundException
     */
    public E update(String string) throws ValidationException, IllegalArgumentException, FileNotFoundException {
        E entity=ParseEntityFromString(string);
        E ent= repo.update(entity);
        if (ent!=entity)
            notifyObservers(new ChangeEvent(OperationType.UPDATE,entity,ent));
        return ent;
    }

    @Override
    public void addObserver(Observer<ChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<ChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(ChangeEvent t) {
        observers.forEach(x->x.update(t));
    }
}



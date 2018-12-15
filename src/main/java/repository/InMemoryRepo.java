package repository;

import domain.HasID;
import domain.Validator.ValidationException;
import domain.Validator.Validator;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class InMemoryRepo<ID, E extends HasID<ID>> implements  CrudRepository<ID,E> {
    /**
     * CRUD operations repository interface
     * @param <ID> - type Student must have an attribute of type ID
     * @param <Student> -  type of entities saved in repository
     */
    private Validator<E> validator;

    Map<ID,E> entities;

    public InMemoryRepo(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    /**
     *
     * @param id -the id of the entity to be returned
     * id must not be null
     * @return the element with the specified id
     */
    @Override
    public E findOne(ID id) {
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    /**
     *
     * @return all elements in the repository
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     *
     * @param entity the element that will be added in repo
     * entity must be not null
     * @return null for succes, the entity for failure
     * @throws ValidationException if the entity doesn't respect the validation norms
     * @throws IllegalArgumentException if you try to add null
     */
    @Override
    public E save(E entity) throws ValidationException,IllegalArgumentException {
        if (entity == null) throw new IllegalArgumentException("Can't add null");
        validator.validate(entity);
            return entities.putIfAbsent(entity.getID(), entity);
    }

    /**
     * @param id the id of the element that will be deleted
     * id must be not null
     * @return the deleted element if it is done succesfully
     * @throws IllegalArgumentException if you try to delete an inexistent entity
     */
    @Override
    public E delete(ID id) throws IllegalArgumentException, FileNotFoundException {
        if (entities.containsKey(id))
            return entities.remove(id);
        throw new IllegalArgumentException();
    }

    /**
     *
     * @param entity
     * entity must not be null
     * @return the old entity
     * @throws ValidationException in case that the new entity isn't ok
     * @throws IllegalArgumentException in case that the entity we want to update doesn't exists
     */
    @Override
    public E update(E entity) throws ValidationException, IllegalArgumentException, FileNotFoundException {
        if (entity==null)
            throw new IllegalArgumentException("The new entity can't be null");
        validator.validate(entity);
        if(entities.get(entity.getID()) != null) {
            E aux=entities.get(entity.getID());
            entities.put(entity.getID(),entity);
            return aux;
        }
        else throw new IllegalArgumentException("The entity you want to update doesn't exist");

    }
}

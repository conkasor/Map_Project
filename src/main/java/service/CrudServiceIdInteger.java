package service;

import domain.HasID;
import domain.Validator.ValidationException;
import repository.CrudRepository;

import java.util.Comparator;
import java.util.Optional;

public abstract class CrudServiceIdInteger<E extends HasID<Integer>> extends CrudService<Integer, HasID<Integer>> {
    private int currentID;

    public CrudServiceIdInteger(CrudRepository repo) {
        super(repo);
        currentID = getIDMax();
    }

    @Override
    protected abstract HasID<Integer> ParseEntityFromString(String string);

    /**
     *
     * @return the current id that follows in the list of entities
     */
    private int getIDMax() {
        Optional<HasID<Integer>> max = findAll().stream().max(Comparator.comparingInt(HasID::getID));
        return max.map(HasID::getID).orElse(-1);
    }

    /**
     *
     * @param string the string that will be converted to entity
     * @return the entity will be saved with the auto generated id
     * @throws ValidationException
     */
    @Override
    public HasID<Integer> save(String string) throws ValidationException,IllegalArgumentException {
        return super.save(Integer.toString(currentID += 1) + "`" + string);
    }
}


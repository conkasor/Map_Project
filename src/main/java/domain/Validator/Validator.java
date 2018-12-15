package domain.Validator;

public interface Validator<E> {
    void validate(E entity) throws ValidationException;
}

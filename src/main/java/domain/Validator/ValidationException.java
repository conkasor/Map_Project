package domain.Validator;

public class ValidationException extends RuntimeException {
    public ValidationException(String errs) {
        super(errs);
    }
}

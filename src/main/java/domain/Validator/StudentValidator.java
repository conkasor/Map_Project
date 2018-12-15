package domain.Validator;

import domain.Student;

public class StudentValidator implements  Validator<Student> {
    /**
     *
     * @param entity the entity that will be validated
     * @throws ValidationException if something is wrong
     */
    @Override
    public void validate(Student entity) throws ValidationException {
        String errs="";
        if (entity.getID()<0)
            errs=errs+"id can't be negative\n";
        if (entity.getGroup()<100 || entity.getGroup()>1000)
            errs=errs+"grooup should be between 100 and 999\n";
        if (!entity.getLabTeacher().matches("^[A-Z a-z]+$"))
            errs=errs+"Teacher name isn't a valid name \n";
        if (!entity.getEmail().matches(".+@.+\\..+"))
            errs=errs+"Email is not a valid one \n";
        if (!entity.getName().matches("^[A-Z a-z]+$"))
            errs=errs+"Name is nota valid one \n";
        if (!errs.equals(""))
            throw new ValidationException(errs);
    }
}

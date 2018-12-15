package domain.Validator;

import domain.Grade;

public class GradeValidator implements Validator<Grade> {
    @Override
    public void validate(Grade entity) throws ValidationException {
        String errs="";
        if (entity.getID()<0)
            errs+="Id-ul can't be negative";
        if (entity.getHomework()==null)
            errs+="Homework can't be null";
        if (entity.getStudent()==null)
            errs+="Student can't be null";
        if (entity.getValue()>10 || entity.getValue()<0)
            errs+="Value can't be otside [0,10]";
        if (!errs.equals(""))
            throw new  ValidationException(errs);
    }
}

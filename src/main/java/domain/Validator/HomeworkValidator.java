package domain.Validator;

import domain.Homework;

public class HomeworkValidator implements Validator<Homework> {
    @Override
    public void validate(Homework entity) throws ValidationException {
        String errs="";
        if (entity.getDescription().equals(""))
            errs+="Description can't be empty ";
        if (entity.getDeadline()<0 || entity.getDeadline()>14)
            errs+="Deadline isn't between 1-14 ";
        if (entity.getID()<0)
            errs+="Id can't be negative ";
        if (entity.getReceived()<0 || entity.getReceived()>14)
            errs+="Recevied can't be outside [0-14]";
        if (!errs.equals(""))
            throw new ValidationException(errs);
    }
}

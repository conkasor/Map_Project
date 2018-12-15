package repository;

import domain.Grade;
import domain.Homework;
import domain.Student;
import domain.Validator.GradeHomeworkException;
import domain.Validator.ValidationException;
import domain.Validator.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.stream.Collectors;

public class GradesRepoXML extends XMLRepo<Integer, Grade> {
    private CrudRepository repoS;
    private CrudRepository repoH;

    public GradesRepoXML(Validator validator, String file, CrudRepository<Integer, Student>repoS, CrudRepository<Integer, Homework>repoH) {
        super(validator, file);
        this.repoS=repoS;
        this.repoH=repoH;
        loadData();
    }

    @Override
    protected Element createElementFromEntity(Document document, Grade m) {
        Element e = document.createElement("Grade");
        e.setAttribute("id", String.valueOf(m.getID()));
        e.setIdAttribute("id",true);
        Element desc = document.createElement("Sid");
        desc.setTextContent(String.valueOf(m.getStudent().getID()));
        e.appendChild(desc);
        Element message = document.createElement("Hid");
        message.setTextContent(String.valueOf(m.getHomework().getID()));
        e.appendChild(message);
        Element value = document.createElement("value");
        value.setTextContent(String.valueOf(m.getValue()));
        e.appendChild(value);
        Element wGraded = document.createElement("wGraded");
        wGraded.setTextContent(String.valueOf(m.getWeekGraded()));
        e.appendChild(wGraded);
        Element feedback = document.createElement("feedback");
        feedback.setTextContent(String.valueOf(m.getFeedback()));
        e.appendChild(feedback);
        return e;
    }

    @Override
    protected Grade createEntityFromElement(Element messageTaskElement) throws GradeHomeworkException {
        int id = Integer.parseInt(messageTaskElement.getAttribute("id"));
        NodeList nods = messageTaskElement.getChildNodes();
        int sid = Integer.parseInt(messageTaskElement.getElementsByTagName("Sid")
                .item(0)
                .getTextContent());

        int hid =Integer.parseInt(messageTaskElement.getElementsByTagName("Hid")
                .item(0)
                .getTextContent());
        Student S;
        Homework H;

        S = (Student) repoS.findOne(sid);
        H = (Homework) repoH.findOne(hid);

        if (S==null || H==null)
            return null;
        float value =Float.parseFloat(messageTaskElement.getElementsByTagName("value")
                .item(0)
                .getTextContent());
        int wGraded =Integer.parseInt(messageTaskElement.getElementsByTagName("wGraded")
                .item(0)
                .getTextContent());
        String feedback=messageTaskElement.getElementsByTagName("feedback")
                .item(0).getTextContent();

        return new Grade(S,H,value,id,wGraded,feedback);

    }

    @Override
    public Grade save(Grade entity) throws ValidationException {
        findAll().forEach(x->{if (x.getHomework().getID()==entity.getHomework().getID() && x.getStudent().getID()==entity.getStudent().getID()) try {
            throw new ValidationException("This student was evaluated for this Homework");
        } catch (ValidationException e) {
            throw e;
        }
        });
        return super.save(entity);
    }
}

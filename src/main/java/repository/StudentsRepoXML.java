package repository;

import domain.Student;
import domain.Validator.ValidationException;
import domain.Validator.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class StudentsRepoXML extends XMLRepo<Integer, Student> {
    public StudentsRepoXML(Validator validator, String file) {
        super(validator, file);
            loadData();

        }


    @Override
    protected Element createElementFromEntity(Document document, Student m) {
        Element e = document.createElement("Student");
        e.setAttribute("id", String.valueOf(m.getID()));


        Element desc = document.createElement("name");
        desc.setTextContent(m.getName());
        e.appendChild(desc);

        Element message = document.createElement("group");
        message.setTextContent(String.valueOf(m.getGroup()));
        e.appendChild(message);

        Element from = document.createElement("email");
        from.setTextContent(m.getEmail());
        e.appendChild(from);

        Element to = document.createElement("labTeacher");
        to.setTextContent(m.getLabTeacher());
        e.appendChild(to);
        e.setIdAttribute("id",true);
        return e;
    }

    @Override
    protected Student createEntityFromElement(Element messageTaskElement) {
        int id = Integer.parseInt(messageTaskElement.getAttribute("id"));
        NodeList nods = messageTaskElement.getChildNodes();
        String name =messageTaskElement.getElementsByTagName("name")
                .item(0)
                .getTextContent();

        int group =Integer.parseInt(messageTaskElement.getElementsByTagName("group")
                .item(0)
                .getTextContent());

        String email =messageTaskElement.getElementsByTagName("email")
                .item(0)
                .getTextContent();

        String labTeacher =messageTaskElement.getElementsByTagName("labTeacher")
                .item(0)
                .getTextContent();
        return new Student(id,name,group,email,labTeacher);

    }
}

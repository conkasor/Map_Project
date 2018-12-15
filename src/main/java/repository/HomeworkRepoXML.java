package repository;

import domain.Homework;
import domain.Validator.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class HomeworkRepoXML extends XMLRepo<Integer, Homework> {
    public HomeworkRepoXML(Validator validator, String file) {
        super(validator, file);
        loadData();
    }

    @Override
    protected Element createElementFromEntity(Document document, Homework m) {
        Element e = document.createElement("Homework");
        e.setAttribute("id", String.valueOf(m.getID()));
        e.setIdAttribute("id",true);
        Element desc = document.createElement("description");
        desc.setTextContent(m.getDescription());
        e.appendChild(desc);

        Element message = document.createElement("deadline");
        message.setTextContent(String.valueOf(m.getDeadline()));
        e.appendChild(message);

        Element from = document.createElement("received");
        from.setTextContent(String.valueOf(m.getReceived()));
        e.appendChild(from);
        return e;
    }

    @Override
    protected Homework createEntityFromElement(Element messageTaskElement) {
        int id = Integer.parseInt(messageTaskElement.getAttribute("id"));
        NodeList nods = messageTaskElement.getChildNodes();
        String description =messageTaskElement.getElementsByTagName("description")
                .item(0)
                .getTextContent();

        int deadline =Integer.parseInt(messageTaskElement.getElementsByTagName("deadline")
                .item(0)
                .getTextContent());

        int received = Integer.parseInt(messageTaskElement.getElementsByTagName("received")
                .item(0)
                .getTextContent());

        return new Homework(id,description,deadline,received);

    }

}

package repository;

import domain.HasID;
import domain.Validator.GradeHomeworkException;
import domain.Validator.ValidationException;
import domain.Validator.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.util.Optional;

public abstract class XMLRepo<ID, E extends HasID<ID>> extends InMemoryRepo<ID,E> {
    String fileName;
    public XMLRepo(Validator validator, String file) {
        super(validator);
        fileName=file;
    }
    @Override
    public E save(E entity) throws ValidationException {

        var stuff = super.save(entity);
        if (stuff == null){
            writeToFile();
        }

        return stuff;
    }
    protected void loadData() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(this.fileName);

            Element root = document.getDocumentElement();
            NodeList children = root.getChildNodes();
            for(int i=0; i < children.getLength(); i++) {
                Node entityElement = children.item(i);
                if(entityElement instanceof Element) {
                    Optional<E> entity = Optional.ofNullable( createEntityFromElement((Element)entityElement));
                    entity.ifPresent(entity1 -> {
                        try {
                            super.save(entity1);
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void writeToFile(){
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();
            Element root  = document.createElement("root");
            document.appendChild(root);
            super.findAll().forEach(m->{
                Element e = createElementFromEntity(document,m);
                root.appendChild(e);
            });

            //write Document to file
            Transformer transformer = TransformerFactory.
                    newInstance().newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(fileName));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public E delete(ID id) throws IllegalArgumentException, FileNotFoundException {
        Optional<E>rez=Optional.ofNullable(super.delete(id));
        if (rez.isPresent())
            writeToFile();
        return rez.get();
    }

    @Override
    public E update(E entity) throws ValidationException, IllegalArgumentException, FileNotFoundException {
        Optional<E>rez=Optional.ofNullable(super.update(entity));
        if (rez.isPresent())
            writeToFile();
        return rez.get();
    }

    protected abstract Element createElementFromEntity(Document document, E m);

    protected abstract E createEntityFromElement(Element messageTaskElement) throws GradeHomeworkException;
}

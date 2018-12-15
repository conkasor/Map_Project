package sample;

import com.sun.jdi.IntegerValue;
import domain.Homework;
import domain.Student;
import domain.Validator.HomeworkValidator;
import domain.Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import repository.HomeworkRepoXML;
import service.HomeworkService;
import util.Event;
import util.Observer;

import java.io.FileNotFoundException;
import java.util.List;

import static sun.security.krb5.Confounder.intValue;


public class HomeworkController implements Observer {

    private ObservableList<Homework> model= FXCollections.observableArrayList();
    HomeworkService service;
    @FXML
    TableView tableView;
    @FXML
    Label labelDeadline;
    @FXML
    Label labelWeekProposed;
    @FXML
    TableColumn tableColumnId;
    @FXML
    TableColumn tableColumnDescription;
    @FXML
    TableColumn tableColumnDeadline;
    @FXML
    TableColumn tableColumnWeek;
    @FXML
    Button buttonAdd;
    @FXML
    Button buttonDelete;
    @FXML
    Button buttonUpdate;

    @FXML
    Label labelCurrentId;
    @FXML
    TextField textFieldDescription;
    @FXML
    Slider sliderDeadline;
    @FXML
    Slider sliderWeek;

    public HomeworkController(HomeworkService service) {

        this.service=service;

    }

    @FXML
    public void initialize(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Homework, Integer>("ID"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<Homework, String>("description"));
        tableColumnDeadline.setCellValueFactory(new PropertyValueFactory<Homework, Integer>("deadline"));
        tableColumnWeek.setCellValueFactory(new PropertyValueFactory<Homework, Integer>("received"));

        initializeSliderDeadline();
        initializeSliderWeek();
        buttonAdd.setOnAction(e->handleAdd());
        buttonDelete.setOnAction(e->handleDelete());
        buttonUpdate.setOnAction(e->handleUpdate());
        model.setAll((List<Homework>)(List)service.findAll());
        tableView.setItems(model);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, old,value)->{tableSeleciton((Homework) value);});
        service.addObserver(this);
    }

    private void tableSeleciton(Homework newValue) {
            if (newValue==null)
            {
                labelCurrentId.setText("");
                textFieldDescription.setText("");
                sliderDeadline.setValue(service.getCurrentWeek()+1);
                sliderWeek.setValue(sliderWeek.getMax());

            }
            else
            {
                labelCurrentId.setText(String.valueOf(newValue.getID()));
                textFieldDescription.setText(newValue.getDescription());
                sliderDeadline.setValue(newValue.getDeadline());
                sliderWeek.setValue(newValue.getReceived());
            }
        }


    private void handleAdd(){
        String string=collectStrings();
        try {
            service.save(string);
        } catch (ValidationException e) {
            showMessage(Alert.AlertType.ERROR, "Validation failed ", e.getMessage());
        }
    }
    private void handleDelete(){

        try {
            int id= Integer.parseInt(labelCurrentId.getText());

            if (service.delete(id)==null)
                showMessage(Alert.AlertType.ERROR,"This homework Doesn't exist","Can't delete inexsitent Homework");
            else
                showMessage(Alert.AlertType.INFORMATION,"Successfully","Homework deleted successfully");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (NumberFormatException e) {
            showMessage(Alert.AlertType.ERROR, "This homework Doesn't exist", "Can't delete inexsitent Homework");

        }

    }
    private void handleUpdate(){
        String str=collectStrings();
        str=labelCurrentId.getText()+"`"+str;
        try {
            service.update(str);
        } catch (ValidationException e) {
            showMessage(Alert.AlertType.ERROR,"Validation Error!",e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    static void showMessage(Alert.AlertType type, String header, String text) {
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    private String collectStrings() {
        String str="";
        str+=textFieldDescription.getText();
        str+="`"+Integer.toString(Double.valueOf(sliderDeadline.getValue()).intValue());
        str+="`"+Integer.toString(Double.valueOf(sliderWeek.getValue()).intValue());
        return str;
    }

    private void initializeSliderWeek() {
        sliderWeek.setMax(service.getCurrentWeek());
        sliderWeek.setMin(1);
        sliderWeek.setValue(sliderWeek.getMax());
        sliderWeek.valueProperty().addListener(o-> refreshWeek());
        sliderWeek.setBlockIncrement(1);
        refreshWeek();
    }

    private void initializeSliderDeadline() {
        sliderDeadline.setMax(14);
        sliderDeadline.setMin(service.getCurrentWeek());
        sliderDeadline.setValue(sliderDeadline.getMin()+1);
        sliderDeadline.valueProperty().addListener(o-> refreshDeadline());
        sliderDeadline.setBlockIncrement(1);
        refreshDeadline();
    }

    private void refreshDeadline() {
        int val=Double.valueOf(sliderDeadline.getValue()).intValue();
        labelDeadline.setText("Deadline:\n"+Integer.toString(val));
    }

    private void refreshWeek() {
        int val=Double.valueOf(sliderWeek.getValue()).intValue();
        labelWeekProposed.setText("Week Proposed:\n"+Integer.toString(val));
    }

    @Override
    public void update(Event event) {
        model.setAll((List<Homework>) (List)service.findAll());
    }
}

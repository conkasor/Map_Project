package sample;

import domain.HasID;
import domain.Student;
import domain.Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import service.StudentsService;
import util.Event;
import util.Observer;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentsController implements Observer {
    StudentsView view;
    StudentsService service;
    private ObservableList<Student> model;

    public StudentsController(StudentsService service) {
             this.service = service;
        List<HasID<Integer>> list= service.findAll();
        model= FXCollections.observableArrayList((List<Student>)(List)list);
    }

    public ObservableList<Student> getModel() {
        return model;
    }

    public void showStudentDetails(Student newValue) {
        if (newValue==null)
        {
            view.labelId.setText("");
            view.textFieldName.setText("");
            view.textFieldGroup.setText("");
            view.textFieldEmail.setText("");
            view.textFieldLabProf.setText("");

        }
        else
        {
            view.labelId.setText(String.valueOf(newValue.getID()));
            view.textFieldName.setText(newValue.getName());
            view.textFieldGroup.setText(String.valueOf(newValue.getGroup()));
            view.textFieldEmail.setText(newValue.getEmail());
            view.textFieldLabProf.setText(newValue.getLabTeacher());
        }
    }

    public String extractStudent(){

        String st=view.textFieldName.getText()+"`";
        st+= view.textFieldGroup.getText()+"`";
        st+=view.textFieldEmail.getText()+"`";
        st+=view.textFieldLabProf.getText();
        return st;
    }
    public void handleAddStudent(ActionEvent actionEvent) {
        String s= extractStudent();
        clearFields();
        try {
            Student saved= (Student) service.save(s);
            if (saved==null) {
                showMessage(Alert.AlertType.INFORMATION, "Saved Successfully ", "");
                //showMessageTaskDetails(null);
            }
            else

                showErrorMessage("This student already exists","");
        } catch (ValidationException e) {
            showErrorMessage("I not valid",e.getMessage());
        }
        catch (IllegalArgumentException e) {
            showErrorMessage("You can't add null",e.getMessage());
        }
        catch (IndexOutOfBoundsException e){
            showErrorMessage("Fields can't be empty","You need to complete all the fileds");
        }
    }

    static void showMessage(Alert.AlertType type, String header, String text) {
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }
    static void showErrorMessage(String h,String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setHeaderText(h);
        message.setContentText(text);
        message.showAndWait();
    }

    public void handleClearFields(ActionEvent actionEvent) {
        clearFields();
        view.disableDependentButtons(true);
    }

    private void clearFields() {
        view.labelId.setText("");
        view.textFieldName.setText("");
        view.textFieldGroup.setText("");
        view.textFieldEmail.setText("");
        view.textFieldLabProf.setText("");
    }

    public void setView(StudentsView view){
        this.view=view;

    }

    @Override
    public void update(Event event) {
        List<HasID<Integer>> list= service.findAll();
        model.setAll((List<Student>)(List)list);
    }

    public void handleDeleteMessage(ActionEvent actionEvent) {
        int id=Integer.valueOf(view.labelId.getText());
        clearFields();
        try {
            if (service.delete(id)!=null)
                showMessage(Alert.AlertType.INFORMATION, "Student Deleted", "");
            else
                showMessage(Alert.AlertType.INFORMATION, "This Student doesn't exist anymore", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleUpdateMessage(ActionEvent actionEvent) {
        String s= extractStudent();
        s=view.labelId.getText()+"`"+s;
        clearFields();
        try {
            Student saved= (Student) service.update(s);
                showMessage(Alert.AlertType.INFORMATION, "Upadate successful", "");
                //showMessageTaskDetails(null);
            } catch (ValidationException e) {
           showErrorMessage("Validation Failed",e.getMessage());
       } catch (IllegalArgumentException e) {
       } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        view.tableView.getSelectionModel().clearSelection();
    }

    public void deselectSearch(ActionEvent actionEvent) {
        view.searchStudent.setFocusTraversable(false);
    }

    public void handleFilter(KeyEvent keyEvent) {

        if (!   view.searchStudent.getText().equals(""))
            model.setAll(service.filterByName(view.searchStudent.getText()));
        else
            model.setAll((List<Student>)(List)service.findAll());
    }
}

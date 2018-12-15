package sample;

import domain.*;
import domain.Validator.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.GradesService;
import service.HomeworkService;
import service.StudentsService;
import util.Event;
import util.FxUtilTest;
import util.Observer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;
import static java.util.Arrays.*;

public class GradeController implements Observer {
    private String DEFAULT_HOMEWORK;
    private Main app;
    private HomeworkService SH;
    private StudentsService SS;
    private GradesService service;
    private ObservableList<String> modelStudents = FXCollections.observableArrayList();
    private ObservableList<GradeDTO> modelGradesDTO = FXCollections.observableArrayList();
    private ObservableList<String> modelHomework = FXCollections.observableArrayList();
    private ObservableList<String> modelGroups = FXCollections.observableArrayList();
    private ObservableList<String> modelNames = FXCollections.observableArrayList();
    private List<Grade> listToFilter;

    @FXML
    TableView tableViewGradeDTO;
    @FXML
    TableColumn tableColumnStudent;
    @FXML
    TableColumn tableColumnHomework;
    @FXML
    TableColumn tableColumnValue;
    @FXML
    TableColumn tableColumnDate;


    @FXML
    ComboBox comboBoxHomework;
    @FXML
    ComboBox comboBoxAbsences;
    @FXML
    TextField textFieldGrade;
    @FXML
    TextArea textAreaFeedback;
    @FXML
    Button buttonAdd;
    @FXML
    VBox VBoxAdd;
    @FXML
    Button buttonManage;
    @FXML
    VBox VBoxFilters;
    @FXML
    Button buttonFilters;
    @FXML
    Button buttonFilter;
    @FXML
    ComboBox<String> comboBoxStudents;
    @FXML
    ComboBox<String> comboBoxDescription;
    @FXML
    ComboBox<String> comboBoxName;
    @FXML
    ComboBox<String> comboBoxGroup;
    @FXML
    DatePicker datePickerFrom;
    @FXML
    DatePicker datePickerTo;
    @FXML
    Button buttonGradeGroup;
    @FXML
    ComboBox<String> comboBoxGroups;
    @FXML
    ComboBox<String> comboBoxHomeworks;


    private Predicate<Grade> p2;

    public GradeController(GradesService service, StudentsService sserv, HomeworkService hserv, Main app) {
        SS = sserv;
        SH = hserv;
        this.app = app;
        this.service = service;
        service.addObserver(this);
        SH.addObserver(this);
        SS.addObserver(this);
    }

    public void handleAddVisibility(boolean value) {
        VBoxAdd.setVisible(value);
        VBoxAdd.setManaged(value);
        buttonManage.setDisable(value);
        handleFilterVisibility(!value);
    }

    public void handleShowHomework() throws IOException {
        app.changeScene("/HomeworkViewFXML.fxml", new HomeworkController(SH));
    }

    @FXML
    public void initialize() {
        DEFAULT_HOMEWORK = "lab" + service.getCurrentWeek();

        handleAddVisibility(true);
        buttonFilters.setOnAction(x -> handleAddVisibility(false));
        buttonManage.setOnAction(x -> handleAddVisibility(true));
        tableColumnStudent.setCellValueFactory(new PropertyValueFactory<Homework, String>("name"));
        tableColumnHomework.setCellValueFactory(new PropertyValueFactory<Homework, String>("homework"));
        tableColumnValue.setCellValueFactory(new PropertyValueFactory<Homework, Float>("value"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<Homework, Integer>("date"));
        comboBoxStudents.setItems(modelStudents);

        update(new Event() {
        });
        List<Student> nid = (List<Student>) (Object) SS.findAll();
        List<String> l = nid.stream().map(x -> x.getID() + "," + x.getName()).collect(Collectors.toList());
        modelStudents.setAll(l);
        tableViewGradeDTO.setItems(modelGradesDTO);
        updateHomework();
        buttonAdd.setOnAction(x -> handleAdd());
        comboBoxHomework.setValue(DEFAULT_HOMEWORK);
        comboBoxAbsences.setValue("0");
        comboBoxHomework.setOnAction(x -> handleFeedback());
        comboBoxAbsences.setOnAction(x -> handleFeedback());
        buttonAdd.setOnAction(x -> handleAdd());
        comboBoxDescription.setItems(modelHomework);
        comboBoxName.setItems(modelNames);
        comboBoxGroup.setItems(modelGroups);
        comboBoxGroups.setItems(modelGroups);
        comboBoxHomeworks.setItems(modelHomework);
        FxUtilTest.autoCompleteComboBoxPlus(comboBoxStudents, (typedText, objectToCompare) -> objectToCompare.toLowerCase().contains(typedText.toLowerCase()));
        FxUtilTest.autoCompleteComboBoxPlus(comboBoxDescription, ((typedText, objectToCompare) -> objectToCompare.toString().toLowerCase().contains(typedText.toLowerCase())));
        FxUtilTest.autoCompleteComboBoxPlus(comboBoxName, ((typedText, objectToCompare) -> objectToCompare.toLowerCase().contains(typedText.toLowerCase())));
        FxUtilTest.autoCompleteComboBoxPlus(comboBoxGroup, ((typedText, objectToCompare) -> objectToCompare.toLowerCase().contains(typedText.toLowerCase())));
        FxUtilTest.autoCompleteComboBoxPlus(comboBoxHomeworks, ((typedText, objectToCompare) -> objectToCompare.toLowerCase().contains(typedText.toLowerCase())));
        FxUtilTest.autoCompleteComboBoxPlus(comboBoxGroups, (((typedText, objectToCompare) -> objectToCompare.toLowerCase().contains(typedText.toLowerCase()))));
        comboBoxDescription.getEditor().textProperty().addListener((observable, oldValue, newValue) -> handleFilters());
        comboBoxName.getEditor().textProperty().addListener((observable, oldValue, newValue) -> handleFilters());
        comboBoxGroup.getEditor().textProperty().addListener((observable, oldValue, newValue) -> handleFilters());
        datePickerFrom.setOnAction(x -> handleFilters());
        datePickerTo.setOnAction(x -> handleFilters());
        initializep2();
    }

    private void initializep2() {
        p2 = x ->
        {
            try {
                return Integer.toString(x.getStudent().getGroup()).
                        contains(FxUtilTest.getComboBoxValue(comboBoxGroup));
            } catch (NullPointerException e) {
                return true;
            }
        };
    }

    private void handleFilterVisibility(Boolean value) {
        VBoxFilters.setVisible(value);
        VBoxFilters.setManaged(value);
        buttonFilters.setDisable(value);

    }


    public void handleFilters() {
        Predicate<Grade> p1 = x ->
        {
            try {
                return x.getStudent().getName().
                        contains(FxUtilTest.getComboBoxValue(comboBoxName));
            } catch (NullPointerException e) {
                return true;
            }
        };

        Predicate<Grade> p3 = x ->
        {
            try {
                if (FxUtilTest.getComboBoxValue(comboBoxDescription) == null)
                    return true;
                return x.getHomework().getDescription().
                        equals(FxUtilTest.getComboBoxValue(comboBoxDescription));
            } catch (NullPointerException e) {
                return true;
            }

        };
        Predicate<Grade> p4 = x ->
        {
            try {
                LocalDate now = datePickerFrom.getValue();
                LocalDate start = LocalDate.of(2018, 10, 1);
                return x.getWeekGraded() >= toIntExact(ChronoUnit.WEEKS.between(start, now)) + 1;
            } catch (NullPointerException e) {
                return true;
            }
        };
        Predicate<Grade> p5 = x ->
        {
            try {
                LocalDate now = datePickerTo.getValue();
                LocalDate start = LocalDate.of(2018, 10, 1);
                return x.getWeekGraded() <= toIntExact(ChronoUnit.WEEKS.between(start, now)) + 1;
            } catch (NullPointerException e) {
                return true;
            }
        };
        List<Grade> l = (List<Grade>) (Object) service.findAll();
        updateDTO(l.stream()
                .filter(p3.and(p2).and(p1).and(p4).and(p5))
                .collect(Collectors.toList()));

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private List<Integer> getGroups() {
        List<Student> l = (List<Student>) (Object) SS.findAll();
        l.stream().filter(distinctByKey(Student::getGroup));
        List<Integer> li = l.stream()
                .map(x -> x.getGroup())
                .distinct()
                .collect(Collectors.toList());
        return li;
    }

    private void handleAdd() {
        try {
            Student st = (Student) SS.findOne(Integer.valueOf(FxUtilTest.getComboBoxValue(comboBoxStudents).split(",")[0]));
            if (st == null) throw new NullPointerException();
            Homework hw = SH.getByName((String) comboBoxHomework.getSelectionModel().getSelectedItem()).get();
            float value = isGradeValid();
            int weeks = service.getCurrentWeek() - Integer.parseInt((String) comboBoxAbsences.getSelectionModel().getSelectedItem());
            if (service.save(st.getID().toString() + "`" + hw.getID().toString() + "`" + Float.toString(value) + "`" + Integer.toString(weeks) + "`" +
                    textAreaFeedback.getText()) == null)
                makeGradeRight();


        } catch (NullPointerException e) {
            e.printStackTrace();
            showMessage(Alert.AlertType.ERROR, "Student is null!", "You must select a student");
        } catch (ValidationException e) {
            showMessage(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        } catch (NumberFormatException e) {
            showMessage(Alert.AlertType.ERROR, "Grade is not VALID!", "Please insert a grade from 1-10");
        } catch (NoSuchElementException e) {
            showMessage(Alert.AlertType.ERROR, "Select the Homework!", "You must select a Homework to be graded");
        } catch (GradeHomeworkException e) {
            showMessage(Alert.AlertType.ERROR, "This homework can't be graded", e.getMessage());
        }

    }

    private void makeGradeRight() throws GradeHomeworkException {
        List<Grade> l = (List<Grade>) (Object) service.findAll();
        int max = 0;
        Grade gr = (Grade) service.findOne(0);
        for (Grade el : l) {
            if (el.getID() >= max)
                max = el.getID();
            gr = el;
        }
        try {
            float penalities = (float) ((gr.getHomework().getDeadline() - gr.getWeekGraded()) * 2.5);
            int motivated=service.getCurrentWeek() - gr.getWeekGraded();
            gr.calculate();
            gr.setWeekGraded(service.getCurrentWeek());


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Grading!");
            float finalgrade=gr.getValue();
            if (finalgrade<1)
                finalgrade=1;
            alert.setHeaderText("Are you sure you want to grade This homework?");
            alert.setContentText("Student name: " + gr.getStudent().getName() + "\n" +
                    "Homework: " + gr.getHomework().getDescription() + "\n" +
                    "Penalities: " + Float.toString(penalities) + "\n" +
                    "Deadline: " + gr.getHomework().getDeadline() + "\n" +
                    "Motivated Absences" + Integer.toString(motivated) + "\n" +
                    "Final Grade: " + Float.toString(finalgrade) + "\n");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    service.updateEntity(gr);
                    showMessage(Alert.AlertType.INFORMATION, "Success!", "Grade Added Successfully");
                    service.writeGradesOfStudent(gr, gr.getFeedback());
                    updateDTO((List<Object>) (Object) service.findAll());
                } catch (ValidationException e) {
                    gr.setValue(1);
                    service.updateEntity(gr);
                    showMessage(Alert.AlertType.INFORMATION, "Success!", "Grade Added Successfully");
                    service.writeGradesOfStudent(gr, gr.getFeedback());
                    updateDTO((List<Object>) (Object) service.findAll());
                }
            } else {
                service.delete(gr.getID());
            }
        } catch (GradeHomeworkException e) {
            try {
                service.delete(gr.getID());
                throw e;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GradeDTO convertGradetoDTO(Grade grade) {
        return new GradeDTO(grade.getStudent().getName(), grade.getHomework().getDescription(), grade.getValue(), grade.getWeekGraded());
    }

    private void updateDTO(List<Object> lista) {
        List<GradeDTO> l = lista
                .stream()
                .map(x -> convertGradetoDTO((Grade) x)).collect(Collectors.toList());
        modelGradesDTO.setAll(l);
    }

    public void filterTable() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TableView.fxml"));
        String group = FxUtilTest.getComboBoxValue(comboBoxGroup);
        if (group != null) {

            List<Grade> lG = ((List<Grade>) (Object) service.findAll()).stream().filter(p2).collect(Collectors.toList());
            List<String> lSs = ((List<Student>) (Object) SS.findAll()).stream()
                    .filter(x -> Integer.toString(x.getGroup()).equals(group))
                    .map(Student::getName)
                    .collect(Collectors.toList());
            List<String> lHs = ((List<Homework>) (Object) SH.findAll()).stream().map(x -> x.getDescription()).collect(Collectors.toList());
            loader.setController(new tableController(lG, lHs, lSs, group));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage primaryStage = new Stage();
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.setTitle("GRADES");
            primaryStage.show();
        }
    }

    static void showMessage(Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    private float isGradeValid() {

        float value = Float.valueOf(textFieldGrade.getText());
        if (value > 10 || value < 1)
            throw new NumberFormatException();
        return value;

    }

    private void updateHomework() {

        List<Homework> mythings = (List<Homework>) (Object) SH.findAll();
        List<String> descList = mythings
                .stream()
                .map(Homework::getDescription)
                .collect(Collectors.toList());
        modelHomework.setAll(descList);
        comboBoxHomework.setItems(modelHomework);
    }

    void handleFeedback() {
        try {
            int deadline = SH.getByName(comboBoxHomework.getValue().toString()).get().getDeadline();
            int dif = deadline - service.getCurrentWeek() + Integer.parseInt(comboBoxAbsences.getValue().toString());
            if (dif < 0) {
                float val = (float) (-dif * 2.5);
                String message = "NOTA A FOST DIMINUATĂ CU" + Float.toString(val) + "DATORITA INTARZIERILOR";
                if (val > 5)
                    textAreaFeedback.setText("ATENTIE NU SE POATE PREDA ACEASTA TEMA");
                else
                    textAreaFeedback.setText(message);

            } else textAreaFeedback.setText("");
        } catch (NullPointerException e) {
        }
        ;
    }

    public void handleFilterStudent(String newValue) {

        if (!newValue.equals("")) {
            List<Student> nid = (List<Student>) (Object) SS.findAll();
            List<String> l = nid.stream().map(x -> x.getID() + "," + x.getName()).collect(Collectors.toList());
            modelStudents.setAll(l);
        }
        comboBoxStudents.hide();
        comboBoxStudents.show();

    }

    @Override
    public void update(Event event) {
        updateHomework();
        List<Object> l = new ArrayList<>(service.findAll());
        updateDTO(l);
        modelGroups.setAll(getGroups().stream().map(x -> x.toString()).collect(Collectors.toList()));
        List<Student> nid = (List<Student>) (Object) SS.findAll();
        List<String> ll = nid.stream().map(x -> x.getID() + "," + x.getName()).collect(Collectors.toList());
        modelNames.setAll(ll.stream().map(x -> x.split(",")[1]).collect(Collectors.toList()));


    }

    public Optional<List<String>> showGrader(String name, int deadline, int currentWeek) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Grade Dialog");
        dialog.setHeaderText("You need to set the grade and the motivated absences for: " + name);
        ButtonType gradeButtonType = new ButtonType("Grade", ButtonBar.ButtonData.OK_DONE);
        ButtonType skipButtonType = new ButtonType("Skip", ButtonBar.ButtonData.OTHER);
        ButtonType FinishButtonType = new ButtonType("Finish", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(gradeButtonType, ButtonType.FINISH, skipButtonType);
        Node gradeButton = dialog.getDialogPane().lookupButton(gradeButtonType);
        Node skipButton = dialog.getDialogPane().lookupButton(skipButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField grade = new TextField();
        grade.setPromptText("The grade");
        Label messageLabel = new Label("");
        TextArea feedback = new TextArea();

        gradeButton.setDisable(true);
        ComboBox<Integer> motivated = new ComboBox<>();
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i <= 14; i++)
            l.add(i);
        ObservableList<Integer> model = FXCollections.observableArrayList();
        model.setAll(l);
        motivated.setItems(model);
        motivated.setValue(0);
        grid.add(new Label("Grade:"), 0, 0);
        grid.add(grade, 1, 0);
        grid.add(new Label("Motivated"), 0, 1);
        grid.add(motivated, 1, 1);
        Predicate p = x -> {
            try {
                float valueGrade = Float.parseFloat(grade.getText().trim());
                if (deadline + motivated.getValue() - currentWeek >= -2) {
                    messageLabel.setText("");

                } else {
                    messageLabel.setText("You need to motivate at least " +
                            Integer.toString(-(deadline + motivated.getValue() - currentWeek) - 2) + "absences");
                    return true;
                }
                if (deadline + motivated.getValue() - currentWeek < 0)
                    feedback.setText("Nota va fi diminuata cu " + Double.toString(-(deadline + motivated.getValue() - currentWeek) * 2.5) + " datorita intarzierilor");
                else feedback.setText("");
                if (valueGrade < 1 || valueGrade > 10) {
                    messageLabel.setText("Grade must be between 1-10");
                    return true;
                }
                return false;
            } catch (NumberFormatException e) {
                messageLabel.setText("Grade Invalid format");
                return true;
            }
        };
        grade.textProperty().addListener((observable, oldValue, newValue) -> {
            gradeButton.setDisable(p.test(""));
        });
        System.out.println(deadline + motivated.getValue() - currentWeek);
        motivated.setOnAction(x -> {
            gradeButton.setDisable(p.test(""));
        });

        VBox vBox = new VBox(grid, feedback, messageLabel);
        dialog.getDialogPane().setContent(vBox);
        Platform.runLater(() -> grade.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == gradeButtonType) {
                return asList(grade.getText(), motivated.getValue().toString(), feedback.getText());
            }
            if (dialogButton == skipButtonType) {
                return new ArrayList<>();
            }
            return Collections.singletonList("0");
        });
        Optional<List<String>> result = dialog.showAndWait();
        return result;
    }


    public void handleAddGroup() {
        String gr = FxUtilTest.getComboBoxValue(comboBoxGroups);
        String homework = FxUtilTest.getComboBoxValue(comboBoxHomeworks);
        int k = 0;
        if (gr != null && homework != null) {
            List<Student> lg = ((List<Student>) (Object) SS.findAll()).stream()
                    .filter(x -> Integer.toString(x.getGroup()).equals(gr))
                    .collect(Collectors.toList());
            Homework h = ((List<Homework>) (Object) SH.findAll())
                    .stream()
                    .filter(x -> x.getDescription() == homework).findFirst().get();

            for (Student x : lg) {
                if (((List<Grade>) (Object) service.findAll()).stream()
                        .filter(y -> y.getHomework().getID() == h.getID() && y.getStudent().getID() == x.getID())
                        .findFirst().isEmpty()) {
                    k++;

                    List<String> l = showGrader(x.getName(), h.getDeadline(), service.getCurrentWeek()).get();
                    if (l.size() == 1) return;
                    if (!l.isEmpty()) {
                        try {
                            service.save(x.getID() + "`" + h.getID() + "`" + Float.parseFloat(l.get(0)) + "`" +
                                    (service.getCurrentWeek() - Integer.valueOf(l.get(1))) + "`" +
                                    l.get(2)
                            );
                        } catch (ValidationException e) {
                        }
                        try {
                            makeGradeRight();
                        } catch (GradeHomeworkException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
        if (k == 0)

            showMessage(Alert.AlertType.INFORMATION, "All students for this Homework have benn graded", "");
    }

}
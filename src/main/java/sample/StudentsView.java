package sample;

import domain.Student;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import util.Event;
import util.Observer;

public class StudentsView implements Observer {
StudentsController ctrl;
    public StudentsView(StudentsController ctrl) {
    this.ctrl=ctrl;
    initView();
    disableDependentButtons(true);
    }

    private BorderPane borderPane;
    TableView<Student> tableView=new TableView<>();
    
    Label labelId =new Label();
    TextField textFieldName=new TextField();
    TextField textFieldGroup=new TextField();
    TextField textFieldEmail=new TextField();
    TextField textFieldLabProf=new TextField();
    TextField searchStudent=new TextField();



    private Button buttonAdd=new Button("Add");
    private Button buttonUpdate=new Button("Update");
    private Button buttonDelete=new Button("Delete");
    private Button buttonClear = new Button("ClearAll");


    private void initView() {
        borderPane=new BorderPane();
        //top AnchorPane
        borderPane.setTop(initTop());
        //left
        borderPane.setLeft(initLeft());
        //center
        borderPane.setCenter(initCenter());

        Label copyright=new Label("@Filipoiu 100% not copied best project 2k18");
        AnchorPane a=new AnchorPane(copyright);
        AnchorPane.setBottomAnchor(copyright,50d);
        borderPane.setBottom(a);

    }

    public AnchorPane initCenter(){
        AnchorPane centerAnchorPane=new AnchorPane();
        GridPane gridPane=createGridPane();
        //anchor the gridpane
        centerAnchorPane.getChildren().add(gridPane);
        AnchorPane.setLeftAnchor(gridPane,20d);
        //AnchorPane.setTopAnchor(gridPane,20d);


        HBox buttonsGroups=createButtons();
        //anchor the buttons
        AnchorPane.setBottomAnchor(buttonsGroups,50d);
        AnchorPane.setLeftAnchor(buttonsGroups,20d);
        // buttonsGroups.setPadding(new Insets(30));
        centerAnchorPane.getChildren().add(buttonsGroups);
        return centerAnchorPane;
    }

    public HBox createButtons(){
        //init HBox Button


        HBox hb=new HBox(5, buttonAdd,buttonUpdate, buttonDelete,buttonClear);
        buttonAdd.setOnAction(ctrl::handleAddStudent);
        buttonUpdate.setOnAction(ctrl::handleUpdateMessage);
        buttonClear.setOnAction(ctrl::handleClearFields);
       buttonDelete.setOnAction(ctrl::handleDeleteMessage);
       searchStudent.setOnKeyTyped(ctrl::handleFilter);
       searchStudent.setOnAction(ctrl::deselectSearch);

       return hb;

    }

    private GridPane createGridPane() {
        {
            GridPane gridPaneMessageDetails=new GridPane();

            gridPaneMessageDetails.setHgap(5);
            gridPaneMessageDetails.setVgap(5);

            Label labelId=createLabel("Id:");
            Label labelDesc=createLabel("Name");
            Label labelFrom=createLabel("Group");
            Label labelTo=createLabel("Email");
            Label labelMessage=createLabel("LabProf");

            gridPaneMessageDetails.add(labelId,0,0);
            gridPaneMessageDetails.add(labelDesc,0,1);
            gridPaneMessageDetails.add(labelFrom,0,2);
            gridPaneMessageDetails.add(labelTo,0,3);
            //gridPaneMessageDetails.add(labelDate,0,4);
            gridPaneMessageDetails.add(labelMessage,0,4);

            gridPaneMessageDetails.add(this.labelId,1,0);
            gridPaneMessageDetails.add(textFieldName,1,1);
            gridPaneMessageDetails.add(textFieldGroup,1,2);
            gridPaneMessageDetails.add(textFieldEmail,1,3);
            gridPaneMessageDetails.add(textFieldLabProf,1,4);
            //gridPaneMessageDetails.add(textFieldDate,1,3);

            //gridPaneMessageDetails.addRow(5,new Label("dssndnmsndmnsmnd"));

            ColumnConstraints c1=new ColumnConstraints();
            c1.setPrefWidth(60d);

            ColumnConstraints c2=new ColumnConstraints();
            c2.setPrefWidth(250d);

            gridPaneMessageDetails.getColumnConstraints().addAll(c1,c2);

            return gridPaneMessageDetails;
        }
    }

    private Label createLabel(String s){
        Label l=new Label(s);
        l.setFont(new Font(15));
        l.setTextFill(Color.RED);
        l.setStyle("-fx-font-weight: bold");
        return l;
    }

    private AnchorPane initLeft() {
        AnchorPane leftAnchorPane=new AnchorPane();
        tableView=createMessageTaskTable();

        searchStudent.setPromptText("Filter by Name");
        VBox hb=new VBox(2,tableView,searchStudent);
        leftAnchorPane.getChildren().add(hb);
        AnchorPane.setLeftAnchor(tableView,20d);
        //AnchorPane.setBottomAnchor(tableView,100d);
        return  leftAnchorPane;
    }

    private TableView<Student> createMessageTaskTable() {
        TableColumn<Student,String> idColumn=new TableColumn<>("ID");
        TableColumn<Student,String> nameColumn=new TableColumn<>("Name");
        TableColumn<Student,String> groupColumn=new TableColumn<>("Group");
        TableColumn<Student,String> emailColumn=new TableColumn<>("Email");
        TableColumn<Student,String> labProfColumn=new TableColumn<>("LabProf");

        tableView.getColumns().addAll(idColumn, nameColumn,groupColumn,emailColumn,labProfColumn);

        //render data
        idColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("ID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("Name"));

        groupColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("Group"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("Email"));

        labProfColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("labTeacher"));

        //bind data
        tableView.setItems(ctrl.getModel());

        //add listener
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->{
                    ctrl.showStudentDetails(newValue);
                    if (labelId.getText()=="") {
                       disableDependentButtons(true);
                    }else disableDependentButtons(false);});
        //tableView.getSelectionModel().selectFirst();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tableView;
    }
    public void disableDependentButtons(boolean value){
        buttonUpdate.setDisable(value);
        buttonDelete.setDisable(value);
    }

    private AnchorPane initTop() {
        AnchorPane root=new AnchorPane();
        Label title=new Label("Students options");
        root.getChildren().add(title);
        return root;
    }


    public BorderPane getView() {
        return borderPane;
    }

    @Override
    public void update(Event event) {


        tableView.setItems(ctrl.getModel());
    }
}

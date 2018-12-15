package sample;

import domain.Grade;
import domain.Homework;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class tableController {
    @FXML
    TableView tableView;
    private List<String> lStudents;
    private String group;
    public tableController(List<Grade> lGrades, List<String> lHomework, List<String> lSs, String group) {
        this.lGrades = lGrades;
        this.lHomework = lHomework;
        lStudents=lSs;
        this.group=group;
    }

    public class Local{
        private int x=-1;
        public String getName() {
            return name;
        }

        public String getGrade(){
            x++;
            try {
                if (grades.get((x + 1) % grades.size()).toString().equals("0.0"))
                    return null;
                return grades.get((x + 1) % grades.size()).toString();
            }   catch(IndexOutOfBoundsException e){return null;}
        }
        public void setName(String name) {
            this.name = name;
        }

        public Local(List<Float> grades, String name) {
            this.grades = grades;
            this.name = name;
        }
        public int Gsize(){
            return lHomework.size();
        }

        List<Float>grades;
        private String name;
    }
    List<TableColumn> listColumns=new ArrayList<>();
    List<Local> l;
@FXML
public void initialize(){

    createColumn("Grupa"+group);
    lHomework.forEach(x->createColumn(x));
    listColumns.get(0).setCellValueFactory(new PropertyValueFactory<Local,String>("name"));
    ObservableList<Local> modelGroups = FXCollections.observableArrayList();
    l=makeList();
    setCellFactoryForGrades();
    modelGroups.setAll(l);
    tableView.setItems(modelGroups);
    }

    private void setCellFactoryForGrades() {


        for (int i = 1; i < l.get(0).Gsize() + 1; i++)
            listColumns.get(i).setCellValueFactory(new PropertyValueFactory<Local, String>("grade"));

}

    private void createColumn(String x) {
            listColumns.add(new TableColumn(x));
            tableView.getColumns().add(listColumns.get(listColumns.size()-1));
    }
    private List<Local> makeList() {

        List<Local> l = new ArrayList<>();
        List<String> listDistinctNames = lStudents
                .stream()
                .distinct()
                .collect(Collectors.toList());

        for (String x : listDistinctNames) {
            List<Float> grades = new ArrayList<>();
            for (int j=0;j<lHomework.size();j++)
                grades.add(Float.parseFloat("0"));
            List<String> lDV = lGrades.stream()
                    .filter(z -> z.getStudent().getName() == x)
                    .map(z -> z.getHomework().getDescription() + "`" + Float.toString(z.getValue()))
                    .collect(Collectors.toList());

            for (int j=0;j<lHomework.size();j++) {
                String str=lHomework.get(j);
                if (!lDV.stream().filter(i -> i.split("`")[0].matches(str)).findFirst().isEmpty())
                    grades.set(j+1,Float.parseFloat(lDV.stream().filter(i -> i.split("`")[0].matches(str)).findFirst().get().split("`")[1]));



            }
            l.add(new Local(grades,x));
        }
        return l;
    }

    private List<Grade> lGrades;
    private List<String> lHomework;
}

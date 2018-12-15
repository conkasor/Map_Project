package sample;

import com.sun.javafx.stage.EmbeddedWindow;
import domain.Grade;
import domain.Student;
import domain.Validator.GradeValidator;
import domain.Validator.HomeworkValidator;
import domain.Validator.StudentValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repository.*;
import service.GradesService;
import service.HomeworkService;
import service.StudentsService;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage=new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{
       //Parent root = FXMLLoader.load(getClass().getResource("/GradeViewFXML.fxml"));
//        StudentsService serv=new StudentsService(new StudentsRepoXML(new StudentValidator(),"students.xml") {
//        });
//
//        StudentsController sc=new StudentsController(serv);
//        StudentsView sv=new StudentsView(sc);
//        sc.setView(sv);
//        serv.addObserver(sc);
//
//        primaryStage.setScene(new Scene(sv.getView(),300,275));
//        primaryStage.setTitle(" World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
        HomeworkRepoXML repo = new HomeworkRepoXML(new HomeworkValidator(), "homework.xml");
        HomeworkService serv = new HomeworkService(repo);
        StudentsRepoXML repos = new StudentsRepoXML(new StudentValidator(), "students.xml");
        StudentsService sserv = new StudentsService(repos);
        GradesRepoXML grepo = new GradesRepoXML(new GradeValidator(), "grades.xml", repos, repo);
        GradesService service = new GradesService(grepo,sserv,serv);
        this.primaryStage=primaryStage;
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/GradeViewFXML.fxml"));
        loader.setController(new GradeController(service,sserv,serv,this));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setTitle("GRADES");
        primaryStage.show();

//
//        StudentsController sc=new StudentsController(sserv);
//        StudentsView sv=new StudentsView(sc);
//        sc.setView(sv);
//        serv.addObserver(sc);
//        Stage primaryStage1 = new Stage();
//        primaryStage1.setScene(new Scene(sv.getView(),700,650));
//        primaryStage1.setTitle("Students");
        //primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage1.show();
    }

        public void changeScene(String name,Object Controller){
            FXMLLoader loader= new FXMLLoader(getClass().getResource(name));
            loader.setController(Controller);
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.setTitle("GRADES");
            primaryStage.show();

        }

    public static void main(String[] args) {
        launch(args);
    }
}

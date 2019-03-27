package main;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.View;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception{

        Group root= new Group();
        Scene scene= new Scene(root ,780,500);

        Controller controller= new Controller();
        View view= new View();
        Model model= new Model();

        controller.link(model, view);

        root.getChildren().add(view);

        primaryStage.setTitle("SERVER");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public  static void main(String[] args){
        launch(args);
    }
}

package main;

import controller.Controller;
import controller.ControllerServer;
import controller.TCPServer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.View;
import view.ViewServer;

public class MainServer extends Application {

    public void start(Stage primaryStage) throws Exception{

        Group root= new Group();
        Scene scene= new Scene(root ,780,500);

        ControllerServer controller= new ControllerServer();
        ViewServer view= new ViewServer();
        Model model= new Model();

        controller.link(model, view);

        root.getChildren().add(view);

        primaryStage.setTitle("SERVER");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public  static void main(String[] args){
        TCPServer server=new TCPServer ();
        server.ServerStart ();
        launch(args);
    }
}

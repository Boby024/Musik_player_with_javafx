package main;

import controller.Controller;
import controller.ControllerClient;
import controller.TCPClient;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.View;
import view.ViewClient;

public class MainClient extends Application {

    public void start(Stage primaryStage) throws Exception{

        Group root= new Group();
        Scene scene= new Scene(root ,780,500);

        ControllerClient controller= new ControllerClient();
        ViewClient view= new ViewClient ();
        Model model= new Model();

        controller.link(model, view);

        root.getChildren().add(view);

        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public  static void main(String[] args){
        Thread clientAnmeldung =new Thread (new TCPClient());
        clientAnmeldung.start ();
        launch(args);
    }
}

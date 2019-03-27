package controller;

// LE BOUTTON ENTFERN DOIT ETRE RERAVAILLER CAR IL NE FONCTIONNE PAS


import IDGenerator.IDOverFlowException;
import IDGenerator.IdGenerator;
import interfaces.ISong;
import interfaces.SerializableStrategy;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import model.*;
import view.View;
import view.ViewClient;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

public class ControllerClient {

    public static Model model = new Model() ;
    public static ViewClient view = new ViewClient() ;
    private MediaPlayer mediaPlayer;
    private int indexPlayedMusic ;
    private SerializableStrategy strategy ;



    public void link(Model model ,ViewClient view){
        this.model = model ;
        this.view = view ;

        // view.getLibraryListview().setItems(model.getLibrary());

        // view.getPlaylistListview().setItems(model.getPlaylist());

        view.getImportSongs().setOnAction(event ->{
            try {
                final DirectoryChooser dialog = new DirectoryChooser();
                final File directory = dialog.showDialog(view.getImportSongs().getScene().getWindow());
                if (directory != null) {
                    File[] files = directory.listFiles();

                    assert files != null;
                    for (File f:files) {
                        //make a song with path and title

                        ISong song = new Song(cellFactory(f.getName()),"","",f.getPath(),IdGenerator.getNextID());
                        //add into the library
                        model.getLibrary().addSong(song);
                    }

                    updateLibrary();
                }
            }catch (NullPointerException| RemoteException | IDOverFlowException  e){
                e.printStackTrace();
            }
        });




        //zeigt parametter des ausgewählte Lied by Library
        view.getLibraryView().setOnMouseClicked(event -> {
            int index = view.getLibraryView().getSelectionModel().getSelectedIndex();
            if (0 <= index && index<= model.getLibrary().size() ) {

                view.getTitleTextField().setText(model.getLibrary().get(index).getTitle());
                view.getInterpretTextField().setText(model.getLibrary().get(index).getInterpret());
                view.getAlbumTextFiled().setText(model.getLibrary().get(index).getAlbum());
                view.getPlaylistView().getSelectionModel().clearSelection();

            }

        });

        view.getPlaylistView().setOnMouseClicked(event -> {
            int index = view.getPlaylistView().getSelectionModel().getSelectedIndex();
            if (index <= model.getPlaylist().size()  && index >0) {
                view.getTitleTextField().setText(model.getPlaylist().get(index).getTitle());
                view.getInterpretTextField().setText(model.getPlaylist().get(index).getInterpret());
                view.getAlbumTextFiled().setText(model.getPlaylist().get(index).getAlbum());
                view.getLibraryView().getSelectionModel().clearSelection();
            }
        });



        view.getPlayButton().setOnAction(event -> {
            try {
                TCPClient.remoteMethodes.play (view.getPlaylistView ().getSelectionModel ().getSelectedIndex ());
            }
            catch(RemoteException err) {
                err.printStackTrace ();
            }
                });


        view.getPauseButton().setOnAction(event -> {
            try {
                TCPClient.remoteMethodes.pause();
            }
            catch(RemoteException err) {
                err.printStackTrace ();
            }
        });

        view.getNextButton().setOnAction(event -> {

            try {
                TCPClient.remoteMethodes.nächste ();
            }
            catch(RemoteException err) {
                err.printStackTrace ();
            }
        });

        

        view.getCommitButton().setOnAction(event -> {
            String titel = view.getTitleTextField().getText();
            String interpret = view.getInterpretTextField().getText();
            String album = view.getAlbumTextFiled().getText();
            int index = -1;

            if(view.getPlaylistView().getSelectionModel().isEmpty()==false){
            index = view.getPlaylistView().getSelectionModel().getSelectedIndex();
            }
            else if (view.getLibraryView().getSelectionModel().isEmpty()==false) {
                index = view.getLibraryView().getSelectionModel().getSelectedIndex();
            }
            try {
                TCPClient.remoteMethodes.commit (titel,interpret,album,index);
            }
            catch(RemoteException err) {
                err.printStackTrace ();
            }
        });

        view.getAddButton().setOnAction(event -> {
            int index = view.getLibraryView().getSelectionModel().getSelectedIndex();
            try {
                TCPClient.remoteMethodes.add (index);
            }
            catch(RemoteException err) {
                err.printStackTrace ();
            }
        });


        view.getAddAllButton().setOnAction(event -> {
            try {
                TCPClient.remoteMethodes.addAll ();
            }
            catch(RemoteException err) {
                err.printStackTrace ();
            }
        });


        view.getDeleteAllButton().setOnAction(event -> {
            try {
                TCPClient.remoteMethodes.entfernAll ();
            }
            catch(RemoteException err) {
                err.printStackTrace ();
            }
        });

        view.getDeleteButton().setOnAction(event -> {
            try {
                TCPClient.remoteMethodes.entfernt (view.getPlaylistView().getSelectionModel().getSelectedIndex());
            }
            catch(RemoteException err) {
                err.printStackTrace ();
            }
        });


        view.getSaveButton().setOnAction(e->{  //Save event
            String strategieName = view.getComboBox().getSelectionModel().getSelectedItem();   //nimmt den ausgewählte Methode an
            try {
                TCPClient.remoteMethodes.savePlaylist (strategieName);
            }
            catch(RemoteException err) {
                err.printStackTrace ();
            }
        });

        view.getLoadButton().setOnAction(e->{
            String strategieName = view.getComboBox().getSelectionModel().getSelectedItem();  //nimmt den ausgewählte Methode an
            try {
                TCPClient.remoteMethodes.loadPlaylist (strategieName);
            }
            catch(RemoteException err) {
                err.printStackTrace ();
            }
        });
/*
        view.getDeleteButton().setOnAction(e->{
            String strategieName = view.getComboBox().getSelectionModel().getSelectedItem();
            if (strategieName.equals("JDBC")) {
                SerializableStrategy strategy1 = new JDBCStrategy();
                ((JDBCStrategy) strategy1).deleteStore(1);
                ((JDBCStrategy) strategy1).deleteStore(2);

            }
        });
*/
    }

    // Strategie Pattern
    private SerializableStrategy strategiePattern(String str ){
        SerializableStrategy strategy ;
        switch (str){
            case "XML" : strategy = new XMLStrategie(); break;
            case "JDBC" : strategy = new JDBCStrategy();break;
            default:  strategy = new BinaryStrategy(); break;

        }
        return strategy;
    }



    // private int indexeLib = 0;            A IMPLEMENTER
    public void updateLibrary(){
        view.getLibraryObserve().clear();

        for (int i = 0; i <model.getLibrary().size();++i){

            //   take the title of each music to the observe

            view.getLibraryObserve().add(i +" ) "+model.getLibrary().get(i).getTitle());
        }
        //show
        view.getLibraryView().setItems(view.getLibraryObserve());
    }


    public void updatePlaylist(){
        view.getPlaylistObserve().clear();
        for (int i = 0; i <model.getPlaylist().size() ; i++) {
            //   take the title of each music to the observe

            view.getPlaylistObserve().add(i + ") "+ model.getPlaylist().get(i).getTitle());
        }
        //show
        view.getPlaylistView().setItems(view.getPlaylistObserve());
    }

    private String cellFactory (String s){      // delete the extension name .mp3 from the title

        String title = s.substring(0,s.indexOf(".")) ;
        return title ;
    }


 //Gib zuruck aktuelleZeit für Server
    public  String getAktuelleZeitServer(){
        String timeSekond ;
        String timeMinute ;
        double zeit = mediaPlayer.getCurrentTime().toSeconds();
        int sekond =(int) mediaPlayer.getCurrentTime().toSeconds()%60; // Anzahl der Sekunde
        int minute =(int) mediaPlayer.getCurrentTime().toSeconds()/60;  // Anzahl der Minute

        if (zeit >=10){
            timeSekond = String.valueOf(sekond);
        }else {
            timeSekond = "0"+sekond;
        }

        if (zeit >=60){
            timeMinute = String.valueOf(minute);
        }else {
            timeMinute = "0"+minute;
        }

        String aktuelZeit = timeMinute+":"+timeSekond;
        return aktuelZeit;
    }

    private void updateTime(){ // methode zur Aktualisierung von spielZeit
        //aktuelleZeitListener = (observableValue, oldValue, newValue) -> viewServer.getTimeLabel().setText(getAktuelleZeitServer());
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> view.getTimeLabel().setText(getAktuelleZeitServer()));
    }
    //Gib zuruck aktuelleZeit für Gäste
    public String getAktuelleZeit()throws RemoteException{
        String aktuelZeit = "00:00";
        if (mediaPlayer != null) {
            String timeSekond;
            String timeMinute;
            double zeit = mediaPlayer.getCurrentTime().toSeconds();
            int sekond = (int) mediaPlayer.getCurrentTime().toSeconds() % 60; // Anzahl der Sekunde
            int minute = (int) mediaPlayer.getCurrentTime().toSeconds() / 60;  // Anzahl der Minute

            if (zeit >= 10) {
                timeSekond = String.valueOf(sekond);
            } else {
                timeSekond = "0" + sekond;
            }

            if (zeit >= 60) {
                timeMinute = String.valueOf(minute);
            } else {
                timeMinute = "0" + minute;
            }

            aktuelZeit = timeMinute+":"+timeSekond;
        }

        System.out.println(aktuelZeit);
        return aktuelZeit;
    }
    
}

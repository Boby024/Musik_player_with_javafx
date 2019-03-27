package controller;

import IDGenerator.IDOverFlowException;
import IDGenerator.IdGenerator;
import interfaces.IRemoteMethodesClient;
import interfaces.ISong;
import javafx.application.Platform;
import model.BinaryStrategy;
import model.JDBCStrategy;
import model.XMLStrategie;
import interfaces.SerializableStrategy;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import model.Model;
import model.Playlist;
import model.Song;
import view.ViewServer;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ControllerServer {

    public static Model model = new Model() ;
    private static ViewServer viewServer = new ViewServer() ;
    private static MediaPlayer mediaPlayer;
    private static int indexPlayedMusic ;
    private static SerializableStrategy strategy ;



    public void link(Model model , ViewServer viewServer) {
        this.model = model;
        this.viewServer = viewServer;

        //zeigt parametter des ausgewählte Lied by Library
        viewServer.getLibraryView().setOnMouseClicked(event -> {
            int index = viewServer.getLibraryView().getSelectionModel().getSelectedIndex();
            if (0 <= index && index<= model.getLibrary().size() ) {

                viewServer.getTitleTextField().setText(model.getLibrary().get(index).getTitle());
                viewServer.getInterpretTextField().setText(model.getLibrary().get(index).getInterpret());
                viewServer.getAlbumTextFiled().setText(model.getLibrary().get(index).getAlbum());
                viewServer.getPlaylistView().getSelectionModel().clearSelection();

            }

        });

        viewServer.getPlaylistView().setOnMouseClicked(event -> {
            int index = viewServer.getPlaylistView().getSelectionModel().getSelectedIndex();
            if (index <= model.getPlaylist().size()  && index >0) {
                viewServer.getTitleTextField().setText(model.getPlaylist().get(index).getTitle());
                viewServer.getInterpretTextField().setText(model.getPlaylist().get(index).getInterpret());
                viewServer.getAlbumTextFiled().setText(model.getPlaylist().get(index).getAlbum());
                viewServer.getLibraryView().getSelectionModel().clearSelection();
            }
        });
    }
        public static void getImportSongs() {
        /*    try {
                final DirectoryChooser dialog = new DirectoryChooser();
                final File directory = dialog.showDialog(viewServer.getImportSongs().getScene().getWindow());
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
            }*/
        }

       public static void play(int musicIndex) {

            if (mediaPlayer == null|| mediaPlayer.getStatus().equals(MediaPlayer.Status.DISPOSED)){

                indexPlayedMusic = musicIndex;
                Media media = new Media(new File(model.getPlaylist().get(indexPlayedMusic).getPath()).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                mediaPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        indexPlayedMusic = (indexPlayedMusic+1)%model.getPlaylist().size();
                        viewServer.getPlaylistView().getSelectionModel().select(indexPlayedMusic);  //select((indexPlayedMusic + 1)%model.getPlaylist().size());

                        viewServer.getTitleTextField().setText(model.getPlaylist().get(indexPlayedMusic).getTitle());
                        viewServer.getInterpretTextField().setText(model.getPlaylist().get(indexPlayedMusic).getInterpret());
                        viewServer.getAlbumTextFiled().setText(model.getPlaylist().get(indexPlayedMusic).getAlbum());

                        mediaPlayer.stop();
                        mediaPlayer.dispose();

                        Media media = new Media(new File(model.getPlaylist().get(indexPlayedMusic).getPath()).toURI().toString());
                        mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.play();
                    }
                });

            }

            if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)){
                mediaPlayer.play();

            }

            if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
                mediaPlayer.stop();
                mediaPlayer.dispose();
                int index = viewServer.getPlaylistView().getSelectionModel().getSelectedIndex();
                Media media = new Media(new File(model.getPlaylist().get(index).getPath()).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();

            }

        }

       public static void pause() {
            if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){

                mediaPlayer.pause();
            }
        }

        public static void next() {

            if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){

                indexPlayedMusic = (indexPlayedMusic+1)%model.getPlaylist().size();
                viewServer.getPlaylistView().getSelectionModel().select(indexPlayedMusic);  //select((indexPlayedMusic + 1)%model.getPlaylist().size());

                viewServer.getTitleTextField().setText(model.getPlaylist().get(indexPlayedMusic).getTitle());
                viewServer.getInterpretTextField().setText(model.getPlaylist().get(indexPlayedMusic).getInterpret());
                viewServer.getAlbumTextFiled().setText(model.getPlaylist().get(indexPlayedMusic).getAlbum());

                mediaPlayer.stop();
                mediaPlayer.dispose();

                Media media = new Media(new File(model.getPlaylist().get(indexPlayedMusic).getPath()).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();

            }

        }

        public static void commit(String titel, String interpret, String album, int index) {

                    ISong s = model.getLibrary().get(index);
                    s.setTitle(titel);
                    s.setAlbum(album);
                    s.setInterpret(interpret);
                    model.getLibrary().set(index, s);
            updateLibrary();
            updatePlaylist();
        }

        public static void add(int index) {
            if (index >= 0 && index <= model.getLibrary().size()){
                model.getPlaylist().add( model.getLibrary().get(index));
                updatePlaylist();
            }
        }

        public static void addAll() {
            model.setPlaylist(model.getLibrary());
            updatePlaylist();
        }

        public static void deleteAll() {

            try {
                model.getPlaylist().clearPlaylist();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            updatePlaylist();

        }

       public static void delete(int index) {
                ISong song = model.getPlaylist().get(index);
                model.getPlaylist().remove(song);
            updatePlaylist();
        }



       public static void save (String strategieName) {  //Save event
           if (null!= strategieName){
                strategy = strategiePattern(strategieName);  // herstellen wir die entsprechende Strategie

                try {
                    //schreibe Library
                    strategy.openWritableLibrary();
                    strategy.writeLibrary(model.getLibrary());
                    strategy.closeWritableLibrary();

                    //schreibe Playlist
                    strategy.openWritablePlaylist();
                    strategy.writePlaylist(model.getPlaylist());
                    strategy.closeWritablePlaylist();

                    System.out.println("done"); // nur einen test zu prüfen

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }else {
                System.out.println("Wählen Sie bitte erstens eine Strategie aus !!"); // nur einen test zu prüfen
            }
        }

        public static void load(String strategieName) {
            if (null!= strategieName) {
                strategy = strategiePattern(strategieName); // herstellen wir die entsprechende Strategie

                try {
                    //lesen Library
                    strategy.openReadableLibrary();
                    model.setLibrary((Playlist) strategy.readLibrary());


                    //lesen Playlist
                    strategy.openReadablePlaylist();
                    model.setPlaylist((Playlist) strategy.readPlaylist());


                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();

                    System.out.println("Sie haben nichts zuvor gespeichert");
                }finally {
                    strategy.closeReadableLibrary();
                    strategy.closeReadablePlaylist();
                }
            }else {
                System.out.print("Wählen Sie bitte  zuvor eine Strategie aus !!");
            }

            updateLibrary();
            updatePlaylist();
        }
            // dieses Event wird  aktiviert , wenn man die JDBC serialisierung macht
       public static void deleteDB () {
            String strategieName = viewServer.getComboBox().getSelectionModel().getSelectedItem();
            if (strategieName.equals("JDBC")) {
                SerializableStrategy strategy1 = new JDBCStrategy();
                ((JDBCStrategy) strategy1).deleteStore(1);
                ((JDBCStrategy) strategy1).deleteStore(2);

            }
        }



    // Strategie Pattern
    private static SerializableStrategy strategiePattern(String str ){
        SerializableStrategy strategy ;
        switch (str){
            case "XML" : strategy = new XMLStrategie(); break;
            case "JDBC" : strategy = new JDBCStrategy();break;
            default:  strategy = new BinaryStrategy(); break;

        }
        return strategy;
    }

    public static void update() throws RemoteException {
        ArrayList<IRemoteMethodesClient> clients = model.getClientList ();
        for (IRemoteMethodesClient c : clients) {
            try {
                c.update (ControllerServer.model.getLibrary (), ControllerServer.model.getPlaylist ());
            } catch (NotBoundException err) {
                err.printStackTrace ();
            }
        }
    }


    // private int indexeLib = 0;            A IMPLEMENTER
    private static void updateLibrary(){
        viewServer.getLibraryObserve().clear();

        for (int i = 0; i <model.getLibrary().size();++i){

            //   take the title of each music to the observe

            viewServer.getLibraryObserve().add(i +" ) "+model.getLibrary().get(i).getTitle());
        }
        //show
        viewServer.getLibraryView().setItems(viewServer.getLibraryObserve());
    }


    private static void updatePlaylist(){
        viewServer.getPlaylistObserve().clear();
        for (int i = 0; i <model.getPlaylist().size() ; i++) {
            //   take the title of each music to the observe

            viewServer.getPlaylistObserve().add(i + ") "+ model.getPlaylist().get(i).getTitle());
        }
        //show
        viewServer.getPlaylistView().setItems(viewServer.getPlaylistObserve());
    }

    private static String cellFactory (String s){      // delete the extension name .mp3 from the title

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

        if (sekond >=10){
            timeSekond = String.valueOf(sekond);
        }else {
            timeSekond = "0"+sekond;
        }

        if (minute >=10){
            timeMinute = String.valueOf(minute);
        }else {
            timeMinute = "0"+minute;
        }

        String aktuelleZeit = timeMinute+":"+timeSekond;
        return aktuelleZeit;
    }

    private void updateTime(){ // methode zur Aktualisierung von spielZeit
        //aktuelleZeitListener = (observableValue, oldValue, newValue) -> viewServer.getTimeLabel().setText(getAktuelleZeitServer());
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> viewServer.getTimeLabel().setText(getAktuelleZeitServer()));
    }
    //Gib zuruck aktuelleZeit für Gäste
    public static String getAktuelleZeit()throws RemoteException{
        String aktuelZeit = "00:00";
        if (mediaPlayer != null) {
            String timeSekond;
            String timeMinute;
            double zeit = mediaPlayer.getCurrentTime().toSeconds();
            int sekond = (int) mediaPlayer.getCurrentTime().toSeconds() % 60; // Anzahl der Sekunde
            int minute = (int) mediaPlayer.getCurrentTime().toSeconds() / 60;  // Anzahl der Minute

            if (sekond >= 10) {
                timeSekond = String.valueOf(sekond);
            } else {
                timeSekond = "0" + sekond;
            }

            if (minute >= 10) {
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
    /*
     // update Lieder in Library Listview
    private static void updateLibrary() {
        viewServer.getLibraryObserve().clear();

        for (int i = 0; i < model.getLibrary().size(); ++i){

            //   take the title of each music to the observe
            viewServer.getLibraryObserve().add(i +" ) "+ model.getLibrary().get(i).getTitle());
        }
        //show
        Platform.runLater(() -> {
            viewServer.getLibraryListview().setItems(viewServer.getLibraryObserve());

        });
    }

    // update Lieder in Playlist Listview
    private static void updatePlaylist() {
        viewServer.getPlaylistObserve().clear();
        for (int i = 0; i < model.getPlaylist().size() ; i++) {
            //   take the title of each music to the observe

            viewServer.getPlaylistObserve().add(i + ") "+ model.getPlaylist().get(i).getTitle());
        }

        //show
        Platform.runLater(() -> {
            viewServer.getPlaylistListview().setItems(viewServer.getPlaylistObserve());

        });
    }

    private String cellFactory (String s) throws RemoteException{      // delete the extension name .mp3 from the title

        String title = s.substring(0,s.indexOf(".")) ;
        return title ;
    }*/
/*
    private void importMusic() throws RemoteException {
        //Take All Folder path
        try {
            File file = new File("C:\\Users\\samod\\Desktop\\Mp3-music");
            File[] files = file.listFiles(); //classified into a table

            assert files != null;
            for (File f : files) {
                //make a song with path and title

                interfaces.ISong song = null;
                try {
                    song = new Song(cellFactory(f.getName()), "", "", f.getPath(), IdGenerator.getNextID());
                } catch (IDOverFlowException e) {
                    e.printStackTrace();
                }

                //add into the library
                model.getLibrary().addSong(song);

            }

            updateLibrary();

        } catch (NullPointerException | RemoteException e) {
            e.printStackTrace();
        }
    }*/

    //RMI methode
    /*
    @Override
    public synchronized void play(int index) throws RemoteException {
        if (mediaPlayer == null|| mediaPlayer.getStatus().equals(MediaPlayer.Status.DISPOSED)){

            indexPlayedMusic = index;
            Media media = new Media(new File(modelServer.getPlaylist().get(indexPlayedMusic).getPath()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            updateTime();

            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    indexPlayedMusic = (indexPlayedMusic+1)% modelServer.getPlaylist().size();
                    viewServer.getPlaylistListview().getSelectionModel().select(indexPlayedMusic);  //select((indexPlayedMusic + 1)%modelServer.getPlaylist().size());

                    viewServer.getTitelTextfield().setText(modelServer.getPlaylist().get(indexPlayedMusic).getTitle());
                    viewServer.getInterpretTextfield().setText(modelServer.getPlaylist().get(indexPlayedMusic).getInterpret());
                    viewServer.getAlbumTextfield().setText(modelServer.getPlaylist().get(indexPlayedMusic).getAlbum());

                    mediaPlayer.stop();
                    mediaPlayer.dispose();

                    Media media = new Media(new File(modelServer.getPlaylist().get(indexPlayedMusic).getPath()).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                    updateTime();
                }
            });

        }

        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)){
            mediaPlayer.play();
            updateTime();

        }

        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
            indexPlayedMusic = index;
            mediaPlayer.stop();
            mediaPlayer.dispose();
            Media media = new Media(new File(modelServer.getPlaylist().get(index).getPath()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            updateTime();

        }
    }*/
/*
    @Override
    public synchronized void nächste(int index) throws RemoteException {
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){

            indexPlayedMusic = (index+1)% modelServer.getPlaylist().size();
            viewServer.getPlaylistListview().getSelectionModel().select(indexPlayedMusic);  //select((indexPlayedMusic + 1)%modelServer.getPlaylist().size());

            getMetadaten(indexPlayedMusic,modelServer.getPlaylist());

            mediaPlayer.stop();
            mediaPlayer.dispose();

            Media media = new Media(new File(modelServer.getPlaylist().get(indexPlayedMusic).getPath()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            updateTime();

        }

    }*/
/*
    @Override
    public synchronized void pause() throws RemoteException {
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){

            mediaPlayer.pause();
        }

    }*/
/*
    @Override
    public void commit(String title, String interpret, String album, long index) throws RemoteException {

        if (index == -1) {

            modelServer.getLibrary().get((int)index).setTitle(title);
            modelServer.getLibrary().get((int)index).setAlbum(album);
            modelServer.getLibrary().get((int)index).setInterpret(interpret);


        } else {
            try {
                index = viewServer.getPlaylistListview().getSelectionModel().getSelectedIndex();
                long iD = modelServer.getPlaylist().get((int)index).getId();

                interfaces.Song s = modelServer.getLibrary().findSongByID(iD);
                s.setTitle(title);
                s.setAlbum(album);
                s.setInterpret(interpret);
                modelServer.getLibrary().set((int)index, s);


            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        try {
            updateLibrary();
            updatePlaylist();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }*/
/*
    @Override
    public synchronized void add(int index ) throws RemoteException {
        if (index >= 0 && index <= modelServer.getLibrary().size()){
            modelServer.getPlaylist().add( modelServer.getLibrary().get(index));
            try {
                updatePlaylist();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public synchronized void addAll(Playlist playlist,int i) throws RemoteException {

        modelServer.setPlaylist(playlist);
        try {
            updatePlaylist();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void entfernt(int index) throws RemoteException {
        if (index > -1){
            interfaces.Song song = modelServer.getPlaylist().get(viewServer.getLibraryListview().getSelectionModel().getSelectedIndex());
            modelServer.getPlaylist().remove(song);
        }

        try {
            updatePlaylist();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void entfernAll() throws RemoteException {
        try {
            modelServer.getPlaylist().clearPlaylist();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            updatePlaylist();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void deleteDataBank() throws RemoteException {
        String strategieName = viewServer.getComboBox().getSelectionModel().getSelectedItem();
        if (strategieName.equals("JDBC")) {
            SerializableStrategy strategy1 = new JDBCStrategy();
            ((JDBCStrategy) strategy1).deleteStore(1);
            ((JDBCStrategy) strategy1).deleteStore(2);
        }

    }

    @Override
    public synchronized void playlistUpdate() throws RemoteException, NotBoundException {
        ArrayList<String> clients = new ArrayList<>(Arrays.asList(registry.list()));
        clients.remove(tcpServer.getNameServer());
        for (String s : clients) {
            RMIInterface controllerClient = (RMIInterface) registry.lookup(s);
            controllerClient.addAll(modelServer.getPlaylist(),2); //2 für "Es geht um Playlist"
        }

    }

    @Override
    public synchronized void libraryUpdate() throws RemoteException, NotBoundException {

        ArrayList<String> clients = new ArrayList<>(Arrays.asList(registry.list()));
        clients.remove(tcpServer.getNameServer());
        for (String s : clients) {
            RMIInterface controllerClient = (RMIInterface) registry.lookup(s);
            controllerClient.addAll(modelServer.getLibrary(),1); //1 für "Es geht um Library"
        }

        //ArrayList<String> rmiClientList =  TCPServer.

    }

    @Override
    public synchronized void savePlaylist() throws RemoteException {
        String strategieName = viewServer.getComboBox().getSelectionModel().getSelectedItem();   //nimmt den ausgewählte Methode an
        if (null!= strategieName){
            try {
                strategy = strategiePattern(strategieName);  // herstellen wir die entsprechende Strategie
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            try {
                //schreibe Library
                strategy.openWritableLibrary();
                strategy.writeLibrary(modelServer.getLibrary());

                //schreibe Playlist
                strategy.openWritablePlaylist();
                strategy.writePlaylist(modelServer.getPlaylist());

                System.out.println("Geschafft"); // nur einen test zu prüfen

            } catch (IOException e1) {
                e1.printStackTrace();
            }finally {
                strategy.closeWritableLibrary();
                strategy.closeWritablePlaylist();
            }

        }else {
            System.out.println("Wählen Sie zuvor eine Strategie bitte !!"); // nur einen test zu prüfen
        }

    }

    @Override
    public synchronized void loadPlaylist() throws RemoteException {
        String strategieName = viewServer.getComboBox().getSelectionModel().getSelectedItem();  //nimmt den ausgewählte Methode an
        if (null!= strategieName) {
            try {
                strategy = strategiePattern(strategieName); // herstellen wir die entsprechende Strategie
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            try {
                //lesen Library
                strategy.openReadableLibrary();
                modelServer.setLibrary((Playlist) strategy.readLibrary());

                //lesen Playlist
                strategy.openReadablePlaylist();
                modelServer.setPlaylist((Playlist) strategy.readPlaylist());

            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();

                System.out.println("Sie haben nichts zuvor gespeichert");
            }finally {
                strategy.closeReadableLibrary();
                strategy.closeReadablePlaylist();
            }
        }else {
            System.out.print("Wählen Sie zuvor eine Strategie bitte !!");
        }

        try {
            updateLibrary();
            updatePlaylist();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }*/
/*
    private void getMetadaten(int index, model.Playlist p) {
        if (0 <= index && index<= p.size()){
            viewServer.getTitelTextfield().setText(p.get(index).getTitle());
            viewServer.getInterpretTextfield().setText(p.get(index).getInterpret());
            viewServer.getAlbumTextfield().setText(p.get(index).getAlbum());
        }

    }

}*/

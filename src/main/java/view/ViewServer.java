package view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Song;


/*The following class (View) displays the Model data and will send user's actions to the Controller. */
public class ViewServer extends BorderPane{

    //First, we present the buttons
    ComboBox<String> comboBox= new ComboBox<>(); //ComboBox for a dropdown of buttons that surpass a certain limit
    ObservableList<String> observeCombo=FXCollections.observableArrayList("Binary","XML","JDBC");

    // our three Labels come next
    Label titleLabel= new Label("Title");
    Label albumLabel= new Label("Album");
    Label interpretLabel= new Label("Interpret");
    Label timeLabel= new Label("Time");

    // Adding the textFields to the three Labels
    TextField titleTextField= new TextField();
    TextField albumTextFiled= new TextField();
    TextField interpretTextField= new TextField();


    //ListView for display of our Songs
    ListView<Song> libraryView= new ListView<>();
    ListView<Song> playlistView= new ListView<>();

    //ObservableList to tract manipulations and changes in our music lists
    private ObservableList<Song>  libraryObserve= FXCollections.observableArrayList();
    private ObservableList<Song>  playlistObserve= FXCollections.observableArrayList();

    //H- and VBoxes our UI layout
    private HBox hBox1 = new HBox();
    private HBox hBox2 = new HBox();
    private HBox hBox3 = new HBox();
    private HBox hBox4 = new HBox();
    private VBox vBox1 = new VBox();
    private VBox vBox2 = new VBox();
    private VBox vBox5 = new VBox();


    //set up the H- and VBoxes inside this class constructor
    public ViewServer(){
        hBox1.getChildren().addAll(); //these buttons are horizontally separated from each other
        hBox1.setSpacing(50);
        comboBox.prefWidth(300); //size of dropdown button
        comboBox.setPromptText("Funktionalität");
        comboBox.setItems(observeCombo);
        //top
        setTop(hBox1);//topmost buttons (the above three)

        //Left
        //  libraryView.setMinSize(150,400);
        setLeft(libraryView);

        //center
        //  playlistView.setMaxSize(200,500);
        setCenter(playlistView);

        //hBox10.getChildren().addAll(libraryView,playlistView);
        //hBox10.setSpacing(3);


        //Right
        vBox2.getChildren().addAll(titleLabel, titleTextField, albumLabel, albumTextFiled, interpretLabel, timeLabel, interpretTextField);
        hBox2.getChildren().addAll();
        hBox2.setSpacing(5);
        hBox3.getChildren().addAll();
        hBox3.setSpacing(5);
        hBox4.getChildren().addAll();

        vBox5.getChildren().addAll();

        //vBox5.setSpacing(15);
        vBox1.getChildren().addAll(vBox2, hBox2, hBox3, hBox4);
        vBox1.setSpacing(20);
        setRight(vBox1);  //we set it to the right of the screen

        //bottom
        setBottom(vBox5);

        autosize(); //we use autosize() method for our buttons to fit its text

    }

//we write getter and setter methods for our class members, first for the buttons


    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public  void setComboBox(ComboBox<String> comboBox) {
        this.comboBox = comboBox;
    }


    //getter setter methods for the labels


    public Label getTitleLabel() {
        return titleLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public void setTitleLabel(Label titleLabel) {
        this.titleLabel = titleLabel;
    }

    public Label getAlbumLabel() {
        return albumLabel;
    }

    public void setAlbumLabel(Label albumLabel) {
        this.albumLabel = albumLabel;
    }

    public Label getInterpretLabel() {
        return interpretLabel;
    }

    public void setInterpretLabel(Label interpretLabel) {
        this.interpretLabel = interpretLabel;
    }

    //getter setter methods for the three textFields


    public TextField getTitleTextField() {
        return titleTextField;
    }

    public void setTitleTextField(TextField titleTextField) {
        this.titleTextField = titleTextField;
    }

    public TextField getAlbumTextFiled() {
        return albumTextFiled;
    }

    public void setAlbumTextFiled(TextField albumTextFiled) {
        this.albumTextFiled = albumTextFiled;
    }

    public TextField getInterpretTextField() {
        return interpretTextField;
    }

    public void setInterpretTextField(TextField interpretTextField) {
        this.interpretTextField = interpretTextField;
    }

    //getter setter for the listView


    public ListView getLibraryView() {
        return libraryView;
    }

    public void setLibraryView(ListView libraryView) {
        this.libraryView = libraryView;
    }

    public ListView getPlaylistView() {
        return playlistView;
    }

    public void setPlaylistView(ListView<Song> playlistView) {
        this.playlistView = playlistView;
    }

//getter setter for the observableList


    public ObservableList getLibraryObserve() {
        return libraryObserve;
    }

    public void setLibraryObserve(ObservableList libraryObserve) {
        this.libraryObserve = libraryObserve;
    }

    public ObservableList getPlaylistObserve() {
        return playlistObserve;
    }

    public void setPlaylistObserve(ObservableList playlistObserve) {
        this.playlistObserve = playlistObserve;
    }
}

package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;


import java.io.*;


public class Song implements interfaces.ISong,Serializable,Externalizable {

    private static final long serialVersionUID = 700L;

    private long id = 0 ;

    private SimpleStringProperty titel = new SimpleStringProperty();

    private SimpleStringProperty interpret = new SimpleStringProperty();

    private SimpleStringProperty album = new SimpleStringProperty();

    private SimpleStringProperty pfad = new SimpleStringProperty();

    public Song(){

    }

    public Song(String title,String album,String interpret,long id){
        setTitle(title);
        setAlbum(album);
        setInterpret(interpret);
        setId(id);
    }

    public Song(String title,String album,String interpret,String pfad,long id){
        setTitle(title);
        setAlbum(album);
        setInterpret(interpret);
        setPath(pfad);
        setId(id);
    }

    @Override
    public String getAlbum() {
        return this.album.getValue();
    }

    @Override
    public void setAlbum(String album) {
        this.album.setValue(album);
    }

    @Override
    public String getInterpret() {
        return interpret.getValue();
    }

    @Override
    public void setInterpret(String interpret) {
        this.interpret.setValue(interpret);
    }

    @Override
    public String getPath() {
        return pfad.getValue();
    }

    @Override
    public void setPath(String path) {
        this.pfad.setValue(path);
    }

    @Override
    public String getTitle() {
        return titel.getValue();
    }

    @Override
    public void setTitle(String title) {
        this.titel.setValue(title);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id ;
    }

    @Override
    public ObservableValue<String> pathProperty() {
        return pfad;
    }

    @Override
    public ObservableValue<String> albumProperty() {
        return album;
    }

    @Override
    public ObservableValue<String> interpretProperty() {
        return interpret;
    }

    @Override
    public ObservableValue<String> titleProperty() {
        return titel;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(titel.getValue());
        out.writeObject(interpret.getValue());
        out.writeObject(album.getValue());
        out.writeObject(pfad.getValue());
        out.writeObject(id);

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        String str = (String) in.readObject();
        if (null==str){
            titel.setValue("");
        }else {
            titel.setValue(str);
        }
        str = (String) in.readObject();
        if (null==str){
            interpret.setValue("");
        }else {
            interpret.setValue(str);
        }
        str = (String) in.readObject();
        if (null==str){
            album.setValue("");
        }else {
            album.setValue(str);
        }
        str = (String) in.readObject();
        if (null==str){
            pfad.setValue("");
        }else {
            pfad.setValue(str);
        }
        id = (long) in.readObject();
    }
}

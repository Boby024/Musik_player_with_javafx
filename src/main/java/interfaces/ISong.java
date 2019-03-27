package interfaces;

import javafx.beans.value.ObservableValue;

import java.io.Serializable;

//Dieses Interface darf auf keinen Fall geändert werden!
public interface ISong {
    String getAlbum() ;

    void setAlbum(String album) ;

    String getInterpret() ;

    void setInterpret(String interpret) ;

    String getPath() ;

    void setPath(String path) ;

    String getTitle() ;

    void setTitle(String title) ;

    long getId();

    void setId(long id);

    ObservableValue<String> pathProperty();
    ObservableValue<String> albumProperty();
    ObservableValue<String> interpretProperty();
    ObservableValue<String> titleProperty();

    String toString();
}

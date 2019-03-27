package interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


//Dieses Interface darf auf keinen Fall geändert werden!
public interface IPlaylist extends Remote, Serializable, Iterable<ISong>{
    boolean addSong(ISong s) throws RemoteException;
    boolean deleteSong(ISong s) throws RemoteException;
    boolean deleteSongByID(long id) throws RemoteException;
    void setList(ArrayList<ISong> s) throws RemoteException;
    ArrayList<ISong> getList() throws RemoteException;
    void clearPlaylist() throws RemoteException;
    int sizeOfPlaylist() throws RemoteException;
    ISong findSongByPath(String name) throws RemoteException;
    ISong findSongByID(long id) throws RemoteException;
}

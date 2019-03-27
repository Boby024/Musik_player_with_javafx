package interfaces;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Remote;
import model.Playlist;

public interface IRemoteMethodesServer extends Remote {
  
    void play(int index) throws RemoteException;
    void nächste() throws RemoteException;
    void pause() throws RemoteException;
    void commit(String title,String interpret,String album,int index) throws RemoteException;
    void add(int index) throws RemoteException;
    void addAll() throws RemoteException;
    void entfernt(int index) throws RemoteException;
    void entfernAll() throws RemoteException;
    void importSongs() throws RemoteException;
    void deleteDataBank() throws RemoteException;
    void savePlaylist(String strat) throws RemoteException;
    void loadPlaylist(String strat) throws RemoteException;

}

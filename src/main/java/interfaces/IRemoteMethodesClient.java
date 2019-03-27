package interfaces;

import model.Playlist;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteMethodesClient extends Remote {
    void update(Playlist library, Playlist playlist) throws RemoteException, NotBoundException;
}

package controller;

import interfaces.IRemoteMethodesClient;
import model.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RemoteMethodesClient implements IRemoteMethodesClient {

    //Aktuallisiert die view auf dem Client
    @Override
    public void update(Playlist library, Playlist playlist) throws RemoteException, NotBoundException {
        ControllerClient.model.setLibrary(library);
        ControllerClient.view.setLibraryObserve (library);
        ControllerClient.model.setPlaylist(playlist);
        ControllerClient.view.setPlaylistObserve (playlist);
    }
}

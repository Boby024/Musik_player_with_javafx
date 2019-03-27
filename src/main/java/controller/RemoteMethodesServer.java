package controller;

import interfaces.IRemoteMethodesClient;
import interfaces.IRemoteMethodesServer;
import model.Playlist;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class RemoteMethodesServer implements IRemoteMethodesServer {
    @Override
    public synchronized void play(int index) throws RemoteException {
        ControllerServer.play(index);
    }

    @Override
    public synchronized void nächste() throws RemoteException {
        ControllerServer.next();
    }

    @Override
    public synchronized void pause() throws RemoteException {
        ControllerServer.pause ();
    }

    @Override
    public synchronized void commit(String title, String interpret, String album, int index) throws RemoteException {
        ControllerServer.commit(title,interpret,album,index);
        try {
            ControllerServer.update ();
        }
        catch (RemoteException err) {
            err.printStackTrace ();
        }
    }

    @Override
    public synchronized void add(int index) throws RemoteException {
        ControllerServer.add (index);
        ControllerServer.update ();
    }

    @Override
    public synchronized void addAll() throws RemoteException {
        ControllerServer.addAll ();
        ControllerServer.update ();
    }

    @Override
    public synchronized void entfernt(int index) throws RemoteException {
        ControllerServer.delete(index);
        ControllerServer.update ();
    }

    @Override
    public synchronized void entfernAll() throws RemoteException {
        ControllerServer.deleteAll ();
        ControllerServer.update ();

    }

    @Override
    public void importSongs() throws RemoteException {

    }

    @Override
    public synchronized void deleteDataBank() throws RemoteException {
        ControllerServer.deleteDB ();
    }

    @Override
    public synchronized void savePlaylist(String strat) throws RemoteException {
        ControllerServer.save (strat);
    }

    @Override
    public synchronized void loadPlaylist(String strat) throws RemoteException {
        ControllerServer.load (strat);
        ControllerServer.update ();

    }
}

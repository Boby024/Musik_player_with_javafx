package model;

import interfaces.IRemoteMethodesClient;

import java.util.ArrayList;

public class Model   {

    private Playlist library = new Playlist();
    private Playlist playlist = new Playlist();
    private static ArrayList<IRemoteMethodesClient> clientList;


    public Model(){

    }

    public Playlist getLibrary() { return library; }

    public void setLibrary(Playlist library) {
        this.library = library;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public static synchronized ArrayList<IRemoteMethodesClient> getClientList() { return clientList;}

}

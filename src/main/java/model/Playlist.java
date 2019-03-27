package model ;

import interfaces.ISong;
import javafx.collections.ModifiableObservableListBase;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Playlist extends ModifiableObservableListBase<ISong> implements interfaces.IPlaylist{
    private static final long serialVersionUID = 700L;

    private ArrayList<ISong> arrayList = new ArrayList<>();


    @Override
    public boolean addSong(ISong s) throws RemoteException {
        return arrayList.add((Song) s);
    }

    @Override
    public boolean deleteSong(ISong s) throws RemoteException {
        return arrayList.remove(s);
    }

    @Override
    public boolean deleteSongByID(long id) throws RemoteException {
        for (ISong s:arrayList) {
            if(s.getId() == id){
                return arrayList.remove(s) ;
            }
        }
        return false;
    }

    @Override
    public void setList(ArrayList<ISong> s) throws RemoteException {
        arrayList = s ;
    }

    @Override
    public ArrayList<ISong> getList() throws RemoteException {
        return arrayList;
    }

    @Override
    public void clearPlaylist() throws RemoteException {
        arrayList = new ArrayList<>();
    }

    @Override
    public int sizeOfPlaylist() throws RemoteException {
          return arrayList.size();
    }

    @Override
    public ISong findSongByPath(String name) throws RemoteException {
        Song song = new model.Song();
        for (ISong s:arrayList) {
            if (s.getPath().equals(name)){
                return s;
            }
        }
        return song;
    }

    @Override
    public ISong findSongByID(long id) throws RemoteException {
        Song song = new model.Song();
        for (ISong s:arrayList) {
            if (s.getId()== id){
                return  s;
            }
        }
        return song;
    }

    @Override
    public ISong get(int index) {
        return arrayList.get(index);
    }

    @Override
    public int size() {
        return arrayList.size();
    }

    @Override
    protected void doAdd(int index, ISong element) {
        arrayList.add(index, (Song) element);
    }

    @Override
    protected ISong doSet(int index, ISong element) {
        return arrayList.set(index, (Song) element);
    }

    @Override
    protected ISong doRemove(int index) {
        return arrayList.remove(index);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ArrayList<ISong> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<ISong> arrayList) {
        this.arrayList = arrayList;
    }

    public void getIndexById (long id){
    }
}

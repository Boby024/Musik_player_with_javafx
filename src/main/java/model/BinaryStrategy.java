package model;

import interfaces.IPlaylist;
import interfaces.ISong;
import interfaces.SerializableStrategy;

import java.io.*;

public class BinaryStrategy implements SerializableStrategy {

    private FileOutputStream fo;
    private ObjectOutputStream os;
    private FileInputStream fi;
    private ObjectInputStream is;

    private model.Playlist library = new model.Playlist();
    private model.Playlist playlist= new model.Playlist();

    // das File für das Library  wird hier erstellt
    public void openWritableLibrary() throws IOException {
        fo =  new FileOutputStream("Library.ser");
        os = new ObjectOutputStream(fo);
    }

    // Referenz auf Lib.ser
    public void openReadableLibrary() throws IOException {
        fi = new FileInputStream("Library.ser");
        is = new ObjectInputStream(fi);

    }
    //  das File für das Playlist wird hier erstellt
    public void openWritablePlaylist() throws IOException {
        fo =  new FileOutputStream("Playlist.ser");
        os = new ObjectOutputStream(fo);
    }

    // Referenz auf Play.ser
    public void openReadablePlaylist() throws IOException {
        fi = new FileInputStream("Playlist.ser");
        is = new ObjectInputStream(fi);
    }

    public void writeSong(ISong s) throws IOException {
        os.writeObject(s);

    }

    public ISong readSong() throws IOException, ClassNotFoundException {
        return (ISong) is.readObject();
    }

    // hier machen wir eine Kopie von Songs in das File " Lib.ser "
    public void writeLibrary(IPlaylist p) throws IOException {
        model.Song song;
        for(int i=0; i < p.sizeOfPlaylist(); i++ ){
            song = (model.Song) p.getList().get(i);
            model.Song s = new model.Song(song.getTitle(), song.getInterpret(),song.getAlbum(),song.getPath(),song.getId());
            writeSong(s);

        }

    }

    // jetzt wieder auf Library wird jeder Song geschrieben
    public IPlaylist readLibrary() throws IOException, ClassNotFoundException {
        try{
            while (is.readObject() != null){

                library.add(readSong());
            }
        }finally {
            return library;
        }
    }

    // hier wird jeder kopierte Songs in das File " Play.ser " gespeichert
    public void writePlaylist(IPlaylist p) throws IOException {
        model.Song song;
        for(int i=0; i < p.sizeOfPlaylist(); i++ ){
            song = (model.Song) p.getList().get(i);
            model.Song s = new model.Song(song.getTitle(), song.getInterpret(),song.getAlbum(),song.getPath(),song.getId());
            writeSong(s);

        }
    }

    // jetzt wieder auf Playlist wird jeder Song geschrieben
    public IPlaylist readPlaylist() throws IOException, ClassNotFoundException {
        try {
            while (readSong() != null){
                playlist.add(library.findSongByID(readSong().getId()));
            }
        }finally {
            return playlist;
        }
    }

    // nach dem Schreiben wird  das File "Lib.ser" geschlossen
    public void closeWritableLibrary() {
        try {
            os.flush();
            fo.close();
			
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // nach dem Lesen wird  das File "Lib.ser" geschlossen
    public void closeReadableLibrary() {
        try {
            is.close();
            fi.close();
			
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // nach dem Schreiben wird  das File "Play.ser" geschlossen
    public void closeWritablePlaylist() {
        try {
            os.flush();
            fo.close();
			
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // nach dem Lesen wird  das File "Play.ser" geschlossen
    public void closeReadablePlaylist() {
        try {
            is.close();
            fi.close();
			
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

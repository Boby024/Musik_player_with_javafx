package model;


import interfaces.IPlaylist;
import interfaces.SerializableStrategy;
import interfaces.ISong;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class XMLStrategie implements SerializableStrategy {

    public XMLStrategie() {

    }

    // herstelle ich die Attribut global damit sie überall genuzt erden können

    private FileOutputStream fo ;
    private XMLEncoder encoder ;
    private FileInputStream fi ;
    private XMLDecoder decoder ;


    private model.Playlist library = new model.Playlist();
    private model.Playlist playlist = new model.Playlist();



    @Override
    public void openWritableLibrary() throws IOException {
        //herstelle  das File
        fo = new FileOutputStream("Library.xml");
        encoder = new XMLEncoder(fo);

    }

    @Override
    public void openReadableLibrary() throws IOException {
        //nehme den File
        fi = new FileInputStream ( "Library.xml" );
        decoder = new XMLDecoder( fi );

    }

    @Override
    public void openWritablePlaylist() throws IOException {
        //herstelle  das File
        fo = new FileOutputStream("Playlist.xml");
        encoder = new XMLEncoder(fo);

    }

    @Override
    public void openReadablePlaylist() throws IOException {
        //nehme den File
        fi = new FileInputStream ( "Playlist.xml" );
        decoder = new XMLDecoder( fi );

    }

    @Override
    public void writeSong(ISong s) throws IOException {
        //schreibe das Objekt
        encoder.writeObject(s);

    }


    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        //Lese ich ein Object
        return  (Song) decoder.readObject();

    }

    @Override
    public void writeLibrary(IPlaylist p) throws IOException {
        //schreibe ich die ganz Library
        model.Song song ;
        for (int i = 0; i <p.sizeOfPlaylist() ; i++) {
            song = (model.Song) p.getList().get(i);
            model.Song s = new model.Song(song.getTitle(),song.getInterpret(),song.getAlbum(),song.getPath(),song.getId()); // herstelle das gleiche Song
            writeSong(s);
        }
    }



    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {
        try {
            while (decoder.readObject()!=null){
                library.add(readSong()); // füge jede Song in library

            }

        }finally {
            return library;
        }

    }

    @Override
    public void writePlaylist(IPlaylist p) throws IOException {
        //schreibe ich die ganz Playlist
        model.Song song ;
        for (int i = 0; i <p.sizeOfPlaylist() ; i++) {
            song = (model.Song) p.getList().get(i);
            model.Song s = new model.Song(song.getTitle(), song.getInterpret(), song.getAlbum(), song.getPath(), song.getId()); // herstelle das gleiche Song
            writeSong(s);
        }
    }


    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {
        try {
            while (decoder.readObject()!=null){
                playlist.add(readSong()); // füge jede Song in playlist

            }

        }finally {
            return playlist;
        }

    }

    @Override
    public void closeWritableLibrary() {
        //schliesse ich alles
        try {
            encoder.flush();
            fo.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeReadableLibrary() {
        //schliesse ich alles
        try {
            decoder.close();
            fi.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeWritablePlaylist() {
        //schliesse ich alles
        try {
            encoder.flush();
            fo.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeReadablePlaylist() {
        //schliesse ich alles
        try {
            decoder.close();
            fi.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

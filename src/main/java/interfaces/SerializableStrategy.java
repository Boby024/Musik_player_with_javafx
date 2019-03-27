package interfaces;

import java.io.IOException;

//Dieses Interface darf auf keinen Fall geändert werden!
public interface SerializableStrategy {

    /*
      Setup Serializing the library
     */
    void openWritableLibrary() throws IOException;

    /*
      Setup Deserializing the library
     */
    void openReadableLibrary() throws IOException;

    /*
      Setup Serializing the Playlist
     */
    void openWritablePlaylist() throws IOException;

    /*
      Setup Deserializing the Playlist
     */
    void openReadablePlaylist() throws IOException;



    /*
     Write a Song to the recently opened Medium
     */
    void writeSong(ISong s) throws IOException;

    /*
     Read a song from the recently opened medium
     */
    ISong readSong() throws IOException, ClassNotFoundException;

    /*
     Write songs from the library by calling writeSong for each Song in library
    */
    void writeLibrary(IPlaylist p) throws IOException;

    /*
     Read songs into the library by calling readSong until null is returned
     */
    IPlaylist readLibrary() throws IOException, ClassNotFoundException;

    /*
     Write a PlayList by calling writeSong for each Song
    */
    void writePlaylist(IPlaylist p) throws IOException;

    /*
     Read a playlist by calling readSong until null is returned
     */
    IPlaylist readPlaylist() throws IOException, ClassNotFoundException;



    /*
     Finish writing/reading by closing all Streams
     */
    void closeWritableLibrary();
    void closeReadableLibrary();

    void closeWritablePlaylist();
    void closeReadablePlaylist();
}



package model;

import interfaces.IPlaylist;
import interfaces.ISong;
import interfaces.SerializableStrategy;


import java.io.IOException;
import java.sql.*;

public class JDBCStrategy implements SerializableStrategy {

    // hier erstelle ich die Attribut global damit sie überall genutzt werden können
    private  Connection con;
    private  PreparedStatement pstmt ;
    private  ResultSet rs ;
    private  boolean libSelect = true;
    private model.Playlist library = new model.Playlist();
    private model.Playlist playlist = new model.Playlist();




    @Override
    public void openWritableLibrary() throws IOException {
        //Herstelle die Tabelle  Library mit Culum
        try {

            Class.forName("org.sqlite.JDBC");

            con = DriverManager.getConnection("jdbc:sqlite:library.db");

            pstmt = con.prepareStatement("CREATE TABLE IF NOT EXISTS Library (ID integer, titel text, interpret text,album text,path text);");
            pstmt.executeUpdate();
        } catch (ClassNotFoundException| SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void openReadableLibrary() throws IOException {
        // Selectiere oder nehme die ganze Tablelle
        try {
            Class.forName("org.sqlite.JDBC");

            con = DriverManager.getConnection("jdbc:sqlite:library.db");

            pstmt = con.prepareStatement("SELECT * FROM Library ;");
            rs = pstmt.executeQuery();
        } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void openWritablePlaylist() throws IOException {
        //Herstelle die Tabelle  Library mit Culum
        try {
            Class.forName("org.sqlite.JDBC");

            con = DriverManager.getConnection("jdbc:sqlite:library.db");
            pstmt = con.prepareStatement("CREATE TABLE IF NOT EXISTS Playlist (ID integer, titel text, interpret text,album text,path text);");
            pstmt.executeUpdate();
        } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void openReadablePlaylist() throws IOException {
        // Selectiere oder nehme die ganze Tablelle
        try {
            Class.forName("org.sqlite.JDBC");

            con = DriverManager.getConnection("jdbc:sqlite:library.db");

            pstmt = con.prepareStatement("SELECT * FROM Playlist ;");
            rs = pstmt.executeQuery();
        } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void writeSong(ISong s) throws IOException {
        //schreibe ich die ganz Library
        try {
            if (libSelect){

                pstmt = con.prepareStatement("INSERT INTO Library (ID, titel, interpret, album,path) VALUES (?,?,?,?,?);");
                pstmt.setLong(1,s.getId());      //schreibe den Id des song
                pstmt.setString(2,s.getTitle());    //schreibe  den Title
                pstmt.setString(3,s.getInterpret());    //schreibe den Interpret
                pstmt.setString(4,s.getAlbum());    //schreibe ich den album
                pstmt.setString(5,s.getPath()); //schreibe den Path
                pstmt.executeUpdate();

            }else {

                pstmt = con.prepareStatement("INSERT INTO Playlist (ID, titel, interpret, album,path) VALUES (?,?,?,?,?);");
                pstmt.setLong(1,s.getId());   //schreibe den Id des song
                pstmt.setString(2,s.getTitle()); //schreibe  den Title
                pstmt.setString(3,s.getInterpret());  //schreibe den Interpret
                pstmt.setString(4,s.getAlbum());   //schreibe ich den album
                pstmt.setString(5,s.getPath());   //schreibe den Path
                pstmt.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        // Lese ich jede Song
        Song song = new model.Song();
        try {
            song = new model.Song(rs.getString("titel"), rs.getString("album"),
                    rs.getString("interpret"),rs.getString("path"),rs.getLong("ID")); // herstelle ich das gleiche Song

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return song;
    }

    @Override
    public void writeLibrary(IPlaylist p) throws IOException {
        // schreibe die ganze Library
        libSelect = true ;
        try{
            pstmt = con.prepareStatement("DELETE FROM Library");
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        for (int i = 0; i <p.sizeOfPlaylist() ; i++) {
            writeSong(p.getList().get(i));
        }
    }


    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {
        // Lese ich jede Song
        try {
            while (rs.next()) {
                library.add((model.Song)readSong()); // addiere den Song
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return library;
    }

    @Override
    public void writePlaylist(IPlaylist p) throws IOException {
        // schreibe die ganze Playlist
        libSelect = false ;
        try{
            pstmt = con.prepareStatement("DELETE FROM Playlist");
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        for (int i = 0; i <p.sizeOfPlaylist() ; i++) {
            writeSong(p.getList().get(i));
        }
    }




    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {
        // Lese ich jede Song
        try {
            while (rs.next()) {
                playlist.add(library.findSongByID(readSong().getId()));  // addiere ich der selber Song von der Library in Playlist
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return playlist;
    }

    @Override
    public void closeWritableLibrary() {
        //schliesse ich alles
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeReadableLibrary() {
        //schliesse ich alles
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeWritablePlaylist() {
        //schliesse ich alles
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeReadablePlaylist() {
        //schliesse ich alles
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteStore (int i) {
        // entferne ich die Tabelle wenn es existiert
        try {
            if (i == 1) {  // wenn es geht um Library(1)

                Class.forName("org.sqlite.JDBC");

                con = DriverManager.getConnection("jdbc:sqlite:library.db");

                pstmt = con.prepareStatement("DROP TABLE IF  EXISTS Library ;");
                pstmt.executeUpdate();

            } else if (i == 2) { // wenn es geht um Playlist(2)

                Class.forName("org.sqlite.JDBC");

                con = DriverManager.getConnection("jdbc:sqlite:library.db");

                pstmt = con.prepareStatement("DROP TABLE IF  EXISTS Playlist;");
                pstmt.executeUpdate();

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


}
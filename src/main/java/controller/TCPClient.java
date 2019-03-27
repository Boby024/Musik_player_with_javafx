package controller;

import interfaces.IRemoteMethodesClient;
import interfaces.IRemoteMethodesServer;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TCPClient implements Runnable {
    //IP des Server
final static String IP = "127.0.0.1";
final static int PORT = 5020;
final static String PASSWORD="zugang";
//RMI zum Server
public static IRemoteMethodesServer remoteMethodes;

    @Override
    public void run() {
        String msg="";
        //Erzeugen des Anmeldestrings am Server
        try
        {
            String rmi = InetAddress.getLocalHost().getHostName();
            msg=rmi+","+PASSWORD;
        }
        catch(UnknownHostException err){
            err.printStackTrace ();
        }

        try {
            //Anmelden am Server
            Socket socket = new Socket (IP, PORT);
            PrintWriter pw = new PrintWriter (new OutputStreamWriter (socket.getOutputStream ()));
            pw.print(msg);
            pw.flush ();
            //Antwort des Server verarbeiten
            BufferedReader br = new BufferedReader (new InputStreamReader (socket.getInputStream ()));
            char[] buffer=new char[100];
            int laenge =br.read(buffer,0,100);
            String server =new String (buffer,0,laenge);
            //Regestrieren der Methoden des Clienten und des Servers an der registry
            RemoteMethodesClient RM = new RemoteMethodesClient();
            IRemoteMethodesClient stub = (IRemoteMethodesClient) UnicastRemoteObject.exportObject( RM, 0 );
            try {
                Registry registry = LocateRegistry.getRegistry ();
                registry.rebind( "RemoteMethodesClient", stub );

                remoteMethodes = (IRemoteMethodesServer) registry.lookup(server);
            }
            catch (RemoteException err) {
                err.printStackTrace ();
            }
            catch (NotBoundException err){
                err.printStackTrace ();
            }


        }
        catch (IOException err){
            err.printStackTrace ();
        }
    }
}

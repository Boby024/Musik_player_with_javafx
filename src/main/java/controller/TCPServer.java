package controller;

import java.io.*;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import interfaces.IRemoteMethodesClient;
import interfaces.IRemoteMethodesServer;
import model.*;

public class TCPServer {
    final static int PORT = ;

    final static String PASSWORD="zugang";
    static String RMI;
    static Registry registry;

    public void ServerStart() {
        //Start des Server
        try {
            final ServerSocket serverSocket = new ServerSocket (PORT);
            final ExecutorService threadPool = Executors.newCachedThreadPool ();

            //Erzeugen des Antwortstrings an die sich anmeldenden Clients
            try {
                RMI = "//" + InetAddress.getLocalHost ().getHostAddress () + "/" + "RemoteMethodesServer";
            } catch (UnknownHostException err) {
                err.printStackTrace ();
            }

            //registrieren der ServerMethoden an der registry
            try {
                LocateRegistry.createRegistry (Registry.REGISTRY_PORT);
            } catch (RemoteException err) {
                err.printStackTrace ();
            }
            RemoteMethodesServer RM = new RemoteMethodesServer ();
            IRemoteMethodesServer stub = (IRemoteMethodesServer) UnicastRemoteObject.exportObject (RM, 0);


            registry = LocateRegistry.getRegistry ();
            registry.rebind ("RemoteMethodesServer", stub);

            //Start des Threads der auf anmeldende Clients wartet
            Thread clientThread = new Thread (new ClientAnmeldung (serverSocket, threadPool));
            clientThread.start ();

        } catch (IOException err) {
            err.printStackTrace ();
        }
    }
}

class ClientAnmeldung implements Runnable {
ServerSocket serverSocket;
ExecutorService threadPool;

public ClientAnmeldung (ServerSocket serverSocket, ExecutorService threadPool){
    this.serverSocket=serverSocket;
    this.threadPool = threadPool;
}
    @Override
    public void run(){
    try {
        while (true) {
            //ankommende Clients bekommen einen eigenen Thread in dem sie abgearbeitet werden
            Socket client = serverSocket.accept ();
            threadPool.execute (new ClientHandling (serverSocket, client));

        }
    }
        catch(IOException err){
            err.printStackTrace ();
        }
        finally{
            threadPool.shutdown ();
            try {
                threadPool.awaitTermination (1, TimeUnit.SECONDS);
                if (!serverSocket.isClosed ()) {
                    serverSocket.close ();
                }
            } catch (IOException err) {
                err.printStackTrace ();
            }
            catch (InterruptedException err){
                err.printStackTrace ();
            }
        }
    }
}

//Abhandlung eines anmeldenden Clients
class ClientHandling implements Runnable {
    final ServerSocket sSocket;
    final Socket cSocket;

    public ClientHandling(ServerSocket sSocket, Socket cSocket){
        this.sSocket=sSocket;
        this.cSocket=cSocket;
    }
    @Override
    public void run(){
        char buffer[]=new char[100];
        String msg;
        String client="";

        try {
            //Anmeldung überprüfen
            BufferedReader br = new BufferedReader (new InputStreamReader (cSocket.getInputStream ()));
            int laenge=br.read(buffer,0,100);
            msg=new String(buffer,0,laenge);
            String[] arg = msg.split(",");
            if(arg[1]==TCPServer.PASSWORD) {
                client=arg[0];;
                //registrieren des Clients an der registry und speichern diesen in einer ArrayList
                IRemoteMethodesClient clientMethod =(IRemoteMethodesClient) TCPServer.registry.lookup("//"+client+"/RemoteMethodesClient");
                Model.getClientList().add(clientMethod);
            }
            //Antwort an den Client
            PrintWriter pw = new PrintWriter (new OutputStreamWriter (cSocket.getOutputStream ()));
            pw.print(TCPServer.RMI);
            pw.flush ();

        }
        catch(IOException err){
            err.printStackTrace ();
        }
        catch(NotBoundException err){
            err.printStackTrace ();
        }
        finally {

            if(!cSocket.isClosed ()){
                try{cSocket.close();}
                catch(IOException err){err.printStackTrace ();}
            }
        }
    }
}

package UDP;



import controller.ControllerServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer  implements Runnable{
    public UDPServer() {

    }

    @Override
    public void run() {
        while (true) {
            try (DatagramSocket socket = new DatagramSocket(5000);) {
                // Neues Paket anlegen
                DatagramPacket packet = new DatagramPacket(new byte[4], 4);
                // Auf Paket warten
                System.out.println("test");
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Daten auslesen
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                int len = packet.getLength();
                byte[] data = packet.getData();

                System.out.println("Anfrage von " + address + " vom Port " + port + " mit der Länge " + len + "\n" + new String(data));

                // Nutzdaten in ein Stringobjekt übergeben
                String command = new String(data);

                try {
                    // Erstes Kommando filtern

                    if (command.equals("TIME")) {
                        String aktuelleZeit = ControllerServer.getAktuelleZeit();
                        byte[] time = aktuelleZeit.getBytes();

                        // Paket mit neuen Daten (Datum) als Antwort vorbereiten
                        packet = new DatagramPacket(time, time.length, address, port);

                        try {
                            // Paket versenden
                            socket.send(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {

                        byte[] time = new String("Command unknown").getBytes();

                        // Paket mit Information, dass das Schlüsselwort ungültig ist als
                        // Antwort vorbereiten
                        packet = new DatagramPacket(time, time.length, address, port);
                        try {
                            // Paket versenden
                            socket.send(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }
}

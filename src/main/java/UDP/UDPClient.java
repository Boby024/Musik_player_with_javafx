package UDP;

import javafx.application.Platform;
import view.View;
import view.ViewClient;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {
    private ViewClient view;

    public UDPClient(ViewClient view) {
        this.view = view;
    }

    public void run() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try (DatagramSocket dSocket = new DatagramSocket();) {
            try {
                while (true) {

                    String inhalt ="TIME";

                    byte buffer[] = inhalt.getBytes();

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ia, 5000);
                    dSocket.send(packet);

                    byte answer[] = new byte[1024];
                    packet = new DatagramPacket(answer, answer.length);

                    //TODO: prevent freezing
                    dSocket.receive(packet);


                    String time = new String(packet.getData(), 0, packet.getLength());


                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            view.getTimeLabel().setText(time);
                        }
                    });


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }

}

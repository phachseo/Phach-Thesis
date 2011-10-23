package team4;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class Connector extends Thread {

    public String GREETING = "GNUTELLA CONNECT/0.4";
    public String READY = "GNUTELLA OK";
    public byte[] greeting = (GREETING + "\n\n").getBytes();
    public byte[] ready = (READY + "\n\n").getBytes();
    public String CONFIRM = "GNUTELLA OK";
    public byte[] confirm = (CONFIRM + "\n\n").getBytes();
    private Connection connection;
    private static int TIMEOUT = 10000;

    // Constructor for making a connection to a servent
    public Connector(String aHost, int aPort, int t) {
        try {
            //Socket socket = SocketMaker.makeSocket(aHost, aPort, t);
            Socket socket = new Socket(aHost, aPort);
            connection = new Connection(socket, Connection.OUTGOING);

        } catch (IOException e) {
            System.err.println("Exception in Connector :" + e.getMessage());
        }
    }

    public Connector(String aHost, int aPort) {
        this(aHost, aPort, TIMEOUT);
    }

    public void run() {
        try {
            System.out.println("Connecting to " + connection.getIPAddress());

            connection.getByteWriter().write(greeting, 0, greeting.length);
            connection.getByteWriter().flush();

            String incoming = connection.getTextReader().readLine();
            //String newline = connection.getTextReader().readLine();

            if (incoming == null || incoming.indexOf(READY) == -1) {
                return;
            } else {
//                if (!HostArray.OutgoingHostsisLive(connection)) {
                    System.out.println("Connect to : " + connection.getIPAddress().toString());
                    Host h = new Host(connection.getIPAddress().toString(), connection.getIPAddress().getPort());

                    HostCache.addHost(h);
                    HostArray.OutgoingHostaddConnection(connection);
                    
                    Server server = new Server(connection);
                    server.start();
//                } else {
//                    JOptionPane.showMessageDialog(null, "CONNECTOR - ip : " + connection.getIPAddress().toString() + " da co trong HostArray");
//                }

            }
        } catch (Exception e) {
            System.out.println("Connection failed.");
            NetworkManager.notify(connection.getIPAddress());
        }
    }
}

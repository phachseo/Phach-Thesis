package Server;

import Client.Mine;
import Network.HostArray;
import Client.Preferences;
import Client.Connection;
import Client.HostCache;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class Listener extends Thread {

    private static int LISTENING_PORT = 12345;
    public Connection connection;
    public static String GREETING = "GNUTELLA CONNECT/0.4";
    public static String READY = "GNUTELLA OK";
    public static String BUSY = "GNUTELLA BUSY";
    public static byte[] greeting = (GREETING + "\n\n").getBytes();
    public static byte[] ready = (READY + "\n\n").getBytes();
    public static byte[] busy = (BUSY + "\n\n").getBytes();
    public static String PHACHSEO = "PHACHSEO NO1";
    public static byte[] phachseo = (PHACHSEO + "\n\n").getBytes();

    public static int count = 0;

    public void run() {
        try {   //ServerSocket ss = new ServerSocket
            ServerSocket ss = new ServerSocket(LISTENING_PORT, MIN_PRIORITY, InetAddress.getLocalHost());

            while (true) {
                Socket socket = ss.accept();
                connection = new Connection(socket, Connection.INCOMING);

                System.out.println("Received connection from : " + connection.getIPAddress().toString() + "  - port : "
                        + connection.getSocket().getPort() + "  - localport : " + connection.getSocket().getLocalPort());

                String incoming = connection.getTextReader().readLine();
                if (incoming == null) {
                    continue;
                } else if (incoming.indexOf(GREETING) == -1) {       //   incoming.in
//                       if(incoming.indexOf(PHACHSEO)!= 1){
//                        System.out.println("YEAHHHHHHHHHHHHHHHHHHH");
//                        MRerServer mrserver = new MRerServer(connection, incoming);
//                        mrserver.start();
//
//                       }

                    DownloadServer downloadserver = new DownloadServer(connection, incoming);
                    downloadserver.start();
                    continue;

                } else if (HostArray.IncomingHostsgetCount() >= Preferences.MAX_LIVE) {
                    connection.getTextReader().readLine(); // Gets rid of the extra newline
                    connection.getByteWriter().write(busy, 0, busy.length);
                    connection.getByteWriter().flush();
                } else if (((Mine.ipString).equals(connection.getIPAddress().toString())) && (Mine.port == connection.getIPAddress().getPort())) { // loopback message
                    connection.getTextReader().readLine(); // Gets rid of the extra newline
                    connection.getByteWriter().write(busy, 0, busy.length);
                    connection.getByteWriter().flush();
                } else {
                    connection.getTextReader().readLine(); // Gets rid of the extra newline
                    connection.getByteWriter().write(ready, 0, ready.length);
                    connection.getByteWriter().flush();

//                    if (!HostArray.IncomingHostsisLive(connection)) {

                    System.out.println("count = " + (++count));
                        Server server = new Server(connection);
                        server.start();
                        HostArray.IncomingHostsaddConnection(connection);
                        HostCache.addConnection(connection);
//                    }
//                    else {
//                        JOptionPane.showMessageDialog(null, "LISTENER - ip : " + connection.getIPAddress().toString() + " da co trong HostArray");
//                    }
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

package MapReduce;

import Client.Mine;
import Network.HostArray;
import Client.Preferences;
import Client.Connection;
import Client.HostCache;
import Server.Server;
import java.net.*;
import java.io.*;
import java.util.Arrays;

public class MRListener extends Thread {

    private static int LISTENING_PORT = 12346;
    int myport;
    public Connection connection;
    public static String GREETING = "GNUTELLA CONNECT/0.4";
    public static String READY = "GNUTELLA OK";
    public static String BUSY = "GNUTELLA BUSY";
    public static byte[] greeting = (GREETING + "\n\n").getBytes();
    public static byte[] ready = (READY + "\n\n").getBytes();
    public static byte[] busy = (BUSY + "\n\n").getBytes();
// public static String PHACHSEO = "PHACHSEO NO1";
// public static byte [] phachseo = (PHACHSEO+"\n\n").getBytes

    public MRListener(int port) {
        myport = port;
    }

    public void run() {
        try {   //ServerSocket ss = new ServerSocket
            // System.out.println("Port goi di:"+ Mine.getPort());
            ServerSocket ss = new ServerSocket(myport, MIN_PRIORITY, InetAddress.getLocalHost());

            while (true) {
                Socket socket = ss.accept();
                connection = new Connection(socket, Connection.INCOMING);
                String incoming = "";
                String incomingraw = connection.getTextReader().readLine();
                while (incomingraw != null) {
                    System.out.println("IncomingRaw la :" + incomingraw);

                    incoming += (incomingraw + "\n");
                    System.out.println("Incoming la :" + incoming);

                    incomingraw = connection.getTextReader().readLine();
                   
                }
                System.out.println(" nhan dc du lieu ban dau la :" + incoming);
                if (incoming == null) {
                    continue;
                } else if (incoming.indexOf(GREETING) == -1) {
//        {       //   incoming.in
//                       if(incoming.indexOf(PHACHSEO)!= 1){
//                        System.out.println("YEAHHHHHHHHHHHHHHHHHHH");
//                        MRerServer mrserver = new MRerServer(connection, incoming);
//                        mrserver.start();
//
//                       }
//                       else if {
                    MRerServer mrerserver = new MRerServer(connection, incoming);
                    mrerserver.start();
                    continue;
                } else if (HostArray.IncomingHostsgetCount() >= Preferences.MAX_LIVE) {
                    connection.getTextReader().readLine(); // Gets rid of the extra newline
                    connection.getByteWriter().write(busy, 0, busy.length);
                    connection.getByteWriter().flush();
                } else if (((Mine.ipString).equals(connection.getIPAddress().toString())) && (Mine.port == connection.getIPAddress().getPort())) {
                    connection.getTextReader().readLine(); // Gets rid of the extra newline
                    connection.getByteWriter().write(busy, 0, busy.length);
                    connection.getByteWriter().flush();
                } else {
                    connection.getTextReader().readLine(); // Gets rid of the extra newline
                    connection.getByteWriter().write(ready, 0, ready.length);
                    connection.getByteWriter().flush();
                    Server server = new Server(connection);
                    server.start();
                    HostArray.IncomingHostsaddConnection(connection);
                    HostCache.addConnection(connection);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

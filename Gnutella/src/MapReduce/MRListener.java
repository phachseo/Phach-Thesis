package MapReduce;

import Client.Mine;
import Network.HostArray;
import Client.Preferences;
import Client.Connection;
import Client.HostCache;
import Server.Server;
import java.net.*;
import java.io.*;
import java.nio.CharBuffer;
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
            int count = 0;
            while (true) {

                

                Socket socket = ss.accept();
                connection = new Connection(socket, Connection.INCOMING);
                String incoming = "";
                String buff="";
                
//                String incomingraw = connection.getTextReader().readLine();
//            
//                System.out.println("incomingraw la : "+ incomingraw);
//                
//                while (!incomingraw.equals("")) {
//                    
//
//                    System.out.println("IncomingRaw la :" + incomingraw);
//
//                    incoming += (incomingraw + "\r\n");
//                    System.out.println("Incoming la :" + incoming);
//
//                   
//                   // System.out.println("count = " + ++count);
//
//                }
                 

                
                Reader r = new InputStreamReader(connection.getByteReader());
                int c;
                System.out.println("before while");
                while ((c = r.read()) != -1) {
                  
                    incoming += (char)c;
                       // System.out.println(" trong while : "+ incoming);
                        if(incoming.endsWith("Range: bytes=0-"))
                     //     System.out.print((char) c);
                            break;
                }

                System.out.println("phachseo");
                System.out.println(" nhan dc du lieu ban dau la :" + incoming);

                
//                if (incoming == null) {
//                    continue;
//                } else if (incoming.indexOf(GREETING) == -1) {
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
//                    continue;
//                } else if (HostArray.IncomingHostsgetCount() >= Preferences.MAX_LIVE) {
//                    connection.getTextReader().readLine(); // Gets rid of the extra newline
//                    connection.getByteWriter().write(busy, 0, busy.length);
//                    connection.getByteWriter().flush();
//                } else if (((Mine.ipString).equals(connection.getIPAddress().toString())) && (Mine.port == connection.getIPAddress().getPort())) {
//                    connection.getTextReader().readLine(); // Gets rid of the extra newline
//                    connection.getByteWriter().write(busy, 0, busy.length);
//                    connection.getByteWriter().flush();
//                } else {
//                    connection.getTextReader().readLine(); // Gets rid of the extra newline
//                    connection.getByteWriter().write(ready, 0, ready.length);
//                    connection.getByteWriter().flush();
//                    Server server = new Server(connection);
//                    server.start();
//                    HostArray.IncomingHostsaddConnection(connection);
//                    HostCache.addConnection(connection);
//                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

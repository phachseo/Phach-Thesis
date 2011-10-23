package team4;

/*
Network Manager - started by main
 */
import java.net.*;
import java.io.*;
import java.util.Arrays;

public class NetworkManager {

    public static void writeToOne(IPAddress ip, Packet packet) {
        if (HostArray.OutgoingHostsisLive(ip.toString())) {
//            System.out.println("write to one : 1");
            Connection connection = HostArray.OutgoingHostsgetConnection(ip.toString());
            try {
//                System.out.println("write to one : 2");
                connection.getByteWriter().write(packet.contents(), 0, packet.totalLength());
                connection.getByteWriter().flush();
            } catch (IOException e) {
                System.out.println("write to one : 3");
                try {
                    System.out.println("write to one : 4");
                    connection.getSocket().close();
                    HostArray.OutgoingHostsremoveConnection(ip);
                } catch (IOException exception) {
                    System.out.println("write to one :5");
                    System.err.println(exception);
                }
            }
        }
    }

    public static void writeToAll(Packet packet) {

        for (int i = 0; i < HostArray.OutgoingHostsgetCount(); i++) {
            Connection c = HostArray.OutgoingHostsgetConnection(i);
            try {
                c.getByteWriter().write(packet.contents(), 0, packet.totalLength());
                c.getByteWriter().flush();
            } catch (IOException e) {
                try {
                    c.getSocket().close();
                    HostArray.OutgoingHostsremoveConnection(c);
                } catch (IOException exception) {
                    System.err.println(exception);
                }
            }
        }
    }

    public static void writeButOne(IPAddress ip, Packet packet) {
        for (int i = 0; i < HostArray.OutgoingHostsgetCount(); i++) {
            Connection c = HostArray.OutgoingHostsgetConnection(i);
            if (!(c.compareConnections(ip.toString()))) {
                try {
                    c.getByteWriter().write(packet.contents(), 0, packet.totalLength());
                    c.getByteWriter().flush();
                } catch (IOException e) {
                    try {
                        c.getSocket().close();
                        HostArray.OutgoingHostsremoveConnection(c);
                    } catch (IOException exception) {
                        System.err.println(exception);
                    }
                }
            }
        }
    }

    public static void notify(IPAddress ip) // Remove socket from open connection list, based on its IP.
    {
        if (HostArray.OutgoingHostsisLive(ip.toString())) {
            System.out.println("Killing " + ip);
            Connection c = HostArray.OutgoingHostsgetConnection(ip.toString());
            try {
                c.getSocket().close();
                HostArray.OutgoingHostsremoveConnection(c);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}

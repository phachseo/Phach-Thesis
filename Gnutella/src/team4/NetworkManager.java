package team4;

/*
Network Manager - started by main
 */
import java.net.*;
import java.io.*;
import java.util.Arrays;

public class NetworkManager {

    public static void writeToOne(IPAddress ip, Packet packet) {
        if (HostArray.IncomingHostsisLive(ip)) {
            Connection connection = HostArray.IncomingHostsgetConnection(ip);
            try {
                connection.getByteWriter().write(packet.contents(), 0, packet.totalLength());
                connection.getByteWriter().flush();
            } catch (IOException e) {
                try {
                    connection.getSocket().close();
                    HostArray.IncomingHostsremoveConnection(ip);
                } catch (IOException exception) {
                    System.err.println(exception);
                }
            }
        }
    }

    public static void writeToAll(Packet packet) {

        for (int i = 0; i < HostArray.IncomingHostsgetCount(); i++) {
            Connection c = HostArray.IncomingHostsgetConnection(i);
            try {
                c.getByteWriter().write(packet.contents(), 0, packet.totalLength());
                c.getByteWriter().flush();
            } catch (IOException e) {
                try {
                    c.getSocket().close();
                    HostArray.IncomingHostsremoveConnection(c);
                } catch (IOException exception) {
                    System.err.println(exception);
                }
            }
        }
    }

    public static void writeButOne(IPAddress ip, Packet packet) {
        for (int i = 0; i < HostArray.IncomingHostsgetCount(); i++) {
            Connection c = HostArray.IncomingHostsgetConnection(i);
            if (!(c.compareConnections(ip))) {
                try {
                    c.getByteWriter().write(packet.contents(), 0, packet.totalLength());
                    c.getByteWriter().flush();
                } catch (IOException e) {
                    try {
                        c.getSocket().close();
                        HostArray.IncomingHostsremoveConnection(c);
                    } catch (IOException exception) {
                        System.err.println(exception);
                    }
                }
            }
        }
    }

    public static void notify(IPAddress ip) // Remove socket from open connection list, based on its IP.
    {
        if (HostArray.IncomingHostsisLive(ip)) {
            System.out.println("Killing " + ip);
            Connection c = HostArray.IncomingHostsgetConnection(ip);
            try {
                c.getSocket().close();
                HostArray.IncomingHostsremoveConnection(c);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}

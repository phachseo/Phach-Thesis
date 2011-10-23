package team4;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class HostArray {

    private static Connection[] hosts;

    public static boolean isNull() {
        if (hosts == null) {
            return true;
        } else {
            return false;
        }
    }

    public static synchronized int getCount() {
        if (isNull()) {
            return 0;
        } else {
            return hosts.length;
        }
    }

    public static synchronized void addConnection(Connection c) {
        System.out.println("phachseo 111111111111");
        if (isNull()) {
            System.out.println("phachseo 2222222222222");
            hosts = new Connection[1];
            hosts[0] = c;
            Searcher.updateAddedConnection(c);
        } else if (!isLive(c)) {
            System.out.println("phachseo 33333333333333");
            Connection[] temp = new Connection[hosts.length + 1];
            System.arraycopy(hosts, 0, temp, 0, hosts.length);
            temp[hosts.length] = c;
            hosts = temp;
            Searcher.updateAddedConnection(c);
        }
    }

    public static synchronized void removeConnection(Connection c) {
        removeConnection(c.getIPAddress());
    }

    public static synchronized void removeConnection(IPAddress ip) {
        if (!(isNull()) && isLive(ip)) {
            Connection[] temp = new Connection[hosts.length - 1];
            int j = 0;
            for (int i = 0; i < hosts.length; i++) {
                if (ip.equals(hosts[i].getIPAddress())) {
                    continue;
                }
                temp[j] = hosts[i];
                j++;
            }
            hosts = temp;
            Searcher.updateRemovedConnection(ip);
        }
    }

    public static synchronized Connection getConnection(int i) {
        if ((!isNull() && (i < getCount()))) {
            return hosts[i];
        } else {
            return null;
        }
    }

    public static synchronized Connection getConnection(IPAddress ip) {
        Connection c = null;
        for (int i = 0; i < hosts.length; i++) {
            if (ip.equals(hosts[i].getIPAddress())) {
                c = hosts[i];
            }
        }
        return c;
    }

    public static synchronized boolean isLive(String ipString) {
        if (!isNull()) {
            for (int i = 0; i < hosts.length; i++) {
                InetAddress inet = hosts[i].getSocket().getInetAddress();
                if ((ipString.equals(inet.getHostName())) || (ipString.equals(inet.getHostAddress()))) {
                    //          System.out.println(ipString + " ?= " + inet.getHostName());
                    //          System.out.println(ipString + " ?= " + inet.getHostAddress());
                    return true;
                }

            }
            return false;
        } else {
            return false;
        }
    }

    public static synchronized boolean isLive(Connection c) {
        return (isLive(c.getIPAddress()));
    }

    public static synchronized boolean isLive(IPAddress ip) {
//        try {
            System.out.println("IP to compare : " + ip.toString());
            for (int i = 0; i < hosts.length; i++) {
                System.out.println("ip in hosts : " + hosts[i].getIPAddress().toString());
                if (ip.equals(hosts[i].getIPAddress())) {
                    return true;
                }
            }
            return false;
//        } catch (NullPointerException exception) {
//            System.out.println("Null pointer error in HostArray");
//            return false;
//        }
    }
}

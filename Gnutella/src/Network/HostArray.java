package Network;

import Client.IPAddress;
import Client.Connection;
import MRonP2P.Searcher;
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class HostArray {

    private static Connection[] IncomingHosts;
    public static Connection[] OutgoingHosts;

    public static boolean IncomingHostisNull() {
        if (IncomingHosts == null) {
            return true;
        } else {
            return false;
        }
    }

    public static synchronized int IncomingHostsgetCount() {
        if (IncomingHostisNull()) {
            return 0;
        } else {
            return IncomingHosts.length;
        }
    }

    public static synchronized void IncomingHostsaddConnection(Connection c) {
//        System.out.println("phachseo 111111111111");
        if (IncomingHostisNull()) {
//            System.out.println("phachseo 2222222222222");
            IncomingHosts = new Connection[1];
            IncomingHosts[0] = c;
            Searcher.updateAddedConnection(c);
        } else if (!IncomingHostsisLive(c)) {
//            System.out.println("phachseo 33333333333333");
            Connection[] temp = new Connection[IncomingHosts.length + 1];
            System.arraycopy(IncomingHosts, 0, temp, 0, IncomingHosts.length);
            temp[IncomingHosts.length] = c;
            IncomingHosts = temp;
            Searcher.updateAddedConnection(c);
        }
    }

    public static synchronized void IncomingHostsremoveConnection(Connection c) {
        IncomingHostsremoveConnection(c.getIPAddress());
    }

    public static synchronized void IncomingHostsremoveConnection(IPAddress ip) {
        if (!(IncomingHostisNull()) && IncomingHostsisLive(ip)) {
            Connection[] temp = new Connection[IncomingHosts.length - 1];
            int j = 0;
            for (int i = 0; i < IncomingHosts.length; i++) {
                if (ip.equals(IncomingHosts[i].getIPAddress())) {
                    continue;
                }
                temp[j] = IncomingHosts[i];
                j++;
            }
            IncomingHosts = temp;
            Searcher.updateRemovedConnection(ip);
        }
    }

    public static synchronized Connection IncomingHostsgetConnection(int i) {
        if ((!IncomingHostisNull() && (i < IncomingHostsgetCount()))) {
            return IncomingHosts[i];
        } else {
            return null;
        }
    }

    public static synchronized Connection IncomingHostsgetConnection(IPAddress ip) {
        Connection c = null;
        for (int i = 0; i < IncomingHosts.length; i++) {
            if (ip.equals(IncomingHosts[i].getIPAddress())) {
                c = IncomingHosts[i];
            }
        }
        return c;
    }

    public static synchronized boolean IncomingHostsisLive(String ipString) {
        if (!IncomingHostisNull()) {
            for (int i = 0; i < IncomingHosts.length; i++) {
                InetAddress inet = IncomingHosts[i].getSocket().getInetAddress();
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

    public static synchronized boolean IncomingHostsisLive(Connection c) {
        return (IncomingHostsisLive(c.getIPAddress()));
    }

    public static synchronized boolean IncomingHostsisLive(IPAddress ip) {
        try {
            System.out.println("IP to compare : " + ip.toString());
            for (int i = 0; i < IncomingHosts.length; i++) {
                System.out.println("ip in hosts : " + IncomingHosts[i].getIPAddress().toString());
                if (ip.equals(IncomingHosts[i].getIPAddress())) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException exception) {
            System.out.println("Null pointer error in HostArray");
            return false;
        }
    }








     public static boolean OutgoingHostisNull() {
        if (OutgoingHosts == null) {
            return true;
        } else {
            return false;
        }
    }

    public static synchronized int OutgoingHostsgetCount() {
        if (OutgoingHostisNull()) {
            return 0;
        } else {
            return OutgoingHosts.length;
        }
    }

    public static synchronized void OutgoingHostaddConnection(Connection c) {
      //  System.out.println("phachseo 111111111111");
        if (OutgoingHostisNull()) {
            //System.out.println("phachseo 2222222222222");
            OutgoingHosts = new Connection[1];
           OutgoingHosts[0] = c;
            Searcher.updateAddedConnection(c);
        } else if (!OutgoingHostsisLive(c)) {
           // System.out.println("phachseo 33333333333333");
            Connection[] temp = new Connection[OutgoingHosts.length + 1];
            System.arraycopy(OutgoingHosts, 0, temp, 0, OutgoingHosts.length);
            temp[OutgoingHosts.length] = c;
            OutgoingHosts = temp;
            Searcher.updateAddedConnection(c);
        }
    }

    public static synchronized void OutgoingHostsremoveConnection(Connection c) {
        OutgoingHostsremoveConnection(c.getIPAddress());
    }

    public static synchronized void OutgoingHostsremoveConnection(IPAddress ip) {
        if (!(OutgoingHostisNull()) && OutgoingHostsisLive(ip)) {
            Connection[] temp = new Connection[OutgoingHosts.length - 1];
            int j = 0;
            for (int i = 0; i < OutgoingHosts.length; i++) {
                if (ip.equals(OutgoingHosts[i].getIPAddress())) {
                    continue;
                }
                temp[j] = OutgoingHosts[i];
                j++;
            }
            OutgoingHosts = temp;
            Searcher.updateRemovedConnection(ip);
        }
    }

    public static synchronized Connection OutgoingHostsgetConnection(int i) {
        if ((!OutgoingHostisNull() && (i < OutgoingHostsgetCount()))) {
            return OutgoingHosts[i];
        } else {
            return null;
        }
    }

    public static synchronized Connection OutgoingHostsgetConnection(IPAddress ip) {
        Connection c = null;
        for (int i = 0; i < OutgoingHosts.length; i++) {
            if (ip.equals(OutgoingHosts[i].getIPAddress())) {
//                System.out.println("Host array : bingo 1");
                c = OutgoingHosts[i];
            }
        }
        return c;
    }

    public static synchronized Connection OutgoingHostsgetConnection(String ip) {
        Connection c = null;
        for (int i = 0; i < OutgoingHosts.length; i++) {
            if (ip.equals(OutgoingHosts[i].getIPAddress().toString())) {
//                System.out.println("Host array : bingo 2");
                c = OutgoingHosts[i];
            }
        }
        return c;
    }

    public static synchronized boolean OutgoingHostsisLive(String ipString) {
        if (!OutgoingHostisNull()) {

           // System.out.println("OUT GOING HOSTLENGHT LA : "+ OutgoingHosts.length);
            for (int i = 0; i < OutgoingHosts.length; i++) {
                InetAddress inet = OutgoingHosts[i].getSocket().getInetAddress();
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

    public static synchronized boolean OutgoingHostsisLive(Connection c) {
        return (OutgoingHostsisLive(c.getIPAddress()));
    }

    public static synchronized boolean OutgoingHostsisLive(IPAddress ip) {
//        try {
//            System.out.println("IP to compare : " + ip.toString());
            for (int i = 0; i < OutgoingHosts.length; i++) {
//                System.out.println("ip in hosts : " + OutgoingHosts[i].getIPAddress().toString());
                if (ip.equals(OutgoingHosts[i].getIPAddress())) {
                    return true;
                }
            }
            return false;



    }

}

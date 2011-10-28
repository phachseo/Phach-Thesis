package Handler;

import Packet.Pong;
import Packet.Ping;
import Client.IPAddress;
import Client.Mine;
import Network.NetworkManager;
import Server.SharedDirectory;
import java.util.*;

public class PingHandler extends Thread {

    public static Map pt;  //ping table    
    Ping ping;

    public PingHandler(IPAddress pingIP, Ping ping) {
        this.ping = ping;
        ping.setIP(pingIP);  //set ping's IP Address
    }

    public static void initPingTable() {
        pt = new Hashtable(5000);
    }

    public void run() {
//	if (!pt.containsKey(ping))  //check that ping is not already in table
//	    {
//       if(!Searcher.pingtable.containsKey(ping.getMessageID())){
//            Searcher.pingtable.put(ping.getMessageID(), 1);
           // System.out.println(" dia chi ping cua goi tin la : "+ ping.getIP());
            NetworkManager.writeButOne(ping.getIP(), ping);
            //pt.put((Packet) ping, ping);
            //    System.out.println("so luong file 1:"+ SharedDirectory.getOurNumFiles());
            //      System.out.println("port phachseo1:"+ Mine.getPort());

            Pong response = new Pong(Mine.getPort(), Mine.getIPAddress(), SharedDirectory.getOurNumFiles(),
                    SharedDirectory.getOurKb(), ping.getMessageID());

            NetworkManager.writeToOne(ping.getIP(), response);
//	    }
//        }
    }
}

package Server;

import Network.Host;
import Network.NetworkManager;
import Handler.ResponseHandler;
import Handler.PingHandler;
import Handler.QHitHandler;
import Handler.Formgrouphandler;
import Handler.QHandler;
import Handler.PongHandler;
import Packet.Packet;
import Packet.Response;
import Packet.Formgroup;
import Packet.Pong;
import Packet.Query;
import Packet.Ping;
import Packet.QueryHit;
import Client.IPAddress;
import Client.Pinger;
import Client.Connection;
import Client.HostCache;
import MRonP2P.Searcher;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Server extends Thread {
    //    SearchPanel.FormAction parent;

    BufferedInputStream in;
    IPAddress mine;
    // Response response;
    //SearchPanel.FormAction parent;
    public static ArrayList<Integer> idList;

    public Server(Connection c) {
        in = c.getByteReader();
        mine = c.getIPAddress();
        idList = new ArrayList<Integer>();
    }

    public static void add(int group) {
        idList.add(group);

    }

    public void run() {
        while (true) {
            try {
                if (in.available() < Packet.HEADER_LENGTH) {
                    continue;
                }
                byte[] temp = new byte[Packet.HEADER_LENGTH];

                for (int i = 0; i < Packet.HEADER_LENGTH; i++) {
                    temp[i] = (byte) in.read();
                }



                Packet header = new Packet(temp);


                if ((header.identify() != Packet.PONG) && (header.identify() != Packet.PING)
                        && (header.identify() != Packet.QUERY) && (header.identify() != Packet.QUERYHIT)
                        && (header.identify() != Packet.FORMGROUP_REQUEST) && (header.identify() != Packet.RESPONSE)) {
                    break; // If the data is not something we expect, die.
                }


                byte[] newpacket = new byte[(header.length() + Packet.HEADER_LENGTH)]; /* The syntax here is unfortunate, because headers don't store
                their own size. */


                header.decrementTtl();
                header.incrementHops();

                for (int i = 0; i < Packet.HEADER_LENGTH; i++) // First fill the packet with the header.
                {
                    newpacket[i] = temp[i];
                }
                for (int i = Packet.HEADER_LENGTH; i < (header.length() + Packet.HEADER_LENGTH); i++) // Then fill the rest.
                {
                    newpacket[i] = (byte) in.read();
                }

                //

                if (header.getTtl() < 0) // Kill old packets (but only after we've removed them from the input stream).
                {
                    continue;
                }

                if (header.identify() == Packet.PING) // We don't have to do any packet construction if all we've got is a Ping.
                {
                    Ping ping = new Ping(newpacket);
                    //   System.out.println("1");
                   
                    boolean isPacketContainedInHashtable = false;
                    Enumeration e = Searcher.pingtable.keys();
                    while (e.hasMoreElements()) {
                        Packet savedPacket = (Packet) e.nextElement();
                        if (ping.compare(savedPacket)) {
                            isPacketContainedInHashtable = true;
                            break;
                        }
                    }
                    if (!isPacketContainedInHashtable) {
                        Searcher.pingtable.put((Packet) ping, ping);
                        
                         System.out.println("dia chi? ping toi' la` :"+ ping.getIP().toString());
                        
//                        String pingIPString = ping.getIP().toString();
//                        int pingPort = ping.getPort();
//                        Host h = new Host(pingIPString, pingPort);
//
//                        if(!HostCache.isPresent(h)){
//                        HostCache.addHost(h);
//                        }
//                        
                        


                        PingHandler handler = new PingHandler(ping.getIP(), ping);
                        //  handler.server = this;
                        handler.start();
                    }
                    continue;
//                    PingHandler handler = new PingHandler(mine, ping);
//                    //     System.out.println("2");
//                    handler.start();
//                    //      System.out.println("3");
//                    continue;
                }
                if (header.identify() == Packet.FORMGROUP_REQUEST) {
           //     System.out.println("Dia chi Mine la : "+ mine);
                    Formgroup form = new Formgroup(newpacket);
                                                                           
                  //  System.out.println("dia chi nhan dc la : "+ form.getIP().toString());
                    form.decrementTtl();
                    form.incrementHops();

                    boolean isPacketContainedInHashtable = false;
                    Enumeration e1 = Searcher.HashtableID.keys();
                    while (e1.hasMoreElements()) {
                        Packet savedPacket = (Packet) e1.nextElement();
                        if (form.compare(savedPacket)) {
                            isPacketContainedInHashtable = true;
                            JOptionPane.showMessageDialog(null, "doi lai");
                            break;
                        }
                    }
                    if (!isPacketContainedInHashtable) {
                        Searcher.HashtableID.put((Packet) form, form);
                        Formgrouphandler handler = new Formgrouphandler(form.getIP(), form);
                        handler.server = this;
                        handler.start();
                        
                    }
                    continue;
                }
                if (header.identify() == Packet.RESPONSE) {
                    //  System.out.println("phachseo1");
                   // System.out.println("Nhan dc phan hoi");
                    Response res = new Response(newpacket);
                 //   System.out.println("DIA CHI GOI TIN RESPONSE NHAN DC LA : "+ res.getIP().toString());

                    ResponseHandler handler = new ResponseHandler(res.getIP(), res);
                    handler.start();
                    
                  //  System.out.println("khoi dong thread phan hoi");

                    //  Pinger.formgroup(res);
                    continue;
                }
                if (header.identify() == Packet.PONG) {
                
                    Pong pong = new Pong(newpacket);
                        System.out.println(" NHAN DC GOI PONG: "+ pong.getIP());
                    Host h = new Host( pong.getIP().toString(), pong.getPort());
                    if(!HostCache.isPresent(h)){
                        HostCache.addHost(h);
                        }
                    PongHandler handler = new PongHandler(pong.getIP(), pong);
                    handler.start();
                    Pinger.inform(pong);
                    continue;
                } else if (header.identify() == Packet.QUERY) {
                    Query query = new Query(newpacket);
                    QHandler handler = new QHandler(mine, query);
                    handler.start();
                    continue;
                } else {
                    QueryHit queryhit = new QueryHit(newpacket);
                    QHitHandler handler = new QHitHandler(mine, queryhit);
                    handler.start();
                    Searcher.inform(mine, queryhit);
                }
            } catch (Exception e) // If there's a problem, we just die.
            {
                System.out.println("Exception in Server : " + e.getLocalizedMessage());
                break;
            }
        }
        NetworkManager.notify(mine);
    }
}

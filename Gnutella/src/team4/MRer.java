package team4;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**int
 *
 * @author phachseo
 */
import java.io.*;
import java.lang.Integer;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class MRer extends Thread {

    //   private String myip;
    //   private int myport;
    private int mygroupID;
    private int filesize = 0;
    Groupcache cache;
    static Hashtable sumhash = new Hashtable();
    static int hashtableReceiveCount = 0;
    //   public String GREETING = "GNUTELLA CONNECT/0.4";
    //   public byte[] greeting = (GREETING + "\n\n").getBytes();
    static int numberofhosts = 0;

    public MRer(int GroupID) {
        //   myip = ip;
        //  myport = port;
        mygroupID = GroupID;

    }

    public static synchronized void addhash(Hashtable hashtable) throws FileNotFoundException {

        hashtablepro.add(hashtable, sumhash);
        hashtableReceiveCount++;

        if (hashtableReceiveCount == numberofhosts) {
            hashtablepro.out(sumhash);

            PrintWriter out = new PrintWriter(new File("phach.txt"));
            Enumeration q = sumhash.keys();
            //Collection m= hastable2.values();
            while (q.hasMoreElements()) {
                String temp = (String) q.nextElement();
                //  System.out.println(temp+" "+hastable2.get(temp));

                out.write(temp + " ");
                out.write(sumhash.get(temp) + "\r\n");


            }
            out.close();

        }
    }

    public void run() {

        // try {
        System.out.println("Starting Mapreduce Job");
//            Socket a = new Socket(myip, myport);

        // Connection MRconnection = new Connection(a, Connection.MRING);



//            MRconnection.getByteWriter().write(greeting, 0, greeting.length);
        //MRconnection.getByteWriter().flush();

        //  connection.getByteWriter().write(greeting, 0, greeting.length);
        //   connection.getByteWriter().flush();

        int d = Groupcache.getCount();
        System.out.println("phachseo la :" + d);
        for (int i = 0; i < Groupcache.getCount(); i++) {
            Group group = Groupcache.Groups.get(i);
            System.out.println("ID 8 la:" + group.GroupID);
            System.out.println("ID 9 la:" + mygroupID);
            System.out.println("Size group la: " + Groupcache.getCount());
            if (mygroupID == group.GroupID) {
                ArrayList<Host> hosts = group.Hosts;
                  numberofhosts = hosts.size();
                for (int j = 0; j < hosts.size(); ++j) {
                    System.out.println("J la: " + j);
                    System.out.println("Hostsize la:" + hosts.size());

                    Host host = hosts.get(j);
                    // Host host = Group.Hosts.get(j);
                    System.out.println("port ben kia la:" + host.getPort());

                    System.out.println("name la:" + host.getName());
                    MapReducethread newthread = new MapReducethread(host.getPort(), host.getName(), mygroupID);
                    newthread.mrer = this;
                    newthread.start();
                    // break;

                }
                break;
//                else continue;
            }




        }


        //      } catch (IOException e) {
        //        System.out.println("Unable to do the job.");
        //    }


    }
}

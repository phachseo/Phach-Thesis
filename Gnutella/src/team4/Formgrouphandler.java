/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team4;

import java.util.*;

import java.awt.Dialog;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author phachseo
 */
public class Formgrouphandler extends Thread {

    public static Map pt;  //ping table
    Formgroup formgroup;
    Server server;
    // Server parent;
    // Preferences parent;

    public Formgrouphandler(IPAddress IP, Formgroup formgroup) {
        this.formgroup = formgroup;
        formgroup.setIP(IP);  //set formgroup's IP Address

    }
    public static Arrays[] phach;
    public static int temp;

    public void run() {
//        String temphash = formgroup.getMessageID().toString();
//    System.out.println("Hashtable da co la :");
//               hashtablepro.out(Searcher.HashtableID);
//            System.out.println("Key la :"+ temphash);

//        if (!Searcher.HashtableID.containsKey(formgroup.getMessageID())) {
//
////           
//            Searcher.HashtableID.put(formgroup.getMessageID(), "aaa");
////            hashtablepro.out(Searcher.HashtableID);
        System.out.println("chay vao formgroup handler");
              System.out.println("dia chi nhan dc thu 2 la : "+ formgroup.getIP().toString());
        int n = JOptionPane.showConfirmDialog(
                null,
                "Would you like to join the MapReduce Group ?",
                "Inform Group",
                JOptionPane.YES_NO_OPTION);

        //    byte[] e = Packa
        if (n == JOptionPane.YES_OPTION) {

            // Group = new group[5];

// MRListener mrlistent = new MRListener();
            //    mrlistent.start();

            //ArrayList l = new ArrayList<Integer>();

            int a = formgroup.getID();
            // System.out.println("port 2 la:" + formgroup.getPort());
            //   l.add(new ID(a));
            server.add(a);
          //  System.out.println(" a la`:" + a);
            int d = 0;
            // System.out.println("ID 3 la:" + server.idList.get(0));
          //  System.out.println("Size cua tui la:" + server.idList.size());

            temp = Mine.getPort() + server.idList.size();

            for (int i = 0; i < server.idList.size(); i++) {
                int b = server.idList.get(i);


                if (formgroup.getID() == b) {

                   // System.out.println(" port cua tui la:" + temp);
                    Response r = new Response(true, temp, Mine.getIPAddress(), a);
                    // System.out.println("port 3 la:" +port);
            //     System.out.println("Dia chi IP cua goi tin nhan dc la : "+ formgroup.);
                    NetworkManager.writeToOne(formgroup.getIP(), r);
                    
                    //System.out.println("goi goi tin ve");
                    MRListener mrlistent = new MRListener(temp);
                    mrlistent.start();
                  //  System.out.println(" mo? port ben server" + temp);
                    //  port++;
                }
                //   break;

                //    Pinger.formgroup(r);
            }
        }

        if (n == JOptionPane.NO_OPTION) {
            Response r = new Response(false, Mine.getPort(), Mine.getIPAddress(), 0);
            NetworkManager.writeToOne(formgroup.getIP(), r);
        }
//            Formgroup formgroup2 = new Formgroup(formgroup.getID(), formgroup.getPort(),formgroup.getIP());
        NetworkManager.writeButOne(formgroup.getIP(), formgroup);
    }
}

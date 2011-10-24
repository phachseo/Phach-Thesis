/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team4;

import java.util.Map;

/**
 *
 * @author phachseo
 */
public class ResponseHandler extends Thread {

    public static Map pt;  //ping table
    Response response;
    public static int temp;
    
    public ResponseHandler(IPAddress IP, Response response) {
        this.response = response;
        response.setIP(IP);  //set ping's IP Address
    }

    public void run() {

       // System.out.println("phachseo2");
        if (response.getAllow()) {
            System.out.println("nhan dc phan hoi");
             Searcher.updateGroupInfo(response.getID());
            String ipname = response.getIP().toString();
            int port = response.getPort();
            int id = response.getID();
            System.out.println("port 4 la:"+port);

            //    l.add(new ID(a));
            System.out.println("ID 4 la:" + id);
            Host newhost = new Host(ipname, port, id);
            //Hostcache2.addHost(newhost);

            // Groupcach = new
            Groupcache.addHosttoGroup(newhost);
            
//            for(int j=0;j< Groupcache.getCount();j++)
//            {
                temp=Mine.getPort()+ Groupcache.getCount();

//            }
                System.out.println("response handler : port = " + temp);
                MRListener mrlisten = new MRListener(temp);
            mrlisten.start();

          //     System.out.println("ID 5 la: "+ Groupcache.Groups.get(0).GroupID);
        } else {
        }
    }
}

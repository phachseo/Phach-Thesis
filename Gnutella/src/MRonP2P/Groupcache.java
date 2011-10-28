/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MRonP2P;

import Network.Host;
import java.util.ArrayList;

/**
 *
 * @author phachseo
 */
public class Groupcache {

    public static ArrayList<Group> Groups;

    public Groupcache() {
        Groups = new ArrayList<Group>();

    }

 

    public static synchronized void addHosttoGroup(Host hostname) {

        for (int i = 0; i <= Groups.size(); i++) {
      //      System.out.println("ID 5:"+ Groups.get(i).GroupID+"\n");

             // System.out.println("ID 6:"+ hostname.GroupID+"\n");

            if( Groups.get(i).GroupID == hostname.GroupID){
                Group group = Groups.get(i);
                group.add(hostname);
                break;
            }
        }


    }

    public static synchronized int getCount() {
                    return Groups.size();
        }


    public static void addGroup(Group group) {
        Groups.add(group);

    }
}

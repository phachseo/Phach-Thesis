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
public class Group {

    public int GroupID;
    public static ArrayList <Host> Hosts;
    public String[] work;

    public Group(int agroup) {

        this.GroupID = agroup;
        Hosts = new ArrayList<Host>();


    }
    public void add (Host hostname){
    Hosts.add(hostname);
    }
}

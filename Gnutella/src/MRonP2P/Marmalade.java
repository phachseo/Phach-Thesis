package MRonP2P;

import Client.Searcher;
import Server.SharedDirectory;
import Server.Listener;
import Client.Mine;
import Handler.PingHandler;
import Handler.QHandler;
import Client.Preferences;
import Client.PeriodicConnector;
import Client.Pinger;
import java.io.File;
 
public class Marmalade {

    public static void main(String[] args) {


        System.out.println("phachseo");

        System.out.println("Setting up hash tables...");
        QHandler.initQueryTable();
        PingHandler.initPingTable();

        //Preferences.initHashTable(Mine.getIPAddress());
        System.out.println("Determining network address...");
        Mine.updateAddress();

        System.out.println("Reading preferences file...");
        new Searcher();

        Preferences.readFromFile();
        System.out.println("Setting up file table...");

        new SharedDirectory(Preferences.SHAREPATH, Preferences.SAVEPATH);

        Listener listener = new Listener();
        listener.start(); // Beginning listening for network connections

        PeriodicConnector periodicconnector = new PeriodicConnector(Preferences.AUTO_CONNECT); // Begin actively trying to connect
        periodicconnector.start();

        Pinger pinger = new Pinger();
        pinger.start(); // Start sending out periodic pings.
    }
}

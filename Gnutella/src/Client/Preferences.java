package Client;
import Client.HostCache;
import Network.Host;
import java.io.*;
import java.util.*;


public class Preferences {

    public static String FILE_NAME = "D:\\Phach-Thesis\\Gnutella\\src\\MRonP2P\\preferences.txt";
    public static int MAX_LIVE = 5;
    public static int MAX_CACHE = 100;
    public static boolean AUTO_CONNECT = true;
    public static int PINGER_TIME = 10000;
    public static int CONNECTOR_TIME = 10000;
    public static String SHAREPATH = "C:\\temp";
    public static String SAVEPATH = "C:\\temp1";
    static SearchPanel form;
    public static Hashtable messagehashID;
    // public static Preferences parent;
//  public static void initHashTable()
//    {
//        Formgrouphandler formgrouphandler;
//	messagehashID = new Hashtable();
//      form = new SearchPanel();
//        form .parent= this;
//        formgrouphandler = new Formgrouphandler(mine, null);
//        formgrouphandler.parent = this;
//        
//    }

    public static void readFromFile() {
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = fileIn.readLine()) != null) {
                //System.out.print("adff");
                if (line.startsWith("Host: ")) {
                    String address = line.substring(6);
                    StringTokenizer t = new StringTokenizer(address, ":");
                    Host h = new Host(t.nextToken(), Integer.parseInt(t.nextToken()));
                    HostCache.addHost(h);
                    continue;
                }
                if (line.startsWith("Max-Live: ")) {
                    MAX_LIVE = Integer.parseInt(line.substring(10));
                    continue;
                } else if (line.startsWith("Max-Cache: ")) {
                    MAX_CACHE = Integer.parseInt(line.substring(11));
                    continue;
                } else if (line.startsWith("Auto-Connect: ")) {
                    AUTO_CONNECT = ((Boolean.valueOf(line.substring(14))).booleanValue());
                    continue;
                } else if (line.startsWith("Pinger-Time: ")) {
                    PINGER_TIME = Integer.parseInt(line.substring(13));
                    continue;
                } else if (line.startsWith("Connector-Time: ")) {
                    CONNECTOR_TIME = Integer.parseInt(line.substring(16));
                    continue;
                } else if (line.startsWith("Shared-Directory: ")) {
                    SHAREPATH = line.substring(18);
                    System.out.println("Shared-Directory is " + SHAREPATH);
                    continue;
                } else if (line.startsWith("Download-Directory: ")) {
                    SAVEPATH = line.substring(20);
                    System.out.println("Download-Directory is " + SAVEPATH);
                    continue;
                }
            }
            fileIn.close();
        } catch (IOException e) {
            System.out.println("Unable to read preferences file");
        }
    }

    public static void writeToFile() {
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter(FILE_NAME));
            for (int i = 0; i < HostCache.getCount(); i++) {
                fileOut.println("Host: " + HostCache.hosts[i].getName() + ":" + HostCache.hosts[i].getPort());
            }
            fileOut.println("Max-Live: " + MAX_LIVE);
            fileOut.println("Max-Cache: " + MAX_CACHE);
            fileOut.println("Auto-Connect: " + AUTO_CONNECT);
            fileOut.println("Shared-Directory: " + SHAREPATH);
            fileOut.println("Download-Directory: " + SAVEPATH);

            System.out.println("Written to file");
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Unable to write to preferences file");
        }
    }

    /*atic class SearchPanel extends JPanel {

        private File fileName;
        private int numLines;
        private String[] data;
        private BufferedReader fileInput;
        private static DefaultTableModel groupTable;
        private static DefaultTableModel searchModel;
        private static DefaultTableModel downloadModel;
        private static JTextField searchField;
        private static JTable table;
        private static JTable downloadtable;
        private static JTable mrtable;
        private static DefaultTableModel statsModel;
        Groupcache groupcache = new Groupcache();
        static int number = 0;

        public SearchPanel() {
        }*/
    }



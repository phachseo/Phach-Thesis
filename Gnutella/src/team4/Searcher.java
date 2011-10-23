package team4;

/**
Big hairy GUI
 */
//import team4.Packet.Query;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class Searcher {
    // private static int Speed has to come from the user.

    static LinkedList searches = new LinkedList();
    static SearchPanel mypanel;
    static ConnectionPanel myconnectionpanel;
    static MonitorPanel mymonitorpanel;
    Formgroup form;
    //added constructor
    public static Hashtable HashtableID;
    public static Hashtable pingtable;
    public static Hashtable pongtable;

    public Searcher() {
        TabbedPaneFrame frame = new TabbedPaneFrame();
        mypanel = frame.getSearchPanel();
        myconnectionpanel = frame.getConnectionPanel();
        mymonitorpanel = frame.getMonitorPanel();
        frame.show();
        HashtableID = new Hashtable();
        pingtable = new Hashtable();
    }

    public static void addSearch(Query search) // Called when the user presses the "Search" button
    {
        searches.add(search);
    }

    public static void clear() // Called when the user presses the "Clear" button
    {
        searches = new LinkedList();
    }

    public static void addFileTransfer(IPAddress ip, String name) {
        mypanel.addFileTransfer(ip, name);
    }

    public static void updateConnectionStatus(IPAddress ip, String name, String status) {
        mypanel.updateConnectionStatus(ip, name, status);
    }

    public static void updateFileTransferStatus(IPAddress ip, String name, int percent) {
        mypanel.updateFileTransferStatus(ip, name, percent);
    }

    public static void inform(IPAddress ip, QueryHit qh) {
        Integer port = new Integer(qh.getPort());
        String myip = qh.getIP().toString();

        Iterator iter = searches.iterator();
        while (iter.hasNext()) {
            Query b = (Query) iter.next();
            if (qh.compare(b)) {
                ResultSet r = qh.getResults();
                while (r.more()) {
                    Integer index = new Integer(r.getIndex());
                    Integer size = new Integer(r.getFilesize());
                    String name = r.getName();
                    mypanel.addQHit(index, name, size, myip, port);
                }
            }
        }
    }

    public static void inform(Query q) {
        mymonitorpanel.addQuery(q.getIP().toString(), q.getIP().getPort(), q.getSearchString());
    }

    public static void updateGroupInfo(int groupid) {
        mypanel.updatestatgroup(groupid);

    }

    public static void updateInfo(int hosts, int totalkb, int totalfiles) {
        myconnectionpanel.updateStats(hosts, totalfiles, totalkb);
    }

    public static void updateAddedConnection(Connection c) {
        myconnectionpanel.addAConnection(c.getIPAddress().toString(), c.getIPAddress().getPort(), c.getTypeString(), "Connected");
    }

    public static void updateRemovedConnection(IPAddress ip) {
        myconnectionpanel.removeAConnection(ip.toString(), ip.getPort(), "Disconnected");
    }

    public static void updateHostCache(Host h, boolean flag) {
        if (flag) {
            myconnectionpanel.addAHostInCache(h.getName(), h.getPort());
        } else {
            myconnectionpanel.removeAHostInCache(h.getName(), h.getPort());
        }
    }
}

class TabbedPaneFrame extends JFrame implements ChangeListener {

    private JTabbedPane tabbedPane;
    private ConnectionPanel cPanel;
    private SearchPanel sPanel;
    private MonitorPanel mPanel;
    private JMenuItem newMarmalade;
    private JMenuItem exitMarmalade;

    public TabbedPaneFrame() {
        setTitle("Marmalade");

        // get screen dimensions
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int inset = 100;
        setBounds(inset, inset, 800, 550);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        newMarmalade = fileMenu.add("New");
        exitMarmalade = fileMenu.add("Exit");

        MenuHandler m = new MenuHandler();
        newMarmalade.addActionListener(m);
        exitMarmalade.addActionListener(m);

        // JMenu optionsMenu = new JMenu ("Options");
        //menuBar.add(optionsMenu);

        cPanel = new ConnectionPanel();
        sPanel = new SearchPanel();
        mPanel = new MonitorPanel();

        //Border etched = BorderFactory.createEtchedBorder();
        //Border titled = BorderFactory.createTitledBorder(line, "Connections");
        Border line = BorderFactory.createLineBorder(Color.black);
        cPanel.setBorder(line);
        sPanel.setBorder(line);
        mPanel.setBorder(line);

        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(this);
        tabbedPane.addTab("Connections", cPanel);
        tabbedPane.addTab("Search", sPanel);
        tabbedPane.addTab("Monitor", mPanel);

        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                //Preferences.writeToFile();
                System.exit(0);
            }
        });

        getContentPane().add(tabbedPane, "Center");
    }

    /**
    Inner class - MenuHandler
     */
    class MenuHandler implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == newMarmalade) {
            } else if (evt.getSource() == exitMarmalade) {
                Preferences.writeToFile();
                System.exit(0);
            }
        }
    }

    public void stateChanged(ChangeEvent event) {
        JTabbedPane pane = (JTabbedPane) event.getSource();
    }

    public ConnectionPanel getConnectionPanel() {
        return (cPanel);
    }

    public SearchPanel getSearchPanel() {
        return (sPanel);
    }

    public MonitorPanel getMonitorPanel() {
        return (mPanel);
    }
}

class ConnectionPanel extends JPanel {

    private static DefaultTableModel liveModel;
    private static DefaultTableModel cacheModel;
    private static DefaultTableModel statsModel;
    private static JTable liveTable;
    private static JTable cacheTable;
    private static JTable statsTable;
    private static JTextField ipField;
    private JTable table;

    public ConnectionPanel() {
        setLayout(null);
        ipField = new JTextField(20);
        add(ipField);
        ipField.setBounds(50, 200, 250, 25);

        JButton connectButton = new JButton("Connect");
        add(connectButton);
        connectButton.setBounds(320, 200, 150, 25);
        connectButton.addActionListener(new ConnectAction());

        liveModel = new DefaultTableModel(0, 0);
        liveModel.addColumn((Object) "Remote host");
        liveModel.addColumn((Object) "Port");
        liveModel.addColumn((Object) "Type");
        liveModel.addColumn((Object) "Status");

        liveTable = new JTable(liveModel);

        JScrollPane scroll = new JScrollPane(liveTable);
        scroll.setBackground(Color.blue);
        add(scroll);
        scroll.setBounds(50, 50, 700, 120);

        JButton deleteConnection = new JButton("Delete Connection");
        add(deleteConnection);
        deleteConnection.setBounds(575, 200, 175, 25);
        deleteConnection.addActionListener(new DeleteConnectionAction());

        statsModel = new DefaultTableModel(0, 0);
        statsModel.addColumn((Object) "Hosts");
        statsModel.addColumn((Object) "Total Files");
        statsModel.addColumn((Object) "Total kB");

        statsTable = new JTable(statsModel);

        JScrollPane statsScroll = new JScrollPane(statsTable);
        statsScroll.setBackground(Color.blue);
        add(statsScroll);
        statsScroll.setBounds(50, 280, 300, 50);

        Object[] newStatsRow = new Object[3];
        statsModel.addRow(newStatsRow);



        cacheModel = new DefaultTableModel(0, 0);
        cacheModel.addColumn((Object) "Remote host");
        cacheModel.addColumn((Object) "Port");

        cacheTable = new JTable(cacheModel);
        //downloadtable.getTableHeader().setBackground(Color.black);
        //downloadtable.getTableHeader().setForeground(Color.black);

        JScrollPane cacheScroll = new JScrollPane(cacheTable);
        cacheScroll.setBackground(Color.blue);
        add(cacheScroll);
        cacheScroll.setBounds(450, 280, 300, 150);

        JButton delete = new JButton("Delete host");
        add(delete);
        delete.setBounds(525, 440, 150, 25);
        delete.addActionListener(new DeleteAction());

    }

    public static void addAConnection(String host, int port, String type, String status) {
        Object[] newRow = new Object[4];
        newRow[0] = host;
        newRow[1] = new Integer(port);
        newRow[2] = type;
        newRow[3] = status;
        liveModel.insertRow(0, newRow);
    }

    public static void removeAConnection(String host, int port, String status) {
        for (int i = 0; i < liveModel.getRowCount(); i++) {
            String ip = (String) (liveModel.getValueAt(i, 0));
            if (ip.equals(host)) {
                liveModel.removeRow(i);
            }
        }
    }

    public static void addAHostInCache(String host, int port) {
        Object[] newRow = new Object[2];
        newRow[0] = host;
        newRow[1] = new Integer(port);
        cacheModel.insertRow(0, newRow);
    }

    public static void removeAHostInCache(String host, int port) {
        for (int i = 0; i < cacheModel.getRowCount(); i++) {
            String h = (String) (cacheModel.getValueAt(i, 0));
            if (host.equals(h)) {
                cacheModel.removeRow(i);
            }
        }
    }

    public static void updateStats(int hosts, int files, int kb) {
        statsModel.setValueAt(new Integer(hosts), 0, 0);
        statsModel.setValueAt(new Integer(files), 0, 1);
        statsModel.setValueAt(new Integer(kb), 0, 2);
    }

    class ConnectAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            String ip = ipField.getText();
            StringTokenizer st = new StringTokenizer(ip, ":");
            if (st.countTokens() == 2) {
                ip = st.nextToken();
                int port = Integer.parseInt(st.nextToken());
                Connector connector = new Connector(ip, port);
                connector.start();
            }
        }
    }

    class DeleteAction implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            int rowIndex = cacheTable.getSelectedRow();
            String ip = (String) cacheTable.getValueAt(rowIndex, 0);
            Integer port = (Integer) cacheTable.getValueAt(rowIndex, 1);
            Host h = new Host(ip, port.intValue());
            HostCache.removeHost(h);
        }
    }

    class DeleteConnectionAction implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            int rowIndex = liveTable.getSelectedRow();
            String ip = (String) liveTable.getValueAt(rowIndex, 0);
            Integer port = (Integer) liveTable.getValueAt(rowIndex, 1);
            StringTokenizer s = new StringTokenizer(ip, ".");
            int ip1 = Integer.parseInt(s.nextToken());
            int ip2 = Integer.parseInt(s.nextToken());
            int ip3 = Integer.parseInt(s.nextToken());
            int ip4 = Integer.parseInt(s.nextToken());
            IPAddress ipaddress = new IPAddress(ip1, ip2, ip3, ip4, port.intValue());
            NetworkManager.notify(ipaddress);
        }
    }
}

class SearchPanel extends JPanel {
    // Preferences parent;
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
        setLayout(null);
        searchField = new JTextField(30);
        add(searchField);
        searchField.setBounds(50, 25, 300, 25); //(sets location and size of search field)

        JButton searchButton = new JButton("Search");
        add(searchButton);
        searchButton.setBounds(400, 25, 150, 25);
        searchButton.addActionListener(new SearchAction());

        JButton clearButton = new JButton("Clear");
        add(clearButton);
        clearButton.setBounds(600, 25, 150, 25);
        clearButton.addActionListener(new ClearAction());

        JButton download = new JButton("Download");
        add(download);
        download.setBounds(100, 290, 150, 25);
        download.addActionListener(new DownloadAction());

        JButton open = new JButton("Choose file");
        //open.addActionListener(this);
        add(open);
        open.setBounds(200, 290, 150, 25);
        open.addActionListener(new OpenAction());



        JButton MapReduce = new JButton("Mapreduce");
        add(MapReduce);
        MapReduce.setBounds(300, 290, 150, 25);
        MapReduce.addActionListener(new MRAction());

        JButton FormGroup = new JButton("FormGroup");
        add(FormGroup);
        FormGroup.setBounds(500, 290, 150, 25);
        FormGroup.addActionListener(new FormAction());

        searchModel = new DefaultTableModel(0, 0);
        searchModel.addColumn((Object) "File Index");
        searchModel.addColumn((Object) "File Name");
        searchModel.addColumn((Object) "File Size");
        searchModel.addColumn((Object) "IP Address");
        searchModel.addColumn((Object) "Port");

        groupTable = new DefaultTableModel(0, 0);
        groupTable.addColumn((Object) "Group");


        downloadModel = new DefaultTableModel(0, 0);
        downloadModel.addColumn((Object) "IP Address");
        downloadModel.addColumn((Object) "File");
        downloadModel.addColumn((Object) "Connection Status");
        downloadModel.addColumn((Object) "File Transfer Progress");

        table = new JTable(searchModel);
        //table.getTableHeader().setBackground(Color.black);
        //table.getTableHeader().setForeground(Color.red);

        downloadtable = new JTable(downloadModel);
        //downloadtable.getTableHeader().setBackground(Color.black);
        //downloadtable.getTableHeader().setForeground(Color.orange);

        mrtable = new JTable(groupTable);

        Object[] newStatsRow = new Object[1];
        groupTable.addRow(newStatsRow);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll);
        scroll.setBounds(50, 70, 700, 130);

        scroll = new JScrollPane(downloadtable);
        add(scroll);
        scroll.setBounds(50, 330, 700, 130);

        scroll = new JScrollPane(mrtable);
        add(scroll);
        scroll.setBounds(650, 200, 100, 130);
    }

    public static void addQHit(Integer index, String name, Integer size, String ip, Integer port) {
        Object[] newRow = new Object[5];
        newRow[0] = index;
        newRow[1] = name;
        newRow[2] = size;
        newRow[3] = ip;
        newRow[4] = port;
        searchModel.addRow(newRow);
    }
//public static void addGroup(int GroupID){
    //      Object[] newRow = new Object[GroupID];
    //    mapreduce.addRow(newRow);

//}
    public static void addFileTransfer(IPAddress ip, String name) {
        for (int i = 0; i < downloadtable.getRowCount(); i++) /* If we've already tried downloading the same thing before, update it properly on the table rather
        than adding a new one. */ {
            if (((IPAddress) downloadtable.getValueAt(i, 0)).equals(ip) && ((String) downloadtable.getValueAt(i, 1)).equals(name)) {
                updateConnectionStatus(ip, name, "Connecting...");
            }
        }
        Object[] newRow = new Object[4];
        newRow[0] = ip;
        newRow[1] = name;
        newRow[2] = "Connecting...";
        newRow[3] = "0% Complete";
        downloadModel.addRow(newRow);
    }
//   public boolean accept(File f) {
//        return f.getName().toLowerCase().endsWith(".gif")
//            || f.isDirectory();
//      }

    public static void updateConnectionStatus(IPAddress ip, String name, String status) {
        for (int i = 0; i < downloadtable.getRowCount(); i++) {
            if (((IPAddress) downloadtable.getValueAt(i, 0)).equals(ip) && ((String) downloadtable.getValueAt(i, 1)).equals(name)) {
                downloadModel.setValueAt(status, i, 2);
                break;
            }
        }
    }

    public static void updateFileTransferStatus(IPAddress ip, String name, int percent) {
        for (int i = 0; i < downloadtable.getRowCount(); i++) {
            if (((IPAddress) downloadtable.getValueAt(i, 0)).equals(ip) && ((String) downloadtable.getValueAt(i, 1)).equals(name)) {
                downloadModel.setValueAt((String) (percent + "% Complete"), i, 3);
                break;
            }
        }
    }

    public static void updatestatgroup(int id) {
        //  groupTable.setValueAt(groupTable, 0, 0);
        //  groupTable.setValueAt(new Integer(id), 0, 0);
        Object[] newRow = new Object[1];
        newRow[0] = id;
//        newRow[1] = name;
//        newRow[2] = "Connecting...";
//        newRow[3] = "0% Complete";
        groupTable.addRow(newRow);
    }

    public void getNumberOfLines() {
        int counter = 0;
        // Open the file
        openFile();
        // Loop through file incrementing counter
        try {
            String line = fileInput.readLine();
            while (line != null) {
                counter++;
                System.out.println("(" + counter + ") " + line);
                line = fileInput.readLine();
            }
            numLines = counter;

            int rowIndex = mrtable.getSelectedRow();
            System.out.println(rowIndex);
            Integer groupID = (Integer) mrtable.getValueAt(rowIndex, 0);
            for (int i = 0; i < Groupcache.getCount(); i++) {
                Group group = Groupcache.Groups.get(i);
//            System.out.println("ID 8 la:" + group.GroupID);
//            System.out.println("ID 9 la:" + groupID);
                System.out.println("Size group la: " + Groupcache.getCount());
                if (groupID == group.GroupID) {
                    ArrayList<Host> hosts = group.Hosts;
                    number = hosts.size();

                    int part[] = new int[10];
                    for (int j = 0; j < number; j++) {
                        part[j] = numLines / number;

                        //System.out.print(line);

                    }
                    if (numLines % number != 0) {
                        part[number - 1] = numLines % number;
                    }
//                    for (int k = 0; k < part.length; k++) {

//                        PrintWriter out = new PrintWriter(new File("phach." + k + ".txt"));
//                        for (int l = 0; l < part[k]; l++) {
//                            out.write(line);
//                            out.write("\r\n");
//                        }






//                for (int j = 0; j < hosts.size(); ++j) {
//                    System.out.println("J la: " + j);
//                    System.out.println("Hostsize la:" + hosts.size());
//
//                    Host host = hosts.get(j);
//                    // Host host = Group.Hosts.get(j);
//                    System.out.println("port ben kia la:" + host.getPort());
//
//                    System.out.println("name la:" + host.getName());
//                   
//                    // break;
//
//                }
                        //   break;
//
//                        break;
//                    }
                    closeFile();
                }
            }
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(this, "Error reading File",
                    "Error 5: ", JOptionPane.ERROR_MESSAGE);
            closeFile();
            System.exit(1);
        }
    }

    public void closeFile() {
        if (fileInput != null) {
            try {
                fileInput.close();
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(this, "Error Opening File",
                        "Error 4: ", JOptionPane.ERROR_MESSAGE);
            }
        }
        System.out.println("File closed");
    }

    public void openFile() {
        try {
            FileReader file = new FileReader(fileName);
            fileInput = new BufferedReader(file);
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(this, "Error Opening File",
                    "Error 4: ", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("File opened");
    }

    public void getFileName() {
        // Display file dialog so user can select file to open
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);

        // If cancel button selected return
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        // Obtain selected file

        fileName = fileChooser.getSelectedFile();
//
//        if (checkFileName()) {
//            openButton.setEnabled(false);
//            readButton.setEnabled(true);
//        }
    }

    public void readFile() {
        // Disable read button
        // readButton.setEnabled(false);

        // Dimension data structure
        getNumberOfLines();
        data = new String[numLines];

        // Read file
        readTheFile();
        // Output to text area	
//        textArea.setText(data[0] + "\n");
//        for (int index = 1; index < data.length; index++) {
//            textArea.append(data[index] + "\n");
//        }
        // Rnable open button
        // openButton.setEnabled(true);
    }

    private void readTheFile() {
        openFile();
        System.out.println("Read the file");
        try {
            for (int index = 0; index < data.length; index++) {
                data[index] = fileInput.readLine();
                System.out.println(data[index]);
                //System.out.println("so dong "+numLines);
            }
            closeFile();
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(this, "Error reading File",
                    "Error 5: ", JOptionPane.ERROR_MESSAGE);
            closeFile();
            System.exit(1);
        }
    }

    class DownloadAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            int rowIndex = table.getSelectedRow();
            Integer index = (Integer) table.getValueAt(rowIndex, 0);
            String name = (String) table.getValueAt(rowIndex, 1);
            String ip = (String) table.getValueAt(rowIndex, 3);
            Integer port = (Integer) table.getValueAt(rowIndex, 4);
            Downloader downloader = new Downloader(index.intValue(), name, ip, port.intValue());
            downloader.start();
        }
    }

    class SearchAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            Query a = new Query(0, searchField.getText()); // All searches are minimum speed 0 for now...
            NetworkManager.writeToAll(a);
            Searcher.addSearch(a);
        }
    }

    class ClearAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            searchModel.setRowCount(0);
            downloadModel.setRowCount(0);
            Searcher.clear();
        }
    }

    class OpenAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            if (event.getActionCommand().equals("Choose file")) {
                getFileName();
                openFile();
                readFile();
                getNumberOfLines();

            }

        }
    }

    class MRAction implements ActionListener {
        //  Host hosts;
        //    gr = new ArrayList<Group>();

        //  Groupcache groups;
        public void actionPerformed(ActionEvent event) {
            //  int port = Mine.getPort();
            //   String ip = Mine.getIPAddress().toString();

            //int group = 10;

            int rowIndex = mrtable.getSelectedRow();
            System.out.println(rowIndex);
            Integer groupID = (Integer) mrtable.getValueAt(rowIndex, 0);

            MRer mrer = new MRer(groupID.intValue());
            mrer.start();
        }
    }

    class FormAction implements ActionListener {

        private IPAddress ip;
        int b;
        // IPAddress form;

        public void actionPerformed(ActionEvent event) {
            //  boolean yes;
            //  yes = true;
            // int a = 1;
            //  while (a == 1) {
            //    b = 10;
            //  System.out.println("phachseo aaaaaaaaaa");
            b = (int) ((255 * Math.random()) - 120); // Unique group ID, and more twos complement problems
            Group group = new Group(b);

            //  Groupcache cache = new Groupcache();
            Groupcache.Groups.add(group);

            // System.out.println("ID 1 la`:" + b);

            // System.out.println(" port 1 la:" + Mine.getPort());

            Formgroup formgroup = new Formgroup(b, Mine.getPort(), Mine.getIPAddress());
            //   formgroup.parent = this;
//               System.out.println("so message id la : "+formgroup.getMessageID());
//               System.out.println("so message id la : "+formgroup.getMessageID());

//                formgroup.getMessageID();
//                       System.out.println("Lay dc ID la : "+    formgroup.getMessageID());
//                 formgroup.getMessageID();
//             System.out.println("Lay dc ID la : "+    formgroup.getMessageID());
            // System.out.println("Lay dc ID la :"+ formgroup.getMessageID().toString());
            Searcher.HashtableID.put((Packet) formgroup, formgroup);
            // System.out.println("Hashtable lan 1 la :");
//               System.out.println(" bang Hashtable dc luu la :");
            //    hashtablepro.out(Searcher.HashtableID);
            NetworkManager.writeToAll(formgroup);

            // a++;
            // }

        }
    }
}

class MonitorPanel extends JPanel {

    private static DefaultTableModel monitorModel;
    private static JTable table;

    public MonitorPanel() {
        setLayout(null);

        monitorModel = new DefaultTableModel(0, 0);
        monitorModel.addColumn((Object) "Remote host");
        monitorModel.addColumn((Object) "Port");
        monitorModel.addColumn((Object) "Query");

        JTable monitorTable = new JTable(monitorModel);

        JScrollPane scroll = new JScrollPane(monitorTable);
        scroll.setBackground(Color.blue);
        add(scroll);
        scroll.setBounds(50, 50, 700, 400);
    }

    public void addQuery(String host, int port, String query) {
        Object[] newRow = new Object[3];
        newRow[0] = host;
        newRow[1] = new Integer(port);
        newRow[2] = query;
        monitorModel.insertRow(0, newRow);
    }
}

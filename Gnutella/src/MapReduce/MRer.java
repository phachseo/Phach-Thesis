package MapReduce;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**int
 *
 * @author phachseo
 */
import MRonP2P.Group;
import MRonP2P.Groupcache;
import MRonP2P.hashtablepro;
import Network.Host;
import java.io.*;
import java.lang.Integer;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class MRer extends Thread {

    private int mygroupID;
    private int filesize = 0;
    Groupcache cache;
    static Hashtable sumhash = new Hashtable();
    static int hashtableReceiveCount = 0;
    static int numberofhosts = 0;
    File fileName;
    String[] paragraphs;

    public MRer(int GroupID, File file) {
        mygroupID = GroupID;
        fileName = file;
        divideWork();
    }

    public void divideWork() {
        int counter = 0;
        String[] a = new String[10000];
        BufferedReader fileInput = null;
        int numLines = 0;

        try {
            FileReader file = new FileReader(fileName);
            fileInput = new BufferedReader(file);
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error Opening File", "Error 4: ", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("File opened");

        try {

            String text = "";
            String line = fileInput.readLine();

            while (line != null) {
                counter++;
                System.out.println("(" + counter + ") " + line);
                text += (line + "\n");

                line = fileInput.readLine();

            }

            numLines = counter;
            int part[] = new int[10];
            int number = 0;


            for (int i = 0; i < Groupcache.getCount(); i++) {
                Group group = Groupcache.Groups.get(i);
                if (mygroupID == group.GroupID) {
                    ArrayList<Host> hosts = group.Hosts;
                    number = hosts.size();
                    System.out.println(" so luong host : " + number);


                    int division = numLines / number;
                    for (int j = 0; j < number; j++) {
                        part[j] = division;

                        System.out.print(" so luong line co trong  part " + j + " la : " + part[j]);

                    }
                    if (numLines % number != 0) {
                        part[number - 1] = part[number - 1] + (numLines % number);
                        System.out.println(" part cuoi la :" + part[number - 1]);
                    }
                    String line2 = fileInput.readLine();
                    System.out.println(line2);
                    System.out.println(line);
                }
            }
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Error Opening File", "Error 4: ", JOptionPane.ERROR_MESSAGE);
                }
            }
            System.out.println("File closed");

            StringTokenizer token = new StringTokenizer(text);
            int count = 0;
            int i = 0;
            String paragraph = "";
            paragraphs = new String[number];

            while (token.hasMoreTokens()) {
                ++count;
                paragraph += token.nextToken();

                if (count == part[i]) {
                    paragraphs[i] = paragraph;
                    paragraph = "";
                    ++i;
                    count = 0;
                }
            }



        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error reading File", "Error 5: ", JOptionPane.ERROR_MESSAGE);
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error Opening File", "Error 4: ", JOptionPane.ERROR_MESSAGE);
                }
            }
            System.exit(1);
        }
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

                hashtableReceiveCount = 0;

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
                    MapReducethread newthread = new MapReducethread(host.getPort(), host.getName(), mygroupID, paragraphs[j]);
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

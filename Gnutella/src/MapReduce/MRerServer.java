/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MapReduce;

import Client.Connection;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.*;
import java.io.*;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.Severity;
import MRonP2P.hashtablepro;
import MRonP2P.hashtablepro;
import MRonP2P.list;

/**
 *
 * @author phachseo
 */
public class MRerServer extends Thread {

    Connection myconnection;
    String incoming;
    static int groupID;
    // public static String FILE_NAME = "C:\\read\\task1.docx";
    //  public static String SAVEMR = "C:\\temp1";
    public static String FILE_NAME = "read/4.txt";
    public static String FILE_NAME2 = "read/2.txt";
    public static String SAVEMR = "C:\\temp1";
    public static Integer count = new Integer(0);
    public static Integer count2 = new Integer(0);

    public MRerServer(Connection c, String incoming) {

        c.changeType(Connection.SMRING);
        myconnection = c;
        this.incoming = incoming;
    }

    public void run() {

        StringTokenizer st = new StringTokenizer(incoming, "/");
        if ((st.countTokens() == 5) && (st.nextToken().equals("GET ")) && (st.nextToken().equals("get"))) {

            groupID = Integer.parseInt(st.nextToken());
            System.out.println("so GroupID la:" + groupID);
            String filecontent = st.nextToken();
            if (filecontent.endsWith(" HTTP")) {

                filecontent = filecontent.substring(0, (filecontent.length() - 5));
                System.out.println("Chay vao Mr server Nhan dc du lieu : " + filecontent);
                BufferedReader fileIn = null;
                try {

                    //fileIn = new BufferedReader(new FileReader(filecontent));
                    String line = filecontent;
                    ArrayList array = new ArrayList<list>();
                    Hashtable hashtable = new Hashtable();
                    Hashtable hashnew = new Hashtable();
                    while (line != null) {
                        Scanner tokenize = new Scanner(line);
                        while (tokenize.hasNext()) {
                            array.add(tokenize.next());
                        }
                    }
                    Iterator i = array.iterator();
                    while (i.hasNext()) {
                        //System.out.println(i.next());
                        String temp = (String) i.next();
                        count = (Integer) hashtable.get(temp);
                        if (count == null) {
                            hashtable.put(temp, new Integer(1));
                        } else {
                            hashtable.put(temp, new Integer(count.intValue() + 1));
                        }
                    }
                    Enumeration e = hashtable.keys();
                    String responsestring = "";
                    while (e.hasMoreElements()) {
                        String temp = (String) e.nextElement();
                        responsestring = responsestring + "<" + temp + " " + hashnew.get(temp);

                    }
                    int size = hashnew.size();
                    System.out.print(responsestring);
                    String responsestring2 = ("HTTP 200 OK\r\nServer: " + responsestring + "\r\nContent-type: application/binary\r\nContent-length: " + size + "\r\n\r\n");
                    byte[] response = responsestring2.getBytes();
                    myconnection.getByteWriter().write(response, 0, response.length);
                    myconnection.getByteWriter().flush();

                } catch (IOException ex) {
                    System.out.println("loiiiiiiiiiiiiiiiiiiiiiii");
                }
            }
        }
    }
}
//
//
//            BufferedReader fileIn2 = null;
//                Enumeration e = hashtable.keys();
//                Collection n = hashtable.values();
//                while (e.hasMoreElements()) {
//                    String temp = (String) e.nextElement();
//                    System.out.println(temp + "  " + hashtable.get(temp));
//                }
//System.out.println(n);
//                hashtablepro.add(hashtable, hashnew);
//                String a = "phach";
//                String b = "hoang";
//                String c = a + ":" + b;
//               String hash = hashtable.toString();
//
//                byte[] hashsend = hash.getBytes();
//                myconnection.getByteWriter().write(hashsend, 0, hash.length());
//                myconnection.getByteWriter().flush();
//
//                fileIn.close();
//
//                fileIn2 = new BufferedReader(new FileReader(FILE_NAME2));
//                String line2;
//                ArrayList array2 = new ArrayList<list>();
//                Hashtable hashtable2 = new Hashtable();
//                while ((line2 = fileIn2.readLine()) != null) {
//                    Scanner tokenize = new Scanner(line2);
//                    while (tokenize.hasNext()) {
//                        array2.add(tokenize.next());
//                    }
//
//
//                }
//
//                Iterator j = array2.iterator();
//
//                while (j.hasNext()) {
//                    //System.out.println(i.next());
//                    String temp = (String) j.next();
//                    count2 = (Integer) hashtable2.get(temp);
//                    if (count2 == null) {
//                        hashtable2.put(temp, new Integer(1));
//                    } else {
//                        hashtable2.put(temp, new Integer(count2.intValue() + 1));
//                    }
//                }
//
//                hashtablepro.add(hashtable2, hashnew);
//   hashtablepro.out(hashnew);
//                Enumeration e = hashtable.keys();
//                Collection n = hashtable.values();
//                while (e.hasMoreElements()) {
//                    String temp = (String) e.nextElement();
//                    System.out.println(temp + "  " + hashtable.get(temp));
//                }
//                PrintWriter out = new PrintWriter(new File("phach.txt"));
//
//                while (e.hasMoreElements()) {
//                    String temp = (String) e.nextElement();
//                    System.out.println(temp + "  " + hashtable.get(temp));
//                }
//            Enumeration q = hashtable.keys();
//            while (q.hasMoreElements()) {
////                String temp = (String) q.nextElement();
////                System.out.println(temp + "  " + hashtable2.get(temp));
////            }
//                fileIn2.close();
//               Enumeration e = hashnew.keys();
//                try { //NetworkOutp
//                    FileOutputStream fileOut =
//                            new FileOutputStream("employee.ser");
//                    ObjectOutputStream out =
//                            new ObjectOutputStream(fileOut);
//                    out.writeObject(e);
//                    out.close();
//                    fileOut.close();
//                } catch (IOException i) {
//                    i.printStackTrace();
//                }
//
//        for (int i = 0; i < Server.idList.size(); i++) {
//            int b = Server.idList.get(i);
//            if (groupID == b) {
//
//
//
//
//
//                File tosend = SharedDirectory.getFile(index);
//                int size = SharedDirectory.getFileSize(index);
//
//                String responsestring = ("HTTP 200 OK\r\nServer: Marmalade\r\nContent-type: application/binary\r\nContent-length:  \r\n\r\n");
//                byte[] response = responsestring.getBytes();
//
//                try {
//                    myconnection.getByteWriter().write(response, 0, response.length);
//                    myconnection.getByteWriter().flush();
//                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(tosend));
//                    for (int j = 0; j < size; j++) {
//                        myconnection.getByteWriter().write((byte) in.read());
//                    }
//                    myconnection.getByteWriter().close();
//                } catch (Exception e) {
//                    System.out.println("Unable to upload file.");
//                }
//
//
//            }
//            break;
//        }
//
//
//            StringTokenizer st = new StringTokenizer(incoming,"/");
//            if ((st.countTokens() == 4) && (st.nextToken().equals("GET ")) && (st.nextToken().equals("get")))
//	    {
//                int groupID =Integer.parseInt(st.nextToken());
//                for(int i=0;i>= Server.idList.get(i);i++)
//                {
//
//                String sourceip = st.nextToken();
//                if ( (sourceip.endsWith(" HTTP")))  {
//
//                String responsestring = ("HTTP 200 OK\r\nServer: Marmalade\r\nContent-type: application/binary\r\nContent-length:  \r\n\r\n");
//				byte[] response = responsestring.getBytes();
//
//                        try
//				    {
//					myconnection.getByteWriter().write(response, 0, response.length);
//					myconnection.getByteWriter().flush();
//					BufferedInputStream in = new BufferedInputStream(new FileInputStream(tosend));
//					for (int i = 0; i < size; i++)
//					    {
//						myconnection.getByteWriter().write((byte)in.read());
//					    }
//					myconnection.getByteWriter().close();
//				    }
//				catch (Exception e)
//				    {
//					System.out.println("Unable to upload file.");
//				    }


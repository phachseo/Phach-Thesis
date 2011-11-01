/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MapReduce;

import Client.Mine;
import Client.Connection;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author phachseo
 */
public class MapReducethread extends Thread {
    Hashtable hash = new Hashtable();
    String desip;
    int desport;
     MRer mrer;
    int groupID;
    private boolean oktorunmr = false;
    String getstring = "";
   int hashsize=0;
   String work;
    public MapReducethread(int port, String hostname, int GroupID, String workToDo) {
        desip = hostname;
        desport = port;
        groupID = GroupID;
        work = workToDo;
    }

    public void run() {
        try {
//PrintWriter out = new PrintWriter(new File("phi2.txt"));
            MRListener mo = new MRListener(Mine.getPort() + 1);
            mo.start();
            System.out.println("IP va port la: " + desip + desport);

            Socket socket = new Socket(desip, desport);
            Connection MRconnection = new Connection(socket, Connection.MRING);

                System.out.println(" cong viec goi di la :"+ work);
            String greetstring = "GET /get/" + groupID + "/" + work +"HTTP/1.0\r\nConnection: Keep-Alive\r\\nPHACHSEO NO1\r\nRange: bytes=0-\r\n\r\n";
            byte[] greeting = greetstring.getBytes();

            MRconnection.getByteWriter().write(greeting, 0, greeting.length);
            MRconnection.getByteWriter().flush();
            MRconnection.getByteWriter().close();
            //   MRconnection.closeByteWriter();
            String responseline;
            MRconnection.setSocket(socket);
            
            while (!((responseline = MRconnection.getTextReader().readLine()).equals(""))) // Run through the HTTP header
            {
                
                //responseline = MRconnection.getTextReader().readLine();
//                StringTokenizer st = new StringTokenizer(responseline, "/");
//                if((st.countTokens()==5)&& (st.nextToken().equals("HTTP 200 OK"))){
//                int hashsize =Integer.parseInt(st.nextToken());
//                System.out.println("size la: "+ hashsize);
//                if(hashsize>0&& (responseline.startsWith("Hashtable: "))){
//                    getstring = responseline.substring(11);
//                System.out.print(getstring);
//
//                }
//
//
//
//
//               }
                responseline = MRconnection.getTextReader().readLine();
               // System.out.println(responseline);
                 if(responseline.startsWith("Server: ")){
//                     System.out.println("11111");
                    getstring = responseline.substring(8);
                    System.out.print(getstring);
                 //   StringTokenizer tok = new StringTokenizer(getstring,"<");
                    }
                if (responseline.startsWith("Content-length: ")) {
                    hashsize = Integer.parseInt(responseline.substring(16));
//                    System.out.println("size  hashtable la:"+ hashsize);

                }
            //    for(int i=0; i <hashsize;i++){

               
               //System.out.println("tok dc la: "+ st.nextToken());
                //  StringTokenizer st2 = new StringTokenizer(st," ");
              //  }


                }

             StringTokenizer st = new StringTokenizer(getstring,"<");
                while(st.hasMoreElements()){
                String temp = st.nextToken();
               // System.out.println(" temp la :"+ temp);
                StringTokenizer st2 = new StringTokenizer(temp," ");
                while (st2.hasMoreElements()){
                    String temp2 = st2.nextToken();
                    String temp1 = st2.nextToken();
                    int  temp3 = Integer.parseInt(temp1);
                    //System.out.println("temp2 la: "+ temp2);
                    //System.out.println("temp3 la: "+ temp3);

                    hash.put(temp2, temp3);
                    
              //      out.write(temp2+" ");
               //     out.write(temp3+"\r\n");

                }

            }
                
         mrer.addhash(hash);
                
              //  hashtablepro.out(hash);
             //   out.close();
              //  hashtablepro.out(hash);
              




        } catch (UnknownHostException ex) {
            Logger.getLogger(MapReducethread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MapReducethread.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}

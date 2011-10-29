package Client;

/*
  Lame way to get local IP address. Java's localHost method returns
  127.0.0.1; s this opens a socket to Sun' web server.
*/
import Client.IPAddress;
import java.net.*;

public class Mine
{
  public static  int port  = 12345;
  public static  String  ipString = "";
  static  IPAddress ipObject = new IPAddress(127,0,0,1,12348);
    static  byte[] serventID = new byte[16];
    
  public static void updateAddress()
  {
    try
    {
      Socket s = new Socket("google.com", 80);
      ipString = s.getLocalAddress().getHostAddress();
      System.out.println("Local address: " + InetAddress.getLocalHost().getHostAddress());
      byte[] ipbytes = InetAddress.getLocalHost().getAddress();
      ipObject = new IPAddress(ipbytes[0], ipbytes[1], ipbytes[2], ipbytes[3], port);
      for (int i = 0; i < 16; i++)
	  serventID[i] = ipbytes[i % 4];
      s.close();
    }
    catch (Exception e)
    {
    }
  }

    public static IPAddress getIPAddress()
    {
      return ipObject;
    }
  
    public static byte[] getServentIdentifier()
    {
	return serventID;
    }
		    
  public static int getPort()
  {
    return port;
  }

    public static int getSpeed()
    {
	return (128);
    }

}


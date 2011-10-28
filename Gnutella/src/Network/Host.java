package Network;

/**
   Host has a string IP address and int port
*/
import java.net.*;

public class Host
{
  public String hostName;
  private int hostPort;
 // private  int hhost
  public int GroupID;
    
  public Host(String aHostName, int aHostPort)
  {
    hostName = aHostName;
    hostPort = aHostPort;
    //hostNumb = aHostNumb;
  }
  public Host(String aHostName, int aHostPort, int GroupID){
    hostName = aHostName;
    hostPort = aHostPort;
    this.GroupID = GroupID;


  }

  public int getID(){
   return this.GroupID;
  }

  public String getName()
  {
    return hostName;
  }
  
  public int getPort()
  {
    return hostPort;
  }

  public boolean equals(Host h)
  {
    return ((hostName.equals(h.hostName)) && (hostPort == h.hostPort));
  }
}

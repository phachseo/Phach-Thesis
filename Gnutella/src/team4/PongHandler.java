package team4;


/**Similar to QHitHandler, PongHandler will process pongs that match pings forwarded
   by us, not pings that originated with Pinger.*/

class PongHandler extends Thread
{
    Pong pong;
    IPAddress pingIP;
    IPAddress ip;
    Ping pingMatch;

    public PongHandler (IPAddress ip, Pong pong)
    {
	this.pong = pong;
	this.ip = ip;
    }

    public void run()
    {
//	 int size = HostCache.getCount();
//        boolean isIPContainedInHostCache = false;
//        System.out.println("so luong host la :"+ size);
//        for (int k = 0; k < size; k++) {
//            
//             System.out.println("Dia chi ip Pong la :"+ pong.getIP().toString());
//             System.out.println("Dia chi ip trong cache la :"+ HostCache.hosts[k].hostName);
//              System.out.println("so K la: "+ k);
//            if (pong.getIP().toString().equals(HostCache.hosts[k].hostName)) 
//            {
//                 isIPContainedInHostCache = true;
//             break;
//            }
//           
//           //break;
//        
//
//           if (! isIPContainedInHostCache ) {
             //System.out.println("chay vao de add host");
        String ipname = pong.getIP().toString();
	int port = pong.getPort();
     //  System.out.println("Port phachseo:"+ port);
        int num = pong.getNumFiles();
       // System.out.println("so luong file 2:"+ num);
	Host newhost = new Host(ipname, port);
	HostCache.addHost(newhost);

//	if (PingHandler.pt.containsKey(pong))
//	    {
//		pingMatch = (Ping) PingHandler.pt.get((Packet) pong); /**Matching pong is used as key to obtain original ping.*/
//		pingIP = pingMatch.getIP();
//
//
//
//                NetworkManager.writeToOne (pingIP, pong);
//	    }
    }
    //}
//}
}



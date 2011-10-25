package team4;

public class Ping extends Packet
{
    private IPAddress ip;  /**need to store ip address in order to properly
			      route matching pong*/
		private int index = HEADER_LENGTH;
    public Ping(int port, IPAddress IP)
    {
	super(Packet.PING, 0);


        contents[index + 4] = (byte)(port >>> 8);
       // System.out.println("port 4:"+ contents [index+ 4]);
	contents[index + 5] = (byte)(port & 0xff);
        //System.out.println("port 4:"+ contents [index+ 5]);
	// convert ip address to 4 bytes; need to check format of ip
	// address -- Little Endian????

	contents[index + 0] = (byte)(IP.getFirst());
        //System.out.println("6:"+ contents[index +6]);
	contents[index + 1] = (byte)(IP.getSecond());
         //System.out.println("7:"+ contents[index +7]);
	contents[index + 2] = (byte)(IP.getThird());
         //System.out.println("6:"+ contents[index +8]);
	contents[index + 3] = (byte)(IP.getFourth());
    }


    public Ping(byte[] rawdata)
    {
	super(rawdata);
    }
    
    public IPAddress getIP()
    {
	  //IP = (((contents[index + 3] & 0xff) << 24) | ((contents[index + 2] & 0xff) << 16) | ((contents[index + 1] & 0xff) << 8) | (contents[index + 0] & 0xff));
//        System.out.printf("ip1 : 0x%02x\n",contents[index + 6]);
        int ip1 = (0x000000ff & contents[index + 0]) ;
//        System.out.printf("ip1 : 0x%02x\n", ip1);

//        System.out.printf("ip2 : 0x%02x\n", contents[index + 7]);
        int ip2 = (0x000000ff & contents[index + 1]);
//        System.out.printf("ip2 : 0x%02x\n", ip2);

//        System.out.printf("ip3 : 0x%02x\n", contents[index + 8]);
        int ip3 = (0x000000ff & contents[index + 2]);
//        System.out.printf("ip3 : 0x%02x\n", ip3);

//        System.out.printf("ip4 : 0x%02x\n", contents[index + 9]);
        int ip4 = (0x000000ff & contents[index + 3]);
//        System.out.printf("ip4 : 0x%02x\n", ip4

//                );
        int port = (((contents[index + 4] & 0xff) << 8) | (contents[index + 5] & 0xff));
        IPAddress newIP = new IPAddress(ip1, ip2, ip3, ip4, port);

	return (newIP);
    }
public int getPort()
    {
	int port = (((contents[index + 4] & 0xff) << 8) | (contents[index + 5] & 0xff));
	return (port);
    }
    public void setIP(IPAddress ip)
    {
	this.ip = ip;
    }
}

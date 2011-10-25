/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team4;

/**
 *
 * @author phachseo
 */
public class Formgroup extends Packet {
private int index = HEADER_LENGTH;
private IPAddress ip;
//private int groupID = 1;
    String getIP;
   SearchPanel.FormAction parent;
    public Formgroup(int GroupID,int port,IPAddress IP)
    {
        super(FORMGROUP_REQUEST, 10);
   /*       for (int  a=0; a < 16; a++)
	    {
		contents[a] = (byte)((255 * Math.random()) - 120); // Unique group ID, and more twos complement problems
	    }
    *
    *
*/
         //System.out.println("ID:"+ GroupID);
         //  System.out.println("port:"+ port);
        //  System.out.println("IP:"+ IP);

        contents[index+3] = (byte)(GroupID >>> 24);
        contents[index+2] = (byte)((GroupID & 0xffffff) >>> 16);
        contents[index+1] = (byte)((GroupID & 0xffff)>>>8);
        contents[index+0] = (byte)(GroupID & 0xff);


       
        // convert port to two bytes
	contents[index + 4] = (byte)(port >>> 8);
       // System.out.println("port 4:"+ contents [index+ 4]);
	contents[index + 5] = (byte)(port & 0xff);
        //System.out.println("port 4:"+ contents [index+ 5]);
	// convert ip address to 4 bytes; need to check format of ip
	// address -- Little Endian????
        
	contents[index + 6] = (byte)(IP.getFirst());
        //System.out.println("6:"+ contents[index +6]);
	contents[index + 7] = (byte)(IP.getSecond());
         //System.out.println("7:"+ contents[index +7]);
	contents[index + 8] = (byte)(IP.getThird());
         //System.out.println("6:"+ contents[index +8]);
	contents[index + 9] = (byte)(IP.getFourth());
        //System.out.println("9:"+ contents[index +9]);


    }



   public Formgroup(byte[] rawdata)
    {
        super(rawdata);
    }
public IPAddress getIP()
    {
        IPAddress IP;


          //IP = (((contents[index + 3] & 0xff) << 24) | ((contents[index + 2] & 0xff) << 16) | ((contents[index + 1] & 0xff) << 8) | (contents[index + 0] & 0xff));
        System.out.printf("ip1 : 0x%02x\n",contents[index + 6]);
        int ip1 = (0x000000ff & contents[index + 6]) ;
//        System.out.printf("ip1 : 0x%02x\n", ip1);

        System.out.printf("ip2 : 0x%02x\n", contents[index + 7]);
        int ip2 = (0x000000ff & contents[index + 7]);
//        System.out.printf("ip2 : 0x%02x\n", ip2);

//        System.out.printf("ip3 : 0x%02x\n", contents[index + 8]);
        int ip3 = (0x000000ff & contents[index + 8]);
//        System.out.printf("ip3 : 0x%02x\n", ip3);
        
//        System.out.printf("ip4 : 0x%02x\n", contents[index + 9]);
        int ip4 = (0x000000ff & contents[index + 9]);
//        System.out.printf("ip4 : 0x%02x\n", ip4

//                );
        int port = (((contents[index + 4] & 0xff) << 8) | (contents[index + 5] & 0xff));
        IPAddress newIP = new IPAddress(ip1, ip2, ip3, ip4, port);
       
	return (newIP);
    }
public void setIP(IPAddress ip)
    {
	this.ip = ip;
    }

   public int getPort()
    {
	int port = (((contents[index + 4] & 0xff) << 8) | (contents[index + 5] & 0xff));
	return (port);
    }
public int getID(){
      


      int id = (((contents[index + 3] & 0xff) << 24) | ((contents[index + 2] & 0xff) << 16) | ((contents[index + 1] & 0xff) << 8) | (contents[index + 0] & 0xff));
	return (id);
}



//  public byte[] getGroupID(){
//    byte [] groupID = new byte[16];
//    for(int a=0; a< 16; a++)
//    {
//        groupID[a]= contents[a];
//    }
//    return (groupID);
//    }
//
//
//
//      public int byteArrayToInt(byte[] b, int offset) {
//        int value = 0;
//        for (int i = 0; i < 4; i++) {
//            int shift = (4 - 1 - i) * 8;
//            value += (b[i + offset] & 0x000000FF) << shift;
//        }
//        return value;
//      }
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Packet;

import Client.IPAddress;

/**
 *
 * @author phachseo
 */
public class Response extends Packet {

    private int index = HEADER_LENGTH;
    private IPAddress ip;
    public Response (boolean yes)
    {

        super(Packet.RESPONSE,1);
         setAllow(yes);


    }
    public Response(boolean yes, int port, IPAddress IP, int GroupID) {
        super(Packet.RESPONSE, 11);
        setAllow(yes);

      // System.out.println("port 2 la: " + port);

        contents[index + 1] = (byte) (port >>> 8);
        contents[index + 2] = (byte) (port & 0xff);

        // convert ip address to 4 bytes; need to check format of ip
        // address -- Little Endian????
        contents[index + 3] = (byte) IP.getFirst();
        contents[index + 4] = (byte) IP.getSecond();
        contents[index + 5] = (byte) IP.getThird();
        contents[index + 6] = (byte) IP.getFourth();

 

          contents[index + 10] = (byte) (GroupID >>> 24);
        contents[index + 9] = (byte) ((GroupID & 0xffffff) >>> 16);
        contents[index + 8] = (byte) ((GroupID & 0xffff) >>> 8);
        contents[index + 7] = (byte) (GroupID & 0xff);
        
        }

    public Response(byte[] rawdata) {
        super(rawdata);
    }

    public void setIP(IPAddress ip) {
        this.ip = ip;
    }

    public void setAllow(boolean yes) {
        this.contents[index + 0] = (byte) (yes ? 1 : 0);
    }

    public boolean getAllow() {
        return (this.contents[index + 0] & 255) != 0;
    }

    public int getPort() {
        int port = (((contents[index + 1] & 0xff) << 8) | (contents[index + 2] & 0xff));
        return (port);
    }

    public IPAddress getIP() {
        return (new IPAddress((contents[index + 3] & 0xff), (contents[index + 4] & 0xff), (contents[index + 5] & 0xff), (contents[index + 6] & 0xff), getPort()));
    }

    public int getID() {

        int id = (((contents[index + 10] & 0xff) << 24) | ((contents[index + 9] & 0xff) << 16) | ((contents[index + 8] & 0xff) << 8) | (contents[index + 7] & 0xff));
        return (id);
    }
}

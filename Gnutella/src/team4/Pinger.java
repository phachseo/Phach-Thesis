package team4;

public class Pinger extends Thread {

    static int hosts = 0;
    static int totalkb = 0;
    static int totalfiles = 0;
    static int groupid = 0;
    static Ping myping;

    public void run() {
        while (true) {
            try {
                sleep(Preferences.PINGER_TIME);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            //   System.out.println("information host :"+ hosts+ "kb"+totalkb+"file"+totalfiles+ "id "+groupid);
            Searcher.updateInfo(hosts, totalkb, totalfiles);

            System.out.println("Host Array La :" + HostArray.IncomingHostsgetCount());
              Searcher.updateGroupInfo(groupid);

            myping = new Ping();
            //  System.out.println("messageid cua goi ping la :"+ myping.getMessageID());
            Searcher.pingtable.put((Packet) myping, myping);
            hosts = 0;
            totalkb = 0;
            totalfiles = 0;
            groupid = 0;
            NetworkManager.writeToAll(myping);
        }
    }

    public static void inform(Pong pong) {
        if (pong.compare(myping)) {
            hosts++;
            totalfiles += pong.getNumFiles();
            totalkb += pong.getKb();
        }
    }

    public static void formgroup(Response response) {

        groupid += response.getID();

    }
}

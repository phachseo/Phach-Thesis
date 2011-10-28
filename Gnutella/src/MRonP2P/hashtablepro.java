package MRonP2P;

import java.util.Enumeration;
import java.util.Hashtable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author phachseo
 */
public class hashtablepro {
    public hashtablepro(){
        Hashtable hashnew = new Hashtable();
    }
    public static void add (Hashtable hashtable, Hashtable hashnew) {
        Integer counts = new Integer(0);
        Integer counts2 = new Integer(0);
        Enumeration e = hashtable.keys();
        while (e.hasMoreElements()) {
            String temp = (String) e.nextElement();
            counts = (Integer) hashnew.get(temp);
            counts2 = (Integer) hashtable.get(temp);
            if (counts == null) {
                hashnew.put(temp, counts2);
            } else {
                hashnew.put(temp, counts.intValue() + counts2.intValue());
            }
        }
        return;
        // xuat ra gia tri cua hashnew
    }
    public static void out (Hashtable hashout){
        Enumeration e1 = hashout.keys();
        while(e1.hasMoreElements()){
            String temp = (String) e1.nextElement();
            System.out.println(temp+"    "+hashout.get(temp));
        }
    }
}

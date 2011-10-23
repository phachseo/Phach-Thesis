/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phachseo
 */
public class Mapper {

    public static String FILE_NAME = "C:\\temp2\\task1.docx";
    public static String READPATH = "C:\\temp2";
     public static String RESULTPATH = "C:\\temp3";
    public static void readFromFile() {
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(FILE_NAME));
            String line;





        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

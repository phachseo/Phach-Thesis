package team4;

// FILE CHOOSER APPLICATION
// 14 June 2003
// Frans Coenen
// University of Liverpool

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class chooser extends JFrame implements ActionListener {

    // GUI features
    private BufferedReader fileInput;
    private JTextArea textArea;
    private JButton openButton, readButton;
    // Other fields
    private File fileName;
    private String[] data;
    private int numLines;

    public chooser(String s) {
        super(s);

        // Content pane
        Container container = getContentPane();
        //container.setBackground(Color.pink);
        container.setLayout(new BorderLayout(5, 5)); // 5 pixel gaps

        // Open button
        openButton = new JButton("Open File");
        openButton.addActionListener(this);
        container.add(openButton, BorderLayout.NORTH);

        // Read file button
        readButton = new JButton("Read File");
        readButton.addActionListener(this);
        readButton.setEnabled(false);
        container.add(readButton, BorderLayout.SOUTH);

//        // Text area	
//        textArea = new JTextArea(10, 15);
//        container.add(new JScrollPane(textArea), BorderLayout.SOUTH);
    }

    /* ACTION PERFORMED */
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals("Open File")) {
            getFileName();
        }
        if (event.getActionCommand().equals("Read File")) {
            readFile();
        }
    }

    /* OPEN THE FILE */
    private void getFileName() {
        // Display file dialog so user can select file to open
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);

        // If cancel button selected return
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        // Obtain selected file

        fileName = fileChooser.getSelectedFile();

        if (checkFileName()) {
            openButton.setEnabled(false);
            readButton.setEnabled(true);
        }
    }

    /* READ FILE */
    private void readFile() {
        // Disable read button
        readButton.setEnabled(false);

        // Dimension data structure
        getNumberOfLines();
        data = new String[numLines];

        // Read file
        readTheFile();
        // Output to text area	
//        textArea.setText(data[0] + "\n");
//        for (int index = 1; index < data.length; index++) {
//            textArea.append(data[index] + "\n");
//        }
        // Rnable open button
        openButton.setEnabled(true);
    }

    private void getNumberOfLines() {
        int counter = 0;
        // Open the file
        openFile();
        // Loop through file incrementing counter
        try {
            String line = fileInput.readLine();
            while (line != null) {
                counter++;
                System.out.println("(" + counter + ") " + line);
                line = fileInput.readLine();
            }
            numLines = counter;
            closeFile();
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(this, "Error reading File",
                    "Error 5: ", JOptionPane.ERROR_MESSAGE);
            closeFile();
            System.exit(1);
        }
    }

    private void readTheFile() {
        openFile();
        System.out.println("Read the file");
        try {
            for (int index = 0; index < data.length; index++) {
                data[index] = fileInput.readLine();
                System.out.println(data[index]);
                //System.out.println("so dong "+numLines);
            }
            closeFile();
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(this, "Error reading File",
                    "Error 5: ", JOptionPane.ERROR_MESSAGE);
            closeFile();
            System.exit(1);
        }
    }

    private boolean checkFileName() {
        if (fileName.exists()) {
            if (fileName.canRead()) {
                if (fileName.isFile()) {
                    return (true);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "ERROR 3: File is a directory");
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "ERROR 2: Access denied");
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "ERROR 1: No such file!");
        }

        return (false);
    }

    private void openFile() {
        try {
            FileReader file = new FileReader(fileName);
            fileInput = new BufferedReader(file);
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(this, "Error Opening File",
                    "Error 4: ", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("File opened");
    }

    private void closeFile() {
        if (fileInput != null) {
            try {
                fileInput.close();
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(this, "Error Opening File",
                        "Error 4: ", JOptionPane.ERROR_MESSAGE);
            }
        }
        System.out.println("File closed");
    }

   
}
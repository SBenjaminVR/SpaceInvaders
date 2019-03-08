/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 *
 * @author Humberto Gonzalez 
 * @author Benjamin Valdez
 */
public class WriteFile {

    private String path;
    private boolean append_to_file = false; //Set true if you don't want to overwrite

    /**
     * If it's true, it rewrites the text file, otherwise it just appends text
     * @param append to choose if you rewrite the textfile or not
     */
    public void setAppend(boolean append) {
        append_to_file = append;
    }
    
    /**
     * Choose the name of the text file
     * @param file_path is the name of the file
     */
    public WriteFile(String file_path) {
        path = file_path;
    }

    /**
     * Constructor to write a textFile 
     * @param file_path is the name of the file
     * @param append_value to choose if you rewrite the textfile or not
     */
    public WriteFile(String file_path, boolean append_value) {
        path = file_path;
        append_to_file = append_value;
    }
    
    /**
     * Writes the text into the textFile
     * @param textLine is the text that is going to be in the textFile 
     * @throws IOException 
     */
    public void writeToFile(String textLine) throws IOException {
        FileWriter write = new FileWriter(path, append_to_file);
        PrintWriter print_line = new PrintWriter(write);
        print_line.printf( "%s" + "%n" , textLine);
        
        print_line.close();
    }
    
}

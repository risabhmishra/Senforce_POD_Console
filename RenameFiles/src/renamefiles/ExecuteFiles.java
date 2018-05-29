/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renamefiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 *   program to execute all files in the folder sequentially
 * 
 * @author Amit
 */
 
public class ExecuteFiles {
        
    // method to get all files sequentially
    public void getAllFiles(String directoryPath) throws FileNotFoundException, IOException{
        File dir = new File(directoryPath);
        File[] directoryListing = dir.listFiles();
        
        if (directoryListing == null) {
            System.out.println("no any files found in this directory");
            return;
        }
        
        // to retrieve files sequentially....
        Arrays.sort(directoryListing);           
        for (File fileToRead : directoryListing) {
//            System.out.println(fileToRead.getName());
            executeCommands(fileToRead);
        }
        
    }
    
    //function to execute all commands in a file
    public void executeCommands(File fileToRead) throws FileNotFoundException, IOException{
        
        FileReader fileReader = new FileReader(fileToRead);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String inputLine;
        
        //to excute all commands in a file
        while ((inputLine = bufferedReader.readLine()) != null) {
            Runtime.getRuntime().exec(inputLine);
        }
        fileReader.close();
    }
    
}

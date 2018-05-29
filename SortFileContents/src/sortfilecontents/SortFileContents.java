/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortfilecontents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Program to sort the contents of message.properties  
 *
 * @author Amit
 */

public class SortFileContents {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String inputLine;
        //temporary list which stores contents of file before any comments...
        List<String> lineList = new ArrayList<>();
        //  list which contains contents of file in sorted order..
        List<String> outputList = new ArrayList<>();
        // command line input ---- /home/sensen/Documents/messages.properties
        String inputFile = args[0];                 
        File f = new File(inputFile);

        // if file doesn't exists
        if (!f.exists() || f.isDirectory()) {
            System.out.println("File not exists");
            return;
        }
        if(f.length()==0){
        	System.out.println("File Empty");
            return;
        }
        
        FileReader fileReader = new FileReader(inputFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);


        while ((inputLine = bufferedReader.readLine()) != null) {
            // for comments.. 
            if(inputLine.trim().startsWith("#")){
                //sort the contents  of temporary list and then add it to sorted list
            	Collections.sort(lineList, String::compareToIgnoreCase);
       
                outputList.addAll(lineList);
                lineList.clear();   //clear linelist to store contents after this comment..
                
                outputList.add(inputLine.trim());     // and then add this comment to outputlist
            }
            else if(inputLine.trim().length()>0){
            	lineList.add(inputLine.trim());
            }
            
        }
        // for remaining contents in linelist...
        if(!lineList.isEmpty()){
            Collections.sort(lineList, String::compareToIgnoreCase);
            outputList.addAll(lineList);
        }
        
        fileReader.close();
        
        FileWriter fileWriter = new FileWriter(inputFile);
        PrintWriter writer = new PrintWriter(fileWriter);
        
        //now writing in the file..
        for (String outputLine : outputList) {
            writer.print(outputLine + "\n");
        }
        
        writer.flush();
        writer.close();
        fileWriter.close();
  
    }

    
}
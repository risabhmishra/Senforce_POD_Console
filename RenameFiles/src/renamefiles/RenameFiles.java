/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    
package renamefiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * program to rename files according to their commit time.
 * File renamed as : yyyymmddhhmmss-filename
 * 
 * @author Amit
 */

public class RenameFiles {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        //new Directory for all commited files
        final String commitedDirectory = "CommitedFiles";
        //path for any directory "test" must be given as : /home/sensen/Documents/test/
        String myDirectoryPath = args[0];
        Process p;
        Process p1;
        File dir = new File(myDirectoryPath);
        File[] directoryListing = dir.listFiles();

        if (directoryListing == null) {
            System.out.println("no any files found in this directory");
            return;
        }
        Runtime.getRuntime().exec(new String[]{"mkdir",
            myDirectoryPath + commitedDirectory});
        
        
        for (File child : directoryListing) {
            String fileName = child.getName();
            String command = "git -C " + myDirectoryPath + 
                    " log -1 --format=%cd -C " 
                    + myDirectoryPath + fileName;
            String commitTime = null;

            // to get commit time
            //"git log -1 --format=%cd " + fileName
            //git -C /home/sensen/Documents/test log -1 --format=%cd -C /home/sensen/Documents/test/a.txt

            p = Runtime.getRuntime().exec(command);
            p.waitFor();

            try (BufferedReader buf
                    = new BufferedReader(new InputStreamReader(p.getInputStream()))) {

                commitTime = buf.readLine();

            }
            
            //check if file is commited
            if (commitTime != null) {
                //command to move commited file to new directory...
                command = "mv " + myDirectoryPath 
                        + fileName + " " + myDirectoryPath
                        + commitedDirectory + "/" + change(commitTime) + "-" + fileName;
                p1 = Runtime.getRuntime().exec(command);
                p1.waitFor();

            }
            
           
        }
        
        // calls this method to execute all the commited files sequentially
        new ExecuteFiles().getAllFiles(myDirectoryPath + commitedDirectory);
    }

    
    // to get commit time in this format : yearmmddhhmmss
    public static String change(String commitTime) {
        
        String str[] = commitTime.split("\\s+");
        String splitTime[] = str[3].split(":");
        String updated = str[4]  + getMonth(str[1])  + str[2]  
                + splitTime[0] + splitTime[1] + splitTime[2];
        return updated;

    }

    //returns month number from month name..
    public static String getMonth(String month) {
        
        switch (month) {   
            case "Jan":
                return String.format("%02d",Calendar.JANUARY);  // 00
            case "Feb":
                return String.format("%02d",Calendar.FEBRUARY); // 01
            case "Mar":
                return String.format("%02d",Calendar.MARCH);
            case "Apr":
                return String.format("%02d",Calendar.APRIL);
            case "May":
                return String.format("%02d",Calendar.MAY);
            case "Jun":
                return String.format("%02d",Calendar.JUNE);
            case "Jul":
                return String.format("%02d",Calendar.JULY);
            case "Aug":
                return String.format("%02d",Calendar.AUGUST);
            case "Sep":
                return String.format("%02d",Calendar.SEPTEMBER);
            case "Oct":
                return String.format("%02d",Calendar.OCTOBER);
            case "Nov":
                return String.format("%02d",Calendar.NOVEMBER);   // 10
            case "Dec":
                return String.format("%02d",Calendar.DECEMBER);   // 11
            default:
                return " ";
        }
    }

}
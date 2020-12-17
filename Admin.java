/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrsystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author niata
 */
public class Admin {
    
    
    static String filename = "admin.txt";
    
    
    public void RegisterPatient(String name,String ICNo, String email, int age ,String allergies) throws Exception{
        int count = readAll(Patient.filename).size();
        System.out.println(count);
        String ID = "P" + (count +1);
        
        Patient pat = new Patient(ID, name, ICNo, email,age ,allergies);
        pat.genkeypair();
        
        String record = String.join("|",ID,name,ICNo,email,Integer.toString(age),allergies);
        pat.write(record);
        System.out.println(record);
        

    }
    
    public boolean LoginCheck(String username, String Password){
     boolean success = false;
     
         ArrayList<String> data = this.readAll(this.filename);
         
         for(String record : data){
            String[] split = record.split("\\|");
            //System.out.println(split[0] + split[1]);
             if(split[0].equals(username) && split[1].equals(Password)){
                success = true;               
                break;
            }   
         }  
         return success;                      
    }
    
    
    public static ArrayList<String> readAll(String path) {
        //System.out.println(Test.readAll());
        ArrayList<String> list = new ArrayList();
        try  
            {  
                //the file to be opened for reading  
                FileInputStream fis=new FileInputStream(path);       
                Scanner sc=new Scanner(fis);    //file to be scanned  
                //returns true if there is another line to read  
                while(sc.hasNextLine())  
                {  
                    String line = sc.nextLine();
                    //populate it into list object
                    list.add(line);     //returns the line that was skipped  
                }  
                sc.close();     //closes the scanner 
                
            }  
        catch(IOException e)  
            {  
                e.printStackTrace();  
            }
        return list;
    }
    
    
    
}





/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrsystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jinhern
 */
public class Transaction {
    
    public static final String FILENAME = "trnxpool.txt";
    

    private String PID;
    private String DgID_ProID;
    private String DID;
    private String Pat_ICNo;
    private String date;
    private String res_type;
    private String diagnosis_procedure;

public Transaction(String PID,String DgID_ProID,String DID,String Pat_ICNo, String date,String res_type,String diagnosis_procedure) {
        this.PID = PID;
        this.DgID_ProID = DgID_ProID;
        this.DID = DID;
        this.Pat_ICNo = Pat_ICNo;
        this.date = date;
        this.res_type = res_type;
        this.diagnosis_procedure = diagnosis_procedure;
    }

       public String getPID() {
        return PID;
    }

    public String getDgID_ProID() {
        return DgID_ProID;
    }

    public String getDID() {
        return DID;
    }

    public String getPat_ICNo() {
        return Pat_ICNo;
    }

    public String getDate() {
        return date;
    }

    public String getRes_type() {
        return res_type;
    }

    public String getDiagnosis_procedure() {
        return diagnosis_procedure;
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
    
    public static void empty(){
        try {
            FileChannel.open(Paths.get(FILENAME), StandardOpenOption.WRITE).truncate(0).close();
        } catch (IOException ex) {
            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return "Transaction{" + "orderItem=" + PID + ", DgID/ProID: " + DgID_ProID + ", DID:" + DID + ", Pat_ICNo:" + Pat_ICNo + ", date:" + date + ", res_type: " + res_type + ", Diagnosis/Procedure:" + diagnosis_procedure + '}';
    }

   
    
    
    
}

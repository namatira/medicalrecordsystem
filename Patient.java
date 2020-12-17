/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author niata
 */
public class Patient {
    
    private String ID ;
    private String name ;
    private String ICNo;
    private String email ;
    private int age;
    private String allergies ;
    
    static String filename = "patient.txt";
    public String PUBLICKEY_FILE ;
    public String PRIVATEKEY_FILE ;
    
    public Patient(){       
    }
    
    public Patient(String ID,String name,String ICNo, String email, int age ,String allergies){
        this.ID = ID;
        this.name = name;
        this.age = age;
        this.email = email;
        this.allergies = allergies;
        
    }
    
    public void setID(String ID){
        this.ID =ID;
    }
    public String getID(){
        return ID;
    }
    public void setName(String name){
        this.name =name;
    }
    public String getName(){
        return name;
    }
    public void setICNo(String ICNo){
        this.ICNo = ICNo;
    }
    public String getICNo(){
        return ICNo;
    }
    public void setEmail(String email){
        this.ID =ID;
    }
    public String getEmail(){
        return email;
    }
    public void setAllergies(String allergies){
        this.allergies = allergies;
    }
    public String getAllergies(){
        return allergies;
    }

    
    public void genkeypair() throws Exception{
        PUBLICKEY_FILE = ID + "keypair/PublicKey";
        PRIVATEKEY_FILE = ID + "keypair/PrivateKey";
        KeyMaker.mkKeypair(PUBLICKEY_FILE,PRIVATEKEY_FILE); 
    }
    
    
    public void write(String record){
    
        //PrintWriter
        try(PrintWriter out = new PrintWriter(new FileWriter(Patient.filename, true))){
            out.println(record);//write to file

        }catch(IOException ex){
            //any exception/error
            System.out.println("Error: Failed to write.");

        }
    }
    
}

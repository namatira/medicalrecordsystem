/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrsystem;

import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author niata
 */
public class Doctor {
    
    private String ID ;
    private String name ;
    private String password;
    private String email ;
    private String speciality ;

    
    static String filename = "doctor.txt";
    static String Diagnosis_File = "diagnosis.txt";
    static String Procedure_File = "procedure.txt";
    static String BlockchainFile = "bchains.txt";
    
    
    
    public String PUBLICKEY_FILE ;
    public String PRIVATEKEY_FILE ;
    String Patient_PUBLICKEY_FILE;
    String Patient_PRIVATEKEY_FILE;
    //algorithm
    public static final String ALGORITHM = "RSA";
    static MySignature ms = new MySignature();
    
    //Blockchain
    //data structure
    static LinkedList<Block> bchain = new LinkedList();
    
    public Doctor(){       
    }
    public Doctor(String ID,String name,String password, String email, String speciality){
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.speciality = speciality;
        
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
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }
    public void setEmail(String email){
        this.ID =ID;
    }
    public String getEmail(){
        return email;
    }
    public void setSpeciality(String speciality){
        this.speciality = speciality;
    }
    public String getSpeciality(){
        return speciality;
    }

    
    public void genkeypair() throws Exception{
        PUBLICKEY_FILE = ID + "keypair/PublicKey";
        PRIVATEKEY_FILE = ID + "keypair/PrivateKey";
        KeyMaker.mkKeypair(PUBLICKEY_FILE,PRIVATEKEY_FILE); 
    }
    

    
    public boolean LoginCheck(String username, String Password){
     boolean success = false;
     
         ArrayList<String> data = this.readAll(Doctor.filename);
         for(String record : data){
            String[] split = record.split("\\|");
            //System.out.println(split[0] + split[1]);
             if(split[0].equals(username) && split[2].equals(Password)){
                success = true;
                this.ID = split[0];
                this.name = split[1];
                this.password = split[2];
                this.email = split[3];
                this.speciality = split[4];
                this.PUBLICKEY_FILE = ID + "keypair/PublicKey";
                this.PRIVATEKEY_FILE = ID + "keypair/PrivateKey";
                
                break;
            }   
         }  
         return success;                      
    }
    
    
    
    public static void write(Object record){
    
        //PrintWriter
        try(PrintWriter out = new PrintWriter(new FileWriter(Doctor.filename, true))){
            out.println(record);//write to file
            //if 'write' is succeeded
            //return true;
        }catch(IOException ex){
            //any exception/error
            System.out.println("Error: Failed to write.");
            //return false;
        }
    }
    public static void write(Object record, String path){
    
        //PrintWriter
        try(PrintWriter out = new PrintWriter(new FileWriter(path, true))){
            out.println(record);//write to file
            //if 'write' is succeeded
            //return true;
        }catch(IOException ex){
            //any exception/error
            System.out.println("Error: Failed to write.");
            //return false;
        }
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
    
    
    public void addDiagnosis(String PID,  String date, String test,String labresult,String diagnosis) throws Exception{
        int count = readAll(Diagnosis_File).size();
        System.out.println(count);
        String DgID = "Dg" + (count +1);
        String record = String.join ("|",PID, DgID, this.ID, date, test, labresult, diagnosis );
        System.out.println(record);
        
        
        //ADD SENSITIVE DATA TO THE BLOCKCHAIN
        writetoTransactions(PID,DgID,this.ID,Doctor.getPatientICNo(PID),date, labresult,diagnosis);
        count = readAll(BlockchainFile).size();
        System.out.println(count);
        if(count == 0){
            firstBlock();
       
        }else{
            nextBlock();
        }

        ms.setPublicKeyFile(this.PUBLICKEY_FILE);
        ms.setPrivateKeyFile(this.PRIVATEKEY_FILE);
        
        String ds = ms.sign(record);       
        System.out.println("Signiture: " + ds);
        System.out.println("Sending...");
        
        //sending data to verify
        Thread.sleep(3000);
                
        boolean isValid = ms.verify(record,ds);
        System.out.println("\nthe data and digital signiture matched: " + isValid);
        
        //Asymmetric Cryptography
        
        AsymmtricCrypto crypto = new AsymmtricCrypto();
        
        Patient_PUBLICKEY_FILE = PID + "keypair/PublicKey";
        //Patient_PRIVATEKEY_FILE = PID + "keypair/PrivateKey";
        
        String data = String.join ("|",DgID, this.ID, date, test, labresult, diagnosis );
        
        String encrypted = crypto.encrypt(data, getPublicKey(Patient_PUBLICKEY_FILE));
        
        System.out.println("CipherText: " + encrypted);
        
        record = String.join("|",PID,encrypted, ds);
                
           
        write(record,Doctor.Diagnosis_File);
        
    }
    
    public void addProcedure(String PID,  String date, String treatment,String procedureType,String procedureName) throws Exception{
        int count = readAll(Procedure_File).size();
        System.out.println(count);
        String ProID = "Pro" + (count +1); //Procedure ID
        String record = String.join ("|",PID, ProID, this.ID, date, treatment, procedureType, procedureName );

        System.out.println(record);
        
        //ADD SENSITIVE DATA TO THE BLOCKCHAIN
        writetoTransactions(PID,ProID,this.ID,Doctor.getPatientICNo(PID),date, procedureType,procedureName);
        count = readAll(BlockchainFile).size();
        System.out.println(count);
        if(count == 0){
            firstBlock();
       
        }else{
            nextBlock();
        }
        
        ms.setPublicKeyFile(this.PUBLICKEY_FILE);
        ms.setPrivateKeyFile(this.PRIVATEKEY_FILE);
        
        String ds = ms.sign(record);
        
        System.out.println("Signiture: " + ds);
        
        System.out.println("Sending...");
        
//        //sending data to verify
//        Thread.sleep(3000);
//                
//        boolean isValid = ms.verify(record,ds);
//        System.out.println("\nthe data and digital signiture matched: " + isValid);
        
        //Asymmetric Cryptography
        
        AsymmtricCrypto crypto = new AsymmtricCrypto();
        
        Patient_PUBLICKEY_FILE = PID + "keypair/PublicKey";
        //Patient_PRIVATEKEY_FILE = PID + "keypair/PrivateKey";
        
        String data = String.join ("|",ProID, this.ID, date, treatment, procedureType, procedureName);
        
        String encrypted = crypto.encrypt(data, getPublicKey(Patient_PUBLICKEY_FILE));
        
        System.out.println("CipherText: " + encrypted);
        
        record = String.join("|",PID,encrypted,ds);
        
        write(record,Doctor.Procedure_File);
        
    }
    
    public void ViewPatientMedicalRecord(String PID) throws Exception{
        Patient_PRIVATEKEY_FILE = PID + "keypair/PrivateKey";
        
        //Read Patient information
        String p_name = "" ;
        String ICNo = "";
        String email  = "";
        String age = "";
        String allergies = "";
        ArrayList<String> data = readAll(Patient.filename);
        //System.out.println(data);
        for(String record : data){
            String[] split = record.split("\\|");
            //System.out.println(split[0] + split[1]);
             if(split[0].equals(PID)){
                p_name = split[1];
                ICNo = split[2];
                email = split[3];
                age = split[4];
                allergies = split[5];
                break;
            }   
        }
         
        System.out.println("Patient ID: " + PID);
        System.out.println("Name: " + p_name + "\tIC Number: " + ICNo);
        System.out.println("Age: " + age + "\t\tEmail: " + email);
        System.out.println("Allergies: " + allergies);
         
        //Read Diagnosis information
        AsymmtricCrypto crypto = new AsymmtricCrypto();
        System.out.println("PAST DIAGNOSIS: ");
        data = readAll(Doctor.Diagnosis_File);
        String encrypted = "";
        int count = 0;
        String diagnosis = "";
        for(String record : data){
            String[] split = record.split("\\|");
            //System.out.println(split[0] + split[1]);
             if(split[0].equals(PID)){
                encrypted = split[1];
                diagnosis = crypto.decrypt(encrypted, Doctor.getPrivateKey(Patient_PRIVATEKEY_FILE));
                String[] split2 = diagnosis.split("\\|");
                System.out.println("Diagnosis ID: " + split2[0] );
                System.out.println("Doctor Name: " + Doctor.getDoctorName(split2[1]) + "\t\tDoctor ID: " + split2[1]);
                System.out.println("Date: " + split2[2] );
                System.out.println("Lab Test: " + split2[3] + "\t\tResult: " + split2[4] );
                System.out.println("Diagnosis: " + split2[5] + "\n");
                ms.setPublicKeyFile(split2[1] + "keypair/PublicKey");
                ms.setPrivateKeyFile(split2[1] + "keypair/PrivateKey");
                //sending data to verify
                Thread.sleep(3000);

                boolean isValid = ms.verify(split[0]+"|" + diagnosis,split[2]);
                System.out.println("\nthe data and digital signiture matched: " + isValid);
                
                System.out.println("\n\n");
                
                count++;
            }   
        }
        if (count == 0){
            System.out.println ("No Past DIAGNOSIS: \n");
        }
        
        
        //Read Procedures information
        System.out.println("PAST Procedures: ");
        data = readAll(Doctor.Procedure_File);
        encrypted = "";
        count = 0;
        String procedure = "";
        for(String record : data){
            String[] split = record.split("\\|");
            //System.out.println(split[0] + split[1]);
             if(split[0].equals(PID)){
                encrypted = split[1];
                procedure = crypto.decrypt(encrypted, Doctor.getPrivateKey(Patient_PRIVATEKEY_FILE));
                String[] split2 = procedure.split("\\|");
                System.out.println("Procedure ID: " + split2[0] );
                System.out.println("Doctor Name: " + Doctor.getDoctorName(split2[1]) + "\t\tDoctor ID: " + split2[1]);
                System.out.println("Date: " + split2[2] );
                System.out.println("Treatment: " + split2[3] + "\t\tProcedure Type: " + split2[4] );
                System.out.println("Procedures: " + split2[5] + "\n");
                ms.setPublicKeyFile(split2[1] + "keypair/PublicKey");
                ms.setPrivateKeyFile(split2[1] + "keypair/PrivateKey");
                //sending data to verify
                Thread.sleep(3000);

                boolean isValid = ms.verify(split[0]+"|" + procedure,split[2]);
                System.out.println("\nthe data and digital signiture matched: " + isValid);
                
                System.out.println("\n\n");
                
                count++;
            }   
        }
        if (count == 0){
            System.out.println ("No Past Procedures.\n");
        }
    
    }
    
    
    
    public static PublicKey getPublicKey(String path) throws Exception{
        byte[] keyBytes = Files.readAllBytes(Paths.get(path));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
         
        return KeyFactory.getInstance(ALGORITHM).generatePublic(spec);
        
    }
    public static PrivateKey getPrivateKey(String path) throws Exception{
        byte[] keyBytes = Files.readAllBytes(Paths.get(path));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(spec);
    }
    
    
    public static String getDoctorName(String DID){
        String DocName = "";
        ArrayList<String> data = readAll(Doctor.filename);

         for(String record : data){
            String[] split = record.split("\\|");
            //System.out.println(split[0] + split[1]);
             if(split[0].equals(DID)){
                DocName = split[1];
                
                break;
            }   
         } 
    
         return DocName;
    }
    public static String getPatientICNo(String PID){
        String ICNo = "";
        ArrayList<String> data = readAll(Patient.filename);

         for(String record : data){
            String[] split = record.split("\\|");
            //System.out.println(split[0] + split[1]);
             if(split[0].equals(PID)){
                ICNo = split[2];
                
                break;
            }   
         } 
    
         return ICNo;
    }
    
    public static void writetoTransactions(String PID,String DgID_ProID,String DID,String Pat_ICNo, String date,String res_type,String diagnosis_procedure){
        Transaction t = new Transaction( PID, DgID_ProID, DID, Pat_ICNo,  date, res_type, diagnosis_procedure);

            String record = String.join("|",t.getPID(),t.getDgID_ProID(),t.getDID(),t.getPat_ICNo(), t.getDate(),t.getRes_type(),t.getDiagnosis_procedure());
            write(record,Transaction.FILENAME);
    }
    
    public static void firstBlock() {
       
        System.out.println("--- Transaction objects ---");
        List<Transaction> trnxPool = TrnxPoolAdapter.getTransactions();
        trnxPool.stream().forEach( System.out::println );
        
        System.out.println("--- Transactions with hashes ---");
        List<List<String>> trnxPool_hashes = TrnxPoolAdapter.getTransactionsHashes();
        System.out.println( trnxPool_hashes );
        
        
        Block b1 = new Block(trnxPool_hashes, "0"); //genesis block
        bchain.add(b1);
        //clear the trnxpool.txt
        Transaction.empty();
        Blockchain.persist(bchain);
        //distribute/display the linkedlist elements/blocks
         out(bchain);
        
    }
    
    public static void nextBlock(){
        List<List<String>> trnxPool_hashes = TrnxPoolAdapter.getTransactionsHashes();
        bchain = Blockchain.get();
        Block block = new Block(trnxPool_hashes, bchain.getLast().getCurrentHash() );
        bchain.add(block);
        Transaction.empty();
        Blockchain.persist(bchain);
        out(bchain);

    }
        public static void out(LinkedList<Block> bchain){
        String temp = new GsonBuilder().setPrettyPrinting().create().toJson(bchain);
        System.out.println( temp );
        Blockchain.distribute(temp);
    }

    
    
}
    



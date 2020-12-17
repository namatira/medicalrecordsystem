/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrsystem;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author niata
 */
public class MRSystem {
    
    //algorithm
    public static final String ALGORITHM = "RSA";
    static String Patient_File = "patient.txt";

    public static void main(String[] args) throws Exception {
       
        System.out.println("Main Menu: ");
        System.out.println("1. Login - Doctor");
        System.out.println("2. Login - Admin");
        System.out.println("3. Register as New Doctor");
        System.out.println("\nChoice: ");
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        while(option > 3 || option < 1){
            System.out.println("Invalid choice!Try again: ");
            option = sc.nextInt();
        }
        String empty = sc.nextLine();

        switch (option) {
            case 1:
                {
                    Doctor doc = new Doctor();
                    System.out.println("Enter DoctorID: ");
                    String userID = sc.nextLine();
                    System.out.println("Enter password: ");
                    String password = sc.nextLine();
                    if (doc.LoginCheck(userID,password) == true){
                        System.out.println("Hello, Dr. " + doc.getName());
                        System.out.println("Menu: ");
                        System.out.println("1. Add Lab Results and Diagnosis");
                        System.out.println("2. Add Procedure record");
                        System.out.println("3. View Patien Records");
                        
                        int choice = sc.nextInt();
                        while(choice > 3 || choice < 1){
                            System.out.println("Invalid choice!Try again: ");
                            choice = sc.nextInt();
                        }
                        sc.nextLine();
                        
                        if(choice == 1){
                            ADD_Diagnosis(doc);
                            
                        }else if(choice == 2){
                            ADD_Procedure(doc);
                        }else if(choice == 3){
                            System.out.println("Enter Patient ID");
                            String PID =sc.nextLine();
                            while (PatientExists(PID) == false){
                                System.out.println("Patient Doesnt Exist!\nEnter  valid Patient ID:");
                                PID =sc.nextLine();
                            }
                            doc.ViewPatientMedicalRecord(PID);
                        }
                        
                    }else{
                        System.out.println("Login Failed");
                    }       break;
                }
            case 2:
                {
                    Scanner scan = new Scanner(System.in);
                    Admin admin = new Admin();
                    System.out.println("Enter username: ");
                    String username = scan.nextLine();
                    System.out.println("Enter password: ");
                    String password = scan.nextLine();
                    admin.LoginCheck(username, password);
                    if (admin.LoginCheck(username, password) == true){
                        System.out.println("Menu: ");
                        System.out.println("1. Register New Patient");
                        System.out.println("2.Exit");
                        
                        
                        int choice = scan.nextInt();
                        empty = scan.nextLine();
                        
                        if(choice == 1){
                            System.out.println("Arrived ");
                            Register_New_Patient(admin);
                        }else {
                            System.exit(0);
                        }
                        
                    }else{
                        System.out.println("Login Failed");
                    }       break;
                }
            case 3:
                Register_Doctor();
                break;
            default:
                break;
        }

           
    }
    
    
    
    
    public static void Register_Doctor() throws Exception{
        int count = Doctor.readAll(Doctor.filename).size();
        System.out.println(count);
        String ID = "D" + (count +1);
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Name: ");
        String name = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();
        System.out.println("Enter email: ");
        String email = sc.nextLine();
        System.out.println("Enter speciality: ");
        String speciality = sc.nextLine();

        Doctor doc = new Doctor(ID, name, password, email, speciality);
        doc.genkeypair();
        
        String record = String.join("|",ID,name,password,email,speciality);
        doc.write(record);
        System.out.println(record);
        
    }
    
    public static void ADD_Diagnosis(Doctor doc) throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Patient ID");
        String PID =sc.nextLine();
        while (PatientExists(PID) == false){
            System.out.println("Patient Doesnt Exist!\nEnter  valid Patient ID:");
            PID =sc.nextLine();
        }
        String date = (java.time.LocalDate.now()).toString(); //LocalDate to String
        System.out.println("Enter Lab Test Type: ");
        String test = sc.nextLine();
        System.out.println("Enter Lab Result: ");        
        String labresult = sc.nextLine();
        System.out.println("Enter Diagnosis: "); 
        String diagnosis = sc.nextLine();
        doc.addDiagnosis(PID,date,test, labresult,diagnosis);
        
    }
    
    public static void ADD_Procedure(Doctor doc) throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Patient ID");
        String PID =sc.nextLine();
        while (PatientExists(PID) == false){
            System.out.println("Patient Doesnt Exist!\nEnter  valid Patient ID:");
            PID =sc.nextLine();
        }
        String date = (java.time.LocalDate.now()).toString(); //LocalDate to String
        System.out.println("Enter Treatment Name: ");
        String treatment = sc.nextLine();
        System.out.println("Enter Procedure type (s : surgical / ns : non-surgical");        
        String procedureType = sc.nextLine();
        while(!(procedureType.equalsIgnoreCase("s")) && !(procedureType.equalsIgnoreCase("ns"))){
            System.out.println("Invalid Procedure type!\nProcedure type (s : surgical / ns : non-surgical: ");
            procedureType = sc.nextLine();
        }
        if(procedureType.equalsIgnoreCase("s")){
            procedureType = "surgical";
        }else{
            procedureType = "non-surgical";
        }
        System.out.println("Enter Procedure name: "); 
        String procedureName = sc.nextLine();
        doc.addProcedure(PID,date,treatment, procedureType,procedureName);

        
    }
    
    public static boolean PatientExists(String ID){
    boolean success = false;   
         ArrayList<String> data = Doctor.readAll(Patient_File);
         //System.out.println(data);
         for(String record : data){
            String[] split = record.split("\\|");
            //System.out.println(split[0] + split[1]);
             if(split[0].equals(ID)){
                success = true;

                break;
            }   
         }  
         return success;                      
    }
    
    public static void Register_New_Patient(Admin admin) throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Patient Name: ");
        String name = sc.nextLine();
        System.out.println("Enter ICNo: ");
        String ICNo = sc.nextLine();
        System.out.println("Enter email: ");
        String email = sc.nextLine();
        System.out.println("Enter age: ");
        int age = Integer.parseInt(sc.nextLine());
        //sc.nextLine();
        System.out.println("Enter any allergies (eg. one, two, three) - if non, enter (no allergies): ");
        String allergies = sc.nextLine();
        
        admin.RegisterPatient(name, ICNo, email, age, allergies);
    
    }
   

}







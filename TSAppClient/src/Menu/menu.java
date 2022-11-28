/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menu;


import java.rmi.NotBoundException;

import java.security.*;
import java.util.*;
import java.sql.Date;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Doctor;
import pojos.Patient;
import pojos.Signal;
import pojos.User;


public class menu {

    public static InputStream console = (System.in);
    
  
    public static void main(String[] args) throws Exception {
        try {
            Socket socket = utilities.CommunicationWithServer.connectToServer();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter (outputStream,true);
            BufferedReader bf = new BufferedReader (new InputStreamReader (inputStream));
            Scanner sc = new Scanner (System.in);
            
            String trashcan;
            int choice=1;
	
            do {
                try {
                    System.out.println("Welcome to Telesomnia.");
                    System.out.println("1. Register");
                    System.out.println("2. Login");
                    System.out.println("0. Exit");
                    choice = sc.nextInt();
                    System.out.println(choice);
                    printWriter.println(choice);
                    System.out.println("after pw");
                    switch(choice) {
                        case 0:
                            utilities.CommunicationWithServer.ReleaseResources(printWriter, bf);
                            utilities.CommunicationWithServer.exitFromServer(inputStream, outputStream, socket);
                        case 1:
                            System.out.println("inside switch 1");
                            register(bf, printWriter);
                            break;
                        case 2:
                            System.out.println("inside switch 2");
                            login(socket, inputStream, outputStream, bf, printWriter);
                            break;
                        default:
                            System.out.println("Please introduce a valid option.");
                    }
                } catch (Exception e) {
                        trashcan = sc.next();
                        System.out.println("Please introduce a valid option.");
                }
            }while(true);
        } catch (IOException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    private static void register(BufferedReader br, PrintWriter pw) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce your personal data: ");
        Patient p = createPatient(br, pw);
        System.out.println("Here is a list of all the doctors:");
        int size = Integer.parseInt(br.readLine());
        for (int i=0;i<size;i++){
            System.out.println(br.readLine());
        }
        System.out.println("Introduce the id of your doctor:");
        int id = sc.nextInt();
        pw.println(id);
    }

                  
    private static void login(Socket socket, InputStream inputStream, OutputStream outputStream, BufferedReader bf, PrintWriter pw) throws Exception{
        Scanner sc = new Scanner(System.in);
        User user = new User();
        System.out.println("Please enter your username and password:");
        System.out.println("Username:");
        String username = sc.next();
        System.out.println("Password:");
        String password = sc.next();
        user.setPassword(password);
        user.setUsername(username);
        utilities.CommunicationWithServer.sendUser(pw, user);
        String line = bf.readLine();
        if(line.equals("Wrong username or password")) {
            System.out.println("Wrong username or password");
        } else if(line.equals("patient")){
                 System.out.println("I received that user is a patient");
                patientMenu(socket, inputStream, outputStream, bf, pw, user.getUserId());
        } else if (line.equals("doctor")){
                System.out.println("I received that user is a doctor");
                doctorMenu(socket, inputStream, outputStream, bf, pw);
        }
    }
    
    
    public static void patientMenu(Socket socket, InputStream inputStream, OutputStream outputStream, BufferedReader br, PrintWriter pw, int userId) throws Exception{
        Scanner sc = new Scanner (System.in);
        String trashcan;
        int option=0;
        Patient patient = utilities.CommunicationWithServer.receivePatient(br);
        System.out.println("Hello Mr/Ms "+patient.getSurname());
        do{
            int a = 0;
            
            
            System.out.println("Choose an option [0-3]:"
                            + "\n1. Start recording \n2. Stop recording \n3. Consult my recordings \n4. Change BITalino MAC address \n0.Exit");
            do {
                try {
                    option = sc.nextInt();
                    pw.println(option);
                    a = 1;
                } catch (Exception e) {
                    trashcan = sc.next();
                    System.out.println("Please select a valid option.");
                }
            } while (a==0);

            switch (option) {
                case 0:
                    System.out.println("Thank you for using our system");
                    utilities.CommunicationWithServer.ReleaseResources(pw, br);
                    utilities.CommunicationWithServer.exitFromServer(inputStream, outputStream, socket);
                    break;
                case 1:
                    System.out.println("Your going to record your ECG and EMG signals");
                    utilities.CommunicationWithServer.recordSignal(patient, pw);
                    break;
                case 2: //esta igual sobra
                    System.out.println("You can stop recording your signal");

                    break;
                case 3:
                    System.out.println("Here you can consult all your signals");
                    showSignals(br, pw);	
                    break;
                case 4:
                    System.out.println("Change BITalino MAC address");
                    updateMacAddress(pw, patient);
                    break;
                default:
                    System.out.println("Not a valid option.");
                    break;
            }		
        }while(true);
    }
        
        
    private static void doctorMenu(Socket socket, InputStream inputStream, OutputStream outputStream, BufferedReader bf, PrintWriter pw) throws Exception {
        Scanner sc = new Scanner (System.in);
        String trashcan;
        int option=0;
        Doctor doctor = utilities.CommunicationWithServer.receiveDoctor(bf);
        System.out.println("Hello Dr. " + doctor.getDsurname());
        do{
            int a = 0;
            
            
            System.out.println("Choose an option[0-2]:");
            System.out.println("\n1. Register a new Doctor \n2. See list of all my patients \n3. Edit Patient \n4. Consult recordings of a patient \n5. Delete  \n 0. Exit");
            do {
                try {
                    option = sc.nextInt();
                    pw.println(option);
                    a = 1;
                } catch (Exception e) {
                    trashcan = sc.next();
                    System.out.println("Please select a valid option.");
                }
            } while (a==0);
       
            switch(option) {
            case 0:
                System.out.println("Thank you for using our system");
                utilities.CommunicationWithServer.ReleaseResources(pw, bf);
                utilities.CommunicationWithServer.exitFromServer(inputStream, outputStream, socket);
            
                break;
            case 1: 
                System.out.println("Register a new Doctor");
                createDoctor(bf, pw);
                break;
            case 2:
                System.out.println("See list of all my patients");
                utilities.CommunicationWithServer.receivePatientList(bf);
                break;
            case 3:
                System.out.println("Edit Patient");
                utilities.CommunicationWithServer.receivePatientList(bf);
                System.out.println("Introduce medcard of patient to update:");
                int medcard = sc.nextInt();
                editPatient(bf,pw, medcard);
                break;
            case 4:
                System.out.println("Consult recordings of a patient");
                utilities.CommunicationWithServer.receivePatientList(bf);
                System.out.println("Introduce medcard of patient to update:");
                int medcard2 = sc.nextInt();
                pw.println(medcard2);
                showSignals(bf, pw);
                break;
            case 5:
                System.out.println("Delete Patient");
                utilities.CommunicationWithServer.receivePatientList(bf);
                System.out.println("Introduce medcard of patient to update:");
                int medcard5 = sc.nextInt();
                pw.println(medcard5);
                 String line = bf.readLine();
                if (line.equalsIgnoreCase("success")){
                    System.out.println("Patient succesfully deleted");
                }else{
                    System.out.println("Error with deleting");
                }
                break;
            default:
                System.out.println("Not a valid option.");
                break;
            }
        } while(true);
    }    

    
    public static Patient createPatient (BufferedReader br, PrintWriter pw) throws NotBoundException, Exception {
        Scanner sc = new Scanner (System.in);
        Patient p = new Patient();
         System.out.println("Please, input the patient info:");
        
        System.out.print("Medical card number: "); 
        Integer medCardNumber=1; 
        Boolean validMedNumber = false;
        do{
            try {
                medCardNumber = sc.nextInt(); 
                validMedNumber = true;
            }catch(Exception e) {
                    System.out.println("Please introduce a valid medical card number which only contains numbers");
            }
        }while (validMedNumber = false);
        p.setMedical_card_number(medCardNumber);

       
        System.out.print("Name: "); 
        String name = sc.next();
        p.setName(name);
        
        System.out.print("Surname: "); 
        String surname = sc.next();
        p.setSurname(surname);
        
       

        System.out.print("Gender: ");
        String gender = sc.next();  
        do{
            if (gender.equalsIgnoreCase("male")) {
                    gender = "Male";
            } else if (gender.equalsIgnoreCase("female")) {
                    gender = "Female";
            } else{
                 System.out.print("Not a valid gender. Please introduce a gender (Male or Female): ");
                 gender = sc.next();
            }
        }while (!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female")));
        p.setGender(gender);
        
        System.out.print("Date of birth [yyyy-mm-dd]: ");	
        String birthdate = sc.next();
        Date bdate; 
        try {
            bdate = Date.valueOf(birthdate);
            if (bdate.before(Date.valueOf(LocalDate.now())) || bdate.equals(Date.valueOf(LocalDate.now()))) {
                p.setDob(bdate);
            } else {
                do {
                    System.out.print("Please introduce a valid date [yyyy-mm-dd]: ");
                    birthdate = sc.next();
                    bdate = Date.valueOf(birthdate);
                } while ((!bdate.before(Date.valueOf(LocalDate.now()))) || (!bdate.equals(Date.valueOf(LocalDate.now()))));
                p.setDob(bdate);
            }
        } catch (Exception e) {
            int b=0;
            do {	
                System.out.print("Please introduce a valid date format [yyyy-mm-dd]: ");
                birthdate = sc.next();
                bdate = Date.valueOf(birthdate);
                if (bdate.before(Date.valueOf(LocalDate.now())) || bdate.equals(Date.valueOf(LocalDate.now()))) {
                        p.setDob(bdate);
                } else {
                    do {
                        System.out.print("Please introduce a valid date [yyyy-mm-dd]: ");							
                        birthdate = sc.next();
                        bdate = Date.valueOf(birthdate);
                    } while ((!bdate.before(Date.valueOf(LocalDate.now()))) || (!bdate.equals(Date.valueOf(LocalDate.now()))));
                    p.setDob(bdate);
                }
                b=1;
            } while (b==0);
        }

        System.out.print("Address: ");				
        String address = sc.next();
        p.setAddress(address);

        System.out.print("Email: ");			
        String email = sc.next();
        p.setEmail(email);
        
        System.out.print("Diagnosis: "); 
        String diagnosis = sc.next();
        p.setDiagnosis(diagnosis);
        
        System.out.print("Allergies: "); 
        String allergies = sc.next();
        p.setAllergies(allergies);
        
        System.out.print("Bitalino MACAddress: ");				
        String mac = sc.next();
        p.setMacAddress(mac);
        
        System.out.println("Let's proceed with the registration, the username and password will be autogenerated by the system:");
        utilities.CommunicationWithServer.sendPatient(pw, p);
        System.out.println("after send patient");
        User user = utilities.CommunicationWithServer.receiveUser(br);
        System.out.println("The autogenerated username is: "+ user.getUsername());
        System.out.println("The autogenerated password is: "+ user.getPassword());
        String line = br.readLine();
        if (line.equals("Patient successfully registered")){
            System.out.println("Success");
            return p;
        } else{
            System.out.println("Patient not registered");
            return null;
        }
    }
    

    private static Patient selectPatient(BufferedReader br, PrintWriter pw) throws Exception{
        Scanner sc = new Scanner (System.in);
        //Show list with all patients.
        List<String> CompletePatientList = utilities.CommunicationWithServer.receivePatientList(br);
        for(int i =0;i<CompletePatientList.size();i++){
                System.out.println(CompletePatientList.get(i));
        }
        //Chose a Patient
        List<Patient> patientList = new ArrayList();
        Patient patient = null;
        while(patientList.isEmpty()){
            Integer medCard=null;
            System.out.println(patientList.toString());
            System.out.println("Enter the medical card number of the chosen patient: ");
            try{
                medCard = sc.nextInt();
            }catch(Exception ex){
                System.out.println("Not a valid medical card number ONLY NUMBERS");
            }
            pw.print(medCard);
            patient = utilities.CommunicationWithServer.receivePatient(br);
        }
        return patient; 
    }

    
    private static void deletePatient(BufferedReader br, PrintWriter pw) throws Exception {
        Scanner sc = new Scanner (System.in);
        //Show list with all patients.
        List<String> CompletePatientList = utilities.CommunicationWithServer.receivePatientList(br);
        for(int i =0;i<CompletePatientList.size();i++){
                System.out.println(CompletePatientList.get(i));
        }
        
        //Chose a Patient to delete
        System.out.println("Introduce de medical card number of the patient to delete: ");
        String medcard = sc.nextLine();
        pw.println(medcard);
        String line = br.readLine();
        if (line.equalsIgnoreCase("success")){
            System.out.println("Patient succesfully deleted");
        }else{
            System.out.println("Error with deleting");
        }
    }
    

    public static void createDoctor(BufferedReader br, PrintWriter pw) throws Exception{
        Scanner sc = new Scanner (System.in);
        Doctor d = new Doctor();

        System.out.println("Please, input the doctor info:");
        System.out.print("Name: ");
        String name = sc.next();
        d.setDname(name); 

        System.out.print("Surname: ");
        String surname = sc.next();
        d.setDsurname(surname);
        
        System.out.print("Email: ");
        String email = sc.next();
        d.setDemail(email);
        
        
        
        System.out.println("Let's proceed with the registration, the username and password will be autogenerated by the system:");
        utilities.CommunicationWithServer.sendDoctor(pw, d);
        User user = utilities.CommunicationWithServer.receiveUser(br);
        System.out.println("The autogenerated username is: "+ user.getUsername());
        System.out.println("The autogenerated password is: "+ user.getPassword());
        String line = br.readLine();
        if (line.equals("Doctor successfully registered")){
            System.out.println("Success");
        } else{
            System.out.println("Doctor not registered");
        }
    }
    
    
    private static void showSignals (BufferedReader br, PrintWriter pw) throws Exception{
        //int size = Integer.parseInt(br.readLine());
        Scanner sc = new Scanner (System.in);
        //Show list with all signals
        List<String> signalFilenames = utilities.CommunicationWithServer.ShowSignals(br, pw);
        System.out.println(signalFilenames.size());
        
        for(int i=0; i<signalFilenames.size();i++){
            System.out.println(signalFilenames.get(i));
        }
        System.out.println("hola");
        //Choose a signal
        /*
        List<String> signalList = new ArrayList<>();
        Signal signal = null;
        while(signalList.isEmpty()){*/
            //System.out.println(signalList.toString());
            System.out.println("Introduce filename of the signal:");
            String signalName = sc.next();
           // Signal s = db.jdbc.SQLiteSignalManager.selectSignalByName
            pw.println(signalName);
           
            String signal = br.readLine();
            System.out.println(signal);
            
        //}
    }
    
    private static void editPatient (BufferedReader bf,PrintWriter pw, int medcard) throws Exception{
        Scanner sc = new Scanner (System.in);
        int option=1;
        String update;
        pw.println(medcard);
        Patient p = utilities.CommunicationWithServer.receivePatient(bf);
        while(option != 0) {
            System.out.println("Choose an option[0-2]:");
            System.out.println("\n0. Back \n1. Diagnosis \n2. Allergies");
            option = sc.nextInt();
            pw.println(option);
            switch(option) {
                case 0:
                    option=0;
                    break;
                case 1:
                        System.out.println("Write diagnosis:");
                        update = sc.next();
                        p.setDiagnosis(update);
                        utilities.CommunicationWithServer.sendPatient(pw, p);
                        break;
                case 2:
                        System.out.println("Write Allergies:");
                        update = sc.next();
                        p.setAllergies(update);
                        utilities.CommunicationWithServer.sendPatient(pw, p);
                        break;
                default:
                     System.out.println("Not valid option");
                     break;
            }
        }
    }
    
    private static void updateMacAddress (PrintWriter pw, Patient p) throws Exception{
       Scanner sc = new Scanner (System.in);
        String update;
        System.out.println("Write Bitalino MacAddress:");
        update = sc.next();
        p.setMacAddress(update);
        utilities.CommunicationWithServer.sendPatient(pw, p);
    }
}

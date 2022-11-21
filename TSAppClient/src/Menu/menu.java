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
            
            String trashcan;
            int choice=1;
	
            do {
                try {
                    System.out.println("Welcome to Telesomnia.");
                    System.out.println("1. Register");
                    System.out.println("2. Login");
                    System.out.println("0. Exit");
                    choice = bf.read();
                    printWriter.println(choice);
                    switch(choice) {
                        case 1:
                            
                            register(bf, printWriter);
                            break;
                        case 2:
                            login(socket, inputStream, outputStream, bf, printWriter);
                            break;
                    }
                } catch (Exception e) {
                        trashcan = bf.readLine();
                        System.out.println("Please introduce a valid option.");
                }
            }while(choice != 0);
        } catch (IOException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    private static void register(BufferedReader br, PrintWriter pw) throws Exception {
        System.out.println("Introduce your personal data: ");
        Patient p = createPatient(br, pw);
        User user = utilities.CommunicationWithServer.receiveUser(br);
        System.out.println("The autogenerated username is: "+ user.getUsername());
        System.out.println("The autogenerated username is: "+ user.getPassword());
    }

                  
    private static void login(Socket socket, InputStream inputStream, OutputStream outputStream, BufferedReader bf, PrintWriter pw) throws Exception{
        User user = new User();
        System.out.println("Please enter your username and password:");
        System.out.println("Username:");
        String username = bf.readLine();
        System.out.println("Password:");
        String password = bf.readLine();
        user.setPassword(password);
        user.setUsername(username);
        utilities.CommunicationWithServer.sendUser(pw, user);
        String line = bf.readLine();
        if(line.equals("Wrong username or password")) {
            System.out.println("Wrong username or password");
            return;
        } else if(line.equals("patient")){
                patientMenu(socket, inputStream, outputStream, bf, pw, user.getUserId());
        } else{
                doctorMenu(socket, inputStream, outputStream, bf, pw);
        }
    }
    
    
    public static void patientMenu(Socket socket, InputStream inputStream, OutputStream outputStream, BufferedReader bf, PrintWriter pw, int userId) throws Exception{
        String trashcan;
        int option=0;
        do{
            int a = 0;
            
            Patient patient = utilities.CommunicationWithServer.receivePatient(bf);
            System.out.println("Hellos Mr/Ms "+patient.getSurname());
            System.out.println("Choose an option [0-3]:"
                            + "\n1. Start recording \n2. Stop recording \n3. Consult my recordings \n4. Change BITalino MAC address \n0.Exit");
            do {
                try {
                    option = bf.read();
                    a = 1;
                } catch (Exception e) {
                    trashcan = bf.readLine();
                    System.out.println("Please select a valid option.");
                }
            } while (a==0);

            switch (option) {
                case 0:
                    System.out.println("Thank you for using our system");
                    utilities.CommunicationWithServer.ReleaseResources(pw, bf);
                    utilities.CommunicationWithServer.exitFromServer(inputStream, outputStream, socket);
                    break;
                case 1:
                    System.out.println("Your going to record your ECG and EMG signals");
                    utilities.CommunicationWithServer.recordSignal(patient, 100,pw);
                    break;
                case 2: //esta igual sobra
                    System.out.println("You can stop recording your signal");

                    break;
                case 3:
                    System.out.println("Here you can consult all your signals");
                    showSignals(selectPatient(bf, pw));	
                    break;
                case 4:
                    System.out.println("Change BITalino MAC address");
                    updateMacAddress(bf, pw, patient);
                    break;
                default:
                    System.out.println("Not a valid option.");
                    break;
            }		
        }while(true);
    }
        
        
    private static void doctorMenu(Socket socket, InputStream inputStream, OutputStream outputStream, BufferedReader bf, PrintWriter pw) throws Exception {
        String trashcan;
        int option=0;
        do{
            int a = 0;
            
            Doctor doctor = utilities.CommunicationWithServer.receiveDoctor(bf);
            System.out.println("Hellos Dr. " + doctor.getDsurname());
            System.out.println("Choose an option[0-2]:");
            System.out.println("\n1.Register a new Doctor \n2. See list of all my patients \n3. Edit Patient \n4. Consult recordings of a patient \n 0. Exit");
            do {
                try {
                    option = bf.read();
                    a = 1;
                } catch (Exception e) {
                    trashcan = bf.readLine();
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
                editPatient(bf, pw, selectPatient(bf, pw));
                break;
            case 4:
                System.out.println("Consult recordings of a patient");
                showSignals(selectPatient(bf, pw));
                break;
            case 5:
                System.out.println("Delete Patient");
                deletePatient(bf, pw);
                break;
            default:
                System.out.println("Not a valid option.");
                break;
            }
        } while(true);
    }    

    
    public static Patient createPatient (BufferedReader br, PrintWriter pw) throws NotBoundException, Exception {
        Patient p = new Patient();

        System.out.println("Please, input the patient info:");
        System.out.print("Name: "); 
        String name = br.readLine();
        p.setName(name);
        
        System.out.print("Surname: "); 
        String surname = br.readLine();
        p.setSurname(surname);
        
        System.out.print("Medical card number: "); 
        Integer medCardNumber=1; 
        Boolean validMedNumber = false;
        do{
            try {
                medCardNumber = br.read(); 
                validMedNumber = true;
            }catch(Exception e) {
                    System.out.println("Please introduce a valid medical card number which only contains numbers");
            }
        }while (validMedNumber = false);
        p.setMedical_card_number(medCardNumber);

        System.out.print("Gender: ");
        String gender = br.readLine();   
        do{
            if (gender.equalsIgnoreCase("male")) {
                    gender = "Male";
            } else if (gender.equalsIgnoreCase("female")) {
                    gender = "Female";
            } else{
                 System.out.print("Not a valid gender. Please introduce a gender (Male or Female): ");
                 gender = br.readLine();
            }
        }while (!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female")));
        p.setGender(gender);
        
        System.out.print("Date of birth [yyyy-mm-dd]: ");	
        String birthdate = br.readLine();
        Date bdate; 
        try {
            bdate = Date.valueOf(birthdate);
            if (bdate.before(Date.valueOf(LocalDate.now())) || bdate.equals(Date.valueOf(LocalDate.now()))) {
                p.setDob(bdate);
            } else {
                do {
                    System.out.print("Please introduce a valid date [yyyy-mm-dd]: ");
                    birthdate = br.readLine();
                    bdate = Date.valueOf(birthdate);
                } while ((!(bdate.before(Date.valueOf(LocalDate.now()))) || bdate.equals(Date.valueOf(LocalDate.now()))));
                p.setDob(bdate);
            }
        } catch (Exception e) {
            int b=0;
            do {	
                System.out.print("Please introduce a valid date format [yyyy-mm-dd]: ");
                birthdate = br.readLine();
                bdate = Date.valueOf(birthdate);
                if (bdate.before(Date.valueOf(LocalDate.now())) || bdate.equals(Date.valueOf(LocalDate.now()))) {
                        p.setDob(bdate);
                } else {
                    do {
                        System.out.print("Please introduce a valid date [yyyy-mm-dd]: ");							
                        birthdate = br.readLine();
                        bdate = Date.valueOf(birthdate);
                    } while ((!bdate.before(Date.valueOf(LocalDate.now()))) || bdate.equals(Date.valueOf(LocalDate.now())));
                    p.setDob(bdate);
                }
                b=1;
            } while (b==0);
        }

        System.out.print("Address: ");				
        String address = br.readLine();
        p.setAddress(address);

        System.out.print("Email: ");			
        String email = br.readLine();
        p.setEmail(email);
        
        System.out.print("Diagnosis: "); 
        String diagnosis = br.readLine();
        p.setDiagnosis(diagnosis);
        
        System.out.print("Allergies: "); 
        String allergies = br.readLine();
        p.setAllergies(allergies);
        
        System.out.print("Bitalino MACAddress: ");				
        String mac = br.readLine();
        p.setMacAddress(mac);
        
        System.out.println("Let's proceed with the registration, the username and password will be autogenerated by the system:");
        pw.println("Register");
        utilities.CommunicationWithServer.sendPatient(pw, p);
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
                medCard = br.read();
            }catch(Exception ex){
                System.out.println("Not a valid medical card number ONLY NUMBERS");
            }
            pw.print(medCard);
            patient = utilities.CommunicationWithServer.receivePatient(br);
        }
        return patient; 
    }

    
    private static void deletePatient(BufferedReader br, PrintWriter pw) throws Exception {
        //Show list with all patients.
        List<String> CompletePatientList = utilities.CommunicationWithServer.receivePatientList(br);
        for(int i =0;i<CompletePatientList.size();i++){
                System.out.println(CompletePatientList.get(i));
        }
        //Chose a Patient to delete
        System.out.println("Introduce de medical card number of the patient to delete: ");
        String medcard = br.readLine();
        pw.println("Medical Card= " + medcard);
    }
    

    public static void createDoctor(BufferedReader br, PrintWriter pw) throws Exception{
        Doctor d = new Doctor();

        System.out.println("Please, input the doctor info:");
        System.out.print("Name: ");
        String name = br.readLine();
        d.setDname(name); 

        System.out.print("Surname: ");
        String surname = br.readLine();
        d.setDsurname(surname);
        
        System.out.print("Email: ");
        String email = br.readLine();
        d.setDemail(email);
        
        System.out.print("Id: ");
        int id = br.read();
        d.setDoctorId(id);
        
        System.out.println("Let's proceed with the registration, the username and password will be autogenerated by the system:");
        pw.println("Register Doctor");
        utilities.CommunicationWithServer.sendDoctor(pw, d);
        String line = br.readLine();
        if (line.equals("Doctor successfully registered")){
            System.out.println("Success");
        } else{
            System.out.println("Doctor not registered");
        }
    }
    
    
    private static void showSignals (Patient P) throws Exception{
         Scanner sc = new Scanner(System.in);
         String signalFilename;
        // Saca listado de todas las señales  del server
                    
        System.out.println("Introduce filename of the signal:");
        signalFilename=sc.next();
        // Enviamos filename al server y que la busque
    }
    
    private static void editPatient (BufferedReader br, PrintWriter pw, Patient p) throws Exception{
        int option=1;
        String update;
                 
        while(option != 0) {
            System.out.println("Choose an option[0-2]:");
            System.out.println("\n0. Back \n1. Diagnosis \n2. Allergies");
            option = br.read();
            
            switch(option) {
                case 0:
                    option=0;
                    break;
                case 1:
                        System.out.println("Write diagnosis:");
                        update = br.readLine();
                        pw.println("Update diagnosis");
                        p.setDiagnosis(update);
                        //pw.println(update);
                        utilities.CommunicationWithServer.sendPatient(pw, p);
                        break;
                case 2:
                        System.out.println("Write Allergies:");
                        update = br.readLine();
                        pw.println("Update allergies");
                        p.setAllergies(update);
                        //pw.println(update);
                        utilities.CommunicationWithServer.sendPatient(pw, p);
                        break;
                default:
                     System.out.println("Not valid option");
                     break;
            }
        }
    }
    
    private static void updateMacAddress (BufferedReader br, PrintWriter pw, Patient p) throws Exception{
        String update;
        System.out.println("Write Bitalino MacAddress:");
        update = br.readLine();
        pw.println("Update Bitalino MacAddress");
        p.setMacAddress(update);
        //pw.println(update);      
        utilities.CommunicationWithServer.sendPatient(pw, p);
    }
}

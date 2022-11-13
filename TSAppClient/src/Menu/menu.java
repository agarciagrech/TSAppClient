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

import utilities.*;

public class menu {
   
     
       
    

  
    public static void main(String[] args) throws Exception {
        try {
            Socket socket = new Socket("localhost",9000);
            InputStream console = (System.in);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter (outputStream,true);
            BufferedReader bf = new BufferedReader (new InputStreamReader (inputStream));
            
        Scanner sc = new Scanner(System.in);
        int option;
        String trashcan;
        int id=0;
        int choice=1;
	
            do {
                try {
                    System.out.println("Welcome to Telesomnia. Choose Your Role");
                    System.out.println("1. Patient");
                    System.out.println("2. Doctor");
                    System.out.println("0. Exit");
                    choice = sc.nextInt();
                    printWriter.println(choice);
                    switch(choice) {
                    case 1:
                        //id = login();
                        //patientMenu(id);
                        Patient p = createPatient();
                        System.out.println("Your going to record your ECG and EMG signals");
                        utilities.CommunicationWithServer.recordSignal(p, 10);
                        break;
                    case 2:
                        //id = login();
                        //doctorMenu(id);
                        do{
                        System.out.println("Choose an option[0-4]:");
                        System.out.println("\n1.Register \n2. Register a new patient \n3. Edit Patient \n4. Show my Patients \n0. Exit");
                        option = sc.nextInt();
                        
                        printWriter.println(option);
                        Patient p_createdbyDoc = new Patient();
                        
                        switch(option){
                            case 0:
                                System.out.println("Thank you for using our system");
                                utilities.CommunicationWithServer.exitFromServer(printWriter, bf);
                                utilities.CommunicationWithServer.ReleaseResources(inputStream, outputStream, socket);
                                System.exit(0);
                                break;
                            case 1: System.out.println("Register");
                                Doctor d = createDoctor();
                                utilities.CommunicationWithServer.sendDoctor(socket, printWriter, d);
                                break;
                            case 2: System.out.println("Register a new Patient");
                                p_createdbyDoc = createPatient();
                                utilities.CommunicationWithServer.sendPatient(socket, printWriter, p_createdbyDoc);
                                break;
                            case 3:
                                System.out.println("Edit Patient");
                                p_createdbyDoc = editPatient(p_createdbyDoc);
                                utilities.CommunicationWithServer.sendPatient(socket, printWriter, p_createdbyDoc);
                                break;
                            case 4:
                                System.out.println("Show my Patients");
                                utilities.CommunicationWithServer.receivePatientList(socket, bf);
                                break;
                            }
                        } while(option != 0);
                        break;
                
                    case 0: 
                        utilities.CommunicationWithServer.exitFromServer(printWriter, bf);
                        utilities.CommunicationWithServer.ReleaseResources(inputStream, outputStream, socket);
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Not a valid option.");
                        break;
                    }
                
                } catch (Exception e) {
                        trashcan = sc.next();
                        System.out.println("Please introduce a valid option.");
                }
            }while(choice != 0);
    
        } catch (IOException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
	
        
        public static void patientMenu(Integer userID) throws Exception{
		Scanner sc = new Scanner (System.in);
		Patient patient = null;
                String trashcan;
                int option=0;
		do{

			int a = 0;
                      
			// Enviar patient id al server y que me devuelva un patient
			System.out.println("Hello Mr/Ms "+patient.getSurname());
			System.out.println("Choose an option [0-3]:"
					+ "\n1. Start recording \n2. Stop recording \n3. Show my recordings \n0.Exit");
			List<Signal> signals = new ArrayList<>();

			do {
				try {
					option = sc.nextInt();
					a = 1;
				} catch (Exception e) {
					trashcan = sc.next();
					System.out.println("Please input a valid option.");
				}
			} while (a==0);

			switch (option) {
				case 0:
					System.out.println("Thank you for using our system");
					// Desconectar 
					System.exit(0);
				case 1:
                                    
					System.out.println("You can start recording your signal");
					
					break;
				case 2:
					System.out.println("You can stop recording your signal");
		
					break;
				case 3:
					System.out.println("Please enter your medical card:");
                                        int medCard = Integer.parseInt(sc.next());
                                        System.out.println("Here you can see all your signals recorded");
                                        showSignals(patient);	
					break;
				default:
					System.out.println("Not a valid option.");
					break;
			}		
		}while(true);
	}
        
        
        
        
        
    private static String register(String name, String surname, Integer idUser) throws Exception { //AÑADIR EL EXISTING USERNAME
        //autogenerate username
        User user = new User();
        String trashcan;
        Scanner sc = null;
        String username = ""+name.charAt(0)+"."+surname+""+Integer.valueOf(surname.substring(0, 1));
        //autogenerated password
        String[] symbols = {"0", "1", "9", "7", "K", "Q", "a", "b", "c", "U","w","3","0"};
        int length = 14;
        Random random;
        random = SecureRandom.getInstanceStrong();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
             int indexRandom = random.nextInt ( symbols.length );
             sb.append( symbols[indexRandom] );
        }
        String password = sb.toString();
/* AL SEVER
        Pedir roles 
*/
		//ask the user for a role
		System.out.println("Type the chosen role ID: ");
		Integer id = null;
		int a=0;
		do {
			try {
				id = sc.nextInt();
                                
				a=1;
			} catch (Exception e) {
				trashcan = sc.next();
				System.out.println("Not a valid role id. Try again.");
			}
		} while (a==0);

		
                
        //generate the hash
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] hash = md.digest();
        System.out.println("The autogenerated username is:"+ username);
        System.out.println("The autogenerated password is:"+ password);
         user.setRole(id);
         user.setPassword(password);
         user.setUsername(username);
         return user.toString();
        
    }

    // Coger username y password, se la pasamos al server y compruebe --> si bien que me envíe un id y si no pues que me invie un -1
    private static int login() throws Exception{
        Scanner sc = new Scanner (System.in);
        User user = new User();
        int id =0;
        do{
        System.out.println("Please enter your username and password:");
        System.out.println("Username:");
        String username = sc.next();
        System.out.println("Password:");
        String password = sc.next();
        user.setPassword(password);
        user.setUsername(username);
        // parte del server 
        }while (id == -1);
        return id;
    }
    
    
    public static void changePassword() {
        Scanner sc = new Scanner (System.in);
        try{
            System.out.println("Please enter your username and password:");
            System.out.println("Username:");
            String username = sc.next();
            System.out.println("Password:");
            String password = sc.next();
            User user = new User (username, password);
            // Enviar al server --> busque el user
            System.out.println("Introduce the new password: ");
            String newPassword1 = sc.next();
            System.out.println("Confirm your new password: ");
            String newPassword2 = sc.next();
            if(newPassword1.equals(newPassword2)) {
                    // Mandamos al server 
                    System.out.println("Password updated");
            } else {
                    System.out.println("Error. Password confirmation does not match");
            }
        }catch(Exception ex) {
                ex.printStackTrace();
        }
    }
    
 //For the final practice this will be void, but right now, as the db doesn't work, it will return a patient.  
 public static Patient createPatient () throws NotBoundException, Exception {

        Scanner sc = new Scanner (System.in);
        Patient p = new Patient();

        System.out.println("Please, input the patient info:");
        System.out.print("Name: "); 
        String name = sc.next();
        p.setName(name);
        System.out.print("Surname: "); 
        String surname = sc.next();
        p.setSurname(surname);
        System.out.print("Medical card number: "); 
        Integer medCardNumber=1; 
        Boolean validMedNumber = false;
        
        /*
        do { 
            try {
            medCardNumber = sc.nextInt(); 
            validMedNumber = true;
            }catch(Exception e) {
                    System.out.println("Please introduce a valid medical card number which only contains numbers");
            }
            System.out.print("Error. Please introduce medical card number: "); 
        } while (patientman.selectPatient(medCardNumber) != null&&(!validMedNumber) );
        */
        do{
        try {
            medCardNumber = sc.nextInt(); 
            validMedNumber = true;
            }catch(Exception e) {
                    System.out.println("Please introduce a valid medical card number which only contains numbers");
            }
        
        }while (validMedNumber = false);
        // COmprobar que no hay un paciente con la medical card number
        p.setMedical_card_number(medCardNumber);

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
                    } while ((!bdate.before(Date.valueOf(LocalDate.now()))) || bdate.equals(Date.valueOf(LocalDate.now())));
                    p.setDob(bdate);
            }
        } catch (Exception e) {
            int b=0;
            do {
                    try {	
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
                                    } while ((!bdate.before(Date.valueOf(LocalDate.now()))) || bdate.equals(Date.valueOf(LocalDate.now())));
                                    p.setDob(bdate);
                            }
                            b=1;
                    } catch (Exception e1) {
                    }
            } while (b==0);
        }

        System.out.print("Address: ");				
        String address = sc.next();
        p.setAddress(address);

        System.out.print("Email: ");// SOmetimes it works and others it skips to asking for macaddress				
        String email = sc.next();
        p.setEmail(email);
        
        System.out.print("Bitalino MACAddress: ");				
        String mac = sc.next();
        p.setMacAddress(mac);
        
        //System.out.println("Let's proceed with the registration, the username and password will be autogenerated by the system:");
        //register(p.getName(),p.getSurname(), p.getMedical_card_number()); 
        //System.out.println("The patient was succesfully added to the database");
        return p;
    }
    

    private static Patient selectPatient() throws Exception{
        List<Patient> patientList = new ArrayList<Patient>();
        // que el server nos envie una lista de pacietnes
        Patient patient = null;
        Scanner sc = new Scanner(System.in);
        while(patientList.isEmpty()){
            Integer medCard=null;
            System.out.println(patientList.toString());
            System.out.println("Enter the medical card number of the chosen patient: ");
            try{
                medCard = Integer.parseInt(sc.next());
            }catch(Exception ex){
                System.out.println("Not a valid medical card number ONLY NUMBERS");
            }
            
        }
        // Eviar medcard al server busque y nos devuelva un paciente
        return patient; 
    }

    private static void deletePatient() throws Exception {
        // que el server me de lista 
        Scanner sc = new Scanner (System.in);
        
        System.out.println("Introduce de medical card number of the patient to delete: ");
        String medcard = sc.next();
        
        // Enviar al server y que busque y elimine
    }
    
    //For the final practice this will be void, but right now, as the db doesn't work, it will return a patient.  
    public static Doctor createDoctor() throws Exception{
        Doctor d = new Doctor();
        Scanner sc = new Scanner (System.in);

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
        
        System.out.print("Id: ");
        int id = sc.nextInt();
        d.setDoctorId(id);
        
        //System.out.println("Let's proceed with the registration, the username and password will be autogenerated by the system:");
        return d;
        //register(d.getDname(), d.getDsurname(), d.getDoctorId()); 
        //System.out.println("The doctor was succesfully added to the database");
    }
    
    
   
    
    private static void doctorMenu(Integer d) throws Exception {
        Patient patient = selectPatient();
        Scanner sc = new Scanner (System.in);
        int option=0;
        String trashcan;
        do {
            System.out.println("Choose an option[0-2]:");
            System.out.println("\n1.Add a patient \n2. Edit Patient \n3. Consult recordings \\n 0. Exit");

            int a = 0;
            do {
                try {
                        option = sc.nextInt();
                        a = 1;
                } catch (Exception e) {
                        trashcan = sc.next();
                        System.out.println("Please input a valid option.");
                }
            } while (a==0);

            switch(option) {
            case 0:
                    System.out.println("Thank you for using our system");
                    // Desconectar --> release resources y todo
                    System.exit(0);
            case 1: System.out.println("Register a new Patient");
                    createPatient();
            case 2:
                    System.out.println("Edit Patient");
                    editPatient(patient);
                    break;
            case 3:
                    System.out.println("Consult recordings");
                    showSignals(patient);
                    break;
            }
        } while(true);
    }
    
    private static void showSignals (Patient P) throws Exception{
         Scanner sc = new Scanner(System.in);
         String signalFilename;
        // Saca listado de todas las señales  del server
                    
                    System.out.println("Introduce filename of the signal:");
                    signalFilename=sc.next();
                    // Enviamos filename al server y que la busque
    }
    
    private static Patient editPatient (Patient p) throws Exception{
        Scanner sc = new Scanner(System.in);
        int option=0;
        String trashcan;
        String update;
        int a = 0;
                
        do {
            System.out.println("Choose an option[0-3]:");
            System.out.println("\n0. Back \n1. Diagnosis \n2. Allergies \n3. Bitalino MacAddres");
         do {
                try {
                        option = sc.nextInt();
                        a = 1;
                } catch (Exception e) {
                        trashcan = sc.next();
                        System.out.println("Please input a valid option.");
                }
            } while (a==0);

            switch(option) {
                case 0:
                    option = 0;
                    break;
            case 1:
                    System.out.println("Write diagnosis:");
                    update = sc.next();
                    p.setDiagnosis(update); //Esto se hará por db
                    // Mandar diagnosis al server y que haga update
                    break;
            case 2:
                    System.out.println("Write Allergies:");
                    update = sc.next();
                    p.setAllergies(update); //Esto se hará por db
                    // Mandar diagnosis al server y que haga update
                    break;
            case 3:
                    System.out.println("Write Bitalino MacAddress:");
                    update = sc.next();
                    p.setMacAddress(update); //Esto se hará por db
                    // Mandar diagnosis al server y que haga update
                    break;
            }
        } while(option != 0);
        return p;
    }
}

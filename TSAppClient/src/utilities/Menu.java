/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import BITalino.BITalino;
import BITalino.BITalinoException;
import BITalino.BitalinoDemo;
import BITalino.Frame;
import java.rmi.NotBoundException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import pojos.Patient;
import pojos.Signal;

/**
 *
 * @author agarc
 */
public class Menu {
    private static Scanner sc = new Scanner(System.in);
    
    public static Signal recordSignal (String name){
        Frame[] frame;
        BITalino bitalino = null;
        Signal s = new Signal();
        int[] ecg_values = new int[10];
        int[] emg_values = new int[10];
        
         try {
            bitalino = new BITalino();
            // Code to find Devices
            //Only works on some OS
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println(devices);

            //You need TO CHANGE THE MAC ADDRESS
            //You should have the MAC ADDRESS in a sticker in the Bitalino
            String macAddress = "98:D3:91:FD:3E:C4"; //codigo del BITALINO
            
            //Sampling rate, should be 10, 100 or 1000
            int SamplingRate = 10;
            bitalino.open(macAddress, SamplingRate);

            // Start acquisition on analog channels A2 and A6
            // For example, If you want A1, A3 and A4 you should use {0,2,3}
            int[] channelsToAcquire = {1,2};
            bitalino.start(channelsToAcquire);

            //Read in total 10000000 times --> por que elegimos este num
            for (int j = 0; j < 10; j++) {

                //Each time read a block of 10 samples --> por que elegimos este num
                int block_size=10;
                frame = bitalino.read(block_size);

                System.out.println("size block: " + frame.length);

                //Store the samples --> preguntar si se guarda el fichero 
                for (int i = 0; i < frame.length; i++) {
                    ecg_values[i]=frame[i].analog[0];
                    emg_values[i]=frame[i].analog[1];
                   // System.out.println(" seq: " + frame[i].seq + " "
                            //+ frame[i].analog[0] + " ");
                }
                s.setECG_values(ecg_values);
                s.setEMG_values(emg_values);
                //s.ImprimirECG(s.getECG_values());
                //s.ImprimirEMG(s.getEMG_values());
            }
            //stop acquisition
            bitalino.stop();
        } catch (BITalinoException ex) {
            Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //close bluetooth connection
                if (bitalino != null) {
                    bitalino.close();
                }
            } catch (BITalinoException ex) {
                Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return s; 
    }
    
    public static Patient sendPatient(){
        sc = new Scanner (System.in);
        Patient p = new Patient();

        System.out.println("Please, input the patient info:");
        System.out.print("Name: "); 
        String name = sc.next();
        p.setName(name);
        System.out.print("Surname: "); 
        String surname = sc.next();
        p.setSurname(surname);
        System.out.print("Medical card number: "); 
        
        Integer medCardNumber = sc.nextInt();     
        p.setMedical_card_number(medCardNumber);

        System.out.print("Gender: ");
        String gender = sc.next();   
        if (gender.equalsIgnoreCase("male")) {
            gender = "Male";
        } else {
            gender = "Female";
        }
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

        System.out.print("Email:: ");				
        String email = sc.next();
        p.setEmail(email);
        
        return p;
    }
    
    public void recievePatient(){
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.*;
import utilities.*;

/**
 *
 * @author agarc
 */
public class CommunicationWithServer {
    
    public static Socket connectToServer() {
        Socket socket1 = new Socket();
        /*
        try {
            //String[] datos = getDataFromFile();
            //in datos[1] we have the number of the port whereas in datos[0] the ip address
            //int ip = ;
            //socket1 = new Socket(datos[0], ip);
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        return socket1;
    }
    
    public static void sendDoctor(PrintWriter printWriter, Doctor doctor) {
       printWriter.println(doctor.toString());
    }
    
    public static void sendPatient(PrintWriter printWriter, Patient patient) {
        printWriter.println(patient.toString());
    }
    
    public static void sendSignal(PrintWriter printWriter, Signal signal) {
        printWriter.println(signal.toString());
    }
    
    public static boolean exitFromServer(PrintWriter printWriter, Socket socket) {
        try {
            //try {
                printWriter.close();
           /* } catch (IOException ex) {
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
   
    
    public static boolean recievePatient(BufferedReader bufferReader){
        boolean recieved = true; 
        Patient p = new Patient();
        try{
            String line = bufferReader.readLine();
            line=line.replace("{", "");
            line=line.replace("Patient", "");
            String[] atribute = line.split(",");
            SimpleDateFormat  format = new SimpleDateFormat("dd/MM/yyyy"); 
        
            for (int i =0;i <atribute.length; i++){
                String[] data2 = atribute[i].split("=");
                for (int j =0;j <data2.length - 1; j++){
                    data2[j]=data2[j].replace(" ", "");
                    switch(data2[j]){
                        case "medical_card_number": p.setMedical_card_number(Integer.parseInt(data2[j+1]));
                                                     break;
                        case "name":p.setName(data2[j+1]); 
                                     break;
                        case "surname":  p.setSurname(data2[j+1]);
                                        break;
                         case "dob": 
                            try{
                               p.setDob(format.parse(data2[j+1]));
                            }catch(ParseException ex){
                                
                            }
                                     break;
                        case "address": p.setAddress(data2[j+1]);
                                        break;
                        case "email": p.setEmail(data2[j+1]);
                                     break;
                        case "diagnosis": p.setDiagnosis(data2[j+1]);
                                         break;
                        case "allergies":  p.setAllergies(data2[j+1]);
                                        break;
                        case "gender": p.setGender(data2[j+1]);
                                        break;
                        case "userId": p.setUserId(Integer.parseInt(data2[j+1]));
                                        break;
                        case "macAddress": p.setMacAddress(data2[j+1]);
                                         break;
                    }
 
                }
                
             }
        System.out.println("Patient recieved:");
        System.out.println(p.toString());
        
        
        }catch(IOException exception){
            recieved = false;
        }
        return recieved; 
    }
        public static boolean recieveDoctor(BufferedReader bufferReader){
        boolean recieved = true; 
        Doctor d= new Doctor();
        try{
            String line = bufferReader.readLine();
            line=line.replace("{", "");
            line=line.replace("Patient", "");
            String[] atribute = line.split(",");
            for (int i =0;i <atribute.length; i++){
                String[] data2 = atribute[i].split("=");
                for (int j =0;j <data2.length - 1; j++){
                    data2[j]=data2[j].replace(" ", "");
                    switch(data2[j]){
                         case "name":d.setDname(data2[j+1]); 
                                     break;
                        case "surname":d.setDsurname(data2[j+1]);
                                        break;
                        case "email": d.setDemail(data2[j+1]); 
                                     break;
                        case "id":d.setDoctorId(Integer.parseInt(data2[j+1]));
                                        break;
                    }
 
                }
                
             }
        System.out.println("Doctor recieved:");
        System.out.println(d.toString());
        
        
        }catch(IOException exception){
            recieved = false;
        }
        return recieved; 
    }
    public static void main(String args[]) throws IOException {
        System.out.println("Starting Client...");
        Socket socket = new Socket("localhost", 9009);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        Integer medical_card_number = 0;
        String name = "paquito";
        String surname = "perez";
        Date dob = new Date(2001, 01, 01);
        String address = "calle 1";
        String email = "dfhbvidfhbg@gmail.com";
        String diagnosis = "fihbaeifhbvaifbviafhbv";
        String allergies = "fhbvidfbnijdgbnid";
        String gender = "male";
        Integer userId = 1;
        String macAddress = "dzgbihfgbihb";
        Patient p = new Patient(medical_card_number, name, surname, dob, address,  email,  diagnosis,  allergies,  gender,  userId,  macAddress);
        
        /*
        String name = "First signal";
        Signal s = new Signal();
        Patient p = Menu.sendPatient();
        s = Menu.recordSignal(name);
        String ecg = Arrays.toString(s.getECG_values());
        String emg = Arrays.toString(s.getEMG_values());
        printWriter.println(p.toString());
        printWriter.println(ecg);
        printWriter.println(emg);
*/
        sendPatient(printWriter,p);
        System.out.println("Connection established... sending text");
        printWriter.println("Stop");
        exitFromServer(printWriter, socket);
        System.exit(0);
    }
    
    }



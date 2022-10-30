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
        Patient p1 = new Patient();
        try{
            String [] pData = new String[200];
            String line = bufferReader.readLine();
            //Ver como se cogen los datos de la consola y meterlos en line, después ir asociándolos a pData.
            
            p1.setMedical_card_number(Integer.parseInt(pData[0]));
            p1.setName(pData[1]);
            p1.setSurname(pData[2]);
            //p1.setDob(pData[3]); //convertir string en date
            p1.setAddress(pData[4]);
            p1.setEmail(pData[5]);
            p1.setDiagnosis(pData[6]);
            p1.setAllergies(pData[7]);
            p1.setGender(pData[8]);
            p1.setUserId(Integer.parseInt(pData[9]));
            p1.setMacAddress(pData[10]);
        }catch(IOException exception){
            recieved = false;
        }
        return recieved; 
    }
    
    public static void main(String args[]) throws IOException {
        System.out.println("Starting Client...");
        Socket socket = new Socket("localhost", 9009);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        /*Integer medical_card_number = 0;
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
        */
        String name = "First signal";
        Signal s = new Signal();
        Patient p = Menu.sendPatient();
        s = Menu.recordSignal(name);
        String ecg = Arrays.toString(s.getECG_values());
        String emg = Arrays.toString(s.getEMG_values());
        printWriter.println(p.toString());
        printWriter.println(ecg);
        printWriter.println(emg);
        System.out.println("Connection established... sending text");
        printWriter.println("Header File\n\n");
        printWriter.println("Stop");
        exitFromServer(printWriter, socket);
        System.exit(0);
    }
    
    }



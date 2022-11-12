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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import pojos.*;
import utilities.*;

/**
 *
 * @author agarc
 */
public class CommunicationWithServer {
    
    static Scanner sc = new Scanner(System.in);
    static Socket socket = new Socket();
    static InputStream inputStream = null;
    static OutputStream outputStream = null;
               
    
    public static boolean connectToServer() {
        try {
            socket= new Socket("192.168.1.200",9200);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            BufferedReader br = new BufferedReader (new InputStreamReader(inputStream));
            PrintWriter pw = new PrintWriter(outputStream,true);
            int choice = sc.nextInt();
            try {
                System.out.println("Please, introduce your role (1-patient/2-doctor): ");
                pw.print(choice);
                br.readLine();
                if (br.readLine().equals("Connection stablished")){
                    return true;
                }
            } catch (IOException ex) {
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return false;
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
    
    
    public void recordSignal(Patient p, int samplingRate) {
        try{
        socket= new Socket("192.168.1.200",9200);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(outputStream,true);
        BufferedReader bf = new BufferedReader (new InputStreamReader (inputStream));
        
        Frame[] frame;
        BITalino bitalino = null;
        Signal s = new Signal();
        int[] ecg_values = new int[1000000];
        int[] emg_values = new int[100];
        try {
            bitalino = new BITalino();
            // Code to find Devices
            //Only works on some OS
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println(devices);


            String macAddress = p.getMacAddress();

            //Sampling rate, should be 10, 100 or 1000
            bitalino.open(macAddress, samplingRate);

            // Start acquisition on analog channels A2 and A6
            // For example, If you want A1, A3 and A4 you should use {0,2,3}
            int[] channelsToAcquire = {1,2}; //for ECG and EMG
            bitalino.start(channelsToAcquire);

            //Read in total 10000000 times --> por que elegimos este num
            for (int j = 0; j < 10000000; j++) {

                //Each time read a block of 10 samples --> por que elegimos este num
                int block_size=10;
                frame = bitalino.read(block_size);

                System.out.println("size block: " + frame.length);

                for (int i = 0; i < frame.length; i++) {
                    ecg_values[i]=frame[i].analog[0];
                    emg_values[i]=frame[i].analog[1];
                    System.out.println(" seq: " + frame[i].seq + " "
                            + frame[i].analog[0] + " ");
                }
                s.setECG_values(ecg_values);
                s.setEMG_values(emg_values);

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
            // Send signals to server:
            pw.println("ECG");
            for (int a = 0;a<ecg_values.length; a++){
                pw.print(ecg_values[a]);
            }
            pw.println("ECG END");
            String ECGresponse = bf.readLine();
            if (ECGresponse.equalsIgnoreCase("success")){
                System.out.println("Record sended correctly to server");
            }else{
                System.out.println("There was a problem sending ECG to server");
            }
            pw.println("EMG");
            for (int a = 0;a<ecg_values.length; a++){
                pw.print(ecg_values[a]);
            }
            pw.println("EMG END");
            String EMGresponse = bf.readLine();
            if (EMGresponse.equalsIgnoreCase("success")){
                System.out.println("Record sended correctly to server");
            }else{
                System.out.println("There was a problem sending EMG to server");
            }
            try {
                //Type of signal + date ".txt"
                Calendar c = Calendar.getInstance();
                String day=Integer.toString(c.get(Calendar.DATE));
                String month=Integer.toString(c.get(Calendar.MONTH));
                String year=Integer.toString(c.get(Calendar.YEAR));
                pw.println("ECG filename= ECG"+ day+month+year );
                pw.println("EMG filename= ECG"+ day+month+year );
                String ruta = "../TSApp/ECG"+day+month+year+".txt";
                String ruta2 = "../TSApp/EMG"+day+month+year+".txt";
                String contenido = Arrays.toString(s.getECG_values());
                String contenido2 = Arrays.toString(s.getEMG_values());
                File file = new File(ruta);
                File file2 = new File(ruta2);
                if (!file.exists()) {
                    file.createNewFile();
                }
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                FileWriter fwECG = new FileWriter(file);
                FileWriter fwEMG = new FileWriter(file2);
                BufferedWriter bwECG = new BufferedWriter(fwECG);
                BufferedWriter bwEMG = new BufferedWriter(fwEMG);
                bwECG.write(contenido);
                bwEMG.write(contenido2);
                bwECG.close();
                bwEMG.close();
                
                System.out.println("Ok");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ReleaseResources(inputStream, outputStream);
        }
    }
    // This method is going to return the filenames of all the signals recorded:
    public String[] ShowSignals(Patient p){
        try {
        socket= new Socket("192.168.1.200",9200);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(outputStream,true);
        BufferedReader bf = new BufferedReader (new InputStreamReader (inputStream));
            
        String[] filenames = null;
        List singals = new ArrayList();
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        // Pedimos al server que nos envie la lista de señales:
        pw.println("Send Signals");
        pw.println("Patient= "+p.getMacAddress());
        // VOY A ASUMIR QUE SE ENVIAN LOS FILENAME SEPARADOS POR \n
        String line = bf.readLine();
        while ((line = bf.readLine()) != null) {
            filenames =line.split("/n");
        }
        return filenames;
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            ReleaseResources(inputStream, outputStream);
        }
        return null;
    }
    
    public static String loginCheck(String username,String password){
        try {
            socket= new Socket("192.168.1.200",9200);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(outputStream,true);
            BufferedReader bf = new BufferedReader (new InputStreamReader (inputStream));
            
            pw.print(1);
            pw.println("Username="+username+"Password="+password);
            String response = bf.readLine();
            if(response.equals("Correct login")){
                return "Correct login";
            }else{
                if(response.equals("Incorrect username or password")){
                    return "User not found on db";
                }
                else{
                    return "Problem connecting with server";
                }
            }
           } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ReleaseResources(inputStream, outputStream);
        }
        return null;
    }
    
    public static void ReleaseResources(InputStream inputStream, OutputStream outputStream){
        try{
            inputStream.close();
        }catch(IOException ex){
             Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            outputStream.close();
        }catch(IOException ex){
             Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     public static void ReleaseAllResources(InputStream inputStream, OutputStream outputStream, Socket socket ){
        try{
            inputStream.close();
        }catch(IOException ex){
             Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            outputStream.close();
        }catch(IOException ex){
             Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            socket.close();
        }catch(IOException ex){
             Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        boolean check;
       check = connectToServer();
       if (check == true){
           System.out.println("user role part done correctly");
       }else{
           System.out.println("Error in sending user role");
       }
       // Probando el login:
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
        
        String answer = loginCheck(user.getUsername(),user.getPassword());
        System.out.println(answer);
        }while (id == -1);
        
       
       
    }
}
    
    
    



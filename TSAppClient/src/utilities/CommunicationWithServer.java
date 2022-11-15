/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import BITalino.*;
import java.io.*;
import java.net.Socket;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import pojos.*;
/**
 *
 * @author agarc
 */
public class CommunicationWithServer {

    public static Socket socket = new Socket();
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;
               
    
    public boolean connectToServer() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            BufferedReader br = new BufferedReader (new InputStreamReader(inputStream));
            PrintWriter pw = new PrintWriter(outputStream,true);
            int choice = br.read();
            System.out.println("Please, introduce your role (1-patient/2-doctor): ");
            pw.print(choice);
            br.readLine();
            if (br.readLine().equals("Connection stablished")){
                return true;
            }
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return false;
    }
    
    public static void sendDoctor(Socket socket,PrintWriter pw, Doctor doctor) {
         pw.println(doctor.toString());
    }
    
    public static void sendPatient(Socket socket,PrintWriter pw,Patient patient) {
        pw.println(patient.toString());
    }
    
    public static void sendSignal(PrintWriter printWriter, Signal signal) {
        printWriter.println(signal.toString());
    }
    
    public static boolean receivePatient(Socket socket, BufferedReader bf){
        boolean recieved = true;
        try{
            Patient p = new Patient();
        
            String line = bf.readLine();
            line=line.replace("{", "");
            line=line.replace("Patient", "");
            String[] atribute = line.split(",");
            SimpleDateFormat  format = new SimpleDateFormat("dd/MM/yyyy"); 
        
            for (int i =0;i <atribute.length; i++){
                String[] data2 = atribute[i].split("=");
                for (int j =0;j <data2.length - 1; j++){
                    data2[j]=data2[j].replace(" ", "");
                    switch(data2[j]){
                        case "medical_card_number": 
                            p.setMedical_card_number(Integer.parseInt(data2[j+1]));
                            break;
                        case "name":
                            p.setName(data2[j+1]);
                            break;
                        case "surname":
                            p.setSurname(data2[j+1]);
                            break;
                         case "dob":
                            try {
                                p.setDob(format.parse(data2[j+1]));
                            } catch (ParseException ex) {
                                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        case "address": 
                            p.setAddress(data2[j+1]);
                            break;
                        case "email":
                            p.setEmail(data2[j+1]);
                            break;
                        case "diagnosis": 
                            p.setDiagnosis(data2[j+1]);
                            break;
                        case "allergies":
                            p.setAllergies(data2[j+1]);
                            break;
                        case "gender": 
                            p.setGender(data2[j+1]);
                            break;
                        case "userId": 
                            p.setUserId(Integer.parseInt(data2[j+1]));
                            break;
                        case "macAddress": 
                            p.setMacAddress(data2[j+1]);
                            break;
                    }
                }
            }
            System.out.println("Patient received:");
            System.out.println(p.toString());
        }catch(IOException exception){
            recieved = false;
        }
        return recieved; 
    }
    
    
    public static boolean receiveDoctor(BufferedReader bufferReader){
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
    
    
    
    public static void recordSignal(Patient p, int samplingRate, PrintWriter pw) {
        Frame[] frame;
        BITalino bitalino = null;
        Signal s = new Signal();
        int[] ecg_values = new int[10];
        int[] emg_values = new int[10];
        
        try {
            bitalino = new BITalino();
            // Code to find Devices
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println(devices);

            String macAddress = p.getMacAddress();

            bitalino.open(macAddress, samplingRate);

            int[] channelsToAcquire = {1,2}; //for EMG and ECG
            bitalino.start(channelsToAcquire);

            //Read in total 10000000 times --> por que elegimos este num
            for (int j = 0; j < 10; j++) {
                //Each time read a block of 10 samples to make the trials easier, but we plan to change it
                int block_size=1;
                frame = bitalino.read(block_size);

                System.out.println("size block: " + frame.length);

                for (int i = 0; i < frame.length; i++) {
                    ecg_values[j]=frame[i].analog[1];
                    emg_values[j]=frame[i].analog[0];
                    System.out.println(" seq: " + frame[i].seq + " "
                            + frame[i].analog[0] + " seq: " + frame[i].seq + " "
                            + frame[i].analog[1] + " ");
                }
            }
            System.out.println(Arrays.toString(ecg_values));
            System.out.println(Arrays.toString(emg_values));
            s.setECG_values(ecg_values);
            s.setEMG_values(emg_values);
            //stop acquisition
            bitalino.stop();

            pw.println("ECG: " + Arrays.toString(ecg_values) + " // " + "EMG: " + Arrays.toString(emg_values));
            //Type of signal + date ".txt"
            Calendar c = Calendar.getInstance();
            String day=Integer.toString(c.get(Calendar.DATE));
            String month=Integer.toString(c.get(Calendar.MONTH));
            String year=Integer.toString(c.get(Calendar.YEAR));

            pw.println("ECG filename= ECG"+ day+month+year );
            pw.println("EMG filename= EMG"+ day+month+year );

            String ruta = "../TSAppClient/ECG"+day+month+year+".txt";
            String ruta2 = "../TSAppClient/EMG"+day+month+year+".txt";
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
    }
    
    
    // This method is going to return the filenames of all the signals recorded:
    public String[] ShowSignals(Patient p){
        try {
            socket = new Socket("localhost", 9000);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(outputStream,true);
            BufferedReader bf = new BufferedReader (new InputStreamReader (inputStream));

            String[] filenames = null;
            List singals = new ArrayList();
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
        return null;
    }
    
    
    public static void receivePatientList(Socket socket,BufferedReader bf){
        List<String> patientList = new ArrayList();
        boolean stop= true;
        try {
            while(stop){
               String line = bf.readLine();
               if (!line.equalsIgnoreCase("End of list")) {
                   stop=true;
                   System.out.println(line);
                   patientList.add(line);
               }else{
                   stop=false;
               }
           }   
            System.out.println("after while");
            for(int i =0;i<patientList.size();i++){
                System.out.println(patientList.get(i));
            }
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void exitFromServer(InputStream inputStream, OutputStream outputStream, Socket socket ){
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
    
    public static boolean ReleaseResources(PrintWriter pw, BufferedReader br) {
        pw.close();
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
    
    
    



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import BITalino.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import pojos.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 *
 * @author agarc
 */
public class CommunicationWithServer {
    
    public static Socket connectToServer() {
        Socket socket = new Socket();
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter (outputStream,true);
            BufferedReader bf = new BufferedReader (new InputStreamReader (inputStream));
            System.out.println("Introduce your IP: ");
            String ip = bf.readLine();
            socket = new Socket(ip, 9000);
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return socket;
    }
    
    public static void sendDoctor(PrintWriter pw, Doctor doctor) {
         pw.println(doctor.toString());
    }
    
    public static void sendPatient(PrintWriter pw,Patient patient) {
        pw.println(patient.toString());
    }
    
    public static void sendSignal(PrintWriter printWriter, Signal signal) {
        printWriter.println(signal.toString());
    }
    
    public static void sendUser(PrintWriter printWriter, User user) {
        printWriter.println(user.toString());
    }
    
    public static Patient receivePatient(BufferedReader bf){
        Patient p = new Patient();
        try{
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
        }catch(IOException ex){
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p; 
    }
    
    
    public static Doctor receiveDoctor(BufferedReader bufferReader){
        Doctor d= new Doctor();
        try{
            String line = bufferReader.readLine();
            line=line.replace("{", "");
            line=line.replace("Doctor", "");
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
        }catch(IOException ex){
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return d;
    }
    
    
    public static Signal receiveSignal(BufferedReader br){
        Signal s = new Signal();
        try {
        String line = br.readLine();
        line=line.replace("{", "");
        line=line.replace("Signal", "");
        String[] atribute = line.split(",");
        SimpleDateFormat  format = new SimpleDateFormat("dd/MM/yyyy");
        
        for (int i =0;i <atribute.length; i++){
            String[] data2 = atribute[i].split("=");
            for (int j =0;j <data2.length - 1; j++){
                data2[j]=data2[j].replace(" ", "");
                switch(data2[j]){
                    case "signalId":
                        s.setSignalId(Integer.parseInt(data2[j+1]));
                        break;
                    case "ECG_values":
                        //no estoy segura de si están separados por una coma o un espacio
                        String[] separatedString = data2[j+1].split(",");
                        int[] ECG = new int[separatedString.length];
                        for(int k=0; k<separatedString.length; k++){
                            ECG[k] = Integer.parseInt(separatedString[k]);
                        }
                        s.setECG_values(ECG);
                        break;

                    case "EMG_values":
                        separatedString = data2[j+1].split(",");
                        int[] EMG = new int[separatedString.length];
                        for(int k=0; k<separatedString.length; k++){
                            EMG[k] = Integer.parseInt(separatedString[k]);
                        }
                        s.setEMG_values(EMG);
                        break;
                    case "startDate":
                        try {
                            s.setStartDate(format.parse(data2[j+1]));
                        } catch (ParseException ex) {
                            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                }
            }
        }
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
    
    public static User receiveUser (BufferedReader br){
        User u = new User();
        try {
        String line = br.readLine();
        line=line.replace("{", "");
        line=line.replace("User", "");
        String[] atribute = line.split(",");
        for (int i =0;i <atribute.length; i++){
            String[] data2 = atribute[i].split("=");
            for (int j =0;j <data2.length - 1; j++){
                data2[j]=data2[j].replace(" ", "");
                switch(data2[j]){
                    case "username":
                        u.setUsername(data2[j+1]);
                        break;
                    case "password":
                        u.setPassword(data2[j+1]);
                        break;
                    case "role":
                        u.setRole(Integer.parseInt(data2[j+1]));
                        break;
                    case "userId":
                        u.setUserId(Integer.parseInt(data2[j+1]));
                        break;
                }
            }
        }
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
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
    public static String[] ShowSignals(BufferedReader bf, PrintWriter pw, Patient p){
        try {
            String[] filenames = null;
            // Pedimos al server que nos envie la lista de señales:
            pw.println("Send Signals");
            pw.println("Patient= "+p.getMacAddress());
            // VOY A ASUMIR QUE SE ENVIAN LOS FILENAME SEPARADOS POR \n
            String line = bf.readLine();
            while ((line = bf.readLine()) != null) {
                filenames =line.split("\n");
            }
            return filenames;
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public static List<String> receivePatientList(BufferedReader bf){
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
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return patientList;
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
    
    
    



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import BITalino.*;
import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import pojos.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class CommunicationWithServer {

    public static Socket connectToServer() throws IOException {
        boolean connected = false;
        Socket socket = null; 
        do {
            socket = new Socket();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Introduce your IP: ");
            String ip = br.readLine();
            try {
                System.out.println(connected);
                socket = new Socket(ip, 9000);

                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                
                connected = socket.isConnected();  
                System.out.println(connected);
            } catch (IOException ex) {
                if (connected == false) {
                    System.out.println("Connection failed");
                }
                
            }
        } while (!connected);

        return socket;
    }

    public static void sendDoctor(PrintWriter pw, Doctor doctor) {
        pw.println(doctor.toString());
        System.out.println("doctor sended");
    }

    public static void sendPatient(PrintWriter pw, Patient patient) {
        System.out.println(patient.toString()); //SE MANDA BIEN
        pw.println(patient.toString());

    }

    public static void sendSignal(PrintWriter printWriter, Signal signal) {
        printWriter.println(signal.toString());
    }

    public static void sendUser(PrintWriter printWriter, User user) {
        printWriter.println(user.toString());
    }

    public static Patient receivePatient(BufferedReader bf) {
        Patient p = new Patient();

        try {
            String line = bf.readLine();
            line = line.replace("{", "");
            line = line.replace("Patient", "");
            line = line.replace("}", "");
            String[] atribute = line.split(",");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < atribute.length; i++) {
                String[] data2 = atribute[i].split("=");
                for (int j = 0; j < data2.length - 1; j++) {
                    data2[j] = data2[j].replace(" ", "");
                    switch (data2[j]) {
                        case "medical_card_number":
                            p.setMedical_card_number(Integer.parseInt(data2[j + 1]));
                            break;
                        case "name":
                            p.setName(data2[j + 1]);
                            break;
                        case "surname":
                            p.setSurname(data2[j + 1]);
                            break;
                        case "dob":
                            try {
                            p.setDob(format.parse(data2[j + 1]));
                        } catch (ParseException ex) {
                            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;

                        case "address":
                            p.setAddress(data2[j + 1]);
                            break;
                        case "email":
                            p.setEmail(data2[j + 1]);
                            break;
                        case "diagnosis":
                            p.setDiagnosis(data2[j + 1]);
                            break;
                        case "allergies":
                            p.setAllergies(data2[j + 1]);
                            break;
                        case "gender":
                            p.setGender(data2[j + 1]);
                            break;

                        case "userId":
                            p.setUserId(Integer.parseInt(data2[j + 1]));
                            break;
                        case "macAddress":
                            p.setMacAddress(data2[j + 1]);
                            break;
                    }
                }
            }
            System.out.println("Patient received:");
            System.out.println(p.toString());
            return p;
        } catch (IOException ex) {
            return null;
        } catch (NotBoundException e) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    public static Doctor receiveDoctor(BufferedReader bufferReader) {
        Doctor d = new Doctor();
        try {
            String line = bufferReader.readLine();
            line = line.replace("{", "");
            line = line.replace("Doctor", "");
            line = line.replace("}", "");
            String[] atribute = line.split(",");
            for (int i = 0; i < atribute.length; i++) {
                String[] data2 = atribute[i].split("=");
                for (int j = 0; j < data2.length - 1; j++) {
                    data2[j] = data2[j].replace(" ", "");
                    switch (data2[j]) {
                        case "name":
                            d.setDname(data2[j + 1]);
                            break;
                        case "surname":
                            d.setDsurname(data2[j + 1]);
                            break;
                        case "email":
                            d.setDemail(data2[j + 1]);
                            break;
                        case "id":
                            d.setDoctorId(Integer.parseInt(data2[j + 1]));
                            break;
                    }
                }
            }
            System.out.println("Doctor recieved:");
            System.out.println(d.toString());
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return d;
    }

    public static Signal receiveSignal(BufferedReader br) {
        Signal s = new Signal();
        try {
            String line = br.readLine();
            line = line.replace("{", "");
            line = line.replace("Signal", "");
            String[] atribute = line.split(",");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < atribute.length; i++) {
                String[] data2 = atribute[i].split("=");
                for (int j = 0; j < data2.length - 1; j++) {
                    data2[j] = data2[j].replace(" ", "");
                    switch (data2[j]) {
                        case "signalId":
                            s.setSignalId(Integer.parseInt(data2[j + 1]));
                            break;
                        case "ECG_values":
                            String[] separatedString = data2[j + 1].split(",");
                            List<Integer> ECG = new ArrayList();
                            for (int k = 0; k < separatedString.length; k++) {
                                ECG.add(k, Integer.parseInt(separatedString[k]));
                            }
                            s.setECG_values(ECG);
                            break;

                        case "EMG_values":
                            separatedString = data2[j + 1].split(",");
                            List<Integer> EMG = new ArrayList();
                            for (int k = 0; k < separatedString.length; k++) {
                                EMG.add(k, Integer.parseInt(separatedString[k]));
                            }
                            s.setEMG_values(EMG);
                            break;
                        case "startDate":
                        try {
                            s.setStartDate(format.parse(data2[j + 1]));
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
        System.out.println(s.toString());
        return s;
    }

    public static User receiveUser(BufferedReader br) {
        User u = new User();
        try {
            String line = br.readLine();
            System.out.println(line);
            line = line.replace("{", "");
            line = line.replace("User", "");
            line = line.replace("}", "");
            String[] atribute = line.split(",");
            for (int i = 0; i < atribute.length; i++) {
                String[] data2 = atribute[i].split("=");
                for (int j = 0; j < data2.length - 1; j++) {
                    data2[j] = data2[j].replace(" ", "");
                    switch (data2[j]) {
                        case "username":
                            u.setUsername(data2[j + 1]);
                            break;
                        case "password":
                            u.setPassword(data2[j + 1]);
                            break;
                        case "role":
                            u.setRole(Integer.parseInt(data2[j + 1]));
                            break;
                        case "userId":
                            u.setUserId(Integer.parseInt(data2[j + 1]));
                            break;
                    }
                }
            }
            System.out.println(u.toString());
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public static void recordSignal(Patient p, PrintWriter pw) {
        Frame[] frame;
        BITalino bitalino = null;
        Signal s = new Signal();
        ArrayList<Integer> ecg_vals = new ArrayList<Integer>();
        ArrayList<Integer> emg_vals = new ArrayList<Integer>();
        try {
            bitalino = new BITalino();
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println(devices);

            String macAddress = p.getMacAddress();

            bitalino.open(macAddress, 100);

            int[] channelsToAcquire = {1, 2}; //for EMG and ECG
            bitalino.start(channelsToAcquire);

            //Read in total 10000000 times --> por que elegimos este num
            //Each time read a block of 10 samples to make the trials easier, but we plan to change it
            int block_size = 16;

            // Start loop to calculate time to send signal TODO 
            for (int j = 0; j < 750; j++) {
                frame = bitalino.read(block_size);

                for (int i = 0; i < frame.length; i++) {
                    //pw.println(frame[i].analog[0]);
                    System.out.println(frame[i].analog[0]);
                    ecg_vals.add(frame[i].analog[0]);
                }


                for (int a = 0; a < frame.length; a++) {
                    System.out.println(frame[a].analog[1]);
                    emg_vals.add(frame[a].analog[1]);
                }

            }
            bitalino.stop();
            pw.println(ecg_vals.size());
            for (int k = 0; k < ecg_vals.size(); k++) {
                pw.println(ecg_vals.get(k));
            }
            pw.println(emg_vals.size());
            for (int m = 0; m < emg_vals.size(); m++) {
                pw.println(emg_vals.get(m));
            }

            System.out.println("Ok");
        } catch (BITalinoException ex) {
            Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (bitalino != null) {
                    bitalino.close();
                }
            } catch (BITalinoException ex) {
                Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static List<String> ShowSignals(BufferedReader bf, PrintWriter pw) {
        try {
            List<String> filenames = new ArrayList();

            int size = Integer.parseInt(bf.readLine());  
            for (int i = 0; i < size; i++) {
                filenames.add(bf.readLine());
            }
            return filenames;
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<String> receivePatientList(BufferedReader bf) {
        List<String> patientList = new ArrayList();
        boolean stop = true;
        try {
            while (stop) {
                String line = bf.readLine();
                if (!line.equalsIgnoreCase("End of list")) {
                    stop = true;
                    System.out.println(line);
                    patientList.add(line);
                } else {
                    stop = false;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return patientList;
    }

    public static void exitFromServer(PrintWriter pw, BufferedReader br, InputStream inputStream, OutputStream outputStream, Socket socket) {
        pw.close();
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

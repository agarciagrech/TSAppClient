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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import pojos.Signal;

/**
 *
 * @author agarc
 */
public class Menu {
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
    
}

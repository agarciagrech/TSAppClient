/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Signal {
    private Integer signalId;
    private List<Integer> ECG_values; 
    private List<Integer> EMG_values;
    private Date startDate;
    private String ECGFilename;
    private String EMGFilename;

    public Integer getSignalId() {
        return signalId;
    }

    public Signal() {
    }
    
    public List<Integer> getECG_values() {
        return ECG_values;
    }

    public List<Integer> getEMG_values() {
        return EMG_values;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setSignalId(Integer signalId) {
        this.signalId = signalId;
    }

    public void setECG_values(List<Integer> ECG_values) {
        this.ECG_values = ECG_values;
    }

    public void setEMG_values(List<Integer> EMG_values) {
        this.EMG_values = EMG_values;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public void CreateECGFilename (String patientName){
        Calendar c = Calendar.getInstance();
        String day=Integer.toString(c.get(Calendar.DATE));
        String month=Integer.toString(c.get(Calendar.MONTH));
        String year=Integer.toString(c.get(Calendar.YEAR));
        String hour = Integer.toString(c.get(Calendar.HOUR));
        String minute = Integer.toString(c.get(Calendar.MINUTE));
        String second = Integer.toString(c.get(Calendar.SECOND));
        String millisecond = Integer.toString(c.get(Calendar.MILLISECOND));
        this.ECGFilename=patientName+"ECG"+day+month+year+"_"+hour+minute+second+millisecond+".txt";     
    }
    
    public void CreateEMGFilename (String patientName){
        Calendar c = Calendar.getInstance();
        String day=Integer.toString(c.get(Calendar.DATE));
        String month=Integer.toString(c.get(Calendar.MONTH));
        String year=Integer.toString(c.get(Calendar.YEAR));
        String hour = Integer.toString(c.get(Calendar.HOUR));
        String minute = Integer.toString(c.get(Calendar.MINUTE));
        String second = Integer.toString(c.get(Calendar.SECOND));
        String millisecond = Integer.toString(c.get(Calendar.MILLISECOND));
        this.EMGFilename=patientName+"EMG"+day+month+year+"_"+hour+minute+second+millisecond+".txt";     
    }
    
    public void StartDate(){
        Calendar c = Calendar.getInstance();
        String day=Integer.toString(c.get(Calendar.DATE));
        String month=Integer.toString(c.get(Calendar.MONTH));
        String year=Integer.toString(c.get(Calendar.YEAR));
        String date =year+"/"+month+"/"+day; 
        this.startDate= new Date (date);
    }
    
    public void StoreECGinFile(String patientName){
       FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            CreateECGFilename(patientName);
            String ruta = "../TSAppClient/"+this.ECGFilename;
            String contenido = ECG_values.toString();
            File file = new File(ruta);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(contenido);
            
        } catch (IOException ex) {
            Logger.getLogger(Signal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Signal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
   }
    
    public void StoreEMGinFile(String patientName){
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            CreateEMGFilename(patientName);
            String ruta = "../PatientTS/"+this.EMGFilename;
            String contenido = EMG_values.toString();
            File file = new File(ruta);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(contenido);
            
        } catch (IOException ex) {
            Logger.getLogger(Signal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Signal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
   }
    
    
    @Override
    public String toString() {
        
        return "Signal{" + "signalId=" + signalId + ", ECG_values=" + ECG_values + ", EMG_values=" + EMG_values + ", startDate=" + startDate + '}';
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import java.util.Date;

/**
 *
 * @author agarc
 */
public class Signal {
    private Integer signalId;
    private int[] ECG_values; 
    private int[] EMG_values;
    private Date startDate;
    private String sname;

    public Integer getSignalId() {
        return signalId;
    }

    public Signal() {
    }
    
    

    public int[] getECG_values() {
        return ECG_values;
    }

    public int[] getEMG_values() {
        return EMG_values;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getSname() {
        return sname;
    }

    public void setSignalId(Integer signalId) {
        this.signalId = signalId;
    }

    public void setECG_values(int[] ECG_values) {
        this.ECG_values = ECG_values;
    }

    public void setEMG_values(int[] EMG_values) {
        this.EMG_values = EMG_values;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }
    
    
    @Override
    public String toString() {
        System.out.println("ECG");
        for (int i=0; i<ECG_values.length; i++){
            System.out.println(ECG_values[i]);
        } 
        System.out.println("EMG");
        for (int i=0; i<EMG_values.length; i++){
            System.out.println(EMG_values[i]);
        } 
        return "Signal{" + "signalId=" + signalId + ", ECG_values=" + ECG_values + ", EMG_values=" + EMG_values + ", startDate=" + startDate + ", sname=" + sname + '}';
    }
    
}

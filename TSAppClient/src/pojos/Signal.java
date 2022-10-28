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
}

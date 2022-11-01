/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author agarc
 */

public class Patient implements Serializable{
    private static final long serialVersionUID = -1156840724257282729L;
    
    private Integer medical_card_number;
    private String name;
    private String surname;
    private Date dob;
    private String address;
    private String email;
    private String diagnosis;
    private String allergies;
    private String gender;
    //private Signal signal;
    private Integer userId;
    private String macAddress; 

    public Patient() {
    }

    
    public Patient(Integer medical_card_number, String name, String surname, Date dob, String address, String email, String diagnosis, String allergies, String gender, Integer userId, String macAddress) {
        this.medical_card_number = medical_card_number;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.address = address;
        this.email = email;
        this.diagnosis = diagnosis;
        this.allergies = allergies;
        this.gender = gender;
        this.userId = userId;
        this.macAddress = macAddress;
    }

    public Integer getMedical_card_number() {
        return medical_card_number;
    }

    public void setMedical_card_number(Integer medical_card_number) {
        this.medical_card_number = medical_card_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
    
    public String formatDate (Date dob){
        SimpleDateFormat  formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(dob);
    }

    @Override
    public String toString() {
        return "Patient{" + "medical_card_number=" + medical_card_number + ", name=" + name + ", surname=" + surname + ", dob=" + formatDate(dob) + ", address=" + address + ", email=" + email + ", diagnosis=" + diagnosis + ", allergies=" + allergies + ", gender=" + gender + ", userId=" + userId + ", macAddress=" + macAddress + '}';
    }
    
    
    
}

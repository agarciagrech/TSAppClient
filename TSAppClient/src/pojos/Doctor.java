/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author agarc
 */
public class Doctor {
    private Integer doctorId; //Unique for each doctor - cannot be repeated for another patient.
    private String dname;
    private String dsurname;
    private String demail;
    private Integer userId;

    public Doctor() {
    }

    public Doctor(Integer doctorId, String dname, String dsurname, String demail, Integer userId) {
        this.doctorId = doctorId;
        this.dname = dname;
        this.dsurname = dsurname;
        this.demail = demail;
        this.userId = userId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDsurname() {
        return dsurname;
    }

    public void setDsurname(String dsurname) {
        this.dsurname = dsurname;
    }

    public String getDemail() {
        return demail;
    }

    public void setDemail(String demail) {
        this.demail = demail;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Doctor{" + "doctorId=" + doctorId + ", dname=" + dname + ", dsurname=" + dsurname + ", demail=" + demail + ", userId=" + userId + '}';
    }
    
    
}

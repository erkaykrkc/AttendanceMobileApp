package com.example.yoklamauygulamasi;

public class StudentItem {
    private int studentID;
    private String studentName;
    private String status;

    private long sID;



    public StudentItem(long sID,int studentID, String studentName) {
        this.sID=sID;
        this.studentID = studentID;
        this.studentName = studentName;
        status="";
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getsID() {
        return sID;
    }

    public void setsID(long sID) {
        this.sID = sID;
    }
}

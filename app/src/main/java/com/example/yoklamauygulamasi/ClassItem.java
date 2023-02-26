package com.example.yoklamauygulamasi;

public class ClassItem {

    private long classID;
    String className;
    String subjectName;

    public ClassItem(long classID, String className, String subjectName) {
        this.classID = classID;
        this.className = className;
        this.subjectName = subjectName;
    }
    public ClassItem(String className, String subjectName) {
        this.className = className;
        this.subjectName = subjectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public long getClassID() {
        return classID;
    }

    public void setClassID(long classID) {
        this.classID = classID;
    }
}

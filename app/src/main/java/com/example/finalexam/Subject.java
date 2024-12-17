package com.example.finalexam;

public class Subject {
    private String subjectId; //in firestore using string
    private String subjectName; //in firestore using string
    private double credits; //in firestore using number

    // Empty constructor required for Firestore
    public Subject() {
    }

    public Subject(String subjectId, String subjectName, double credits) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.credits = credits;
    }


    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }
}

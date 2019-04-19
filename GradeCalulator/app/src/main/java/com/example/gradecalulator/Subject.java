package com.example.gradecalulator;

public class Subject {
    private String subject_detail;
    private int credit;

    public Subject(String subject, int credit){
        this.subject_detail = subject;
        this.credit = credit;
    }

    public String getSubject_detail() {
        return subject_detail;
    }

    public void setSubject_detail(String subject_detail) {
        this.subject_detail = subject_detail;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCredit() {
        return credit;
    }
}

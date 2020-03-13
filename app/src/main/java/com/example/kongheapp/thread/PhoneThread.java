package com.example.kongheapp.thread;

public class PhoneThread extends Thread {
    private String url;
    private String number;
    private PhoneThread(String url,String number){
        this.url=url;
        this.number=number;
    }
    public void run(){

    }
}

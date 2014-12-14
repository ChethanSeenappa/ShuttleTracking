package com.example.techsutra.shuttletracking;

/**
 * Created by TechSutra on 12/13/14.
 */
public class AppController {


    private UploadData connection =new UploadData();

    private String address;
    private int port;
    private String loginMessage;


    public AppController(String address, int port, String loginMessage) {

        this.address = address;
        this.port = port;
        this.loginMessage = loginMessage;
    }

    public void sendData(){
        connection.connect(address,port);
        connection.send(loginMessage);
    }
}

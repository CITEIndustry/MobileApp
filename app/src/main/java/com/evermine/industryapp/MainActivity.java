package com.evermine.industryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText serverInput;
    private EditText userInput;
    private EditText passwordInput;
    private Button send;
    private WebSocketClient client;
    private ArrayList<SwitchElm> switchList;
    private ArrayList<Slider> sliderList;
    private ArrayList<Dropdown> dropdownList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //10.0.2.2
        connecta("10.0.2.2","8888");
        serverInput = findViewById(R.id.serverInput);
        userInput = findViewById(R.id.userInput);
        passwordInput = findViewById(R.id.passwordInput);
        switchList = new ArrayList<SwitchElm>();
        //switchList.add(new SwitchElm(1,"on"));
        sliderList = new ArrayList<Slider>();
        //sliderList.add(new Slider(1,4.5f,0,100,100));
        dropdownList = new ArrayList<Dropdown>();
        send= findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(String.valueOf(userInput.getText()),String.valueOf(passwordInput.getText()));
                sendUser(user);
                /*
                Intent intent = new Intent(MainActivity.this, Manage.class);
                intent.putExtra("switch", switchList);
                intent.putExtra("slider", sliderList);
                intent.putExtra("dropdown", dropdownList);
                startActivity(intent);

                 */
            }
        });

    }

    public void connecta (String host,String port) {
        try {
            client = new WebSocketClient(new URI("ws://" + host + ":" + port), (Draft) new Draft_6455()) {
                @Override
                public void onMessage(String message) { onMessageListener(message); }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to: " + getURI());
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from: " + getURI());
                }

                @Override
                public void onError(Exception ex) { ex.printStackTrace(); }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error: " + host + " no és una direcció URI de WebSocket vàlida");
        }
    }

    protected void onMessageListener (String message) {
        String data[] = message.split(";;");
        if (message.equals("message::OK")){
            System.out.println("Va bien");
            send("getComponents");
        }
        else if(message.equals("Send")){
            Intent intent = new Intent(MainActivity.this, Manage.class);
            intent.putExtra("switch", switchList);
            intent.putExtra("slider", sliderList);
            intent.putExtra("dropdown", dropdownList);
            startActivity(intent);
        }
        else{
            for(String value:data){
                String values[] = value.split("::");
                if(values[0].equals("switch")){
                    addSwitch(values);
                }
                else if(values[0].equals("slider")){
                    addSlider(values);
                }
                else if(values[0].equals("dropdown")){
                    addDropdown(values);
                    System.out.println("add dropdown");
                }

            }
        }
    }
    protected void send (String message) {
        //String missatge = "si";
        try {
            client.send(message);
        } catch (WebsocketNotConnectedException e) {
            System.out.println("Connexió perduda ...");
            connecta("192.168.16.209","8888");
        }
    }

    private void  sendUser(User user){
        client.send("User::"+user.getName()+"::"+user.getPassword());
    }

    public static byte[] objToBytes (Object obj) {
        byte[] result = null;
        try {
            // Transforma l'objecte a bytes[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            result = bos.toByteArray();
        } catch (IOException e) { e.printStackTrace(); }
        return result;
    }
    public void addSwitch(String[] values){
        switchList.add(new SwitchElm(Integer.parseInt(values[1]),values[2]));
    }
    public void addSlider(String[] values){
        sliderList.add(new Slider(Integer.parseInt(values[1]),Float.parseFloat(values[2]),Integer.parseInt(values[3]),Integer.parseInt(values[4]),Float.parseFloat(values[5])));
    }
    public void addDropdown(String[] values){
        String[] options = values[3].split("/");
        Dropdown dp = new Dropdown(Integer.parseInt(values[1]),Integer.parseInt(values[2]),options.length);
        for(int i = 0 ;i<options.length;i++){
            String value[] = options[i].split(":");
            dp.setOption(i,0,value[0]);
            dp.setOption(i,1,value[1]);
        }
        dropdownList.add(dp);
    }
}
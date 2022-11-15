package com.evermine.industryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
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
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private boolean logged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //10.0.2.2
        serverInput = findViewById(R.id.serverInput);
        userInput = findViewById(R.id.userInput);
        passwordInput = findViewById(R.id.passwordInput);
        connecta(String.valueOf(serverInput.getText()),"8888");
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
            }
        });
        serverInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    client.close();
                    connecta(String.valueOf(serverInput.getText()),"8888");
                    return true;
                }
                return false;
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
                    Manage.getInstance().finish();
                    showDialog("Error: The connection with the server has been lost");
                    System.out.println("Disconnected from: " + getURI());
                }

                @Override
                public void onError(Exception ex) { ex.printStackTrace(); }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error: " + host + " is not valid");
        }
    }

    protected void onMessageListener (String message) {
        String data[] = message.split(";;");
        if (message.equals("message::OK")){
            System.out.println("Va bien");
            logged=true;
            send("getComponents");
        }
        else if(message.equals("message::ERROR")){

            showDialog("Error: The username or password is not valid");
        }
        else if(message.equals("message::ERROREMPTY")){
            showDialog("Error: There is no component in the desktop application");
        }
        else if(message.equals("Send")){
            Intent intent = new Intent(MainActivity.this, Manage.class);
            System.out.println("Intent open");
            intent.putExtra("switch", switchList);
            intent.putExtra("slider", sliderList);
            intent.putExtra("dropdown", dropdownList);
            startActivity(intent);
            dropdownList.clear();
            sliderList.clear();
            switchList.clear();
        }
        else if(logged){
            for(String value:data){
                String values[] = value.split("::");
                if(values[0].equals("switch")){
                    addSwitch(values);
                }
                else if(values[0].equals("slider")){
                    addSlider(values);
                    System.out.println("slideeeeer");
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
            System.out.println("Error to connect ...");
            //connecta(String.valueOf(serverInput.getText()),"8888");
        }
    }

    private void  sendUser(User user){
        try{
            client.send("User::"+user.getName()+"::"+user.getPassword());
        }catch(WebsocketNotConnectedException e){
            showDialog("Error: Could not establish connection to the server, please check your server ip");
        }catch (Exception e){
            showDialog("Error: Unknown error");
        }
        
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
        for (String str:values){
            System.out.println(str);
        }
        String[] options = values[3].split("/");
        Dropdown dp = new Dropdown(Integer.parseInt(values[1]),Integer.parseInt(values[2]),options.length);
        for(int i = 0 ;i<options.length;i++){
            String value[] = options[i].split(":");
            dp.setOption(i,0,value[0]);
            dp.setOption(i,1,value[1]);
        }
        dropdownList.add(dp);
    }
    public void showDialog(String message) {
        //handler.removeCallbacks(runnable);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(message)
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
        });

    }
}
package com.evermine.industryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText serverInput;
    private EditText userInput;
    private EditText passwordInput;
    private Button send;
    public static WebSocketClient client;
    private Map<String,Block> blocks;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private boolean logged = false;
    private static WeakReference<Activity> mainActivityRef;
    static MainActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity=this;
        //10.0.2.2
        serverInput = findViewById(R.id.serverInput);
        userInput = findViewById(R.id.userInput);
        passwordInput = findViewById(R.id.passwordInput);
        connecta(String.valueOf(serverInput.getText()),"8888");
        blocks = new HashMap<String, Block>();
        //switchList.add(new SwitchElm(1,"on"));
        //sliderList.add(new Slider(1,4.5f,0,100,100));
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
                    //Manage ma = (Manage) mainActivityRef.get();
                    //ma.test();
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
            //sensorList.add(new Sensor(1,"Cº",5,10));
            intent.putExtra("blocks", (Serializable) blocks);
            startActivity(intent);
            blocks.clear();
        }else if(data[0].equals("change")){

            String values[] = data[1].split("::");
            if(values[0].equals("switch")){
                System.out.println("snfsbfibou "+ values[1]+ values[1]+values[2]+values[3]);
                Manage.getInstance().updateSwitch(values[1],Integer.parseInt(values[2]),values[3]);
            }else if(values[0].equals("slider")){
                Manage.getInstance().updateSlider(values[1],Integer.parseInt(values[2]),Integer.parseInt(values[3]));
            }else if(values[0].equals("dropdown")){
                Manage.getInstance().updateDropdown(values[1],Integer.parseInt(values[2]),Integer.parseInt(values[3]));
            }
        }
        else if(logged){
            for(String value:data){
                String values[] = value.split("::");
                if(values[0].equals("block")){
                    addBlock(values[1]);
                }
                if(values[0].equals("switch")){
                    addSwitch(values);
                }
                else if(values[0].equals("slider")){
                    addSlider(values);
                    System.out.println("slideeeeer");
                }
                else if(values[0].equals("dropdown")){
                    addDropdown(values);
                }
                else if(values[0].equals("sensor")){
                    System.out.println("sensorrrrr");
                    addSensor(values);
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

    private void sendUser(User user){
        Intent intent = new Intent(MainActivity.this, Manage.class);
        //addFakeData();
        //sensorList.add(new Sensor(1,"Cº",5,10));
        //intent.putExtra("blocks", (Serializable) blocks);
        //startActivity(intent);
        //blocks.clear();

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

    public void  addBlock(String values){
        blocks.put(values,new Block(values));
    }
    public void addSwitch(String[] values){
        System.out.println("swiiitch"+values);
        blocks.get(values[1]).addSwitch(values);
    }
    public void addSlider(String[] values){
        blocks.get(values[1]).addSlider(values);
    }
    public void addSensor(String[] values){
        blocks.get(values[1]).addSensor(values);
    }
    public void addDropdown(String[] values){
        blocks.get(values[1]).addDropdown(values);
    }

    public void onchange(String component,String blockID, int id, int value){
        if(component.equals("switch")){
            if(value==1){
                send("change;;switch::"+blockID+"::"+id+"::on");
            }else{
                send("change;;switch::"+blockID+"::"+id+"::off");
            }
        }else if(component.equals("slider")){
            send("change;;slider::"+blockID+"::"+id+"::"+value);
        }
        else if(component.equals("dropdown")){
            send("change;;dropdown::"+blockID+"::"+id+"::"+value);
        }
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

    public void addFakeData(){
        addBlock("Test");
        //Block1
        System.out.println(blocks.get("Test"));
        addSwitch(new String[]{"","Test","1", "on","TestSwitch"});
        addSwitch(new String[]{"","Test","2", "on","TestSwitch"});
        addSlider(new String[]{"","Test","1","5","10","0","1","TestSlider"});
        addDropdown(new String[]{"","Test","1","0","TestLabel","0:Label1/1:Label2"});
        addSensor(new String[]{"","Test","1","Cº","3","10","6","TestSensor"});
        //Block2
        addBlock("Test2");
        System.out.println(blocks.get("Test2"));
        addSwitch(new String[]{"","Test2","1", "on","TestSwitch"});
        addSlider(new String[]{"","Test2","1","5","10","0","1","TestSlider"});
        addDropdown(new String[]{"","Test2","1","0","TestLabel","0:Label1/1:Label2"});
        addSensor(new String[]{"","Test2","1","Cº","3","10","6","TestSensor"});
    }

    public static MainActivity getInstance(){
        return mActivity;
    }
}
package com.evermine.industryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private EditText serverInput;
    private EditText userInput;
    private EditText passwordInput;
    private Button send;
    private WebSocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //10.0.2.2
        connecta("192.168.16.209","8888");
        serverInput = findViewById(R.id.serverInput);
        userInput = findViewById(R.id.serverInput);
        passwordInput = findViewById(R.id.passwordInput);
        send= findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                envia("si");
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
        //textView.append(message + "\n");
    }
    protected void envia (String message) {
        //String missatge = "si";
        try {
            client.send(message);
        } catch (WebsocketNotConnectedException e) {
            System.out.println("Connexió perduda ...");
            connecta("192.168.16.209","8888");
        }
    }
}
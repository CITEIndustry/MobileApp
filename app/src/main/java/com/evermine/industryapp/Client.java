package com.evermine.industryapp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends org.java_websocket.client.WebSocketClient {


    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int port = 8888;
        String host = "localhost";
        String location = "ws://" + host + ":" + port;

    }

    public Client (URI uri, Draft draft) {
        super (uri, draft);
    }
    public static void saluda(ArrayList<String> credencials,Client client){
        int random = (int) (Math.random()*(4-1));
        client.send(credencials.get(0));
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
    public static Object bytesToObject (ByteBuffer arr) {
        Object result = null;
        try {
            // Transforma el ByteButter en byte[]
            byte[] bytesArray = new byte[arr.remaining()];
            arr.get(bytesArray, 0, bytesArray.length);

            // Transforma l'array de bytes en objecte
            ByteArrayInputStream in = new ByteArrayInputStream(bytesArray);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();

        } catch (ClassNotFoundException e) { e.printStackTrace();
        } catch (UnsupportedEncodingException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
        return result;
    }
    public static void enviaCredencials(ArrayList<String> saluts, Client client){
        client.send(saluts.get(0));
    }
    @Override
    public void onMessage(String message) {
        System.out.println("Rebut confirmacio: " + message);
    }
    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("T'has connectat a: " + getURI());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("T'has desconnectat de: " + getURI());
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error amb la connexió del socket");
    }

    static public Client connecta(String location) {
        Client client = null;

        try {
            client = new Client(new URI(location), (Draft) new Draft_6455());
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error: " + location + " no és una direcció URI de WebSocket vàlida");
        }

        return client;
    }
}

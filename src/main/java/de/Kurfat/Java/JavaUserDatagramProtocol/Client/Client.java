package de.Kurfat.Java.JavaUserDatagramProtocol.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public abstract class Client {
    
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public void setGson(Gson gson) {
        GSON = gson;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DatagramSocket socket;
    private Thread thread;

    private InetAddress server_address;
    private int server_port;

    public Client(InetAddress server_address, int server_port) throws UnknownHostException, IOException {
        this.server_address = server_address;
        this.server_port = server_port;
        this.socket = new DatagramSocket();
        this.thread = new Thread(() -> {
            while (Thread.currentThread().isInterrupted() == false) {
                try {
                    byte[] buffer = new byte[1000];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    byte[] in = new byte[packet.getLength()];
                    System.arraycopy(buffer, 0, in, 0, in.length);
                    onInput(JsonParser.parseString(new String(in)));
                } catch (Exception e) {
                    onException(e);
                }
            }
        });
        this.thread.start();
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }
    public int getPort() {
        return socket.getPort();
    }
    public boolean isClosed() {
        return socket.isClosed();
    }

    public void close() {
        this.thread.interrupt();
        this.socket.disconnect();
        this.socket.close();
    }

    public InetAddress getServerAddress() {
        return server_address;
    }
    public int getServerPort() {
        return server_port;
    }

    public void send(Object object) throws IOException {
        byte[] buffer = GSON.toJson(object).getBytes();
        socket.send(new DatagramPacket(buffer, buffer.length, server_address, server_port));
    }
    
    abstract public void onInput(JsonElement input);
    abstract public void onException(Exception exception);
}

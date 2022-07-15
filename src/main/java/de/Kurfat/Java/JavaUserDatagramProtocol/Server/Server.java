package de.Kurfat.Java.JavaUserDatagramProtocol.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public abstract class Server {
    
    protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public void setGson(Gson gson) {
        GSON = gson;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DatagramSocket socket;
    private Thread thread;

    public Server(int port, int buffer_size) throws JsonIOException, JsonSyntaxException, IOException {
        this.socket = new DatagramSocket(port);
        this.thread = new Thread(() -> {
            while (Thread.currentThread().isInterrupted() == false) {
                try {
                    byte[] buffer = new byte[buffer_size];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    byte[] in = new byte[packet.getLength()];
                    System.arraycopy(buffer, 0, in, 0, in.length);
                    onInput(new Connection(packet.getAddress(), packet.getPort()) {
                        public DatagramSocket getSocket() {
                            return socket;
                        };
                    }, JsonParser.parseString(new String(in)));
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

    abstract public void onInput(Connection connection, JsonElement input);
    abstract public void onException(Exception exception);

}

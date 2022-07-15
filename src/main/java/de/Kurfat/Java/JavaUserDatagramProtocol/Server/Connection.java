package de.Kurfat.Java.JavaUserDatagramProtocol.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class Connection {
    
    private InetAddress address;
    private int port;

    public Connection(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public abstract DatagramSocket getSocket();

    public InetAddress getAddress() {
        return address;
    }
    public int getPort() {
        return port;
    }
    public void send(Object object) throws IOException {
        byte[] message = Server.GSON.toJson(object).getBytes();
        getSocket().send(new DatagramPacket(message, message.length, address, port));
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + port;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Connection other = (Connection) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (port != other.port)
            return false;
        return true;
    }
}

package de.Kurfat.Java.JavaUserDatagramProtocol.Protocol;

import com.google.gson.JsonElement;

public class Packet {
    
    private String packetID;
    private long created;
    private JsonElement data;

    public Packet(String packetID, JsonElement data) {
        this.packetID = packetID;
        this.created = System.currentTimeMillis();
        this.data = data;
    }
    public Packet(String packetID) {
        this(packetID, null);
    }
    
    public String getPacketID() {
        return packetID;
    }
    public long getCreated() {
        return created;
    }
    public JsonElement getData() {
        return data;
    }

}

package server.tools;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Iterator;

public class PacketsSafe {
    private static HashMap<String, DatagramPacket> packets = new HashMap<>();

    public static void put(String username, DatagramPacket packet){
        packets.put(username, packet);
    }
    public static DatagramPacket get(String username){
        return packets.get(username);
    }
}

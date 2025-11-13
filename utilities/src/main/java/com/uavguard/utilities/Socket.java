package com.uavguard.utilities;

import java.net.*;

public class Socket {

    public static void sendPacket(byte[] data, int port) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress addr = InetAddress.getByName("192.168.1.1");
        DatagramPacket pkt = new DatagramPacket(data, data.length, addr, port);
        socket.send(pkt);
        socket.close();
    }
}

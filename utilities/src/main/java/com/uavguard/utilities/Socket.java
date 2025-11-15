package com.uavguard.utilities;

import java.net.*;
import java.util.function.Consumer;

public class Socket {

    private boolean running = false;

    public void sendPacket(byte[] data, String ip, int port) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();

        DatagramSocket socket = new DatagramSocket();
        InetAddress addr = InetAddress.getByName(ip);
        DatagramPacket pkt = new DatagramPacket(data, data.length, addr, port);
        socket.send(pkt);
        for (byte b : data) {
            System.out.printf("%02X", b);
        }

        System.out.printf(" to " + ip);
        System.out.println();
        socket.close();
    }

    public void startServer(
        int port,
        String ip,
        byte[] startBytes,
        Consumer<byte[]> callback
    ) throws Exception {
        running = true;
        DatagramSocket socket = new DatagramSocket(port);
        byte[] buf = new byte[1024];

        DatagramPacket out = new DatagramPacket(
            startBytes,
            startBytes.length,
            InetAddress.getByName(ip),
            port
        );
        socket.send(out);

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            byte[] data = new byte[packet.getLength()];
            System.arraycopy(buf, 0, data, 0, packet.getLength());
            callback.accept(data);
        }

        socket.close();
    }

    public void stopServer() {
        running = false;
    }
}

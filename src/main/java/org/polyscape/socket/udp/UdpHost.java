package org.polyscape.socket.udp;

import org.polyscape.socket.Packet;
import org.polyscape.socket.PacketProcessor;
import org.polyscape.socket.Protocol;
import org.polyscape.socket.SocketMode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

public final class UdpHost extends Thread {
    private final PacketProcessor mainProcessor;

    private final byte[] buffer = new byte[256];

    private final DatagramSocket socket;
    public UdpHost(PacketProcessor packetProcessor) throws IOException {
        this.mainProcessor = packetProcessor;
        this.socket = new DatagramSocket(mainProcessor.port);
    }


    @Override
    public void run(){
        while (mainProcessor.running){
            try {
                DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(udpPacket);
                InetAddress address = udpPacket.getAddress();
                int port = udpPacket.getPort();
                //udpPacket = new DatagramPacket(buffer, buffer.length, address, port);

                final String received = new String(udpPacket.getData(), 0, udpPacket.getLength());

                processPacket(received, udpPacket, address, port);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void processPacket(String packetData, DatagramPacket datagramPacket, InetAddress address, int port) throws IOException {
        List<String> data = Arrays.stream(packetData.split("\\|")).toList();

        if(data.isEmpty()) return;

        final String head = data.get(0);

        for (Packet<?> packet: mainProcessor.packets) {
            if(packet.protocol != Protocol.UDP && packet.mode != SocketMode.Host) continue;
            boolean runArgument = packet.runArgument.test(head);
            if(runArgument){
                packet.executeOn(data, datagramPacket);
                byte[] bt = packet.compilePacket().getBytes();
                socket.send(new DatagramPacket(bt, bt.length, address, port));
            }
        }
    }

    public void close(){
        socket.close();
    }
}

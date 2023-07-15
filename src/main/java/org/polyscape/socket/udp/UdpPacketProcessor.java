package org.polyscape.socket.udp;

import org.polyscape.socket.Packet;
import org.polyscape.socket.PacketProcessor;
import org.polyscape.socket.Protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

public final class UdpPacketProcessor {

    private final PacketProcessor mainProcessor;

    public UdpPacketProcessor(PacketProcessor packetProcessor){
        this.mainProcessor = packetProcessor;
    }

    public void sendPacket(Packet<?> packet) throws IOException {
        assert packet.protocol == Protocol.UDP;

        final DatagramSocket socket = new DatagramSocket();
        byte[] bytes = packet.compilePacket().getBytes();

        DatagramPacket udpPacket = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(mainProcessor.address), mainProcessor.port);

        socket.send(udpPacket);
        byte[] buffer = new byte[512];
        DatagramPacket udpPacketR = new DatagramPacket(buffer, buffer.length);

        socket.receive(udpPacketR);

        String data = new String(udpPacketR.getData(), 0, udpPacketR.getLength());

        processPacket(data);

    }


    private void processPacket(String packetData){
        List<String> data = Arrays.stream(packetData.split("\\|")).toList();

        if(data.isEmpty()) return;

        final String head = data.get(0);

        for (Packet<?> packet: mainProcessor.packets) {
            if(packet.protocol != Protocol.UDP) continue;
            boolean runArgument = packet.runArgument.test(head);
            if(runArgument){
                packet.executeOn(data);
            }

        }

    }

}

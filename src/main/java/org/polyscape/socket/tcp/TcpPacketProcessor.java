package org.polyscape.socket.tcp;

import org.polyscape.socket.Packet;
import org.polyscape.socket.PacketProcessor;
import org.polyscape.socket.Protocol;
import org.polyscape.socket.SocketMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class TcpPacketProcessor {

    private final PacketProcessor mainProcessor;

    public TcpPacketProcessor(PacketProcessor packetProcessor){
        this.mainProcessor = packetProcessor;
    }


    public void sendPacket(Packet<?> packet) throws IOException {
        assert packet.protocol == Protocol.TCP;

        final Socket socket = connect();

        sendData(socket, packet.compilePacket());

        String data = Objects.requireNonNull(receiveData(socket)).readLine();

        processPacket(data);

        socket.close();
    }


    private void sendData(final Socket socket, final String data) throws IOException {
        final OutputStream stream = socket.getOutputStream();

        byte[] toSendBytes = data.getBytes();
        int toSendLen = toSendBytes.length;
        byte[] toSendLenBytes = new byte[4];
        toSendLenBytes[0] = (byte)(toSendLen & 0xff);
        toSendLenBytes[1] = (byte)((toSendLen >> 8) & 0xff);
        toSendLenBytes[2] = (byte)((toSendLen >> 16) & 0xff);
        toSendLenBytes[3] = (byte)((toSendLen >> 24) & 0xff);

        stream.write(toSendLenBytes);
        stream.write(toSendBytes);
    }

    private Socket connect() throws IOException {
        return new Socket(mainProcessor.address, mainProcessor.port);
    }

    private BufferedReader receiveData(final Socket socket) {
        try {
            return new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void processPacket(String packetData){
         List<String> data = Arrays.stream(packetData.split("\\|")).toList();

         if(data.isEmpty()) return;

         final String head = data.get(0);

        for (Packet<?> packet: mainProcessor.packets) {
            if(packet.protocol != Protocol.TCP && packet.mode != SocketMode.Client) continue;
            boolean runArgument = packet.runArgument.test(head);
            if(runArgument){
                packet.executeOn(data);
            }
        }
    }

}

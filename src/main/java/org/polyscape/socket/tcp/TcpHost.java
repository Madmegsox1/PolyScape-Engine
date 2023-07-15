package org.polyscape.socket.tcp;

import org.polyscape.socket.Packet;
import org.polyscape.socket.PacketProcessor;
import org.polyscape.socket.Protocol;
import org.polyscape.socket.SocketMode;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class TcpHost extends Thread {
    private final PacketProcessor mainProcessor;
    private final ServerSocket serverSocket;

    public TcpHost(PacketProcessor packetProcessor) throws IOException {
        this.mainProcessor = packetProcessor;
        this.serverSocket = new ServerSocket(mainProcessor.port);
    }


    @Override
    public void run() {
        while (mainProcessor.running) {
            try {
                final Socket socket = receiveSocket();

                assert socket != null;

                String fromIp = socket.getInetAddress().getHostAddress();

                if (mainProcessor.getWhitelistAddresses().size() > 0) {
                    if (!mainProcessor.getWhitelistAddresses().contains(fromIp)) {
                        sendData(socket, "Error-005");
                    }
                }

                final String data = Objects.requireNonNull(receiveData(socket));

                processPacket(data, socket);

                socket.close();

            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processPacket(String packetData, Socket socket) throws IOException, NoSuchAlgorithmException {

        List<String> data = Arrays.stream(packetData.split("\\|")).toList();

        if (data.isEmpty()) return;

        final String head = data.get(0);
        boolean sentFlag = false;
        for (Packet<?> packet : mainProcessor.packets) {
            if (packet.protocol != Protocol.TCP && packet.mode != SocketMode.Host) continue;
            boolean runArgument = packet.runArgument.test(head);
            if (runArgument) {
                sentFlag = true;
                packet.executeOn(data, socket);
                sendData(socket, packet.compilePacket());
            }
        }

        if(!sentFlag){
            sendData(socket, "Error-003");
        }

    }


    private String receiveData(final Socket socket) {

        try {
            InputStream is = socket.getInputStream();
            String s = readStreamBytes(is);

            if (s.isEmpty() || s.isBlank()) {
                return new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            } else {
                return s;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Socket receiveSocket() {
        try {
            return serverSocket.accept();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String readStreamBytes(InputStream is) throws IOException {
        byte[] lenBytes = new byte[4];
        is.read(lenBytes, 0, 4);
        int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
        byte[] receivedBytes = new byte[len];
        is.read(receivedBytes, 0, len);

        return new String(receivedBytes, 0, len);
    }

    private void sendData(final Socket socket, final String data) throws IOException {
        final OutputStream stream = socket.getOutputStream();

        byte[] toSendBytes = data.getBytes();
        int toSendLen = toSendBytes.length;
        byte[] toSendLenBytes = new byte[4];
        toSendLenBytes[0] = (byte) (toSendLen & 0xff);
        toSendLenBytes[1] = (byte) ((toSendLen >> 8) & 0xff);
        toSendLenBytes[2] = (byte) ((toSendLen >> 16) & 0xff);
        toSendLenBytes[3] = (byte) ((toSendLen >> 24) & 0xff);

        stream.write(toSendLenBytes);
        stream.write(toSendBytes);
    }


    public void close() throws IOException {
        serverSocket.close();
    }

}

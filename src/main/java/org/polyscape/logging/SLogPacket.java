package org.polyscape.logging;
import org.polyscape.socket.Packet;
import org.polyscape.socket.Protocol;
import org.polyscape.socket.SocketMode;

import java.net.Socket;
import java.util.List;

public final class SLogPacket extends Packet<String> {

    private Logger instance;

    public SLogPacket(Logger instance) {
        super("SLog", Protocol.TCP, SocketMode.Host);
        this.runArgument = n -> n.equals("CLog");
        this.instance = instance;
    }

    @Override
    public void executeOn(List<String> packetData, Socket socket) {
        StringBuilder data = new StringBuilder();
        for (String d : instance.toStream) {
            data.append(d).append('|');
        }

        this.setPacketData(data.toString());
    }
}

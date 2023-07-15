package org.polyscape.socket;

import java.net.DatagramPacket;
import java.net.Socket;
import java.util.List;
import java.util.function.Predicate;

public abstract class Packet<T> {

    public final Protocol protocol;

    public final SocketMode mode;

    public volatile T packetData;

    public volatile String packetHead;

    public volatile Predicate<String> runArgument;

    public Packet(String packetHead){
        protocol = Protocol.TCP;
        this.mode = SocketMode.Client;
        this.packetHead = packetHead;
        runArgument = n -> (n.equals(packetHead));
    }

    public Packet(String packetHead, Protocol protocol, SocketMode mode){
        this.protocol = protocol;
        this.packetHead = packetHead;
        this.mode = mode;
        runArgument = n -> (n.equals(packetHead));
    }

    public void executeOn(List<String> packetData, DatagramPacket socket) {}
    public void executeOn(List<String> packetData, Socket socket) {}
    public void executeOn(List<String> packetData) {}

    public void setPacketData(T data){
        packetData = data;
    }

    public String compilePacket(){
        if(packetData.toString().endsWith("|")){
            return packetHead + "|" + packetData.toString().substring(0, packetData.toString().length()-1);
        }

        return packetHead + "|" + packetData.toString();
    }


}

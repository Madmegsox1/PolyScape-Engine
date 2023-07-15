package org.polyscape.socket;


import org.polyscape.socket.tcp.TcpHost;
import org.polyscape.socket.tcp.TcpPacketProcessor;
import org.polyscape.socket.udp.UdpHost;
import org.polyscape.socket.udp.UdpPacketProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public final class PacketProcessor extends Thread {

    public volatile SocketMode mode;
    public volatile String address;
    public ArrayList<Packet<?>> packets;
    public volatile Queue<Packet<?>> packetQueue;
    public volatile int port = 4000;
    public volatile boolean running;

    private TcpPacketProcessor tcpPacketProcessor;
    private UdpPacketProcessor updPacketProcessor;
    private TcpHost tcpHost;
    private UdpHost udpHost;

    private ArrayList<String> whitelistAddresses;


    public PacketProcessor(SocketMode mode, int port){
        init(mode, port);
    }

    public PacketProcessor(SocketMode mode) {
        init(mode, this.port);
    }

    private void init(SocketMode mode, int port){
        try {
            this.port = port;
            this.mode = mode;
            this.packets = new ArrayList<>();
            this.packetQueue = new LinkedList<>();
            this.whitelistAddresses = new ArrayList<>();
            this.running = false;
            this.tcpPacketProcessor = new TcpPacketProcessor(this);
            this.updPacketProcessor = new UdpPacketProcessor(this);
            if(mode.equals(SocketMode.Host)) {
                this.tcpHost = new TcpHost(this);
                this.udpHost = new UdpHost(this);
            }

            tcpPipeline();
            udpPipeline();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void udpPipeline() {
        if(mode.equals(SocketMode.Host)){
            this.running = true;
            udpHost.start();
        }else if(mode.equals(SocketMode.Client) && !running){
            this.running = true;
            Thread thread = new Thread(this, "Client-TCP-UDP-Thread");
            thread.start();
        }
    }


    private void tcpPipeline() {
        if (mode.equals(SocketMode.Host)) {
            this.running = true;
            tcpHost.start();
        } else if (mode.equals(SocketMode.Client) && !running) {
            this.running = true;
            Thread thread = new Thread(this, "Client-TCP-UDP-Thread");
            thread.start();
        }
    }

    @Override
    public void run() {
        if (mode.equals(SocketMode.Client)) {
            while (running) {
                while (!packetQueue.isEmpty()) {
                    try {
                        Packet<?> p = packetQueue.remove();
                        sendPacket(p);
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                    }
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }


    private void sendPacket(Packet<?> packet) throws IOException {
        if (packet.protocol == Protocol.TCP) {
            tcpPacketProcessor.sendPacket(packet);
        } else if (packet.protocol == Protocol.UDP) {
            updPacketProcessor.sendPacket(packet);
        }
    }

    public void addWhitelistAddresses(String... address){
        whitelistAddresses.addAll(Arrays.stream(address).toList());
    }

    public ArrayList<String> getWhitelistAddresses() {
        return whitelistAddresses;
    }
}

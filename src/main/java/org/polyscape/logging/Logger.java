package org.polyscape.logging;


import org.polyscape.socket.PacketProcessor;
import org.polyscape.socket.SocketMode;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class Logger {

    private final PrintStream printStream;

    public boolean streamToInet;

    public ArrayList<String> toStream;

    private PacketProcessor processor;

    private DateFormat dateFormat;

    public Logger() {
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        printStream = System.out;
        streamToInet = false;
    }

    public Logger(PrintStream printStream) {
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        this.printStream = printStream;
        streamToInet = false;
    }


    public void info(Object text) {

        String d = dateFormat.format(new Date());
        log( "[" + d + "] "+ "[INFO] " + text);
    }

    public void error(Object text) {
        String d = dateFormat.format(new Date());
        log("[" + d + "] "+ "[ERROR] " + text);
    }

    public void log(Object text) {
        printStream.print(String.valueOf(text) + '\n');

        if (streamToInet) {
            if (toStream.size() > 150) {
                toStream = new ArrayList<>(toStream.subList(toStream.size() - 150, toStream.size()));
            }

            toStream.add(String.valueOf(text));
        }
    }


    public void initStream(int port) {
        streamToInet = true;
        toStream = new ArrayList<>();
        processor = new PacketProcessor(SocketMode.Host, port);
        processor.packets.add(new SLogPacket(this));
    }

    public void addWhitelistAddress(String... address) {
        processor.addWhitelistAddresses(address);
    }
}

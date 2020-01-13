package main.java.interfaces;

import java.io.IOException;

public interface Firewall {
    public boolean accept_packet(String direction, String protocol, Integer port, String ip_address) throws IOException;
}

package main.java.interfaces;

public interface Firewall {
    public boolean accept_packet(String direction, String protocol, Integer port, String ip_address);
}

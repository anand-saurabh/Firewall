package main.java.interfaces;

import main.java.rules.FirewallRules;

import java.io.IOException;

public interface Firewall {
    public boolean accept_packet(String direction, String protocol, Integer port, String ip_address) throws IOException;
    public void formRules() throws IOException;
    public void sortAllFiles() throws IOException, ClassNotFoundException;
    public void setRules(FirewallRules firewallRules);
    public void deleteFiles();
}

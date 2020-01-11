package main.java.impl;

import main.java.interfaces.Firewall;
import main.java.rules.FirewallRules;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FirewallImpl implements Firewall {

    BufferedReader br;
    FirewallRules rules;
    public FirewallImpl(String path) throws FileNotFoundException {
        File f = new File(path);
        br = new BufferedReader(new FileReader(f));
    }
    public FirewallImpl(FirewallRules firewallRules)
    {
        this.rules = firewallRules;
    }
    public boolean accept_packet(String direction, String protocol, Integer port, String ip_address)
    {
        return rules.validateDirectionAndProtocol(direction, protocol)
                && rules.valdatePortAndIpAddress(port, ip_address);
    }



}

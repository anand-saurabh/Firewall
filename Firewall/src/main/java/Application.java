package main.java;

import main.java.impl.FirewallImpl;
import main.java.interfaces.Firewall;
import main.java.rules.FirewallRules;

import java.io.File;
import java.io.IOException;

public class Application {

    public static void main(String [] args) throws IOException, ClassNotFoundException {
        String path = new File("").getAbsolutePath();
        Firewall firewall
                = new FirewallImpl(path + "/src/main/resources/test.csv");
        firewall.formRules();
        firewall.sortAllFiles();

        FirewallRules firewallRules = new FirewallRules();
        firewall.setRules(firewallRules);

//        boolean ans = firewall.accept_packet("outbound", "tcp", 12000, "192.169.9.11");
//        System.out.println("The answer is " + ans);

        firewall.deleteFiles();
    }
}

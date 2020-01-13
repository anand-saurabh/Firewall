package test.java;

import main.java.impl.FirewallImpl;
import main.java.interfaces.Firewall;
import main.java.rules.FirewallRules;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FirewallImplTest {

    Firewall firewall;
    FirewallRules firewallRules;
    final static String rootPath = new File("").getAbsolutePath();

    public FirewallImplTest() throws IOException, ClassNotFoundException {
        firewallRules
                = new FirewallRules();
        firewall = new FirewallImpl(rootPath + "/src/test/resources/test.csv");

        firewall.setRules(firewallRules);
        firewall.formRules();
        firewall.sortAllFiles();
    }

    @Test
    public void shouldReturnFalseForInvalidPort() throws IOException {

        boolean res = firewall.accept_packet("inbound", "tcp", 900000, "255.34.21.12");
        assertEquals(false, res);
    }

    @Test
    public void shouldReturnTrueForIpAddress() throws IOException {
        boolean res = firewall.accept_packet("inbound", "tcp", 87, "255.34.21.123");
        assertEquals(false, res);
    }

    @Test
    public void shouldReturnFalseForInvalidDirection() throws IOException {
        boolean res = firewall.accept_packet("out", "tcp", 87, "255.34.21.12");
        assertEquals(false, res);
    }

    @Test
    public void shouldReturnFalseForInvalidProtocol() throws IOException {
        boolean res = firewall.accept_packet("inbound", "ftp", 32, "255.34.21.12");
        assertEquals(false, res);
    }

    @Test
    public void shouldReturnFalseForInvalidIpAdd() throws IOException {
        boolean res = firewall.accept_packet("inbound", "tcp", 123, "231.34.21.12");
        assertEquals(false, res);
    }


    @Test
    public void shouldReturnFalseForInvalidData() throws IOException, ClassNotFoundException {
        boolean res = firewall.accept_packet("inbound", "tcp", 78, "220.170.10.11");
        assertEquals(false, res);
    }

    @Test
    public void shouldReturnFalseForInvalidPortNumber() throws IOException, ClassNotFoundException {

        boolean res = firewall.accept_packet("inbound", "tcp", 60, "192.170.10.11");
        assertEquals(false, res);
    }

    @Test
    public void shouldReturnTrueForValidIp() throws IOException, ClassNotFoundException {
        boolean res = firewall.accept_packet("inbound", "tcp", 78, "192.178.10.1");
        assertEquals(true, res);
    }


}
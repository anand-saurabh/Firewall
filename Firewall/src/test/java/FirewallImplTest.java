package test.java;

import main.java.impl.FirewallImpl;
import main.java.interfaces.Firewall;
import main.java.rules.FirewallRules;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FirewallImplTest {

    Firewall firewall;

    public FirewallImplTest() {
        FirewallRules firewallRules
                = new FirewallRules();
        firewall = new FirewallImpl(firewallRules);
    }

    @Test
    public void shouldReturnFalseForInvalidPort()
    {

        boolean res = firewall.accept_packet("inbound", "tcp", 900000, "255.34.21.12");
        assertEquals(false, res);
    }

    @Test
    public void shouldReturnFalseForInvalidIp()
    {
        boolean res = firewall.accept_packet("inbound", "tcp", 87, "255.34.21.12321");
        assertEquals(false, res);
    }

    @Test
    public void shouldReturnFalseForInvalidDirection()
    {
        boolean res = firewall.accept_packet("out", "tcp", 87, "255.34.21.12");
        assertEquals(false, res);
    }

    @Test
    public void shouldReturnFalseForInvalidProtocol()
    {
        boolean res = firewall.accept_packet("inbound", "ftp", 32, "255.34.21.12");
        assertEquals(false, res);
    }

    @Test
    public void shouldReturntrueForValidData()
    {
        boolean res = firewall.accept_packet("inbound", "tcp", 123, "255.34.21.12");
        assertEquals(true, res);
    }

    @Test
    public void shouldReturnFalseForInvalidIpAdd()
    {
        boolean res = firewall.accept_packet("inbound", "tcp", 123, "255.34.21.12.1");
        assertEquals(false, res);
    }

}
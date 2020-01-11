package main.java.rules;

public class FirewallRules {

    public static final String INBOUND = "inbound";
    public static final String OUTBOUND = "outbound";

    public static final String TCP = "tcp";
    public static final String UDP = "udp";


    public boolean validateDirectionAndProtocol(String direction, String protocol)
    {
        if(!(INBOUND.equals(direction) || OUTBOUND.equals(direction))) {
            return false;
        }
        if(!(UDP.equals(protocol) || TCP.equals(protocol)))
        {
            return false;
        }
        return true;
    }

    public boolean valdatePortAndIpAddress(Integer port, String ip)
    {
        if(!(port >= 1 && port <= 65535))
        {
            return false;
        }
        String[] splittedIp = ip.split("\\.");
        if(splittedIp.length != 4)
            return false;
        int part1 = Integer.valueOf(splittedIp[0]);
        int part2 = Integer.valueOf(splittedIp[1]);

        int part3 = Integer.valueOf(splittedIp[2]);
        int part4 = Integer.valueOf(splittedIp[3]);


        if(!(part1 >= 0 &&  part1 <= 255))
        {
            return false;
        }
        if(!(part2 >= 0 && part2 <= 255))
        {
            return false;
        }
        if(!(part3 >= 0 && part3 <= 255))
        {
            return false;
        }
        if(!(part4 >= 0 && part4 <= 255))
        {
            return false;
        }
        return true;
    }
}

package main.java.rules;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.net.InetAddress;

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

    public boolean validatePortAndIpAddress(Integer port, String ip)
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

    public boolean validateData(FileReader f, Integer port, String ip) throws IOException {
        BufferedReader reader = new BufferedReader(f);
        String temp;

        String prev;
        temp = reader.readLine();
        prev = temp;
        Integer ipAddress = Integer.parseInt(ip.replaceAll("\\.", ""));
        while(temp != null && !temp.isBlank() && Integer.parseInt(temp.split("\\,")[0].split("\\-")[0]) <= port)
        {
            prev = temp;
            temp = reader.readLine();
        }
        if(prev != null && !prev.isBlank()) {

            String [] str = prev.split("\\,");
            String []portRange = str[0].split("\\-");
            String [] ips = str[1].split("\\-");
            if(!(port == Integer.parseInt(portRange[0]) || (portRange[1] != null && ((port == Integer.parseInt(portRange[1]))
                    || (port > Integer.parseInt(portRange[0]) && port < Integer.parseInt(portRange[1]))))))
            {
                return false;
            }
            if(ips.length == 2 && ips[1] != null)
            {

                long ipLo = ipToLong(InetAddress.getByName(ips[0]));
                long ipHi = ipToLong(InetAddress.getByName(ips[1]));
                long ipToTest = ipToLong(InetAddress.getByName(ip));
                return (ipToTest >= ipLo && ipToTest <= ipHi);
            }
            else if (!(ips[0] == ip))
            {
                return false;
            }
        }
        else
        {
            return false;
        }
        return true;
    }

      public long ipToLong(InetAddress ip) {
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }
}

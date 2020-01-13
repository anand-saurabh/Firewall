package main.java.impl;

import com.google.code.externalsorting.csv.CsvExternalSort;
import main.java.interfaces.Firewall;
import main.java.rules.FirewallRules;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class FirewallImpl implements Firewall {

    FileWriter myWriter1;
    FileWriter myWriter2;
    FileWriter myWriter3;
    FileWriter myWriter4;
    File f1;
    File f1_out;
    File f2;
    File f2_out;
    File f3;
    File f3_out;
    File f4;
    File f4_out;

    final static String rootPath = new File("").getAbsolutePath();

    Map<String,List<String>>
            map = new HashMap<String, List<String>>();

    Map<String,List<String>>
            portMap = new HashMap<String, List<String>>();

    BufferedReader br;
    FirewallRules rules;

    public FirewallImpl(String path) throws IOException {
        File f = new File(path);
        br = new BufferedReader(new FileReader(f));
        map.put("inbound_tcp", new ArrayList<>());
        map.put("inbound_udp", new ArrayList<>());
        map.put("outbound_tcp", new ArrayList<>());
        map.put("outbound_udp", new ArrayList<>());

        f1 = new File(rootPath + "/src/main/resources/inbound_tcp.csv");
        f1.createNewFile();

        f1_out = new File(rootPath + "/src/main/resources/inbound_tcp_output.csv");
        f1_out.createNewFile();


        f2 = new File(rootPath + "/src/main/resources/inbound_udp.csv");
        f2.createNewFile();

        f2_out = new File(rootPath + "/src/main/resources/inbound_udp_output.csv");
        f2_out.createNewFile();

        f3 = new File(rootPath + "/src/main/resources/outbound_tcp.csv");
        f3.createNewFile();

        f3_out = new File(rootPath + "/src/main/resources/outbound_tcp_output.csv");
        f3_out.createNewFile();

        f4 = new File(rootPath + "/src/main/resources/outbound_udp.csv");
        f4.createNewFile();

        f4_out = new File(rootPath + "/src/main/resources/outbound_udp_output.csv");
        f4_out.createNewFile();

    }

    public FirewallImpl(FirewallRules firewallRules) {
        this.rules = firewallRules;
    }

    public boolean accept_packet(String direction, String protocol, Integer port, String ip_address) throws IOException {
//        if(!rules.validateDirectionAndProtocol(direction, protocol)
//                && rules.validatePortAndIpAddress(port, ip_address))
//            return false;
        boolean ans = false;
        switch (direction + "_" + protocol)
        {
            case "outbound_udp": ans = rules.validateData(new FileReader(f4_out), port, ip_address);
            break;
            case "outbound_tcp": ans = rules.validateData(new FileReader(f3_out), port, ip_address);
            break;
            case "inbound_udp": ans = rules.validateData(new FileReader(f2_out), port, ip_address);
            break;
            case "inbound_tcp": ans = rules.validateData(new FileReader(f1_out), port, ip_address);
            break;
        }
        return ans;
    }

    public void formRules() throws IOException {
        myWriter1 = new FileWriter(rootPath + "/src/main/resources/inbound_tcp.csv");
        myWriter2 = new FileWriter(rootPath + "/src/main/resources/outbound_udp.csv");
        myWriter3 = new FileWriter(rootPath + "/src/main/resources/inbound_udp.csv");
        myWriter4 = new FileWriter(rootPath + "/src/main/resources/outbound_tcp.csv");

        String line = br.readLine();
        String temp [];
        while (line != null) {
            temp = line.split("\\,");
            if (line.contains("inbound") && line.contains("udp")) {
                List<String> values = map.get(("inbound_udp"));

                values.add(temp[2] + "," + temp[3]);

                map.put("inbound_udp", values);

                if(map.values().size() == 1) {
                    writeToFile(map);
                }
            }
            if (line.contains("outbound") && line.contains("tcp")) {
                List<String> values = map.get(("outbound_tcp"));

                values.add(temp[2] + "," + temp[3]);

                map.put("outbound_tcp", values);

                if(map.values().size() == 1) {
                    writeToFile(map);
                }
            }
            if (line.contains("inbound") && line.contains("tcp")) {
                List<String> values = map.get(("inbound_tcp"));

                values.add(temp[2] + "," + temp[3]);


                map.put("inbound_tcp", values);

                if(map.values().size() == 1) {
                    writeToFile(map);
                }
            }
            if (line.contains("outbound") && line.contains("udp")) {
                List<String> values = map.get(("outbound_udp"));

                values.add(temp[2] + "," + temp[3]);

                map.put("outbound_udp", values);

                if(map.values().size() == 2) {
                    writeToFile(map);
                }
            }
            line = br.readLine();
        }
        writeToFile(map);
        br.close();
        myWriter1.close();
        myWriter2.close();
        myWriter3.close();
        myWriter4.close();
    }

    private void writeToFile(Map<String, List<String>> map) throws IOException {

        List<String> val1 = map.get("inbound_tcp");

        List<String> val2 = map.get("outbound_udp");

        List<String> val3 = map.get("inbound_udp");

        List<String> val4 = map.get("outbound_tcp");

        writeToFile(val1, myWriter1);
        writeToFile(val2, myWriter2);
        writeToFile(val3, myWriter3);
        writeToFile(val4, myWriter4);

        myWriter1.flush();
        myWriter2.flush();
        myWriter3.flush();
        myWriter4.flush();

        map = new HashMap<String, List<String>>();
        map.put("inbound_tcp", new ArrayList<>());
        map.put("inbound_udp", new ArrayList<>());
        map.put("outbound_tcp", new ArrayList<>());
        map.put("outbound_udp", new ArrayList<>());

    }

    public void setRules(FirewallRules rules) {
        this.rules = rules;
    }

    private void writeToFile(List<String> values, FileWriter fileWriter) throws IOException {
        StringBuffer temp = new StringBuffer();
        for (String val : values)
        {
            fileWriter.write(val);
            fileWriter.write("\n");
            fileWriter.flush();
        }
    }

    private void sortFiles(File inputfile,File outputfile) throws IOException, ClassNotFoundException {
        Comparator<CSVRecord> comparator = (op1, op2) -> Integer.parseInt(op1.get(0).split("\\-")[0]) -
                Integer.parseInt(op2.get(0).split("\\-")[0]);
        List<File> sortInBatch = CsvExternalSort.sortInBatch(inputfile, comparator, CsvExternalSort.DEFAULTMAXTEMPFILES, Charset.defaultCharset(), null, false, 0);
        CsvExternalSort.mergeSortedFiles(sortInBatch, outputfile, comparator, Charset.defaultCharset(), false, true);
    }

    public void sortAllFiles() throws IOException, ClassNotFoundException {

        sortFiles(f1, f1_out);

        sortFiles(f2, f2_out);

        sortFiles(f3, f3_out);

        sortFiles(f4, f4_out);

    }

    public void deleteFiles()
    {
        File f = new File(rootPath + "/src/main/resources/");

        for (File temp : f.listFiles()) {
            if (!temp.getName().contains("test")) {
                temp.delete();
            }
        }
    }
}

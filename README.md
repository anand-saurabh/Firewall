Here is a basic firewall implementation. I have given priority to reduce the number of in-memory data we hold as the input rule file can be very big and we can't store all the rules in a hashmap or similar structure in-memory for our comparison of test data.
My approach:
 
1) Break the big input .csv file into 4 smaller .csv files containing the rules data based on direction and protocol :

a)inbound_tcp data b)outbound_tcp data c)inbound_udp data d)outbound_udp data

This helps to reduce the number of records we need to read for matching rules (as we are not keeping rules in-memory as rules can be too many) while we need to test our input data. Suppose, if we break the big input rules files based on Ips or port ranges, there will be a lot of small files created and we need to maintain an in-memory look-up of what port ranges or IP address ranges a particular smaller file fragment contains. So, I decided to break the bigger file based on direction (inbound/outbound) and protocol(tcp or udp). This gives us only 4 smaller splitted files.

2) Further, sort the records in each splitted file based on the port numbers in descending order. This helps to find relevant rules record from the file without reading the whole file every time we get a test data.


How does the program work:

1) First, the constructor takes the input rules file. After that, we call a method to break the bigger input rules file into 4 smaller .csv files as described in step 1.

2) Second, these small files are sorted in descending order based on port numbers (in case of range, we take the start of port number to sort them). Here, we use external sorting to sort each individual small file.

3) Now, suppose the test data given to accept packet method is: "outbound", "tcp", 12000, "192.169.9.11"

We know that for "direction" as "outbound" and "protocol" as "tcp" we have a separate rules file which we created in step 1 from the large bigger input rules file. So, we start searching from the beginning of the outbound_tcp_output.csv file and look for a record whose port number is just smaller or equal to the port number passed to accept_packet method (as our files are sorted based on port numbers). Once we find this record, we just need to compare the ip address of this record with the one in test data and see if the ip address passed to accept method is valid or not.
If it is, we return true, else we return false.

Thoughts which I put in for coming up with this solution:

1) Input rules file can be very large as described in the problem statement
2) We can't store these rules (ip and port number ranges for each direction and protocol) records in-memory
3) We need to limit the number of splitted file we maintain
4) Also, we shouldn't keep the full file (either the smaller or the main input file) in memory at a time, so used BufferedReader to read line by line each record while matching rules for the test input.

Future enhancements:

We can further come up with a model where we can create an index-based query searching like how it happens in read world databases. We can break the bigger input rules file into a smaller limited number of files where each file would contain rules for a range of port or ip addresses only. We can maintain the names of these file and what ranges of port or ip address it contains in a file or in-memory(if feasible). This will reduce the number of reads (the number of rows in a csv to be read) which we need to do to find whether the test input is valid or not.
Testing: To test this, I wrote unit test cases using JUnit and used a test input .csv file and tested the rules (using assert statements in test cases) for different ips, port number, protocol, and direction.

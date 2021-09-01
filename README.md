# The Project
A SNMP Process Monitor written in Java according to the MVC design pattern with GUI made in JavaFX. 

The picture below is a representation of how the MVC design pattern is used, every name inside the boxes corresponds to a Java Class.

![alt-text](https://i.imgur.com/Otg4dG1.png)

In order to retrieve process information and performance metrics the [HOST-RESOURCES-MIB](http://www.net-snmp.org/docs/mibs/host.html) plus the [UCD-SNMP-MIB](http://www.net-snmp.org/docs/mibs/ucdavis.html) are used (*walk* and *bulk* traversals set in the Initial View, with the possibility of **Multi-Threading** in the *bulk* traversal).

## Dependencies

To compile this project you need to have **Java SDK 11**, **JavaFX 11** and **Net-Snmp** properly configured.

**SNMP4J** is the API used for doing SNMP queries to the MIB Tree.

## Features

- **Monitoring capabilities**: list all the processes on your machine, information includes path, type of software, execution status, CPU Usage Time and Memory Usage;
- **Memory metrics**: physical and virtual memory total size, available space and swapspace;
- **Data Visualization** in the form of graphics (Scatter Chart, Pie Chart and Bar Chart) plus table view of process information;
- **Export the results** in Binary,HTML and XML formats.

## Interface

The Initial View is where you will set the parameters of your SNMP-v2c Agent in order to perform the Queries. This application will poll information according to a given period, which you will select, whose options are in seconds (1,2,3,4,5 and 10). After the polling period has passed, some elements of the GUI that display the data will be refreshed (such as the table and graphics).

![Initial View](https://i.imgur.com/UNjGVvM.png)

If you introduce valid settings for the SNMP4J queries, you will be prompted to the Process View where you will be able to see your processes in a table format, this information will be updated according to the polling period you have set in the previous view. From this view you can decide to export the results, given the output you choose, found in the table or check out the Performance Analysis View where you'll be shown further information in graphical format.

![Process View](https://i.imgur.com/cMVZ3dJ.png)

Finally, the last view will show information in various charts. The bottom 2 charts are in decreasing order of how much resources a given process is using, the bottom-left chart refers to the CPU Usage meanwhile the bottom-right refers to Memory Usage (KB).

![Perf View](https://i.imgur.com/Xzse6JX.png)


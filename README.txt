improved Marchal algorithm readme

Change the path "/Users/shilei1025/Documents/workspace" to where you store the marchal file and you can choose which data file you want to test in data/nmea.
Also you can change the value of THRESHOLD to define the threshold distance from point to link.

Map description:
The map is located in data/map.txt
The first line has the number of nodes (N) in the map.
Each of the N following lines have: [Node ID] [Longitude] [Latitude] [Average SNR] [DOP].
After that, there is another line with the number of links (L).
Each of the following L lines have: [Start Node ID] [End Node ID] [Average Speed] [No. times crossed].

Trace input:
Traces are in NMEA format and are expected to be in folder data/nmea. All files with .txt extension are considered.

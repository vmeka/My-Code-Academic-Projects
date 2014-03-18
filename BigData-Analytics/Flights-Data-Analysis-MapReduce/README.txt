==========================================================================================
		COSC 6397 - BIG DATA ANALYTICS - FLIGHT DATA ANALYSIS
==========================================================================================
				Venkata Yeshes Meka  - 1141507 - vmeka@uh.edu
------------------------------------------------------------------------------------------

The deliverables contains:
1. Code : contains the source code for Part1 , Part2 of the requirements. 
	a. Delay-By-OriginAirport - Has the source files related to the Part 1
		***The package name used is "hw1airport"
	b. Delay-By-OriginAirport-PerMonth - has the source files related to Part 2
		***The package name used is "hw1airportmonth"
2. Project Report: containing all the analysis and results of the job.
3. README.txt: with all the information about the files. 


===========
HOW TO RUN
===========
Part 1.:
a. Compile the source code and run the build file.

b.  Now navigate to the /lib/ directory of the build and use the following command for the execution. 
		yarn jar <NameOfJar>.jar hw1airport.FlightsDriver <input-dataset-directory> <output-directory>


Part 2:
a. Compile the source code and run the build file.

b. Now navigate to the /lib/ directory of the build and use the following command for the execution. 
		yarn jar <NameOfJar>.jar hw1airportmonth.FlightsDriver <input-dataset-directory> <output-directory>


==============
ABOUT THE CODE
===============
1. FlightsDriver.java : Contains the Job Configurations and details about the Mapper and Reducer classes along with the specification for number of reducers.

2. FlightsMapper.java : This is the Mapper code for generating the intermediate Key-Value pairs. 

3. FlightsReducer.java :  This is the Reducer code for aggregating the results and generating the final results.


This file contains two things.

1. descriptions of files
2. execution note (parameters and how to use test cases)

============
Description of files
============
The source codes are contains in "src" folder.
Including:

In default package:

*MainProgram: the starting point of this program.
*TimeControler: the timer function which used to compute execution time and decision rate.

In BasicStructures package:

* Vector2: 2D float vector.
* Vector3: 3D float vector.
* ColorVectorRGB: 3D float vector, used to record the RGB valuse of a color.

In BasicBehavior package:

*Seek: the seek behavior in assignment  1. (used in this assignment)
*Seek_Steering_New: also the seek behavior in assignment 1 (not used in this assignment)

In DrawData package:

*BreadcrumbIndo: the data structure to record the position and orientation data.
*DropShape: this class will generate a Drop shape.
*CharacterDrop: this class will generate a character with assigned shape and with a breadcrumb queue.

In MovementStructures

*KiematicData: this class contains the kinematic variables.
*SteeringData: this class contains the Steering variables.
*KinematicOperations: this class contains some basic operation of kinematic variables, such as computing distance, update velocity.
*SystemParameter: set the max Speed and max Acceleration.
*ResultChange: this class is used to record the computation result.

In Variables package
*GlobalSetting: this class contains the parameter for graph generate and demo.

===================
Assignment implementation:
===================

In GraphAlgorithm package:

*Dijkstra: the Dijkstra algorithm is implemented in here.
*AStar: AStar algorithm is implemented in here.
*Heuristic: the interface of heuristic functions.
*H1: Heuristic 1, the Euclidean distance.
*H2: Heuristic 2, the Manhattan distance.
*H3: Heuristic 3, the random guess.

In Graph Dat package
*Node: the data structure to record nodes.
*Edge: the data structure to record edges.
*IndexWeightPair: the data structure contains index and weight, used in node
*TwoIndexCosetPair: the data structure contains two indeices and weight, used in edge
*MapGenerator:  This class is used to get dots about obstacles and nodes.
*GraphGenerator: This class generate the edges between nodes, and also reduce duplicated edges.
*GraphData: This class contains the graph information which will further used in graph algorithms.

==========
Execution Note
==========
All the parameters for demo are contained in GlobalSetting.java  (In Variables package)
The data of small graph are in SmallMap folder.
The data of large graph are in LargeMap folder.
The data for experiments in write up aree in TestCases folder.

-------------------
When the program start, it will load 

obstacles' position:  

*Wall.txt 
*Level2.txt
*Level3.txt 
are different types of obstacles.

nodes positions: 
*Target.txt is the nodes which represent the work station on map.
*Roads.txt contains the nodes which scatter on the map.

All the test data used in experiments (in write up) are in TestCase folder.
To run the test, use the file to replace Road.txt

For example:
To run the 100 node case: change 100_1.txt (in TextCase folder)  --> Road.txt 
The total node will be 100 (road nodes) + 16 (target nodes)

After loading all the node positions, the program will generate a new graph and display it according to parameter setting.
Then will automatically run Dijkstra, 3 versions of A* for default test case.

Here are the parameters (all in GlobalSetting.java) :
* testStart: assign the start point for default test.
* testGoal: assign the goal point for default test.

*HeuristicMode: it controls the path finding algorithm for the character. 
0 -> A* with Euclidean distance
1 -> A* with Manhattan distance
2 -> A* with random guess
3 -> Dijkstra

*NeededPath: this parameter decides the degree of each node, if it is 5, a node will link to 5 neatest nodes.

*wallWeight: to assign weight to obstacle wall.
*level2Weight: to assign weight to obstacle level2.
*level3Weight: to assign weight to obstacle level3.

*numberOfBread: to control the number of breadcrumbs.  
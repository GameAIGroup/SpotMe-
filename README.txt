This file contains two things.

1. descriptions of files
2. execution note (parameters and how to demo)

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

*Seek: the seek behavior in assignment  1. 
*Pursue: contains prediction function for AI bots to predict player's movement

In DrawData package:

*BreadcrumbIndo: the data structure to record the position and orientation data.
*DropShape: this class will generate a Drop shape.
*CharacterDrop: this class will generate a character with assigned shape and with a breadcrumb queue. (used for player)
*HumanShape: this class will generate a Human shape.
*CharacterHuman: this class will generate a character with assigned shape and with a breadcrumb queue. (used for AI bots)
*HeartShape: display player's live

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
*BotVision: This class implement the vision area of AI bots.

In MovementStructures

*KiematicData: this class contains the kinematic variables.
*SteeringData: this class contains the Steering variables.
*KinematicOperations: this class contains some basic operation of kinematic variables, such as computing distance, update velocity.
*SystemParameter: set the max Speed and max Acceleration.
*ResultChange: this class is used to record the computation result.


In Variables package
*GlobalSetting: this class contains the parameter for graph generate and demo.
*CommonFunction: This class contains some common function for the whole program.
*Predictions: This class is the data strcuture for recording AI bots prediction about player movement.
*PublicGraph: This class contains the data of the tile map.


==========
Execution Note
==========
This part contains:
1. how to play our game 
2. how to reproduce all of the the experiments in report 

-------------------
Game instruction
-------------------
Control: 
Use arrow keys to control the player. It can move toward 4 directions.

Goal: 
The player's goal is simple. 
To win the game, player only need to evade bots and try to reach exit before health points become zero

Rules: 
1. The red square on the map are safespot which player can hide from bots sight, but it have time limit.
2. Player will have total 4 lives, which is 800 health points.

-------------------
Demo Instruction
-------------------
All the parameters are contains in GlobalSetting.java (in Variable folder)

//weapon power

minusPoint : 
this variable controls how many health points will be reduced when bot attack.
In report, we test 10, 15, 20.

//safespot

numberOfSafeSpot: this variable controls the number of safe spot.
In report, we test 5, 8, 10.

//Bot vision

maxVisionAngle: this variable controls the vision angle of AI bots.
In report, we test 60, 80, 100, 150, 180 degree. 

maxVisionRange: this variable controls the vision range of AI bots.
In report, we test 80, 100, 150.

//Player AI

playerAIEnable: 
if the value is false, the program become playable game(switch to user control).
if the value is true, player control will be taken by AI player.

AIMode: this variable decide the player AI mode.
0: Mode 2.  -- seek exit and flee
1: Mode 3. -- seek exit and random safespot
2: Mode 4. -- seek exit and closest safespot
3: Mode 5. -- seek exit and safespot closer to exit
4: Mode 6. -- seek exit and random safespot, if not safespot close enough, flee
5: Mode 7. -- seek exit and closest safespot, if not safespot close enough, flee
6: Mode 8. -- seek exit and safespot closer to exit, if not safespot close enough, flee
7: Mode 1. -- seek exit


//Bot

botMode:
decide the wander mode of bots.
0: randomly wander in the map
1: each bot wander different part of the map




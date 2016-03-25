/*
 * License information:
 * 
 * ===================
 * Project Information
 * ===================
 * Name: CSC 584, Assignments
 * 
 * Topic:
 * This pr1ogram is created for 2016 spring, CSC 584 Assignments
 * Assignment 1: test various movement algorithms.
 * Assignment 2: test path finding and path following.
 *  
 * ==================
 * Author information
 * ==================
 * Name: Yi-Chun Chen
 * UnityID: ychen74
 * Student ID:200110436
 * 
 * ==========
 * References
 * ==========
 * 1. textbook.
 * 
 */


/*
 * Program Descriptions
 * =================
 * Coding Convention
 * =================
 * - global: Pascal casing.
 * - local: Camel casing
 * - function input: Pascal casing
 * - function output: Pascal casing
 * - function name: Camel casing
 * - class name: Pascal casing  
 *
 *=====
 *Logic
 *=====
 *- Each basic behavior will be called as function, and return new acceleration or velocity
 *
 *
 */

/*
 * ==============
 * Import Library
 * ==============
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import BasicBehavior.*;
import BasicStructures.*;
import DrawData.*;
import GraphAlgorithm.*;
import GraphData.*;
import MovementStructures.*;
//import OldFile.*;
import Variables.GlobalSetting;
import processing.core.*;

/*
 * =============
 * Class Declare
 * =============
 */
public class MainProgram extends PApplet{
/*
 * ============================
 * Variables for Shared Setting
 * ============================
 */
	private int windowWidth;
	private int windowHeight;
	private SystemParameter Sys;
	private KinematicOperations OperK;
	private TimeControler decisionTimer;
	private TimeControler breadTimer;

	
	
	//Setting for environment
	private ColorVectorRGB backgroundColor;
	private CharacterDrop character;
	
	//private Seek_Steering_New Seek;
	
	private Seek Seek;
	
	private Vector2 originalPoint;
	private Vector2 initialTarget;
	
	//Seek function
	//private KinematicStructure tempK;
	//private KinematicBehavior tempKB;
	//private Seek_Steering tempS;
	private float maxVelocity;
	
	private ResultChange tempResult;
	
	private PImage img;
	private MapGenerator mapCreate;
	private boolean showObstacleNode;
	
	private GraphGenerator GraphGenerator;
	private GraphData G;
	
	private Dijkstra D;
	private AStar A1;
	private AStar A2;
	private AStar A3;
	
	private List<Node> currentNodeList;
	private List<Edge> currentEdgeList;
	
	private TimeControler diffTime;
	
/*
 * ====================
 * Variables for Others
 * ====================
 */

/*
 * (non-Javadoc)
 * @see processing.core.PApplet#settings()
 * ===========================
 * Setting and Initializations
 * ===========================
 */
	
	boolean isSeeking = false;
	List<Integer> currentTargetQueue;
	int targetIndex;
	int closestIndex;
	
	public static GlobalSetting globalS;
	
	public void settings(){
		
		globalS = new GlobalSetting();
		currentTargetQueue = new ArrayList<Integer>();
		
		windowWidth = 800;
		windowHeight = 600;
		
		img = loadImage("gold-garrison.png");
		//img = loadImage("Girl.png");
		
		size(windowWidth, windowHeight);
		// input this operations for each kinematics
		
		
		Sys = new SystemParameter(5, 5*0.1f);

		OperK = new KinematicOperations(this, Sys);
		originalPoint = new Vector2(0, 0);
		decisionTimer = new TimeControler();
		decisionTimer.initialTimer();
		breadTimer = new TimeControler();
		breadTimer.initialTimer();

		diffTime = new TimeControler();
		
		
		backgroundColor = new ColorVectorRGB(255, 255, 255);
		
		
		Vector2 currentShapePosition = new Vector2(windowWidth/2 , windowHeight/2);
		Vector2 initialVelocity = new Vector2(0, 0);
		Vector2 initialAccel = new Vector2(0, 0);
		ColorVectorRGB tempColor = new ColorVectorRGB(23, 228, 119);

		
		maxVelocity = 5;
		
		character = new CharacterDrop(
			this,
			20,
			20,
			originalPoint,
			currentShapePosition,
			0,
			initialVelocity,
			0,
			OperK,				
			initialAccel,
			0,
			tempColor,
			backgroundColor,
			GlobalSetting.numberOfBread
		);
	
			System.out.println("");
	}
/*
 * 	(non-Javadoc)
 * @see processing.core.PApplet#draw()
 * =========
 * Draw Loop
 * =========
 */

	
	public void draw(){
		background(backgroundColor.getR(), backgroundColor.getG(), backgroundColor.getB());
		
		image(img,0,0);
		
		character.updatePosition(character.getK().getPosition());
		character.updateOrientation(character.getK().getOrientation());		
		
		//make decisions in 0.02 sec frequency
		if(decisionTimer.checkTimeSlot(20)){
			//make one decision
		}

		//record
		if(breadTimer.checkTimeSlot(200)){
			character.updateBreadQueue(character.getPosition(), character.getOrientation());
		}
		//display		

		//GraphGenerator.edgeDraw();

		character.display();

	}
/*
 * ==========================
 * Start Point of the Program
 * ==========================	
 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(" This is new program.");
		PApplet.main(new String[] { "--present", "MainProgram" });
	}
}

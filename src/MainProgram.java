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
import GraphAlgorithm.AStar;
import GraphAlgorithm.Dijkstra;
import GraphAlgorithm.H1;
import GraphAlgorithm.H2;
import GraphAlgorithm.H3;
import GraphData.Edge;
import GraphData.GraphData;
import GraphData.GraphGenerator;
import GraphData.MapGenerator;
import GraphData.Node;
import MovementStructures.*;
import Variables.CommonFunction;
import Variables.GameDisplay;
//import OldFile.*;
import Variables.GlobalSetting;
import Variables.PublicGraph;
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

	private KinematicOperations OperK;
	private TimeControler decisionTimer;
	private TimeControler breadTimer;
	
	//Setting for environment
	private ColorVectorRGB backgroundColor;
	private CharacterDrop character;
	
	private CharacterHuman[] Bot;
	//private CharacterHuman test;
	private int NumberOfBots;
	private List<Integer>[] botsTargetQueue;
	
	private Vector2 originalPoint;
	
	//Seek function
	//private Seek Seek;
	//private AStar A1;
	private ResultChange tempResult;
	private Vector2 initialTarget;
	boolean isSeeking = false;
	List<Integer> currentTargetQueue;
	int targetIndex;
	int closestIndex;
	
	private List<Node> currentNodeList;
	private List<Edge> currentEdgeList;
	
	

	private PImage img;
	

	
	private Pursue pursue;
	
	//key control
	private int upMove;
	private int downMove;
	private int leftMove;
	private int rightMove;
	
	
	
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
	public PublicGraph publicG;
	public CommonFunction CF;
	
	public static SystemParameter Sys;
	public static GlobalSetting globalS;
	public static GameDisplay gameDisplay;
	
	public void settings(){

		globalS = new GlobalSetting();
		// set system parameter: max V, max Acceleration
		Sys = new SystemParameter(5, 5*0.1f, PI/2.0f);
		// input this operations for each kinematics
		OperK = new KinematicOperations(this, Sys);

		CF = new CommonFunction(OperK);
		publicG = new PublicGraph(this, OperK);
		
		gameDisplay = new GameDisplay(this);

		
		windowWidth = GlobalSetting.screenWidth;
		windowHeight = GlobalSetting.screenHeight;

		img = loadImage("TestBackground.JPG");
		backgroundColor = new ColorVectorRGB(255, 255, 255);

		size(windowWidth, windowHeight);
		

		
		originalPoint = new Vector2(0, 0);

		//the decision rate
		decisionTimer = new TimeControler();
		decisionTimer.initialTimer();
		// the time slat for record breadcrumbs
		breadTimer = new TimeControler();
		breadTimer.initialTimer();
		
		Vector2 currentShapePosition = new Vector2(64 , windowHeight-64);
		currentShapePosition = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, currentShapePosition)).coordinate;
		Vector2 initialVelocity = new Vector2(0, 0);
		Vector2 initialAccel = new Vector2(0, 0);
		ColorVectorRGB tempColor = new ColorVectorRGB(23, 228, 119);

	

		currentNodeList = new ArrayList<Node>();
		currentNodeList = PublicGraph.G.nodeList;
		
		currentEdgeList = new ArrayList<Edge>();
		currentEdgeList = PublicGraph.G.edgeList;

	
		//Seek for test 
	
		
		//set character
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
				GlobalSetting.numberOfBread,
				Sys,
				0
		);
		
		initialTarget = currentShapePosition;
		tempResult = new ResultChange(
				character.getPosition().getX(),
				character.getPosition().getY(),
				character.getK().getOrientation(),
				character.getK().getVelocity().getX(),
				character.getK().getVelocity().getY(),
				character.getK().getRotation(),
				OperK,
				character.getS().getLinearAccel().getX(),
				character.getS().getLinearAccel().getY(),
				character.getS().getAngularAccel()
		);	
		
		NumberOfBots = GlobalSetting.numberOfbots;
		Bot = new CharacterHuman[NumberOfBots];
		//botsTargetQueue = new ArrayList<Integer>[NumberOfBots]();

		//prediction------------------------------------------------------------------------------------------		
		//test Prediction is OK
		Vector2 a = new Vector2(20, 10);
		character.updateMyPrediction(a);
		System.out.println(character.getMyPrediction().getX());
		//End of prediction------------------------------------------------------------------------------------

				
		
		for(int i = 0; i<NumberOfBots ;i ++ ){
			Vector2 botPosition = new Vector2((float)Math.random()*windowWidth, (float)Math.random()*windowHeight);
			
			while(PublicGraph.graphGenerator.ObsOverlapList.get(CommonFunction.findClose(currentNodeList, botPosition))==1){
				botPosition = new Vector2((float)Math.random()*windowWidth, (float)Math.random()*windowHeight);
			}
			
			Bot[i] = new CharacterHuman(
					this,
					20,
					20,
					originalPoint,
					botPosition,
					0,
					initialVelocity,
					0,
					OperK,				
					initialAccel,
		
					0,
					new ColorVectorRGB((float)Math.random()*255, (float)Math.random()*255, (float)Math.random()*255),
					backgroundColor,
					GlobalSetting.numberOfBread,
					Sys,
					0
			);
		}		
		
		
		currentTargetQueue = new ArrayList<Integer>();
		
		//key control
		upMove = 0;
		downMove = 0;
		leftMove = 0;
		rightMove = 0;
		
		
		System.out.println("");
	}
/*
 * 	(non-Javadoc)
 * @see processing.core.PApplet#draw()
 * =========
 * Draw Loop
 * =========
 */

	int count =0;

	public void draw(){
		background(backgroundColor.getR(), backgroundColor.getG(), backgroundColor.getB());
		
		image(img,0,0);
		
		
		character.updatePosition(character.getK().getPosition());
		character.updateOrientation(character.getK().getOrientation());		
		
		for(int i = 0; i < NumberOfBots; i++){
			Bot[i].updatePosition(Bot[i].getK().getPosition());
			Bot[i].updateOrientation(Bot[i].getK().getOrientation());		
		}

		//================Decision Tree=================================================================================
		
		boolean checkPlayer = false;
		boolean inShootingRange = false;
		boolean isCloset = false;
		boolean haveWeapon  = false;
		boolean inSafeSpot = false;
		boolean receiveRequest  = false; //when to switch back to no receive?
		int[] otherbots;//
		//--------------
		otherbots = new int[NumberOfBots];
		
		
		if(Bot[1].checkSeekMode() == true){
			if ( checkPlayer == true){
				//1. seek mode- purse
				Bot[1].isSeekMode();
				//---Bot[1].updateMyPrediction(pursue.makePrediction(targetPastPosition, targetCurrentPosition, selfCurrentPosition));
				//ask for other wander bots to support
				for (int i = 0; i < otherbots.length ; i++){
					if ( Bot[i].checkSeekMode() == false){
						Bot[i].receiveRequest();
						//use this bot's prediction
						Bot[i].updateMyPrediction(Bot[1].givePrediction()); 
					}
				}
				
				//2. is in shooting range
				if( inShootingRange == true ){
					
					if( isCloset == true ){
					
						if( haveWeapon == true){
							//shoot();
						}
						else{//don't have weapon
							//requestWeapon();
						}
					}
					else{//not the closest
						Bot[1].isSeekMode();
						//---Seek.updateTargetPosition(Bot[1].getMyPrediction());
					}
						
				}
				else{//not in shooting range
					Bot[1].isSeekMode();
					//----Seek.updateTargetPosition(Bot[1].getMyPrediction());
				}
			}
			else{//no player around
				if( inSafeSpot == true ){
					//wander;
					Bot[1].isWanderMode();
				}
				else{//go to prediction from other bots
					Bot[1].isSeekMode();
					//---Seek.updateTargetPosition(Bot[1].getMyPrediction());
				}
			}
		}
		else{
			//wandering Mode
			if ( checkPlayer == true){
				Bot[1].isSeekMode();
				//---Bot[1].updateMyPrediction(pursue.makePrediction(targetPastPosition, targetCurrentPosition, selfCurrentPosition));
				//---Seek.updateTargetPosition(Bot[1].getMyPrediction());			
			}
			else{//no player around
				if ( character.checkRequest() == true ){
					Bot[1].isSeekMode();
					//---Bot[1].updateMyPrediction(pursue.makePrediction(targetPastPosition, targetCurrentPosition, selfCurrentPosition));
					//---Seek.updateTargetPosition(Bot[1].getMyPrediction());	
				}
				else{
					//keep wandering
					Bot[1].isWanderMode();
				}
			}
		}
		
		//================End of Decision Tree=================================================================================
		

		//Gathering dots
		
		//make decisions in 0.02 sec frequency
		if(decisionTimer.checkTimeSlot(20)){
			//for test safe spot
			count = (count+1)%200;
			if(count == 0){
				PublicGraph.graphGenerator.recreateAllSafeSpots();
				PublicGraph.graphGenerator.updateOverlapSafeSpots();
				
			}
			else if(count == 100){
				int tempIndex = (int) (Math.random()*PublicGraph.graphGenerator.SafeSpotsList.size());
				PublicGraph.graphGenerator.removeSafeSpots(tempIndex);
				PublicGraph.graphGenerator.addSafeSpots();
				PublicGraph.graphGenerator.updateOverlapSafeSpots();
			}
			
			if(count%2 == 0){
				GlobalSetting.characterHealthPoints = (GlobalSetting.characterHealthPoints+GlobalSetting.characterMaxHealth-1)%GlobalSetting.characterMaxHealth;
			}
			
			for(int i = 0 ; i < NumberOfBots; i++){
				Bot[i].Wander();
			}
		}


		//record
		if(breadTimer.checkTimeSlot(200)){
			character.updateBreadQueue(character.getPosition(), character.getOrientation());
			for(int i = 0; i < NumberOfBots; i++){
				Bot[i].updateBreadQueue(Bot[i].getPosition(), Bot[i].getOrientation());
			}
		}
		//display		
		GameDisplay.displayLives();
		
		PublicGraph.graphGenerator.edgeDraw();
		//PublicGraph.graphGenerator.displayObstacle();
		PublicGraph.graphGenerator.displaySafeSpot();
		//PublicGraph.graphGenerator.nodeDisplay(this);
		GameDisplay.displayLives();
		GameDisplay.displayHealth();

		//mapCreate.nodeDisplay(this);
		character.display();
		for(int i = 0; i < NumberOfBots; i++){
			Bot[i].display();
		}

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
	public void mouseReleased(){

		stroke(0);
		ellipse( mouseX, mouseY, 5, 5 );
		text( "x: " + mouseX + " y: " + mouseY, mouseX + 2, mouseY );	
		PublicGraph.mapCreate.markObstacles(new Vector2(mouseX, mouseY));
	}
	public void keyPressed() {
		if (keyCode == UP) {
			upMove = upMove-GlobalSetting.keyMoveDistance;
			//System.out.println("UP: "+upMove);
			character.Move(upMove, downMove, leftMove, rightMove);
			upMove = 0;
			downMove = 0;
			leftMove = 0;
			rightMove = 0;

		} 
	    else if (keyCode == DOWN) {
			downMove = downMove+GlobalSetting.keyMoveDistance;
			//System.out.println("Down: "+downMove);
			character.Move(upMove, downMove, leftMove, rightMove);
			upMove = 0;
			downMove = 0;
			leftMove = 0;
			rightMove = 0;

	    }
	    else if(keyCode == LEFT){
			leftMove = leftMove-GlobalSetting.keyMoveDistance;
			//System.out.println("Left: "+leftMove);
			character.Move(upMove, downMove, leftMove, rightMove);
			upMove = 0;
			downMove = 0;
			leftMove = 0;
			rightMove = 0;

	    }
	    else if(keyCode == RIGHT){
			rightMove = rightMove+GlobalSetting.keyMoveDistance;
			//System.out.println("Right: "+rightMove);
			character.Move(upMove, downMove, leftMove, rightMove);
			upMove = 0;
			downMove = 0;
			leftMove = 0;
			rightMove = 0;

	    }
	}	

}

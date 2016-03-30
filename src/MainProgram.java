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
	
	private CharacterDrop[] Bot;
	private int NumberOfBots;
	private List<Integer>[] botsTargetQueue;
	
	private Vector2 originalPoint;
	
	//Seek function
	private Seek Seek;
	private AStar A1;
	private ResultChange tempResult;
	private Vector2 initialTarget;
	boolean isSeeking = false;
	List<Integer> currentTargetQueue;
	int targetIndex;
	int closestIndex;
	
	private List<Node> currentNodeList;
	private List<Edge> currentEdgeList;
	
	

	private PImage img;
	
	//for graph
	private MapGenerator mapCreate;
	private GraphGenerator graphGenerator; 
	private GraphData G;
	
	private Pursue pursue;
	public CommonFunction CF;
	
	
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
	
	public static GlobalSetting globalS;
	
	public void settings(){
		
		globalS = new GlobalSetting();
		// set system parameter: max V, max Acceleration
		Sys = new SystemParameter(5, 5*0.1f, PI/2.0f);
		// input this operations for each kinematics
		OperK = new KinematicOperations(this, Sys);

		CF = new CommonFunction(OperK);

		
		windowWidth = GlobalSetting.screenWidth;
		windowHeight = GlobalSetting.screenHeight;

		img = loadImage("TestBackground.JPG");

		size(windowWidth, windowHeight);
		backgroundColor = new ColorVectorRGB(255, 255, 255);

		
		originalPoint = new Vector2(0, 0);
		
		//the decision rate
		decisionTimer = new TimeControler();
		decisionTimer.initialTimer();
		// the time slat for record breadcrumbs
		breadTimer = new TimeControler();
		breadTimer.initialTimer();
		
		Vector2 currentShapePosition = new Vector2(64 , windowHeight-64);
		Vector2 initialVelocity = new Vector2(0, 0);
		Vector2 initialAccel = new Vector2(0, 0);
		ColorVectorRGB tempColor = new ColorVectorRGB(23, 228, 119);

		//This part order cannot be changed
		
		mapCreate = new MapGenerator(5, 7, "test.txt");
		//mapCreate.createRandomGraph("random.txt");

		mapCreate.drawDot(GlobalSetting.tileNumber, windowWidth, windowHeight);
		
		mapCreate.readTile(this);
		mapCreate.readObstacle(this);
		//mapCreate.isObstacle = true;
		
		graphGenerator = new GraphGenerator(mapCreate, OperK, this);
		graphGenerator.createEdge();
		
		G = new GraphData(graphGenerator.nodeList, graphGenerator.edgeList, this);		

		currentNodeList = new ArrayList<Node>();
		currentNodeList = G.nodeList;
		
		currentEdgeList = new ArrayList<Edge>();
		currentEdgeList = G.edgeList;

	
		//Seek for test 
		Seek = new Seek(
				5.0f,
				100.0f,
				0.1f,
				currentShapePosition,
				1,
				currentShapePosition,
				initialVelocity,
				0,
				0,
				initialAccel,
				0,
				OperK,
				Sys.getMaxV(),
				Sys.getMaxV()*0.1f,
				this
				
		);	
		
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
			0, 
			Seek
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
		Bot = new CharacterDrop[NumberOfBots];
		//botsTargetQueue = new ArrayList<Integer>[NumberOfBots]();
		
		
		//prediction------------------------------------------------------------------------------------------		
		//test Prediction is OK
		Vector2 a = new Vector2(20, 10);
		character.updateMyPrediction(a);
		println(character.getMyPrediction().getX());
		//End of prediction------------------------------------------------------------------------------------

		
		
		
		for(int i = 0; i<NumberOfBots ;i ++ ){
			Vector2 botPosition = new Vector2((float)Math.random()*windowWidth, (float)Math.random()*windowHeight);
			
			while(graphGenerator.ObsOverlapList.get(CommonFunction.findClose(currentNodeList, botPosition))==1){
				botPosition = new Vector2((float)Math.random()*windowWidth, (float)Math.random()*windowHeight);
			}
			
			Bot[i] = new CharacterDrop(
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
					i,
					Seek
			);
		}		
		
		
		currentTargetQueue = new ArrayList<Integer>();
		
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
		
		boolean checkPlayer;
		boolean inShootingRange;
		boolean isCloset;
		boolean haveWeapon;
		boolean inSafeSpot;
		boolean receiveRequest; //when to switch back to no receive?
		int[] otherbots;//
	
		if(Bot[1].checkSeekMode() == true){
			if ( checkPlayer == true){
				//1. seek mode- purse
				Bot[1].isSeekMode();
				Bot[1].updateMyPrediction(pursue.makePrediction(targetPastPosition, targetCurrentPosition, selfCurrentPosition));
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
						Seek.updateTargetPosition(Bot[1].getMyPrediction());
					}
						
				}
				else{//not in shooting range
					Bot[1].isSeekMode();
					Seek.updateTargetPosition(Bot[1].getMyPrediction());
				}
			}
			else{//no player around
				if( inSafeSpot == true ){
					//wander;
					Bot[1].isWanderMode();
				}
				else{//go to prediction from other bots
					Bot[1].isSeekMode();
					Seek.updateTargetPosition(Bot[1].getMyPrediction());
				}
			}
		}
		else{
			//wandering Mode
			if ( checkPlayer == true){
				Bot[1].isSeekMode();
				Bot[1].updateMyPrediction(pursue.makePrediction(targetPastPosition, targetCurrentPosition, selfCurrentPosition));
				Seek.updateTargetPosition(Bot[1].getMyPrediction());			
			}
			else{//no player around
				if ( character.checkRequest() == true ){
					Bot[1].isSeekMode();
					Bot[1].updateMyPrediction(pursue.makePrediction(targetPastPosition, targetCurrentPosition, selfCurrentPosition));
					Seek.updateTargetPosition(Bot[1].getMyPrediction());	
				}
				else{
					//keep wandering
					Bot[1].isWanderMode();
				}
			}
		}
		
		//================End of Decision Tree=================================================================================
		

		if(isSeeking == false){
			//if(mousePressed){
				isSeeking = true;
				//call path finding

				initialTarget = new Vector2((float)Math.random()*windowWidth, (float)Math.random()*windowHeight);
				targetIndex = CommonFunction.findClose(currentNodeList, initialTarget);
				
				while(graphGenerator.ObsOverlapList.get(targetIndex)== 1){
					initialTarget = new Vector2((float)Math.random()*windowWidth, (float)Math.random()*windowHeight);
					targetIndex = CommonFunction.findClose(currentNodeList, initialTarget);
				}
				
				closestIndex = CommonFunction.findClose(currentNodeList, character.getK().getPosition());
				
				//System.out.println(targetIndex+ ", " + closestIndex);
				H2 h1 = new H2(currentNodeList, currentEdgeList, targetIndex, closestIndex, OperK);
				
				A1 = new AStar(h1, currentNodeList, currentEdgeList, targetIndex, closestIndex);

				while(A1.openList.size()>0){
					A1.computeAStar(G.nodeList, G.edgeList);
					//System.out.println("-----------");
				}
				if(A1.isFind == false){
					System.out.println("Didn't find!!");
				}
				else{
				}
				currentTargetQueue.clear();
				currentTargetQueue.addAll(A1.result);
			//}
	
		}
		


		//Gathering dots
		
		//make decisions in 0.02 sec frequency
		if(decisionTimer.checkTimeSlot(20)){
			count = (count+1)%50;
			//make one decision
			if(isSeeking == true){
				if(currentTargetQueue.size()>0){
					//if(findClose(currentNodeList,character.getK().getPosition())!=currentTargetQueue.get(0)){
					if(OperK.getDisBy2Points(currentNodeList.get(currentTargetQueue.get(0)).coordinate, character.getK().getPosition())>5){
						//System.out.println("Current Target = " + currentTargetQueue.get(0));
						initialTarget = currentNodeList.get(currentTargetQueue.get(0)).coordinate;
					}
					else{
						currentTargetQueue.remove(0);
					}

					tempResult = Seek.computeSeek(initialTarget);
			
					character.setK(tempResult.getK());
					character.setS(tempResult.getS());
				}
				else{
					isSeeking = false;
					
				}
				if(count == 0){
					isSeeking = false;
				}
				
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

		graphGenerator.edgeDraw();
		graphGenerator.displayObstacle();
		//graphGenerator.nodeDisplay(this);

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
		mapCreate.markObstacles(new Vector2(mouseX, mouseY));
	}

}

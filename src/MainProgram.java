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

import java.awt.Window;
import java.awt.event.KeyEvent;
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
import GraphData.BotVision;
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
import MovementStructures.KinematicOperations.*;

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
	private boolean isGameOver = false;
	private int windowWidth;
	private int windowHeight;

	private KinematicOperations OperK;
	private TimeControler botDecisionTimer;
	private TimeControler playerDecisionTimer;
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
	private List<Node> highlightedNodes;
	private BotVision botVision;
	
	

	private PImage img, gameOver;

	private PImage[] LevelBack;
	


        //for graph
	private MapGenerator mapCreate;
	private GraphGenerator graphGenerator; 
	private GraphData G;
	private KinematicOperations kinematicOp;


	
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
	boolean setLevel = false;
	
	public void settings(){

		globalS = new GlobalSetting();
		// set system parameter: max V, max Acceleration
		Sys = new SystemParameter(5, 5*0.1f, PI/2.0f);
		// input this operations for each kinematics
		OperK = new KinematicOperations(this, Sys);
		publicG = new PublicGraph(this, OperK);
		
		CF = new CommonFunction(OperK);

		
		gameDisplay = new GameDisplay(this);

		
		windowWidth = GlobalSetting.screenWidth;
		windowHeight = GlobalSetting.screenHeight;

		img = loadImage("TestBackground.JPG");
		gameOver = loadImage("game-over.jpg");
		
		LevelBack = new PImage[3];
		for(int i = 0 ;i < GlobalSetting.LevelNumber; i++){
			LevelBack[i] = loadImageIO("Level"+(i+1)+".JPG");
		}
		backgroundColor = new ColorVectorRGB(255, 255, 255);

		size(windowWidth, windowHeight);
		

		
		originalPoint = new Vector2(0, 0);

		//the decision rate
		botDecisionTimer = new TimeControler();
		botDecisionTimer.initialTimer();
		playerDecisionTimer = new TimeControler();
		playerDecisionTimer.initialTimer();

		// the time slat for record breadcrumbs
		breadTimer = new TimeControler();
		breadTimer.initialTimer();
		

		//What should be redo 
		InitilizeAll();
		
		System.out.println("");
	}
	
	private void HighlightNodes(List<Node> nodes)
	{
		for (Node node: nodes)
		{
			pushMatrix();
			fill(240, 255, 125);
			ellipse(node.coordinate.x, node.coordinate.y, (float)5, (float)5);
			popMatrix();
		}
	}
	
	public void checkWinningCondition()
	{
		if ((character.getPosition().x > 500) && (character.getPosition().y < 20))
		{
			GlobalSetting.LevelControl = (GlobalSetting.LevelControl+1)%GlobalSetting.LevelNumber;
			InitilizeAll();
		}
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
		if (isGameOver)
		{
			image(gameOver,width/4, height/4, width/2, height/2);
			return;
		}
		
		checkWinningCondition();
		
		background(backgroundColor.getR(), backgroundColor.getG(), backgroundColor.getB());
		
		image(LevelBack[GlobalSetting.LevelControl],0,0);
	
		character.updatePosition(character.getK().getPosition());
		character.updateOrientation(character.getK().getOrientation());		
		
		for(int i = 0; i < NumberOfBots; i++){
			Bot[i].updatePosition(Bot[i].getK().getPosition());
			Bot[i].updateOrientation(Bot[i].getK().getOrientation());		
		}
		

		//Gathering dots
		if(playerDecisionTimer.checkTimeSlot(200)){
			//For testing safe spots
			character.Move(upMove, downMove, leftMove, rightMove);
			upMove = 0;
			downMove = 0;
			leftMove = 0;
			rightMove = 0;
		}
		
		boolean[] checkPlayer;
		checkPlayer = new boolean[NumberOfBots];
		
		for(int botIter = 0; botIter < NumberOfBots; botIter++){
			checkPlayer[botIter] = false;
			botVision = Bot[botIter].getVisionRangeNodes(G, OperK, graphGenerator, character); 
			checkPlayer[botIter] = botVision.isCharacterInVision(character, Bot[botIter], OperK);
			
			if(checkPlayer[botIter] == true){
				pushMatrix();
				fill(240, 255, 125, 125);
				ellipse(character.getPosition().x, character.getPosition().y, 200, 200);
				popMatrix();
			}
		}

		
		//make decisions in 0.02 sec frequency
		if(botDecisionTimer.checkTimeSlot(1000)){
			//bot decision cycle
			count = (count +1)%200;
			//For testing safe spots
			int[] otherbots;//
			//--------------
			otherbots = new int[NumberOfBots];
			
			CommonFunction.activateSafeSpot(count, 0, 100);
			for(int botIter = 0 ; botIter < NumberOfBots; botIter++){

				//Bot[botIter].Wander();
				//put decision tree in here, since there is not multi-thread
				

				//================Decision Tree=================================================================================

				boolean inShootingRange = false;
				boolean isCloset = false;
				boolean haveWeapon  = false;
				boolean inSafeSpot = false;
				boolean receiveRequest  = false; //when to switch back to no receive?
				
				// when bot is seeking
				if(Bot[botIter].checkSeekMode() == true){
					if ( checkPlayer[botIter] == true){
						//System.out.println("System: bot "+botIter+" is seeking and sees the player right now!");
						//1. seek mode- purse
						Bot[botIter].isSeekMode();
						Vector2 myprediction = pursue.makeUnitTimePrediction(GlobalSetting.pastPosition[botIter], character.getPosition(), Bot[botIter].getPosition());
						GlobalSetting.predictions.setMyPrediction(botIter, myprediction);
						System.out.println("player now in ("+ character.getPosition().x+", "+character.getPosition().y+")");
						System.out.println("bot "+botIter+" predict player in (" + GlobalSetting.predictions.getMyPrediction(botIter).x +", " +GlobalSetting.predictions.getMyPrediction(botIter).y+")");
						//Seek the prediction
						Bot[botIter].Seek(myprediction);
						
/*
						//ask for other wander bots to support
						for (int i = 0; i < otherbots.length ; i++){
							if ( Bot[i].checkSeekMode() == false){
								Bot[i].receiveRequest();
								//use this bot's prediction
								Bot[i].updateMyPrediction(Bot[1].givePrediction()); 
							}
						}
*/
						
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
								Bot[botIter].isSeekMode();
								//---Seek.updateTargetPosition(Bot[1].getMyPrediction());
							}
								
						}
						else{//not in shooting range
							Bot[botIter].isSeekMode();
							//----Seek.updateTargetPosition(Bot[1].getMyPrediction());
						}
					}
					else{//no player around
						if( inSafeSpot == true ){
							//wander;
							Bot[botIter].isWanderMode();
						}
						else{//go to prediction from other bots
							Bot[botIter].isSeekMode();
							Vector2 myprediction = new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
							int checkPredictionIter = 0;
							while(checkPredictionIter < NumberOfBots){
								if(checkPredictionIter != botIter){
									// use other's prediction
									if(GlobalSetting.predictions.getMyPrediction(checkPredictionIter).x >= 0 &&GlobalSetting.predictions.getMyPrediction(checkPredictionIter).y >= 0){
										//some one have prediction, use it
										myprediction = GlobalSetting.predictions.getMyPrediction(checkPredictionIter);
										GlobalSetting.predictions.setMyPrediction(botIter, myprediction);
										//also use others' recorded past position
										GlobalSetting.pastPosition[botIter] = GlobalSetting.pastPosition[checkPredictionIter];
										//System.out.println("Bot " + botIter + " Get prediction from bot" + checkPredictionIter +" , ("+myprediction.x +", "+myprediction.y+")");
										break;
									}
								}
								checkPredictionIter++;
							}
							Bot[botIter].Seek(myprediction);
							//---Seek.updateTargetPosition(Bot[1].getMyPrediction());
						}
					}
				}
				else{
					//wandering Mode
					Bot[botIter].Wander();
					//Bot[botIter].Seek(new Vector2(100, 100));
					if ( checkPlayer[botIter] == true){
						System.out.println("System: bot "+botIter+" sees the player right now!");
						Bot[botIter].isSeekMode();
						//no past position
						Vector2 myprediction = pursue.makeUnitTimePrediction(new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY), character.getPosition(), Bot[botIter].getPosition());
						GlobalSetting.predictions.setMyPrediction(botIter, myprediction);
						//save past position
						GlobalSetting.pastPosition[botIter] = character.getPosition();
						System.out.println("bot "+botIter+" predict player in (" + GlobalSetting.predictions.getMyPrediction(botIter).x +", " +GlobalSetting.predictions.getMyPrediction(botIter).y+")");
						//---Bot[1].updateMyPrediction(pursue.makePrediction(targetPastPosition, targetCurrentPosition, selfCurrentPosition));
						//---Seek.updateTargetPosition(Bot[1].getMyPrediction());			
					}
					else{//no player around
						//if ( character.checkRequest() == true ){
							//System.out.println("bot "+botIter+ " didn't see player");
							Vector2 myprediction = new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
							int checkPredictionIter = 0;
							while(checkPredictionIter < NumberOfBots){
								if(checkPredictionIter != botIter){
									// use other's prediction
									if(GlobalSetting.predictions.getMyPrediction(checkPredictionIter).x >= 0 &&GlobalSetting.predictions.getMyPrediction(checkPredictionIter).y >= 0){
										//some one have prediction, use it
										myprediction = GlobalSetting.predictions.getMyPrediction(checkPredictionIter);
										GlobalSetting.predictions.setMyPrediction(botIter, myprediction);
										//also use others' recorded past position
										GlobalSetting.pastPosition[botIter] = GlobalSetting.pastPosition[checkPredictionIter];
										//System.out.println("Bot " + botIter + " Get prediction from bot" + checkPredictionIter +" , ("+myprediction.x +", "+myprediction.y+")");
										break;
									}
								}
								checkPredictionIter++;
							}
							
							if(myprediction.x >=0 && myprediction.y >= 0){
								// prediction updated by other's data
								Bot[botIter].isSeekMode();
							}
							else{
								Bot[botIter].isWanderMode();
							}
							
							//---Bot[1].updateMyPrediction(pursue.makePrediction(targetPastPosition, targetCurrentPosition, selfCurrentPosition));
							//---Seek.updateTargetPosition(Bot[1].getMyPrediction());	
						//}
						//else{
							//keep wandering
						//}
					}
				}
				
				//================End of Decision Tree=================================================================================
				
				
				
				/*
				if (botVision.isCharacterInVision(character, Bot[i], OperK))
				{
					pushMatrix();
					fill(240, 255, 125, 125);
					ellipse(character.getPosition().x, character.getPosition().y, 200, 200);
					popMatrix();
					// if find player, reduce health points.
					GlobalSetting.characterHealthPoints = (GlobalSetting.characterHealthPoints+GlobalSetting.characterMaxHealth-GlobalSetting.deductionPerShot)%GlobalSetting.characterMaxHealth;
					if (GlobalSetting.characterHealthPoints <= 0)
					{
						isGameOver = true;
					}
				}
				*/

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
		//PublicGraph.graphGenerator.edgeDraw();
		//PublicGraph.graphGenerator.displayObstacle();
		PublicGraph.graphGenerator.displaySafeSpot();
		//PublicGraph.graphGenerator.nodeDisplay(this);

		//mapCreate.nodeDisplay(this);
		character.display();
		for(int i = 0; i < NumberOfBots; i++){
			HighlightNodes(Bot[i].getVisionRangeNodes(G, OperK, graphGenerator, character).visionNodes);
		}
		
		for(int i = 0; i < NumberOfBots; i++){
			Bot[i].display();
		}
		GameDisplay.displayLives();
		GameDisplay.displayHealth();

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
	public void InitilizeAll(){
		GlobalSetting.characterHealthPoints = GlobalSetting.characterMaxHealth;
		//setLevel = true;
		PublicGraph.mapCreate.readObstacle(this, GlobalSetting.LevelControl+1);
		PublicGraph.graphGenerator = new GraphGenerator(PublicGraph.mapCreate, OperK, this);
		PublicGraph.graphGenerator.createEdge();
		
		PublicGraph.G = new GraphData(PublicGraph.graphGenerator.nodeList, PublicGraph.graphGenerator.edgeList, this);			
		
		PublicGraph.graphGenerator.recreateAllSafeSpots();
		PublicGraph.graphGenerator.updateOverlapSafeSpots();	
		
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
		pursue = new Pursue(OperK, Sys);
		
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
		//Vector2 a = new Vector2(20, 10);
		//character.updateMyPrediction(a);
		//System.out.println(character.getMyPrediction().getX());
		//End of prediction------------------------------------------------------------------------------------

				
		
		for(int i = 0; i<NumberOfBots ;i ++ ){
			Vector2 botPosition = new Vector2((float)Math.random()*(windowWidth-100)+50 , (float)Math.random()*(windowHeight-100)+50);
			botPosition = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, botPosition)).coordinate;
			
/*
			while(PublicGraph.graphGenerator.ObsOverlapList.get(CommonFunction.findClose(currentNodeList, botPosition))==1){
				botPosition = new Vector2((float)Math.random()*(windowWidth-00)+50 , (float)Math.random()*(windowHeight-100)+50);
			}
*/			
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
					//new ColorVectorRGB((float)Math.random()*255, (float)Math.random()*255, (float)Math.random()*255),
					new ColorVectorRGB((float)255*(i%2), (float)255*(i%2), (float)255*(i%2)),
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
		
	}
	public void keyPressed() {
		if (keyCode == UP) {
			upMove = upMove-GlobalSetting.keyMoveDistance;
			//System.out.println("UP: "+upMove);
			character.updateOrientation(0);

			//character.Move(upMove, downMove, leftMove, rightMove);

		} 
	    else if (keyCode == DOWN) {
			downMove = downMove+GlobalSetting.keyMoveDistance;
			//System.out.println("Down: "+downMove);
			character.updateOrientation(PI);

			//character.Move(upMove, downMove, leftMove, rightMove);
			//upMove = 0;
			//downMove = 0;
			//leftMove = 0;
			//rightMove = 0;

	    }
	    else if(keyCode == LEFT){
			leftMove = leftMove-GlobalSetting.keyMoveDistance;
			//System.out.println("Left: "+leftMove);
			character.updateOrientation(-PI/2);

			//character.Move(upMove, downMove, leftMove, rightMove);
			//upMove = 0;
			//downMove = 0;
			//leftMove = 0;
			//rightMove = 0;

	    }
	    else if(keyCode == RIGHT){
			rightMove = rightMove+GlobalSetting.keyMoveDistance;
			//System.out.println("Right: "+rightMove);
			character.updateOrientation(PI/2);
			//character.Move(upMove, downMove, leftMove, rightMove);
			//upMove = 0;
			//downMove = 0;
			//leftMove = 0;
			//rightMove = 0;

	    }
		
		if(keyCode == KeyEvent.VK_SPACE){
			GlobalSetting.LevelControl = (GlobalSetting.LevelControl+1)%GlobalSetting.LevelNumber;
			InitilizeAll();
		}
	}	

}

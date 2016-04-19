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
	private TimeControler gameTime;
	
	//Setting for environment
	private ColorVectorRGB backgroundColor;
	private CharacterDrop character;
	
	private CharacterHuman[] Bot;
	//private CharacterHuman test;
	private int NumberOfBots;
	private List<Integer>[] botsTargetQueue;
	
	private Vector2 originalPoint;
	private Vector2 characterSeekPosition;
	
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
	private PImage imgWin, gameWin;

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
	int[] wanderCount;
	boolean[] keepSeekFlag; 
	
	boolean winCheck = false;
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
		//imgWin = loadImage("win.JPG");
		gameWin =  loadImage("win.png");
		
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
		
		gameTime = new TimeControler();
		gameTime.initialTimer();

		
		
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
			ellipse(node.coordinate.x, node.coordinate.y, (float)GlobalSetting.nodeSize, (float)GlobalSetting.nodeSize);
			popMatrix();
		}
	}
	
	public void checkWinningCondition()
	{
		//System.out.println(character.getPosition().x+ ", " +character.getPosition().y);
		if ((character.getPosition().x > 620) && (character.getPosition().y < 30))
		{
			if(GlobalSetting.LevelControl == 0){
				System.out.println("________________________________________________________________________");
				System.out.print(" AI Mode: "+GlobalSetting.AIMode);
				System.out.print(", Reduce health per attack: "+GlobalSetting.minusPoint );
				System.out.print(", Safespot: "+GlobalSetting.numberOfSafeSpot );
				System.out.println(", Bot mode: "+GlobalSetting.botMode );	
				System.out.print(" Bot vision angle: "+Math.toDegrees(GlobalSetting.maxVisionAngle) );
				System.out.println(", Bot vision range: "+GlobalSetting.maxShootRange );		
			}
			String time = gameTime.computeTime( gameTime.getTimeDiffer());
			System.out.print(" Finish level " + GlobalSetting.LevelControl +" in " +time+ " sec");
			System.out.println(", Remaining health "+(GlobalSetting.characterLives*GlobalSetting.characterMaxHealth +  GlobalSetting.characterHealthPoints) );
			System.out.println("________________________________________________________________________");
			GlobalSetting.LevelControl = (GlobalSetting.LevelControl+1)%GlobalSetting.LevelNumber;
			//GlobalSetting.LevelControl = GlobalSetting.LevelControl +1;
			GlobalSetting.numberOfbots = GlobalSetting.numberOfbots+(1+GlobalSetting.LevelControl);
			GlobalSetting.initialBot();
			InitilizeAll();
			gameTime.setStartTime();
			if(GlobalSetting.LevelControl == 0 && winCheck == false){
				winCheck = true;
			}
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
		else if(winCheck == true){
			image(gameWin,0, 0, width, height);

			return;
		}

		
		if (GlobalSetting.playerAIEnable && playerDecisionTimer.checkTimeSlot(100))
		{
			characterSeekPosition = character.getSeekPosition(Bot, OperK);
			character.Seek(characterSeekPosition);
		}
		
		
		// fix this part to ensure there is 
		boolean checkGunExist = false;
		int checkGunIter = 0;
		while(checkGunIter < GlobalSetting.numberOfbots){
			if(GlobalSetting.haveGun[checkGunIter] == true){
				checkGunExist = true;
				break;
			}
			checkGunIter++;
		}
		
		if(checkGunExist == true){
			// at least wee have a gun in game
		}
		else{
			//int assignGun = 0;
			int assignGun = (int) (Math.random()*GlobalSetting.numberOfbots);
			GlobalSetting.haveGun[assignGun] = true;
		}
		
		checkWinningCondition();
		
		background(backgroundColor.getR(), backgroundColor.getG(), backgroundColor.getB());
		
		image(LevelBack[GlobalSetting.LevelControl],0,0);
	
		character.updatePosition(character.getK().getPosition());
		character.updateOrientation(character.getK().getOrientation());		
		
		for(int i = 0; i < NumberOfBots; i++){
			Bot[i].updatePosition(Bot[i].getK().getPosition());
			Bot[i].updateOrientation(Bot[i].getK().getOrientation());
			keepSeekFlag[i] = false;
		}
		

		//Gathering dots
		if(playerDecisionTimer.checkTimeSlot(100)){
			//For testing safe spots
			character.Move(upMove, downMove, leftMove, rightMove);
			upMove = 0;
			downMove = 0;
			leftMove = 0;
			rightMove = 0;
		}
		
		boolean[] checkPlayer;
		checkPlayer = new boolean[NumberOfBots];
		
		boolean[] askWeapon;
		askWeapon = new boolean[NumberOfBots];

		for(int botIter = 0; botIter < NumberOfBots; botIter++){
			checkPlayer[botIter] = false;
			botVision = Bot[botIter].getVisionRangeNodes(G, OperK, graphGenerator, character); 
			checkPlayer[botIter] = botVision.isCharacterInVision(character, Bot[botIter], OperK);
			
			if(checkPlayer[botIter] == true){
				//save distance to  player
				GlobalSetting.distance2Player[botIter]  = OperK.getDisBy2Points(character.getPosition(), Bot[botIter].getPosition());
				pushMatrix();
				fill(0, 255, 125, 125);
				ellipse(character.getPosition().x, character.getPosition().y, 200, 200);
				popMatrix();
			}
			else{
				GlobalSetting.distance2Player[botIter] = Float.POSITIVE_INFINITY;
			}

			//================Decision Tree=================================================================================

			boolean inShootingRange = false;
			boolean isCloset = false;
			boolean haveWeapon  = false;
			boolean inSafeSpot = false;
			boolean receiveRequest  = false; //when to switch back to no receive?
			
			// when bot is seeking
			if(Bot[botIter].checkSeekMode() == true){
				if ( checkPlayer[botIter] == true){
					//System.out.println("is Seek---System: bot "+botIter+" is seeking and sees the player right now!");
					//1. seek mode- purse
					//Bot[botIter].isSeekMode();
					Vector2 myprediction = pursue.makeUnitTimePrediction(GlobalSetting.pastPosition[botIter], character.getPosition(), Bot[botIter].getPosition());
					GlobalSetting.predictions.setMyPrediction(botIter, myprediction);
					GlobalSetting.pastPosition[botIter]  = character.getPosition();

					//System.out.println("is Seek---player now in ("+ character.getPosition().x+", "+character.getPosition().y+")");
					//System.out.println("is Seek---bot "+botIter+" predict player in (" + GlobalSetting.predictions.getMyPrediction(botIter).x +", " +GlobalSetting.predictions.getMyPrediction(botIter).y+")");
					
					//Seek the prediction
					for(int requestIter = 0 ; requestIter < NumberOfBots; requestIter ++){
						//System.out.println("is Seek---bot "+botIter+ " update bot "+requestIter+" prediction");
						Bot[botIter].clearWander();
						Bot[requestIter].isSeekMode();
						GlobalSetting.predictions.setMyPrediction(requestIter, myprediction);
					}
					//Bot[botIter].Seek(myprediction);
					
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
					inShootingRange = checkShoot(character.getMyPrediction(), Bot[botIter].getPosition());
					if( inShootingRange == true ){
						//System.out.println("Bot " + botIter + " should shoot player");
						if(checkClosestBot() == botIter){
							isCloset = true;
						}
						else{
							isCloset = false;
							
						}
						if( isCloset == true ){
							//Bot[botIter].shapeColor = new ColorVectorRGB(0, 0, 0);
							if( GlobalSetting.haveGun[botIter] == true){
								//shoot();
								
							}
							else{//don't have weapon
								//requestWeapon();
								for(int gunIter = 0 ; gunIter < GlobalSetting.numberOfbots ; gunIter++){
									askWeapon[gunIter] = false;
								}
								askWeapon[botIter] = true;
							}
						}
						else{//not the closest
							Bot[botIter].clearWander();
							Bot[botIter].isSeekMode();
							//---Seek.updateTargetPosition(Bot[1].getMyPrediction());
						}
							
					}
					else{//not in shooting range
						//Bot[botIter].shapeColor = new ColorVectorRGB(255, 255, 0);

						Bot[botIter].clearWander();
						Bot[botIter].isSeekMode();
						//----Seek.updateTargetPosition(Bot[1].getMyPrediction());
					}
					
				}
				else{//no player around
					//wander;
					Bot[botIter].isWanderMode();

					if( inSafeSpot == true ){
						//wander;
						Bot[botIter].isWanderMode();
					}
					else{//go to prediction from other bots
						//Bot[botIter].isSeekMode();
						//System.out.println("is Seek---bot "+ botIter+ " is seeking, but have NO!!! target");
						
						Vector2 oldPrediction = GlobalSetting.predictions.getMyPrediction(botIter);
						Vector2 myprediction = new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
						int checkPredictionIter = 0;
						
						//if there is new prediction
						while(checkPredictionIter < NumberOfBots){
							if(checkPredictionIter != botIter){
								// use other's prediction
								if(GlobalSetting.predictions.getMyPrediction(checkPredictionIter).x >= 0 &&GlobalSetting.predictions.getMyPrediction(checkPredictionIter).y >= 0){
									//some one have prediction, use it
									myprediction = GlobalSetting.predictions.getMyPrediction(checkPredictionIter);
									//GlobalSetting.predictions.setMyPrediction(botIter, myprediction);
									//also use others' recorded past position
									//System.out.println("is Seek---Bot " + botIter + " Get prediction from bot" + checkPredictionIter +" , ("+myprediction.x +", "+myprediction.y+")");
									break;
								}
							}
							checkPredictionIter++;
						}
						
						if(myprediction.x <= 0 && myprediction.y <= 0){
							//no one have prediction
							if(OperK.getDisBy2Points(Bot[botIter].getPosition(), oldPrediction)<5){
								Bot[botIter].isWanderMode();
								//System.out.println("is Seek---Change from seeking to wander because no prediction, and have reached old prediction");
								GlobalSetting.predictions.setMyPrediction(botIter, new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
								GlobalSetting.pastPosition[botIter] =  new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);								//Bot[botIter].Wander();
							}
							else{
								keepSeekFlag[botIter] = true;
								if(wanderCount[botIter] <= 50){
									Bot[botIter].clearWander();
									Bot[botIter].isSeekMode();
									//System.out.println("is Seek---keep seek old prediction, and have not reached old prediction");
								}
								else{
									keepSeekFlag[botIter] = false;
									Bot[botIter].isWanderMode();
									GlobalSetting.predictions.setMyPrediction(botIter, new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
									GlobalSetting.pastPosition[botIter] =  new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);											
									//System.out.println("seek too much time, maybe trapped, go wander");
								}
								//wanderCount = (wanderCount +1)%100;
							}

							//Bot[botIter].isWanderMode();
							//Bot[botIter].Wander();
						}
						else{
							Bot[botIter].clearWander();
							if(OperK.getDisBy2Points(Bot[botIter].getPosition(), myprediction)<5){
								Bot[botIter].isWanderMode();
								//System.out.println("is Seek---Have reached old prediction, start wandering");
								GlobalSetting.predictions.setMyPrediction(botIter, new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
								GlobalSetting.pastPosition[botIter] =  new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
								//Bot[botIter].Wander();
							}
							else{
								Bot[botIter].clearWander();
								Bot[botIter].isSeekMode();
								GlobalSetting.predictions.setMyPrediction(botIter, myprediction);
								GlobalSetting.pastPosition[botIter] = GlobalSetting.pastPosition[checkPredictionIter];
								//System.out.println("is Seek---Have not reached old prediction");
								
								keepSeekFlag[botIter] = true;
								if(wanderCount[botIter] <= 50){
									Bot[botIter].clearWander();
									Bot[botIter].isSeekMode();
									//System.out.println("**is Seek---keep seek old prediction, and have not reached old prediction");
								}
								else{
									keepSeekFlag[botIter] = false;
									Bot[botIter].isWanderMode();
									GlobalSetting.predictions.setMyPrediction(botIter, new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
									GlobalSetting.pastPosition[botIter] =  new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);											
									//System.out.println("** seek too much time, maybe trapped, go wander");
								}
							}

							//Bot[botIter].Seek(myprediction);
						}
						
						
						
						//---Seek.updateTargetPosition(Bot[1].getMyPrediction());
					}


				}
			}
			else{
				//wandering Mode
				//Bot[botIter].Wander();
				//Bot[botIter].Seek(new Vector2(100, 100));
				if ( checkPlayer[botIter] == true){
					//System.out.println("System: bot "+botIter+" sees the player right now!");
					Bot[botIter].clearWander();
					Bot[botIter].isSeekMode();
					//no past position
					Vector2 myprediction = pursue.makeUnitTimePrediction(new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY), character.getPosition(), Bot[botIter].getPosition());
					GlobalSetting.predictions.setMyPrediction(botIter, myprediction);
					//save past position
					GlobalSetting.pastPosition[botIter] = character.getPosition();
					//System.out.println("bot "+botIter+" predict player in (" + GlobalSetting.predictions.getMyPrediction(botIter).x +", " +GlobalSetting.predictions.getMyPrediction(botIter).y+")");
					//send request to other bots
					for(int requestIter = 0 ; requestIter < NumberOfBots; requestIter ++){
						//System.out.println("bot "+botIter+ " request bot "+requestIter+" join seek");
						Bot[botIter].clearWander();
						Bot[requestIter].isSeekMode();
						GlobalSetting.predictions.setMyPrediction(requestIter, myprediction);
					}
					//---Bot[1].updateMyPrediction(pursue.makePrediction(targetPastPosition, targetCurrentPosition, selfCurrentPosition));
					//---Seek.updateTargetPosition(Bot[1].getMyPrediction());			
				}
				else{//no player around
					//if ( character.checkRequest() == true ){
					//System.out.println("bot "+botIter+ " didn't see player");
					Vector2 myprediction = new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
					if(myprediction.x >=0 && myprediction.y >= 0){
						// prediction updated by other's data
						Bot[botIter].clearWander();
						Bot[botIter].isSeekMode();
					}
					else{
						Bot[botIter].isWanderMode();
						GlobalSetting.predictions.setMyPrediction(botIter, new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
						GlobalSetting.pastPosition[botIter] =  new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);						
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
		}

		
		//make decisions in 0.02 sec frequency
		if(botDecisionTimer.checkTimeSlot(100)){
			//bot decision cycle
			count = (count +1)%100;
			//For testing safe spots
			int[] otherbots;//
			//--------------
			otherbots = new int[NumberOfBots];
			
			CommonFunction.activateSafeSpot(count, 0, 100);
			for(int botIter = 0 ; botIter < NumberOfBots; botIter++){

				//Bot[botIter].Wander();
				//put decision tree in here, since there is not multi-thread
				//first test (reduce health)
				
				//
				if(keepSeekFlag[botIter] == true){
					wanderCount[botIter] = (wanderCount[botIter]+1)%100;
				}
				else{
					wanderCount[botIter] = 0;
				}
				if(checkPlayer[botIter] == true){
					//change bot color
					
					if(GlobalSetting.haveGun[botIter] == true){
						Bot[botIter].shapeColor = new ColorVectorRGB(255,0,0); 
						if(GlobalSetting.characterHealthPoints >= 0 ){
							GlobalSetting.characterHealthPoints = GlobalSetting.characterHealthPoints - GlobalSetting.minusPoint;
							if(GlobalSetting.characterHealthPoints <= 0 ){
								GlobalSetting.characterHealthPoints = 0;
								if(GlobalSetting.characterLives > 0){
									GlobalSetting.characterLives = GlobalSetting.characterLives -1;
									GlobalSetting.characterHealthPoints = GlobalSetting.characterMaxHealth;
									//character.updatePosition(new Vector2(200, 500));
									//re born to original points.
								}
								else{
									System.out.println("Game Over!!");
									isGameOver = true;
									//After game over do something
								}
							}
						}
						//Bot[botIter].shapeColor = new ColorVectorRGB(255,0,0); 
						//System.out.println("Bot " + botIter + " attack player " + GlobalSetting.minusPoint + " points");
					}
					else{
						//System.out.println("Bot " + botIter + " ask for weapon!");
						Bot[botIter].shapeColor = new ColorVectorRGB(0,0,255); 
						for(int gunIter = 0; gunIter < GlobalSetting.numberOfbots ; gunIter++){
							if(askWeapon[gunIter] == true){
								Bot[gunIter].shapeColor = new ColorVectorRGB(255,0,0); 
								GlobalSetting.haveGun[gunIter] = true;
								//askWeapon[gunIter] = false;
							}
							else{
								Bot[gunIter].shapeColor = new ColorVectorRGB(0,0,255); 
								GlobalSetting.haveGun[gunIter] = false;
							}
						}
						//Bot[botIter].shapeColor = new ColorVectorRGB(0,0,255); 
					}
				}
				else{
					if(GlobalSetting.haveGun[botIter] == true){
						Bot[botIter].shapeColor = new ColorVectorRGB(255,114,94); 
					}
					else{
						Bot[botIter].shapeColor = new ColorVectorRGB(94,181,255);
					}					
				}
			
				
				if(Bot[botIter].checkSeekMode()){
					Bot[botIter].Seek(GlobalSetting.predictions.getMyPrediction(botIter));
					GlobalSetting.distance2Player[botIter]  = OperK.getDisBy2Points(character.getPosition(), Bot[botIter].getPosition());

					if(GlobalSetting.haveGun[botIter] == true){
						Bot[botIter].shapeColor = new ColorVectorRGB(255,0,0); 
					}
					else{
						Bot[botIter].shapeColor = new ColorVectorRGB(0,0,255);
					}
				}
				else{
					GlobalSetting.distance2Player[botIter] = Float.POSITIVE_INFINITY;
					Bot[botIter].Wander();
					if(GlobalSetting.haveGun[botIter] == true){
						Bot[botIter].shapeColor = new ColorVectorRGB(255,114,94); 
					}
					else{
						Bot[botIter].shapeColor = new ColorVectorRGB(94,181,255);
					}						
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
		PublicGraph.graphGenerator.edgeDraw();
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
		


		List<Vector2> charaSafe = character.getSafeSpot();
		for(int safeIter = 0; safeIter < charaSafe.size(); safeIter++){
			pushMatrix();
			fill(255);
/*			
			ellipse((PublicGraph.graphGenerator.SafeSpotsList.get(safeIter).ObstacleMax.x +PublicGraph.graphGenerator.SafeSpotsList.get(safeIter).ObstacleMin.x)/2,
					(PublicGraph.graphGenerator.SafeSpotsList.get(safeIter).ObstacleMax.y +PublicGraph.graphGenerator.SafeSpotsList.get(safeIter).ObstacleMin.y)/2, 10, 10);
*/
			ellipse(charaSafe.get(safeIter).x, charaSafe.get(safeIter).y, 10, 10);
			popMatrix();
		}
		pushMatrix();
		fill(0);
		ellipse(characterSeekPosition.x, characterSeekPosition.y, 10, 10);
		popMatrix();

		
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
		NumberOfBots = GlobalSetting.numberOfbots;		
		wanderCount = new int[GlobalSetting.numberOfbots];
		keepSeekFlag = new boolean[GlobalSetting.numberOfbots]; 

		for(int i = 0 ; i < GlobalSetting.numberOfbots; i++){
			wanderCount[i] = 0;
			keepSeekFlag[i] = false;
		}

		
		characterSeekPosition = new Vector2(631, 19);
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
		

		Bot = new CharacterHuman[NumberOfBots];
		//botsTargetQueue = new ArrayList<Integer>[NumberOfBots]();

		//prediction------------------------------------------------------------------------------------------		
		//test Prediction is OK
		//Vector2 a = new Vector2(20, 10);
		//character.updateMyPrediction(a);
		//System.out.println(character.getMyPrediction().getX());
		//End of prediction------------------------------------------------------------------------------------

				
		
		for(int i = 0; i<NumberOfBots ;i ++ ){
			Vector2 botPosition;
			
			if(GlobalSetting.botMode == 0){
				botPosition = new Vector2((float)Math.random()*(windowWidth-100)+50 , (float)Math.random()*(windowHeight-100)+50);
				botPosition = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, botPosition)).coordinate;
			}
			else if(GlobalSetting.botMode == 1){
				botPosition = new Vector2((float)Math.random()*(GlobalSetting.screenWidth/GlobalSetting.numberOfbots-50)+GlobalSetting.screenWidth/GlobalSetting.numberOfbots*i, (float)Math.random()*(GlobalSetting.screenHeight-100)+50);
				botPosition = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, botPosition)).coordinate;

			}
			else{
				botPosition = new Vector2((float)Math.random()*(windowWidth-100)+50 , (float)Math.random()*(windowHeight-100)+50);
				botPosition = PublicGraph.G.nodeList.get(CommonFunction.findClose(PublicGraph.G.nodeList, botPosition)).coordinate;
			}
/*
			while(PublicGraph.graphGenerator.ObsOverlapList.get(CommonFunction.findClose(currentNodeList, botPosition))==1){
				botPosition = new Vector2((float)Math.random()*(windowWidth-00)+50 , (float)Math.random()*(windowHeight-100)+50);
			}
*/			ColorVectorRGB botTempColor;
			if(GlobalSetting.haveGun[i] == true){
				botTempColor = new ColorVectorRGB(255,114,94); 
			}
			else{
				botTempColor = new ColorVectorRGB(94,181,255);
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
					//new ColorVectorRGB((float)Math.random()*255, (float)Math.random()*255, (float)Math.random()*255),
					//new ColorVectorRGB((float)255*(i%2), (float)255*(i%2), (float)255*(i%2)),
					botTempColor,
					backgroundColor,
					GlobalSetting.numberOfBread,
					Sys,
					i
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
	public void SystemLog(String input){
		pushMatrix();
		//text(50, 50, input);
		popMatrix();
	}
	public boolean checkShoot(Vector2 playerPosition, Vector2 botPosition){
		float distance;
		distance = OperK.getDisBy2Points(playerPosition, botPosition);
		if(distance <= GlobalSetting.maxShootRange){
			return true;
		}
		else{
			return false;
		}
	}
	public int checkClosestBot(){
		int currentClose = 0;
		float currentDis = Float.POSITIVE_INFINITY;
		for(int i = 0; i < GlobalSetting.numberOfbots; i++){
			if(i == 0){
				currentClose = 0;
				currentDis = GlobalSetting.distance2Player[i];
			}
			if(GlobalSetting.distance2Player[i] < currentDis){
				currentClose = i;
				currentDis = GlobalSetting.distance2Player[i];
				
			}
		}
		return currentClose;
	}


}

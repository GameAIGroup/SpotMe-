package Variables;

import BasicStructures.Vector2;

public class GlobalSetting {
	public static int screenWidth;
	public static int screenHeight;

	public static int HeuristicMode;

	public static int numberOfBread;
	public static int tileNumber;
	public static int nodeSize;
	public static int obstacleMargin;
	
	public static double maxVisionAngle;
	public static float maxVisionRange;
	public static float maxShootRange;

	
	public static int numberOfbots;
	
	public static int wanderTimeBound;
	
	public static int keyMoveDistance;
	
	public static int sizeOfSafeSpot;
	public static int numberOfSafeSpot;
	
	public static int characterHealthPoints;
	public static int characterMaxHealth;
	public static int deductionPerShot;

	public static int characterLives;
	
	//prediction------------------------------------------------------------------------------------------
	public static Predictions predictions;
	public static Vector2[] pastPosition;
	//End of prediction------------------------------------------------------------------------------------
	
	public static int LevelControl;
	public static int LevelNumber; 
	
	public static boolean playerAIEnable;
	public static int characterRadius;
	
	public static int minusPoint;
	public static boolean[] haveGun;
	public static float[] distance2Player;

	public static int botMode;
	public static int AIMode;
	public static int fleeDistance;
	
	public GlobalSetting(){
		screenWidth = 800;
		screenHeight = 600;
		
		HeuristicMode = 1;

		numberOfBread = 1;
		
		numberOfbots = 2;
		wanderTimeBound = 20;
		
		
		//weapon
		minusPoint =  15;

		initialBot();
		//End of prediction------------------------------------------------------------------------------------
		
		keyMoveDistance = 10;
		
		sizeOfSafeSpot = 50;
		numberOfSafeSpot = 5;
		
		characterHealthPoints = 200;
		characterMaxHealth = 200;
		deductionPerShot = 4;

		characterLives = 3;		
		
		LevelControl = 0;
		LevelNumber = 3;
		
		tileNumber = 50;
		nodeSize = 10;
		obstacleMargin = 3;


		maxVisionAngle = Math.toRadians(80);
		maxVisionRange = 100;
		maxShootRange = 100;

		
		//maxVisionAngle = 0.6;
		
		playerAIEnable = true;
		botMode = 1;
		AIMode = 0;
		fleeDistance = 200;
		

	}
	public static void initialBot(){
		haveGun = new boolean[numberOfbots];
		distance2Player = new float[numberOfbots];
		for(int i = 0 ; i< numberOfbots; i++){
			if(i == 0 ){
				//default weapon is in bot 0
				haveGun[i] = true;
			}
			distance2Player[i] = Float.POSITIVE_INFINITY;
 		}
		
		//prediction------------------------------------------------------------------------------------------
		predictions = new Predictions(numberOfbots);
		pastPosition = new Vector2[numberOfbots];
		for(int i = 0 ; i< numberOfbots; i++){
			pastPosition[i] = new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
 		}
	}
}

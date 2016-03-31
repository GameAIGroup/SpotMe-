package Variables;

public class GlobalSetting {
	public static int screenWidth;
	public static int screenHeight;

	public static int HeuristicMode;

	public static int numberOfBread;
	public static int tileNumber;
	public static int nodeSize;
	public static int obstacleMargin;
	public static double maxVisionAngle;
	
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
	//End of prediction------------------------------------------------------------------------------------
	
	public static int LevelControl;
	public static int LevelNumber; 
	
	public GlobalSetting(){
		screenWidth = 800;
		screenHeight = 600;
		
		HeuristicMode = 1;

		numberOfBread = 1;
		
		tileNumber = 50;
		nodeSize = 5;
		obstacleMargin = 3;
		
		numberOfbots = 3;
		wanderTimeBound = 5;
		
		
		//prediction------------------------------------------------------------------------------------------
		predictions = new Predictions(numberOfbots);
		//End of prediction------------------------------------------------------------------------------------
		
		keyMoveDistance = 10;
		
		sizeOfSafeSpot = 50;
		numberOfSafeSpot = 5;
		
		characterHealthPoints = 100;
		characterMaxHealth = 100;
		deductionPerShot = 2;

		characterLives = 3;		
		
		LevelControl = 0;
		LevelNumber = 3;
		
		tileNumber = 100;
		nodeSize = 5;
		obstacleMargin = 3;

		maxVisionAngle = 0.3;

	}
}

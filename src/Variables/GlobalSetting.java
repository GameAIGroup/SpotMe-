package Variables;

public class GlobalSetting {
	public static int screenWidth;
	public static int screenHeight;

	public static int HeuristicMode;

	public static int numberOfBread;
	public static int tileNumber;
	public static int nodeSize;
	public static int obstacleMargin;
	
	public static int numberOfbots;
	
	public static int wanderTimeBound;
	
	public static int keyMoveDistance;
	
	
	//prediction------------------------------------------------------------------------------------------
	public static Predictions predictions;
	//End of prediction------------------------------------------------------------------------------------
	
	
	public GlobalSetting(){
		screenWidth = 800;
		screenHeight = 600;
		
		HeuristicMode = 1;

		numberOfBread = 10;
		
		tileNumber = 50;
		nodeSize = 5;
		obstacleMargin = 3;
		
		numberOfbots = 3;
		wanderTimeBound = 5;
		
		
		//prediction------------------------------------------------------------------------------------------
		predictions = new Predictions(numberOfbots);
		//End of prediction------------------------------------------------------------------------------------
		
		keyMoveDistance = 10;
	}
}

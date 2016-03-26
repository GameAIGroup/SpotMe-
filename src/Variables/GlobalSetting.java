package Variables;

public class GlobalSetting {
	public static int screenWidth;
	public static int screenHeight;
	public static int testStart;
	public static int testGoal;
	public static int HeuristicMode;
	public static int generatedNode;
	public static int NeededPath;
	
	public static int wallWeight;
	public static int level2Weight;
	public static int level3Weight;
	
	public static int numberOfBread;
	public static int tileNumber;
	public static int nodeSize;
	public static int obstacleMargin;
	
	public GlobalSetting(){
		screenWidth = 800;
		screenHeight = 600;
		
		testStart = 6;
		testGoal = 10;
		
		HeuristicMode = 1;
		
		generatedNode = 1500;
		
		NeededPath = 5;
		
		wallWeight = 10000;
		level2Weight = 20000;
		level3Weight = 50000;
		
		numberOfBread = 100;
		
		tileNumber = 50;
		nodeSize = 5;
		obstacleMargin = 3;
	}
}

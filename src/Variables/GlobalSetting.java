package Variables;

public class GlobalSetting {
	public static int testStart;
	public static int testGoal;
	public static int HeuristicMode;
	public static int generatedNode;
	public static int NeededPath;
	
	public static int wallWeight;
	public static int level2Weight;
	public static int level3Weight;
	
	public static int numberOfBread;
	
	public GlobalSetting(){
		testStart = 6;
		testGoal = 10;
		
		HeuristicMode = 1;
		
		generatedNode = 1500;
		
		NeededPath = 5;
		
		wallWeight = 10000;
		level2Weight = 20000;
		level3Weight = 50000;
		
		numberOfBread = 100;
		
	}
}

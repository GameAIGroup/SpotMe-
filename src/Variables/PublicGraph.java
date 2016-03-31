package Variables;

import GraphData.GraphData;
import GraphData.GraphGenerator;
import GraphData.MapGenerator;
import MovementStructures.KinematicOperations;
import processing.core.PApplet;

public class PublicGraph {
	//for graph
	public static MapGenerator mapCreate;
	public static GraphGenerator graphGenerator; 
	public static GraphData G;
	PApplet parent;
	
	public PublicGraph(PApplet P, KinematicOperations OperK){
		//This part order cannot be changed
		
		this.parent = P;
		mapCreate = new MapGenerator(5, 7, "test.txt");
		//mapCreate.createRandomGraph("random.txt");

		mapCreate.drawDot(GlobalSetting.tileNumber, GlobalSetting.screenWidth, GlobalSetting.screenHeight);
		
		mapCreate.readTile(P);
		mapCreate.readObstacle(P, 1);
		//mapCreate.isObstacle = true;
		
		graphGenerator = new GraphGenerator(mapCreate, OperK, P);
		graphGenerator.createEdge();
		
		G = new GraphData(graphGenerator.nodeList, graphGenerator.edgeList, P);			
		
		graphGenerator.recreateAllSafeSpots();
		graphGenerator.updateOverlapSafeSpots();
		
	}
}

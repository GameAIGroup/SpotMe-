package GraphData;

import BasicStructures.Vector2;

public class ObstacleArea {
	public Vector2 ObstacleMin;
	public Vector2 ObstacleMax;
	
	public ObstacleArea(float minX, float minY, float maxX, float maxY){
		ObstacleMin = new Vector2(minX, minY);
		ObstacleMax = new Vector2(maxX, maxY);
	}
}

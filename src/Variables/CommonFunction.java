package Variables;

import java.util.List;

import BasicStructures.Vector2;
import GraphData.Node;
import MovementStructures.KinematicOperations;

public class CommonFunction {
	private static KinematicOperations OperK;
	
	public CommonFunction(KinematicOperations OperK){
		this.OperK = OperK;
	}
	public static int findClose(List<Node> NodeList, Vector2 Point){
		int resultIndex = 0;
		float minDistance = 0;
		float tempDistance = 0;
		
		for(int i = 0; i< NodeList.size();i++){
			tempDistance = OperK.getDisBy2Points(NodeList.get(i).coordinate, Point);

			if(i == 0){
				minDistance = tempDistance;
				resultIndex = i;
			}
			if(tempDistance < minDistance){
				minDistance = tempDistance;
				resultIndex = i;
			}
		}
		return resultIndex;
	}	
}

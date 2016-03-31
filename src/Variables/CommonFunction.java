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
		//find non-obstacle point
		
		int check = 0;
		for(int i = 0; i< NodeList.size();i++){
			tempDistance = OperK.getDisBy2Points(NodeList.get(i).coordinate, Point);
			if(PublicGraph.graphGenerator.ObsOverlapList.get(i)==0){
				if(check == 0){
					minDistance = tempDistance;
					resultIndex = i;
					check++;
				}
				if(tempDistance < minDistance){
					minDistance = tempDistance;
					resultIndex = i;
				}
			}
			else{
/*
				if(i <= 700 && i >= 500){
					System.out.println("avoid obstacle " +i);
				}
*/
			}

		}
		return resultIndex;
	}
	public static void activateSafeSpot(int cycleCount, int countForRegenerate, int countForAddOne){
		//for test safe spot
		if(cycleCount == countForRegenerate){
			PublicGraph.graphGenerator.recreateAllSafeSpots();
			PublicGraph.graphGenerator.updateOverlapSafeSpots();
			
		}
		else if(cycleCount == countForAddOne){
			int tempIndex = (int) (Math.random()*PublicGraph.graphGenerator.SafeSpotsList.size());
			PublicGraph.graphGenerator.removeSafeSpots(tempIndex);
			PublicGraph.graphGenerator.addSafeSpots();
			PublicGraph.graphGenerator.updateOverlapSafeSpots();
		}
		
		
				
	}
}

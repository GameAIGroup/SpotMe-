package GraphAlgorithm;

import java.util.List;
import java.util.Random;

import GraphData.Edge;
import GraphData.Node;
import MovementStructures.KinematicOperations;

public class H3 implements Heuristic{

	public List<Node> nodeList;
	public List<Edge> edgeList;
	public Node start;
	public Node goal;
	public KinematicOperations operK;
	
	public H3(List<Node> NodeList, List<Edge> EdgeList, int StartIndex, int GoalIndex, KinematicOperations operK){
		this.start = NodeList.get(StartIndex);
		this.goal = NodeList.get(GoalIndex);
		this.nodeList = NodeList;
		this.edgeList = EdgeList;
		this.operK = operK;
		
	}

	@Override
	public float estimateWeight(int current) {
		float resultH;
		//resultH = operK.getDisBy2Points(goal.coordinate, nodeList.get(current).coordinate);
		Random rand = new Random();
		resultH = rand.nextInt(750) + 1;
		
		// TODO Auto-generated method stub
		//System.out.println("H1");
		return resultH;
	}

}

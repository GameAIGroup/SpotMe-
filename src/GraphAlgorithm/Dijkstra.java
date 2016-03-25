package GraphAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import GraphData.*;

public class Dijkstra {
	
	//weight = cost so far
	//public List<TwoIndexCostPair> openList;
	//public List<TwoIndexCostPair> closeList;
	public Map<Integer, TwoIndexCostPair> openList;
	public Map<Integer, TwoIndexCostPair> closeList;

	public Map<Integer, Float> map;
	
	public Node start;
	public Node goal;
	
	public boolean isFind;
	
	public List<Integer> result;
	//public List<Node>
	
	public Dijkstra(List<Node> NodeList, List<Edge> EdgeList, int StartIndex, int GoalIndex){
		this.start = NodeList.get(StartIndex);
		this.goal = NodeList.get(GoalIndex);
		
		map = new HashMap<Integer, Float>();
		result = new ArrayList<Integer>();
		//openList = new ArrayList<TwoIndexCostPair>();
		//closeList = new ArrayList<TwoIndexCostPair>();
		openList = new HashMap<Integer, TwoIndexCostPair>();
		closeList = new HashMap<Integer, TwoIndexCostPair>();
	
		TwoIndexCostPair startPair = new TwoIndexCostPair(StartIndex, -1, 0, 0);
		openList.put(StartIndex ,startPair);
		
		isFind = false;
		
	}
	public void computDijkstra(List<Node> NodeList, List<Edge> EdgeList){
		float smallestCost = 0 ;
		int smallestIndex = start.nodeIndex;
		int count;
		
		if(openList.size() >0){
			Set<Integer> set=openList.keySet();
			count = 0;
			// find the small east
			for(Object obj:set)
			{
				//openList.get(obj);
				//System.out.println(openList.get(obj).cost);
				if(count == 0){
					//first
					count ++;
					smallestCost = openList.get(obj).cost;
					smallestIndex = openList.get(obj).index;
				}
				else{
					if(openList.get(obj).cost < smallestCost){
						smallestCost = openList.get(obj).cost;
						smallestIndex = openList.get(obj).index;
					}
				}
				
			}
			//System.out.println(smallestIndex);
			
			TwoIndexCostPair tempPair;			
			if(smallestIndex == goal.nodeIndex){
				// goal has lowest cost, finish
				tempPair = openList.get(smallestIndex);
				closeList.put(smallestIndex, tempPair);
				openList.remove(smallestIndex);
				
				int pathIndex = smallestIndex;
				//System.out.print("\r\nPath:   ");
				while(pathIndex != start.nodeIndex){
					//System.out.print(pathIndex +"  ");
					result.add(pathIndex);
					pathIndex = closeList.get(pathIndex).previousIndex;
				}
				//System.out.print(pathIndex +"  ");
				result.add(pathIndex);
				
				//System.out.println("end!!");
				isFind = true;
				openList.clear();
			}
			else{
				//add adj into openList
				float newWeight;
				for(int i = 0; i< NodeList.get(smallestIndex).adj.size(); i++){
					 newWeight = smallestCost + NodeList.get(smallestIndex).adj.get(i).weight;

					if(closeList.get(NodeList.get(smallestIndex).adj.get(i).index) != null){
						// new node is closed
					}
					else if(
							
						openList.get(NodeList.get(smallestIndex).adj.get(i).index)!= null && 
						openList.get(NodeList.get(smallestIndex).adj.get(i).index).cost < 
						newWeight
					){
						// in open list but with better cost
					
					}
					else{
						tempPair = new TwoIndexCostPair(NodeList.get(smallestIndex).adj.get(i).index, smallestIndex, newWeight, 0);
						openList.put(NodeList.get(smallestIndex).adj.get(i).index, tempPair);
						//System.out.println("add " + NodeList.get(smallestIndex).adj.get(i).index +", " +newWeight);
					}
				}
				
				//finish adding adj put this to close list
				tempPair = openList.get(smallestIndex);
				closeList.put(smallestIndex, tempPair);
				openList.remove(smallestIndex);
				
			}
			
		}
		else{
			if(isFind == false){
				//System.out.println("End node never show up");
			}
		}
	}
}

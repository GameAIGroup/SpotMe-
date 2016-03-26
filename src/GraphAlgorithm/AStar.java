package GraphAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import GraphData.Edge;
import GraphData.Node;
import GraphData.TwoIndexCostPair;

public class AStar {
	public Map<Integer, TwoIndexCostPair> openList;
	public Map<Integer, TwoIndexCostPair> closeList;

	public Map<Integer, Float> map;
	
	public Node start;
	public Node goal;
	
	public boolean isFind;
	
	public List<Integer> result;
	
	public Heuristic H; 
	
	
	public AStar(Heuristic H, List<Node> NodeList, List<Edge> EdgeList, int StartIndex, int GoalIndex){
		this.start = NodeList.get(StartIndex);
		this.goal = NodeList.get(GoalIndex);
		
		this.H = H;
		
		map = new HashMap<Integer, Float>();
		result = new ArrayList<Integer>();
		//openList = new ArrayList<TwoIndexCostPair>();
		//closeList = new ArrayList<TwoIndexCostPair>();
		openList = new HashMap<Integer, TwoIndexCostPair>();
		closeList = new HashMap<Integer, TwoIndexCostPair>();
	
		float startH = H.estimateWeight(start.nodeIndex); 
		TwoIndexCostPair startPair = new TwoIndexCostPair(StartIndex, -1, 0, startH);
		openList.put(StartIndex ,startPair);
		
		isFind = false;
			
	}
	
	public void computeAStar(List<Node> NodeList, List<Edge> EdgeList){
		float smallestCost = 0 ;
		float smallestH = 0;
		float smallestETC =0;
		int smallestIndex = start.nodeIndex;

		int count;
		
		//H.estimateWeight();
		if(openList.size() >0){
			Set<Integer> set=openList.keySet();
			count = 0;
			// find the small east
			for(Object obj:set)
			{
				//openList.get(obj);
				//System.out.println("\r\n**" + openList.size());
				//System.out.println(smallestIndex +"** " + smallestH +", " + smallestCost +", " +smallestETC);
				if(count == 0){
					//first
					count ++;
					smallestCost = openList.get(obj).cost;
					smallestH = openList.get(obj).h;
					smallestETC = openList.get(obj).etc;
					smallestIndex = openList.get(obj).index;

				}
				else{
					//compare h
					if(openList.get(obj).etc < smallestETC){
						//System.out.println("**" + openList.get(obj).cost);
						smallestH = openList.get(obj).h;
						smallestCost = openList.get(obj).cost;
						smallestETC = openList.get(obj).etc;
						smallestIndex = openList.get(obj).index;
					}
				}
				
			}
			
			
			//System.out.println("\r\n" +smallestIndex);
			//System.out.println("\r\n" + smallestIndex +"** " + smallestH +", " + smallestCost +", " +smallestETC);
			
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
				float costSoFar;
				float tempH;
				float estimateCost;
				for(int i = 0; i< NodeList.get(smallestIndex).adj.size(); i++){
					tempH = H.estimateWeight(NodeList.get(smallestIndex).adj.get(i).index); 
					costSoFar = smallestCost + NodeList.get(smallestIndex).adj.get(i).weight;
					estimateCost = tempH + costSoFar;
					
					//System.out.println(NodeList.get(smallestIndex).adj.get(i).index +"-- " + tempH +", " + costSoFar +", " +estimateCost);
					
					if(closeList.get(NodeList.get(smallestIndex).adj.get(i).index)!=null){
						// in close list, skip or remove form close
						if(closeList.get(NodeList.get(smallestIndex).adj.get(i).index).cost <= estimateCost){
							// if no shorter route
						}
						else{
							//remove from close list
							//tempPair = closeList.get(NodeList.get(smallestIndex).adj.get(i).index);
							closeList.remove(NodeList.get(smallestIndex).adj.get(i).index);							
							
							//new h
							tempH = H.estimateWeight(NodeList.get(smallestIndex).adj.get(i).index); 
							costSoFar = smallestCost + NodeList.get(smallestIndex).adj.get(i).weight;
							estimateCost = tempH + costSoFar;
							
							tempPair = new TwoIndexCostPair(NodeList.get(smallestIndex).adj.get(i).index, smallestIndex, costSoFar, tempH);
							openList.put(NodeList.get(smallestIndex).adj.get(i).index, tempPair);

						}
						
					}
					else if(openList.get(NodeList.get(smallestIndex).adj.get(i).index) != null){
						// in open list
						if(openList.get(NodeList.get(smallestIndex).adj.get(i).index).cost <= estimateCost){
							// if no shorter route
						}
						else{
							//remove from close list
							//new h
							tempH = H.estimateWeight(NodeList.get(smallestIndex).adj.get(i).index); 
							costSoFar = smallestCost + NodeList.get(smallestIndex).adj.get(i).weight;
							estimateCost = tempH + costSoFar;

							tempPair = new TwoIndexCostPair(NodeList.get(smallestIndex).adj.get(i).index, smallestIndex, costSoFar, tempH);
							openList.put(NodeList.get(smallestIndex).adj.get(i).index, tempPair);

						}
						
					}
					else{
						// never visited
						tempH = H.estimateWeight(NodeList.get(smallestIndex).adj.get(i).index); 
						costSoFar = smallestCost + NodeList.get(smallestIndex).adj.get(i).weight;
						estimateCost = tempH + costSoFar;

						tempPair = new TwoIndexCostPair(NodeList.get(smallestIndex).adj.get(i).index, smallestIndex, costSoFar, tempH);
						openList.put(NodeList.get(smallestIndex).adj.get(i).index, tempPair);
						//System.out.println(tempPair.index +"-- " + tempPair.h +", " + tempPair.cost +", " +tempPair.etc);

						
					}
					
					
				}
				
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

package GraphData;

import java.util.*;

import BasicStructures.*;

public class Node {
	public int nodeIndex;
	public Vector2 coordinate;
	public List<Edge> edge;
	public List<IndexWeightPair> adj;
	public double changeInOr;
	public double distanceFromBot;
	
	public Node(int Index, float CoordinateX, float CoordinateY){
		edge = new ArrayList<Edge>();
		adj = new ArrayList<IndexWeightPair>();
		this.nodeIndex = Index;
		coordinate = new Vector2(CoordinateX, CoordinateY);
	}
	public void addNeighbor(Edge e){
		Edge tempe = new Edge(e.upIndex, e.downIndex, e.weight);
		edge.add(e);
	}
	public void addAdj(int Index, float W){
		IndexWeightPair tempW = new IndexWeightPair(Index, W);
		adj.add(tempW);
	}
	
	public void removeNeighbor(int adjIndex){
		edge.remove(adjIndex);
	}
}

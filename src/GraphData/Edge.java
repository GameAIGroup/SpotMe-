package GraphData;

public class Edge {
	public int upIndex;
	public int downIndex;
	public float weight;
	
	public Edge(int fn, int bn, float Weight){
		upIndex = fn;
	    downIndex = bn;
	    this.weight = Weight;
	}
	public void updateWeight(float Weight){
		this.weight = Weight;
	}
}

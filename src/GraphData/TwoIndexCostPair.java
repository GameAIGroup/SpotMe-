package GraphData;

public class TwoIndexCostPair {
	public int index;
	public int previousIndex;
	
	public float cost;
	public float h;
	public float etc;
	
	public TwoIndexCostPair(int Index, int PreviousIndex, float Cost, float heuristic){
		this.index = Index;
		this.previousIndex = PreviousIndex;
		this.cost = Cost;
		this.h = heuristic;
		etc = cost+h;
	}
	
	public void updateCost(float Cost){
		this.cost = Cost;
		etc = Cost +h;
	}
	public void updatePre(int PreIndex){
		this.previousIndex = PreIndex;
	}
	
}

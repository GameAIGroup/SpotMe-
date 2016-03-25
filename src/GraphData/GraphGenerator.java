package GraphData;

import java.util.*;
import BasicStructures.*;
import MovementStructures.*;
import Variables.GlobalSetting;
import processing.core.PApplet;

public class GraphGenerator {
	public List<Vector2> nodeList;
	public int numberOfTarget;
	public int numberOfRoad;
	
	public int wall;
	public int level2;
	public int level3;
	public KinematicOperations operK;
	public List<Edge> edgeList;
	public List<Vector2> obsList;
	public List<Float> weightList;
	PApplet parent;
	public int[] obsCheck;
	public boolean isDifferent;
	
	
	public GraphGenerator(MapGenerator Map, KinematicOperations operK, PApplet P){
		this.operK = operK;
		this.parent = P;
		
		edgeList = new ArrayList<Edge>();
		weightList = new ArrayList<Float>();
		obsList = new ArrayList<Vector2>();
		
		numberOfTarget = Map.targetNode.size();
		numberOfRoad = Map.roadNode.size();
		wall = Map.level1.size();
		level2 = Map.level2.size();
		level3 = Map.level3.size();
		
		nodeList = new ArrayList<Vector2>();
		nodeList.addAll(Map.targetNode);
		nodeList.addAll(Map.roadNode);
/*
		for(int i= 0; i< nodeList.size(); i++){
			System.out.println(nodeList.get(i).x +", " +nodeList.get(i).y);
		}
*/		
		obsList.addAll(Map.level1);
		obsList.addAll(Map.level2);
		obsList.addAll(Map.level3);
		
		//System.out.println( weightList.size());
	}
	
	public void createEdge(){
		//for target link to 3 nearest
		//float[] tempDis = new float[numberOfTarget];
		List<Integer> smallIndex = new ArrayList<Integer>();
		List<Float> smallDis = new ArrayList<Float>();
		int numberLine = GlobalSetting.NeededPath;
		 
		for(int i=0; i < nodeList.size(); i++){
			for(int j = 0; j < nodeList.size(); j++){
				if(i!=j){
					float tempDis = operK.getDisBy2Points(nodeList.get(i), nodeList.get(j));

					//if(tempDis <200){
						if(smallIndex.size() <numberLine){
							smallIndex.add(j);
							smallDis.add(tempDis);
						}
						else{
							int k = 0;
							while(k<numberLine){
								if(smallDis.get(k)> tempDis){
									smallDis.remove(k);
									smallIndex.remove(k);

									smallIndex.add(j);
									smallDis.add(tempDis);
									break;
								}
								k++;
							}
						}
					//}
				}
			}
			//System.out.println(smallIndex.size());
			
			for(int k = 0; k <numberLine; k++){
				if(i < smallIndex.get(k)){
					edgeList.add(new Edge(i, smallIndex.get(k), 0));
				}
				else{
					edgeList.add(new Edge( smallIndex.get(k), i, 0));
				}
			}
			
			smallIndex.clear();
			smallDis.clear();
			
		}//end out for

		//System.out.println("before: "+ edgeList.size());
		//remove duplicate
		HashSet h  =   new  HashSet(edgeList);
		edgeList.clear();
		edgeList.addAll(h);
/*		
		for(int e =0; e< edgeList.size(); e++){
			System.out.println(edgeList.get(e).upIndex +", "+ edgeList.get(e).downIndex);
		}
*/
		//System.out.println("after: "+ edgeList.size());
		
		//Assign weight
		obsCheck = new int[edgeList.size()];
		for(int e =0; e < edgeList.size(); e++){
			float dis;
			dis = operK.getDisBy2Points(nodeList.get(edgeList.get(e).upIndex), nodeList.get(edgeList.get(e).downIndex));
			
			weightList.add(dis);
			edgeList.get(e).updateWeight(dis);
			float disP2L;
			obsCheck[e] =0;
			int w =0;
			while(w< obsList.size()){
				disP2L = distanceP2L(nodeList.get(edgeList.get(e).upIndex), nodeList.get(edgeList.get(e).downIndex), obsList.get(w));
				if(disP2L >= 0 && disP2L <=20){
					//if(w < wall){
						obsCheck[e] =1;
						//System.out.println(edgeList.get(e).upIndex +", " +edgeList.get(e).downIndex);
						if(w < wall){
							weightList.set(e, weightList.get(e)+GlobalSetting.wallWeight);
							edgeList.get(e).updateWeight(weightList.get(e));
							break;
						}
						else if(w>= wall && w < wall+level2){
							weightList.set(e, weightList.get(e)+GlobalSetting.level2Weight);
							edgeList.get(e).updateWeight(weightList.get(e));
							break;
						}
						else if(w >= wall+level2){
							weightList.set(e, weightList.get(e)+GlobalSetting.level3Weight);
							edgeList.get(e).updateWeight(weightList.get(e));
							break;
						}
					//}
				}
				w++;
			}
		}
		
	}
	
	public void edgeDraw(){
		for(int i = 0; i < edgeList.size(); i++){
			//System.out.println(edgeList.get(i).upIndex + ", " + edgeList.get(i).downIndex+ "     " + weightList.get(i));
			
			//if(weightList.get(i)>1){
			//if(edgeList.get(i).upIndex == 8 && weightList.get(i)==1){
			if(obsCheck[i]== 0){

				parent.pushMatrix();
				parent.stroke(255, 255, 255);
				//parent.fill(255, 255, 255);
				parent.line(
						nodeList.get(edgeList.get(i).upIndex).x,
						nodeList.get(edgeList.get(i).upIndex).y,
						nodeList.get(edgeList.get(i).downIndex).x,
						nodeList.get(edgeList.get(i).downIndex).y
				);
				parent.popMatrix();
/*
				parent.pushMatrix();	
					parent.stroke(255, 255, 255);
					parent.fill(255, 255, 255);
					parent.text(
							edgeList.get(i).weight,
							(nodeList.get((edgeList.get(i).upIndex)).x + nodeList.get((edgeList.get(i).downIndex)).x)/2,
							(nodeList.get((edgeList.get(i).upIndex)).y + nodeList.get((edgeList.get(i).downIndex)).y)/2
					);
			 	parent.popMatrix();
*/				
			}
			else{
				parent.pushMatrix();
				parent.stroke(255, 0, 255);
				//parent.fill(255, 255, 255);
				parent.line(
					nodeList.get(edgeList.get(i).upIndex).x,
					nodeList.get(edgeList.get(i).upIndex).y,
					nodeList.get(edgeList.get(i).downIndex).x,
					nodeList.get(edgeList.get(i).downIndex).y
				);
				parent.popMatrix();
/*				
			 	parent.pushMatrix();	
					parent.stroke(255, 255, 0);
					parent.fill(255, 0, 255);
					parent.text(
							edgeList.get(i).weight,
							(nodeList.get((edgeList.get(i).upIndex)).x + nodeList.get((edgeList.get(i).downIndex)).x)/2,
							(nodeList.get((edgeList.get(i).upIndex)).y + nodeList.get((edgeList.get(i).downIndex)).y)/2
					);
			 	parent.popMatrix();
*/
			}
		}
		
	}
	//public void
	public float distanceP2L(Vector2 L1, Vector2 L2, Vector2 P){
		float resultDis = 0 ;
		Vector2 V1 = new Vector2(P.x - L1.x, P.y - L1.y);
		Vector2 V2 = new Vector2(L2.x - L1.x, L2.y - L1.y);
		Vector2 V3 = new Vector2(P.x - L2.x, P.y - L2.y);
		Vector2 V4 = new Vector2(L1.x - L2.x, L1.y - L2.y);

		
		float cosThata1 = (V1.x*V2.x+V1.y*V2.y)/(operK.getLengthByVector2(V1)*operK.getLengthByVector2(V2));
		float cosThata2 = (V3.x*V4.x+V3.y*V4.y)/(operK.getLengthByVector2(V3)*operK.getLengthByVector2(V4));
		
		resultDis = operK.getLengthByVector2(V1)*(float)Math.sin(Math.acos(cosThata1));
		
		if(cosThata1 <0 || cosThata2 <0){
			return -1;
		}
		else{
			return resultDis;
		}
	}
	
	public List<Vector2> getNodeList(){
		return nodeList;
	}
	public List<Edge> getEdgeList(){
		return edgeList;
	}	

}

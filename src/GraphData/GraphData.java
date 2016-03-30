package GraphData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import BasicStructures.*;
import Variables.GlobalSetting;
import processing.core.PApplet;

public class GraphData {
	public List<Vector2> nodeCoordsList;
	
	public List<Edge> edgeList;
	public List<Node> nodeList;
	public PApplet parent;
	
	
	public GraphData(List<Vector2> NodeList, List<Edge> EdgeList, PApplet P){
		this.parent = P;
		nodeCoordsList = new ArrayList<Vector2>();
		edgeList = new ArrayList<Edge>();
		nodeList = new ArrayList<Node>();
		
		nodeCoordsList.addAll(NodeList);
		edgeList.addAll(EdgeList);
		
		//jointEdge();
		jointEdge2();

		
		for(int i = 0; i< nodeCoordsList.size(); i++){
			Node tempNode = new Node(i, nodeCoordsList.get(i).x, nodeCoordsList.get(i).y);
			nodeList.add(tempNode);
			
			
			for(int j =0; j < edgeList.size(); j++){
				if(edgeList.get(j).upIndex == i){
					nodeList.get(i).addNeighbor(edgeList.get(j));
					nodeList.get(i).addAdj(edgeList.get(j).downIndex, edgeList.get(j).weight);
				}
				else if(edgeList.get(j).downIndex == i){
					nodeList.get(i).addNeighbor(edgeList.get(j));
					nodeList.get(i).addAdj(edgeList.get(j).upIndex, edgeList.get(j).weight);
				}
			}
		}
		
		
/*
		for(int i = 0; i < nodeList.size(); i++){


			System.out.println("Node:" + i );
			for(int j = 0; j< nodeList.get(i).adj.size(); j++ ){
				//System.out.print("(" +nodeList.get(i).edge.get(j).upIndex+", "+ nodeList.get(i).edge.get(j).downIndex+")  ");
				System.out.print(" "+nodeList.get(i).adj.get(j).index+", ");
				
			}
			
			System.out.println("\r\n----------");
		}
*/

	}
	public void jointEdge(){
		int[][] cleanArray;
		cleanArray = new int[nodeCoordsList.size()][nodeCoordsList.size()];
		List<Integer> removeIndex;
		removeIndex = new ArrayList<Integer>();
		HashMap<Integer, Edge> map = new HashMap<Integer, Edge>();
		
		
		for(int i = 0; i< nodeCoordsList.size(); i++){
			for(int j = 0; j< nodeCoordsList.size(); j++){
				cleanArray[i][j] = 0;
			}
		}
		
		for(int i = 0; i< edgeList.size(); i++){
			if(cleanArray[edgeList.get(i).upIndex][edgeList.get(i).downIndex] == 0 && cleanArray[edgeList.get(i).downIndex][edgeList.get(i).upIndex]==0){
				cleanArray[edgeList.get(i).upIndex][edgeList.get(i).downIndex] = 1;
				cleanArray[edgeList.get(i).downIndex][edgeList.get(i).upIndex] = 1;
			}
			else{
				//get duplicate index
				removeIndex.add(i);
			}
			map.put(i, edgeList.get(i));
		}
		
		for(int i = 0; i< removeIndex.size(); i++){
			map.remove(removeIndex.get(i));
		}
		
		edgeList.clear();
        
		int edgeIter = 0;
		for (Object key : map.keySet()) {
            //System.out.println(key + " : " + map.get(key));
			edgeList.add(map.get(key));
			//System.out.println(edgeList.get(edgeIter).upIndex +", "+edgeList.get(edgeIter).downIndex);
			
			edgeIter++;
		}
	}
	public void jointEdge2(){
		//int[][] cleanArray;
		Map<Integer, Set<Integer>> cleanMap;
		//cleanArray = new int[nodeCoordsList.size()][nodeCoordsList.size()];
		cleanMap = new HashMap<Integer, Set<Integer>>() ;

		Set<Integer> tempSet;
		
		List<Integer> removeIndex;
		removeIndex = new ArrayList<Integer>();
		HashMap<Integer, Edge> map = new HashMap<Integer, Edge>();
		
		
		
		for(int i = 0; i< edgeList.size(); i++){
			if(cleanMap.get(edgeList.get(i).upIndex) == null || cleanMap.get(edgeList.get(i).downIndex) == null){
				//System.out.println("1.   "+ edgeList.get(i).upIndex +",  " + edgeList.get(i).downIndex);
				if(cleanMap.get(edgeList.get(i).upIndex) == null){
					tempSet = new HashSet<Integer>();
					tempSet.add(edgeList.get(i).downIndex);	
					cleanMap.put(edgeList.get(i).upIndex, tempSet);
				}
				else{
					cleanMap.get(edgeList.get(i).upIndex).add(edgeList.get(i).downIndex);
				}
				
				if(cleanMap.get(edgeList.get(i).downIndex) == null){
					tempSet = new HashSet<Integer>();
					tempSet.add(edgeList.get(i).upIndex);
					cleanMap.put(edgeList.get(i).downIndex, tempSet);
				}
				else{
					cleanMap.get(edgeList.get(i).downIndex).add(edgeList.get(i).upIndex);
				}

			}
			else{
				//get duplicate index
				if(cleanMap.get(edgeList.get(i).upIndex).contains(edgeList.get(i).downIndex) == false){
					//System.out.println("2.   "+ edgeList.get(i).upIndex +",  " + edgeList.get(i).downIndex);

					// did not find
					if(cleanMap.get(edgeList.get(i).upIndex) == null){
						tempSet = new HashSet<Integer>();
						tempSet.add(edgeList.get(i).downIndex);	
						cleanMap.put(edgeList.get(i).upIndex, tempSet);
					}
					else{
						cleanMap.get(edgeList.get(i).upIndex).add(edgeList.get(i).downIndex);
					}
					
					if(cleanMap.get(edgeList.get(i).downIndex) == null){
						tempSet = new HashSet<Integer>();
						tempSet.add(edgeList.get(i).upIndex);
						cleanMap.put(edgeList.get(i).downIndex, tempSet);
					}
					else{
						cleanMap.get(edgeList.get(i).downIndex).add(edgeList.get(i).upIndex);
					}
				}
				else{
					//System.out.println("3.   "+ edgeList.get(i).upIndex +",  " + edgeList.get(i).downIndex);

					removeIndex.add(i);
				}
			}
			map.put(i, edgeList.get(i));
		}
		
		for(int i = 0; i< removeIndex.size(); i++){
			map.remove(removeIndex.get(i));
		}
		
		edgeList.clear();
        
		int edgeIter = 0;
		for (Object key : map.keySet()) {
            //System.out.println(key + " : " + map.get(key));
			edgeList.add(map.get(key));
			//System.out.println(edgeList.get(edgeIter).upIndex +", "+edgeList.get(edgeIter).downIndex);
			
			edgeIter++;
		}
	}	
	public void getAdj(int Index){
		
	}
	
	public void reduceEdge(){
		//int[][] cleanArray;
		Map<Integer, Set<Integer>> cleanMap;
		//cleanArray = new int[nodeCoordsList.size()][nodeCoordsList.size()];
		cleanMap = new HashMap<Integer, Set<Integer>>() ;

		Set<Integer> tempSet;
		
		List<Integer> removeIndex;
		removeIndex = new ArrayList<Integer>();
		HashMap<Integer, Edge> map = new HashMap<Integer, Edge>();
		
		
		
		for(int i = 0; i< edgeList.size(); i++){
			if(cleanMap.get(edgeList.get(i).upIndex) == null || cleanMap.get(edgeList.get(i).downIndex) == null){
				//System.out.println("1.   "+ edgeList.get(i).upIndex +",  " + edgeList.get(i).downIndex);
				if(cleanMap.get(edgeList.get(i).upIndex) == null){
					tempSet = new HashSet<Integer>();
					tempSet.add(edgeList.get(i).downIndex);	
					cleanMap.put(edgeList.get(i).upIndex, tempSet);
				}
				else{
					cleanMap.get(edgeList.get(i).upIndex).add(edgeList.get(i).downIndex);
				}
				
				if(cleanMap.get(edgeList.get(i).downIndex) == null){
					tempSet = new HashSet<Integer>();
					tempSet.add(edgeList.get(i).upIndex);
					cleanMap.put(edgeList.get(i).downIndex, tempSet);
				}
				else{
					cleanMap.get(edgeList.get(i).downIndex).add(edgeList.get(i).upIndex);
				}

			}
			else{
				//get duplicate index
				if(cleanMap.get(edgeList.get(i).upIndex).contains(edgeList.get(i).downIndex) == false){
					//System.out.println("2.   "+ edgeList.get(i).upIndex +",  " + edgeList.get(i).downIndex);

					// did not find
					if(cleanMap.get(edgeList.get(i).upIndex) == null){
						tempSet = new HashSet<Integer>();
						tempSet.add(edgeList.get(i).downIndex);	
						cleanMap.put(edgeList.get(i).upIndex, tempSet);
					}
					else{
						cleanMap.get(edgeList.get(i).upIndex).add(edgeList.get(i).downIndex);
					}
					
					if(cleanMap.get(edgeList.get(i).downIndex) == null){
						tempSet = new HashSet<Integer>();
						tempSet.add(edgeList.get(i).upIndex);
						cleanMap.put(edgeList.get(i).downIndex, tempSet);
					}
					else{
						cleanMap.get(edgeList.get(i).downIndex).add(edgeList.get(i).upIndex);
					}
				}
				else{
					//System.out.println("3.   "+ edgeList.get(i).upIndex +",  " + edgeList.get(i).downIndex);

					removeIndex.add(i);
				}
			}
			map.put(i, edgeList.get(i));
		}
		
		for(int i = 0; i< removeIndex.size(); i++){
			map.remove(removeIndex.get(i));
		}
		
		edgeList.clear();
        
		int edgeIter = 0;
		for (Object key : map.keySet()) {
            //System.out.println(key + " : " + map.get(key));
			edgeList.add(map.get(key));
			//System.out.println(edgeList.get(edgeIter).upIndex +", "+edgeList.get(edgeIter).downIndex);
			
			edgeIter++;
		}		
	}
}

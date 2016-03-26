package GraphData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import BasicStructures.Vector2;
import Variables.GlobalSetting;
import processing.core.PApplet;

public class MapGenerator {
	public int nodeNumber;
	public int edgeNumber;
	public int[][] check;
	public String outFile;
	public FileWriter fw2 = null;
	public FileWriter fwObstacle = null;
	
	public Vector2 previousPos;
	public List<Vector2> obstacleNode;
	public List<Vector2> targetNode;
	public List<Vector2> roadNode;
	public boolean isObstacle;
	
	public MapGenerator(int node, int edge, String FileName){
		this.nodeNumber = node;
		this.edgeNumber = edge;
		isObstacle =true;
		
		check = new int[nodeNumber][nodeNumber];
		

		try {
			fw2 = new FileWriter("Points.txt");
			fwObstacle = new FileWriter("recordObstacles.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		for(int i = 0; i < nodeNumber; i++){
			for(int j =0; j< nodeNumber; j++){
				check[i][j] = 0;
			}
		}
		
		outFile = FileName;
		previousPos = new Vector2(-1, -1);
		
		obstacleNode = new ArrayList<Vector2>();
		targetNode = new ArrayList<Vector2>();
		roadNode = new ArrayList<Vector2>();

	}
	// draw dots when mouse click
	public void markObstacles(Vector2 Position){
		float distance = (float) Math.sqrt(Math.pow(previousPos.x - Position.x, 2)+ Math.pow(previousPos.y - Position.y, 2));
		if(previousPos.x <0 && previousPos.y <0){
			//System.out.println("1. record new points");
			try {
				fwObstacle.write(Position.getX() +", "+ Position.getY()+"\r\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fwObstacle.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			previousPos = Position;
		}
		else if(distance <= 10){
			//System.out.println("distance");
		}
		else{
			try {
				fwObstacle.write(Position.getX() +", "+ Position.getY()+"\r\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fwObstacle.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("2. record new points");
			previousPos = Position;
		}
	}
	//set tile dots.
	public void drawDot(int tileNumber, int screenWidth, int screenHeight){
		Set<Integer> tempSet;
		int xPos;
		int yPos;

		int i =0;
		
		while(i < tileNumber){ // y
			yPos = (i* screenHeight/tileNumber) +(screenHeight/tileNumber/2);
			for(int j = 0; j < tileNumber; j++){
				xPos = (j* screenWidth/tileNumber) +(screenWidth/tileNumber/2);

				try {
					fw2.write(xPos +", "+ yPos+"\r\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fw2.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			i++;
		}
		try {
			fw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	//load files
	public void readTile(PApplet p){
		PApplet parent = p;
	    FileReader road1 = null;
	    
	    String tempS;
	    String[] tempSplit;
	    float x;
	    float y;
	    BufferedReader br;
	    
	    try {
			road1 = new FileReader("Points.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    br = new BufferedReader(road1);
	       try {
			while (br.ready()) {
				//System.out.println(br.readLine());
				tempS = br.readLine();
				tempSplit = tempS.split(",");
				x = Float.parseFloat(tempSplit[0]);
				y = Float.parseFloat(tempSplit[1]);
				
				roadNode.add(new Vector2(x, y));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	    //for(int i =0; i< roadNode.size(); i++){
	    	//System.out.println("(" + level1.get(i).x+ ", "+ level1.get(i).y+" )");
	    //}	    
	    
	}
	//load files
	public void readObstacle(PApplet p){
		PApplet parent = p;
	    FileReader obstacle1 = null;
	    String tempS;
	    String[] tempSplit;
	    float x;
	    float y;
	    BufferedReader br;
	    
	    try {
			obstacle1 = new FileReader("Obstacles.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    br = new BufferedReader(obstacle1);
	       try {
			while (br.ready()) {
				//System.out.println(br.readLine());
				tempS = br.readLine();
				tempSplit = tempS.split(",");
				x = Float.parseFloat(tempSplit[0]);
				y = Float.parseFloat(tempSplit[1]);
				
				obstacleNode.add(new Vector2(x, y));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
/*
	    for(int i =0; i< obstacleNode.size(); i++){
	    	System.out.println("(" + obstacleNode.get(i).x+ ", "+ obstacleNode.get(i).y+" )");
	    }	    
*/	    
	}	
	
	 public void nodeDisplay(PApplet P){
		 PApplet parent = P;

		 // Node

		 for(int i = 0 ; i < roadNode.size(); i++){
				parent.pushMatrix();
					parent.stroke(0);
					parent.fill(255, 0, 255);
					parent.ellipse(roadNode.get(i).x, roadNode.get(i).y, GlobalSetting.nodeSize, GlobalSetting.nodeSize);
				parent.popMatrix();
/*			 	
				parent.pushMatrix();	
					parent.stroke(255, 255, 0);
					parent.fill(255, 255, 0);
					parent.text(i+targetNode.size(), roadNode.get(i).x, roadNode.get(i).y);
			 	parent.popMatrix();
*/
		 }		 

	 }
}

package GraphData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import BasicStructures.Vector2;
import processing.core.PApplet;

public class MapGenerator {
	public int nodeNumber;
	public int edgeNumber;
	public int[][] check;
	public String outFile;
	public FileWriter fw2 = null;
	public Vector2 previousPos;
	public List<Vector2> level1;
	public List<Vector2> level2;
	public List<Vector2> level3;
	public List<Vector2> targetNode;
	public List<Vector2> roadNode;
	public boolean isObstacle;
	
	public MapGenerator(int node, int edge, String FileName){
		this.nodeNumber = node;
		this.edgeNumber = edge;
		isObstacle =true;
		
		check = new int[nodeNumber][nodeNumber];
		

		try {
			fw2 = new FileWriter("W2.txt");
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
		
		level1 = new ArrayList<Vector2>();
		level2 = new ArrayList<Vector2>();
		level3 = new ArrayList<Vector2>();
		targetNode = new ArrayList<Vector2>();
		roadNode = new ArrayList<Vector2>();

	}
	public void createRandomGraph(String FileName){
		Random rand = new Random();
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(FileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		for(int k =0; k< edgeNumber; k++){
			int fn = rand.nextInt(((nodeNumber-1) - 0) + 1) + 0;
			int bn = rand.nextInt(((nodeNumber-1) - 0) + 1) + 0;
			while((fn == bn) || (check[fn][bn]!=0 && check[bn][fn]!=0)){
				fn = rand.nextInt(((nodeNumber-1) - 0) + 1) + 0;
				bn = rand.nextInt(((nodeNumber-1) - 0) + 1) + 0;
			}
			if(fn > bn){
				try {
					fw.write(bn +", "+fn+"\r\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					fw.write(fn +", "+bn+"\r\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			check[fn][bn] = 1;
			check[bn][fn] = 1;
			
		}
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void drawDot(Vector2 Position){
		float distance = (float) Math.sqrt(Math.pow(previousPos.x - Position.x, 2)+ Math.pow(previousPos.y - Position.y, 2));
		if(previousPos.x <0 && previousPos.y <0){
			previousPos = Position;
		}
		else if(distance <= 10){
			
		}
		else{
			try {
				fw2.write(Position.getX() +", "+ Position.getY()+"\r\n");
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
			previousPos = Position;
		}
	}
	public void drawDot2(int numberDot, int screenWidth, int screenHeight){
		Map<Integer, Set<Integer>> checkRepeat;
		Set<Integer> tempSet;
		Random rand = new Random();
		int xPos;
		int yPos;

		xPos = rand.nextInt(screenWidth) + 0;
		yPos = rand.nextInt(screenHeight) + 0;
		
		int i =0;
		checkRepeat = new HashMap<Integer, Set<Integer>>();
		
		
		
		while(i < numberDot){
			xPos = rand.nextInt(screenWidth) + 0;
			yPos = rand.nextInt(screenHeight) + 0;

			if(checkRepeat.get(xPos) == null){
				//never show
				tempSet = new HashSet<Integer>();
				tempSet.add(yPos);
				checkRepeat.put(xPos, tempSet);
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
				i++;
			}
			else{
				if(checkRepeat.get(xPos).contains(yPos)==false){
					//not this point
					checkRepeat.get(xPos).add(yPos);
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
					i++;
				}
				else{
					// same point
				}
			}
		}
		
	}	
	public void readObstacles(PApplet p){
		PApplet parent = p;
	    FileReader obstacle1 = null;
	    FileReader obstacle2 = null;
	    FileReader obstacle3 = null;
	    FileReader target1 = null;
	    FileReader road1 = null;

	    
	    String tempS;
	    String[] tempSplit;
	    float x;
	    float y;
	    
		try {
			obstacle1 = new FileReader("Wall.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    BufferedReader br = new BufferedReader(obstacle1);
	       try {
			while (br.ready()) {
				//System.out.println(br.readLine());
				tempS = br.readLine();
				tempSplit = tempS.split(",");
				x = Float.parseFloat(tempSplit[0]);
				y = Float.parseFloat(tempSplit[1]);
				
				level1.add(new Vector2(x, y));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	    for(int i =0; i< level1.size(); i++){
	    	//System.out.println("(" + level1.get(i).x+ ", "+ level1.get(i).y+" )");
	    }

	    
	    
		try {
			obstacle2 = new FileReader("Level2.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    br = new BufferedReader(obstacle2);
	       try {
			while (br.ready()) {
				//System.out.println(br.readLine());
				tempS = br.readLine();
				tempSplit = tempS.split(",");
				x = Float.parseFloat(tempSplit[0]);
				y = Float.parseFloat(tempSplit[1]);
				
				level2.add(new Vector2(x, y));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	    for(int i =0; i< level2.size(); i++){
	    	//System.out.println("(" + level1.get(i).x+ ", "+ level1.get(i).y+" )");
	    }

	    
		try {
			obstacle3 = new FileReader("Level3.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    br = new BufferedReader(obstacle3);
	       try {
			while (br.ready()) {
				//System.out.println(br.readLine());
				tempS = br.readLine();
				tempSplit = tempS.split(",");
				x = Float.parseFloat(tempSplit[0]);
				y = Float.parseFloat(tempSplit[1]);
				
				level3.add(new Vector2(x, y));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	    for(int i =0; i< level3.size(); i++){
	    	//System.out.println("(" + level1.get(i).x+ ", "+ level1.get(i).y+" )");
	    }	    
		
	    
	    try {
			target1 = new FileReader("Target.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    br = new BufferedReader(target1);
	       try {
			while (br.ready()) {
				//System.out.println(br.readLine());
				tempS = br.readLine();
				tempSplit = tempS.split(",");
				x = Float.parseFloat(tempSplit[0]);
				y = Float.parseFloat(tempSplit[1]);
				
				targetNode.add(new Vector2(x, y));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	    for(int i =0; i< targetNode.size(); i++){
	    	//System.out.println("(" + level1.get(i).x+ ", "+ level1.get(i).y+" )");
	    }	    
	    
	    try {
			road1 = new FileReader("Road.txt");
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
	       
	    for(int i =0; i< roadNode.size(); i++){
	    	//System.out.println("(" + level1.get(i).x+ ", "+ level1.get(i).y+" )");
	    }	    
	    
	}
	
	 public void obstacleDisplay(PApplet P){
		 PApplet parent = P;
		 if(isObstacle == true){
			 for(int i = 0 ; i < level1.size(); i++){
					parent.pushMatrix();
						parent.stroke(0);
						parent.fill(255, 0, 0);
						parent.ellipse(level1.get(i).x, level1.get(i).y, 40, 40);
					parent.popMatrix();
	/*				
				 	parent.pushMatrix();	
						parent.stroke(0);
						parent.fill(0, 0, 0);
						parent.text(i, level1.get(i).x, level1.get(i).y);
				 	parent.popMatrix();				
	*/
			 }
			 for(int i = 0 ; i < level2.size(); i++){
					parent.pushMatrix();
						parent.stroke(0);
						parent.fill(0, 255, 255);
						parent.ellipse(level2.get(i).x, level2.get(i).y, 40, 40);
					parent.popMatrix();
	/*
					parent.pushMatrix();	
						parent.stroke(0);
						parent.fill(0, 0, 0);
						parent.text(i+level1.size(), level2.get(i).x, level2.get(i).y);
				 	parent.popMatrix();				
	*/
			 }
			 for(int i = 0 ; i < level3.size(); i++){
					parent.pushMatrix();
						parent.stroke(0);
						parent.fill(0, 0, 255);
						parent.ellipse(level3.get(i).x, level3.get(i).y, 40, 40);
					parent.popMatrix();
	/*
					parent.pushMatrix();	
						parent.stroke(0);
						parent.fill(0, 0, 0);
						parent.text(i+level1.size()+level2.size(), level3.get(i).x, level3.get(i).y);
				 	parent.popMatrix();				
	*/
			 }
		 }
		 // Node
		 for(int i = 0 ; i < targetNode.size(); i++){
			 	parent.pushMatrix();
					parent.stroke(0);
					parent.fill(255, 255, 0);
					parent.ellipse(targetNode.get(i).x, targetNode.get(i).y, 20, 20);
				parent.popMatrix();

				parent.pushMatrix();	
					parent.stroke(0);
					parent.fill(0, 0, 0);
					parent.text(i, targetNode.get(i).x-4, targetNode.get(i).y+4);
			 	parent.popMatrix();

		 }		 
		 for(int i = 0 ; i < roadNode.size(); i++){
				parent.pushMatrix();
					parent.stroke(0);
					parent.fill(255, 0, 255);
					parent.ellipse(roadNode.get(i).x, roadNode.get(i).y, 10, 10);
				parent.popMatrix();
			 	parent.pushMatrix();	
					parent.stroke(255, 255, 0);
					parent.fill(255, 255, 0);
					parent.text(i+targetNode.size(), roadNode.get(i).x, roadNode.get(i).y);
			 	parent.popMatrix();

		 }		 

	 }
}

package DrawData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import BasicStructures.*;
import GraphAlgorithm.AStar;
import GraphAlgorithm.H2;
import GraphData.GraphGenerator;
import GraphData.Node;
import BasicBehavior.Seek;
import MovementStructures.*;
import Variables.CommonFunction;
import Variables.GlobalSetting;
import Variables.PublicGraph;
import processing.core.PApplet;
import Variables.GlobalSetting;

public class CharacterDrop {
	//shape
	//private DropShape nowShape;
	private ColorVectorRGB shapeColor;
	private ColorVectorRGB backgroundColor;
	
	private DropShape[] shape;
	//breadcrumb
	private boolean isBreadcrumb;
	private List<BreadcrumbInfo> breadQueue;
	//private List<String> testbreadQueue;

	private int breadNumber;
	
	//KinematicData
	private KinematicData paraK;
	private KinematicOperations operK;
	
	//SteeringData
	private SteeringData paraS;
	private AStar A;
	private Seek Seek;
	private boolean isSeeking;
	private int count = 0;
	public List<Integer> targetQueue;
	private ResultChange tempResult;

	
	Vector2 currentTarget;
	
	int targetIndex;
	AStar A1;

	
	//prediction
	private int myNumber;
	private Vector2 prediction;
	//End of prediction
	//Request
	private boolean beRequest;
	private boolean isSeek;
	//End of Request

	public CharacterDrop(
			PApplet P,
			float CircleSize,
			float TriangleSize,			
			Vector2 OriginalPoint,
			Vector2 CurrentPosition,
			float CurrentOrientation,
			Vector2 CurrentVelocity,
			float CurrentRotation,
			KinematicOperations K,
			Vector2 LinearAccel,
			float AngularAccel,
			ColorVectorRGB Color,
			ColorVectorRGB BackColor,
			int NumberOfBread,
			SystemParameter Sys,
			int number
	)
	{
		//prediction------------------
		myNumber = number;
				
		//End of prediction-----------
		
		breadQueue = new  ArrayList<BreadcrumbInfo>();
		//testbreadQueue = new  ArrayList<String>();

		this.breadNumber = NumberOfBread;
		this.shapeColor = Color;
		this.backgroundColor = BackColor;
		
		this.operK = K;
		
		this.paraK = new KinematicData(
			CurrentPosition.getX(),
			CurrentPosition.getY(),
			CurrentOrientation,
			CurrentVelocity.getX(),
			CurrentVelocity.getY(),
			CurrentRotation,
			K
		);
		
		updateBreadQueue(paraK.getPosition(),paraK.getOrientation());
		
		this.paraS = new SteeringData(LinearAccel.getX(), LinearAccel.getY(), AngularAccel);

		isBreadcrumb = true;
		
		shape = new DropShape[breadNumber];
		//System.out.println(CurrentPosition.getX()+ ", " +CurrentPosition.getY() );
		
		//nowShape = new DropShape(P, CircleSize, TriangleSize, OriginalPoint, CurrentPosition, CurrentOrientation, Color);
		for(int i = 0; i< breadNumber; i++){
			shape[i] = new DropShape(P, CircleSize, TriangleSize, OriginalPoint, CurrentPosition, CurrentOrientation, Color);
			//System.out.println(CurrentPosition.getX()+ ", " +CurrentPosition.getY() );
		}
		isSeeking = false;
		count = 0;
		
		targetQueue = new ArrayList<Integer>();
		currentTarget = getPosition();
		
		tempResult = new ResultChange(
				getPosition().getX(),
				getPosition().getY(),
				getK().getOrientation(),
				getK().getVelocity().getX(),
				getK().getVelocity().getY(),
				getK().getRotation(),
				operK,
				getS().getLinearAccel().getX(),
				getS().getLinearAccel().getY(),
				getS().getAngularAccel()
			);			

		Seek = new Seek(
				5.0f,
				100.0f,
				0.1f,
				getPosition(),
				1,
				getPosition(),
				getK().getVelocity(),
				0,
				0,
				getS().getLinearAccel(),
				0,
				operK,
				Sys.maxVelocity,
				Sys.maxAcceleration,
				P
		);

	}
	
	
	//prediction------------------------------------------------------------------------------------------

	public void updateMyPrediction(Vector2 newPrediction){
		GlobalSetting.predictions.setMyPrediction(myNumber, newPrediction);
	}
	public int getNumber(){
		return myNumber;
	}
		
	public Vector2 getMyPrediction(){		
		return GlobalSetting.predictions.getMyPrediction(myNumber);
	}
		
	//End of prediction------------------------------------------------------------------------------------

	//Request---------------------------------------------------------------------------------------------
	public boolean checkSeekMode(){
		return isSeek;
	}
	
	public void isSeekMode(){
		isSeek = true;
	}
	public void isWanderMode(){
		isSeek = false;
		beRequest = false;
	}
	
	public boolean checkRequest(){
		return beRequest;
	}
	public Vector2 givePrediction(){
		return GlobalSetting.predictions.getMyPrediction(myNumber);
	}
	public void receiveRequest(){
		beRequest = true;
	}
		
	//End of Request---------------------------------------------------------------------------------------
	
	
	
	//update character position
	public void updatePosition(Vector2 NewPosition){
		this.paraK.setPosition(NewPosition);
		//System.out.println("test "+ paraK.getPosition().getX() + ", " + paraK.getPosition().getY());
		//updateBreadQueue(paraK.getPosition(), paraK.getOrientation());
	}
	public void updatePosition(float NewPositionX, float NewPositionY){
		this.paraK.setPosition(NewPositionX, NewPositionY);
		//System.out.println("**test "+ paraK.getPosition().getX() + ", " + paraK.getPosition().getY());

		//updateBreadQueue(paraK.getPosition(), paraK.getOrientation());
	}
	public Vector2 getPosition(){
		return paraK.getPosition();
	}
	
	//orientation
	public float getOrientation(){
		return paraK.getOrientation();
	}
	public void updateOrientation(float NewOrientation){
		paraK.setOrientation(NewOrientation);
	}
	
	
	public KinematicData getK(){
		return paraK;
	}
	public SteeringData getS(){
		return paraS;
	}
	
	public void setK(KinematicData K){
		this.paraK = K;
	}
	public void setS(SteeringData S){
		this.paraS = S;
	}
	
	//update shape color
	public void updateShapeColor(ColorVectorRGB CurrentColor, int Index){
		shape[Index].updateColor(CurrentColor);
	}
	public void updateShapeColor(float CurrentColorR, float CurrentColorG, float CurrentColorB, int Index){
		shape[Index].updateColor(new ColorVectorRGB(CurrentColorR, CurrentColorG, CurrentColorB));
	}
	
	
	//update shape position
	public void updateShapePosition(Vector2 CurrentPosition, int Index){
		shape[Index].updatePosition(CurrentPosition);
	}
	public void updateOrientation(float CurrentOrientation, int Index){
		shape[Index].updateOrientation(CurrentOrientation);
	}
	
	//control breadcrumb
	public void turnOffBread(){
		isBreadcrumb = false;
	}
	public void turnOnBread(){
		isBreadcrumb = true; 

	}
	
	//display things
	public void display(){
		//testshape.display();
		if(isBreadcrumb == true){
			//draw shape
			

			
			for(int i = 0; i < breadQueue.size(); i++){
				updateShapePosition(breadQueue.get(i).getPosition(), i);
				updateOrientation(breadQueue.get(i).getOrientation(), i);
				//System.out.println(breadQueue.get(i).getOrientation());
				//System.out.println(breadQueue.get(i).getPosition().getX() +", "+ breadQueue.get(i).getPosition().getY());
				updateShapeColor(
					shapeColor.getR()+(backgroundColor.getR() - shapeColor.getR())* ( breadQueue.size() - i-1)/ breadNumber,
					shapeColor.getG()+(backgroundColor.getG() - shapeColor.getG())* ( breadQueue.size() - i-1)/ breadNumber,
					shapeColor.getB()+(backgroundColor.getB() - shapeColor.getB())* ( breadQueue.size() - i-1)/ breadNumber,
					i
				);
				//display
				shape[i].display();
			}
			
			//nowShape.updatePosition(paraK.getPosition());
			//nowShape.updateColor(shapeColor);
			//nowShape.display();			

			
			//System.out.println("-------------------");
		}
		else{
			// Do nothing here
		}
		
	}
	
	//record position
	public void updateBreadQueue(Vector2 CurrentPosition, float CurrentOrientation){
		
		if(breadQueue.size() < breadNumber ){
			//System.out.println("Start to add" + "size = "+ breadQueue.size() + " orientation " + CurrentOrientation);
			breadQueue.add(
					new BreadcrumbInfo(CurrentPosition.getX(), CurrentPosition.getY(), CurrentOrientation)
			);

			Iterator iterator = breadQueue.iterator();

/*			
			int i =0;
			while(iterator.hasNext()) {
			    Object next = iterator.next();
			    System.out.println(next.toString()+"   "+ breadQueue.get(i).getPosition().getX() + ", " +breadQueue.get(i).getPosition().getY());
			}
			System.out.println("------------");
*/
		}
		else{
			breadQueue.remove(0);
			breadQueue.add(
					new BreadcrumbInfo(CurrentPosition.getX(), CurrentPosition.getY(), CurrentOrientation)
			);
/*
			Iterator iterator = breadQueue.iterator();

			int i =0;
			while(iterator.hasNext()) {
			    Object next = iterator.next();
			    System.out.println(next.toString()+"   "+ breadQueue.get(i++).shapeVariables.position.x);
			}
*/			
		}

	}
	
	public void Seek(Vector2 target){
		currentTarget = target;
		int targetIndex = CommonFunction.findClose(PublicGraph.G.nodeList, currentTarget);
		
		int closestIndex = CommonFunction.findClose(PublicGraph.G.nodeList, getK().getPosition());

		//System.out.println(targetIndex+ ", " + closestIndex);
		H2 h1 = new H2(PublicGraph.G.nodeList, PublicGraph.G.edgeList, targetIndex, closestIndex, operK);
		
		A1 = new AStar(h1, PublicGraph.G.nodeList, PublicGraph.G.edgeList, targetIndex, closestIndex);

		while(A1.openList.size()>0){
			A1.computeAStar(PublicGraph.G.nodeList, PublicGraph.G.edgeList);
			//System.out.println("-----------");
		}
		if(A1.isFind == false){
			System.out.println("Didn't find!!");
		}
		//System.out.println("");
		targetQueue.clear();
		targetQueue.addAll(A1.result);
		//remove self
		targetQueue.remove(0);

		if(targetQueue.size()>0){

			//if(findClose(currentNodeList,character.getK().getPosition())!=currentTargetQueue.get(0)){
			if(operK.getDisBy2Points(PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate, getK().getPosition())>5){
				//System.out.println("Current Target = " + targetQueue.get(0));
				currentTarget = PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate;
				//System.out.println("----Seek ("+ targetQueue.get(0)+ ") ");
			}
			else{
				targetQueue.remove(0);
			}
			//currentTarget = PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate;
			//System.out.println("----Seek ("+ targetQueue.get(0)+ ") ");

			//targetQueue.remove(0);
			tempResult = Seek.computeSeek(currentTarget);
			
			setK(tempResult.getK());
			setS(tempResult.getS());
			//updatePosition(currentTarget);
		}
	}
	
	public Vector2 getSeekPosition(CharacterHuman[] Bots, KinematicOperations Operk)
	{
		Vector2 seekposition;
		int numberOfBots = Bots.length;
		CharacterHuman nearestBot = null;
		Vector2 distanceVector;
		double distance, nearestDistance;
		nearestDistance = Double.POSITIVE_INFINITY;
		
		for (int i=0; i< numberOfBots; i++)
		{
			distance = Math.sqrt(Math.pow(Bots[i].getPosition().x - this.getPosition().x, 2) + Math.pow(Bots[i].getPosition().y - this.getPosition().y, 2));
			if (distance < 100)
			{
				if (distance < nearestDistance)
				{
					nearestBot = Bots[i];
					nearestDistance = distance;
				}
			}
		}
		
		if (nearestDistance < 100)
		{
			distanceVector = Operk.normalizeVector2(new Vector2(-(nearestBot.getPosition().x - this.getPosition().x), -(nearestBot.getPosition().y - this.getPosition().y)));
			distanceVector = new Vector2((float)(distanceVector.x * nearestDistance), (float)(distanceVector.y * nearestDistance));
			seekposition = new Vector2(distanceVector.x + this.getPosition().x, distanceVector.y + this.getPosition().y);
		}
		else
		{
			seekposition = new Vector2(631, 19);
		}
		return seekposition;
	}

	public void Wander(){


		if(isSeeking == false){
			//if(mousePressed){
				isSeeking = true;
				//call path finding
				currentTarget = new Vector2((float)Math.random()*GlobalSetting.screenWidth, (float)Math.random()*GlobalSetting.screenHeight);
				
				int targetIndex = CommonFunction.findClose(PublicGraph.G.nodeList, currentTarget);
				
				while(PublicGraph.graphGenerator.ObsOverlapList.get(targetIndex)== 1){
					currentTarget = new Vector2((float)Math.random()*GlobalSetting.screenWidth, (float)Math.random()*GlobalSetting.screenHeight);
					targetIndex = CommonFunction.findClose(PublicGraph.G.nodeList, currentTarget);
				}
				
				int closestIndex = CommonFunction.findClose(PublicGraph.G.nodeList, getK().getPosition());
				
				//System.out.println(targetIndex+ ", " + closestIndex);
				H2 h1 = new H2(PublicGraph.G.nodeList, PublicGraph.G.edgeList, targetIndex, closestIndex, operK);
				
				A1 = new AStar(h1, PublicGraph.G.nodeList, PublicGraph.G.edgeList, targetIndex, closestIndex);

				while(A1.openList.size()>0){
					A1.computeAStar(PublicGraph.G.nodeList, PublicGraph.G.edgeList);
					//System.out.println("-----------");
				}
				if(A1.isFind == false){
					System.out.println("Didn't find!!");
				}
				else{
/*
					System.out.print("\r\nAStar with H1 Path: ");
					for(int i = 0 ;i < A1.result.size(); i++){
						System.out.print(" " + A1.result.get(i)+" ");
					}
*/
				}
				//System.out.println("");
				targetQueue.clear();
				targetQueue.addAll(A1.result);
		}
		
		count = (count+1)%50;

		if(count == 0){
			isSeeking = false;
		}
		//Gathering dots
		
		//make decisions in 0.02 sec frequency
		//make one decision

		if(isSeeking == true){
			if(targetQueue.size()>0){

				//if(findClose(currentNodeList,character.getK().getPosition())!=currentTargetQueue.get(0)){
	
				if(operK.getDisBy2Points(PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate, getK().getPosition())>5){
					//System.out.println("Current Target = " + targetQueue.get(0));
					currentTarget = PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate;
				}
				else{
					targetQueue.remove(0);
				}
				//currentTarget = PublicGraph.G.nodeList.get(targetQueue.get(0)).coordinate;
				//targetQueue.remove(0);
				tempResult = Seek.computeSeek(currentTarget);
				//System.out.println(tempResult.getK().getPosition().x + ", "+tempResult.getK().getPosition().y);
				setK(tempResult.getK());
				setS(tempResult.getS());

				if(count == 0){
					isSeeking = false;
					targetQueue.clear();
				}

			}
			else{
				isSeeking = false;
				
			}
			
		}

	}
	public void Move(int upMove, int downMove, int leftMove, int rightMove){
		Vector2 target = new Vector2(0, 0);
/*		
		target = new Vector2(
				normalizeKeyMove(getPosition().getX()+(float)leftMove+(float)rightMove),
				normalizeKeyMove(getPosition().getY()+(float)upMove+(float)downMove)
				);
*/
		target = new Vector2(
				getPosition().getX()+normalizeKeyMove((float)leftMove)+normalizeKeyMove((float)rightMove),
				getPosition().getY()+normalizeKeyMove((float)upMove)+normalizeKeyMove((float)downMove)
		);
		
		int targetIndex = CommonFunction.findClose(PublicGraph.G.nodeList, target);
		updatePosition(PublicGraph.G.nodeList.get(targetIndex).coordinate);

		//updatePosition(target);
		
	}
	public float  normalizeKeyMove(float totalMove){
		float result =totalMove ;
		//System.out.println(totalMove);
		if(totalMove >= GlobalSetting.keyMoveDistance){
			result = ((float)totalMove/Math.abs(totalMove))*GlobalSetting.keyMoveDistance;
		}
		return result;
	}
	

}

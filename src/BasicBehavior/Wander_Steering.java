package BasicBehavior;

import BasicStructures.*;
import MovementStructures.*;
import processing.core.PApplet;

public class Wander_Steering{
	
	private PApplet parent;
	private ResultChange R;
	private float ROS;
	private float ROD;
	private float timeToTarget;
	private float timeConstant;
	private KinematicOperations operK;
	private SystemParameter sys;

	
	
	
	public Wander_Steering(
		float TimeConstant,
		Vector2 charaP,
		Vector2 charaV,
		float charaO,
		float charaR,
		Vector2 charaL,
		float charaA,
		KinematicOperations OperK,
		SystemParameter Sys,
		PApplet P
	){
		R = new ResultChange(
			charaP.getX(),
			charaP.getY(),
			charaO,
			charaV.getX(),
			charaV.getY(),
			charaR,
			OperK,
			charaL.getX(),
			charaL.getY(),
			charaA
		);
		 
		this.timeConstant = TimeConstant;
		this.operK = OperK;
		this.sys = Sys;

	
/*		
		KinematicData tempK = new KinematicData(charaP.getX(), charaP.getY(), charaO, charaV.getX(), charaV.getY(), charaR, OperK);
		SteeringData tempS = new SteeringData(charaL.getX(), charaL.getY(), charaA);		
		this.R.updateK(tempK);
		this.R.updateS(tempS);
*/		
		
	}
	public ResultChange computeWander(float currentOrientation, boolean isChangeOrientation){
		float newOrientation = currentOrientation;
		Vector2 tempV = new Vector2(0, 0);
		
		if(isChangeOrientation == false){
		}
		else{
			System.out.println("change orientation");
			newOrientation = decideOrientation();
		}
		tempV = operK.getVFromOrien(newOrientation);
		tempV = operK.clip2MaxVelocity(tempV);
		System.out.println(tempV.x +", "+tempV.y);
		
		R.updateVelocity(tempV);
		//System.out.println(R.getVelocity().getX() +", " + R.getVelocity().getY());
		R.updateRotation(0);
		R.updatePosition(operK.updatePositionByV(R.getPosition(), R.getVelocity(), timeConstant));
		R.updateOrientation(operK.getOrientationByV(newOrientation, R.getVelocity()));
		
		return R;

	}
	public float reandomBinomial(){
		float result = (float)(Math.random() - Math.random());
		return result;
	}
	public float decideOrientation(){
		float newOrientation =0;
		newOrientation = (0 + (float)Math.random()* sys.getMaxRotation())-parent.PI; 		
		return newOrientation;
	}

}

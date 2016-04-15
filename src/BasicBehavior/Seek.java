package BasicBehavior;

import BasicStructures.*;
import MovementStructures.*;
import processing.core.PApplet;

public class Seek{
	
	private PApplet parent;
	private ResultChange R;
	private float ROS;
	private float ROD;
	private float timeToTarget;
	private float timeConstant;
	private Vector2 targetPosition;
	private KinematicOperations operK;
	
	private float maxV;
	private float maxA;
	
	public Seek(
		float RadiusOfS,
		float RadiusOfD,
		float TimeToTarget,
		Vector2 TargetPosition,
		float TimeConstant,
		Vector2 charaP,
		Vector2 charaV,
		float charaO,
		float charaR,
		Vector2 charaL,
		float charaA,
		KinematicOperations OperK,
		float maxVelocity,
		float maxAccel,
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
		 
		this.ROS = RadiusOfS;
		this.ROD = RadiusOfD;
		this.timeToTarget = TimeToTarget;
		this.timeConstant = TimeConstant;
		this.operK = OperK;
		targetPosition = TargetPosition;
		this.maxV = maxVelocity;
		this.maxA = maxAccel;
	
/*		
		KinematicData tempK = new KinematicData(charaP.getX(), charaP.getY(), charaO, charaV.getX(), charaV.getY(), charaR, OperK);
		SteeringData tempS = new SteeringData(charaL.getX(), charaL.getY(), charaA);		
		this.R.updateK(tempK);
		this.R.updateS(tempS);
*/		
		
	}
	public ResultChange stupidSeek(Vector2 TargetPosition){
		Vector2 tempV;

		tempV = new Vector2((TargetPosition.getX() - R.getPosition().getX()), (TargetPosition.getY() - R.getPosition().getY()));
		R.updateVelocity(tempV);
		R.updateRotation(0);
		R.updatePosition(operK.updatePositionByV(R.getPosition(), R.getVelocity(), timeConstant));
		//R.updatePosition(TargetPosition);
		//System.out.println("Targ: " + TargetPosition.getX() + ", " + TargetPosition.getY());
		R.updateOrientation(operK.getOrientationByV(R.getOrientation(), R.getVelocity()));
		//System.out.println(R.getOrientation());
				
		return R;
	}	
	public ResultChange computeSeek(Vector2 TargetPosition){
		targetPosition = TargetPosition;
		
		Vector2 tempV;

		tempV = new Vector2((TargetPosition.getX() - R.getPosition().getX()), (TargetPosition.getY() - R.getPosition().getY()));
		tempV = operK.normalizeVector2(tempV);
		//System.out.println(maxV);
		tempV = new Vector2((float)tempV.x *maxV, (float)tempV.y *maxV);

		R.updateVelocity(tempV);
		//System.out.println("---" +tempV.x +", " + tempV.y);
		R.updateRotation(0);
		R.updatePosition(operK.updatePositionByV(R.getPosition(), R.getVelocity(), timeConstant));
		R.updateOrientation(operK.getOrientationByV(R.getOrientation(), R.getVelocity()));
		
		//Arrive

		//System.out.println(R.getOrientation());

		//R.updatePosition(new Vector2(R.getPosition().getX()+2.0f, R.getPosition().getY()-2.0f));
		//R.updateOrientation((R.getOrientation()+0.1f));

/*		
		float distance2Target = operK.getLengthByVector2(direction2Target);
		
		
*/		
		return R;

	}
	public void updateTargetPosition(Vector2 TargetPosition){
		targetPosition = TargetPosition;
	}
	public boolean checkSlow(Vector2 CurrentPosition){

		boolean isSlow = false;
		isSlow = operK.checkMatch(targetPosition, CurrentPosition, ROD);
		return isSlow;
	}	
	
	
}

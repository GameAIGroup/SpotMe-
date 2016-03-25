package BasicBehavior;

import BasicStructures.*;
import MovementStructures.*;
import processing.core.PApplet;

public class Seek_Steering_New{
	
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
	
	public Seek_Steering_New(
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
	public ResultChange computeSeek(Vector2 TargetPosition){
		targetPosition = TargetPosition;
		Vector2 direction2Target;
		float distance2Target;
		float targetSpeed;
		Vector2 LAccel;

		
		//Arrive

		//System.out.println((TargetPosition.getX() - R.getPosition().getX()) + ", " + (TargetPosition.getY() - R.getPosition().getY())+"  || " + TargetPosition.getX() +", " +TargetPosition.getY());
		
		
		//get new V and A

		if(operK.getDisBy2Points(TargetPosition, R.getPosition()) < ROS){
			//System.out.println("should stop!");
			LAccel = new Vector2(0, 0);
			R.updateLinearAccel(LAccel);
			R.updateAngularAccel(0);
	
			R.updateVelocity(operK.updateVByA(new Vector2(0, 0), LAccel, timeConstant));
			//System.out.println(R.getVelocity().getX() +", " + R.getVelocity().getY());
			R.updateRotation(0);

			R.updatePosition(operK.updatePositionByV(R.getPosition(), R.getVelocity(), timeConstant));

			
		}
		else{
			if(operK.getDisBy2Points(TargetPosition, R.getPosition()) <ROD){
				direction2Target = new Vector2((TargetPosition.getX() - R.getPosition().getX()), (TargetPosition.getY() - R.getPosition().getY()));
				distance2Target = operK.getLengthByVector2(direction2Target);
				targetSpeed = maxV * (distance2Target/ ROD);
				Vector2 targetV = direction2Target;
				targetV = operK.normalizeVector2(targetV);
				targetV = new Vector2(targetV.getX() * targetSpeed, targetV.getY() * targetSpeed);
				
				LAccel = new Vector2(targetV.getX() - R.getVelocity().getX(),  targetV.getY() - R.getVelocity().getY());
				LAccel = new Vector2(LAccel.getX()/ timeToTarget, LAccel.getY()/ timeToTarget );
				
				if(operK.getLengthByVector2(LAccel) > maxA){
					LAccel = operK.normalizeVector2(LAccel);
					LAccel = new Vector2(LAccel.getX()* maxA, LAccel.getY()* maxA);
				}
				
				R.updateLinearAccel(LAccel);
				R.updateAngularAccel(0);
		
				R.updateVelocity(operK.updateVByA(R.getVelocity(), LAccel, timeConstant));
				//System.out.println(R.getVelocity().getX() +", " + R.getVelocity().getY());
				R.updateRotation(0);
				
				R.updatePosition(operK.updatePositionByV(R.getPosition(), R.getVelocity(), timeConstant));
		
				R.updateOrientation(operK.getOrientationByV(R.getOrientation(), R.getVelocity()));
				
				//System.out.println(targetV.getX() +", " + targetV.getY());
				

				//Vector2 targetSpeed = 
			}
			else{
				direction2Target = new Vector2((TargetPosition.getX() - R.getPosition().getX()), (TargetPosition.getY() - R.getPosition().getY()));
				distance2Target = operK.getLengthByVector2(direction2Target);
				
				LAccel = direction2Target;
				LAccel = operK.clip2MaxAcceleration(LAccel);
				
				targetSpeed = maxV;
				
				R.updateLinearAccel(LAccel);
				R.updateAngularAccel(0);
		
				R.updateVelocity(operK.updateVByA(R.getVelocity(), LAccel, timeConstant));
				//System.out.println(R.getVelocity().getX() +", " + R.getVelocity().getY());
				R.updateRotation(0);
				
				R.updatePosition(operK.updatePositionByV(R.getPosition(), R.getVelocity(), timeConstant));
		
				R.updateOrientation(operK.getOrientationByV(R.getOrientation(), R.getVelocity()));
			}
		}
		
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

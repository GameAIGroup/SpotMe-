/*
 * This class contains opertations apply on kinematic variable, including
 * get next position
 * get new velocity
 * get distance between points
 * get direction from velocity
 * get velocity direction from two points
 * get relative Position
 * get relative Velocity
 *  
 */
package MovementStructures;

import BasicStructures.*;
import processing.core.*;

public class KinematicOperations {
	// use Pi or Other
	private PApplet parent;
	private SystemParameter Sys;
	
	public KinematicOperations( PApplet P, SystemParameter Sys){
		this.parent = P;
		this.Sys = new SystemParameter(0, 0, 0);
		this.Sys = Sys;
	}
	
	// update position by position += velocity * time
	public Vector2 updatePositionByV(Vector2 CurrentPosition, Vector2 Velocity, float TimeConstant){
		float unitTime;
		Vector2 resultPosition = new Vector2(0, 0);
		
		//prevent error of time setting
		if(TimeConstant >= 1){
			unitTime = TimeConstant;
		}
		else{
			unitTime = 1;
		}
		
		//position += velocity * time
		resultPosition.setVector2(CurrentPosition.getX()+Velocity.getX()*unitTime, CurrentPosition.getY()+Velocity.getY()*unitTime);
		return resultPosition;
	}
	// update orientation by orientation += rotation * time
	public float updateOrientationByV(float CurrentOrientation, float Rotation, float TimeConstant){
		float unitTime;
		float resultOrientation = 0;
		
		//prevent error of time setting
		if(TimeConstant >= 1){
			unitTime = TimeConstant;
		}
		else{
			unitTime = 1;
		}
		
		//position += velocity * time
		resultOrientation = CurrentOrientation + Rotation * unitTime;
		return resultOrientation;
	}
	
	//update Velocity  v += at
	public Vector2 updateVByA(Vector2 CurrentVelocity, Vector2 Acceleration, float TimeConstant){
		float unitTime;
		Vector2 resultVelocity = new Vector2(0, 0);
		
		//prevent error of time setting
		if(TimeConstant >= 1){
			unitTime = TimeConstant;
		}
		else{
			unitTime = 1;
		}
		
		//position += velocity * time
		resultVelocity.setVector2(CurrentVelocity.getX()+Acceleration.getX()*unitTime, CurrentVelocity.getY()+Acceleration.getY()*unitTime);
		return resultVelocity;
	}
	
	// update orientation by orientation += rotation * time
	public float updateRotationByA(float CurrentRotation, float Angular, float TimeConstant){
		float unitTime;
		float resultRotation = 0;
		
		//prevent error of time setting
		if(TimeConstant >= 1){
			unitTime = TimeConstant;
		}
		else{
			unitTime = 1;
		}
		
		//position += velocity * time
		resultRotation = CurrentRotation + Angular * unitTime;
		return resultRotation;
	}
	
	// get orientation by V
	public float getOrientationByV(float CurrentOrientation, Vector2 TargetVelocity){
		//Compute Next Orientation1 (orientation = tan-1(-x, z))
		double resultOrientation;
		if(TargetVelocity.getX() == 0 &&TargetVelocity.getY() == 0){
			//no velocity
			resultOrientation = CurrentOrientation;
		}
		else{
			resultOrientation = Math.atan2((double)(-1)* TargetVelocity.getX(), (double)(1)*TargetVelocity.getY())+ (double)parent.PI;
/*
			if(Math.abs(resultOrientation) <= parent.PI){
			}
			else{
				if(resultOrientation > parent.PI){
					resultOrientation = resultOrientation - 2* parent.PI;
				}
				else{
					resultOrientation = resultOrientation + 2* parent.PI;
				}
			}
*/
		}
		return (float)resultOrientation;		
	}
	
	// distance of 2 points
	public float getDisBy2Points( Vector2 TargetPoint, Vector2 CurrentPoint){
		float resultDistance = 0;
		resultDistance = (float) Math.sqrt(
				Math.pow(TargetPoint.getX() - CurrentPoint.getX(),2)+
				Math.pow(TargetPoint.getY() - CurrentPoint.getY(),2)
		);
		return resultDistance;
	}
	
	//length of Vector
	public float getLengthByVector2(Vector2 TargetVector){
		float resultDistance = 0;
		resultDistance = (float) Math.sqrt(
				Math.pow(TargetVector.getX(), 2)+
				Math.pow(TargetVector.getY(), 2)
		);
		return resultDistance;		
	}
	
	//get velocity from orientation
	public Vector2 getVFromOrien(float Orientation){
		Vector2 resultVelocity = new Vector2(0, 0);
		// cos = x, sin =y
		resultVelocity.x = (float) Math.cos(Orientation);
		resultVelocity.y = (float) Math.sin(Orientation);
		return resultVelocity;		
	}
	
	//Clip to Max Velocity
	public Vector2 clip2MaxVelocity(Vector2 CurrentVelocity){
		Vector2 resultVelocity = new Vector2(0, 0);
		float VLength = getLengthByVector2(CurrentVelocity);
		System.out.println(VLength);
		if(VLength > Sys.getMaxV()){
			resultVelocity = normalizeVector2(CurrentVelocity);
			resultVelocity = new Vector2(resultVelocity.getX() * Sys.getMaxV(), resultVelocity.getY() * Sys.getMaxV());
		}
		else{
			resultVelocity = new Vector2(CurrentVelocity.x * Sys.getMaxV(), CurrentVelocity.y * Sys.getMaxV());
			//System.out.println(Sys.getMaxV() +", "+ resultVelocity.y);
		}
		
		return resultVelocity;
	}
	//Clip to Max Acceleration
	public Vector2 clip2MaxAcceleration(Vector2 CurrentAcceleration){
		Vector2 resultAccel = new Vector2(0, 0);
		float ALength = getLengthByVector2(CurrentAcceleration);
		if(ALength > Sys.getMaxA()){
			resultAccel = normalizeVector2(CurrentAcceleration);
			resultAccel = new Vector2(resultAccel.getX() * Sys.getMaxA(), resultAccel.getY() * Sys.getMaxA());
		}
		
		return resultAccel;
	}
	
	//Clip to Max Velocity
	public Vector2 clip2MaxVelocity(Vector2 CurrentVelocity, float targetSpeed){
		Vector2 resultVelocity = new Vector2(0, 0);
		float VLength = getLengthByVector2(CurrentVelocity);
		if(VLength > Sys.getMaxV()){
			resultVelocity = normalizeVector2(CurrentVelocity);
			resultVelocity = new Vector2(resultVelocity.getX() * targetSpeed, resultVelocity.getY() * targetSpeed);
		}
		
		return resultVelocity;
	}
	//Clip to Max Acceleration
	public Vector2 clip2MaxAcceleration(Vector2 CurrentAcceleration, float targetAccel){
		Vector2 resultAccel = new Vector2(0, 0);
		float ALength = getLengthByVector2(CurrentAcceleration);
		if(ALength > Sys.getMaxA()){
			resultAccel = normalizeVector2(CurrentAcceleration);
			resultAccel = new Vector2(resultAccel.getX() * targetAccel, resultAccel.getY() * targetAccel);
		}
		
		return resultAccel;
	}	
	
	
	
	public Vector2 normalizeVector2(Vector2 target){
		float VLength = getLengthByVector2(target);

		float normalizeFactor;
		normalizeFactor = (float) 1/ VLength;
		
		Vector2 newV = new Vector2(normalizeFactor * target.getX(), normalizeFactor * target.getY());
		
		return newV;
	}
	
	public SystemParameter getSys(){
		return Sys;
	}
	
	public Vector2 computeVFromTarget(Vector2 TargetPosition, Vector2 CurrentPosition){
		// cos = x, sin =y
		Vector2 velocityDirection = new Vector2(TargetPosition.getX() - CurrentPosition.getX(), TargetPosition.getY() - CurrentPosition.getY());
		//normaliz
		//float distance = (float) Math.sqrt(Math.pow(velocityDirection.x, 2)+ Math.pow(velocityDirection.y, 2));
		//float normalizeFactor = 1/distance;
		//Vector2 resultPropotion = new Vector2(velocityDirection.x*normalizeFactor, velocityDirection.y*normalizeFactor);
		velocityDirection = normalizeVector2(velocityDirection); 
		return velocityDirection;
	}
	public boolean checkMatch(Vector2 TargetPosition, Vector2 CurrentPosition, float Range){
		float distance;
		distance = getDisBy2Points(TargetPosition, CurrentPosition);
		// the distance for decreasing speed to zero is (n+1)/2 * velocity, if we decide to use n step to reduce speed to 0.
		if(distance < Range){
			return true;
		}
		else{
			return false;
		}
	}		
	public float getTheThirdLength(Vector2 v1, Vector2 v2, Vector2 v3){
		float Result = 0;

		Vector2 V1V2 = new Vector2(v1.x - v2.x, v1.y - v2.y);
		Vector2 V3V2 = new Vector2(v3.x - v2.x, v3.y - v2.y);

		//System.out.println("input: ("+v1.x+", "+v1.y+") ("+v2.x+", "+v2.y+")("+v3.x+", "+v3.y+")");
		
		float cosAngle = ((V1V2.x*V3V2.x)+(V1V2.y*V3V2.y))/(getLengthByVector2(V1V2)*getLengthByVector2(V3V2));

		
		Result = (float) Math.sqrt(
				Math.pow(getLengthByVector2(V1V2),2)+
				Math.pow(getLengthByVector2(V3V2),2)-
				(2*getLengthByVector2(V1V2)*getLengthByVector2(V3V2)*cosAngle)
		);
		System.out.println("cos: "+cosAngle +" result: " + Result);
		return Result;
	}
}

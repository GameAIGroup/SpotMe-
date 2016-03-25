/*
 * This class is used to return computing result
 */
package MovementStructures;

import BasicStructures.*;

public class ResultChange {
	private KinematicData resultK;
	private SteeringData resultS;

	public ResultChange(	
		float PositionX,
		float PositionY,
		float Orientation,
		float VelocityX,
		float VelocityY,
		float rotation,
		KinematicOperations K,
		float LinearX,
		float LinearY,
		float Angular
	){
		resultK = new KinematicData(PositionX, PositionY, Orientation, VelocityX, VelocityY, rotation, K);
		resultS = new SteeringData(LinearX, LinearY, Angular);
	}
	
	//update all
	public void updateK(KinematicData K){
		this.resultK = K;
	}
	public KinematicData getK(){
		return resultK;
	}
	
	public void updateS(SteeringData S){
		this.resultS = S;
	}
	public SteeringData getS(){
		return resultS;
	}	
	
	//position
	public void updatePosition(Vector2 Position){
		resultK.setPosition(Position);
	}
	public Vector2 getPosition(){
		return resultK.getPosition();
	}

	//V
	public void updateVelocity(Vector2 Velocity){
		resultK.setVelocity(Velocity);
	}
	public Vector2 getVelocity(){
		return resultK.getVelocity();
	}

	//orientation
	public void updateOrientation(float Orientation){
		resultK.setOrientation(Orientation);
	}
	public float getOrientation(){
		return resultK.getOrientation();
	}

	//rotation
	public void updateRotation(float Rotation){
		resultK.setRotation(Rotation);
	}
	public float getRotation(){
		return resultK.getRotation();
	}
	
	//acceleration
	public void updateLinearAccel(Vector2 LinearAccel){
		resultS.setLinear(LinearAccel);
	}
	public Vector2 getLinearAccel(){
		return resultS.getLinearAccel();
	}
	
	
	//angular
	public void updateAngularAccel(float AngularAccel){
		resultS.setAngualr(AngularAccel);
	}
	public float getAngularAccel(){
		return resultS.getAngularAccel();
	}
	
	
	
	
	
}

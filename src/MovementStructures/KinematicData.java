/*
 * This class contains kinematic parameters
 */
package MovementStructures;

import BasicStructures.*;

public class KinematicData {
	public Vector2 position;
	public float orientation;
	public Vector2 velocity;
	public float rotation;
	
	private KinematicOperations operK;
	
	public KinematicData(
		float PositionX,
		float PositionY,
		float Orientation,
		float VelocityX,
		float VelocityY,
		float rotation,
		KinematicOperations K
	){
		this.operK = K;
		setPosition(new Vector2(PositionX, PositionY));
		setOrientation(Orientation);
		setVelocity(new Vector2(VelocityX, VelocityY));
	}
	
	//set position
	public void setPosition(Vector2 Position){
		this.position = Position;
	}
	public void setPosition(float PositionX, float PositionY){
		this.position.setVector2(PositionX, PositionY);
	}
	// get position
	public Vector2 getPosition(){
		return position;
	}
	
	
	//set orientation
	public void setOrientation(float Orientation){
		this.orientation = Orientation;
	}
	public float getOrientation(){
		return orientation;
	}
	
	//set velocity
	public void setVelocity(Vector2 Velocity){
		this.velocity = Velocity;
	}
	public void setVelocity(float VelocityX, float VelocityY){
		this.velocity.setVector2(VelocityX, VelocityY);
	}
	public Vector2 getVelocity(){
		return velocity;
	}
	
	//set orientation
	public void setRotation(float Rotation){
		this.rotation = Rotation;
	}
	public float getRotation(){
		return rotation;
	}

	
}

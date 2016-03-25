/*
 * This class contains steering variables
 */
package MovementStructures;

import BasicStructures.*;

public class SteeringData {
	private Vector2 linearAcceleration;
	private float angularAcceleration;
	
	//make Constructor easy 
	public SteeringData(float LinearX, float LinearY, float Angular){
		// set accelerations
		setAccelerations(new Vector2(LinearX, LinearY), Angular);
	}
	
	//Update accelerations
	public void setAccelerations(Vector2 Linear, float Angular){
		this.linearAcceleration = Linear;
		this.angularAcceleration = Angular;
	}
	public void setAccelerations(float LinearX, float LinearY, float Angular){
		this.linearAcceleration.setX(LinearX);
		this.linearAcceleration.setY(LinearY);
		this.angularAcceleration = Angular;
	}
	
	//Set linear acceleration
	public void setLinear(Vector2 Linear){
		this.linearAcceleration = Linear;
	}
	public void setLinear(float LinearX, float LinearY){
		this.linearAcceleration.setX(LinearX);
		this.linearAcceleration.setY(LinearY);
	}	
	
	//Set linear acceleration
	public void setAngualr(float Angular){
		this.angularAcceleration = Angular;
	}	
	
	//Return linear acceleration
	public Vector2 getLinearAccel(){
		return linearAcceleration;
	}
	//Return angular acceleration
	public float getAngularAccel(){
		return angularAcceleration;
	}
}

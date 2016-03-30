package MovementStructures;

public class SystemParameter {
	private float maxVelocity;
	private float maxAcceleration;
	private float maxRotation;
	
	public SystemParameter(
		float MaxVelocity,
		float MaxAcceleration,
		float MaxRotation
	){
		this.maxVelocity = MaxVelocity;
		this.maxAcceleration = MaxAcceleration;
		this.maxRotation = MaxRotation;
	}
	
	
	public float getMaxV(){
		return maxVelocity;
	}
	public float getMaxA(){
		return maxAcceleration;
	}
	public float getMaxRotation(){
		return maxRotation;
	}	
}

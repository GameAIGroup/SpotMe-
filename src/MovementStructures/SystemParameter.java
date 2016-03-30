package MovementStructures;

public class SystemParameter {
	public float maxVelocity;
	public float maxAcceleration;
	public float maxRotation;
	
	public SystemParameter(
		float MaxVelocity,
		float MaxAcceleration,
		float MaxRotation
	){
		maxVelocity = MaxVelocity;
		//System.out.println("--"+maxVelocity);
		maxAcceleration = MaxAcceleration;
		maxRotation = MaxRotation;
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

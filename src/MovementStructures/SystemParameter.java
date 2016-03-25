package MovementStructures;

public class SystemParameter {
	private float maxVelocity;
	private float maxAcceleration;
	
	public SystemParameter(
		float MaxVelocity,
		float MaxAcceleration
	){
		this.maxVelocity = MaxVelocity;
		this.maxAcceleration = MaxAcceleration;
	}
	
	
	public float getMaxV(){
		return maxVelocity;
	}
	public float getMaxA(){
		return maxAcceleration;
	}
}

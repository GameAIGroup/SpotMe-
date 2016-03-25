package DrawData;

import BasicStructures.*;

public class BreadcrumbInfo {
	private Vector2 position;
	private float orientation;
	//private ColorVectorRGB color;
	
	public BreadcrumbInfo(float PositionX, float PositionY, float Orientation){
		position = new Vector2(0, 0);
		setOrientation(Orientation);
		setPosition(PositionX, PositionY);
		//setColor(ColorR, ColorG, ColorB);
	}
	
	//set position
	public void setPosition(Vector2 Position){
		this.position = Position;
	}
	public void setPosition(float PositionX, float PositionY){
		this.position.setVector2(PositionX, PositionY);
	}

	public Vector2 getPosition(){
		return position;
	}
	
	public void setOrientation(float Orientation){
		this.orientation = Orientation;
	}
	public float getOrientation(){
		return orientation;
	}
	
/*	
	//set color
	public void setColor(ColorVectorRGB Color){
		this.color = Color;
	}
	public void setColor(float ColorR, float ColorG, float ColorB){
		this.color.setColorVector(ColorR, ColorG, ColorB);
	}
*/
}

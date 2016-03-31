package Variables;

import BasicStructures.ColorVectorRGB;
import BasicStructures.Vector2;
import DrawData.HeartShape;
import processing.core.PApplet;

public class GameDisplay {
	static HeartShape[] lives;
	static PApplet parent;
	public GameDisplay(PApplet P){
		this.parent = P;
		
		lives = new HeartShape[GlobalSetting.characterLives];
		for(int i = 0; i < GlobalSetting.characterLives ; i++){
			lives[i] = new HeartShape(	
					P,
					20,
					20,			
				    new Vector2(0, 0),
				    new Vector2(50+i*30, 30),
					0,
					new ColorVectorRGB(255, 0, 0)
			);
		}
	}
	
	public static void displayHealth(){
		parent.pushMatrix();
		parent.stroke(0);
		parent.fill(0, 0, 0, 0);

		parent.rect(
				150,
				10,
				GlobalSetting.characterMaxHealth,
				20
				);
		parent.popMatrix();
		
		parent.pushMatrix();
		parent.stroke(0, 0, 0, 0);
		parent.fill(150, 30, 0, 255);

		parent.rect(
				150+1,
				10+1,
				GlobalSetting.characterHealthPoints-1,
				20-1
				);
		parent.popMatrix();			
	}
	public static void displayLives(){
		for(int i = 0; i < GlobalSetting.characterLives; i++){
			lives[i].display();
		}
	}
	public static void displayLevel(){

	}
	public static void displayScore(){
		
	}
}

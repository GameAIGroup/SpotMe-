package Variables;

import BasicStructures.*;

public  class Predictions {
	private int size;
	private Vector2[] predicts;
	
	public Predictions(){
		
	}
	
	public Predictions(int howManyBots){
		size = howManyBots;
		predicts = new Vector2[size]; 
	}
	
	public void setPredictions(Vector2[] bots){
		predicts = bots;
	}
	
	public void setMyPrediction(int botNumber, Vector2 myprediction){
		predicts[botNumber] = myprediction;
	}
	
	public Vector2[] getPredictions(){
		return predicts;
	}
	
	public Vector2 getMyPrediction(int botNumber){
		return predicts[botNumber];
	}
}

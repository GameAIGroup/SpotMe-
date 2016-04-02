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
		//initialize
		for(int i = 0; i < size; i++){
			predicts[i] = new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
		}
	}
	public void reInitial(){
		for(int i = 0; i < size; i++){
			predicts[i].setVector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
		}
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

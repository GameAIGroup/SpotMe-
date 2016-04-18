package BasicBehavior;

import BasicStructures.Vector2;
import MovementStructures.KinematicOperations;
import MovementStructures.SystemParameter;
import Variables.CommonFunction;
import Variables.GlobalSetting;
import Variables.PublicGraph;

public class Pursue {
	
	KinematicOperations operK;
	SystemParameter sys;
	
	public Pursue(KinematicOperations OperK, SystemParameter Sys){
		this.operK = OperK;
		this.sys = Sys;
	}
	
	public Vector2 makePrediction(Vector2 targetPastPosition, Vector2 targetCurrentPosition, Vector2 selfCurrentPosition){
		Vector2 Result = new Vector2( 0, 0 );
		//suppose time difference is 1 time unit.
		//System.out.println("input: ("+selfCurrentPosition.x+", "+selfCurrentPosition.y+") ("+targetPastPosition.x+", "+targetPastPosition.y+")("+targetCurrentPosition.x+", "+targetCurrentPosition.y+")");
		
		Vector2 estimatedVelocity = new Vector2(targetCurrentPosition.x - targetPastPosition.x, targetCurrentPosition.y - targetPastPosition.y);
		float targetMaxVelocity = operK.getLengthByVector2(estimatedVelocity);
		//System.out.println("guessV: ("+estimatedVelocity.x+", "+estimatedVelocity.y+")");
		
		float currentDistance =  operK.getDisBy2Points(targetCurrentPosition, selfCurrentPosition);
		//System.out.println("currentDis: ("+currentDistance+")");
		
		//assume pursue in maxSpeed
		
		float timeDiff = 0.0f;
		float currentNeededTime = GlobalSetting.wanderTimeBound;
		Vector2 newTargetPosition;

		float tempTime = 0.00001f;
		newTargetPosition = new Vector2(estimatedVelocity.x*tempTime+targetCurrentPosition.x, estimatedVelocity.y*tempTime+targetCurrentPosition.y);
		currentNeededTime = operK.getTheThirdLength(selfCurrentPosition, targetCurrentPosition, newTargetPosition)/ sys.getMaxV(); 
		
		
		while(currentNeededTime>timeDiff && timeDiff <GlobalSetting.wanderTimeBound){
			System.out.println("Differ Time = "+timeDiff);
			System.out.println("currentNeededTime = "+currentNeededTime  + ", "+sys.getMaxV());
			System.out.println("-------" + timeDiff);

			timeDiff = timeDiff+0.1f;
			//still not meet, and set bound for timeDiff to avoid the case that never reach the character.
			newTargetPosition = new Vector2((float)estimatedVelocity.x*timeDiff+targetCurrentPosition.x, (float)estimatedVelocity.y*timeDiff+targetCurrentPosition.y);
			currentNeededTime = operK.getTheThirdLength(selfCurrentPosition, targetCurrentPosition, newTargetPosition)/ sys.getMaxV(); 

		}

		// timeDiff is the needed time to reach prediction position
		Result = new Vector2(estimatedVelocity.x*timeDiff, estimatedVelocity.y*timeDiff);
		
		return Result;
	}
	public Vector2 makeUnitTimePrediction(Vector2 targetPastPosition, Vector2 targetCurrentPosition, Vector2 selfCurrentPosition){
		Vector2 Result = new Vector2( 0, 0 );
		//suppose time difference is 1 time unit.
		//System.out.println("input: ("+selfCurrentPosition.x+", "+selfCurrentPosition.y+") ("+targetPastPosition.x+", "+targetPastPosition.y+")("+targetCurrentPosition.x+", "+targetCurrentPosition.y+")");
		
		if(targetPastPosition.x < 0 &&targetPastPosition.y < 0){
			// no past position
			Result  = targetCurrentPosition;
		}
		else{
		
			Vector2 estimatedVelocity = new Vector2(targetCurrentPosition.x - targetPastPosition.x, targetCurrentPosition.y - targetPastPosition.y);
			float targetMaxVelocity = operK.getLengthByVector2(estimatedVelocity);
			//System.out.println("guessV: ("+estimatedVelocity.x+", "+estimatedVelocity.y+")");
			
			//float currentDistance =  operK.getDisBy2Points(targetCurrentPosition, selfCurrentPosition);
			//System.out.println("currentDis: ("+currentDistance+")");
			
			//assume pursue in maxSpeed
			
			Vector2 newTargetPosition;
	
			float tempTime = 1.0f;
			newTargetPosition = new Vector2(estimatedVelocity.x*tempTime+targetCurrentPosition.x, estimatedVelocity.y*tempTime+targetCurrentPosition.y);
			// timeDiff is the needed time to reach prediction position
			int closeIndex = CommonFunction.findClose(PublicGraph.G.nodeList, newTargetPosition);
			Result = PublicGraph.G.nodeList.get(closeIndex).coordinate;
			
		}
		return Result;
	}	
}

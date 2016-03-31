package GraphData;

import java.util.List;

import BasicStructures.Vector2;
import DrawData.CharacterDrop;
import DrawData.CharacterHuman;
import MovementStructures.KinematicOperations;

public class BotVision {
	public List<Node> visionNodes;
	public float maxOrientation;
	public float minOrientation;
	public double maxAllowedDistance;
	
	public BotVision(float maxOr, float minOr, float maxDistace, List<Node> nodes){
		this.visionNodes = nodes;
		this.maxAllowedDistance = maxDistace;
		this.maxOrientation = maxOr;
		this.minOrientation = minOr;
	}
	
	public boolean isCharacterInVision(CharacterDrop character, CharacterHuman bot, KinematicOperations OperK)
	{
		boolean characterInVision = false;
		
		/*Vector2 distanceVector = new Vector2((character.getPosition().x - bot.getPosition().x), (character.getPosition().y - bot.getPosition().y));
		float currentOr = bot.getOrientation();
		float resultantOr = OperK.getOrientationByV(currentOr, distanceVector);
		float changeInOr = bot.getChangeInOrientation(resultantOr, currentOr);
		double distance = Math.sqrt(Math.pow(character.getPosition().x - bot.getPosition().x, 2) + Math.pow(character.getPosition().y - bot.getPosition().y, 2));
		
		if (distance < maxAllowedDistance)
		{
			if ((changeInOr < maxOrientation && changeInOr > minOrientation))
			{
				characterInVision = true;
			}
		}*/
		
		for (Node node: visionNodes)
		{
			if ((character.getPosition().x == node.coordinate.x) && (character.getPosition().y == node.coordinate.y))
			{
				characterInVision = true;
				break;
			}
		}
		return characterInVision;
	}
}

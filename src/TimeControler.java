import java.util.concurrent.TimeUnit;

public class TimeControler {
	public long startTime;
	public long startTime2;
	public long endTime;
	
	public void initialTimer(){
		startTime = System.currentTimeMillis();
	}
	public void setStartTime(){
		startTime = System.currentTimeMillis();
	}
	public boolean checkTimeSlot(long TimeDiffer){
		endTime = System.currentTimeMillis();
		if((endTime - startTime) >= TimeDiffer){
			//System.out.println("time slot" + (endTime - startTime)/1000F);
			setStartTime();
			return true;
		}
		return false;
	}
	public long getTimeDiffer(){
		long result;
		
		endTime = System.currentTimeMillis();
		result = (endTime - startTime);
		return result;
	}
	public String computeTime(long diff){
		String time;
		time = String.format("%06d", 
				TimeUnit.MILLISECONDS.toSeconds(diff)); 		
		return time;
	}
}

package datastructures;

public class TransitionFactory {

	public static final String TRANSITION = "Transition";
	public static final String Phase = "Phase";
	
	public static TransitionInfo createTransitionInfo(String type){
		if(type.equals(TRANSITION))
			return new Transition();
		
		//TODO add more TransitionInfo returns, see Phase etc
		return null;
	}
}

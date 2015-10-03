package algorithms;

import java.util.ArrayList;

import output.Pattern;
import parameters.Parameters;

public abstract class  Algorithm {
	
	private Parameters parameter;
	public abstract void run();
	public abstract ArrayList<Pattern> getPatterns();
	
	public Algorithm(Parameters parameter) {
		this.setParameter(parameter);
	}

	public Parameters getParameter() {
		return parameter;
	}

	public void setParameter(Parameters parameter) {
		this.parameter = parameter;
	}
	
}

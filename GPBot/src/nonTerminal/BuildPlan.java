package nonTerminal;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class BuildPlan  extends GPNode{
	/**
	 * this is a simple build plan
	 */
	
	private static final long serialVersionUID = 1999899518396818868L;

	
	public String toString() {
		return "build plan";// This is for the visual representation
	}

	public int expectedChildren() {
		return 5;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		for (int i=0; i<5; i++ ){
			children[i].eval(state, thread, input, stack, individual, problem);
		}
	}

}
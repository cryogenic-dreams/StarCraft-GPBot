package nonTerminal;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class BasicBuildings extends GPNode {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	public String toString() {
		return "basicbuildings";// This is for the visuals
	}

	@Override
	public String name() {
		return "basicbuildings";//this is for the magic
	}


	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		for(int i = 0; i < children.length; i++) {
			children[i].eval(state, thread, input, stack, individual, problem);
		}

	}
}

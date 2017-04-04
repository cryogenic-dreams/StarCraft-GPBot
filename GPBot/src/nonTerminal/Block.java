package nonTerminal;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Block extends GPNode{
	
	/**
	 * so I can have as many yummy terminals as I want!!!
	 */
	private static final long serialVersionUID = -529302468804088925L;

	public String toString() {
		return "block of terminals,lel";// This is for the visual representation
	}

	public int expectedChildren() {
		return 6;//just as an example
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		for (int i=0; i<6; i++ ){
			children[i].eval(state, thread, input, stack, individual, problem);
		}
	}

}

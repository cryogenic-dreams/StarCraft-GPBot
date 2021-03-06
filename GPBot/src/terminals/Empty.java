package terminals;


import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Empty extends GPNode {

	private static final long serialVersionUID = 1;

	
	public String toString() {
		return "empty";// This is for the visual representation
	}

	@Override
	public String name() {
		return "empty";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 0;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		//This is an empty node, just that

    }
}

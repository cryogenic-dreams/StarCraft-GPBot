package nonTerminal;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class CovertOpsSquads extends GPNode {

	private static final long serialVersionUID = 1;

	
	public String toString() {
		return "covert_ops_squads";// This is for the visual representation
	}

	@Override
	public String name() {
		return "covert_ops_squads";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 2;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		children[0].eval(state, thread, input, stack, individual, problem);
		children[1].eval(state, thread, input, stack, individual, problem);

    }
}

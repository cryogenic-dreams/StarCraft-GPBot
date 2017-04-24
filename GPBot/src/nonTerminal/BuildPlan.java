package nonTerminal;


import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;


public class BuildPlan extends GPNode {

	
	/**
	 * Buildplan contains basic buildings and normal buildings
	 */
	private static final long serialVersionUID = -6139156416511133036L;

	public String toString() {
		return "buildplan";// This is for the visual
	}

	@Override
	public String name() {
		return "buildplan";//this is for the magic
	}

	public int expectedChildren() {
		return 2;//this
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		children[0].eval(state, thread, input, stack, individual, problem);
		children[1].eval(state, thread, input, stack, individual, problem);

	}
}

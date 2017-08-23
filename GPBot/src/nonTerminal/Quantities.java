package nonTerminal;


import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;


public class Quantities extends GPNode{

	private static final long serialVersionUID = 1;


	public String toString() {
		return "quantities";// This is for the visual representation
	}

	@Override
	public String name() {
		return "quantities";//this is for the magic, aka the grammar
	}
	public int expectedChildren() {
		return 2;//this
	}


	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		int quantity = ((GameData) input).q;
		children[0].eval(state, thread, input, stack, individual, problem);
		((GameData) input).q = quantity + ((GameData) input).q;
		children[1].eval(state, thread, input, stack, individual, problem);


    }
}

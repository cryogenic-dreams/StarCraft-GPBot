package nonTerminal;

import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Ini extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "ini";// This is for the visual representation
	}

	@Override
	public String name() {
		return "ini";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 2;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		//this node differentiates between passive mode and aggressive mode
		//initially, the tree containing this root node branches in two trees: passive and aggressive
		GameData gd = ((GameData) (input));
		if (gd.state == 0) {
			children[0].eval(state, thread, input, stack, individual, problem);
		} else {
			children[1].eval(state, thread, input, stack, individual, problem);
		}
	}
}

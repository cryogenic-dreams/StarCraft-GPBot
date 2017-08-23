package terminals;


import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;


public class Quantity1 extends GPNode{

	private static final long serialVersionUID = 1;


	public String toString() {
		return "quantity1";// This is for the visual representation
	}

	@Override
	public String name() {
		return "quantity1";//this is for the magic, aka the grammar
	}
	public int expectedChildren() {
		return 0;//this
	}


	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		((GameData) input).q = 1;

    }
}
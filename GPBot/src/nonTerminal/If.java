package nonTerminal;


import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;


public class If extends GPNode {

	private static final long serialVersionUID = 1;

	
	public String toString() {
		return "if";// This is for the visual representation
	}

	@Override
	public String name() {
		return "if";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 3;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		GameData data = (GameData) input;
		children[0].eval(state, thread, input, stack, individual, problem);
		if(data.condition == true){
			children[1].eval(state, thread, input, stack, individual, problem);
		}
		else{
			children[2].eval(state, thread, input, stack, individual, problem);
		}

    }
}

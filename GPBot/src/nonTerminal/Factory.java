package nonTerminal;

import bwapi.UnitType;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.UnitTuple;

public class Factory extends GPNode {

	private static final long serialVersionUID = 1;


	public String toString() {
		return "factory";// This is for the visual representation
	}

	@Override
	public String name() {
		return "factory";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 3;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		children[2].eval(state, thread, input, stack, individual, problem);
		children[0].eval(state, thread, input, stack, individual, problem);//squads
		rd.bp.push(new UnitTuple(UnitType.Terran_Factory, rd.s)); // Push of yourself and your last node, which is the supply or the quantity
		int supply = rd.s;
		children[1].eval(state, thread, input, stack, individual, problem);
		System.out.println("factory | supply: " + supply);
    }
}

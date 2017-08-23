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

public class Armory extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "armory";// This is for the visual representation
	}

	@Override
	public String name() {
		return "armory";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 3;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		GameData rd = ((GameData) (input));
		children[0].eval(state, thread, input, stack, individual, problem);// tech
		children[1].eval(state, thread, input, stack, individual, problem);// squads
		rd.bp.push(new UnitTuple(UnitType.Terran_Armory, 0)); 
		children[2].eval(state, thread, input, stack, individual, problem);// pre-building
		System.out.println("armory");
	}
}

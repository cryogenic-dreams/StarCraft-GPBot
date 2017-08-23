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

public class EngineeringBay extends GPNode {

	private static final long serialVersionUID = 1;


	public String toString() {
		return "engineering_bay";// This is for the visual representation
	}

	@Override
	public String name() {
		return "engineering_bay";//this is for the magic, aka the grammar
	}
	public int expectedChildren() {
		return 1;//this
	}


	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		children[0].eval(state, thread, input, stack, individual, problem);//tech
		rd.bp.push(new UnitTuple(UnitType.Terran_Engineering_Bay, 0)); // Push of yourself and your last node, which is the supply or the quantity
		System.out.println("engineering_bay");
    }
}

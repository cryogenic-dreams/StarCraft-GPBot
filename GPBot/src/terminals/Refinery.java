package terminals;

import bwapi.UnitType;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.UnitTuple;

public class Refinery extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "refinery";// This is for the visual representation
	}

	@Override
	public String name() {
		return "refinery";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 0;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
	
		rd.bp.push(new UnitTuple(UnitType.Terran_Refinery, 0));
		System.out.println(UnitType.Terran_Refinery);
	}
}

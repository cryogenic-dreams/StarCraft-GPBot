package terminals;

import bwapi.TechType;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.TechTuple;

public class CloakingField extends GPNode {

	private static final long serialVersionUID = 1;


	public String toString() {
		return "cloaking_field";// This is for the visual representation
	}

	@Override
	public String name() {
		return "cloaking_field";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 0;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		rd.bp.push(new TechTuple(TechType.Cloaking_Field, 0)); // Push of yourself and your last node, which is the supply or the quantity
		System.out.println("cloaking_field");
    }
}

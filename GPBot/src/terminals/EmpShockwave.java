package terminals;


import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.TechTuple;
import bwapi.TechType;

public class EmpShockwave extends GPNode {

	private static final long serialVersionUID = 1;


	public String toString() {
		return "emp_shockwave";// This is for the visual representation
	}

	@Override
	public String name() {
		return "emp_shockwave";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 0;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		children[children.length - 1].eval(state, thread, input, stack, individual, problem);
		rd.bp.push(new TechTuple(TechType.EMP_Shockwave, 0)); // Push of yourself and your last node, which is the supply or the quantity
		System.out.println("emp_shockwave");
    }
}

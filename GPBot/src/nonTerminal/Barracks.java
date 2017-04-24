package nonTerminal;

import bwapi.UnitType;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.Tuple;

public class Barracks extends GPNode {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7044772925776571874L;

	public String toString() {
		return "barracks";// This is for the visuals
	}

	@Override
	public String name() {
		return "barracks";//this is for the magic
	}

	public int expectedChildren() {
		return 1;//this
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		GameData rd = ((GameData) (input));
		children[children.length - 1].eval(state, thread, input, stack, individual, problem);
		int supply = rd.s;
		rd.bp.push(new Tuple(UnitType.Terran_Barracks, rd.s)); // Push of yourself and your child node supply
										//rd.s is the Supply
		for(int i = 0; i < children.length - 1; i++) {
			// Eval de cada hijo que es otro edificio
			children[i].eval(state, thread, input, stack, individual, problem);
		}
		System.out.println("Barracks | supply: " + supply);

	}
}

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

public class Tank extends GPNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -237112531994301217L;

	
	public String toString() {
		return "tank";// This is for the visual representation
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "tank";//this is for the magic
	}

	public int expectedChildren() {
		return 2;//this
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		children[children.length - 1].eval(state, thread, input, stack, individual, problem);
		rd.bp.push(new Tuple(UnitType.Terran_Siege_Tank_Siege_Mode, rd.q)); // Push de ti mismo mas el supply que te de tu nodo hijo
		System.out.println("Tank");

		for(int i = 0; i < children.length - 1; i++) {
			// Eval de cada hijo que es otro edificio
			children[i].eval(state, thread, input, stack, individual, problem);
		}
    }
}

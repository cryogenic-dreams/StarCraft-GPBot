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

public class ComsatStation extends GPNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -372031124878543360L;

	public String toString() {
		return "comsat_station";// This is for the visual
	}

	@Override
	public String name() {
		return "comsat_station";//this is for the magic
	}

	public int expectedChildren() {
		return 2;//this
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		children[children.length - 1].eval(state, thread, input, stack, individual, problem);
		int supply = rd.s;
		rd.bp.push(new Tuple(UnitType.Terran_Comsat_Station, rd.s)); // Push de ti mismo mas el supply que te de tu nodo hijo

		for(int i = 0; i < children.length - 1; i++) {
			// Eval de cada hijo que es otro edificio
			children[i].eval(state, thread, input, stack, individual, problem);
		}
		System.out.println("Comsat Station | supply: "+ supply);

	}
}

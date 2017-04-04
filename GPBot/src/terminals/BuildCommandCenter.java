package terminals;

import bwapi.UnitType;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import problem.StarCraftBot;
import support.Tuple;

public class BuildCommandCenter extends GPNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6193914493002309043L;

	/**
	 * 
	 */

	public String toString() {
		return "building command center";// This is for the visual
											// representation
	}

	public int expectedChildren() {
		return 0;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		rd.g = ((StarCraftBot) problem).myDamnGame;

		Tuple t = new Tuple(UnitType.Terran_Command_Center, 3); // the 3 will be
																// a random or
																// pseudo random
																// value, not
																// sure yet
		rd.bp.push(t);
		System.out.println("I'm building command center");

	}
}
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

public class BuildBunker extends GPNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 237593496161913978L;

	public String toString() {
		return "building bunker";// This is for the visual
									// representation
	}

	public int expectedChildren() {
		return 0;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		rd.g = ((StarCraftBot) problem).myDamnGame;

		Tuple t = new Tuple(UnitType.Terran_Bunker, 3); // the 3 will be a
														// random or pseudo
														// random value, not
														// sure yet
		rd.bp.push(t);
		System.out.println("I'm building bunker");

	}
}
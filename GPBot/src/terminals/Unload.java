package terminals;

import bwapi.Unit;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Unload extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "unload";// This is for the visual representation
	}

	@Override
	public String name() {
		return "unload";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 0;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		// this does:
			//unloads all the ships/bunkers

		GameData gd = ((GameData) (input));
		if (!gd.squads.isEmpty()) {
			for (Unit unit : gd.squads) {
				if (unit.canUnload()) {
					unit.unloadAll();
				}
			}
		}
	}
}

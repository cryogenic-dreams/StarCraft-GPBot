package nonTerminal;

import bwapi.Position;
import bwapi.Unit;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class SCVExplore extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "scv_explore";// This is for the visual representation
	}

	@Override
	public String name() {
		return "scv_explore";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 1;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		GameData gd = (GameData) input;
		for (Unit myUnit : gd.workers) {
			myUnit.move(new Position(gd.x, gd.y));
			break;
		}
	}
}

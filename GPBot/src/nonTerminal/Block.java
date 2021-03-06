package nonTerminal;

import bwapi.Position;
import bwapi.Unit;
import bwta.BWTA;
import bwta.Chokepoint;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Block extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "block";// This is for the visual representation
	}

	@Override
	public String name() {
		return "block";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 1;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		// this does:
		// Given a number of units q and its type, it positions them in the
		// nearest chokepoint
		GameData gd = ((GameData) (input));
		gd.q = 0;
		children[0].eval(state, thread, input, stack, individual, problem);
		int q = gd.q;
		if (!BWTA.getChokepoints().isEmpty()) {
			Chokepoint c = BWTA.getChokepoints().get(0);
			Position p = c.getPoint();

			if (!gd.squads.isEmpty()) {
				for (Unit unit : gd.squads) {
					if (unit.isIdle()) {
						unit.move(p);
						q--;
					}
					if (q <= 0) {
						break;
					}
				}
			}
		}
	}
}

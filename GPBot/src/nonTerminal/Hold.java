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
import support.Tuple;

public class Hold extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "2";// This is for the visual representation
	}

	@Override
	public String name() {
		return "2";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 2;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		// this does:
		// at the current position of a unit, it holds it
		// given a unit type and a quantity
		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);
		children[1].eval(state, thread, input, stack, individual, problem);
		int q = gd.q;
		if (!gd.squads.isEmpty()) {

			for (Unit unit : gd.squads) {
				if ((unit.getType() == gd.ut) && (unit.isIdle())) {
					unit.holdPosition();
					q--;
				}
				if (q <= 0) {
					break;
				}
			}
		}
	}
}

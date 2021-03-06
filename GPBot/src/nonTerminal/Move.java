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

public class Move extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "move";// This is for the visual representation
	}

	@Override
	public String name() {
		return "move";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 2;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		//this does:
			//Given a number of units, their type and a position
			//it moves them to that position
		GameData gd = ((GameData) (input));
		gd.q = 0;
		children[0].eval(state, thread, input, stack, individual, problem);
		children[1].eval(state, thread, input, stack, individual, problem);

		int q = gd.q;
		Position p = new Position (gd.x, gd.y);

		if (!gd.squads.isEmpty()) {
			for (Unit unit : gd.squads) {
				if ((unit.isIdle()) && (unit.canMove())) {
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

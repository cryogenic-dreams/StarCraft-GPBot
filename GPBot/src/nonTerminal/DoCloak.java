package nonTerminal;

import bwapi.Unit;
import bwapi.UnitType;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class DoCloak extends GPNode {

	private static final long serialVersionUID = 1;

	
	public String toString() {
		return "do_cloak";// This is for the visual representation
	}

	@Override
	public String name() {
		return "do_cloak";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 1;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		//this does:
			//Given a number of units, this node orders them to cloak
		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);

		int q = gd.q;

		if (!gd.squads.isEmpty()) {
			for (Unit unit : gd.squads) {
				if (((unit.getType() == UnitType.Terran_Ghost) || (unit.getType() == UnitType.Terran_Wraith)) && (unit.isIdle())) {
					unit.cloak();
					q--;
				}
				if (q <= 0) {
					break;
				}
			}
		}

    }
}

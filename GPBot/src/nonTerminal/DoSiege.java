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

public class DoSiege extends GPNode {

	private static final long serialVersionUID = 1;

	
	public String toString() {
		return "do_siege";// This is for the visual representation
	}

	@Override
	public String name() {
		return "do_siege";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 1;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);

		int q = gd.q;

		if (!gd.squads.isEmpty()) {
			for (Unit unit : gd.squads) {
				//no need to check if is sieged or not, the api already changes the unittype
				if ((unit.getType() == UnitType.Terran_Siege_Tank_Tank_Mode ) && (unit.isIdle())) {
					unit.siege();
					q--;
				}
				if (q <= 0) {
					break;
				}
			}
		}

    }
}

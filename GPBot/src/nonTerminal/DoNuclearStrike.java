package nonTerminal;

import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class DoNuclearStrike extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "do_nuclear_strike";// This is for the visual representation
	}

	@Override
	public String name() {
		return "do_nuclear_strike";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 1;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData gd = ((GameData) (input));
		gd.q = 0;
		children[0].eval(state, thread, input, stack, individual, problem);// enemy

		if (!gd.enemies.isEmpty()) {
			if (!gd.squads.isEmpty()) {
				for (Unit unit : gd.squads) {
					if ((unit.getType() == UnitType.Terran_Ghost) && (!unit.isAttacking())) {
						Unit enemy1 = gd.enemies.get(0);
						int distance = unit.getDistance(gd.enemies.get(0));
						for (Unit enemy2 : gd.enemies) {
							// get nearest enemy
							if (unit.getDistance(enemy2) < distance) {
								distance = unit.getDistance(enemy2);
								enemy1 = enemy2;
							}
						}

						if (TechType.Nuclear_Strike.targetsPosition()) {
							unit.useTech(TechType.Nuclear_Strike, enemy1.getPosition());
						} else if (TechType.Nuclear_Strike.targetsUnit()) {
							unit.useTech(TechType.Nuclear_Strike, enemy1);
						} else
							unit.useTech(TechType.Nuclear_Strike);
						break;
					}

				}
			}
		}

	}
}

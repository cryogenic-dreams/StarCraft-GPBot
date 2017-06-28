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

public class DoYamato extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "do_yamato";// This is for the visual representation
	}

	@Override
	public String name() {
		return "do_yamato";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 2;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		// this does:
		// Given a number of bc, they use yamato on the nearest enemy
		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);
		children[1].eval(state, thread, input, stack, individual, problem);

		int q = gd.q;
		if (!gd.enemies.isEmpty()) {
			if (!gd.squads.isEmpty()) {
				for (Unit unit : gd.squads) {
					if ((unit.getType() == UnitType.Terran_Battlecruiser) && (!unit.isAttacking())) {
						Unit enemy1 = gd.enemies.get(0);
						int distance = unit.getDistance(gd.enemies.get(0));
						for (Unit enemy2 : gd.enemies) {
							// get nearest enemy
							if (unit.getDistance(enemy2) < distance) {
								distance = unit.getDistance(enemy2);
								enemy1 = enemy2;
							}
						}

						if (TechType.Yamato_Gun.targetsPosition()) {
							unit.useTech(TechType.Yamato_Gun, enemy1.getPosition());
						} else if (TechType.Yamato_Gun.targetsUnit()) {
							unit.useTech(TechType.Yamato_Gun, enemy1);
						} else
							unit.useTech(TechType.Yamato_Gun);
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

package nonTerminal;

import java.util.ArrayList;
import java.util.List;

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

public class DoLockDown extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "do_lockdown";// This is for the visual representation
	}

	@Override
	public String name() {
		return "do_lockdown";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 2;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		// this does:
		// Given a number of Vessel units, this node orders them to cast lockdown
		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);
		children[1].eval(state, thread, input, stack, individual, problem);

		int q = gd.q;
		List<Unit> mech_enemies = new ArrayList<Unit>();
		if (!gd.enemies.isEmpty()) {
			for (Unit e : gd.enemies) {
				// get enemies with energy
				if (e.getType().isMechanical()) {
					mech_enemies.add(e);
				}
			}
		}
		if (!mech_enemies.isEmpty()) {
			if (!gd.squads.isEmpty()) {
				for (Unit unit : gd.squads) {
					if ((unit.getType() == UnitType.Terran_Ghost) && (!unit.isAttacking())) {
						Unit enemy1 = mech_enemies.get(0);
						int distance = unit.getDistance(mech_enemies.get(0));
						for (Unit enemy2 : mech_enemies) {
							// get nearest enemy
							if (unit.getDistance(enemy2) < distance) {
								distance = unit.getDistance(enemy2);
								enemy1 = enemy2;
							}
						}

						if (TechType.Lockdown.targetsPosition()) {
							unit.useTech(TechType.Lockdown, enemy1.getPosition());
						} else if (TechType.Lockdown.targetsUnit()) {
							unit.useTech(TechType.Lockdown, enemy1);
						} else
							unit.useTech(TechType.Lockdown);
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

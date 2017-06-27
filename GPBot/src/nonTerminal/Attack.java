package nonTerminal;

import bwapi.Unit;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Attack extends GPNode {
	//this node is very important, the probability is set to 70 percent 
	private static final long serialVersionUID = 1;

	public String toString() {
		return "attack";// This is for the visual representation
	}

	@Override
	public String name() {
		return "attack";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 3;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		// this does:
			//At a given quantity q, gets enemies and sends q number of units to attack
			//this only executes when state == 1, that is, when bot is under aggressive mode
		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);
		children[1].eval(state, thread, input, stack, individual, problem);
		children[2].eval(state, thread, input, stack, individual, problem);
		int q = gd.q;
		if ((!gd.enemies.isEmpty()) && (!gd.squads.isEmpty())) {
			for (Unit unit : gd.squads) {
				if (unit.getType() == gd.ut && (!unit.isAttacking()) && (unit.canAttack())) {
					unit.attack(gd.enemies.get(0));
					q--;
				}
				if (q <= 0) {
					break;
				}
			}
		}
	}
}

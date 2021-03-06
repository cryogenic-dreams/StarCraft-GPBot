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
		return 2;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		// this does:
			//At a given quantity q, gets enemies and sends q number of units to attack
			//this only executes when state == 1, that is, when bot is under aggressive mode
		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);//not used
		children[1].eval(state, thread, input, stack, individual, problem);//not used
		
		//int q = gd.q;
		if ((!gd.enemies.isEmpty()) && (!gd.squads.isEmpty())) {
			for (Unit unit : gd.squads) {
				if ((!unit.isAttacking()) && (unit.canAttack())) {
					Unit enemy1 = gd.enemies.get(0);
					int distance = unit.getDistance(gd.enemies.get(0));
					for (Unit enemy2 : gd.enemies) {
						// get nearest enemy
						if (unit.getDistance(enemy2) < distance) {
							distance = unit.getDistance(enemy2);
							enemy1 = enemy2;
						}
					}
					unit.attack(enemy1);
					//q--;
				}
//				if (q <= 0) {
//					break;
//				}
			}
		}
	}
}

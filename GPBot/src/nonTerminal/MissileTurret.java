package nonTerminal;

import bwapi.UnitType;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.UnitTuple;

public class MissileTurret extends GPNode {

	private static final long serialVersionUID = 1;


	public String toString() {
		return "missile_turret";// This is for the visual representation
	}

	@Override
	public String name() {
		return "missile_turret";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 1;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		rd.bp.push(new UnitTuple(UnitType.Terran_Missile_Turret, 0)); // Push of yourself and your last node, which is the supply or the quantity
		children[0].eval(state, thread, input, stack, individual, problem);
		System.out.println("missile_turret");
    }
}

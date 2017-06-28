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

public class DoDefensiveMatrix extends GPNode {

	private static final long serialVersionUID = 1;

	
	public String toString() {
		return "do_defensive_matrix";// This is for the visual representation
	}

	@Override
	public String name() {
		return "do_defensive_matrix";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 1;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		//this does:
			//Given a number of Vessel units, this node orders them to cast a defense matrix
		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);

		int q = gd.q;

		if (!gd.squads.isEmpty()) {
			for (Unit unit : gd.squads) {
				if (((unit.getType() == UnitType.Terran_Science_Vessel) && (!unit.isAttacking()) && (!unit.isDefenseMatrixed()))) {
					if(TechType.Defensive_Matrix.targetsPosition()){
						unit.useTech(TechType.Defensive_Matrix, unit.getPosition());
					}
					else if (TechType.Defensive_Matrix.targetsUnit()){
						unit.useTech(TechType.Defensive_Matrix, unit);
					}
					else unit.useTech(TechType.Defensive_Matrix);
					q--;
				}
				if (q <= 0) {
					break;
				}
			}
		}

    }
}

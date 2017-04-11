package terminals;

import bwapi.UnitType;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.Tuple;

public class SupplyDepot extends GPNode {

	/**
	 * 
	 */

	//OO gods forbid me
	private String name = "supply_depot";
	private int noChildren = 0;
	private UnitType unit = UnitType.Terran_Supply_Depot;
	
	
	
	public String toString() {
		return this.name;// This is for the visual
							// representation
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return this.name;//this is for the magic
	}

	public int expectedChildren() {
		return noChildren;//this
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		rd.bp.push(new Tuple(unit, 0));

	}
}

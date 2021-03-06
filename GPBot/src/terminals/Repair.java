package terminals;

import bwapi.Game;
import bwapi.Unit;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Repair extends GPNode {

	private static final long serialVersionUID = 1;

	public String toString() {
		return "repair";// This is for the visual representation
	}

	@Override
	public String name() {
		return "repair";// this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 0;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData data = (GameData) input;
		Game game = data.g;
		// sends 1st worker to repair 1 building
		for (Unit building : game.self().getUnits()) {
			if (building.getType().isBuilding()) {
				if (building.getHitPoints() <= building.getInitialHitPoints() / 2) {
					for (Unit worker : game.self().getUnits()) {
						if (worker.getType().isWorker()){
							building.repair(building);
							break;
						}	
					}
					break;
				}
			}
		}
	}
}


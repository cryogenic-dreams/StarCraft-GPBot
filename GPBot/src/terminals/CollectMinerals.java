package terminals;

import bwapi.Player;
import bwapi.Unit;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import problem.StarCraftBot;

public class CollectMinerals extends GPNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3572155502474903790L;

	public String toString() {
		return "collect minerals";//This is for the visual representation
	}

	public int expectedChildren() {
		return 0;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		GameData rd = ((GameData) (input));
		rd.g = ((StarCraftBot) problem).myDamnGame;

		Player self = rd.g.self();
		for (Unit myUnit : self.getUnits()) {
			// if it's a worker and it's idle, send it to the closest mineral
			// patch
			if (myUnit.getType().isWorker() && myUnit.isIdle()) {
				Unit closestMineral = null;

				// find the closest mineral
				for (Unit neutralUnit : rd.g.neutral().getUnits()) {
					if (neutralUnit.getType().isMineralField()) {

						if (closestMineral == null
								|| myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
							closestMineral = neutralUnit;

						}
					}
				}

				// if a mineral patch was found, send the worker to gather it
				if (closestMineral != null) {
					myUnit.gather(closestMineral, false);
					System.out.println("I'm collecting minerals");

				}
			}
		}
	}
}

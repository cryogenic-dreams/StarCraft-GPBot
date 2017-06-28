package nonTerminal;

import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.Tuple;

public class DoOpticalFlare extends GPNode {

	private static final long serialVersionUID = 1;

	
	public String toString() {
		return "do_optical_flare";// This is for the visual representation
	}

	@Override
	public String name() {
		return "do_optical_flare";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 1;
	}

		public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		//this does:
			//1 to 1 spell, casted from a medic to a single unit
		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);//enemy

		if (!gd.squads.isEmpty()) {
			for (Unit unit : gd.squads) {
				if (unit.getType() == UnitType.Medic) {
					Unit enemy1 = gd.enemies.get(0);
					int distance = unit.getDistance(gd.enemies.get(0));
					for (Unit enemy2 : gd.enemies){
						//get nearest enemy
						if (unit.getDistance(enemy2) < distance){
							distance = unit.getDistance(enemy2);
							enemy1 = enemy2;
						}
					}
					
					if(TechType.Optical_Flare.targetsPosition()){
						unit.useTech(TechType.Optical_Flare, enemy1.getPosition());
					}
					else if (TechType.Optical_Flare.targetsUnit()){
						unit.useTech(TechType.Optical_Flare, enemy1);
					}
					else unit.useTech(TechType.Optical_Flare);
					break;
				}
				
			}
		}

    }
}

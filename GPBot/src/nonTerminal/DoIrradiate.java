package nonTerminal;

import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.Tuple;

public class DoIrradiate extends GPNode {

	private static final long serialVersionUID = 1;

	
	public String toString() {
		return "do_irradiate";// This is for the visual representation
	}

	@Override
	public String name() {
		return "do_irradiate";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 2;
	}

		public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		//this does:
			//Given a number of Vessel units, this node orders them to cast a shockwave
		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);
		children[1].eval(state, thread, input, stack, individual, problem);
		
		int q = gd.q;
		List<Unit> bio_enemies = null;
		if(!gd.enemies.isEmpty()){
			for (Unit e : gd.enemies){
			//get enemies with energy
				if(e.getType().isOrganic()){
					bio_enemies.add(e);
				}
			}
		}

		if (!gd.squads.isEmpty()) {
			for (Unit unit : gd.squads) {
				if ((unit.getType() == UnitType.Science_Vessel) && (!unit.isAttacking())) {
					Unit enemy1 = bio_enemies.get(0);
					int distance = unit.getDistance(bio_enemies.get(0));
					for (Unit enemy2 : bio_enemies){
						//get nearest enemy
						if (unit.getDistance(enemy2) < distance){
							distance = unit.getDistance(enemy2);
							enemy1 = enemy2;
						}
					}
					
					if(TechType.Defensive_Matrix.targetsPosition()){
						unit.useTech(TechType.Irradiate, enemy1.getPosition());
					}
					else if (TechType.Defensive_Matrix.targetsUnit()){
						unit.useTech(TechType.Irradiate, enemy1);
					}
					else unit.useTech(TechType.Irradiate);
					q--;
				}
				if (q <= 0) {
					break;
				}
			}
		}

    }

    

    
}

package nonTerminal;

import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.Tuple;

public class DoEMP extends GPNode {

	private static final long serialVersionUID = 1;

	
	public String toString() {
		return "do_EMP";// This is for the visual representation
	}

	@Override
	public String name() {
		return "do_EMP";//this is for the magic, aka the grammar
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
		List<Unit> energy_enemies = null;
		if(!gd.enemies.isEmpty()){
			for (Unit e : gd.enemies){
			//get enemies with energy
				if(e.getEnergy() > 0){
					energy_enemies.add(e);
				}
			}
		}

		if (!gd.squads.isEmpty()) {
			for (Unit unit : gd.squads) {
				if ((unit.getType() == UnitType.Science_Vessel) && (!unit.isAttacking())) {
					Unit enemy1 = energy_enemies.get(0);
					int distance = unit.getDistance(energy_enemies.get(0));
					for (Unit enemy2 : energy_enemies){
						//get nearest enemy
						if (unit.getDistance(enemy2) < distance){
							distance = unit.getDistance(enemy2);
							enemy1 = enemy2;
						}
					}
					
					if(TechType.Defensive_Matrix.targetsPosition()){
						unit.useTech(TechType.EMP_Shockwave, enemy1.getPosition());
					}
					else if (TechType.Defensive_Matrix.targetsUnit()){
						unit.useTech(TechType.EMP_Shockwave, enemy1);
					}
					else unit.useTech(TechType.EMP_Shockwave);
					q--;
				}
				if (q <= 0) {
					break;
				}
			}
		}

    }
}

package nonTerminal;

import bwapi.TechType;
import bwapi.Unit;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class DoStimPack extends GPNode {

	private static final long serialVersionUID = 1;

	
	public String toString() {
		return "do_stim_pack";// This is for the visual representation
	}

	@Override
	public String name() {
		return "do_stim_pack";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 1;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData gd = ((GameData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);

		int q = gd.q;

		if (!gd.squads.isEmpty()) {
			for (Unit unit : gd.squads) {
				if(TechType.Stim_Packs.targetsPosition()){
						unit.useTech(TechType.Stim_Packs, unit.getPosition());
					}
					else if (TechType.Stim_Packs.targetsUnit()){
						unit.useTech(TechType.Stim_Packs, unit);
					}
					else unit.useTech(TechType.Stim_Packs);
					q--;
				if (q <= 0) {
					break;
				}
			}
		}

    }
}

﻿package nonTerminal;

import bwapi.UnitType;
import data.GameData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import support.UnitTuple;

public class PhysicsLab extends GPNode {

	private static final long serialVersionUID = 1;


	public String toString() {
		return "physics_lab";// This is for the visual representation
	}

	@Override
	public String name() {
		return "physics_lab";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 3;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		children[0].eval(state, thread, input, stack, individual, problem);//tech
		children[1].eval(state, thread, input, stack, individual, problem);//squads
		rd.bp.push(new UnitTuple(UnitType.Terran_Physics_Lab, 0)); // Push of yourself and your last node, which is the supply or the quantity
		children[2].eval(state, thread, input, stack, individual, problem);
		System.out.println("physics_lab");
    }
}

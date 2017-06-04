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

public class MachineShop extends GPNode {

	private static final long serialVersionUID = 1;


	public String toString() {
		return "machine_shop";// This is for the visual representation
	}

	@Override
	public String name() {
		return "machine_shop";//this is for the magic, aka the grammar
	}

	public int expectedChildren() {
		return 4;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		GameData rd = ((GameData) (input));
		children[children.length - 1].eval(state, thread, input, stack, individual, problem);
		rd.bp.push(new UnitTuple(UnitType.Terran_Machine_Shop, rd.s)); // Push of yourself and your last node, which is the supply or the quantity
		int supply = rd.s;
		for(int i = 0; i < children.length - 1; i++) {
			children[i].eval(state, thread, input, stack, individual, problem);
		}
		System.out.println("machine_shop | supply: " + supply);
    }
}

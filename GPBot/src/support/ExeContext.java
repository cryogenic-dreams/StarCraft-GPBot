package support;

import java.io.Serializable;

import data.GameData;
import ec.EvolutionState;
import ec.gp.ADFStack;
import ec.gp.GPIndividual;
import problem.StarCraftBot;

public class ExeContext  implements Serializable{

	private EvolutionState state;
	private GPIndividual ind;
	private int threadnum;
	private ADFStack stack;
	private GameData input;
	private StarCraftBot stbot;
	private static final long serialVersionUID = 1;

	public ExeContext(EvolutionState state, GPIndividual ind, int threadnum, ADFStack stack, GameData input,
			StarCraftBot stbot) {
		super();
		this.state = state;
		this.ind = ind;
		this.threadnum = threadnum;
		this.stack = stack;
		this.input = input;
		this.stbot = stbot;
	}
	
	
	public StarCraftBot getStbot() {
		return stbot;
	}
	public void setMvr(StarCraftBot stbot) {
		this.stbot = stbot;
	}
	
	
	
	public GameData getInput() {
		return input;
	}
	public void setInput(GameData input) {
		this.input = input;
	}
	
	
	
	public EvolutionState getState() {
		return state;
	}
	public void setState(EvolutionState state) {
		this.state = state;
	}
	
	
	
	public GPIndividual getInd() {
		return ind;
	}
	public void setInd(GPIndividual ind) {
		this.ind = ind;
	}
	
	
	
	public int getThreadnum() {
		return threadnum;
	}
	public void setThreadnum(int threadnum) {
		this.threadnum = threadnum;
	}
	
	
	
	public ADFStack getStack() {
		return stack;
	}
	public void setStack(ADFStack stack) {
		this.stack = stack;
	}
	

	
}

package problem;

import ec.util.*;
import support.ExeContext;
import support.OnEndCallback;
import ec.*;
import bot.TestBot1;
import bwapi.Game;
import data.GameData;
import ec.gp.*;
import ec.gp.koza.*;
import support.GimmeTheGame;

import ec.simple.*;

public class StarCraftBot extends GPProblem implements SimpleProblemForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1814245143656863670L;

	public static final String P_DATA = "data";

	public double currentX;
	public double currentY;
	public Game myDamnGame; //this is important

	public static String[] arguments;

	public void setup(final EvolutionState state, final Parameter base) {
		// very important, remember this
		super.setup(state, base);

		// verify our input is the right class (or subclasses from it)
		if (!(input instanceof GameData))
			state.output.fatal("GPData class must subclass from " + GameData.class, base.push(P_DATA), null);
	}

	@Override
	public void finishEvaluating(EvolutionState state, int threadnum) {
		// TODO Auto-generated method stub
		super.finishEvaluating(state, threadnum);
	}

	public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation,
			final int threadnum) {
		if (!ind.evaluated) // don't bother reevaluating
		{
			GameData input = (GameData) (this.input);
			
			
			ExeContext c = new ExeContext (state, ((GPIndividual) ind), threadnum, stack, input,  this);
			TestBot1 t = new TestBot1(c);
			input.g = t.getGame();
			c.setInput(input);
			t.setCallback(new OnEndCallback() {

				@Override
				public void onEnd(int hits, double sum) {
					// the fitness better be KozaFitness!
					KozaFitness f = ((KozaFitness) ind.fitness);
					f.setStandardizedFitness(state, sum);
					f.hits = hits;
					ind.evaluated = true;
				}
			
			});
			t.setGimmer(new GimmeTheGame() {

				@Override
				public void gimmeIt(Game g) {
					// TODO Auto-generated method stub
					System.out.println("Da game is " + g);
					//input.g = g; this is not necessary
					myDamnGame = g;
				}
				
			});
			t.run();
			
			System.out.println("I'M OUTTA HERE!");
							
			
		}
	}

	public static void main(String[] args) {
		arguments = args;
		run();
	}

	public static void run() {
		// TODO Auto-generated method stub
		Evolve.main(arguments);
	}

}
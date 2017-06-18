package problem;

import ec.util.*;
import support.BotContext;
import support.ExeContext;
import support.Tuple;
import ec.*;
import data.GameData;
import ec.gp.*;
import ec.gp.koza.*;

import ec.simple.*;

public class StarCraftBot extends GPProblem implements SimpleProblemForm {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1;

	public static final String P_DATA = "data";
	// private transient boolean checked = false;

	protected EvolutionState currentEvolutionState;
	protected Individual currentIndividual;
	public transient BotContext bc = null;

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
		//
		// try {
		// workerThread.interrupt();
		// //workerThread.stop();
		// System.err.println("Esperando aqui sentado a que hilo termine");
		// workerThread.join();
		// System.err.println("El hilo ha terminado y todo funciona, o eso
		// creo");
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation,
			final int threadnum) {
		checkNull();
		if (!ind.evaluated) // don't bother reevaluating
		{
			this.currentEvolutionState = state;
			this.currentIndividual = ind;

			ExeContext c = new ExeContext(state, ((GPIndividual) ind), threadnum, stack, (GameData) this.input, this);
			try {
				System.err.println("evaluating");
				bc.getIndividualsQueue().put(c);
				((GameData) input).g = bc.getBot().getGame();
				c.setInput((GameData) input);

				// Wait for game to finish...
				Tuple<Integer, Double> results = bc.getFitnessQueue().take();

				KozaFitness f = ((KozaFitness) this.currentIndividual.fitness);
				f.setStandardizedFitness(this.currentEvolutionState, results.getY());
				f.hits = results.getX();
				this.currentIndividual.evaluated = true;
				// ((GameData) input).g.leaveGame();

				System.err.println(this.currentIndividual.toString());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public StarCraftBot() {

		checkNull();
	}

	public void checkNull() {

		if (bc == null)
			bc = BotContext.getInstance();

	}

	@Override
	public void describe(EvolutionState state, Individual ind, int subpopulation, int threadnum, int log) {
		// TODO Auto-generated method stub
		super.describe(state, ind, subpopulation, threadnum, log);
		System.err.println("I'm finished!!!");
	}

	@Override
	public void prepareToEvaluate(EvolutionState state, int threadnum) {
		// TODO Auto-generated method stub
		super.prepareToEvaluate(state, threadnum);
		checkNull();

	}

}

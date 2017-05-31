package problem;

import ec.util.*;
import support.ExeContext;
import support.Tuple;
import ec.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import bot.Bot;
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
	protected transient BlockingQueue<Tuple<Integer, Double>> fitnessQueue = null;
	protected transient BlockingQueue<ExeContext> individualsQueue = null;
	public static transient String[] arguments;
	protected transient Thread workerThread;
	public transient Bot bot;
	protected EvolutionState currentEvolutionState;
	protected Individual currentIndividual;

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

	
		if (!ind.evaluated) // don't bother reevaluating
		{
			this.currentEvolutionState = state;
			this.currentIndividual = ind;

			ExeContext c = new ExeContext(state, ((GPIndividual) ind), threadnum, stack, (GameData) this.input, this);
			try {
				System.err.println("evaluating");
				this.individualsQueue.put(c);
				((GameData) input).g = bot.getGame();
				c.setInput((GameData) input);

				// Wait for game to finish...
				Tuple<Integer, Double> results = this.fitnessQueue.take();

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

	// public static void main(String[] args) {
	// run(args);

	// }

	public StarCraftBot() {

		// arguments = args;
		fitnessQueue = new SynchronousQueue<>();
		individualsQueue = new SynchronousQueue<>();
		bot = new Bot(fitnessQueue, individualsQueue);
		workerThread = new Thread(bot);
		workerThread.start();
		//checked = true;
	}

	public void checkNull() {

		// arguments = args;
		if (this.fitnessQueue == null)
			fitnessQueue = new SynchronousQueue<>();
		if (this.individualsQueue == null)
			individualsQueue = new SynchronousQueue<>();
		if (this.bot == null) {
			bot = new Bot(fitnessQueue, individualsQueue);
		if(workerThread == null)
			workerThread = new Thread(bot);
		workerThread.start();
		}
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

	// public static void run(String[] arguments) {
	// Evolve.main(arguments);
	// }

}

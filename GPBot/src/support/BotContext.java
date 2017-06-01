package support;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import bot.Bot;

public class BotContext {

	private BlockingQueue<Tuple<Integer, Double>> fitnessQueue = null;
	private BlockingQueue<ExeContext> individualsQueue = null;
	private Thread workerThread;
	private Bot bot;
	private static BotContext bc;

	public static BotContext getInstance() {
		if (bc == null) {
			
			bc = new BotContext();
		}
		return bc;
	}

	private BotContext() {
		fitnessQueue = new SynchronousQueue<>();
		individualsQueue = new SynchronousQueue<>();
		bot = new Bot(fitnessQueue, individualsQueue);
		workerThread = new Thread(bot);
		workerThread.start();
	}

	public BlockingQueue<Tuple<Integer, Double>> getFitnessQueue() {
		return fitnessQueue;
	}

	public void setFitnessQueue(BlockingQueue<Tuple<Integer, Double>> fitnessQueue) {
		this.fitnessQueue = fitnessQueue;
	}

	public BlockingQueue<ExeContext> getIndividualsQueue() {
		return individualsQueue;
	}

	public void setIndividualsQueue(BlockingQueue<ExeContext> individualsQueue) {
		this.individualsQueue = individualsQueue;
	}

	public Thread getWorkerThread() {
		return workerThread;
	}

	public void setWorkerThread(Thread workerThread) {
		this.workerThread = workerThread;
	}

	public Bot getBot() {
		return bot;
	}

	public void setBot(Bot bot) {
		this.bot = bot;
	}

}

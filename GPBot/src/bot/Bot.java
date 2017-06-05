package bot;



import java.util.Random;
import java.util.concurrent.BlockingQueue;

import bwapi.*;
import bwta.BWTA;
import support.ExeContext;
import support.GimmeTheGame;
import support.OnEndCallback;
import support.Tuple;

public class Bot extends DefaultBWListener implements Runnable{
	
	private ExeContext exe;
	protected BlockingQueue<Tuple<Integer,Double>> fitnessQueue = null;
	protected BlockingQueue<ExeContext> individualsQueue = null;
	private Random r;//auxiliar
	private List<Units> workers;
	private List<Units> squads;
	private List<Units> buildings;
	public double currentX;
	public double currentY;
	public boolean go_construct = false;
	private int hits;
	private double sum;
	private Mirror mirror = new Mirror();
	private Game game;
	private Player self;
	int aux = 0;
	
	public Bot(BlockingQueue<Tuple<Integer,Double>> fitnessQueue, BlockingQueue<ExeContext> individualsQueue) {
		super();
		this.fitnessQueue = fitnessQueue;
		this.individualsQueue = individualsQueue;
		hits = 0;
		sum = 0;
		r = new Random();
	}

	
	@Override
	public void run() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();
	}

	

	private GimmeTheGame gimmer;

	public GimmeTheGame getGimmer() {
		return gimmer;
	}

	public void setGimmer(GimmeTheGame gimmer) {
		this.gimmer = gimmer;
	}

	@Override
	public void onUnitCreate(Unit unit) {
		super.onUnitCreate(unit);
		//this is for squads and on complete for buildings???
	}

	@Override
	public void onUnitComplete(Unit arg0) {
		super.onUnitComplete(arg0);
		go_construct = false;
		if (arg0.getType().isBuilding()) {
			this.hits++;
		}
	}

	@Override
	public void onStart() {

		game = mirror.getGame();
		self = game.self();
		//gimmer.gimmeIt(game);// what the hell did i do yesterday...

		try {
			exe = this.individualsQueue.take();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}

		// Use BWTA to analyze map
		// This may take a few minutes if the map is processed first time!
		System.out.println("Analyzing map...");
		BWTA.readMap();
		BWTA.analyze();
		System.out.println("Map data ready");

		// int i = 0;
		// for (BaseLocation baseLocation : BWTA.getBaseLocations()) {
		// System.out.println("Base location #" + (++i) + ". Printing location's
		// region polygon:");
		// for (Position position :
		// baseLocation.getRegion().getPolygon().getPoints()) {
		// System.out.print(position + ", ");
		// }
		// System.out.println();
		// }

		// We're going to locate the build plan call here
		if (exe != null) {
			if(exe.getInput().bp == null) {
				exe.getInput().initializeIfNull();}
			exe.getInput().bp.clear();//clean the stack
			System.out.println("---The Build Plan size is BEFORE: " + exe.getInput().bp.size());
			exe.getInd().trees[0].child.eval(exe.getState(), exe.getThreadnum(), exe.getInput(), exe.getStack(),
				exe.getInd(), exe.getStbot());
			System.out.println("---The Build Plan size is AFTER: " + exe.getInput().bp.size());
		} else {
			System.err.println("===============================================================================\nexe nulo!!!\n===============================================================================");
		}
	}

	@Override
	public void onFrame() {

		if (exe != null) {
		game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
		for (Unit myUnit : self.getUnits()) {
			// if it's a worker and it's idle, send it to the closest mineral
			// patch
			Unit closestMineral = null;
			if (myUnit.getType().isRefinery()) {
				closestMineral = myUnit;
			}
			if (myUnit.getType().isWorker() && myUnit.isIdle()) {

				// find the closest mineral
				for (Unit neutralUnit : game.neutral().getUnits()) {
					if (neutralUnit.getType().isMineralField()) {

						if (closestMineral == null
								|| myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
							closestMineral = neutralUnit;
						}
						// else if (neutralUnit.getType().isRefinery()) {
						// closestMineral = myUnit;
						// }
					}
				}

				// if a mineral patch was found, send the worker to gather it
				if (closestMineral != null) {
					myUnit.gather(closestMineral, false);
					System.out.println("I'm collecting minerals and gas");
				}
			}

		}
		// StringBuilder units = new StringBuilder("My units:\n");

		// build from build plan
		executeBuildPlan(exe.getInput().bp);
	}
	
	public void executeBuildPlan(Stack<Tuple> bp){
		Object o = bp.peek().getX();
		int sup = bp.peek().getY();
		System.out.println("My supply: " + self.allUnitCount());
		System.out.println("Supply: " + sup);
		System.out.println("Building: " + u.toString());
		if (!go_construct) {
			switch (bp.peek().getX().getClass.getName()) {
				// here we do the checkings to build a building from the build
				// plan
				case "UnitType":
					UnitType unit = bp.peek().getX();
					if(unit.isAddon()) || (unit.isBuilding()){
					//buildings
						if ((self.allUnitCount() >= sup) && (self.minerals() >= unit.mineralPrice())
								&& (self.gas() >= unit.gasPrice())) {
							System.out.println("INSIDE");
							if (unit.isAddon()) {
								go_construct = true;
								attachBuilding(bp.pop().getX());
							} else if (unit.isBuilding()) {
								go_construct = true;
								buildBuilding(bp.pop().getX());
							}
						}
					} else {
					//squads
						if ((self.minerals() >= (unit.mineralPrice() * sup)) && (self.gas() >= (unit.gasPrice() * sup))) {
							go_construct = true;
							trainUnit(bp.peek().getX(), bp.pop().getY());
						}
					}
					break;
					
				case "TechType"
					TechType tech = bp.peek().getX();
					if ((self.minerals() >= (tech.mineralPrice())) && (self.gas() >= (tech.gasPrice()))) {
						investigateTech(bp.pop().getX());
					}
					break;
				case "UpgradeType"
					UpgradeType up = bp.peek().getX();
					if ((self.minerals() >= (up.mineralPrice())) && (self.gas() >= (up.gasPrice()))) {
						upgrade(bp.pop().getX());
					}
					break;
				default:
					//do nothing (yet)
			}
		}
	}
		
		
	@Override
	public void onEnd(boolean arg0) {
		super.onEnd(arg0);
		//gameSwitcher();
		try {
			hits= r.nextInt(200);
			sum = r.nextDouble();
			this.fitnessQueue.put(new Tuple<>(hits, sum));
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.err.println("La partida ha terminado!!!!");
		game.drawTextScreen(10, 25, "GG");
		//gameSwitcher();
	}

	public void gameSwitcher(){
		if (game.isPaused()) game.resumeGame();
		else game.pauseGame();
	}
	
	public Game getGame() {
		return game;
	}

	public void buildBuilding(UnitType building) {
		for (Unit myUnit : self.getUnits()) {
			TilePosition tile = getBuildTile(myUnit, building, myUnit.getTilePosition());
			if (tile != null) {
				if ((myUnit.canBuild(building, tile)) & !(myUnit.isConstructing()) & !(myUnit.isGatheringGas())) {
					System.out.println("I'm building: " + building);
					myUnit.build(building, tile);
					break;
				}
			}

		}
	}

	public void attachBuilding(UnitType addon) {
		for (Unit myUnit : self.getUnits()) {
			if (!(myUnit.isConstructing()) & !(myUnit.isGatheringGas())) {
				System.out.println("I'm building: " + addon);
				myUnit.buildAddon(addon);
				break;
			}
		}

	}

	public void trainUnit(UnitType unit, int number) {
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.canTrain(unit)) {
				System.out.println("I'm training: " + unit);
				while (number > 0) {
					myUnit.train();
					number--;
				}
				break;
			}
		}
	}

	public TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile) {
		TilePosition ret = null;
		int maxDist = 3;
		int stopDist = 40;

		// Refinery, Assimilator, Extractor
		if (buildingType.isRefinery()) {
			for (Unit n : game.neutral().getUnits()) {
				if ((n.getType() == UnitType.Resource_Vespene_Geyser)
						&& (Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist)
						&& (Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist)) {
					return n.getTilePosition();
				}
			}
		}

		while ((maxDist < stopDist) && (ret == null)) {
			for (int i = aroundTile.getX() - maxDist; i <= aroundTile.getX() + maxDist; i++) {
				for (int j = aroundTile.getY() - maxDist; j <= aroundTile.getY() + maxDist; j++) {
					if (game.canBuildHere(new TilePosition(i, j), buildingType, builder, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							if (u.getID() == builder.getID())
								continue;
							if ((Math.abs(u.getTilePosition().getX() - i) < 4)
									&& (Math.abs(u.getTilePosition().getY() - j) < 4)) {
								unitsInWay = true;
							}
						}
						if (!unitsInWay) {
							return new TilePosition(i, j);
						}
					}
				}
			}
			maxDist += 2;
		}

		if (ret == null)
			game.printf("Unable to find suitable build position for " + buildingType.toString());
		return ret;
	}
}

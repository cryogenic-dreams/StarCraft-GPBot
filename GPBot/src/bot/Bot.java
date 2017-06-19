package bot;

import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;

import bwapi.*;
import bwta.BWTA;
import support.ExeContext;
import support.GimmeTheGame;
import support.OnEndCallback;
import support.Tuple;

public class Bot extends DefaultBWListener implements Runnable {

	private ExeContext exe;
	protected BlockingQueue<Tuple<Integer, Double>> fitnessQueue = null;
	protected BlockingQueue<ExeContext> individualsQueue = null;
	private Random r;// auxiliar
	private List<Unit> workers;
	private List<Unit> squads;
	private List<Unit> buildings;
	public double currentX;
	public double currentY;
	public boolean go_construct = false;
	private int hits;
	private double sum;
	private Mirror mirror = new Mirror();
	private Game game;
	private Player self;
	int aux = 0;

	public Bot(BlockingQueue<Tuple<Integer, Double>> fitnessQueue, BlockingQueue<ExeContext> individualsQueue) {
		super();
		this.fitnessQueue = fitnessQueue;
		this.individualsQueue = individualsQueue;
		hits = 0;
		sum = 0;
		r = new Random();
		this.workers = new List<Unit>();
		this.squads = new List<Unit>();
		this.buildings = new List<Unit>();
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
		//not used
	}

	@Override
	public void onUnitComplete(Unit arg0) {
		super.onUnitComplete(arg0);
		go_construct = false;
		if (arg0.getType().isBuilding()) {
			this.hits++;
		}
		addList(arg0);
	}

	@Override
	public void onStart() {

		game = mirror.getGame();
		self = game.self();
		
		try {
			exe = this.individualsQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Use BWTA to analyze map
		// This may take a few minutes if the map is processed first time!
		System.out.println("Analyzing map...");
		BWTA.readMap();
		BWTA.analyze();
		System.out.println("Map data ready");


		// We're going to locate the build plan call here
		if (exe != null) {
			if (exe.getInput().bp == null) {
				exe.getInput().initializeIfNull();
			}
			exe.getInput().bp.clear();// clean the stack
			System.out.println("---The Build Plan size is BEFORE: " + exe.getInput().bp.size());
			exe.getInd().trees[0].child.eval(exe.getState(), exe.getThreadnum(), exe.getInput(), exe.getStack(),
					exe.getInd(), exe.getStbot());
			System.out.println("---The Build Plan size is AFTER: " + exe.getInput().bp.size());
		} else {
			System.err.println(
					"===============================================================================\nexe nulo!!!\n===============================================================================");
		}
	}

	@Override
	public void onFrame() {

		if (exe != null) {
			
			game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
			for (Unit myUnit : self.getUnits()) {
				// if it's a worker and it's idle, send it to the closest
				// mineral
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
						
						}
					}

					// if a mineral patch was found, send the worker to gather it
					if (closestMineral != null) {
						myUnit.gather(closestMineral, false);
						System.out.println("I'm collecting minerals and gas");
					}
				}

			}
			
			// build from build plan
			executeBuildPlan();
		}
	}

	public void executeBuildPlan() {
		//The ugly method to execute the buildplan stack
		
		Object o = exe.getInput().bp.peek().getX();
		int sup = (int) exe.getInput().bp.peek().getY();
		
		System.out.println("My supply: " + self.allUnitCount());
		System.out.println("Supply: " + sup);
		System.err.println("Building: " + o.toString());
		
		UnitType unit = (UnitType) exe.getInput().bp.peek().getX();
		if((self.minerals() >= (unit.mineralPrice()+200)) && (self.gas() >= (unit.gasPrice()+100))){
			go_construct = false;
		}
		if (!go_construct) {
	

			if (exe.getInput().bp.peek().getX().getClass() == UnitType.class) {
				
				if (unit.isAddon() || unit.isBuilding()) {
					// buildings
					if ((self.allUnitCount() >= sup) && (self.minerals() >= unit.mineralPrice())
							&& (self.gas() >= unit.gasPrice())) {
						System.out.println("INSIDE");
						if (unit.isAddon()) {
							go_construct = true;
							attachBuilding((UnitType) exe.getInput().bp.pop().getX());
						} else if (unit.isBuilding()) {
							go_construct = true;
							buildBuilding((UnitType) exe.getInput().bp.pop().getX());
						}
					}
				} else {
					// squads
					if ((self.minerals() >= (unit.mineralPrice() * sup)) && (self.gas() >= (unit.gasPrice() * sup))) {
						go_construct = true;
						trainUnit((UnitType) exe.getInput().bp.peek().getX(), (int) exe.getInput().bp.pop().getY());
					}
				}
			}

			else if (exe.getInput().bp.peek().getX().getClass() == TechType.class) {
				//research tech
				TechType tech = (TechType) exe.getInput().bp.peek().getX();
				if ((self.minerals() >= (tech.mineralPrice())) && (self.gas() >= (tech.gasPrice()))) {
					investigateTech((TechType) exe.getInput().bp.pop().getX());
				}
			} else if (exe.getInput().bp.peek().getX().getClass() == UpgradeType.class) {
				//upgrades
				UpgradeType up = (UpgradeType) exe.getInput().bp.peek().getX();
				if ((self.minerals() >= (up.mineralPrice())) && (self.gas() >= (up.gasPrice()))) {
					upgrade((UpgradeType) exe.getInput().bp.pop().getX());
				}
			} else {
				// do nothing (yet)
				System.out.println("NOPE, NOTHING------------------------");
			}
		}
	}

	@Override
	public void onEnd(boolean arg0) {
		super.onEnd(arg0);

		game.drawTextScreen(10, 25, "GG");
		try {
			hits = r.nextInt(200);
			sum = r.nextDouble();
			this.fitnessQueue.put(new Tuple<>(hits, sum));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void gameSwitcher() {
		//not yet used
		if (game.isPaused())
			game.resumeGame();
		else
			game.pauseGame();
	}

	public Game getGame() {
		return game;
	}
	
	public void addList(Unit newUnit) {
		if((newUnit.getType().isBuilding()) || (newUnit.getType().isBuilding())){
			buildings.add(newUnit);
		}
		else if(newUnit.getType().isWorker()){
			workers.add(newUnit);
		}
		else if((newUnit.getType().isOrganic()) || (newUnit.getType().isMechanical())){
			squads.add(newUnit);
		}
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

	public void upgrade(UpgradeType up) {
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.canUpgrade(up)) {
				System.out.println("I'm upgrading: " + up);
				myUnit.upgrade(up);
				break;
			}
		}

	}

	public void investigateTech(TechType tech) {
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.canResearch(tech)) {
				System.out.println("I'm researching: " + tech);
				myUnit.research(tech);
				break;
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
					myUnit.train(unit);
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

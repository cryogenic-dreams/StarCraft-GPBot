package bot;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.BWTA;
import bwta.BaseLocation;
import data.GameData;
import ec.gp.GPTree;
import support.ExeContext;
import support.GimmeTheGame;
import support.PointSystem;
import support.Tuple;
import support.UnitTuple;

public class Bot extends DefaultBWListener implements Runnable {

	private ExeContext exe;
	protected BlockingQueue<Tuple<Integer, Double>> fitnessQueue = null;
	protected BlockingQueue<ExeContext> individualsQueue = null;
	private List<Unit> workers = new ArrayList<Unit>();
	private List<Unit> squads = new ArrayList<Unit>();
	private List<Unit> buildings = new ArrayList<Unit>();
	private List<Unit> enemies = new ArrayList<Unit>();
	public double currentX;
	public double currentY;
	public boolean go_construct = false;
	private int hits;
	private double sum;
	private Mirror mirror = new Mirror();
	private Game game;
	private Player self;
	private int counter;// adds a little delay in the tree evaluation
	private int counter2;
	private int counter3;
	private int counter4;
	private int counter5;

	private boolean ref_exists = false;
	private List<BaseLocation> bases;

	private PointSystem ps;

	public Bot(BlockingQueue<Tuple<Integer, Double>> fitnessQueue, BlockingQueue<ExeContext> individualsQueue) {
		super();
		this.fitnessQueue = fitnessQueue;
		this.individualsQueue = individualsQueue;
		hits = 0;
		sum = 0;
		counter = 0;
		counter2 = 0;
		counter3 = 0;
		counter4 = 0;
		ps = new PointSystem();
		ref_exists = false;
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
		// not used
		go_construct = false;
	}

	@Override
	public void onUnitDestroy(Unit arg0) {
		// eliminate the unit from its list
		super.onUnitDestroy(arg0);

	}

	@Override
	public void onUnitDiscover(Unit arg0) {
		super.onUnitDiscover(arg0);
		if (arg0.getPlayer().isEnemy(game.self())) {
			// found enemy, entering aggressive mode
			game.printf("Found ya!");
			exe.getInput().state = 1;
			enemies.add(arg0);
			attack();
		}
	}

	@Override
	public void onUnitComplete(Unit arg0) {
		super.onUnitComplete(arg0);
		go_construct = false;

		if (arg0.getType() == UnitType.Terran_Refinery)
			ref_exists = true;
		if (arg0.getPlayer() == self) {
			// game.setScreenPosition(arg0.getTilePosition().toPosition());
			addList(arg0);
		}
	}

	@Override
	public void onStart() {
		game = mirror.getGame();
		self = game.self();
		hits = 0;
		sum = 0;
		ps.reset();
		go_construct = false;
		counter = 0;
		counter2 = 0;
		counter3 = 0;
		counter4 = 0;
		counter5 = 0;

		ref_exists = false;

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
			//drawTree(exe.getInd().trees[2], 2);
			drawTree(exe.getInd().trees[0], 0);

			this.workers = new ArrayList<Unit>();
			this.squads = new ArrayList<Unit>();
			this.buildings = new ArrayList<Unit>();
			this.enemies = new ArrayList<Unit>();
			this.bases = BWTA.getBaseLocations();
			exe.getInput().workers = this.workers;
			exe.getInput().squads = this.squads;
			exe.getInput().buildings = this.buildings;
			exe.getInput().enemies = this.enemies;
		} else {
			System.err.println("nexe nulo!!!");
		}
	}

	@Override
	public void onFrame() {
		// this used to be enormous
		try {
			if (exe != null) {

				attackExplore();
				workYourAss();
				switchConstruct();

				executeBuildPlan();// build from build plan mainly (macro)
//				executeMainLoopActions();// miscelaneous actions (micro)
//											// careful with this one as it might
//											// add units on each loop, delaying
//											// it can be a solution
//				executeSquadActions();// squad actions (micro)

			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public void attackExplore() {
		counter5++;
		if (squads.size() > 2) {// do not go alone
		if (counter5 > 200) {
			if (!enemies.isEmpty()) {
				attack();
			} else {
				explore();
			}
			counter5 = 0;
		}
		}
	}

	public void switchConstruct() {
		if (counter4 > 400) {
			go_construct = false;// in case something when wrong
			counter4 = 0;
		}
		counter4++;
	}

	public void executeBuildPlan() {
		if (counter3 > 100) {
			if (self.supplyTotal() - self.supplyUsed() < 6)
				supplyDepot();
			buildPlan();
			counter3 = 0;
		}
		counter3++;
	}

	@SuppressWarnings("unchecked")
	public void buildPlan() {
		// The ugly method to execute the buildplan stack
		// gotta refactor it, it's ugly
		try {

			int sup = (int) exe.getInput().bp.peek().getY();
			if (exe.getInput().bp.peek().getX().getClass() == UnitType.class) {
				UnitType unit = (UnitType) exe.getInput().bp.peek().getX();
				// if ((self.minerals() >= (unit.mineralPrice() + 200)) &&
				// (self.gas() >= (unit.gasPrice() + 100))) {
				// go_construct = false;
				// }
				if (!go_construct) {
					if (unit.isAddon() || unit.isBuilding()) {
						// buildings
						if ((self.supplyUsed() >= sup) && (self.minerals() >= unit.mineralPrice())
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

						int remain = trainUnit((UnitType) exe.getInput().bp.peek().getX(),
								(int) exe.getInput().bp.peek().getY());
						exe.getInput().bp.peek().setY(remain);
						if ((int) exe.getInput().bp.peek().getY() <= 0)
							exe.getInput().bp.pop();
					}
					counter4 = 0;
				}
			}

			else if (exe.getInput().bp.peek().getX().getClass() == TechType.class) {
				// research tech
				TechType tech = (TechType) exe.getInput().bp.peek().getX();
				// if ((self.minerals() >= (tech.mineralPrice() + 200)) &&
				// (self.gas() >= (tech.gasPrice() + 100))) {
				// go_construct = false;
				// }
				if (!go_construct) {
					if ((self.minerals() >= (tech.mineralPrice())) && (self.gas() >= (tech.gasPrice()))) {
						investigateTech((TechType) exe.getInput().bp.pop().getX());
					}
				}
			} else if (exe.getInput().bp.peek().getX().getClass() == UpgradeType.class) {
				// upgrades
				UpgradeType up = (UpgradeType) exe.getInput().bp.peek().getX();
				// if ((self.minerals() >= (up.mineralPrice() + 200)) &&
				// (self.gas() >= (up.gasPrice() + 100))) {
				// go_construct = false;
				// }
				if (!go_construct) {
					if ((self.minerals() >= (up.mineralPrice())) && (self.gas() >= (up.gasPrice()))) {
						upgrade((UpgradeType) exe.getInput().bp.pop().getX());
					}
				}
			} else {
				// do nothing (yet)
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onEnd(boolean arg0) {
		super.onEnd(arg0);
		game.printf("GG");

		ps.inc_points(0, self.killedUnitCount());
		if (self.isVictorious())
			ps.inc_points(2, 0);
		try {
			// hits is the times it got done what we want
			// sum will be the amount of points it got for doing certain actions
			// like winning, killing or building

			// double diff = (sum / ps.PERFECT_POINTS);
			int result = Math.max(ps.PERFECT_POINTS - ps.getMy_points(), 0);
			sum = result;
			if (ps.win)
				hits++;
			ps.writeResults();
			this.fitnessQueue.put(new Tuple<>(hits, sum));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void gameSwitcher() {
		// not yet used, but useful
		if (game.isPaused())
			game.resumeGame();
		else
			game.pauseGame();
	}

	public Game getGame() {
		return game;
	}

	public void addList(Unit newUnit) {
		if ((newUnit.getType().isBuilding()) || (newUnit.getType().isAddon())) {
			buildings.add(newUnit);
		} else if (newUnit.getType().isWorker()) {
			workers.add(newUnit);
		} else if ((newUnit.getType().isOrganic()) || (newUnit.getType().isMechanical())) {
			squads.add(newUnit);
		}
	}

	public void buildBuilding(UnitType building) {
		int i = 0;
		TilePosition tile = null;
		while ((i < workers.size()) && (workers.get(i).isGatheringGas() || workers.get(i).isConstructing()))
			i++;// do not choose the one that is gathering gas

		int tries = 0;
		while ((tile == null) && (tries < 10)) {
			tile = getBuildTile(workers.get(i), building, workers.get(i).getTilePosition());
			tries++;
		}
		if (tile != null) {
			if ((workers.get(i).canBuild(building, tile)) & (!workers.get(i).isConstructing())) {
				workers.get(i).build(building, tile);
				planToString();
				ps.inc_points(1, 0);
			}
		}

	}

	public void upgrade(UpgradeType up) {
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.canUpgrade(up)) {
				myUnit.upgrade(up);
				planToString();
				ps.inc_points(1, 0);
				break;
			}
		}

	}

	public void investigateTech(TechType tech) {
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.canResearch(tech)) {
				myUnit.research(tech);
				planToString();
				ps.inc_points(3, 0);
				break;
			}
		}

	}

	public void attachBuilding(UnitType addon) {
		for (Unit myUnit : workers) {
			if (!(myUnit.isConstructing()) & !(myUnit.isGatheringGas())) {
				myUnit.buildAddon(addon);
				planToString();
				break;
			}
		}

	}

	public int trainUnit(UnitType unit, int number) {

		for (Unit myUnit : buildings) {
			if (myUnit.canTrain(unit)) {
				while (myUnit.getTrainingQueue().size() < 5 && number > 0) {
					if (((self.minerals() >= unit.mineralPrice()) && (self.gas() >= unit.gasPrice()))) {
						int aaaa = self.supplyTotal() - self.supplyUsed();
						System.out.println("supplytotal - supplyused: " + aaaa);
						if (self.supplyTotal() - self.supplyUsed() < 6) {
							supplyDepot();
							return number;
						}

						myUnit.train(unit);
						number--;
						ps.inc_points(4, 1);
					} else
						return number;
				}
			}
		}

		planToString();
		return number;
	}

	public TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile) {
		TilePosition ret = null;
		int maxDist = 2;
		int stopDist = 40;

		if (buildingType.isRefinery() && (!ref_exists)) {
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

	public void executeMainLoopActions() {
		if (counter > 100) {// executes this tree each 100 frames
			exe.getInd().trees[1].child.eval(exe.getState(), exe.getThreadnum(), exe.getInput(), exe.getStack(),
					exe.getInd(), exe.getStbot());
			counter = 0;
		}
		counter++;
	}

	public void executeSquadActions() {
		if (counter2 > 50) {
			exe.getInd().trees[2].child.eval(exe.getState(), exe.getThreadnum(), exe.getInput(), exe.getStack(),
					exe.getInd(), exe.getStbot());
			counter2 = 0;
		}
		counter2++;
	}

	public void workYourAss() {
		// not going to be used in the future, probably
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

					}
				}
				// if a mineral patch was found, send the worker to gather it
				if (closestMineral != null) {
					myUnit.gather(closestMineral, false);
				}
			}
		}
	}

	public void drawTree(GPTree tree, int num) {
		try {
			String fileName = "E:\\StarCraft-GPBot\\GPBot\\file" + num + ".dot";
			File f = new File(fileName);
			PrintStream pw = new PrintStream(f);
			pw.println(tree.child.makeGraphvizTree());

			pw.close();
			Runtime rt = Runtime.getRuntime();
			// open cmd and run graphviz

			rt.exec("dot -Tpng " + fileName + " -o pic" + num + ".png");

			// then open the image with an image viewer

			TimeUnit.SECONDS.sleep(1);// 1 second to go from .dot to png

			Runtime.getRuntime().exec("C:\\Program Files\\Mozilla Firefox\\firefox.exe pic" + num + ".png");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void planToString() {
		Object o = exe.getInput().bp.peek().getX();
		int sup = (int) exe.getInput().bp.peek().getY();

		System.out.println("My supply: " + self.allUnitCount());
		System.out.println("Units: " + sup);
		System.err.println("B/S/T/U: " + o.toString());
	}

	public void attack() {
		if (!squads.isEmpty()) {
			for (Unit unit : squads) {
				if ((!unit.isAttacking()) && (unit.canAttack())) {
					Unit enemy1 = enemies.get(0);
					int distance = unit.getDistance(enemy1);
					for (Unit enemy2 : enemies) {
						// get nearest enemy
						if (unit.getDistance(enemy2) < distance) {
							distance = unit.getDistance(enemy2);
							enemy1 = enemy2;
						}
					}
					unit.attack(enemy1);
					//let's debug like pros
					System.out.println("----ATTACKING: "+ enemy1);
				}
				
			}
		}
	}

	public void explore() {

		if (!squads.isEmpty()) {
			for (BaseLocation b : bases) {// for each base send one squad to
											// explore
				for (Unit unit : squads) {// search for one that can move
					if ((!unit.isAttacking()) && (unit.canMove()) && (!unit.isMoving())) {
						unit.move(b.getPosition());
						Unit first = squads.remove(0);
						squads.add(first);// now last
						break;
					}

				}
			}
		}

	}

	public void supplyDepot() {
		// exe.getInput().bp.push(new
		// UnitTuple(UnitType.Terran_Supply_Depot,0));
		buildBuilding(UnitType.Terran_Supply_Depot);
		go_construct = true;
	}

}

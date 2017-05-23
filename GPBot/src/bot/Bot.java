package bot;



import bwapi.*;
import bwta.BWTA;
//import bwta.BaseLocation;
//import data.GameData;
import support.ExeContext;
import support.GimmeTheGame;
import support.OnEndCallback;

public class Bot extends DefaultBWListener implements Runnable{
	private OnEndCallback callback;

	public OnEndCallback getCallback() {
		return callback;
	}

	public void setCallback(OnEndCallback callback) {
		this.callback = callback;
	}

	private ExeContext exe;
	protected BlockingQueue queue = null;


	
	public Bot(ExeContext exe, BlockingQueue queue) {
		super();
		this.exe = exe;
		this.queue = queue;
		hits = 0;
		sum = 0;
	}

	public double currentX;
	public double currentY;
	public boolean go_construct = false;
	private int hits;
	private double sum;

	private Mirror mirror = new Mirror();

	private Game game;

	private Player self;

	@Override
	public void run() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();

	}

	int aux = 0;

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
		gimmer.gimmeIt(game);// what the hell did i do yesterday...

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
		System.out.println("---The Build Plan size is BEFORE: " + exe.getInput().bp.size());
		// game.pauseGame();
		exe.getInd().trees[0].child.eval(exe.getState(), exe.getThreadnum(), exe.getInput(), exe.getStack(),
				exe.getInd(), exe.getStbot());
		// game.resumeGame();
		System.out.println("---The Build Plan size is AFTER: " + exe.getInput().bp.size());
		// this.sum = exe.getInput().bp.size();
	}

	@Override
	public void onFrame() {

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
/**
		// build from build plan
		Object u = exe.getInput().bp.peek().getX();
		int sup = exe.getInput().bp.peek().getY();
		System.out.println("My supply: " + self.allUnitCount());
		System.out.println("Supply: " + sup);
		System.out.println("Building: " + u.toString());
		
		//Change this tomorrow
		if (!go_construct) {
			if (u.isAddon() || u.isBuilding()) {
				// here we do the checkings to build a building from the build
				// plan
				if ((self.allUnitCount() >= sup) && (self.minerals() >= u.mineralPrice())
						&& (self.gas() >= u.gasPrice())) {
					System.out.println("INSIDE");
					if (u.isAddon()) {
						go_construct = true;
						attachBuilding(exe.getInput().bp.pop().getX());
					} else if (u.isBuilding()) {
						go_construct = true;
						buildBuilding(exe.getInput().bp.pop().getX());
					}
					// go_construct = false;

				}
			} else {
				if ((self.minerals() >= (u.mineralPrice() * sup)) && (self.gas() >= (u.gasPrice() * sup))) {
					go_construct = true;
					trainUnit(exe.getInput().bp.peek().getX(), exe.getInput().bp.pop().getY());
				}
			}

		}**/
	}

	@Override
	public void onEnd(boolean arg0) {
		// TODO Auto-generated method stub
		super.onEnd(arg0);
		gameSwitcher();
		queue.put(hits, sum);
		//callback.onEnd(hits, sum);
		game.drawTextScreen(10, 25, "GG");
		//game.resumeGame();

	}

	public gameSwitcher(){
		if(game.isPaused()){
			game.resumeGame();
		}
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

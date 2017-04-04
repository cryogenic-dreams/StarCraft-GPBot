package bot;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import data.GameData;
import support.ExeContext;
import support.GimmeTheGame;
import support.OnEndCallback;

//import java.util.concurrent.TimeUnit;

public class TestBot1 extends DefaultBWListener {
	private OnEndCallback callback;

	public OnEndCallback getCallback() {
		return callback;
	}

	public void setCallback(OnEndCallback callback) {
		this.callback = callback;
	}

	private ExeContext exe;

	public TestBot1(ExeContext exe) {
		super();
		this.exe = exe;
		hits = 0;
		sum = 0;
	}

	public double currentX;
	public double currentY;

	private int hits;
	private double sum;

	private Mirror mirror = new Mirror();

	private Game game;

	private Player self;

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
		System.out.println("New unit discovered " + unit.getType());
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

		exe.getInd().trees[1].child.eval(exe.getState(), exe.getThreadnum(), exe.getInput(), exe.getStack(),
				exe.getInd(), exe.getStbot());
		System.out.println("---The Build Plan size is AFTER: " + exe.getInput().bp.size());
	}

	@Override
	public void onFrame() {

		game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());// always
																								// as
																								// Terran,
																								// at
																								// least
																								// for
																								// the
																								// moment

		// Let's check if a build plan is generated...
		// bp stack shouldn't be empty
		// oh, it was a list, I thought I made a stack, anyways

		for (Unit myUnit : self.getUnits()) {
			// if it's a worker and it's idle, send it to the closest mineral
			// patch
			if (myUnit.getType().isWorker() && myUnit.isIdle()) {
				Unit closestMineral = null;

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
					System.out.println("I'm collecting minerals");

				}
			}
		}
		// StringBuilder units = new StringBuilder("My units:\n");

		// build from build plan
		UnitType u = exe.getInput().bp.peek().getX();
		int sup = exe.getInput().bp.peek().getY() + aux;
		System.out.println("My supply: " + self.allUnitCount());
		System.out.println("Supply: " + sup);
		System.out.println("Building: " + u);
		if ((self.allUnitCount() >= sup) & (self.minerals() >= u.mineralPrice()) & (self.gas() >= u.gasPrice())) {
			System.out.println("INSIDE");
			buildBuilding(exe.getInput().bp.pop().getX());
		}

		// exe.getInd().trees[0].child.eval(exe.getState(), exe.getThreadnum(),
		// exe.getInput(), exe.getStack(),
		// exe.getInd(), exe.getStbot());

		// draw my units on screen
		// game.drawTextScreen(10, 25, units.toString());
	}

	@Override
	public void onEnd(boolean arg0) {
		// TODO Auto-generated method stub
		super.onEnd(arg0);
		game.pauseGame();
		callback.onEnd(hits, sum);
		game.drawTextScreen(10, 25, "GG");
		game.resumeGame();

	}

	public Game getGame() {
		// TODO Auto-generated method stub
		return game;
	}

	public void buildBuilding(UnitType building) {
		for (Unit myUnit : self.getUnits()) {
			TilePosition tile = getBuildTile(myUnit, building, myUnit.getTilePosition());
			if (tile != null) {
				if ((myUnit.canBuild(building, tile)) & !(myUnit.isConstructing())) {
					System.out.println("I'm building: " + building);
					myUnit.build(building, tile);
					aux++;
					break;
				}
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
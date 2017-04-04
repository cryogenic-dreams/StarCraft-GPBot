package support;

import java.util.Random;

import bwapi.UnitType;

public class RandomBuilding {
	private Random rand = new Random();
	private UnitType randomB;
	private UnitType[] terranBuildings = new UnitType[] { UnitType.Terran_Command_Center, UnitType.Terran_Supply_Depot,
			UnitType.Terran_Refinery, UnitType.Terran_Barracks, UnitType.Terran_Bunker, UnitType.Terran_Engineering_Bay,
			UnitType.Terran_Missile_Turret, UnitType.Terran_Academy, UnitType.Terran_Factory,
			UnitType.Terran_Machine_Shop, UnitType.Terran_Armory, UnitType.Terran_Starport,
			UnitType.Terran_Control_Tower, UnitType.Terran_Science_Facility, UnitType.Terran_Physics_Lab,
			UnitType.Terran_Covert_Ops, UnitType.Terran_Comsat_Station, UnitType.Terran_Nuclear_Silo };

	public RandomBuilding() {
		this.randomB = terranBuildings[rand.nextInt() % terranBuildings.length];
	}

	public UnitType returnRandomBuilding() {
		return randomB;
	}

}

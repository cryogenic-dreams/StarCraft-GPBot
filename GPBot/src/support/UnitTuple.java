package support;

import bwapi.UnitType;

public class UnitTuple extends Tuple {
	
	//Damn Java, you don't have tuples
	private UnitType x;

	
	
	public UnitTuple(UnitType x, int y) {
		super();
		this.x = x;

	}
	public UnitType getX() {
		return x;
	}
	public void setX(UnitType x) {
		this.x = x;
	}

}
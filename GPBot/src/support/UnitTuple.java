package support;

import bwapi.UnitType;

public class UnitTuple extends Tuple<UnitType,Integer> {
	
	//Damn Java, you don't have tuples
	private UnitType x;

	
	
	public UnitTuple(UnitType x, int y) {
		super(x,y);
	}


	public UnitType getX() {
		return x;
	}
	public void setX(UnitType x) {
		this.x = x;
	}
	
	
}
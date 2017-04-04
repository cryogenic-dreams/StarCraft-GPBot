package support;

import bwapi.UnitType;

public class Tuple {
	
	//Damn Java, you don't have tuples
	private UnitType x;
	private int y;
	
	
	public Tuple(UnitType x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public UnitType getX() {
		return x;
	}
	public void setX(UnitType x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}

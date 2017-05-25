package support;

import bwapi.TechType;

public class TechTuple extends Tuple<TechType,Integer>{
	private static final long serialVersionUID = 1;

	private TechType x;
	
	public TechTuple(TechType x, int y) {
		super();
		this.x = x;

	}
	public TechType getX() {
		return x;
	}
	public void setX(TechType x) {
		this.x = x;
	}

}

package support;


import bwapi.UpgradeType;

public class UpgradeTuple extends Tuple<UpgradeType,Integer>{
	
	private UpgradeType x;
	private static final long serialVersionUID = 1;

	public UpgradeTuple(UpgradeType x, int y) {
		super();
		this.x = x;

	}
	public UpgradeType getX() {
		return x;
	}
	public void setX(UpgradeType x) {
		this.x = x;
	}

}

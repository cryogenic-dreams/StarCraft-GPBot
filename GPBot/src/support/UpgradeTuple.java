package support;

import bwapi.UpgradeType;

public class UpgradeTuple extends Tuple<UpgradeType,Integer> {
	
	private UpgradeType x;

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

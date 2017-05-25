package support;

import java.io.Serializable;

public class Tuple<U,I>  implements Serializable {
	
	//Damn Java, you don't have tuples
	private U x;
	private I y;
	private static final long serialVersionUID = 1;

	
	public Tuple(U x, I y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Tuple() {
		// TODO Auto-generated constructor stub
	}
	public U getX() {
		return x;
	}
	public void setX(U x) {
		this.x = x;
	}
	public I getY() {
		return y;
	}
	public void setY(I y) {
		this.y = y;
	}
}

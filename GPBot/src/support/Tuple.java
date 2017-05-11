package support;

public class Tuple<U,I> {
	
	//Damn Java, you don't have tuples
	private U x;
	private I y;
	
	
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

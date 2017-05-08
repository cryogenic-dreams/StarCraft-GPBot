package support;

public class Tuple {
	
	//Damn Java, you don't have tuples
	private Object x;
	private int y;
	
	
	public Tuple(Object x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Tuple() {
		// TODO Auto-generated constructor stub
	}
	public Object getX() {
		return x;
	}
	public void setX(Object x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}

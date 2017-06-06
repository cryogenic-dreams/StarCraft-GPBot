package support;

public class Tuple<U,I>  {
	
	//Damn Java, you don't have tuples
	private U x;
	private I y;

	
	public Tuple(U x, I y) {
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(getX());
		sb.append(", ");
		sb.append(getY());
		sb.append(")");
		
		return sb.toString();
	}
}

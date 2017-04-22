package data;


import java.util.Stack;
import bwapi.Game;
import ec.gp.GPData;
import support.Tuple;

public class GameData extends GPData {

	/**
	 * g is the StarCraft Game class, verrrry important
	 * bp is Build Plan, also important
	 * s is for supply, which I don't know if I need it to be a simple integer or many, we'll see
	 * q is for quantity, it'll be our squad quantity
	 */
	private static final long serialVersionUID = -841133977253680026L;
	public Game g; // return value
	public Stack<Tuple> bp = new Stack<>(); //here we'll save our building plan, it'll have a build and a supply number, just like our usual build plan
	public int s = 200;
	public int q;
	
	public void copyTo(final GPData gpd) // copy my stuff to another GameData
	{
		((GameData) gpd).g = g;
		((GameData) gpd).bp = bp;
		((GameData) gpd).s = s;
		((GameData) gpd).q = q;
	}
}
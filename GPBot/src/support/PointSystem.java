package support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PointSystem {
	private int KILL_POINTS;// 0
	private int BUILDING_POINTS;// 1
	private int WIN_POINTS;// 2
	private int RESEARCH_POINTS;// 3
	private int TRAIN_POINTS;// 4
	public int PERFECT_POINTS;
	private int my_points;
	public boolean win;
	private String FILENAME = "fitnessparams.txt";
	private String FILENAME2 = "results.txt";
	
	public PointSystem() {
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			br = new BufferedReader(new FileReader(FILENAME));
			br.readLine();
			PERFECT_POINTS = Integer.parseInt(br.readLine());
			br.readLine();
			WIN_POINTS = Integer.parseInt(br.readLine());
			br.readLine();
			BUILDING_POINTS = Integer.parseInt(br.readLine());
			br.readLine();
			RESEARCH_POINTS = Integer.parseInt(br.readLine());
			br.readLine();
			TRAIN_POINTS = Integer.parseInt(br.readLine());
			br.readLine();
			KILL_POINTS = Integer.parseInt(br.readLine());
			this.my_points = 0;
			this.win = false;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

	public void reset(){
		this.my_points = 0;
		this.win = false;
	}
	
	public int getMy_points() {
		return my_points;
	}

	public void setMy_points(int my_points) {
		this.my_points = my_points;
	}

	public void inc_points(int choice, int num) {
		switch (choice) {
		// private int KILL_POINTS; --- 0
		// private int BUILDING_POINTS; --- 1
		// private int WIN_POINTS; --- 2
		// private int RESEARCH_POINTS; --- 3
		// private int TRAIN_POINTS; --- 4

		case 0:
			my_points += KILL_POINTS*num;
			break;
		case 1:
			my_points += BUILDING_POINTS;
			break;
		case 2:
			my_points += WIN_POINTS;
			win = true;
			break;
		case 3:
			my_points += RESEARCH_POINTS;
			break;
		case 4:
			my_points += TRAIN_POINTS*num;
			break;
		}
	}

	public void writeResults() {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			fw = new FileWriter(FILENAME2,true);
			bw = new BufferedWriter(fw);
			bw.write(this.toString()+"\r\t");
			System.out.println(this.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}

	@Override
	public String toString() {
		return "PointSystem [my_points=" + my_points + ", WINNER=" + win + "]";
	}

}

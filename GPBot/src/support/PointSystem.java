package support;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PointSystem {
	public int KILL_POINTS;
	public int BUILDING_POINTS;
	public int WIN_POINTS;
	public int RESEARCH_POINTS;
	public int TRAIN_POINTS;
	public int PERFECT_POINTS;
	String FILENAME = "fitnessparams.txt";

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
			RESEARCH_POINTS =Integer.parseInt(br.readLine());
			br.readLine();
			TRAIN_POINTS = Integer.parseInt(br.readLine());
			br.readLine();
			KILL_POINTS = Integer.parseInt(br.readLine());
			
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
}

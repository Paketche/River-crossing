package tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Level{
	
	public final int levelNo;
	public final int[][] tiles = new int[13][9];

	
	/**
	 *  Reads a file and puts the information in an array
	 *  
	 * @param l a number that specifies the level
	 */
	public Level(int l){
		levelNo = l;
		// get according file
		StringBuilder sb = new StringBuilder("Level.csv");
		sb.insert(5, Integer.toString(l));
		
		// read the file
		try{
			FileReader fr = new FileReader("Levels\\" + sb.toString());
			BufferedReader bufferR = new BufferedReader(fr);
			String buffer;
			
			// store what is read in the tiles array
			for (int i = 0; (buffer = bufferR.readLine()) != null; i++){
				// break down the string to tokens
				StringTokenizer st = new StringTokenizer(buffer, ",");
				for (int j = 0; st.hasMoreTokens(); j++){
					tiles[i][j] = Integer.parseInt(st.nextToken());
				}
			}

			fr.close();
		} catch (IOException e){
			System.out.println("cant find file");
		}
	}
}

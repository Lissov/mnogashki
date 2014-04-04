package com.pl.mnogashki;
import android.graphics.*;

public class Game
{
	public int vsize;
	public int hsize;
	
	public int[][] field;
	
	public Point getFreePos(){
		for (int i = 0; i < vsize; i++){
			for (int j = 0; j < hsize; j++){
				if (field[i][j] < 0)
					return new Point(j, i);
			}
		}
		
		return null;
	}
}

package com.pl.mnogashki;
import android.graphics.*;

public class Game
{
	public int vsize;
	public int hsize;
	
	public int[][] field;
	
	public int steps;
	
	public Point getFreePos(){
		for (int i = 0; i < vsize; i++){
			for (int j = 0; j < hsize; j++){
				if (field[i][j] < 0)
					return new Point(j, i);
			}
		}
		
		return null;
	}
	
	public boolean isFinished(){
		for (int i = 0; i < vsize; i++){
			for (int j = 0; j < hsize; j++){
				if (i == vsize - 1 && j == hsize - 1)
					return true;
				
				if (field[i][j] != i * hsize + j + 1)
					return false;
			}
		}
		
		return true; // redundand, just to make compiler happy
	}
}

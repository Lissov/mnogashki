package com.pl.mnogashki;
import java.util.*;
import android.graphics.*;
import android.content.*;
import android.preference.*;
import android.app.*;

public class GameHolder
{
	private static Game _game;
	
	public static Game getGame(Activity activity, boolean forceNew){
		if (_game == null || forceNew)
			constructGame(activity);
			
		return _game;
	}
	
	private static void constructGame(Activity activity){
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
		
		_game = new Game();

		_game.vsize = Integer.parseInt(sharedPrefs.getString(PrefsActivity.KEY_VERT, "4"));
		_game.hsize = Integer.parseInt(sharedPrefs.getString(PrefsActivity.KEY_HORZ, "4"));;
		
		_game.field = new int[_game.vsize][_game.hsize];
		for (int r = 0; r <_game.vsize; r++){
			for (int c = 0; c<_game.hsize; c++){
				if (r == _game.vsize-1 && c == _game.hsize-1)
					_game.field[r][c] = -1;
				else
					_game.field[r][c] = r * (_game.hsize) + c + 1;
			}
		}
		
		scramble(_game, 1000);
	}
	
	private static void scramble(Game game, int steps){
		Random rand = new Random();
		
		int i = 0;
		while (i<steps){
			Point pos = game.getFreePos();
			
			boolean hor = rand.nextBoolean();
			if (hor){
				int newFree = rand.nextInt(game.hsize);
				if (newFree == pos.x) continue;
				int dx = (newFree < pos.x) ? -1 : 1;
				for (int c = pos.x; c != newFree; c += dx)
					game.field[pos.y][c] = game.field[pos.y][c + dx];
				game.field[pos.y][newFree] = -1;
			} else{
				int newFree = rand.nextInt(game.vsize);
				if (newFree == pos.y) continue;
				int dy = (newFree < pos.y) ? -1 : 1;
				for (int r = pos.y; r != newFree; r += dy)
					game.field[r][pos.x] = game.field[r + dy][pos.x];
				game.field[newFree][pos.x] = -1;
			}
			
			i++;
		}
		
		game.steps = 0;
	}
}

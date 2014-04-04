package com.pl.mnogashki;
import android.view.*;
import android.app.*;
import android.graphics.*;
import android.widget.*;
import android.content.*;

public class GameView extends View
{
	private Context _context;
	
	private Paint pntBackground = new Paint();
	private Paint pntBorder = new Paint();
	private Paint pntField = new Paint();
	private Paint pntBlockF = new Paint();
	private Paint pntBlockB = new Paint();
	private Paint pntBlockN = new Paint();
	
	private Game game;
	
	public GameView(Activity context){
		super(context);
		_context = context;
		game = GameHolder.getGame();
		
		pntBackground.setColor(Color.rgb(150, 80, 80));
		pntBorder.setColor(Color.rgb(100, 50, 50));
		pntField.setColor(Color.rgb(130, 60, 60));
		
		pntBlockF.setColor(Color.LTGRAY);
		pntBlockF.setStyle(Paint.Style.FILL);
		pntBlockB.setColor(Color.GRAY);
		pntBlockB.setStyle(Paint.Style.STROKE);
		pntBlockN.setColor(Color.GRAY);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		float w = canvas.getWidth();
		float h = canvas.getHeight();
		
		canvas.drawRect(0,0,w,h, pntBackground);

		float ratio = (float)game.hsize / (float)game.vsize;
		float avw = w * (1 - 2 * Constants.Sizes.BorderPercentage);
		float avh = h * (1 - 2 * Constants.Sizes.BorderPercentage);
		float szw = avw > avh * ratio ? avh * ratio : avw;
		float szh = szw / ratio;
		
		float left = (w - szw) / 2;
		float top = (h - szh) / 2;
		
		float bt = szw * Constants.Sizes.BorderThickness;
		canvas.drawRect(left - bt, top - bt, left, top + szh + bt, pntBorder);
		canvas.drawRect(left + szw, top - bt, left + szw + bt, top + szh + bt, pntBorder);
		canvas.drawRect(left - bt, top - bt, left + szw + bt, top, pntBorder);
		canvas.drawRect(left - bt, top + szh, left + szw + bt, top + szh + bt, pntBorder);
		
		canvas.drawRect(left, top, left + szw, top + szh, pntField);
		
		float blockplace = szw / game.hsize;
		float pad = blockplace * Constants.Sizes.BlockPad;
		float blocksize = blockplace - 2 * pad;
		float rad = blocksize * Constants.Sizes.BlockRoundRadius;
		pntBlockB.setStrokeWidth(blocksize * Constants.Sizes.BlockBorderThickness);
		float th = blocksize * Constants.Sizes.BlockNumberSize;
		pntBlockN.setTextSize(th);
		
		for (int r = 0; r < game.vsize; r++){
			for (int c = 0; c < game.hsize; c++){
				if (game.field[r][c] <= 0) continue;
				
				float l = left + pad + c*blockplace;
				float t = top + pad + r*blockplace;
				RectF rect = new RectF(l, t, l + blocksize, t + blocksize);
				canvas.drawRoundRect(rect, rad, rad, pntBlockF);
				canvas.drawRoundRect(rect, rad, rad, pntBlockB);
				String txt = "" + game.field[r][c];
				float tw = pntBlockN.measureText(txt);
				canvas.drawText(txt, l + (blocksize - tw) / 2, t + (2*blocksize - th) / 2, pntBlockN);
			}
		}
	}
}

package com.pl.mnogashki;
import android.view.*;
import android.app.*;
import android.graphics.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.content.res.*;

public class GameView extends View
{
	private Activity _context;
	
	private Paint pntBackground = new Paint();
	private Paint pntBorder = new Paint();
	private Paint pntField = new Paint();
	private Paint pntBlockF = new Paint();
	private Paint pntBlockFA = new Paint();
	private Paint pntBlockB = new Paint();
	private Paint pntBlockN = new Paint();
	private Paint pntSteps = new Paint();

	private float left;
	private float top;
	private float blockplace;
	private float szw;
	private float szh;

	private int activeRow = -1;
	private int activeCol = -1;
	
	private Game game;
	private static Timer _timer;
	
	public GameView(Activity context){
		super(context);
		_context = context;
		game = GameHolder.getGame(_context, false);
		
		Resources r = _context.getResources();
		pntBackground.setColor(r.getColor(R.color.background));
		pntBorder.setColor(r.getColor(R.color.border));
		pntField.setColor(r.getColor(R.color.field));
		
		pntBlockF.setColor(Color.LTGRAY);
		pntBlockF.setStyle(Paint.Style.FILL);
		pntBlockFA.setColor(Color.RED);
		pntBlockFA.setStyle(Paint.Style.FILL);
		pntBlockB.setColor(Color.GRAY);
		pntBlockB.setStyle(Paint.Style.STROKE);
		pntBlockN.setColor(Color.GRAY);
		pntSteps.setColor(Color.WHITE);
		
		if (_timer != null)
			_timer.cancel();
			
		_timer = new Timer();
		_timer.schedule(new TimerTask(){
			public void run(){
				_context.runOnUiThread(new Runnable(){
						public void run(){automove();}
				});
			}
		}, 500, 40);
	}

	private float beginX;
	private float beginY;
	private float delta = 0;
	private boolean automoving = false;
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if (automoving || game.isFinished())
					return true;
				int r = (int)Math.floor((event.getY() - top) / blockplace);
				int c = (int)Math.floor((event.getX() - left) / blockplace);
				if (r >= 0 && r < game.vsize && c >= 0 && c < game.hsize)
				{
					beginX = event.getX();
					beginY = event.getY();
					activeRow = r;
					activeCol = c;
					delta = 0;
					this.invalidate();
				}
				return true;
			case MotionEvent.ACTION_MOVE:
				if (automoving || game.isFinished())
					return true;
				Point free = game.getFreePos();
				if (activeRow == free.y){
					delta = (event.getX() - beginX) / blockplace;
					if (activeCol > free.x && delta > 0) delta = 0;
					if (activeCol < free.x && delta < 0) delta = 0;
				} 
				if (activeCol == free.x) {
					delta = (event.getY() - beginY) / blockplace;
					if (activeRow > free.y && delta > 0) delta = 0;
					if (activeRow < free.y && delta < 0) delta = 0;					
				}
				if (delta > 1) delta = 1;
				if (delta < -1) delta = -1;
				this.invalidate();
				return true;
			case MotionEvent.ACTION_UP:
				if (activeRow >= 0 && activeCol >= 0){
					automoving = true;
					checkStepComplete();
					this.invalidate();
				}
				return true;
		}
		
		return super.onTouchEvent(event);
	}
	
	private void automove(){
		if (!automoving) 
			return;
		
		if (Math.abs(delta) > 0.5)
		{
			delta = delta * (1f + (1.1f - Math.abs(delta)) * 0.2f);
			if (Math.abs(delta) > 1) delta = delta > 0 ? 1 : -1;
		} else {
			float newdelta = (delta + 0.1f) * Math.abs(delta + 0.1f) + 0.1f * (delta < 0 ? 1 : -1);
			if (newdelta * delta < 0) 
				delta = 0;
			else
				delta = delta + (newdelta - delta) * 0.4f;
		}
		
		this.invalidate();
			
		checkStepComplete();
	}
	
	private void checkStepComplete(){
		boolean complete = false;
		
		if (delta == 0)
		{
			complete = true; 
		}
		
		if (delta == -1 || delta == 1) {
			if (activeRow >= 0 && activeCol >= 0){
				int d = delta == 1 ? -1 : 1;
				Point free = game.getFreePos();
				if (free.y == activeRow){
					for (int i = free.x; i != activeCol; i += d){
						game.field[activeRow][i] = game.field[activeRow][i + d];
					}
					game.field[activeRow][activeCol] = -1;
					game.steps++;
				}
				if (free.x == activeCol) {
					for (int i = free.y; i != activeRow; i += d){
						game.field[i][activeCol] = game.field[i + d][activeCol];
					}
					game.field[activeRow][activeCol] = -1;
					game.steps++;
				}
			}
			complete = true;
		}
		
		if (complete){
			activeRow = -1;
			activeCol = -1;
			delta = 0;
			automoving = false;			
		}
		
		checkFinished();
	}

	private void checkFinished(){
		if (game.isFinished()){
			AlertDialog ad = new AlertDialog.Builder(_context).create();
			ad.setTitle(_context.getResources().getString(R.string.finished_title));
			ad.setMessage(
				String.format(_context.getResources().getString(R.string.finished_message), game.steps)
			);
			ad.setButton(_context.getResources().getString(R.string.shuffle),
				new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dlg, int btn){
						shuffle();
					}
				}
			);

			ad.show();
		}
	}
	
	public void shuffle(){
		game = GameHolder.getGame(_context, true);
		this.invalidate();
	}
	
	private void calcGeometry(Canvas canvas){
		float w = this.getMeasuredWidth();
		float h = this.getMeasuredHeight();
		
		float ratio = (float)game.hsize / (float)game.vsize;
		float avw = w * (1 - 2 * Constants.Sizes.BorderPercentage);
		float avh = h * (1 - 2 * Constants.Sizes.BorderPercentage);
		szw = avw > avh * ratio ? avh * ratio : avw;
		szh = szw / ratio;

		left = (w - szw) / 2;
		top = (h - szh) / 2;		

		blockplace = szw / game.hsize;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		calcGeometry(canvas);
		
		canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(), pntBackground);
		
		float bt = szw * Constants.Sizes.BorderThickness;
		canvas.drawRect(left - bt, top - bt, left, top + szh + bt, pntBorder);
		canvas.drawRect(left + szw, top - bt, left + szw + bt, top + szh + bt, pntBorder);
		canvas.drawRect(left - bt, top - bt, left + szw + bt, top, pntBorder);
		canvas.drawRect(left - bt, top + szh, left + szw + bt, top + szh + bt, pntBorder);
		
		canvas.drawRect(left, top, left + szw, top + szh, pntField);
		
		float stepsH = canvas.getHeight() / 50;
		pntSteps.setTextSize(stepsH);
		canvas.drawText(_context.getResources().getString(R.string.steps) + " " + game.steps, 5, 5 + stepsH, pntSteps);

		float pad = blockplace * Constants.Sizes.BlockPad;
		float blocksize = blockplace - 2 * pad;
		float rad = blocksize * Constants.Sizes.BlockRoundRadius;
		pntBlockB.setStrokeWidth(blocksize * Constants.Sizes.BlockBorderThickness);
		float th = blocksize * Constants.Sizes.BlockNumberSize;
		pntBlockN.setTextSize(th);
		
		Position[] positions = getBlockPositions();
		for (int n = 0; n < positions.length; n++){
			float l = left + pad + positions[n].col*blockplace;
			float t = top + pad + positions[n].row*blockplace;
			RectF rect = new RectF(l, t, l + blocksize, t + blocksize);
			canvas.drawRoundRect(rect, rad, rad, 
					positions[n].active ? pntBlockFA : pntBlockF);
			canvas.drawRoundRect(rect, rad, rad, pntBlockB);
			String txt = "" + (n + 1);
			float tw = pntBlockN.measureText(txt);
			canvas.drawText(txt, l + (blocksize - tw) / 2, t + (2*blocksize - th) / 2, pntBlockN);
		}
	}
	
	private Position[] getBlockPositions(){
		Position[] res = new Position[game.vsize * game.hsize - 1];
		
		for (int r = 0; r < game.vsize; r++){
			for (int c = 0; c < game.hsize; c++){
				int n = game.field[r][c];
				if (n <= 0) continue;
				
				res[n-1] = new Position(r, c, (r == activeRow && c == activeCol) ? true : false);
			}
		}
		
		if (activeRow >= 0 && activeCol >= 0 && delta != 0){
			Point free = game.getFreePos();
			if (free.y == activeRow){
				for (int i = activeCol; i != free.x; i += (delta > 0) ? 1 : -1){
					res[game.field[activeRow][i] - 1].col += delta;
				}
			} else{
				for (int i = activeRow; i != free.y; i += (delta > 0) ? 1 : -1){
					res[game.field[i][activeCol] - 1].row += delta;
				}
			}
		}
		
		return res;
	}
}

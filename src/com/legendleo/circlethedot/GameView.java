package com.legendleo.circlethedot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {

	//画笔
	private Paint background;
	private Paint movePaint;	
	private Paint blockPaint;
	private Paint dotPaint;
	private Paint dotIsSurrPaint;
	
	//屏幕宽高
	private int screenWidth;
	private int screenHeight;
	
	//圆半径
	private int radius;
	
	//选择的坐标
	private int clickX = 0;
	private int clickY = 0;
	
	private GameData gameData;
	private Dot dot;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//初始化 
		gameData = new GameData();
		dot = new Dot();
		
		//背景色画笔
		background = new Paint();
		background.setColor(getResources().getColor(R.color.background));
		
		//可移动点画笔
		movePaint = new Paint();
		movePaint.setColor(getResources().getColor(R.color.movePaint));
		movePaint.setAntiAlias(true);
		
		//不可移动点画笔
		blockPaint = new Paint();
		blockPaint.setColor(getResources().getColor(R.color.blockPaint));
		blockPaint.setAntiAlias(true);
		
		//Dot画笔
		dotPaint = new Paint();
		dotPaint.setColor(getResources().getColor(R.color.dotPaint));
		dotPaint.setAntiAlias(true);
		
		//被围住后的Dot画笔
		dotIsSurrPaint = new Paint();
		dotIsSurrPaint.setColor(getResources().getColor(R.color.dotIsSurrPaint));
		dotIsSurrPaint.setAntiAlias(true);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		//取得屏幕宽高
		screenWidth = w;
		screenHeight = h;
		radius = w/(9*2 + 1);
	}

	public void startGame(){
		
		//恢复初始值
		gameData.init();
		dot.setPos(40);
		dot.setIsSurrounded(false);
		
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//画出背景色
		canvas.drawRect(0, 0, screenWidth, screenHeight, background);
		
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				//未点击过的圆
				if(gameData.getNumByCoordinate(x, y) == 0){
					if(y%2 == 0){
						//偶数行
						canvas.drawCircle(radius*(x*2 + 1), radius*(y*2 + 1), radius, movePaint);
						
					}else{
						
						//奇数行延后一个半径
						canvas.drawCircle(radius*(x*2 + 2), radius*(y*2 + 1), radius, movePaint);
					}
					
				}else if(gameData.getNumByCoordinate(x, y) == 1){

					if(y%2 == 0){
						//偶数行
						canvas.drawCircle(radius*(x*2 + 1), radius*(y*2 + 1), radius, blockPaint);
						
					}else{
						
						//奇数行延后一个半径
						canvas.drawCircle(radius*(x*2 + 2), radius*(y*2 + 1), radius, blockPaint);
					}
					
				}else if(gameData.getNumByCoordinate(x, y) == 2){
					
					//中心圆点
					if(y%2 == 0){
						//偶数行
						canvas.drawCircle(radius*(x*2 + 1), radius*(y*2 + 1), radius, dotPaint);
						
						//判断是否已经被围住
						if(dot.getIsSurrounded())
							canvas.drawCircle(radius*(x*2 + 1), radius*(y*2 + 1), radius/2, dotIsSurrPaint);
						
					}else{
						
						//奇数行延后一个半径
						canvas.drawCircle(radius*(x*2 + 2), radius*(y*2 + 1), radius, dotPaint);
						
						//判断是否已经被围住
						if(dot.getIsSurrounded())
							canvas.drawCircle(radius*(x*2 + 2), radius*(y*2 + 1), radius/2, dotIsSurrPaint);
					}
				}
			}
		}
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN){
			return super.onTouchEvent(event);
		}
		
		//设置可点击范围，不能用屏幕宽度的值（大1个半径）
		if(event.getY() < radius*18){
			
			if((int)(event.getY()/(radius*2))%2 == 0){
				if(event.getX()/(radius*2)<9){
					//偶数行
					clickX = (int)(event.getX()/(radius*2));
					clickY = (int)(event.getY()/(radius*2));
				}
			}else{
				if(event.getX()/radius>1){
					//奇数行
					clickX = (int)((event.getX()-radius)/(radius*2));
					clickY = (int)(event.getY()/(radius*2));
				}
			}
			
			System.out.println("clickX:" + clickX + "  clickY:" + clickY);
			
			//点击后设置坐标值为1
			if(gameData.getIsBlocked(clickX, clickY)){
				
				invalidate();
				
				//判断游戏是否结束
				gameComplete();
				
				return true;
			}else{
				
				return false;
			}
			
		}else{
			
			return false;
		}
		
	}
	
	//判断游戏结束
	private void gameComplete(){
//		//方法一,取消
//		ALL:
//		for (int y = 0; y < 9; y++) {
//			for (int x = 0; x < 9; x++) {
//				
//				//取得Dot位置
//				if(gameData.getNumByCoordinate(x, y) == 2){
					
//					//判断Dot位置是否到达边界
//					if(y == 0 || y == 8 || x == 0 || x == 8){
//						
//						System.out.println("Game Over!");
//
//					    new AlertDialog.Builder(getContext())
//					    .setMessage("Game Over！")
//						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								// TODO Auto-generated method stub
//								
//								startGame();
//							}
//						})
//						.setCancelable(false).show();
//						
//						break ALL;
//						
//					}else{
//						//被周围六边形围住则赢
//						if(gameData.getNumByCoordinate(x-1, y-1) == 1 &&
//						   gameData.getNumByCoordinate(x, y-1) == 1 &&
//						   gameData.getNumByCoordinate(x-1, y) == 1 &&
//						   gameData.getNumByCoordinate(x+1, y) == 1 &&
//						   gameData.getNumByCoordinate(x-1, y+1) == 1 &&
//						   gameData.getNumByCoordinate(x, y+1) == 1){
//							
//						    System.out.println("You win!");
//						    
//						    new AlertDialog.Builder(getContext())
//						    .setMessage("恭喜你！你赢了！")
//							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//								
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									// TODO Auto-generated method stub
//									
//									startGame();
//								}
//							})
//							.setCancelable(false).show();
//						    
//						}else{
//
//							//被围住的判断
//							dot.setCircle(gameData.getCircle());
//							dot.getDots();
//							dot.setPos(y*9 + x);
//							gameData.setDotCoor(x, y, dot.next(y*9 + x));
//							invalidate();
//						}
//
//						break ALL;
//					}
//					
//					
//				}
//				
//			}
//		}
		
		//方法二

		//将当前地图数据传入
		dot.setCircle(gameData.getCircle());
		int currentPos = dot.getPos();  //取得当前位置
		//判断当前Dot位置是否已逃脱
		if(dot.escaped()){
			
			//lose
		    new AlertDialog.Builder(getContext())
		    .setMessage("Game Over！")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					startGame();
				}
			})
			.setCancelable(false).show();
			
		}else{
			//判断Dot是否还有路线可走
			if(dot.tryMove()){
				
				//如果可走（tryMove中已经将下一个位置赋值给当前位置了），则取得下一个位置
				int nextPos = dot.getPos();
				gameData.setDotCoor(currentPos, nextPos);
				invalidate();
				
			}else{
				//win
			    new AlertDialog.Builder(getContext())
			    .setMessage("恭喜你！你赢了！")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						startGame();
					}
				})
				.setCancelable(false).show();
			}
		}
	}
	
	
}



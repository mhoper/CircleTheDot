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

	//����
	private Paint background;
	private Paint movePaint;	
	private Paint blockPaint;
	private Paint dotPaint;
	private Paint dotIsSurrPaint;
	
	//��Ļ���
	private int screenWidth;
	private int screenHeight;
	
	//Բ�뾶
	private int radius;
	
	//ѡ�������
	private int clickX = 0;
	private int clickY = 0;
	
	private GameData gameData;
	private Dot dot;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//��ʼ�� 
		gameData = new GameData();
		dot = new Dot();
		
		//����ɫ����
		background = new Paint();
		background.setColor(getResources().getColor(R.color.background));
		
		//���ƶ��㻭��
		movePaint = new Paint();
		movePaint.setColor(getResources().getColor(R.color.movePaint));
		movePaint.setAntiAlias(true);
		
		//�����ƶ��㻭��
		blockPaint = new Paint();
		blockPaint.setColor(getResources().getColor(R.color.blockPaint));
		blockPaint.setAntiAlias(true);
		
		//Dot����
		dotPaint = new Paint();
		dotPaint.setColor(getResources().getColor(R.color.dotPaint));
		dotPaint.setAntiAlias(true);
		
		//��Χס���Dot����
		dotIsSurrPaint = new Paint();
		dotIsSurrPaint.setColor(getResources().getColor(R.color.dotIsSurrPaint));
		dotIsSurrPaint.setAntiAlias(true);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		//ȡ����Ļ���
		screenWidth = w;
		screenHeight = h;
		radius = w/(9*2 + 1);
	}

	public void startGame(){
		
		//�ָ���ʼֵ
		gameData.init();
		dot.setPos(40);
		dot.setIsSurrounded(false);
		
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//��������ɫ
		canvas.drawRect(0, 0, screenWidth, screenHeight, background);
		
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				//δ�������Բ
				if(gameData.getNumByCoordinate(x, y) == 0){
					if(y%2 == 0){
						//ż����
						canvas.drawCircle(radius*(x*2 + 1), radius*(y*2 + 1), radius, movePaint);
						
					}else{
						
						//�������Ӻ�һ���뾶
						canvas.drawCircle(radius*(x*2 + 2), radius*(y*2 + 1), radius, movePaint);
					}
					
				}else if(gameData.getNumByCoordinate(x, y) == 1){

					if(y%2 == 0){
						//ż����
						canvas.drawCircle(radius*(x*2 + 1), radius*(y*2 + 1), radius, blockPaint);
						
					}else{
						
						//�������Ӻ�һ���뾶
						canvas.drawCircle(radius*(x*2 + 2), radius*(y*2 + 1), radius, blockPaint);
					}
					
				}else if(gameData.getNumByCoordinate(x, y) == 2){
					
					//����Բ��
					if(y%2 == 0){
						//ż����
						canvas.drawCircle(radius*(x*2 + 1), radius*(y*2 + 1), radius, dotPaint);
						
						//�ж��Ƿ��Ѿ���Χס
						if(dot.getIsSurrounded())
							canvas.drawCircle(radius*(x*2 + 1), radius*(y*2 + 1), radius/2, dotIsSurrPaint);
						
					}else{
						
						//�������Ӻ�һ���뾶
						canvas.drawCircle(radius*(x*2 + 2), radius*(y*2 + 1), radius, dotPaint);
						
						//�ж��Ƿ��Ѿ���Χס
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
		
		//���ÿɵ����Χ����������Ļ��ȵ�ֵ����1���뾶��
		if(event.getY() < radius*18){
			
			if((int)(event.getY()/(radius*2))%2 == 0){
				if(event.getX()/(radius*2)<9){
					//ż����
					clickX = (int)(event.getX()/(radius*2));
					clickY = (int)(event.getY()/(radius*2));
				}
			}else{
				if(event.getX()/radius>1){
					//������
					clickX = (int)((event.getX()-radius)/(radius*2));
					clickY = (int)(event.getY()/(radius*2));
				}
			}
			
			System.out.println("clickX:" + clickX + "  clickY:" + clickY);
			
			//�������������ֵΪ1
			if(gameData.getIsBlocked(clickX, clickY)){
				
				invalidate();
				
				//�ж���Ϸ�Ƿ����
				gameComplete();
				
				return true;
			}else{
				
				return false;
			}
			
		}else{
			
			return false;
		}
		
	}
	
	//�ж���Ϸ����
	private void gameComplete(){
//		//����һ,ȡ��
//		ALL:
//		for (int y = 0; y < 9; y++) {
//			for (int x = 0; x < 9; x++) {
//				
//				//ȡ��Dotλ��
//				if(gameData.getNumByCoordinate(x, y) == 2){
					
//					//�ж�Dotλ���Ƿ񵽴�߽�
//					if(y == 0 || y == 8 || x == 0 || x == 8){
//						
//						System.out.println("Game Over!");
//
//					    new AlertDialog.Builder(getContext())
//					    .setMessage("Game Over��")
//						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
//						//����Χ������Χס��Ӯ
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
//						    .setMessage("��ϲ�㣡��Ӯ�ˣ�")
//							.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
//							//��Χס���ж�
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
		
		//������

		//����ǰ��ͼ���ݴ���
		dot.setCircle(gameData.getCircle());
		int currentPos = dot.getPos();  //ȡ�õ�ǰλ��
		//�жϵ�ǰDotλ���Ƿ�������
		if(dot.escaped()){
			
			//lose
		    new AlertDialog.Builder(getContext())
		    .setMessage("Game Over��")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					startGame();
				}
			})
			.setCancelable(false).show();
			
		}else{
			//�ж�Dot�Ƿ���·�߿���
			if(dot.tryMove()){
				
				//������ߣ�tryMove���Ѿ�����һ��λ�ø�ֵ����ǰλ���ˣ�����ȡ����һ��λ��
				int nextPos = dot.getPos();
				gameData.setDotCoor(currentPos, nextPos);
				invalidate();
				
			}else{
				//win
			    new AlertDialog.Builder(getContext())
			    .setMessage("��ϲ�㣡��Ӯ�ˣ�")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
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



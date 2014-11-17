package com.legendleo.circlethedot;

public class GameData {

	private int[] circle = new int[9*9];
	
	//Dot的初始位置
	private int dotX;
	private int dotY;
	
	//随机生成红点个数
	private int redDotNums = 15;

	//记录随机15个红点的位置
	int[] randomOne = new int[redDotNums];
	
	public GameData(){
		init();
		
	}
	
	//初始化数组
	public void init(){
		
		//初始化数值
		dotX = 4;
		dotY = 4;
		
		//生成15个红点位置
		generateRandomNums();
		
		for (int i = 0; i < circle.length; i++) {
			if(i == (dotY*9 + dotX)){
				//将Dot的坐标值设置为2
				circle[i] = 2;
				
			}else{
				
				boolean flag = true;
				
				for (int j : randomOne) {
					if(i == j){
						
						flag = false;
						break;
						
					}
				}
				
				if(flag){

					//可走的点的坐标值设置为0
					circle[i] = 0;
				}else{
					
					//不可走的点的坐标设置为1						
					circle[i] = 1;
				}
				
			}
			
		}
	}
	
	//记录用户点击的圆
	private void setBlockNum(int x, int y){
		
		circle[y*9 + x] = 1;
	}
	
	public boolean getIsBlocked(int x, int y){
		if(circle[y*9 + x] == 0){
			
			setBlockNum(x, y);
			return true;
			
		}else{
			
			return false;
		}
	}
	
	//根据坐标取得坐标的值
	public int getNumByCoordinate(int x, int y){
		
		return circle[y*9 + x];
	}
	
	public int[] getCircle() {
		return circle;
	}
	
	//随机生成15个红点
	public void generateRandomNums(){
		
		for (int i = 0; i < randomOne.length; i++) {
			
			//生成0-80的随机数，对应circle的坐标
			int rd = (int)(Math.random()*81);
			
			//排除Dot的位置
			if(rd == (dotY*9 + dotX)){
				i--;
				
			}else{
				
				if(i == 0){
					//第一位不用判断重复
					randomOne[i] = rd;
					
				}else{
					boolean same = false;
					
					for (int j = 0; j < i; j++) {
						//去掉重复的值
						if(rd == randomOne[j]){
							i--;
							same = true;
							break;
							
						}
						
					}
					//如果与前面生成的随机值不重复则添加
					if(!same){
						randomOne[i] = rd;
					}
				}
			}
		}
		
	}
	
	public void setDotCoor(int currentPos, int nextPos){
		circle[currentPos] = 0;
		circle[nextPos] = 2;
		
	}
	
}

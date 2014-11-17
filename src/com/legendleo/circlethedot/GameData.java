package com.legendleo.circlethedot;

public class GameData {

	private int[] circle = new int[9*9];
	
	//Dot�ĳ�ʼλ��
	private int dotX;
	private int dotY;
	
	//������ɺ�����
	private int redDotNums = 15;

	//��¼���15������λ��
	int[] randomOne = new int[redDotNums];
	
	public GameData(){
		init();
		
	}
	
	//��ʼ������
	public void init(){
		
		//��ʼ����ֵ
		dotX = 4;
		dotY = 4;
		
		//����15�����λ��
		generateRandomNums();
		
		for (int i = 0; i < circle.length; i++) {
			if(i == (dotY*9 + dotX)){
				//��Dot������ֵ����Ϊ2
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

					//���ߵĵ������ֵ����Ϊ0
					circle[i] = 0;
				}else{
					
					//�����ߵĵ����������Ϊ1						
					circle[i] = 1;
				}
				
			}
			
		}
	}
	
	//��¼�û������Բ
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
	
	//��������ȡ�������ֵ
	public int getNumByCoordinate(int x, int y){
		
		return circle[y*9 + x];
	}
	
	public int[] getCircle() {
		return circle;
	}
	
	//�������15�����
	public void generateRandomNums(){
		
		for (int i = 0; i < randomOne.length; i++) {
			
			//����0-80�����������Ӧcircle������
			int rd = (int)(Math.random()*81);
			
			//�ų�Dot��λ��
			if(rd == (dotY*9 + dotX)){
				i--;
				
			}else{
				
				if(i == 0){
					//��һλ�����ж��ظ�
					randomOne[i] = rd;
					
				}else{
					boolean same = false;
					
					for (int j = 0; j < i; j++) {
						//ȥ���ظ���ֵ
						if(rd == randomOne[j]){
							i--;
							same = true;
							break;
							
						}
						
					}
					//�����ǰ�����ɵ����ֵ���ظ������
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

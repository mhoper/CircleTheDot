package com.legendleo.circlethedot;

import java.util.ArrayList;
/*
 * ����ο��ԣ�https://github.com/ZTGeng/PsychoCatJars
 */

public class Dot {
    
    private int pos = 40;
    protected ArrayList<ArrayList<Integer>> edges;

	//�Ƿ񱻰�Χ
	private boolean isSurrounded = false;
    
	private int[] circle = new int[81];
    
	public void setCircle(int[] circle) {
		//���뵱ǰ��ͼ������
		System.arraycopy(circle, 0, this.circle, 0, circle.length);
		getDots();
	}

	//ȡ��ÿһ����Ľ����ŵĿ���λ��
    public void getDots() {

        edges = new ArrayList<ArrayList<Integer>>(81);
        int i, j;
        for (int n = 0; n < 81; n++) {
            edges.add(new ArrayList<Integer>());
            i = n / 9; //y
            j = n % 9; //x
            if (j != 0 && circle[i * 9 + j - 1] != 1) edges.get(n).add(i * 9 + j - 1); // ��߸�
            
            if (i % 2 == 1 && circle[(i - 1) * 9 + j] != 1) edges.get(n).add((i - 1) * 9 + j); // ���ϸ�
            else if (i % 2 == 0 && i != 0 && j != 0 && circle[(i - 1) * 9 + j - 1] != 1) edges.get(n).add((i - 1) * 9 + j - 1); // ���ϸ�
            
            if (i % 2 == 1 && j != 8 && circle[(i - 1) * 9 + j + 1] != 1) edges.get(n).add((i - 1) * 9 + j + 1); // ���ϸ�
            else if (i % 2 == 0 && i != 0 && circle[(i - 1) * 9 + j] != 1) edges.get(n).add((i - 1) * 9 + j); // ���ϸ�
            
            if (j != 8 && circle[i * 9 + j + 1] != 1) edges.get(n).add(i * 9 + j + 1); // �ұ߸�
            
            if (i % 2 == 1 && j != 8 && circle[(i + 1) * 9 + j + 1] != 1) edges.get(n).add((i + 1) * 9 + j + 1); // ���¸�
            else if (i % 2 == 0 && i != 8 && circle[(i + 1) * 9 + j] != 1) edges.get(n).add((i + 1) * 9 + j); // ���¸�
            
            if (i % 2 == 1 && circle[(i + 1) * 9 + j] != 1) edges.get(n).add((i + 1) * 9 + j); // ���¸�
            else if (i % 2 == 0 && i != 8 && j != 0 && circle[(i + 1) * 9 + j - 1] != 1) edges.get(n).add((i + 1) * 9 + j - 1); // ���¸�
        }

        
    }
    
    public int getPos() {
        return pos;
    }
    
    protected void setPos(int n) {
        pos = n;
    }
    
    public boolean tryMove() {
        int n = next(pos);
        if (n == pos) return false;
        
        //��������ߣ�����һ��λ�ø�ֵ��pos
        pos = n;
        return true;
    }
    
    public void close(int i, int j) {
        int v = i * 9 + j;
        // ˫�����v���漰�ı�
        for (int w : edges.get(v)) {
            edges.get(w).remove(Integer.valueOf(v));
        }
        edges.get(v).clear();
    }
    
    protected boolean escaped(int n) {
        return n < 9 || n > 71 || n % 9 == 0 || n % 9 == 8;
    }
    
    public boolean escaped() {
        return escaped(pos);
    }
    
    public int next(int n) {
    	
        ArrayList<Integer> reachable = new ArrayList<Integer>();
        int[] orient = new int[81]; // ��¼���ܷ���ĵ�һ��
        for (int w : edges.get(n)) {  //ѭ����ӵ�һ�㼶�ϵĿ�����λ��
            if (escaped(w)) return w;
            reachable.add(w);
            orient[w] = w;
        }
        int num = 0;
        while (num < reachable.size()) {
            // ��reachable��ȡ����һ��num����������ӵĸ��ӣ�һ���㼶һ���㼶�ļ�飬
            int v = reachable.get(num++);
            for (int w : edges.get(v)) {
            	
            	//�ж�һ�����õ�·�ߣ�ֱ�����������߽磬Ȼ�󷵻ظ��Ӳ㼶��ǵĵ�һ�㼶��λ�ã���ʵ���������������ϵ���߽����С·����
                if (escaped(w)) return orient[v];
                if (!reachable.contains(w)) {
                    reachable.add(w);  //���Ӳ㼶�����ܵ�λ����ӽ�ȥ�������ǹؼ�
                    orient[w] = orient[v]; //���Ӳ㼶�ϵķ�����Ϊ��һ�㼶��λ��
                }
            }
        }
        
        //����Χ��Ż�ִ�����´���
        if (reachable.size() == 0) return n; // һ�����޷���
        
        isSurrounded = true; // �ѱ�Χס���Կɶ�
        return reachable.get(0); // �ѱ�Χס���Կɶ� �����ȡ��һ��ֵ
        
    }
    
    //ȡ���Ƿ�Χס���ֵ
    public boolean getIsSurrounded(){
    	return isSurrounded;
    }
    
    public void setIsSurrounded(boolean bl){
    	this.isSurrounded = bl;
    }
    
    public ArrayList<Integer> around(int n) {
        return edges.get(n);
    }
    
}
package com.legendleo.circlethedot;

import java.util.ArrayList;
/*
 * 该类参考自：https://github.com/ZTGeng/PsychoCatJars
 */

public class Dot {
    
    private int pos = 40;
    protected ArrayList<ArrayList<Integer>> edges;

	//是否被包围
	private boolean isSurrounded = false;
    
	private int[] circle = new int[81];
    
	public void setCircle(int[] circle) {
		//传入当前地图的数据
		System.arraycopy(circle, 0, this.circle, 0, circle.length);
		getDots();
	}

	//取得每一个点的紧邻着的可用位置
    public void getDots() {

        edges = new ArrayList<ArrayList<Integer>>(81);
        int i, j;
        for (int n = 0; n < 81; n++) {
            edges.add(new ArrayList<Integer>());
            i = n / 9; //y
            j = n % 9; //x
            if (j != 0 && circle[i * 9 + j - 1] != 1) edges.get(n).add(i * 9 + j - 1); // 左边格
            
            if (i % 2 == 1 && circle[(i - 1) * 9 + j] != 1) edges.get(n).add((i - 1) * 9 + j); // 左上格
            else if (i % 2 == 0 && i != 0 && j != 0 && circle[(i - 1) * 9 + j - 1] != 1) edges.get(n).add((i - 1) * 9 + j - 1); // 左上格
            
            if (i % 2 == 1 && j != 8 && circle[(i - 1) * 9 + j + 1] != 1) edges.get(n).add((i - 1) * 9 + j + 1); // 右上格
            else if (i % 2 == 0 && i != 0 && circle[(i - 1) * 9 + j] != 1) edges.get(n).add((i - 1) * 9 + j); // 右上格
            
            if (j != 8 && circle[i * 9 + j + 1] != 1) edges.get(n).add(i * 9 + j + 1); // 右边格
            
            if (i % 2 == 1 && j != 8 && circle[(i + 1) * 9 + j + 1] != 1) edges.get(n).add((i + 1) * 9 + j + 1); // 右下格
            else if (i % 2 == 0 && i != 8 && circle[(i + 1) * 9 + j] != 1) edges.get(n).add((i + 1) * 9 + j); // 右下格
            
            if (i % 2 == 1 && circle[(i + 1) * 9 + j] != 1) edges.get(n).add((i + 1) * 9 + j); // 左下格
            else if (i % 2 == 0 && i != 8 && j != 0 && circle[(i + 1) * 9 + j - 1] != 1) edges.get(n).add((i + 1) * 9 + j - 1); // 左下格
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
        
        //如果还可走，则将下一步位置赋值给pos
        pos = n;
        return true;
    }
    
    public void close(int i, int j) {
        int v = i * 9 + j;
        // 双向清除v点涉及的边
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
        int[] orient = new int[81]; // 记录逃跑方向的第一步
        for (int w : edges.get(n)) {  //循环添加第一层级上的可逃跑位置
            if (escaped(w)) return w;
            reachable.add(w);
            orient[w] = w;
        }
        int num = 0;
        while (num < reachable.size()) {
            // 从reachable中取出下一个num，检查其连接的格子，一个层级一个层级的检查，
            int v = reachable.get(num++);
            for (int w : edges.get(v)) {
            	
            	//判断一条可用的路线，直到到达最外层边界，然后返回该子层级标记的第一层级的位置（其实是搜索六个方向上到达边界的最小路径）
                if (escaped(w)) return orient[v];
                if (!reachable.contains(w)) {
                    reachable.add(w);  //将子层级可逃跑的位置添加进去，这里是关键
                    orient[w] = orient[v]; //将子层级上的方向标记为第一层级的位置
                }
            }
        }
        
        //被包围后才会执行以下代码
        if (reachable.size() == 0) return n; // 一步都无法动
        
        isSurrounded = true; // 已被围住但仍可动
        return reachable.get(0); // 已被围住但仍可动 则随便取第一个值
        
    }
    
    //取得是否被围住标记值
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
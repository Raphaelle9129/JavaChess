import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;


public abstract class Piece {

	String piece;
	String color;
	int[][] move;
	int[][] attack;
	int[] pos=new int[2];
	int limit;
	int direction;
	boolean backwards;
	
	boolean hasMoved=false;
	
	boolean[][] movesAvailable=new boolean[8][8];
	
	public Piece(String color, int x, int y) {
		
		pos[0]=x;
		pos[1]=y;
		this.color=color;
		if (color.equals("White")) {
			direction=-1;
		}else {
			direction=1;
		}
		
	}
	
	public void move(int newX, int newY) {
		pos[0]=newX;
		pos[1]=newY;
		hasMoved=true;
		if (this instanceof Pawn) {
			limit=1;
		}
		for(int i=0;i<8;i++){for(int j=0;j<8;j++){movesAvailable[i][j]=false;}}
	}
	
	public String getIcon() {
		return piece;
	}
	
	public void addMove(int x, int y) {
		movesAvailable[x][y]=true;
	}
	
	public boolean getIfMovable(int toX, int toY) {
		return movesAvailable[toX][toY];
	}
	
	public String getColor() {
		return color;
	}
	
	public boolean getBackwards() {
		return backwards;
	}
	
	public int[][] getMovements() {
		return move;
	}
	
	public int[][] getAttacks() {
		return attack;
	}
	
	public int[] getPos() {
		return pos.clone();
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void draw(GraphicsContext g, int x1, int y1, int len, int hei) {
		
		g.setFont(new Font("Regular", (double)len));
		
		g.fillText(piece, x1, y1);
	}
	
}

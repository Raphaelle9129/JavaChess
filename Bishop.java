//fou
public class Bishop extends Piece {

	public Bishop(String color, int x, int y) {
		super(color, x, y);
		
		if (direction==1) {
			piece="\u265D";
		}else {
			piece="\u2657";
		}
		
		move=new int[4][2];
		attack=new int[4][2];
		
		limit=8;
		backwards=true;
		
		move[0][0]=1;
		move[0][1]=1;
	
		move[1][0]=1;
		move[1][1]=-1;
		
		move[2][0]=-1;
		move[2][1]=-1;
		
		move[3][0]=-1;
		move[3][1]=1;
		
		attack=move;
		
	}
}

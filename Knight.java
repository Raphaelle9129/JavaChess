//chevalier d'ebène
public class Knight extends Piece {

	public Knight(String color, int x, int y) {
		super(color, x, y);
		
		if (direction==1) {
			piece="\u265E";
		}else {
			piece="\u2658";
		}
		
		move=new int[8][2];
		attack=new int[8][2];
		
		backwards=true;
		limit=1;
		
		move[0][0]=1;
		move[0][1]=2;
		
		move[1][0]=2;
		move[1][1]=1;
		
		move[2][0]=2;
		move[2][1]=-1;
		
		move[3][0]=1;
		move[3][1]=-2;
		
		move[4][0]=-1;
		move[4][1]=-2;
		
		move[5][0]=-2;
		move[5][1]=-1;
		
		move[6][0]=-2;
		move[6][1]=1;
		
		move[7][0]=-1;
		move[7][1]=2;
				
		attack=move;
	}

}

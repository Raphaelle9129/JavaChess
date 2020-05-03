//tour
public class Rook extends Piece {
	
	
	
	public Rook(String color, int x, int y) {
		
		super(color, x, y);
		
		if (direction==1) {
			piece="\u265C";
		}else {
			piece="\u2656";
		}
		
		move=new int[4][2];
		attack=new int[2][2];
		
		backwards=true;
		limit=8;
		
		move[0][0]=1;
		move[0][1]=0;
		
		move[1][0]=0;
		move[1][1]=1;
		
		move[2][0]=-1;
		move[2][1]=0;
		
		move[3][0]=0;
		move[3][1]=-1;
		
		attack=move;
		
		/*attack[0][0]=1;
		attack[0][1]=0;
		attack[1][0]=0;
		attack[1][1]=direction;*/
		
	}

}

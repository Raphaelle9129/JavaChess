
public class Pawn extends Piece {
	
	public Pawn(String color, int x, int y) {
		
		super(color, x, y);
		
		if (direction==1) {
			piece="\u265F";
		}else {
			piece="\u2659";
		}
		
		move=new int[1][2];
		attack=new int[2][2];
		
		move[0][0]=0;
		attack[0][0]=1;
		attack[1][0]=-1;
		backwards=false;
		limit=2;
		
		move[0][1]=direction;
		attack[0][1]=direction;
		attack[1][1]=direction;
		
	}
}

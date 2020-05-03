import java.util.ArrayList;

//Aragorn
public class King extends Piece{

	ArrayList<Rook> rocks=new ArrayList<Rook>();
	
	boolean menaced=false;
	
	public King(String color, int x, int y) {
		super(color, x, y);
		
		if (direction==1) {
			piece="\u265A";
		}else {
			piece="\u2654";
		}
		
		move=new int[8][2];
		attack=new int[8][2];
		
		backwards=true;
		limit=1;
		
		move[0][0]=1;
		move[0][1]=1;
		
		move[1][0]=1;
		move[1][1]=0;
		
		move[2][0]=1;
		move[2][1]=-1;
		
		move[3][0]=0;
		move[3][1]=-1;
		
		move[4][0]=-1;
		move[4][1]=-1;
		
		move[5][0]=-1;
		move[5][1]=0;
		
		move[6][0]=-1;
		move[6][1]=1;
		
		move[7][0]=0;
		move[7][1]=1;
		
		attack=move;
		
		/*attack[0][0]=1;
		attack[0][1]=direction;
		attack[1][0]=1;
		attack[1][1]=0;
		attack[2][0]=0;
		attack[2][1]=direction;*/
	}

	
}

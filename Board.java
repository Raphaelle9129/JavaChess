import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Board {

	Piece[][] board=new Piece[8][8];
	ArrayList<Piece> blackPieces=new ArrayList<Piece>();
	ArrayList<Piece> whitePieces=new ArrayList<Piece>();
	ArrayList[][] whiteMenace=new ArrayList[8][8];
	ArrayList[][] blackMenace=new ArrayList[8][8];
	ArrayList[][] whiteMoves=new ArrayList[8][8];
	ArrayList[][] blackMoves=new ArrayList[8][8];
	ArrayList<Piece> whiteGraveyard=new ArrayList<Piece>();
	ArrayList<Piece> blackGraveyard=new ArrayList<Piece>();
	int[][] coord=new int[8][8];
	boolean[][] moves=new boolean[8][8];
	Canvas can;

	Piece whiteKing;
	Piece blackKing;
	
	Piece upgrade=null;

	Piece selected=null;
	int dx;
	int dy;
	String turn="White";
	
	boolean winWhite=false;
	boolean winBlack=false;

	public Board() {

		init();
		
	}
	
	public Board(Canvas can) {

		this.can=can;
		init();
		

	}
	
	public void init() {
		
		dx=(int)((can.getWidth()-120)/8);
		dy=(int)(can.getHeight()/8);

		board[0][0]=new Rook("Black", 0, 0); board[7][0]=new Rook("Black", 7, 0);
		board[1][0]=new Knight("Black", 1, 0); board[6][0]=new Knight("Black", 6, 0);
		board[2][0]=new Bishop("Black", 2, 0); board[5][0]=new Bishop("Black", 5, 0);
		board[3][0]=new Queen("Black", 3, 0);
		board[4][0]=new King("Black", 4, 0);
		blackKing=board[4][0];

		board[0][7]=new Rook("White", 0, 7); board[7][7]=new Rook("White", 7, 7);
		board[1][7]=new Knight("White", 1, 7); board[6][7]=new Knight("White", 6, 7);
		board[2][7]=new Bishop("White", 2, 7); board[5][7]=new Bishop("White", 5, 7);
		board[3][7]=new Queen("White", 3, 7);
		board[4][7]=new King("White", 4, 7);
		whiteKing=board[4][7];

		for (int i=0; i<8; i++) {

			board[i][1]=new Pawn("Black", i, 1);
			board[i][6]=new Pawn("White", i, 6);
		}

		for (int i=0; i<2; i++) {
			for (int j=0; j<8; j++) {
				blackPieces.add(board[j][i]);
				whitePieces.add(board[j][i+6]);
			}
		}

		genMenace(whitePieces, whiteMenace);
		genMenace(blackPieces, blackMenace);
		genMoveGrid(whitePieces, whiteMoves);
		genMoveGrid(blackPieces, blackMoves);		

	}

	public void resetMoves() {
		for(int i=0; i<moves.length; i++) {
			for (int j=0; j<moves[i].length; j++) {
				moves[i][j]=false;
			}
		}
	}

	public void genMenace(ArrayList<Piece> Menaces, ArrayList[][] menaceGrid) {

		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				menaceGrid[i][j]=new ArrayList<Piece>();
			}
		}

		for (int i=0; i<Menaces.size(); i++) {

			Piece active=Menaces.get(i);

			int[][] attacks=active.getAttacks();
			int startX=active.getPos()[0];
			int startY=active.getPos()[1];
			int limit=active.getLimit();

			if (active instanceof Pawn) {limit=1;}

			for (int j=0; j<attacks.length; j++) {

				int index=0;
				int control=limit;

				while (index<control) {

					index++;
					int newX=startX+index*attacks[j][0];
					int newY=startY+index*attacks[j][1];

					if (newX>=0 && newX<8 && newY>=0 && newY<8) {

						menaceGrid[newX][newY].add(active);

						if (board[newX][newY]!=null) {

							if (board[newX][newY] instanceof King && !active.getColor().equals(board[newX][newY].getColor())) {

								control=index+1;
							}else {
								index=9;
							}

						}						
					}else {
						index=9;
					}
				}
			}
		}		
	}

	public void genMoveGrid(ArrayList<Piece> pieces, ArrayList[][] movements) {

		for (int j=0; j<8; j++) {
			for (int k=0; k<8; k++) {
				movements[j][k]=new ArrayList<Piece>();
			}
		}

		for (int i=0; i<pieces.size(); i++) {
			Piece active=pieces.get(i);
			getMovements(active, movements);
			for (int j=0; j<8; j++) {
				for (int k=0; k<8; k++) {
					if (moves[j][k]) {
						movements[j][k].add(active);
					}
				}
			}
		}		
	}

	public void getMovements(Piece toMove, ArrayList[][] movements){

		resetMoves();

		int startX=toMove.getPos()[0];
		int startY=toMove.getPos()[1];
		String color=toMove.getColor();
		int[][] Pmoves=toMove.getMovements();
		int[][] attacks=toMove.getAttacks();
		int length=toMove.getLimit();

		ArrayList[][] menaceActive;

		if (toMove.getColor().contentEquals("White")) {
			menaceActive=blackMenace;
		}else {
			menaceActive=whiteMenace;
		}

		if (toMove instanceof Pawn) {

			for (int i=0; i<attacks.length; i++) {

				int nextPx=startX+attacks[i][0];
				int nextPy=startY+attacks[i][1];

				if (nextPx<8 && nextPx>=0 && nextPy<8 && nextPy>=0) {
					if (board[nextPx][nextPy]!=null && !board[nextPx][nextPy].getColor().equals(color)) {
						moves[nextPx][nextPy]=true;
					}
				}
			}
		}

		if (toMove instanceof Rook) {

			if (toMove.hasMoved==false) {

				Piece king;
				if (toMove.getColor().equals("White")) {
					king=whiteKing;
				}else {
					king=blackKing;
				}

				if (king.hasMoved==false) {

					int[] posK=king.getPos();
					int[] posR=toMove.getPos();

					if (menaceActive[posK[0]][posK[1]].size()==0 && menaceActive[posR[0]][posR[1]].size()==0) {
						int d=posK[0]-posR[0]; int start=posR[0];
						int iter=d/Math.abs(d);
						boolean valableRock=true;
						while (start!=posK[0]) {
							start+=iter;
							if (board[start][posK[1]]!=null && menaceActive[start][posK[1]].size()==0) {
								if (!(board[start][posK[1]] instanceof King || board[start][posK[1]] instanceof Rook)) {
									valableRock=false;
								}
							}
						}
						if (valableRock) {
							movements[posR[0]][posR[1]].add(king);
						}

					}

				}

			}
		}

		for (int i=0; i<Pmoves.length; i++) {

			int index=0;

			while (index<length) {

				index++;
				int ddx=index*Pmoves[i][0];
				int ddy=index*Pmoves[i][1];

				int nextX=startX+ddx;
				int nextY=startY+ddy;

				if (nextX<8 && nextX>=0 && nextY<8 && nextY>=0) {


					if (board[nextX][nextY]==null) {

						if (toMove instanceof King) {
							if (menaceActive[nextX][nextY].size()==0) {
								moves[nextX][nextY]=true;
							}
						}else {
							moves[nextX][nextY]=true;
						}

					}else if (board[nextX][nextY]!=null && !(toMove instanceof Pawn) && !board[nextX][nextY].getColor().equals(color)) {

						if (!(board[nextX][nextY] instanceof King)) {
							if (toMove instanceof King) {
								if (menaceActive[nextX][nextY].size()==0) {
									moves[nextX][nextY]=true;
								}
							}else {
								moves[nextX][nextY]=true;
							}
						}
						index=9;

					}else {
						index=9;
					}

				}else {
					index=9;
				}

			}
		}
	}


	public void click(int x, int y, int decal) {

		int caseX=0;
		int caseY=0;
		boolean boardInteract=false;
		
		ArrayList<Piece> GraveyardToPick=null;
		
		if (decal<x && x<780) {
			boardInteract=true;
			caseX=(x-decal)/dx;
			caseY=y/dx;
		}else {
			
			if (x<decal) {
				GraveyardToPick=whiteGraveyard;
			}else {
				GraveyardToPick=blackGraveyard;
			}
			System.out.println(y);
			caseY=y/40;
			
		}
		
		if (upgrade!=null) {
			
			boardInteract=false;
			if (upgrade.getColor().equals("White") && whiteGraveyard.size()>0) {
				
				if (GraveyardToPick!=null && caseY<GraveyardToPick.size() && GraveyardToPick==whiteGraveyard) {
					int[] prePos=upgrade.getPos();
					board[prePos[0]][prePos[1]]=GraveyardToPick.get(caseY);
					GraveyardToPick.set(caseY, upgrade);
					whitePieces.remove(upgrade);
					upgrade=null;
					board[prePos[0]][prePos[1]].move(prePos[0], prePos[1]);
					whitePieces.add(board[prePos[0]][prePos[1]]);
				}
				
			}else if(upgrade.getColor().equals("Black") && blackGraveyard.size()>0) {
				
				if (GraveyardToPick!=null && caseY<GraveyardToPick.size() && GraveyardToPick==blackGraveyard) {
					int[] prePos=upgrade.getPos();
					board[prePos[0]][prePos[1]]=GraveyardToPick.get(caseY);
					GraveyardToPick.set(caseY, upgrade);
					blackPieces.remove(upgrade);
					upgrade=null;
					board[prePos[0]][prePos[1]].move(prePos[0], prePos[1]);
					blackPieces.add(board[prePos[0]][prePos[1]]);
				}
				
			}else {
				
				upgrade=null;
			}			
		}

		if (selected==null && boardInteract) {

			if (board[caseX][caseY]!=null) {

				if (board[caseX][caseY].getColor().equals(turn)) {

					selected=board[caseX][caseY];
				}
			}

		}else if (selected!=null) {

			boolean turnEnded=false;

			ArrayList[][] movesActive;
			ArrayList[][] menaceActive;
			ArrayList<Piece> piecePassive;
			ArrayList<Piece> graveyardActive;

			if (selected.getColor().equals("White")) {
				movesActive=whiteMoves;
				menaceActive=whiteMenace;
				piecePassive=blackPieces;
				graveyardActive=blackGraveyard;
			}else {
				movesActive=blackMoves;
				menaceActive=blackMenace;
				piecePassive=whitePieces;
				graveyardActive=whiteGraveyard;
			}
			//stocker les mouvements dans chaques pieces->liste  pieces bougeables

			if (movesActive[caseX][caseY].indexOf(selected)!=-1) {

				turnEnded=true;

				if (selected instanceof King && board[caseX][caseY] instanceof Rook) {

					int[] preK=selected.getPos();
					int[] preR=board[caseX][caseY].getPos();
					
					int ddx=preR[0]-preK[0];
					int[] newK= {2*(ddx/Math.abs(ddx))+preK[0], preK[1]};
					int[] newR= {(ddx/Math.abs(ddx))+preK[0], preR[1]};
					
					board[newK[0]][newK[1]]=selected;
					board[preK[0]][preK[1]]=null;
					selected.move(newK[0], newK[1]);
					
					board[newR[0]][newR[1]]=board[caseX][caseY];
					board[preR[0]][preR[1]]=null;
					board[newR[0]][newR[1]].move(newR[0], newR[1]);
					
				}else{

					int[] prevPos=selected.getPos();

					if (board[caseX][caseY]!=null) {

						piecePassive.remove(board[caseX][caseY]);
						graveyardActive.add(board[caseX][caseY]);

					}
					
					board[caseX][caseY]=selected;
					board[prevPos[0]][prevPos[1]]=null;
					selected.move(caseX, caseY);
					
					if (selected instanceof Pawn) {
						if (selected.getPos()[1]==0 || selected.getPos()[1]==7) {
							upgrade=selected;
						}
					}
				}
			}				

			if (turnEnded) {		
				
				if (turn.equals("White")) {
					turn="Black";
				}else {
					turn="White";
				}
				
				genMenace(whitePieces, whiteMenace);
				genMenace(blackPieces, blackMenace);
				genMoveGrid(blackPieces, blackMoves);
				genMoveGrid(whitePieces, whiteMoves);		
			
				if (checkMat("White")) {
					System.out.println("Les Noirs ont gagné");winBlack=true;
				}
				if (checkMat("Black")) {
					System.out.println("Les Blancs ont gagné");winWhite=true;
				}
			}

			selected=null;
		}
		

		
	}
	
	public boolean checkMat(String color) {
		
		boolean isMat=false;
		
		ArrayList[][] moves;
		ArrayList[][] menace;
		ArrayList[][] menaceSauver;
		Piece King;
		
		if (color.equals("White")) {
			moves=whiteMoves;
			menace=blackMenace;
			menaceSauver=whiteMenace;
			King=whiteKing;
		}else {
			moves=blackMoves;
			menace=whiteMenace;
			menaceSauver=blackMenace;
			King=blackKing;
		}
		
		int[] posKing=King.getPos();
		
		
		if (menace[posKing[0]][posKing[1]].size()>0) {
			
			System.out.println("Echec");
			
			boolean kingMove=false;
			
			for (int i=0; i<8; i++) {
				for (int j=0; j<8; j++) {
					if (moves[i][j].indexOf(King)!=-1) {
						kingMove=true;
					}
				}
			}
			
			
			if (kingMove) {
				return false;
			
			}else {
				
				if (menace[posKing[0]][posKing[1]].size()>1) {
					return true;
				}
				
				Piece vilain=(Piece)menace[posKing[0]][posKing[1]].get(0);				
				ArrayList<Piece> canMove=new ArrayList();
				
				if (menaceSauver[vilain.getPos()[0]][vilain.getPos()[1]].size()>0) {
					for (int i=0; i<menaceSauver[vilain.getPos()[0]][vilain.getPos()[1]].size(); i++) {
						Piece toAdd=(Piece) menaceSauver[vilain.getPos()[0]][vilain.getPos()[1]].get(i);
						if (!(toAdd instanceof King)) {
							canMove.add(toAdd);
						}
					}
				
				}else if(vilain instanceof Knight || vilain instanceof Pawn) {
					return true;
				}
				
				int[] direction= {vilain.getPos()[0]-posKing[0], vilain.getPos()[1]-posKing[1]};
				
				if (direction[0]!=0) {direction[0]=direction[0]/Math.abs(direction[0]);}
				if (direction[1]!=0) {direction[1]=direction[1]/Math.abs(direction[1]);}
				
				int x=1;
				int y=1;
				while (board[posKing[0]+x*direction[0]][posKing[1]+y*direction[1]]!=vilain) {
					
					if (moves[posKing[0]+x*direction[0]][posKing[1]+y*direction[1]].size()>0) {
						
						for (int i=0; i<moves[posKing[0]+x*direction[0]][posKing[1]+y*direction[1]].size(); i++) {
							Piece toAdd=(Piece) moves[posKing[0]+x*direction[0]][posKing[1]+y*direction[1]].get(i);
							if (canMove.indexOf(toAdd)==-1 && !(toAdd instanceof King)) {
								canMove.add(toAdd);
							}
						}
						
					}
					x++;
					y++;
				}
				
				/*for (int i=0; i<canMove.size(); i++) {
					System.out.print(canMove.get(i).getIcon());
				}*/
				
				if (canMove.size()>0) {
					return false;
				}else {
					return true;
				}
				
			}
		}
		
		
		return isMat;
	}

	public void drawMenace(ArrayList[][] menace, int color, int decal) {

		GraphicsContext g=can.getGraphicsContext2D();

		g.setFont(new Font("Regular", 18));

		for (int i=0; i<8; i++) {

			for (int j=0; j<8; j++) {

				String miniIcon="";
				for (int k=0; k<menace[i][j].size(); k++) {
					miniIcon+=((Piece) menace[i][j].get(k)).getIcon();
				}

				if (color==0) {				
					g.fillText(miniIcon, i*dx+decal, j*dy+20);
				}else {
					g.fillText(miniIcon, i*dx+decal, j*dy+40);
				}

			}
		}

	}

	public void drawGraveyards(int decal) {
		GraphicsContext g=can.getGraphicsContext2D();

		for (int i=0; i<whiteGraveyard.size(); i++) {
			whiteGraveyard.get(i).draw(g, 0, i*40+40, 40, 0);
		}

		for (int j=0; j<blackGraveyard.size(); j++) {
			blackGraveyard.get(j).draw(g, 780, j*40+40, 40, 0);
		}
	}

	public void draw(boolean attacks, int decal) {

		GraphicsContext g=can.getGraphicsContext2D();

		g.setFill(Color.WHITE);
		g.fillRect(0,0, can.getWidth(), can.getHeight());

		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {

				if ((x+y)%2==0) {
					g.setFill(Color.BEIGE);
				}else {
					g.setFill(Color.DIMGRAY);
				}

				g.fillRect(x*dx+decal, y*dy, dx, dy);
			}
		}

		g.setFill(Color.BLACK);

		for (int x=0; x<8; x++) {

			for (int y=0; y<8; y++) {

				if (board[x][y]!=null) {
					if (board[x][y]==selected) {
						g.setFill(Color.GREEN);
					}
					
					if (board[x][y]==whiteKing && winBlack) {
						g.setFill(Color.RED);
					}else if (board[x][y]==blackKing && winWhite){
						g.setFill(Color.RED);
					}					
					if (attacks) {
						board[x][y].draw(g, x*dx+decal, y*dy+dy, dx-20, dy);
					}else {
						board[x][y].draw(g, x*dx+decal, y*dy+dy, dx, dy);
					}
					g.setFill(Color.BLACK);
				}
			}
		}

		if (attacks) {
			drawMenace(whiteMenace, 0, decal);
			drawMenace(blackMenace, 1, decal);
		}
		drawGraveyards(decal);
	}

}

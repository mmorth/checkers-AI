import java.util.ArrayList;

/**
 * Represents the state of the checker board
 * 
 * @author mmort
 *
 */
public class Board {
	/**
	 * The pieces that belong to the human player
	 */
	private ArrayList<Piece> humanPieces;

	/**
	 * The pieces that belong to the agent player
	 */
	private ArrayList<Piece> agentPieces;

	/**
	 * The location of the pieces on the board for quick comparison
	 */
	private char positions[][];

	/**
	 * True if it is the player's turn. False otherwise
	 */
	private boolean isHumanTurn;
	
	/**
	 * True if the previous move was a jump (used for multi-jump logic)
	 */
	private boolean wasJump;

	public Board() {
		this.humanPieces = new ArrayList<>();
		this.agentPieces = new ArrayList<>();
		this.positions = new char[17][17];
		this.isHumanTurn = true;
		this.wasJump = false;
		setInitialPositions();
	}

	public Board(Board b) {
		ArrayList<Piece> tempHP = new ArrayList<>();

		// Populate with copies
		for (Piece piece : b.getHumanPieces()) {
			tempHP.add(new Piece(piece));
		}

		this.humanPieces = new ArrayList<>(tempHP);

		ArrayList<Piece> tempAP = new ArrayList<>();

		// Populate with copies
		for (Piece piece : b.getAgentPieces()) {
			tempAP.add(new Piece(piece));
		}

		this.agentPieces = new ArrayList<>(tempAP);

		this.positions = new char[17][17];

		// Copy positions into new array
		for (int i = 0; i <= 16; i++) {
			for (int j = 0; j <= 16; j++) {
				this.positions[i][j] = b.getPositions()[i][j];
			}
		}

		this.isHumanTurn = b.isHumanTurn();
		this.wasJump = b.isWasJump();
	}

	/**
	 * Set the initial positions of the checker pieces on the board
	 */
	public void setInitialPositions() {
		// Add the initial position of each piece
		agentPieces.add(new Piece('a', 1, 3, false));
		agentPieces.add(new Piece('b', 1, 7, false));
		agentPieces.add(new Piece('c', 1, 11, false));
		agentPieces.add(new Piece('d', 1, 15, false));
		agentPieces.add(new Piece('e', 3, 1, false));
		agentPieces.add(new Piece('f', 3, 5, false));
		agentPieces.add(new Piece('g', 3, 9, false));
		agentPieces.add(new Piece('h', 3, 13, false));
		agentPieces.add(new Piece('i', 5, 3, false));
		agentPieces.add(new Piece('j', 5, 7, false));
		agentPieces.add(new Piece('k', 5, 11, false));
		agentPieces.add(new Piece('l', 5, 15, false));

		humanPieces.add(new Piece('a', 11, 1, true));
		humanPieces.add(new Piece('b', 11, 5, true));
		humanPieces.add(new Piece('c', 11, 9, true));
		humanPieces.add(new Piece('d', 11, 13, true));
		humanPieces.add(new Piece('e', 13, 3, true));
		humanPieces.add(new Piece('f', 13, 7, true));
		humanPieces.add(new Piece('g', 13, 11, true));
		humanPieces.add(new Piece('h', 13, 15, true));
		humanPieces.add(new Piece('i', 15, 1, true));
		humanPieces.add(new Piece('j', 15, 5, true));
		humanPieces.add(new Piece('k', 15, 9, true));
		humanPieces.add(new Piece('l', 15, 13, true));

		// Update board positions in auxiliary array
		for (int i = 0; i <= 16; i++) {
			for (int j = 0; j <= 16; j++) {
				this.positions[i][j] = 'o';
			}
		}

		this.positions[1][3] = 'a';
		this.positions[1][7] = 'a';
		this.positions[1][11] = 'a';
		this.positions[1][15] = 'a';
		this.positions[3][1] = 'a';
		this.positions[3][5] = 'a';
		this.positions[3][9] = 'a';
		this.positions[3][13] = 'a';
		this.positions[5][3] = 'a';
		this.positions[5][7] = 'a';
		this.positions[5][11] = 'a';
		this.positions[5][15] = 'a';

		this.positions[11][1] = 'h';
		this.positions[11][5] = 'h';
		this.positions[11][9] = 'h';
		this.positions[11][13] = 'h';
		this.positions[13][3] = 'h';
		this.positions[13][7] = 'h';
		this.positions[13][11] = 'h';
		this.positions[13][15] = 'h';
		this.positions[15][1] = 'h';
		this.positions[15][5] = 'h';
		this.positions[15][9] = 'h';
		this.positions[15][13] = 'h';
	}

	/**
	 * Draw the board on the console given each piece's position
	 */
	public void drawBoard() {
		// Source:
		// https://stackoverflow.com/questions/16062888/creating-a-checkerboard-in-java-using-a-while-loop
		for (int i = 0; i <= 16; i++) {
			for (int j = 0; j <= 16; j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					System.out.print("+");
				} else if (i % 2 != 0 && j % 2 == 0) {
					System.out.print("|");
				} else if (j % 2 != 0 && i % 2 == 0) {
					System.out.print("----");
				} else if (j % 2 != 0 && i % 2 != 0) {
					boolean isAgent = false;
					for (int k = 0; k < agentPieces.size(); k++) {
						Piece current = agentPieces.get(k);
						if (current.getRow() == i && current.getCol() == j) {
							if (current.isKing()) {
								System.out.print(" A" + current.getNum() + " ");
							} else {
								System.out.print(" a" + current.getNum() + " ");
							}
							isAgent = true;
							break;
						}
					}

					boolean isHuman = false;
					if (isAgent == false) {
						for (int k = 0; k < humanPieces.size(); k++) {
							Piece current = humanPieces.get(k);
							if (current.getRow() == i && current.getCol() == j) {
								if (current.isKing()) {
									System.out.print(" H" + current.getNum() + " ");
								} else {
									System.out.print(" h" + current.getNum() + " ");
								}
								isHuman = true;
								break;
							}
						}
					}

					if (isAgent == false && isHuman == false) {
						System.out.print("    ");
					}
				}
			}
			System.out.println();
		}
	}

	public ArrayList<Piece> getHumanPieces() {
		return humanPieces;
	}

	public void setHumanPieces(ArrayList<Piece> humanPieces) {
		this.humanPieces = humanPieces;
	}

	public ArrayList<Piece> getAgentPieces() {
		return agentPieces;
	}

	public void setAgentPieces(ArrayList<Piece> agentPieces) {
		this.agentPieces = agentPieces;
	}

	public boolean isHumanTurn() {
		return isHumanTurn;
	}

	public void setHumanTurn(boolean isHumanTurn) {
		this.isHumanTurn = isHumanTurn;
	}

	public char[][] getPositions() {
		return positions;
	}

	public void setPositions(char[][] positions) {
		this.positions = positions;
	}

	public boolean isWasJump() {
		return wasJump;
	}

	public void setWasJump(boolean wasJump) {
		this.wasJump = wasJump;
	}

}
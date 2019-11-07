/**
 * Class that represents a move on the current board's state
 * 
 * @author mmort
 *
 */
class Move {
	/**
	 * The row of the Move
	 */
	private int row;

	/**
	 * The column of the move
	 */
	private int col;

	/**
	 * The move description to be printed to the user to see the available moves
	 */
	private String moveDesc;

	/**
	 * The piece that would be moved to the Move position
	 */
	private Piece piece;

	public Move(int row, int col, String moveDesc, Piece piece) {
		this.row = row;
		this.col = col;
		this.moveDesc = moveDesc;
		this.piece = piece;
	}

	public Move() {
		// TODO Auto-generated constructor stub
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public String getMoveDesc() {
		return moveDesc;
	}

	public void setMoveDesc(String moveDesc) {
		this.moveDesc = moveDesc;
	}
}
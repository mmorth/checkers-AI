/**
 * Represents a checker piece on checker board
 * 
 * @author mmort
 *
 */
public class Piece {
	/**
	 * The piece number to allow the player to select which piece to move
	 */
	private final char num;

	/**
	 * The row position of the piece
	 */
	private int row;

	/**
	 * The column position of the piece
	 */
	private int col;

	/**
	 * Represents whether this piece is a king
	 */
	private boolean isKing;

	/**
	 * Represents whether this piece belongs to the human player
	 */
	private boolean isHumanPiece;

	public Piece(char num, int row, int col, boolean isHumanPiece) {
		this.num = num;
		this.row = row;
		this.col = col;
		this.isHumanPiece = isHumanPiece;
	}

	public Piece(Piece piece) {
		this.num = piece.getNum();
		this.row = piece.getRow();
		this.col = piece.getCol();
		this.isKing = piece.isKing();
		this.isHumanPiece = piece.isHumanPiece();
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

	public boolean isKing() {
		return isKing;
	}

	public void setKing(boolean isKing) {
		this.isKing = isKing;
	}

	public char getNum() {
		return num;
	}

	public boolean isHumanPiece() {
		return isHumanPiece;
	}

	public void setHumanPiece(boolean isHumanPiece) {
		this.isHumanPiece = isHumanPiece;
	}
}
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class that represents the Checkers game
 * 
 * @author mmort
 *
 */
public class Checkers implements Game<Board, Move, String> {

	/**
	 * Stores the current state of the checkboard game
	 */
	private Board board;

	@Override
	public Board getInitialState() {
		// Set the initial state of the checker board
		board = new Board();
		return board;
	}

	@Override
	public String[] getPlayers() {
		// Return which player's turn it is
		String players[] = { "Human", "Agent" };
		return players;
	}

	@Override
	public String getPlayer(Board state) {
		// Get the current player's turn
		if (board.isHumanTurn()) {
			return "Human";
		} else {
			return "Agent";
		}
	}

	@Override
	public List<Move> getActions(Board state) {
		// Return list of legal moves
		return determineLegalMoves(state);
	}

	@Override
	public Board getResult(Board state, Move action) {
		// Work with a copy of the state of the board
		Board copyState = new Board(state);
		// Determine which player's turn it is
		if (copyState.isHumanTurn()) {
			// Determine what piece needs to be moved and move it
			for (int i = 0; i < copyState.getHumanPieces().size(); i++) {
				Piece currentPiece = copyState.getHumanPieces().get(i);

				if (action.getPiece().getRow() == currentPiece.getRow()
						&& action.getPiece().getCol() == currentPiece.getCol()
						&& action.getPiece().getNum() == currentPiece.getNum()
						&& action.getPiece().isHumanPiece() == currentPiece.isHumanPiece()) {

					// Remove jumped piece (if applicapable)
					if (Math.abs(action.getRow() - currentPiece.getRow()) == 4
							&& Math.abs(action.getCol() - currentPiece.getCol()) == 4) {
						int jumpedRowDiff = action.getRow() - currentPiece.getRow();
						int jumpedColDiff = action.getCol() - currentPiece.getCol();
						int jumpedRowPiece = currentPiece.getRow() + (jumpedRowDiff / 2);
						int jumpedColPiece = currentPiece.getCol() + (jumpedColDiff / 2);

						// Remove jumped piece
						for (int j = 0; j < copyState.getAgentPieces().size(); j++) {
							Piece currentAgentPiece = copyState.getAgentPieces().get(j);

							if (currentAgentPiece.getRow() == jumpedRowPiece
									&& currentAgentPiece.getCol() == jumpedColPiece
									&& (copyState.getPositions()[jumpedRowPiece][jumpedColPiece] == 'a'
											|| copyState.getPositions()[jumpedRowPiece][jumpedColPiece] == 'A')) {
								copyState.getPositions()[currentAgentPiece.getRow()][currentAgentPiece.getCol()] = 'o';
								copyState.getAgentPieces().remove(j);
								copyState.setWasJump(true);
							}
						}
					}

					// Move piece
					copyState.getPositions()[currentPiece.getRow()][currentPiece.getCol()] = 'o';
					currentPiece.setRow(action.getRow());
					currentPiece.setCol(action.getCol());

					// Check for king me positions
					if (currentPiece.getRow() == 1 && currentPiece.isKing() == false) {
						currentPiece.setKing(true);
					}

					// Update piece position
					if (currentPiece.isKing()) {
						copyState.getPositions()[action.getRow()][action.getCol()] = 'H';
					} else {
						copyState.getPositions()[action.getRow()][action.getCol()] = 'h';
					}

					break;
				}
			}
		} else {
			// Determine what piece needs to be moved and move it
			for (int i = 0; i < copyState.getAgentPieces().size(); i++) {
				Piece currentPiece = copyState.getAgentPieces().get(i);

				if (action.getPiece().getRow() == currentPiece.getRow()
						&& action.getPiece().getCol() == currentPiece.getCol()
						&& action.getPiece().getNum() == currentPiece.getNum()
						&& action.getPiece().isHumanPiece() == currentPiece.isHumanPiece()) {

					// Remove jumped piece (if applicapable)
					if (Math.abs(action.getRow() - currentPiece.getRow()) == 4
							&& Math.abs(action.getCol() - currentPiece.getCol()) == 4) {
						int jumpedRowDiff = action.getRow() - currentPiece.getRow();
						int jumpedColDiff = action.getCol() - currentPiece.getCol();
						int jumpedRowPiece = currentPiece.getRow() + (jumpedRowDiff / 2);
						int jumpedColPiece = currentPiece.getCol() + (jumpedColDiff / 2);

						// Remove jumped piece
						for (int j = 0; j < copyState.getHumanPieces().size(); j++) {
							Piece currentHumanPiece = copyState.getHumanPieces().get(j);

							if (currentHumanPiece.getRow() == jumpedRowPiece
									&& currentHumanPiece.getCol() == jumpedColPiece
									&& (copyState.getPositions()[jumpedRowPiece][jumpedColPiece] == 'h'
											|| copyState.getPositions()[jumpedRowPiece][jumpedColPiece] == 'H')) {
								copyState.getPositions()[currentHumanPiece.getRow()][currentHumanPiece.getCol()] = 'o';
								copyState.getHumanPieces().remove(j);
								copyState.setWasJump(true);
							}
						}
					}

					// Move piece
					copyState.getPositions()[currentPiece.getRow()][currentPiece.getCol()] = 'o';
					currentPiece.setRow(action.getRow());
					currentPiece.setCol(action.getCol());

					// Check for king me positions
					if (currentPiece.getRow() == 15 && currentPiece.isKing() == false) {
						currentPiece.setKing(true);
					}

					// Update board positions
					if (currentPiece.isKing()) {
						copyState.getPositions()[action.getRow()][action.getCol()] = 'A';
					} else {
						copyState.getPositions()[action.getRow()][action.getCol()] = 'a';
					}

					break;
				}
			}
		}

		// Change the player's turn if the last move was not a jump
		if (!copyState.isWasJump()) {
			copyState.setHumanTurn(!copyState.isHumanTurn());
		}
		
		// Return the updated state of the board after the move
		return copyState;
	}

	@Override
	public boolean isTerminal(Board state) {
		// Check if action list is empty (someone has won the game)
		int numLegalMoves = determineLegalMoves(state).size();

		if (numLegalMoves == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public double getUtility(Board state, String player) {
		// Implement the evaluationValue function (see lab report for details)
		double evaluationValue = 0.0;

		for (int i = 0; i < state.getAgentPieces().size(); i++) {
			Piece currentPiece = state.getAgentPieces().get(i);

			// Give precidence to states with more agent pieces
			if (currentPiece.isKing()) {
				evaluationValue += 15;
			} else {
				evaluationValue += 10;
			}

			// Give higher evaluationValue for agent pieces that are on the sides or corners of the board
			if ((currentPiece.getRow() == 1 && currentPiece.getCol() == 1)
					|| (currentPiece.getRow() == 1 && currentPiece.getCol() == 15)
					|| (currentPiece.getRow() == 15 && currentPiece.getCol() == 1)
					|| (currentPiece.getRow() == 15 && currentPiece.getCol() == 15)) {
				// Piece is in the corner
				evaluationValue += 4;
			} else if (currentPiece.getRow() == 1 || currentPiece.getRow() == 15 || currentPiece.getCol() == 1
					|| currentPiece.getCol() == 15) {
				// Piece is on a side
				evaluationValue += 2;
			}
			
			// Give more evaluationValue for agent moves and jumped pieces
			// Check moving up-left
			if (currentPiece.getRow() - 2 >= 1 && currentPiece.getCol() - 2 >= 1
					&& state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'o'
					&& currentPiece.isKing()) {
				evaluationValue++;
			} else if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() - 4 >= 1
					&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'h'
							|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'H')
					&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() - 4] == 'o'
					&& currentPiece.isKing()) {
				evaluationValue += 7;
			}

			// Check moving up-right
			if (currentPiece.getRow() - 2 >= 1 && currentPiece.getCol() + 2 <= 15
					&& state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'o'
					&& currentPiece.isKing()) {
				evaluationValue++;
			} else if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() + 4 <= 15
					&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'h'
							|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'H')
					&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() + 4] == 'o'
					&& currentPiece.isKing()) {
				evaluationValue += 7;
			}

			// Check moving down-left
			if (currentPiece.getRow() + 2 <= 15 && currentPiece.getCol() - 2 >= 1
					&& state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'o') {
				evaluationValue++;
			} else if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() - 4 >= 1
					&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'h'
							|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'H')
					&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() - 4] == 'o') {
				evaluationValue += 7;
			}

			// Check moving down-right
			if (currentPiece.getRow() + 2 <= 15 && currentPiece.getCol() + 2 <= 15
					&& state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'o') {
				evaluationValue++;
			} else if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() + 4 <= 15
					&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'h'
							|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'H')
					&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() + 4] == 'o') {
				evaluationValue += 7;
			}
		}

		for (int i = 0; i < state.getHumanPieces().size(); i++) {
			Piece currentPiece = state.getHumanPieces().get(i);
			
			// Remove evaluationValue for more human pieces on the board
			if (currentPiece.isKing()) {
				evaluationValue -= 15;
			} else {
				evaluationValue-=10;
			}

			// Give lower evaluationValue for human pieces that are on the sides or corners of the board
			if ((currentPiece.getRow() == 1 && currentPiece.getCol() == 1)
					|| (currentPiece.getRow() == 1 && currentPiece.getCol() == 15)
					|| (currentPiece.getRow() == 15 && currentPiece.getCol() == 1)
					|| (currentPiece.getRow() == 15 && currentPiece.getCol() == 15)) {
				// Piece is in the corner
				evaluationValue -= 4;
			} else if (currentPiece.getRow() == 1 || currentPiece.getRow() == 15 || currentPiece.getCol() == 1
					|| currentPiece.getCol() == 15) {
				// Piece is on a side
				evaluationValue -= 2;
			}
			
			// Remove evaluationValue for their moves and jumps
			// Check moving up-left
			if (currentPiece.getRow() - 2 >= 1 && currentPiece.getCol() - 2 >= 1
					&& state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'o') {
				evaluationValue--;
			} else if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() - 4 >= 1
					&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'a'
							|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'A')
					&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() - 4] == 'o') {
				evaluationValue-=7;
			}

			// Check moving up-right
			if (currentPiece.getRow() - 2 >= 1 && currentPiece.getCol() + 2 <= 15
					&& state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'o') {
				evaluationValue--;
			} else if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() + 4 <= 15
					&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'a'
							|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'A')
					&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() + 4] == 'o') {
				evaluationValue-=7;
			}

			// Check moving down-left
			if (currentPiece.getRow() + 2 <= 15 && currentPiece.getCol() - 2 >= 1
					&& state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'o'
					&& currentPiece.isKing()) {
				evaluationValue--;
			} else if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() - 4 >= 1
					&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'a'
							|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'A')
					&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() - 4] == 'o'
					&& currentPiece.isKing()) {
				evaluationValue-=7;
			}

			// Check moving down-right
			if (currentPiece.getRow() + 2 <= 15 && currentPiece.getCol() + 2 <= 15
					&& state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'o'
					&& currentPiece.isKing()) {
				evaluationValue--;
			} else if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() + 4 <= 15
					&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'a'
							|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'A')
					&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() + 4] == 'o'
					&& currentPiece.isKing()) {
				evaluationValue-=7;
			}
		}

		// Return the evaluationValue value for the current state of the board and player
		return evaluationValue;
	}

	/**
	 * Returns a list of legal moves that the current player can make
	 * 
	 * @param state The current state of the checker board
	 * @return The list of legal Move objects
	 */
	public ArrayList<Move> determineLegalMoves(Board state) {
		// If the last move was a jump, return only the valid multi-jump moves
		if (state.isWasJump()) {
			state.setWasJump(false);
			return Checkers.legalJumpMoves(state);
		}
		
		// Determine which player's turn it is
		ArrayList<Move> legalMoves = new ArrayList<>();
		if (state.isHumanTurn()) {
			// Loop through each piece to check legal moves
			for (int i = 0; i < state.getHumanPieces().size(); i++) {
				Piece currentPiece = state.getHumanPieces().get(i);

				// Check moving up-left
				if (currentPiece.getRow() - 2 >= 1 && currentPiece.getCol() - 2 >= 1
						&& state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() - 2, currentPiece.getCol() - 2, "UL", currentPiece));
				} else if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() - 4 >= 1
						&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'a'
								|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'A')
						&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() - 4] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() - 4, currentPiece.getCol() - 4, "UL", currentPiece));
				}

				// Check moving up-right
				if (currentPiece.getRow() - 2 >= 1 && currentPiece.getCol() + 2 <= 15
						&& state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() - 2, currentPiece.getCol() + 2, "UR", currentPiece));
				} else if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() + 4 <= 15
						&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'a'
								|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'A')
						&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() + 4] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() - 4, currentPiece.getCol() + 4, "UR", currentPiece));
				}

				// Check moving down-left
				if (currentPiece.getRow() + 2 <= 15 && currentPiece.getCol() - 2 >= 1
						&& state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() + 2, currentPiece.getCol() - 2, "DL", currentPiece));
				} else if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() - 4 >= 1
						&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'a'
								|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'A')
						&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() - 4] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() + 4, currentPiece.getCol() - 4, "DL", currentPiece));
				}

				// Check moving down-right
				if (currentPiece.getRow() + 2 <= 15 && currentPiece.getCol() + 2 <= 15
						&& state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() + 2, currentPiece.getCol() + 2, "DR", currentPiece));
				} else if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() + 4 <= 15
						&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'a'
								|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'A')
						&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() + 4] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() + 4, currentPiece.getCol() + 4, "DR", currentPiece));
				}
			}
		} else {
			// Loop through each piece to check legal moves
			for (int i = 0; i < state.getAgentPieces().size(); i++) {
				Piece currentPiece = state.getAgentPieces().get(i);

				// Check moving up-left
				if (currentPiece.getRow() - 2 >= 1 && currentPiece.getCol() - 2 >= 1
						&& state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() - 2, currentPiece.getCol() - 2, "UL", currentPiece));
				} else if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() - 4 >= 1
						&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'h'
								|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'H')
						&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() - 4] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() - 4, currentPiece.getCol() - 4, "UL", currentPiece));
				}

				// Check moving up-right
				if (currentPiece.getRow() - 2 >= 1 && currentPiece.getCol() + 2 <= 15
						&& state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() - 2, currentPiece.getCol() + 2, "UR", currentPiece));
				} else if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() + 4 <= 15
						&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'h'
								|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'H')
						&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() + 4] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() - 4, currentPiece.getCol() + 4, "UR", currentPiece));
				}

				// Check moving down-left
				if (currentPiece.getRow() + 2 <= 15 && currentPiece.getCol() - 2 >= 1
						&& state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() + 2, currentPiece.getCol() - 2, "DL", currentPiece));
				} else if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() - 4 >= 1
						&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'h'
								|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'H')
						&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() - 4] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() + 4, currentPiece.getCol() - 4, "DL", currentPiece));
				}

				// Check moving down-right
				if (currentPiece.getRow() + 2 <= 15 && currentPiece.getCol() + 2 <= 15
						&& state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() + 2, currentPiece.getCol() + 2, "DR", currentPiece));
				} else if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() + 4 <= 15
						&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'h'
								|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'H')
						&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() + 4] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() + 4, currentPiece.getCol() + 4, "DR", currentPiece));
				}
			}
		}

		// Return list of legal moves
		return legalMoves;
	}
	
	/**
	 * Returns a list of legal jump moves
	 * @param state The current state of the board
	 * @return The legal jump moves
	 */
	public static ArrayList<Move> legalJumpMoves(Board state) {
		// Determine which player's turn it is
		ArrayList<Move> legalMoves = new ArrayList<>();
		if (state.isHumanTurn()) {
			// Loop through each piece to check legal moves
			for (int i = 0; i < state.getHumanPieces().size(); i++) {
				Piece currentPiece = state.getHumanPieces().get(i);

				// Check moving up-left
				if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() - 4 >= 1
						&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'a'
								|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'A')
						&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() - 4] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() - 4, currentPiece.getCol() - 4, "UL", currentPiece));
				}

				// Check moving up-right
				if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() + 4 <= 15
						&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'a'
								|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'A')
						&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() + 4] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() - 4, currentPiece.getCol() + 4, "UR", currentPiece));
				}

				// Check moving down-left
				if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() - 4 >= 1
						&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'a'
								|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'A')
						&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() - 4] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() + 4, currentPiece.getCol() - 4, "DL", currentPiece));
				}

				// Check moving down-right
				if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() + 4 <= 15
						&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'a'
								|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'A')
						&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() + 4] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() + 4, currentPiece.getCol() + 4, "DR", currentPiece));
				}
			}
		} else {
			// Loop through each piece to check legal moves
			for (int i = 0; i < state.getAgentPieces().size(); i++) {
				Piece currentPiece = state.getAgentPieces().get(i);

				// Check moving up-left
				if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() - 4 >= 1
						&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'h'
								|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() - 2] == 'H')
						&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() - 4] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() - 4, currentPiece.getCol() - 4, "UL", currentPiece));
				}

				// Check moving up-right
				if (currentPiece.getRow() - 4 >= 1 && currentPiece.getCol() + 4 <= 15
						&& (state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'h'
								|| state.getPositions()[currentPiece.getRow() - 2][currentPiece.getCol() + 2] == 'H')
						&& state.getPositions()[currentPiece.getRow() - 4][currentPiece.getCol() + 4] == 'o'
						&& currentPiece.isKing()) {
					legalMoves.add(new Move(currentPiece.getRow() - 4, currentPiece.getCol() + 4, "UR", currentPiece));
				}

				// Check moving down-left
				if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() - 4 >= 1
						&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'h'
								|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() - 2] == 'H')
						&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() - 4] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() + 4, currentPiece.getCol() - 4, "DL", currentPiece));
				}

				// Check moving down-right
				if (currentPiece.getRow() + 4 <= 15 && currentPiece.getCol() + 4 <= 15
						&& (state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'h'
								|| state.getPositions()[currentPiece.getRow() + 2][currentPiece.getCol() + 2] == 'H')
						&& state.getPositions()[currentPiece.getRow() + 4][currentPiece.getCol() + 4] == 'o') {
					legalMoves.add(new Move(currentPiece.getRow() + 4, currentPiece.getCol() + 4, "DR", currentPiece));
				}
			}
		}

		// Return list of legal moves
		return legalMoves;
	}

	public static void main(String[] args) {
		// Setup AlphaBetaSearch and game board
		AlphaBetaSearch<Board, Move, String> abSearch = AlphaBetaSearch.createFor(new Checkers());
		Board board = abSearch.game.getInitialState();
		board.drawBoard();

		// Create Scanner to accept user input
		Scanner input = new Scanner(System.in);

		// Continue looping until the end of the game is reached
		while (!abSearch.game.isTerminal(board)) {

			// Determine which player's turn it is
			if (board.isHumanTurn()) {

				// Get legal moves for human
				List<Move> legalMoves = abSearch.game.getActions(board);

				// Print the list of legal moves
				System.out.println("Available Moves: ");
				for (int i = 0; i < legalMoves.size(); i++) {
					System.out.println(legalMoves.get(i).getPiece().getNum() + " - " + legalMoves.get(i).getMoveDesc());
				}

				// Receive desired move from user
				boolean validInput = false;
				Move selectedMove = null;
				while (validInput == false) {
					System.out.print("\nPiece Selection: ");
					String pieceInput = input.nextLine();
					System.out.print("\nPosition Selection: ");
					String positionChoice = input.nextLine();
					
					char pieceChoice = 'z';
					if (pieceInput.length() >= 1) {
						pieceChoice = pieceInput.charAt(0);
					}

					// Check that input by user is a valid move
					for (int i = 0; i < legalMoves.size(); i++) {
						if (legalMoves.get(i).getPiece().getNum() == pieceChoice
								&& legalMoves.get(i).getMoveDesc().equals(positionChoice)) {
							selectedMove = legalMoves.get(i);
							validInput = true;
							break;
						}
					}

					if (validInput == false) {
						System.out.println("\nInvalid Move Given");
					}
				}

				// Update state of board with user's move
				Board result = abSearch.game.getResult(board, selectedMove);
				board = new Board(result);
				
				// Human multi-jump logic
				boolean jumpDetected = false;
				while(board.isWasJump()) {
					jumpDetected = true;
					board.drawBoard();
					
					// Get legal jump moves for human
					legalMoves = abSearch.game.getActions(board);

					// See if there is a valid multi-jump available
					if (legalMoves.size() == 0) {
						board.setWasJump(false);
					} else {
						// Print the list of legal moves
						System.out.println("Multi-Jump Moves: ");
						for (int i = 0; i < legalMoves.size(); i++) {
							System.out.println(legalMoves.get(i).getPiece().getNum() + " - " + legalMoves.get(i).getMoveDesc());
						}
						
						// Get user input on desired move
						validInput = false;
						while (validInput == false) {
							System.out.print("\nPiece Selection: ");
							String pieceInput = input.nextLine();
							System.out.print("\nPosition Selection: ");
							String positionChoice = input.nextLine();
							
							char pieceChoice = 'z';
							if (pieceInput.length() >= 1) {
								pieceChoice = pieceInput.charAt(0);
							}

							// Check that input by user is a valid move
							for (int i = 0; i < legalMoves.size(); i++) {
								if (legalMoves.get(i).getPiece().getNum() == pieceChoice
										&& legalMoves.get(i).getMoveDesc().equals(positionChoice)) {
									selectedMove = legalMoves.get(i);
									validInput = true;
									break;
								}
							}

							if (validInput == false) {
								System.out.println("\nInvalid Move Given");
							} else {
								// Update state of board with user's move
								result = abSearch.game.getResult(board, selectedMove);
								board = new Board(result);
							}
						}
					}
				}
				
				// Change player's turn after multi-jump complete
				if (jumpDetected == true) {
					board.setHumanTurn(!board.isHumanTurn());
				}
			} else {
				// Determine the agent's action
				Move action = abSearch.makeDecision(board);

				// Print agent's move
				System.out.println("Agent's Move: ");
				System.out.println(action.getPiece().getNum() + " - " + action.getMoveDesc());

				// Update board with the agent's move
				Board result = abSearch.game.getResult(board, action);
				board = new Board(result);
				
				while(board.isWasJump()) {
					board.drawBoard();
					
					// Get legal jump moves for human
					List<Move> legalMoves = abSearch.game.getActions(board);

					if (legalMoves.size() == 0) {
						board.setWasJump(false);
					} else {
						// Determine the agent's action
						action = abSearch.makeDecision(board);

						// Print agent's move
						System.out.println("Agent's Move: ");
						System.out.println(action.getPiece().getNum() + " - " + action.getMoveDesc());

						// Update board with the agent's move
						result = abSearch.game.getResult(board, action);
						board = new Board(result);
					}
					
					// Change player's turn after multi-jump complete
					if (!board.isWasJump()) {
						board.setHumanTurn(!board.isHumanTurn());
					}
				}
			}

			// Draw updated board and switch to the other player's turn
			board.drawBoard();
		}
		
		// Close the scanner
		input.close();

		// Print game over screen
		System.out.println("GAME OVER: ");
		if (board.isHumanTurn()) {
			System.out.println("Agent Wins!");
		} else {
			System.out.println("Human Wins!");
		}
	}
}

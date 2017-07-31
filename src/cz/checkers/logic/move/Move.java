package cz.checkers.logic.move;

import java.io.Serializable;

import cz.checkers.gui.panels.InfoPanel;
import cz.checkers.logic.board.Board;
import cz.checkers.logic.piece.Piece;

public abstract class Move implements Serializable {

	private static final long serialVersionUID = -7630653290341808666L;
	final Board board;
	Piece movedPiece;
	final int destinationCoordinate;

	public Move(Board board, Piece piece, int destinationCoordinate) {
		this.board = board;
		this.movedPiece = piece;
		this.destinationCoordinate = destinationCoordinate;
	}

	public int getCurrentCoordinate() {
		return this.getMovedPiece().getPiecePosition();
	}

	public int getDestinationCoordinate() {
		return destinationCoordinate;
	}

	public Piece getMovedPiece() {
		return this.movedPiece;
	}

	public Piece getAttackedPiece() {
		return null;
	}

	public abstract Board execute();

	public abstract boolean isAttackMove();

	public static class MoveFactory {

		public MoveFactory() {
			throw new RuntimeException("Can not initialize MoveFactory class");
		}

		public static Move createMove(Board board, int currentCoordinate, int destinationCoordinate) {
			for (Move move : board.getCurrentPlayer().getLegalMoves()) {
				if (move.getCurrentCoordinate() == currentCoordinate
						&& move.getDestinationCoordinate() == destinationCoordinate) {
					return move;
				}
			}
			if (currentCoordinate != destinationCoordinate) {
				InfoPanel.setText("Illegal Move");
			}
			return null;
		}
	}

}

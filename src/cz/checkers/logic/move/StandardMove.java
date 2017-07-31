package cz.checkers.logic.move;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.board.Board.Builder;
import cz.checkers.logic.piece.Piece;
import cz.checkers.logic.utils.BoardUtils;

@SuppressWarnings("serial")
public class StandardMove extends Move {
	public StandardMove(final Board board, final Piece movedPiece, final int destinantionCoordinate) {
		super(board, movedPiece, destinantionCoordinate);
	}

	public Board execute() {
		final Builder builder = new Builder();
		// sets non active pieces
		for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {

			if (!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		// sets non active opponents pieces
		for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
		}

		// move the moved piece
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

		Board newBoard = builder.build();
		newBoard.setLastMove(this);
		return newBoard;
	}

	@Override
	public String toString() {

		return String.format("%s    %s  to %s", board.getCurrentPlayer().toString(),
				BoardUtils.getPositionAtCoordinate(movedPiece.getPiecePosition()),
				BoardUtils.getPositionAtCoordinate(this.destinationCoordinate));
	}

	@Override
	public boolean isAttackMove() {
		// TODO Auto-generated method stub
		return false;
	}

}

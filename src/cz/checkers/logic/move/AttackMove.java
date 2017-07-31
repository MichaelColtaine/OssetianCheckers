package cz.checkers.logic.move;

import java.util.ArrayList;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.board.Board.Builder;
import cz.checkers.logic.piece.Piece;
import cz.checkers.logic.utils.BoardUtils;

@SuppressWarnings("serial")
public class AttackMove extends Move {
	private Piece attackedPiece;
	private boolean canStillJump = false;

	public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
		super(board, movedPiece, destinationCoordinate);
		this.attackedPiece = attackedPiece;
	}

	@Override
	public Piece getAttackedPiece() {
		return this.attackedPiece;
	}

	@Override
	public Board execute() {
		Board.Builder builder = new Builder();
		for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
			if (!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
			if (!piece.equals(this.getAttackedPiece())) {
				builder.setPiece(piece);
			}
		}
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.getCurrentPlayer().getAlliance());

		Board newBoard = builder.build();

		this.canStillJump = newBoard.getTile(destinationCoordinate).getPiece().getAttackMoves().size() > 0;

		if (!canStillJump) {
			newBoard.getTile(destinationCoordinate).getPiece().setIsActive(false);
			builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
		}

		if (canStillJump && isAttackMove()) {
			newBoard.setIsJumpInProgress(true);
			newBoard.getTile(destinationCoordinate).getPiece().setIsActive(true);

		} else {
			newBoard.setIsJumpInProgress(false);
			if (!newBoard.getCurrentPlayer().getOpponent().getLegalMoves().isEmpty()) {
				newBoard.setCurrentPlayer(newBoard.getCurrentPlayer().getOpponent().getAlliance());
			} else {
				newBoard.setCurrentPlayer(newBoard.getCurrentPlayer().getAlliance());
			}
		}

		newBoard.setLastMove(this);

		boolean activePiece = false;
		for (Piece p : newBoard.getAllPieces()) {
			if (p.isActive()) {
				activePiece = true;
			}
		}
		/// pokud se jedna dvojskok, tak se vymazou vsechny pohyby figurek
		/// jinych nez te jedne co je aktivni
		ArrayList<Move> moves = new ArrayList<>();
		if (activePiece) {
			for (Move m : newBoard.getCurrentPlayer().getLegalMoves()) {
				if (m.getMovedPiece().isActive()) {
					moves.add(m);
				}
			}
			newBoard.getCurrentPlayer().setLegalMoves(moves);
		}

		return newBoard;
	}

	@Override
	public String toString() {
		return String.format("%s[A]%s to %s", board.getCurrentPlayer().toString(),
				BoardUtils.getPositionAtCoordinate(movedPiece.getPiecePosition()),
				BoardUtils.getPositionAtCoordinate(this.destinationCoordinate));
	}

	@Override
	public boolean isAttackMove() {
		return true;
	}

}

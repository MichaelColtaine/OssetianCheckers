package cz.checkers.logic.piece;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.enums.Alliance;
import cz.checkers.logic.move.Move;

public abstract class Piece implements Serializable {

	private static final long serialVersionUID = -8781441556919284087L;

	protected final int piecePosition;

	private boolean isActive;
	private boolean canAttack;
	private boolean canMove;
	protected List<Move> legalStandardMoves;
	protected List<Move> legalAttackMoves;

	public Piece(final int piecePosition) {
		this.piecePosition = piecePosition;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public boolean canAttack() {
		return canAttack;
	}

	public boolean canMove() {
		return canMove;
	}

	public void setCanMove(boolean b) {
		this.canMove = b;
	}

	public void setCanAttack(boolean b) {
		this.canAttack = b;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getPiecePosition() {
		return this.piecePosition;
	}

	public List<Move> getAttackMoves() {
		if (this.legalAttackMoves == null) {
			return Collections.emptyList();
		}
		return this.legalAttackMoves;
	}

	public List<Move> getStandardMoves() {
		if (this.legalStandardMoves == null) {
			return Collections.emptyList();
		}
		return this.legalStandardMoves;
	}
	

	public abstract void calculateLegalAttackMoves(Board board);

	public abstract void calculateLegalStandardMoves(Board board);

	public abstract Piece movePiece(Move move);

	public abstract Alliance getPieceAlliance();

}

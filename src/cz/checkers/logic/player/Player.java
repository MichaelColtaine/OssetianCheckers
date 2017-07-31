package cz.checkers.logic.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.enums.Alliance;
import cz.checkers.logic.enums.MoveStatus;

import cz.checkers.logic.move.Move;
import cz.checkers.logic.move.MoveTransition;
import cz.checkers.logic.piece.Piece;

public abstract class Player implements Serializable {

	final private static long serialVersionUID = 7951326599377897939L;
	protected Board board;
	protected Collection<Move> legalMoves;

	Player(Board board, Collection<Piece> pieces) {
		this.board = board;
		legalMoves = calculateLegalMoves(board, pieces);
	}

	public Collection<Move> getLegalMoves() {
		return this.legalMoves;
	}

	public void setLegalMoves(List<Move> moves) {
		this.legalMoves = moves;
	}

	public MoveTransition makeMove(Move move) {
		if (!legalMoves.contains(move)) {
			return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		Board transitionBoard = move.execute();
		return new MoveTransition(this.board, transitionBoard, move, MoveStatus.DONE);
	}

	public boolean hasLost() {
		if (!hasPieces()) {
			return true;
		}
		return false;
	}

	public List<Move> calculateLegalMoves(Board board, Collection<Piece> pieces) {
		List<Move> allAttackMoves = new ArrayList<Move>();
		List<Move> allStandardMoves = new ArrayList<Move>();
		List<Move> onePieceCanAttack = new ArrayList<Move>();
		boolean onlyAttack = false;

		for (Piece p : pieces) {
			p.setCanAttack(false);
			p.setCanMove(false);
			if (p.isActive()) {
				p.calculateLegalAttackMoves(board);
				onePieceCanAttack.addAll(p.getAttackMoves());
				return onePieceCanAttack;
			}
			p.calculateLegalAttackMoves(board);
			if (p.getAttackMoves().isEmpty() && !onlyAttack) {
				p.calculateLegalStandardMoves(board);
				if (!p.getStandardMoves().isEmpty()) {
					p.setCanMove(true);
					allStandardMoves.addAll(p.getStandardMoves());
				}
				continue;
			}
			allAttackMoves.addAll(p.getAttackMoves());
			onlyAttack = true;
		}
		if (allAttackMoves.isEmpty()) {
			return allStandardMoves;
		}
		for (Piece p : pieces) {
			if (!p.getAttackMoves().isEmpty()) {
				p.setCanAttack(true);
			}
			p.setCanMove(false);
			if (p.getStandardMoves() != null) {
				p.getStandardMoves().clear();
			}
		}

		return allAttackMoves;

	}

	public abstract Collection<Piece> getActivePieces();

	public abstract boolean hasPieces();

	public abstract Alliance getAlliance();

	public abstract Player getOpponent();

}

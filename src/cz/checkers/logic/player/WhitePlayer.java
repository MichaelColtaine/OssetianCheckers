package cz.checkers.logic.player;

import java.util.Collection;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.enums.Alliance;
import cz.checkers.logic.piece.Piece;

@SuppressWarnings("serial")
public class WhitePlayer extends Player {

	public WhitePlayer(Board board, Collection<Piece> whitePieces) {
		super(board, whitePieces);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.getBlackPlayer();
	}

	@Override
	public boolean hasPieces() {
		return !board.getWhitePieces().isEmpty();
	}

	@Override
	public String toString() {
		return String.format("White: ");
	}

}

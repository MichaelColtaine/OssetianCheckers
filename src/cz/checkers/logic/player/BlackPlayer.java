package cz.checkers.logic.player;

import java.util.Collection;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.enums.Alliance;
import cz.checkers.logic.piece.Piece;

@SuppressWarnings("serial")
public class BlackPlayer extends Player {

	public BlackPlayer(Board board, Collection<Piece> blackPieces) {
		super(board, blackPieces);

	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() {
		return board.getWhitePlayer();
	}

	@Override
	public boolean hasPieces() {
		return this.board.getBlackPieces().size() > 0;
	}

	@Override
	public String toString() {
		return String.format("Black: ");
	}
}

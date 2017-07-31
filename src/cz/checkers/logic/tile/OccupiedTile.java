package cz.checkers.logic.tile;

import cz.checkers.logic.enums.Alliance;
import cz.checkers.logic.piece.Piece;

@SuppressWarnings("serial")
public  class OccupiedTile extends Tile {
	private final Piece pieceOnTile;

	public OccupiedTile(final int coordinate, final Piece piece) {
		super(coordinate);
		this.pieceOnTile = piece;
	}

	@Override
	public boolean isTileOccupied() {
		return true;
	}

	@Override
	public Piece getPiece() {
		return pieceOnTile;
	}

	@Override
	public String toString() {
		return getPiece().getPieceAlliance() == Alliance.BLACK ? getPiece().toString().toLowerCase()
				: getPiece().toString();
	}
	
	

}

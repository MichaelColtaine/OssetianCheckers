package cz.checkers.logic.tile;

import cz.checkers.logic.piece.Piece;

@SuppressWarnings("serial")
public class EmptyTile extends Tile {
	
	
	public EmptyTile(final int coordinate) {
		super(coordinate);
	}

	@Override
	public boolean isTileOccupied() {
		return false;
	}

	@Override
	public Piece getPiece() {
		return null;
	}

	@Override
	public String toString() {
		return "-";
	}
}

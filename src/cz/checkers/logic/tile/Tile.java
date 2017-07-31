package cz.checkers.logic.tile;

import java.io.Serializable;

import cz.checkers.logic.piece.Piece;

public abstract class Tile implements Serializable {

	private static final long serialVersionUID = -2245909101154578096L;

	protected final int tileCoordinate;

	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : new EmptyTile(tileCoordinate);
	}

	protected Tile(final int tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}

	public int getTileCoordinate() {
		return this.tileCoordinate;
	}

	public abstract boolean isTileOccupied();

	public abstract Piece getPiece();

}

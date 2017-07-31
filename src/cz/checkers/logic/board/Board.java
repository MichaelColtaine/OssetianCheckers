package cz.checkers.logic.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.checkers.logic.enums.Alliance;
import cz.checkers.logic.move.Move;
import cz.checkers.logic.piece.BlackPiece;
import cz.checkers.logic.piece.Piece;
import cz.checkers.logic.piece.WhitePiece;
import cz.checkers.logic.player.BlackPlayer;
import cz.checkers.logic.player.Player;
import cz.checkers.logic.player.WhitePlayer;
import cz.checkers.logic.tile.Tile;
import cz.checkers.logic.utils.BoardUtils;

public class Board implements Serializable, Cloneable {

	private final static long serialVersionUID = -5755203128677494906L;

	private List<Tile> gameBoard;
	private Collection<Piece> whitePieces;
	private Collection<Piece> blackieces;
	private Collection<Piece> allPieces;
	private WhitePlayer whitePlayer;
	private BlackPlayer blackPlayer;
	private Player currentPlayer;
	private boolean isJumpInProgress;
	private Move lastMove;

	private Board(Builder builder) {
		this.gameBoard = createGameBoard(builder);
		this.allPieces = calculateActivePieces();
		this.whitePlayer = new WhitePlayer(this, whitePieces);
		this.blackPlayer = new BlackPlayer(this, blackieces);
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
	}

	public Move getLastMove() {
		return this.lastMove;
	}

	public void setLastMove(Move move) {
		this.lastMove = move;
	}

	public Collection<Piece> getAllPieces() {
		return allPieces;
	}

	public void setCurrentPlayer(Alliance alliance) {
		if (alliance == Alliance.WHITE) {
			currentPlayer = whitePlayer;
		} else {
			currentPlayer = blackPlayer;
		}
	}

	public Collection<Piece> calculateActivePieces() {
		Collection<Piece> activePieces = new ArrayList<>();
		this.blackieces = new ArrayList<>();
		this.whitePieces = new ArrayList<>();
		for (Tile tile : gameBoard) {
			if (tile.isTileOccupied()) {
				Piece piece = tile.getPiece();
				activePieces.add(piece);
				if (piece.getPieceAlliance().isBlack()) {
					this.blackieces.add(piece);
				} else {
					this.whitePieces.add(piece);
				}
			}
		}
		return activePieces;
	}
	


	public boolean somePieceCanAttack() {
		for (Piece p : currentPlayer.getActivePieces()) {
			if (p.canAttack()) {
				return true;
			}
		}
		return false;
	}

	public void setIsJumpInProgress(boolean isJumpInProgress) {
		this.isJumpInProgress = isJumpInProgress;
	}

	public boolean isJumpInProgress() {
		return isJumpInProgress;
	}

	public boolean isGameOver() {
		if (whitePlayer.hasLost() || blackPlayer.hasLost()) {
			return true;
		} else if (whitePlayer.getLegalMoves().isEmpty() && blackPlayer.getLegalMoves().isEmpty()) {
			return true;
		}
		return false;
	}

	public static Board createStandardBoard() {
		Builder builder = new Builder();
		populateBoardWithBlackPieces(builder);
		populateBoardWithWhitePieces(builder);
		builder.setMoveMaker(Alliance.WHITE);
		return builder.build();
	}

	public static void populateBoardWithBlackPieces(Builder builder) {
		for (int i = 0; i <= 20; i++) {
			builder.setPiece(new BlackPiece(i));
		}
	}

	public static void populateBoardWithWhitePieces(Builder builder) {
		for (int i = 28; i <= 48; i++) {
			builder.setPiece(new WhitePiece(i));
		}
	}

	private static List<Tile> createGameBoard(Builder builder) {
		List<Tile> tiles = new ArrayList<Tile>();
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			tiles.add(Tile.createTile(i, builder.boardConfig.get(i)));
		}
		return tiles;
	}

	public Tile getTile(int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	public Player getWhitePlayer() {
		return this.whitePlayer;
	}

	public Player getBlackPlayer() {
		return this.blackPlayer;
	}

	public Collection<Piece> getBlackPieces() {
		return this.blackieces;
	}

	public Collection<Piece> getWhitePieces() {
		return this.whitePieces;
	}

	public static class Builder {

		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;

		public Builder() {
			this.boardConfig = new HashMap<>();
		}

		public Builder setPiece(Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}

		public Builder setMoveMaker(Alliance nextMoveMaker) {
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}

		public Board build() {
			return new Board(this);
		}
	}

}

package cz.checkers.logic.piece;

import java.util.ArrayList;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.enums.Alliance;
import cz.checkers.logic.move.AttackMove;
import cz.checkers.logic.move.Move;
import cz.checkers.logic.move.StandardMove;
import cz.checkers.logic.tile.Tile;
import cz.checkers.logic.utils.BoardUtils;

@SuppressWarnings("serial")
public class BlackPiece extends Piece {

	private final int[] SINGLE_MOVE_OFFSETS = { 6, 7, 8 };
	private final int[] ATTACK_MOVE_OFFSETS = { 2, 12, 14, 16, -2, -12, -14, -16 };

	public BlackPiece(int piecePosition) {
		super(piecePosition);

	}

	@Override
	public Alliance getPieceAlliance() {
		return Alliance.BLACK;
	}

	@Override
	public Piece movePiece(Move move) {
		return new BlackPiece(move.getDestinationCoordinate());
	}

	@Override
	public void calculateLegalAttackMoves(Board board) {
		legalAttackMoves = new ArrayList<>();
		for (int offset : ATTACK_MOVE_OFFSETS) {
			Tile targetTile;
			int targetId = this.piecePosition + offset;
			try {
				targetTile = board.getTile(targetId);
			} catch (Exception e) {
				continue;
			}

			if (BoardUtils.isValidTileCoordinate(targetId)) {
				Piece pieceInBetween = board.getTile(targetId - (offset / 2)).getPiece();
				if (pieceInBetween != null) {
					if (BoardUtils.FIRST_COLUMN[piecePosition] && (offset == 12 || offset == -2 || offset == -16)) {
						continue;
					}
					if (BoardUtils.SECOND_COLUMN[piecePosition] && (offset == 12 || offset == -2 || offset == -16)) {
						continue;
					}
					if (BoardUtils.SIXTH_COLUMN[piecePosition] && (offset == 2 || offset == 16 || offset == -12)) {
						continue;
					}
					if (BoardUtils.SEVENTH_COLUMN[piecePosition] && (offset == 2 || offset == 16 || offset == -12)) {
						continue;
					}
					if ((offset == -16 || offset == -14 || offset == -12 || offset == -2 || offset == 16 || offset == 14
							|| offset == 12 || offset == 2) && !targetTile.isTileOccupied()
							&& pieceInBetween.getPieceAlliance().isWhite()) {
						AttackMove attackMove = new AttackMove(board, this, targetId, pieceInBetween);
						legalAttackMoves.add(attackMove);
					}
				}
			}
		}
	}

	@Override
	public void calculateLegalStandardMoves(Board board) {
		legalStandardMoves = new ArrayList<>();
		for (int offset : SINGLE_MOVE_OFFSETS) {
			Tile targetTile;
			int targetId = this.piecePosition + offset;
			try {
				targetTile = board.getTile(targetId);
			} catch (Exception e) {
				continue;
			}

			if (BoardUtils.isValidTileCoordinate(targetId)) {
				if (BoardUtils.FIRST_COLUMN[this.piecePosition] && offset == 6) {
					continue;
				}
				if (BoardUtils.SEVENTH_COLUMN[this.piecePosition] && offset == 8) {
					continue;
				}
				if ((offset == 8 || offset == 7 || offset == 6) && !targetTile.isTileOccupied()) {
					legalStandardMoves.add(new StandardMove(board, this, targetId));
				}
			}
		}
	}

}

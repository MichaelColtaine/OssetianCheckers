package cz.checkers.logic.player.ai;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.move.Move;
import cz.checkers.logic.move.MoveTransition;
import cz.checkers.logic.player.Player;

public class AlphaBeta {

	private int PIECE_VALUE = 7, ATTACK_VALUE = 5, MOVE_VALUE = 1, CHAIN_VALUE = 10;

	public AlphaBeta() {

	}
	
	

	public Move calculateBestMove(Board board, int depth) {
		Move bestMove = null;
		int highest = Integer.MIN_VALUE, lowest = Integer.MAX_VALUE, currentValue = 0;
		for (Move m : board.getCurrentPlayer().getLegalMoves()) {
			MoveTransition moveTransition = board.getCurrentPlayer().makeMove(m);
			if (board.getCurrentPlayer().getAlliance().isWhite()) {
				currentValue = min(moveTransition.getToBoard(), depth - 1, highest, lowest);
			} else if (board.getCurrentPlayer().getAlliance().isBlack()) {
				currentValue = max(moveTransition.getToBoard(), depth - 1, highest, lowest);
			}
			
	
			if (board.getCurrentPlayer().getAlliance().isWhite() && currentValue >= highest) {
				highest = currentValue;
				bestMove = m;
				
				
			} else if (board.getCurrentPlayer().getAlliance().isBlack() && currentValue <= lowest) {
				lowest = currentValue;
				bestMove = m;

				
			}
			if (bestMove == null) {
				bestMove = m;
				
			}
		}
		
	
		return bestMove;
	}

	private int max(Board board, int depth, int highest, int lowest) {
		if (depth == 0 || board.isGameOver()) {
			return evaluate(board);
		}
		int currentHighest = highest;
		for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
			MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			currentHighest = Math.max(currentHighest,
					min(moveTransition.getToBoard(), depth - 1, currentHighest, lowest));
			if (currentHighest >= lowest) {
				return lowest;
			}
		}
		return currentHighest;
	}

	private int min(Board board, int depth, int highest, int lowest) {
		if (depth == 0 || board.isGameOver()) {
			return evaluate(board);
		}

		int currentLowest = lowest;
		for (Move move : board.getCurrentPlayer().getLegalMoves()) {
			MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			currentLowest = Math.min(currentLowest,
					max(moveTransition.getToBoard(), depth - 1, highest, currentLowest));
			if (currentLowest <= highest) {
				return highest;
			}
		}
		return currentLowest;
	}

	private int evaluate(Board board) {
		return scorePlayer(board, board.getWhitePlayer()) - scorePlayer(board, board.getBlackPlayer());
	}

	private int scorePlayer(Board board, Player player) {
		return pieceValue(player) + jumpChain(board) + mobility(player);
	}

	private int pieceValue(Player player) {
		return player.getActivePieces().size() * PIECE_VALUE;
	}

	private int jumpChain(Board board) {
		int value = 0;
		if (board.isJumpInProgress()) {
			value += CHAIN_VALUE;
		}
		return value;
	}

	private int mobility(Player player) {
		int value = 0;
		for (Move m : player.getLegalMoves()) {
			if (m.isAttackMove()) {
				value += ATTACK_VALUE;
			} else {
				value += MOVE_VALUE;
			}
		}
		return value;
	}

}

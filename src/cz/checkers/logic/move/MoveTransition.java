package cz.checkers.logic.move;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.enums.MoveStatus;

public class MoveTransition {

	private Board fromBoard;
	private Board toBoard;
	@SuppressWarnings("unused")
	private Move move;
	private MoveStatus moveStatus;

	public MoveTransition(Board fromBoard, Board toBoard, Move move, MoveStatus moveStatus) {
		this.fromBoard = fromBoard;
		this.toBoard = toBoard;
		this.move = move;
		this.moveStatus = moveStatus;
	}

	public Board getToBoard() {
		return this.toBoard;
	}

	public Board getFromBoard() {
		return this.fromBoard;
	}

	public MoveStatus getMoveStatus() {
		return this.moveStatus;
	}
}

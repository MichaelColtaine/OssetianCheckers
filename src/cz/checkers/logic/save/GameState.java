package cz.checkers.logic.save;

import java.io.Serializable;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.moveLog.MoveLog;

public class GameState implements Serializable {

	private static final long serialVersionUID = 5429234213648333756L;
	private Board board;
	private MoveLog moveLog;

	public Board getBoard() {
		return board;
	}

	public MoveLog getMoveLog() {
		return moveLog;
	}

	public GameState(Board board, MoveLog moveLog) {
		this.board = board;
		this.moveLog = moveLog;
	}

}

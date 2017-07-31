package cz.checkers.logic.moveLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cz.checkers.gui.MainGUI;
import cz.checkers.logic.move.Move;

public class MoveLog extends Observable implements Serializable {

	private static final long serialVersionUID = 7765603949810485674L;
	private final List<Move> moves;
	private final List<Move> forwardMoves;

	private int selectedIndex = Integer.MAX_VALUE;

	public MoveLog() {
		this.moves = new ArrayList<>();
		this.forwardMoves = new ArrayList<>();

	}

	public List<Move> getMoves() {
		return this.moves;
	}

	public void addMove(final Move move) {
		this.moves.add(move);
		this.forwardMoves.clear();
		if (selectedIndex == 0) {
			moves.clear();
			forwardMoves.clear();
			selectedIndex = Integer.MAX_VALUE;
			moves.add(move);
		} else if (selectedIndex < moves.size()) {
			this.moves.subList(selectedIndex, moves.size() - 1).clear();
			this.forwardMoves.clear();
			selectedIndex = Integer.MAX_VALUE;
		}
		MainGUI.getInstance().getHistoryPanel().redo(MainGUI.getInstance().getBoard(), this);
	}

	public int size() {
		return this.moves.size();
	}

	public void clear() {
		this.moves.clear();
	}

	public Move getForwardMove() {
		if (!forwardMoves.isEmpty()) {
			Move returnMove = forwardMoves.get(forwardMoves.size() - 1);
			forwardMoves.remove(forwardMoves.size() - 1);
			moves.add(returnMove);
			return returnMove;
		}
		return null;
	}

	public boolean isEmpty() {
		return this.moves.isEmpty();
	}

	public Move getMoveAtPosition(int pos) {
		if (!moves.isEmpty()) {
			Move returnMove = moves.get(pos - 1);
			return returnMove;
		}
		return null;
	}

	public List<Move> getForwardMoves() {
		return this.forwardMoves;
	}

	public Move removeMove(int index) {
		this.forwardMoves.add(moves.get(index));
		return this.moves.remove(index);
	}

	public void setSelectedIndex(int index) {
		this.selectedIndex = index;

	}

	public int getSelectedIndex() {
		return this.selectedIndex;
	}

}

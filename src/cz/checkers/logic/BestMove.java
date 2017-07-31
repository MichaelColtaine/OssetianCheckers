package cz.checkers.logic;

import java.awt.Cursor;

import javax.swing.SwingWorker;

import cz.checkers.gui.MainGUI;
import cz.checkers.gui.MainGUI.BoardPanelGUI;
import cz.checkers.logic.move.Move;
import cz.checkers.logic.player.ai.AlphaBeta;

public class BestMove extends SwingWorker<Move, String> {

	BoardPanelGUI frame = MainGUI.getInstance().getBoardPanel();

	public BestMove() {
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	@Override
	protected Move doInBackground() throws Exception {
		AlphaBeta min = new AlphaBeta();
		Move bestMove = min.calculateBestMove(MainGUI.getInstance().getBoard(), 3);
		return bestMove;
	}

	@Override
	public void done() {
		frame.setCursor(Cursor.getDefaultCursor());
	}
}

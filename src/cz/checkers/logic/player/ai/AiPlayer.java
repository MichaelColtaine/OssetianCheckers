package cz.checkers.logic.player.ai;

import java.awt.Cursor;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import cz.checkers.gui.MainGUI;
import cz.checkers.gui.MainGUI.BoardPanelGUI;
import cz.checkers.gui.panels.HistoryPanel;
import cz.checkers.gui.panels.InfoPanel;
import cz.checkers.logic.move.Move;
import cz.checkers.logic.utils.BoardUtils;

public class AiPlayer extends SwingWorker<Move, String> {

	private int difficulty;

	private BoardPanelGUI boardPanel = MainGUI.getInstance().getBoardPanel();
	private HistoryPanel historyPanel = MainGUI.getInstance().getHistoryPanel();

	public AiPlayer() {
		boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		historyPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	public void setDifficulty(int value) {
		this.difficulty = value;
	}

	@Override
	protected Move doInBackground() throws Exception {
		if (!MainGUI.getInstance().getBoard().getCurrentPlayer().getLegalMoves().isEmpty()) {

			InfoPanel.setPermaText("Computer player is thinking.");
			boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			historyPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			pause();

			AlphaBeta miniMax = new AlphaBeta();
			Move bestMove = miniMax.calculateBestMove(MainGUI.getInstance().getBoard(), difficulty);
			return bestMove;

		}
		return null;
	}

	@Override
	public void done() {
		try {
			if (get() != null) {
				boardPanel.setCursor(Cursor.getDefaultCursor());
				historyPanel.setCursor(Cursor.getDefaultCursor());
				Move bestMove = get();
				InfoPanel.setPermaText("");
				MainGUI.getInstance().updateBoard(
						MainGUI.getInstance().getBoard().getCurrentPlayer().makeMove(bestMove).getToBoard());
				MainGUI.getInstance().getMoveLog().addMove(bestMove);
				MainGUI.getInstance().getBoardPanel().drawBoard();
				MainGUI.getInstance().moveMadeUpdate();
				InfoPanel.updateInfoPanel();
				if (MainGUI.getInstance().getBoard().isGameOver()) {
					BoardUtils.endGame();
				}
			} else {
				BoardUtils.endGame();
			}
		} catch (InterruptedException | ExecutionException | CancellationException e) {

		} finally {

			boardPanel.setCursor(Cursor.getDefaultCursor());
			historyPanel.setCursor(Cursor.getDefaultCursor());
			MainGUI.getInstance().getBoardPanel().drawBoard();
		}
	}

	public void pause() {
		try {
			boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			historyPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			Thread.sleep(450);
			boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			historyPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} catch (InterruptedException e) {
			boardPanel.setCursor(Cursor.getDefaultCursor());
			historyPanel.setCursor(Cursor.getDefaultCursor());
		}
	}
}

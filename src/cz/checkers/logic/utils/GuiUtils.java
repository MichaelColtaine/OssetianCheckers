package cz.checkers.logic.utils;

import javax.swing.JOptionPane;

import cz.checkers.gui.MainGUI;
import cz.checkers.gui.panels.InfoPanel;
import cz.checkers.logic.board.Board;

import cz.checkers.logic.move.Move;

public class GuiUtils {

	private GuiUtils() {
		throw new RuntimeException("This class can not be instantiated!");
	}

	public static void selectedMove(int index) {
		if (index == 0) {
			MainGUI.getInstance().getMoveLog().setSelectedIndex(index);
			MainGUI.getInstance().getBoard();
			MainGUI.getInstance().updateBoard(Board.createStandardBoard());
			MainGUI.getInstance().getBoardPanel().drawBoard();
			InfoPanel.updateInfoPanel();
		} else if (index != -1) {
			MainGUI.getInstance().getMoveLog().setSelectedIndex(index);
			Move move = MainGUI.getInstance().getMoveLog().getMoveAtPosition(index);
			MainGUI.getInstance().updateBoard(move.execute());
			MainGUI.getInstance().getBoardPanel().drawBoard();
			InfoPanel.updateInfoPanel();
		}
	}

	public static void restartGame() {
		MainGUI.getInstance().getMoveLog().clear();
		Board board = Board.createStandardBoard();
		MainGUI.getInstance().updateBoard(board);
		MainGUI.getInstance().getBoardPanel().drawBoard();
		MainGUI.getInstance().getHistoryPanel().clearHistory();
		InfoPanel.updateInfoPanel();

	}
	
	
	public static void exitPrompt() {
		String[] options = { "Yes", "No", "Save Game" };
		int result = JOptionPane.showOptionDialog(MainGUI.getInstance().getGameFrame(), "Are you sure you want to leave?", "Ossetian Checkers",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		switch (result) {
		case 0:
			MainGUI.getInstance().getGameFrame().dispose();
			System.exit(0);
			break;

		case 1:
			break;
		case JOptionPane.CLOSED_OPTION:

			break;
		case 2:
			MainGUI.getInstance().getBarMenu().saveGame();
		}
	}

	

}

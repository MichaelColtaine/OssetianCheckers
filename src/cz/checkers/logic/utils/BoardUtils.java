package cz.checkers.logic.utils;

import java.awt.Color;
import java.util.concurrent.ExecutionException;

import cz.checkers.gui.MainGUI;
import cz.checkers.gui.panels.InfoPanel;
import cz.checkers.logic.BestMove;
import cz.checkers.logic.board.Board.Builder;
import cz.checkers.logic.move.Move;
import cz.checkers.logic.piece.Piece;
import cz.checkers.logic.player.Player;

public class BoardUtils {

	public static final boolean[] FIRST_COLUMN = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1);
	public static final boolean[] THIRD_COLUMN = initColumn(2);
	public static final boolean[] FOURTH_COLUMN = initColumn(3);
	public static final boolean[] FIFTH_COLUMN = initColumn(4);
	public static final boolean[] SIXTH_COLUMN = initColumn(5);
	public static final boolean[] SEVENTH_COLUMN = initColumn(6);

	public static final String[] ALGEBREIC_NOTATION = initializeAlgebraicNotation();

	public static final int NUM_TILES = 49;
	public static final int NUM_TILES_PER_ROW = 7;

	private BoardUtils() {
		throw new RuntimeException("This class can not be instantiated!");
	}

	public static String getCurrentPlayer() {
		try {
			Player currentPlayer = MainGUI.getInstance().getBoard().getCurrentPlayer();
			return String.format("%s", currentPlayer.toString());
		} catch (Exception e) {

		}
		return "None";
	}

	public static boolean isValidTileCoordinate(int coordinate) {
		return coordinate >= 0 && coordinate < NUM_TILES;
	}

	private static boolean[] initColumn(int columnNumber) {
		final boolean[] column = new boolean[NUM_TILES];
		do {
			column[columnNumber] = true;
			columnNumber += NUM_TILES_PER_ROW;
		} while (columnNumber < NUM_TILES);
		return column;
	}

	public static String getPositionAtCoordinate(int destinationCoordinate) {
		return ALGEBREIC_NOTATION[destinationCoordinate];
	}

	private static String[] initializeAlgebraicNotation() {
		return new String[] { "A7", "B7", "C7", "D7", "E7", "F7", "G7", "A6", "B6", "C6", "D6", "E6", "F6", "G6", "A5",
				"B5", "C5", "D5", "E5", "F5", "G5", "A4", "B4", "C4", "D4", "E4", "F4", "G4", "A3", "B3", "C3", "D3",
				"E3", "F3", "G3", "A2", "B2", "C2", "D2", "E2", "F2", "G2", "A1", "B1", "C1", "D1", "E1", "F1", "G1" };
	}

	public static void endGame() {
		MainGUI.getInstance().getGameSetup().defaultGameSetup();
		InfoPanel.getPauseButton().setSelected(false);
		MainGUI.getInstance().getHistoryPanel().turnOnList(true);
		String winner = "     White Player has won!";
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("unused")
		boolean draw = false;
		if (MainGUI.getInstance().getBoard().getWhitePieces().size() < MainGUI.getInstance().getBoard().getBlackPieces()
				.size()) {
			winner = "     Black Player has won!";
		}
		if (MainGUI.getInstance().getBoard().getWhitePieces().size() == MainGUI.getInstance().getBoard().getBlackPieces()
				.size()) {
			draw = true;
			winner = "     It's a draw!";
		}
		sb.append(winner);
		InfoPanel.setPermaText(sb.toString());
	}
	
	public static void showBestMove(){
		Builder builder = new Builder();

		for (Piece piece : MainGUI.getInstance().getBoard().getAllPieces()) {
			builder.setPiece(piece);
		}

		builder.setMoveMaker(MainGUI.getInstance().getBoard().getCurrentPlayer().getAlliance());

		BestMove bestMove = new BestMove();
		bestMove.execute();
		try {
			Move move = bestMove.get();
			MainGUI.getInstance().getBoardPanel().drawBoard();
			MainGUI.getInstance().getBoardPanel().getTile(move.getCurrentCoordinate()).setBackground(Color.YELLOW);
			MainGUI.getInstance().getBoardPanel().getTile(move.getDestinationCoordinate())
					.setBackground(Color.YELLOW);
			MainGUI.getInstance().updateBoard(builder.build());
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
	}

}

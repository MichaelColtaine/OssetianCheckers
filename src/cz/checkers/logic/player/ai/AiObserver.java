package cz.checkers.logic.player.ai;

import java.util.Observable;
import java.util.Observer;

import cz.checkers.gui.MainGUI;
import cz.checkers.logic.utils.BoardUtils;

public class AiObserver implements Observer {

	private int whiteDif, blackDif;
	AiPlayer aiPlayer;

	public AiObserver() {

	}

	public void setWhiteDif(int depth) {
		this.whiteDif = depth;
	}

	public void setBlackDif(int depth) {
		this.blackDif = depth;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (MainGUI.getInstance().getGameSetup().isAIPlayer((MainGUI.getInstance().getBoard().getCurrentPlayer()))) {
			if (MainGUI.getInstance().getBoard().isGameOver()) {
				BoardUtils.endGame();
			} else {
				int difficulty = 0;
				if (MainGUI.getInstance().getBoard().getCurrentPlayer().getAlliance().isBlack()) {
					difficulty = blackDif;
				} else {
					difficulty = whiteDif;
				}
				aiPlayer = new AiPlayer();
				aiPlayer.setDifficulty(difficulty);
				aiPlayer.execute();
			}
		}
	}

	public void cancelThread() {
		try {
			aiPlayer.cancel(true);
		} catch (Exception e) {
			e.toString();
		}
	}

}

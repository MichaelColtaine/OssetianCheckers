package cz.checkers.gui.menu;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import cz.checkers.gui.MainGUI;
import cz.checkers.gui.panels.InfoPanel;
import cz.checkers.logic.enums.Alliance;
import cz.checkers.logic.enums.PlayerType;
import cz.checkers.logic.player.Player;
import cz.checkers.logic.utils.GuiUtils;

@SuppressWarnings("serial")
public class GameSetup extends JDialog {

	private PlayerType whitePlayerType, blackPlayerType;
	private JPanel whitePanel, blackPanel, whiteDifficultyPanel, blackDifficultyPanel, buttonsPanel;
	private ButtonGroup whiteDifficultyGroup, blackDifficultyGroup, whiteGroup, blackGroup;
	private JRadioButton whiteHumanButton, blackHumanButton, normalWhiteButton, hardWhiteButton, normalBlackButton,
			hardBlackButton, whiteComputerButton, blackComputerButton;
	int normal = 1, hard = 4;

	public GameSetup(JFrame frame) {
		super(frame, true);
		this.setResizable(false);
		//// WHITE
		setUndecorated(true);
		this.whiteGroup = new ButtonGroup();
		whiteHumanButton = new JRadioButton("HUMAN");
		whiteComputerButton = new JRadioButton("COMPUTER");
		whiteGroup.add(whiteHumanButton);
		whiteGroup.add(whiteComputerButton);
		whiteHumanButton.setSelected(true);

		whitePanel = new JPanel(new GridLayout(3, 0));
		
		whitePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		whitePanel.add(new Label("WHITE PLAYER"));
		whitePanel.add(whiteHumanButton);
		whitePanel.add(whiteComputerButton);

		whiteDifficultyPanel = new JPanel();
		whiteDifficultyPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		whiteDifficultyGroup = new ButtonGroup();

		normalWhiteButton = new JRadioButton("Normal", true);
		hardWhiteButton = new JRadioButton("Hard", false);

		normalWhiteButton.setEnabled(false);
		hardWhiteButton.setEnabled(false);

		whiteDifficultyGroup.add(normalWhiteButton);
		whiteDifficultyGroup.add(hardWhiteButton);

		whiteDifficultyPanel.add(normalWhiteButton);
		whiteDifficultyPanel.add(hardWhiteButton);

		whiteHumanButton.addActionListener(e -> {
			normalWhiteButton.setEnabled(false);
			hardWhiteButton.setEnabled(false);
		});

		whiteComputerButton.addActionListener(e -> {
			normalWhiteButton.setEnabled(true);
			hardWhiteButton.setEnabled(true);
		});

		normalWhiteButton.addActionListener(e -> {
			MainGUI.getInstance().getAIObserver().setWhiteDif(normal);
		});

		hardWhiteButton.addActionListener(e -> {
			MainGUI.getInstance().getAIObserver().setWhiteDif(hard);
		});

		//////// BLACK!

		blackGroup = new ButtonGroup();
		blackHumanButton = new JRadioButton("HUMAN");
		blackComputerButton = new JRadioButton("COMPUTER");
		blackGroup.add(blackHumanButton);
		blackGroup.add(blackComputerButton);
		blackHumanButton.setSelected(true);

		blackPanel = new JPanel(new GridLayout(3, 0));
		blackPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		blackPanel.add(new Label("BLACK PLAYER"));
		blackPanel.add(blackHumanButton);
		blackPanel.add(blackComputerButton);

		blackDifficultyPanel = new JPanel();
		blackDifficultyPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		blackDifficultyGroup = new ButtonGroup();

		normalBlackButton = new JRadioButton("Normal", true);
		hardBlackButton = new JRadioButton("Hard", false);

		normalBlackButton.setEnabled(false);
		hardBlackButton.setEnabled(false);

		blackDifficultyGroup.add(normalBlackButton);
		blackDifficultyGroup.add(hardBlackButton);

		blackDifficultyPanel.add(normalBlackButton);
		blackDifficultyPanel.add(hardBlackButton);

		blackHumanButton.addActionListener(e -> {
			normalBlackButton.setEnabled(false);
			hardBlackButton.setEnabled(false);
		});

		blackComputerButton.addActionListener(e -> {
			normalBlackButton.setEnabled(true);
			hardBlackButton.setEnabled(true);
		});

		normalBlackButton.addActionListener(e -> {
			MainGUI.getInstance().getAIObserver().setBlackDif(normal);
		});

		hardBlackButton.addActionListener(e -> {
			MainGUI.getInstance().getAIObserver().setBlackDif(hard);
		});

		// OK & CANCEL panel

		buttonsPanel = new JPanel(new GridLayout(2, 0));
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setMnemonic(KeyEvent.VK_C);
		JButton okButton = new JButton("OK");
		okButton.setMnemonic(KeyEvent.VK_O);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);

		JPanel myPanel = new JPanel(new GridLayout(5, 0));
		getContentPane().add(myPanel);
		myPanel.add(blackPanel);
		myPanel.add(blackDifficultyPanel);
		myPanel.add(whitePanel);
		myPanel.add(whiteDifficultyPanel);
		myPanel.add(buttonsPanel);

		okButton.addActionListener(e -> {
			if (whiteComputerButton.isSelected() || blackComputerButton.isSelected()) {
				InfoPanel.getPauseButton().setEnabled(true);
				InfoPanel.getPauseButton().setSelected(true);
				InfoPanel.startGameCPU();
			} else {
				InfoPanel.getPauseButton().setEnabled(false);
			}
			InfoPanel.updateInfoPanel();
			selectHistory();
			this.setVisible(false);
		});

		cancelButton.addActionListener(e -> {
			getSavedSettings();
			this.setVisible(false);
		});

		pack();
		setVisible(false);
	}

	private void selectHistory() {
		if (MainGUI.getInstance().getHistoryPanel().getSelectedIndex() == -1) {
			GuiUtils.selectedMove(MainGUI.getInstance().getHistoryPanel().getIndexOfLastTurn());
		} else if (MainGUI.getInstance().getHistoryPanel().getSelectedIndex() < MainGUI.getInstance().getHistoryPanel()
				.getIndexOfLastTurn()) {
			GuiUtils.selectedMove(MainGUI.getInstance().getHistoryPanel().getSelectedIndex());
		}
	}

	public void getSavedSettings() {

		if (isBlackPlayer == true) {
			blackHumanButton.setSelected(true);
			blackComputerButton.setSelected(false);

			normalBlackButton.setEnabled(false);
			hardBlackButton.setEnabled(false);
		} else {
			blackHumanButton.setSelected(false);
			blackComputerButton.setSelected(false);

		}

		if (isWhitePlayer == true) {
			whiteHumanButton.setSelected(true);
			whiteComputerButton.setSelected(false);

			normalWhiteButton.setEnabled(false);
			hardWhiteButton.setEnabled(false);
		} else {
			whiteHumanButton.setSelected(false);

		}
	}

	public boolean isWhiteComputerBtnSelected() {
		return whiteComputerButton.isSelected();
	}

	public boolean isBlackComputerBtnSelected() {
		return blackComputerButton.isSelected();
	}

	public void setupGame() {
		if (whiteComputerButton.isSelected()) {
			whitePlayerType = PlayerType.COMPUTER;
		} else {
			whitePlayerType = PlayerType.HUMAN;
		}
		if (blackComputerButton.isSelected()) {
			blackPlayerType = PlayerType.COMPUTER;

		} else {
			blackPlayerType = PlayerType.HUMAN;
		}

		if (blackComputerButton.isSelected() || whiteComputerButton.isSelected()) {
			InfoPanel.getPauseButton().setEnabled(true);
		} else {
		}
	}

	public void defaultGameSetup() {
		whitePlayerType = PlayerType.HUMAN;
		blackPlayerType = PlayerType.HUMAN;
		whiteDifficultyGroup.clearSelection();
		blackDifficultyGroup.clearSelection();
		whiteGroup.clearSelection();
		whiteHumanButton.setSelected(true);
		blackGroup.clearSelection();
		blackHumanButton.setSelected(true);
		normalWhiteButton.setEnabled(false);
		normalWhiteButton.setSelected(true);
		hardWhiteButton.setEnabled(false);
		normalBlackButton.setEnabled(false);
		normalBlackButton.setSelected(true);
		hardBlackButton.setEnabled(false);
		InfoPanel.getPauseButton().setEnabled(false);
		InfoPanel.getSetupButton().setEnabled(true);
		InfoPanel.getNewGameButton().setEnabled(true);
	}

	public void stopGame() {
		whitePlayerType = PlayerType.HUMAN;
		blackPlayerType = PlayerType.HUMAN;
	}

	public void promptUser() {
		saveCurrentSettings();
		setLocationRelativeTo(this.getParent());
		setVisible(true);
		repaint();
	}

	boolean isBlackPlayer, isWhitePlayer;

	public void saveCurrentSettings() {

		if (whiteHumanButton.isSelected()) {
			isWhitePlayer = true;
		} else {
			isWhitePlayer = false;
		}

		if (blackHumanButton.isSelected()) {
			isBlackPlayer = true;
		} else {
			isBlackPlayer = false;
		}

	}

	public boolean isAIPlayer(Player player) {
		if (player.getAlliance() == Alliance.WHITE) {
			return getWhitePlayerType() == PlayerType.COMPUTER;
		}
		return getBlackPlayerType() == PlayerType.COMPUTER;
	}

	PlayerType getWhitePlayerType() {
		return this.whitePlayerType;
	}

	PlayerType getBlackPlayerType() {
		return this.blackPlayerType;
	}

}
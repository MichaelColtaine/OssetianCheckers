package cz.checkers.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import cz.checkers.ResourceLoader;
import cz.checkers.gui.MainGUI;
import cz.checkers.gui.menu.GameSetup;
import cz.checkers.logic.player.Player;
import cz.checkers.logic.utils.GuiUtils;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel {

	static JButton setupButton;
	static JButton newGameButton;

	static JLabel currentPlayerLabel, whitePiecesLabel, blackPiecesLabel, iconLabel, logLabel, matchupLabel;
	public static String blackCurrentPanelPath = "currentB.png", whiteCurrentPanelPath = "currentW.png";
	public static String currentlyDisplayedLabelPath = "currentW.png";
	private static String currentMatchUp = "Game setup:  White[H] vs Black[H]";
	public static JToggleButton pauseButton;

	public InfoPanel() {
		GridLayout layout = new GridLayout(0, 3);
		this.setPreferredSize(new Dimension(20, 60));
		this.setLayout(layout);
		ImageIcon setupIcon = new ImageIcon(ResourceLoader.getImage("setup.png"));

		setupButton = new JButton();
		setupButton.setFocusPainted(false);
		setupButton.setEnabled(true);
		setupButton.setIcon(setupIcon);
		setupButton.setPreferredSize(new Dimension(100, 50));
		setupButton.setBackground(Color.decode("#EEEEEE"));
		setupButton.setMnemonic(KeyEvent.VK_S);

		ImageIcon newGame = new ImageIcon(ResourceLoader.getImage("newGame.png"));

		newGameButton = new JButton();
		newGameButton.setFocusPainted(false);
		newGameButton.setEnabled(true);
		newGameButton.setIcon(newGame);
		newGameButton.setPreferredSize(new Dimension(100, 50));
		newGameButton.setBackground(Color.decode("#EEEEEE"));
		newGameButton.setMnemonic(KeyEvent.VK_N);

		ImageIcon pauseIcon = new ImageIcon(ResourceLoader.getImage("pause.png"));
		ImageIcon playIcon = new ImageIcon(ResourceLoader.getImage("play.png"));

		pauseButton = new JToggleButton();
		pauseButton.setIcon(playIcon);
		pauseButton.setSelectedIcon(pauseIcon);
		pauseButton.setPreferredSize(new Dimension(100, 50));
		pauseButton.setEnabled(false);
		pauseButton.setBackground(Color.decode("#EEEEEE"));
		pauseButton.setMnemonic(KeyEvent.VK_A);
		pauseButton.setSelected(false);

		pauseButton.addActionListener(e -> {
			if (pauseButton.isSelected()) {
				startGameCPU();
				MainGUI.getInstance().moveMadeUpdate();
			} else {

				InfoPanel.setPermaText("");
				pauseGame();

			}
		});

		setupButton.addActionListener(e -> {
			showGameSetup();

		});

		newGameButton.addActionListener(e -> {
			InfoPanel.setPermaText("");
			MainGUI.getInstance().getGameSetup().defaultGameSetup();
			MainGUI.getInstance().getAIObserver().setBlackDif(1);
			MainGUI.getInstance().getAIObserver().setWhiteDif(1);
			GuiUtils.restartGame();
			showGameSetup();
		});

		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(0, 3));
		eastPanel.add(pauseButton);

		eastPanel.add(newGameButton);
		eastPanel.add(setupButton);

		currentPlayerLabel = new JLabel();
		currentPlayerLabel.setIcon(new ImageIcon(ResourceLoader.getImage(currentlyDisplayedLabelPath)));

		JPanel northPanel = new JPanel(new GridLayout(1, 1));
		JPanel centerPanel = new JPanel(new BorderLayout());

		whitePiecesLabel = new JLabel("White pieces:    21");
		blackPiecesLabel = new JLabel("Black pieces:    21");
		matchupLabel = new JLabel(currentMatchUp);
		logLabel = new JLabel();

		logLabel.setFont(new Font(logLabel.getName(), Font.PLAIN, 18));

		northPanel.add(whitePiecesLabel);
		northPanel.add(blackPiecesLabel);
		centerPanel.add(northPanel, BorderLayout.SOUTH);
		centerPanel.add(matchupLabel, BorderLayout.NORTH);
		centerPanel.add(logLabel, BorderLayout.CENTER);

		JPanel p = new JPanel();
		p.add(currentPlayerLabel);
		this.add(p);
		this.add(centerPanel);
		this.add(eastPanel);
	}

	public static void startGameCPU() {
		newGameButton.setEnabled(false);
		setupButton.setEnabled(false);
		MainGUI.getInstance().getHistoryPanel().turnOnList(false);
		MainGUI.getInstance().getGameSetup().setupGame();
		MainGUI.getInstance().getBoardPanel().drawBoard();
	}

	private void showGameSetup() {
		MainGUI.getInstance().getGameSetup().promptUser();
		MainGUI.getInstance().setupUpdate();
	}

	public static void pauseGame() {
		InfoPanel.setText("");
		MainGUI.getInstance().getGameSetup().stopGame();
		MainGUI.getInstance().getAIObserver().cancelThread();
		MainGUI.getInstance().getHistoryPanel().turnOnList(true);
		newGameButton.setEnabled(true);
		setupButton.setEnabled(true);

	}

	public static JToggleButton getPauseButton() {
		return pauseButton;
	}

	public static JButton getSetupButton() {
		return setupButton;
	}

	public static void setText(String text) {
		Timer timer = new Timer();
		logLabel.setForeground(Color.RED);
		logLabel.setText(text);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				logLabel.setText("");
			}
		};
		timer.schedule(task, 1500);
	}

	public static void setPermaText(String text) {
		logLabel.setForeground(Color.BLACK);
		logLabel.setText(text);
	}

	public static JLabel getCurrentPlayerLabel() {
		return currentPlayerLabel;
	}

	public static JLabel getWhitePiecesLabel() {
		return whitePiecesLabel;
	}

	public static JLabel getBlackPiecesLabel() {
		return blackPiecesLabel;
	}

	public static void updateInfoPanel() {

		setCurrentPlayerLabel(MainGUI.getInstance().getBoard().getCurrentPlayer());
		setPiecesCount(MainGUI.getInstance().getBoard().getWhitePieces().size(),
				MainGUI.getInstance().getBoard().getBlackPieces().size());
		setCurrentMatchUp(MainGUI.getInstance().getGameSetup());
	}

	public static void setCurrentMatchUp(GameSetup gameSetup) {
		String black, white;
		if (gameSetup.isBlackComputerBtnSelected()) {
			black = "Black[CPU]";
		} else {
			black = "Black[H]";
		}

		if (gameSetup.isWhiteComputerBtnSelected()) {
			white = "White[CPU]";
		} else {
			white = "White[H]";
		}

		currentMatchUp = white + " vs " + black;
		matchupLabel.setText("Game setup:  " + currentMatchUp);

	}

	public static void setCurrentPlayerLabel(Player player) {
		if (player.getAlliance().isBlack()) {
			if (MainGUI.getInstance().getGameSetup().isBlackComputerBtnSelected()) {
				blackCurrentPanelPath = "currentBcpu.png";
			} else {
				blackCurrentPanelPath = "currentB.png";
			}
			currentPlayerLabel.setIcon(new ImageIcon(ResourceLoader.getImage(blackCurrentPanelPath)));
		} else {
			if (MainGUI.getInstance().getGameSetup().isWhiteComputerBtnSelected()) {
				whiteCurrentPanelPath = "currentWcpu.png";
			} else {
				whiteCurrentPanelPath = "currentW.png";
			}
			currentPlayerLabel.setIcon(new ImageIcon(ResourceLoader.getImage(whiteCurrentPanelPath)));
		}
	}

	public static void setPiecesCount(int whiteCount, int blackCount) {
		getWhitePiecesLabel().setText("White pieces:    " + whiteCount);
		getBlackPiecesLabel().setText("Black pieces:    " + blackCount);
	}

	public static JComponent getNewGameButton() {
		return newGameButton;
	}

}

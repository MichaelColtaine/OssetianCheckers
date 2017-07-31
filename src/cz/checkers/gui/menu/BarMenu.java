package cz.checkers.gui.menu;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu.Separator;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.glass.events.KeyEvent;

import cz.checkers.ResourceLoader;
import cz.checkers.gui.MainGUI;
import cz.checkers.gui.panels.InfoPanel;
import cz.checkers.logic.board.Board;
import cz.checkers.logic.moveLog.MoveLog;
import cz.checkers.logic.save.SaveData;
import cz.checkers.logic.utils.BoardUtils;
import cz.checkers.logic.utils.GuiUtils;

@SuppressWarnings({ "serial", "restriction" })
public class BarMenu extends JMenuBar {

	String SaveLoadPath = System.getProperty("user.dir");

	public BarMenu() {
		this.add(createFileMenu());
		this.add(createHelpMenu());
		this.setFocusable(true);

	}

	private File getSelectedFileWithExtension(JFileChooser c) {
		File file = c.getSelectedFile();

		file = new File(file.toString() + ".save");

		return file;
	}

	public void saveGame() {
		JFileChooser fileChooser = new JFileChooser(SaveLoadPath);
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter saveFilter = new FileNameExtensionFilter("save files (*.save)", "save");
		fileChooser.setFileFilter(saveFilter);
		int option = fileChooser.showSaveDialog(MainGUI.getInstance().getGameFrame());

		if (option == JFileChooser.APPROVE_OPTION) {
			File fileToSave = getSelectedFileWithExtension(fileChooser);
			SaveData.save(MainGUI.getInstance().getGameState(), fileToSave);
			System.exit(0);
		} else if (option == JFileChooser.CANCEL_OPTION) {

		}
	}

	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		fileMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				if (InfoPanel.getPauseButton().isSelected()) {
					InfoPanel.pauseGame();

					InfoPanel.getPauseButton().setSelected(false);
				}
			}

			@Override
			public void menuCanceled(MenuEvent arg0) {
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
			}
		});

		JMenuItem saveItem = new JMenuItem("Save Game");
		saveItem.addActionListener(e -> {
			saveGame();
		});
		saveItem.setMnemonic(KeyEvent.VK_S);
		KeyStroke saveItemKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.MODIFIER_ALT);
		saveItem.setAccelerator(saveItemKey);

		JMenuItem loadItem = new JMenuItem("Open Game");
		loadItem.addActionListener(e -> {
			InfoPanel.setPermaText("");
			InfoPanel.getSetupButton().setEnabled(true);
			JFileChooser fileChooser = new JFileChooser(SaveLoadPath);
			fileChooser.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter saveFilter = new FileNameExtensionFilter("save files (*.save)", "save");
			fileChooser.setFileFilter(saveFilter);

			int option = fileChooser.showOpenDialog(MainGUI.getInstance().getGameFrame());
			if (option == JFileChooser.APPROVE_OPTION) {
				MainGUI.getInstance().getMoveLog().clear();
				File file = fileChooser.getSelectedFile();
				SaveData.load(file);
				Board chessBoard = SaveData.getBoard();
				MoveLog moveLog = SaveData.getMoveLog();
				MainGUI.getInstance().updateBoard(chessBoard);
				MainGUI.getInstance().getHistoryPanel().redo(chessBoard, moveLog);
				InfoPanel.updateInfoPanel();
				MainGUI.getInstance().getBoardPanel().drawBoard();

			}
		});
		loadItem.setMnemonic(KeyEvent.VK_O);
		KeyStroke loadItemKey = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.MODIFIER_ALT);
		loadItem.setAccelerator(loadItemKey);

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(e -> {
			GuiUtils.exitPrompt();
		});

		exitItem.setMnemonic(KeyEvent.VK_X);
		KeyStroke exitItemKey = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.MODIFIER_ALT);
		exitItem.setAccelerator(exitItemKey);

		fileMenu.add(loadItem);
		fileMenu.add(saveItem);
		fileMenu.add(new Separator());
		fileMenu.add(exitItem);
		return fileMenu;
	}

	private JMenu createHelpMenu() {
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				if (InfoPanel.getPauseButton().isSelected()) {
					InfoPanel.pauseGame();
					InfoPanel.getPauseButton().setSelected(false);
				}
			}

			@Override
			public void menuCanceled(MenuEvent arg0) {
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
			}
		});

		JMenuItem rulesItem = new JMenuItem("Show Rules");
		rulesItem.setMnemonic(KeyEvent.VK_R);
		KeyStroke rulesItemKey = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.MODIFIER_ALT);
		rulesItem.setAccelerator(rulesItemKey);

		JDialog helpDialog = new JDialog();
		JPanel rulesPanel = new JPanel();
		JLabel label = new JLabel();

		ImageIcon helpIcon = new ImageIcon(ResourceLoader.getImage("help.png"));

		label.setIcon(helpIcon);
		rulesPanel.add(label);

		helpDialog.add(rulesPanel);
		helpDialog.setSize(600, 600);
		helpDialog.setResizable(false);

		rulesItem.addActionListener(e -> {
			helpDialog.setLocationRelativeTo(this.getParent());
			helpDialog.setVisible(true);
			helpDialog.repaint();
		});

		JMenuItem showBestMoveItem = new JMenuItem("Show Best Move");
		showBestMoveItem.setMnemonic(KeyEvent.VK_B);
		KeyStroke showBestMoveItemKey = KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.MODIFIER_ALT);
		showBestMoveItem.setAccelerator(showBestMoveItemKey);
		showBestMoveItem.addActionListener(e -> {
			BoardUtils.showBestMove();
		});

		JCheckBoxMenuItem highlightBoardItem = new JCheckBoxMenuItem("Show Legal Moves", false);
		highlightBoardItem.setMnemonic(KeyEvent.VK_L);
		KeyStroke highlightBoardItemKey = KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.MODIFIER_ALT);
		highlightBoardItem.setAccelerator(highlightBoardItemKey);

		highlightBoardItem.addActionListener(e -> {
			MainGUI.getInstance().setHighLightLegalMoves(highlightBoardItem.isSelected());
		});

		JCheckBoxMenuItem highlightLastMoveItem = new JCheckBoxMenuItem("Show Last Moves", false);
		highlightLastMoveItem.setMnemonic(KeyEvent.VK_M);
		KeyStroke highlightLastMoveKey = KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.MODIFIER_ALT);
		highlightLastMoveItem.setAccelerator(highlightLastMoveKey);

		highlightLastMoveItem.addActionListener(e -> {
			MainGUI.getInstance().setHighlightLastMove(highlightLastMoveItem.isSelected());
			MainGUI.getInstance().getBoardPanel().drawBoard();
		});

		helpMenu.add(rulesItem);
		helpMenu.add(new Separator());
		helpMenu.add(showBestMoveItem);
		helpMenu.add(highlightBoardItem);
		helpMenu.add(highlightLastMoveItem);
		return helpMenu;
	}

}

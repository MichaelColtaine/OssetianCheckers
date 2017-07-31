package cz.checkers.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

import cz.checkers.ResourceLoader;
import cz.checkers.gui.menu.BarMenu;
import cz.checkers.gui.menu.GameSetup;
import cz.checkers.gui.panels.HistoryPanel;
import cz.checkers.gui.panels.InfoPanel;
import cz.checkers.logic.board.Board;
import cz.checkers.logic.move.Move;
import cz.checkers.logic.move.Move.MoveFactory;
import cz.checkers.logic.move.MoveTransition;
import cz.checkers.logic.moveLog.MoveLog;
import cz.checkers.logic.piece.Piece;
import cz.checkers.logic.player.ai.AiObserver;
import cz.checkers.logic.save.GameState;
import cz.checkers.logic.tile.Tile;
import cz.checkers.logic.utils.BoardUtils;
import cz.checkers.logic.utils.GuiUtils;

public class MainGUI extends Observable {

	private JFrame gameFrame;
	private HistoryPanel historyPanel;
	private BoardPanelGUI boardPanel;
	private MoveLog moveLog;
	private GameSetup gameSetup;
	private BarMenu barMenu;
	private Board board;
	private AiObserver aiObserver;
	private JPanel infoPanel;

	private Tile sourceTile;
	private Tile destination;
	private Piece selectedPiece;
	private boolean highlightLegalMoves;
	private boolean highlightLastMove;
	public String blackPieceIconPath = "bp.png";
	public String whitePieceIconPath = "wp.png";

	private static Dimension FRAME_DIMENSION = new Dimension(700, 700);
	private static Dimension BOARD_DIMENSION = new Dimension(400, 400);
	private static Dimension TILE_DIMENSION = new Dimension(10, 10);

	private static MainGUI INSTANCE = new MainGUI();

	private MainGUI() {
		setTheme();
		this.gameFrame = new JFrame("Ossetian Checkers");
		this.gameFrame.setLayout(new BorderLayout());
		this.barMenu = new BarMenu();
		this.gameFrame.setJMenuBar(barMenu);
		this.gameFrame.setSize(FRAME_DIMENSION);
		this.gameFrame.setLocationRelativeTo(null);
		this.gameFrame.setResizable(false);
		this.gameFrame.setIconImage(ResourceLoader.getImage("icon.png"));

		this.board = Board.createStandardBoard();

		this.aiObserver = new AiObserver();
		this.addObserver(aiObserver);
		this.gameSetup = new GameSetup(this.gameFrame);

		this.highlightLegalMoves = false;
		this.highlightLastMove = false;

		this.historyPanel = new HistoryPanel();
		this.boardPanel = new BoardPanelGUI();

		this.infoPanel = new InfoPanel();
		this.moveLog = new MoveLog();

		this.gameFrame.add(infoPanel, BorderLayout.NORTH);
		this.gameFrame.add(
				new JLabel(
						"                   A                       B                     C                     D                      E                       F                     G"),
				BorderLayout.SOUTH);
		this.gameFrame.add(new JLabel(new ImageIcon(ResourceLoader.getImage("westPanel.png"))), BorderLayout.WEST);
		this.gameFrame.add(this.historyPanel, BorderLayout.EAST);
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

		this.gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.gameFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				InfoPanel.pauseGame();
				InfoPanel.getPauseButton().setSelected(false);
				GuiUtils.exitPrompt();
			}
		});

		aiObserver.setBlackDif(1);
		aiObserver.setWhiteDif(1);
		this.gameFrame.setVisible(true);
	}

	private void setTheme() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {

		}
	}



	public static MainGUI getInstance() {
		return INSTANCE;
	}

	/*
	 * Startuje hru.
	 */
	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				historyPanel.redo(board, moveLog);
				// moveLog.clear();
				boardPanel.drawBoard();
			}
		});
	}

	public void setHighlightLastMove(boolean bol) {
		this.highlightLastMove = bol;
	}


	public void setHighLightLegalMoves(boolean bool) {
		this.highlightLegalMoves = bool;
	}

	

	public void setupUpdate() {
		setChanged();
		notifyObservers();
	}
	
	/*
	 * Posle zpravu AIobserver.  Pokud je na tahu pocitac tak observer zavola minimax a tahne.
	 */

	public void moveMadeUpdate() {
		setChanged();
		notifyObservers();
	}

	public void updateBoard(Board board) {
		this.board = board;
	}



	/*
	 * Vycisti vsechny promenne ktere slouzi k ovladani figurek
	 */

	public void clear() {
		sourceTile = null;
		destination = null;
		selectedPiece = null;
	}
	
	public GameSetup getGameSetup() {
		return this.gameSetup;
	}

	public Board getBoard() {
		return this.board;
	}
	
	
	public BoardPanelGUI getBoardPanel() {
		return this.boardPanel;
	}

	public MoveLog getMoveLog() {
		return this.moveLog;
	}

	public HistoryPanel getHistoryPanel() {
		return this.historyPanel;
	}
	
	public GameState getGameState() {
		return new GameState(board, moveLog);
	}

	public JPanel getInfoPanel() {
		return this.infoPanel;
	}

	public JFrame getGameFrame() {
		return this.gameFrame;
	}

	public AiObserver getAIObserver() {
		return this.aiObserver;
	}
	
	public BarMenu getBarMenu(){
		return this.barMenu;
	}


	/*
	 * Trida TilePanel reprezentuje jednotliva pole herni desky.
	 * 
	 * V teto tride se registruji eventy
	 */

	@SuppressWarnings("serial")
	public class TilePanelGUI extends JPanel {
		private int tileId;
		private Border border = BorderFactory.createLineBorder(Color.BLACK);
		private Color WhiteTileColor = Color.WHITE;
		private Color BlackTileColor = Color.decode("#D18B47");
		private Color colorOfSelectedTile = new Color(0, 0, 0, 0);
		private Color lastMoveHighWhiteColor = new Color(181, 199, 229);
		private Color lastMoveAttackedPieceHighWhiteColor = new Color(255, 140, 140);

		public TilePanelGUI(BoardPanelGUI boardPanel, int tileId) {
			super(new GridBagLayout());
			this.tileId = tileId;
			this.setPreferredSize(TILE_DIMENSION);
			this.assignTileColor();
			this.assignTilePieceIcon();
			this.setBorder(border);

			addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					if (!cantClick()) {
						if (!board.isGameOver()) {
							if (SwingUtilities.isRightMouseButton(e)) {
								clear();
								boardPanel.drawBoard();
							} else if (SwingUtilities.isLeftMouseButton(e)) {
								if (sourceTile == null) {
									sourceTile = board.getTile(tileId);
									selectedPiece = sourceTile.getPiece();
									if (selectedPiece == null) {
										sourceTile = null;
									}
									showInfoMessage();
								} else {
									destination = board.getTile(tileId);
									Move move = MoveFactory.createMove(board, sourceTile.getTileCoordinate(),
											destination.getTileCoordinate());
									MoveTransition transition = board.getCurrentPlayer().makeMove(move);
									if (transition.getMoveStatus().isDone()) {
										board = transition.getToBoard();
										moveLog.addMove(move);

									}
									clear();
								}
							}

							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									if (gameSetup.isAIPlayer(board.getCurrentPlayer())) {
										moveMadeUpdate();
									}
									if (board.isGameOver()) {
										BoardUtils.endGame();
									}
									InfoPanel.updateInfoPanel();
									boardPanel.drawBoard();
								}
							});
						}
					} else {
						InfoPanel.setText("It's CPU's turn -->");
					}
				}

			});
			validate();
		}

		private boolean cantClick() {
			return (gameSetup.isBlackComputerBtnSelected() && board.getCurrentPlayer().getAlliance().isBlack())
					|| (gameSetup.isWhiteComputerBtnSelected() && board.getCurrentPlayer().getAlliance().isWhite());
		}

		private void showInfoMessage() {
			if (sourceTile != null) {
				if (gameSetup.isWhiteComputerBtnSelected() && board.getCurrentPlayer().getAlliance().isWhite()
						|| gameSetup.isBlackComputerBtnSelected() && board.getCurrentPlayer().getAlliance().isBlack()) {
					InfoPanel.setText("It's CPU's turn -->");
					clear();
				} else if (selectedPiece.getPieceAlliance() != board.getCurrentPlayer().getAlliance()) {
					InfoPanel.setText("Not your piece!");
					clear();
				} else if (board.isJumpInProgress() && !sourceTile.getPiece().isActive()) {
					InfoPanel.setText("Some piece can still attack!");
					clear();
				} else if (board.somePieceCanAttack() && !selectedPiece.canAttack()) {
					InfoPanel.setText("Some piece must attack!");
					clear();
				} else if (!selectedPiece.canAttack() && !selectedPiece.canMove()) {
					InfoPanel.setText("This piece can't move!");
					clear();
				}
			}
		}

		private void drawTile() {
			assignTileColor();
			assignTilePieceIcon();
			highlightLegals();
			highlightLastMove();
			changeColorOfSelectedTile();
			validate();
			repaint();
		}

		private void changeColorOfSelectedTile() {
			if (sourceTile != null
					&& sourceTile.getPiece().getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
				if (sourceTile.getPiece().canMove() || sourceTile.getPiece().canAttack()) {
					boardPanel.getTile(sourceTile.getTileCoordinate()).setBackground(colorOfSelectedTile);
				}
			}
		}

		/*
		 * Podle alliance zabrazi bud cernou a nebo bilou ikonku figurky
		 */
		private void assignTilePieceIcon() {
			this.removeAll();
			if (board.getTile(this.tileId).isTileOccupied()) {
				if (board.getTile(this.tileId).getPiece().getPieceAlliance().isBlack()) {
					add(new JLabel(new ImageIcon(ResourceLoader.getImage(blackPieceIconPath))));
				} else {
					add(new JLabel(new ImageIcon(ResourceLoader.getImage(whitePieceIconPath))));
				}
			}
		}

		/*
		 * Nastavi barvu policka podle toho zda jsou sude nebo liche
		 */
		private void assignTileColor() {
			if (this.tileId % 2 != 0) {
				setBackground(WhiteTileColor);
			} else {
				setBackground(BlackTileColor);
			}
		}

		/*
		 * Zmeni barvu policek posledniho skoku.
		 */

		private void highlightLastMove() {
			if (highlightLastMove) {
				if (board.getLastMove() != null) {
					if (!board.getLastMove().isAttackMove()) {
						boardPanel.getTile(board.getLastMove().getCurrentCoordinate())
								.setBackground(lastMoveHighWhiteColor);
						boardPanel.getTile(board.getLastMove().getDestinationCoordinate())
								.setBackground(lastMoveHighWhiteColor);
					} else if (board.getLastMove().isAttackMove()) {
						boardPanel.getTile(board.getLastMove().getCurrentCoordinate())
								.setBackground(lastMoveHighWhiteColor);
						boardPanel.getTile(board.getLastMove().getAttackedPiece().getPiecePosition())
								.setBackground(lastMoveAttackedPieceHighWhiteColor);
						boardPanel.getTile(board.getLastMove().getDestinationCoordinate())
								.setBackground(lastMoveHighWhiteColor);
					}
				}
			}
		}

		/*
		 * Zobrazi legalni pohyby vybrane figurky
		 */
		private void highlightLegals() {
			if (highlightLegalMoves) {
				for (Move move : board.getCurrentPlayer().getLegalMoves()) {
					if (move.getMovedPiece() == selectedPiece
							&& selectedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
						if (move.getDestinationCoordinate() == this.tileId && !board.getTile(tileId).isTileOccupied()) {
							add(new JLabel(new ImageIcon(ResourceLoader.getImage("high.png"))));
						}
					}
				}
			}
		}
	}

	/*
	 * Trida BoardPanel slouzi k vytvoreni GUI reprezentace herni desky
	 */
	@SuppressWarnings("serial")
	public class BoardPanelGUI extends JPanel {
		private List<TilePanelGUI> boardTiles;

		public BoardPanelGUI() {
			super(new GridLayout(7, 7));
			this.boardTiles = new ArrayList<TilePanelGUI>();
			for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
				TilePanelGUI tilePanel = new TilePanelGUI(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_DIMENSION);
			validate();
		}

		public TilePanelGUI getTile(int id) {
			return boardTiles.get(id);
		}

		public List<TilePanelGUI> getAllTiles() {
			return this.boardTiles;
		}

		public void drawBoard() {
			removeAll();
			for (TilePanelGUI tilePanel : boardTiles) {
				tilePanel.drawTile();
				add(tilePanel);
			}
			validate();
			repaint();
		}
	}
}

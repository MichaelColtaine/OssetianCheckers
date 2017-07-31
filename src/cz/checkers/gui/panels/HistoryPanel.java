package cz.checkers.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cz.checkers.logic.board.Board;
import cz.checkers.logic.enums.Alliance;
import cz.checkers.logic.move.Move;
import cz.checkers.logic.moveLog.MoveLog;
import cz.checkers.logic.utils.GuiUtils;

@SuppressWarnings("serial")
public class HistoryPanel extends JPanel {
	private JList<String> list;
	private DefaultListModel<String> listModel;
	JScrollPane scrollPaneList;
	private int turn = 0;
	Alliance lastAlliance;

	public HistoryPanel() {
		this.setLayout(new BorderLayout());
		listModel = new DefaultListModel<>();
		list = new JList<>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.ensureIndexIsVisible(list.getLastVisibleIndex());
		scrollPaneList = new JScrollPane(list);

		scrollPaneList.setPreferredSize(new Dimension(155, 0));
		this.add(scrollPaneList, BorderLayout.WEST);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				GuiUtils.selectedMove(list.getSelectedIndex());
				InfoPanel.setText("");
			}
		});
		list.ensureIndexIsVisible(listModel.size());
	}

	public int getIndexOfLastTurn() {
		return list.getLastVisibleIndex();
	}

	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	
	public void moveFocusOfHistoryPanel(){
		scrollPaneList.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (InfoPanel.getPauseButton().isSelected()) {
					e.getAdjustable().setValue(e.getAdjustable().getMaximum());
				} else {
					e.getAdjustable().setValue(e.getAdjustable().getValue());
				}
			}
		});
	}

	public void redo(Board board, MoveLog log) {
		listModel.clear();
		listModel.addElement("        BEGINNING");
		turnCounter(log);
		moveFocusOfHistoryPanel();
	}
	
	/*
	 * pocita tahy
	 */

	public void turnCounter(MoveLog log) {
		for (Move m : log.getMoves()) {
			if (!(m.isAttackMove() && (m.getMovedPiece().getPieceAlliance() == lastAlliance))) {
				turn++;
			}
			listModel.addElement(turn + ". " + m.toString());
			lastAlliance = m.getMovedPiece().getPieceAlliance();
		}
		turn = 0;
	}
	
	

	public DefaultListModel<String> getListModel() {
		return this.listModel;
	}

	public void turnOnList(boolean b) {
		list.setEnabled(b);
	}

	public void clearHistory() {
		listModel.clear();
	}

}

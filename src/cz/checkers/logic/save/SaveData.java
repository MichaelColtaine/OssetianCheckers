package cz.checkers.logic.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import cz.checkers.gui.MainGUI;
import cz.checkers.logic.board.Board;
import cz.checkers.logic.move.Move;
import cz.checkers.logic.moveLog.MoveLog;

public class SaveData {

	static Board boardToReturn;
	static MoveLog moveLog;

	public static void save(Serializable object, File fileName) {
		try (FileOutputStream fos = new FileOutputStream(fileName);
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(object);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean checkFileExists(File file) {
		return new File(file.toString()).isFile();
	}

	public static Board getBoard() {
		return boardToReturn;
	}

	public static MoveLog getMoveLog() {
		return moveLog;
	}

	public static void load(File file) {
		if (checkFileExists(file)) {
			try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {
				GameState gameState = (GameState) ois.readObject();
				boardToReturn = gameState.getBoard();
				moveLog = gameState.getMoveLog();
				for (Move m : moveLog.getMoves()) {
					MainGUI.getInstance().getMoveLog().addMove(m);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

}

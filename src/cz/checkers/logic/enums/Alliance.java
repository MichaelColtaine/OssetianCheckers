package cz.checkers.logic.enums;

import cz.checkers.logic.player.BlackPlayer;
import cz.checkers.logic.player.Player;
import cz.checkers.logic.player.WhitePlayer;

public enum Alliance {
	WHITE {

		@Override
		public boolean isWhite() {

			return true;
		}

		@Override
		public boolean isBlack() {
			return false;
		}

		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			if(whitePlayer.getLegalMoves().isEmpty()){
				return blackPlayer;
			}
			return whitePlayer;
		}

		
	},
	BLACK {

		@Override
		public boolean isWhite() {
			return false;
		}

		@Override
		public boolean isBlack() {
			return true;
		}

		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			if(blackPlayer.getLegalMoves().isEmpty()){
				return whitePlayer;
			}
			return blackPlayer;
		}

	
		
		
	};
	

	public abstract boolean isWhite();

	public abstract boolean isBlack();

	public abstract Player choosePlayer(WhitePlayer lightPlayer, BlackPlayer darkPlayer);

	

}

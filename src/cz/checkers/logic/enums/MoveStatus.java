package cz.checkers.logic.enums;

public enum MoveStatus {
	DONE {
		@Override
		public boolean isDone() {
			return true;
		}
	},
	ILLEGAL_MOVE {
		public boolean isDone() {
			return false;
		}
	};

	public abstract boolean isDone();
}

import java.util.ArrayList;

public class State {
	
	
	
	private GuiCell[][] board;
	/* Static Variables */
	final static int player = -1;
	final static int free = 0;
	final static int ai = 1;
	// final static int MAX_SHEEP_TOKENS = 16;

	/* Essential Variables */
	State parentState;
	int currentTurn; // the current turn
	int score;

	int childrenLeft;

	int level;

	ArrayList<State> states;
	ArrayList<State> statesTemp = new ArrayList<State>();


	public State(GuiCell[][] board, State parent, int nextTurn, int level) {
		this.board = board;
		this.parentState = parent;
		this.currentTurn = nextTurn;
		this.level = level;
		childrenLeft = level;

	}

	public State(GuiCell[][] guiCells) {
		board = guiCells;
		currentTurn = ai;
	}

	// @Override
	// public boolean equals(Object o) {
	// State s = (State) o;
	//
	// return true;
	// }

	// function to get board
	public GuiCell[][] getBoard() {
		return this.board;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public boolean equals(Object o) {
		State s = (State) o;
		for (int i = 0; i < hexgame.BSIZE; i++) {
			for (int j = 0; j < hexgame.BSIZE; j++) {
				if (this.board[i][j].getValue() == s.getBoard()[i][j].getValue()) {
					return false;
				}

			}
		}

		return true;
	}

	// public boolean contains(ArrayList<GuiCell[][]> boards, GuiCell
	// currentState) {
	//
	// for (int a = 0; a < boards.size(); a++) {
	// if (!boards.get(a).equals(currentState)) {
	// return false;
	// }
	// }
	// return true;
	//
	// }

	public void computeScore() {

		System.out.println("nagcomputer score");

		int sum = 0;
		for (int i = 0; i < hexgame.BSIZE; i++) {
			for (int j = 0; j < hexgame.BSIZE; j++) {
				if (board[i][j].getOwner() == ai) {
					sum++;
				}
			}
		}

		score = sum;

		propagateScore();
	}

	public void propagateScore() {
		if (parentState != null && childrenLeft == 0)
			parentState.submit(this);
	}

	public void submit(State s) {
		if (currentTurn == player) {
			score = Math.min(score, s.getScore());
		} else {
			score = Math.max(score, s.getScore());
		}
		childrenLeft--;
		propagateScore();
	}

	public int getScore() {
		return score;
	}

	public void print(GuiCell[][] dummy) {

		// for (int j = 0; j < Gui.BOARDROW; j++) {
		// System.out.println();
		// for (int z = 0; z < Gui.BOARDCOLUMN; z++) {
		// System.out.print(dummy[j][z].getValue());
		// System.out.print(" | ");
		//
		// }
		//
		// }
	}

	public ArrayList<State> generateStates() {


		return states;

	}

	public int calculateScore() {
		int score = 0;

		return score;
	}
}
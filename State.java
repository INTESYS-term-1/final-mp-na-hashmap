import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class State {

	Map<Coordinate, GuiCell> hashMap = new HashMap<Coordinate, GuiCell>();

	// private GuiCell[][] board;
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

	public State(Map<Coordinate, GuiCell> hashMap, State parent, int nextTurn, int level) {
		this.hashMap = hashMap;
		this.parentState = parent;
		this.currentTurn = nextTurn;
		this.level = level;
		childrenLeft = level;

	}

	// public State(GuiCell[][] guiCells) {
	// board = guiCells;
	// currentTurn = ai;
	// }

	// @Override
	// public boolean equals(Object o) {
	// State s = (State) o;
	//
	// return true;
	// }

	// function to get board
	public Map<Coordinate, GuiCell> getHashMap() {
		return this.hashMap;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public boolean equals(Object o) {
		State s = (State) o;
		if (hashMap.equals(s.getHashMap())) {
			return true;
		}
		return false;
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

		// System.out.println("nagcomputer score");
		//
		// int sum = 0;
		// for (int i = 0; i < hexgame.BSIZE; i++) {
		// for (int j = 0; j < hexgame.BSIZE; j++) {
		// if (board[i][j].getOwner() == ai) {
		// sum++;
		// }
		// }
		// }
		//
		// score = sum;

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

		Collection c = hashMap.values();
		Iterator itr = c.iterator();

		for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {

			entry.getKey().print();
			entry.getValue().print();

			// Tab tab = entry.getValue();
			// do something with key and/or tab
		}
	}

	public void print() {

		// for (int j = 0; j < Gui.BOARDROW; j++) {
		// System.out.println();
		// for (int z = 0; z < Gui.BOARDCOLUMN; z++) {
		// System.out.print(dummy[j][z].getValue());
		// System.out.print(" | ");
		//
		// }
		//
		// }

		// Collection c = hashMap.values();
		// Iterator itr = c.iterator();
		//
		// System.out.println("-------------------------------------------------------");
		// int i = 0;
		// for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
		//
		// if (i != 0)
		// if (i % hexgame.BSIZE - 1 == 0) {
		// System.out.println();
		// System.out.print(entry.getValue().getValue());
		// System.out.print(" | ");
		// } else {
		// System.out.print(entry.getValue().getValue());
		// System.out.print(" | ");
		// }
		//
		// i++;
		//
		// // Tab tab = entry.getValue();
		// // do something with key and/or tab
		// }
		System.out.println();

		for (int i = 0; i < hexgame.BSIZE; i++) {
			for (int j = 0; j < hexgame.BSIZE; j++) {
				// if (hashMap.get(new Coordinate(j, i)).getValue() != 0) {
				// System.out.print(j + " " + i);
				//
				// } else {
				// System.out.print(hashMap.get(new Coordinate(j,
				// i)).getValue());
				//
				// }
				System.out.print(hashMap.get(new Coordinate(i, j)).getValue());

				System.out.print(" | ");

			}
			System.out.println();
		}

		System.out.println();
		System.out.println();

	}

	public ArrayList<State> generateStates() {

		ArrayList<State> states = new ArrayList<State>();

		for (int i = 0; i < hexgame.BSIZE; i++) {
			for (int j = 0; j < hexgame.BSIZE; j++) {

				if (hashMap.get(new Coordinate(i, j)).getOwner() == ai) {

					// System.out.println("hex is at");
					// System.out.println("i: " + (i));
					// System.out.println("j: " + (j));

					// System.out.println("yes");

					for (int k = i - 1, l = j - 1; k > 1 && l > 1; k = k + 1 - 1) {
						System.out.println("K: " + k);

						if (k % 2 == 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k - 1, l)).getOwner() != free) {
								for (int transfer = hashMap.get(new Coordinate(i, j)).getValue()
										- 1; transfer > 0; transfer--) {

									Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

									for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
										Coordinate key = new Coordinate(entry.getKey());
										GuiCell temp = new GuiCell(entry.getValue());

										hashMap2.put(key, temp);
									}

									State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, player,
											level + 1);

									newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
									newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
									newState.getHashMap().get(new Coordinate(i, j))
											.setValue(hashMap.get(new Coordinate(i, j)).getValue() - transfer);

									states.add(newState);
									System.out.println("newstate");
								}
								break;
							} else {
								k--;
							}
						} else if (k % 2 == 0) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k - 1, l - 1)).getOwner() != free) {
								for (int transfer = hashMap.get(new Coordinate(i, j)).getValue()
										- 1; transfer > 0; transfer--) {

									Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

									for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
										Coordinate key = new Coordinate(entry.getKey());
										GuiCell temp = new GuiCell(entry.getValue());

										hashMap2.put(key, temp);
									}

									State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, player,
											level + 1);

									newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
									newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
									newState.getHashMap().get(new Coordinate(i, j))
											.setValue(hashMap.get(new Coordinate(i, j)).getValue() - transfer);

									states.add(newState);
									System.out.println("newstate");

								}
								break;
							} else {
								k--;
								j--;
							}
						} else {
							k--;

						}
						// System.out.println("K: " + k);

					}
				}

			}

		}

		System.out.println(states.size());

		for (

				int i = 0; i < states.size(); i++) {
			states.get(i).print();
		}
		return states;

	}

	/*
	 * if ((j - 1) % 2 == 0) { if (hashMap.get(new Coordinate(i - k, j -
	 * k+1)).getOwner() == free && hashMap.get(new Coordinate(i - k - 1, j - k -
	 * 1)).getOwner() != free) { System.out.println("loop"); for (int transfer =
	 * hashMap.get(new Coordinate(i, j)).getValue() - 1; transfer > 0;
	 * transfer--) {
	 * 
	 * Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();
	 * 
	 * for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
	 * Coordinate key = new Coordinate(entry.getKey()); GuiCell temp = new
	 * GuiCell(entry.getValue());
	 * 
	 * hashMap2.put(key, temp); }
	 * 
	 * State newState = new State(new HashMap(hashMap2), this, player, level +
	 * 1);
	 * 
	 * newState.getHashMap().get(new Coordinate(i - k, j - k+1)).setOwner(ai);
	 * newState.getHashMap().get(new Coordinate(i - k, j -
	 * k+1)).setValue(transfer); newState.getHashMap().get(new Coordinate(i, j))
	 * .setValue(hashMap.get(new Coordinate(i, j)).getValue() - transfer);
	 * 
	 * states.add(newState); } } } else
	 */

	// if ((i - 1) % 2 == 1) { // i is even, iba minus moo
	// System.out.println("Trigger");
	// if (hashMap.get(new Coordinate(i - k, j - k + 1)).getOwner() == free //
	// left
	// // diag
	// // up
	// && hashMap.get(new Coordinate(i - k - 1, j - k + 1)).getOwner() != free)
	// {
	// System.out.println("Trigger2");
	//
	// for (int transfer = hashMap.get(new Coordinate(i, j)).getValue()
	// - 1; transfer > 0; transfer--) {
	//
	// Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();
	//
	// for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
	// Coordinate key = new Coordinate(entry.getKey());
	// GuiCell temp = new GuiCell(entry.getValue());
	//
	// hashMap2.put(key, temp);
	// }
	//
	// State newState = new State(new HashMap(hashMap2), this, player, level +
	// 1);
	//
	// newState.getHashMap().get(new Coordinate(i - k, j - k + 1)).setOwner(ai);
	// newState.getHashMap().get(new Coordinate(i - k, j - k +
	// 1)).setValue(transfer);
	// newState.getHashMap().get(new Coordinate(i, j))
	// .setValue(hashMap.get(new Coordinate(i, j)).getValue() - transfer);
	//
	// states.add(newState);
	// }
	// }
	// }
	// else if ((i - 1) % 2 == 0) {
	// System.out.println("Trigger");
	// if (hashMap.get(new Coordinate(i - k,
	// j-k+1)).getOwner() == free //left diag up
	// && hashMap.get(new Coordinate(i - k-1,
	// j-k)).getOwner() != free) {
	// System.out.println("Trigger2");
	//
	// for (int transfer = hashMap.get(new Coordinate(i,
	// j)).getValue()
	// - 1; transfer > 0; transfer--) {
	//
	// Map<Coordinate, GuiCell> hashMap2 = new
	// HashMap<Coordinate, GuiCell>();
	//
	// for (Map.Entry<Coordinate, GuiCell> entry :
	// hashMap.entrySet()) {
	// Coordinate key = new Coordinate(entry.getKey());
	// GuiCell temp = new GuiCell(entry.getValue());
	//
	// hashMap2.put(key, temp);
	// }
	//
	// State newState = new State(new HashMap(hashMap2),
	// this, player, level + 1);
	//
	// newState.getHashMap().get(new Coordinate(i - k,
	// j-k+1)).setOwner(ai);
	// newState.getHashMap().get(new Coordinate(i - k,
	// j-k+1)).setValue(transfer);
	// newState.getHashMap().get(new Coordinate(i, j))
	// .setValue(hashMap.get(new Coordinate(i,
	// j)).getValue() - transfer);
	//
	// states.add(newState);
	// }
	// }
	// }

	// for right diagonal down
	// for (int k = 0; k + i < hexgame.BSIZE - 1 && k + j <
	// hexgame.BSIZE - 1; k++) {
	// System.out.println("k: " + k);
	// if (hashMap.get(new Coordinate(i + k, j + k)).getOwner()
	// == free
	// && hashMap.get(new Coordinate(i + k + 1, j + k +
	// 1)).getOwner() != free) {
	// System.out.println("loop");
	// for (int transfer = hashMap.get(new Coordinate(i,
	// j)).getValue()
	// - 1; transfer > 0; transfer--) {
	//
	// Map<Coordinate, GuiCell> hashMap2 = new
	// HashMap<Coordinate, GuiCell>();
	//
	// for (Map.Entry<Coordinate, GuiCell> entry :
	// hashMap.entrySet()) {
	// Coordinate key = new Coordinate(entry.getKey());
	// GuiCell temp = new GuiCell(entry.getValue());
	//
	// hashMap2.put(key, temp);
	// }
	//
	// State newState = new State(new HashMap(hashMap2), this,
	// player, level + 1);
	//
	// newState.getHashMap().get(new Coordinate(i + k, j +
	// k)).setOwner(ai);
	// newState.getHashMap().get(new Coordinate(i + k, j +
	// k)).setValue(transfer);
	// newState.getHashMap().get(new Coordinate(i, j))
	// .setValue(hashMap.get(new Coordinate(i, j)).getValue() -
	// transfer);
	//
	// states.add(newState);
	// }
	// }
	// }

	// for (int k = 0; k < hexgame.BSIZE; k++) {
	// for (int l = 0; l < hexgame.BSIZE; l++) {
	// // 1. left diagonal up 2. left diag down 3. right
	// // diag
	// // up 4. rright diag down 5. horitzontal
	//
	//
	//
	//
	// if (k > 0 && l > 0) {
	// if (hashMap.get(new Coordinate(k, l)).getOwner() == free)
	// {
	// // nasa left diagonal sya
	// if (i - k == j - l && i - k >= 0 && j - l >= 0) {
	// System.out.println("points na pumasok: ");
	// System.out.println("m: " + (k));
	// System.out.println("n: " + (l));
	// for (int m = i, n = j; m > 0 && n > 0; m--) {
	//
	// if (m - 2 > -1 && n - 2 > -1 && m >= l) { // to
	// // avoid
	// // null
	//
	// if (hashMap.get(new Coordinate(m - 1, n - 1)).getOwner()
	// == free
	// && (hashMap.get(new Coordinate(m - 2, n - 2)).getOwner()
	// != free
	// || (m - 2 == 0 || n - 2 == 0))) {
	//
	// // System.out.println("m: "
	// // + (m - 1));
	// // System.out.println("n: "
	// // + (n - 1));
	// for (int transfer = hashMap.get(new Coordinate(i,
	// j)).getValue()
	// - 1; transfer > 0; transfer--) {
	//
	// System.out.println("Transfer 1");
	//
	// Map<Coordinate, GuiCell> hashMap2 = new
	// HashMap<Coordinate, GuiCell>();
	//
	// for (Map.Entry<Coordinate, GuiCell> entry : hashMap
	// .entrySet()) {
	// Coordinate key = new Coordinate(entry.getKey());
	// GuiCell temp = new GuiCell(entry.getValue());
	// // Tab tab =
	// // entry.getValue();
	// // do something
	// // with
	// // key and/or
	// // tab
	// hashMap2.put(key, temp);
	// }
	//
	// State newState = new State(new HashMap(hashMap2), this,
	// player,
	// level + 1);
	//
	// newState.getHashMap().get(new Coordinate(m - 1, n - 1))
	// .setOwner(ai);
	// newState.getHashMap().get(new Coordinate(m - 1, n - 1))
	// .setValue(transfer);
	// newState.getHashMap().get(new Coordinate(i, j))
	// .setValue(hashMap.get(new Coordinate(i, j)).getValue()
	// - transfer);
	//
	// states.add(newState);
	//
	// }
	//
	// /*
	// *
	// * else if (board[n - 2][m -
	// * 2].getOwner() == player
	// * || board[n - 2][n -
	// * 2].getOwner() == ai) {
	// * for (int transfer =
	// * origValue - 1; transfer >
	// * 0; transfer--) {
	// * System.out.
	// * println("Transfer 1" );
	// *
	// * GuiCell[][] tempBoard =
	// * new GuiCell[bSize][bSize]
	// * ;
	// *
	// * for (int copyi = 0; copyi
	// * < board.length; copyi++)
	// * for (int copyj = 0; copyj
	// * < board[copyi].length;
	// * copyj++)
	// * tempBoard[copyi][ copyj]
	// * = board[copyi][copyj];
	// *
	// * tempBoard[m - 1][n - 1] =
	// * new GuiCell(m - 1, n - 1,
	// * transfer, ai);
	// * tempBoard[i][j].
	// * setValue( origValue -
	// * transfer);
	// *
	// * State newState = new
	// * State(tempBoard, this,
	// * player, level + 1);
	// *
	// * states.add(newState); } }
	// */
	//
	// }
	// }
	// n++;
	// }
	// }
	// }
	// }
	//
	// // if (i - k == j - l || k - i == j - l || i - k ==
	// // l - j || k - i == l - j || i == k) {
	// //
	// // }
	//
	// }
	//
	// }

	public int calculateScore() {
		int score = 0;

		return score;
	}
}
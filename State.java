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
		int magicNumber = 1;

		ArrayList<State> states = new ArrayList<State>();

		for (int row = 0; row < hexgame.BSIZE; row++) {
			for (int column = 0; column < hexgame.BSIZE; column++) {

				if (hashMap.get(new Coordinate(row, column)).getOwner() == ai) {

					// left diagonal UP
					if (row % 2 == 1) {
						magicNumber = 0;
					} else if (row % 2 == 0) {
						magicNumber = 1;
					}

					for (int k = row - 1, l = column - magicNumber; k > 1 && l > 1; k = k + 1 - 1) {

						if (k % 2 == 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k - 1, l)).getOwner() != free) {
								for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
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
									newState.getHashMap().get(new Coordinate(row, column))
											.setValue(hashMap.get(new Coordinate(row, column)).getValue() - transfer);

									states.add(newState);
									System.out.println("diagonal left up added");
								}
								break;
							} else {
								k--;
							}
						} else if (k % 2 == 0) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k - 1, l - 1)).getOwner() != free) {
								for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
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
									newState.getHashMap().get(new Coordinate(row, column))
											.setValue(hashMap.get(new Coordinate(row, column)).getValue() - transfer);

									states.add(newState);
									System.out.println("diagonal left up added");

								}
								break;

							} else {
								k--;
								l--;
							}

						} else {
							k--;
						}
					}

					if (row % 2 == 1) {
						magicNumber = 1;
					} else if (row % 2 == 0) {
						magicNumber = 0;
					}

					// right diagonal down
					for (int k = row + 1, l = column + magicNumber; k < hexgame.BSIZE
							&& l < hexgame.BSIZE; k = k + 1 - 1) {

						if (k % 2 == 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k + 1, l + 1)).getOwner() != free) {
								for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
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
									newState.getHashMap().get(new Coordinate(row, column))
											.setValue(hashMap.get(new Coordinate(row, column)).getValue() - transfer);

									states.add(newState);
									System.out.println("diagonal right down added");
								}
								break;

							} else {
								k++;
								l++;
							}
						} else if (k % 2 == 0) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k + 1, l)).getOwner() != free) {
								for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
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
									newState.getHashMap().get(new Coordinate(row, column))
											.setValue(hashMap.get(new Coordinate(row, column)).getValue() - transfer);

									states.add(newState);
									System.out.println("diagonal right down added");

								}
								break;

							} else {
								k++;
							}

						} else {
							k--;// di aabot dito
						}
					}
				}

			}
			System.out.println("Row: " + row);
			System.out.println("loop");

		}

		System.out.println(states.size());
		for (int i = 0; i < states.size(); i++) {
			states.get(i).print();
		}
		return states;

	}

	public int calculateScore() {
		int score = 0;

		return score;
	}
}
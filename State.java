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

		this.hashMap = new HashMap(hashMap);
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

		int sum = 0;

		System.out.println("Compute score eto: ");
		System.out.println("AI: " +aiScore());
		System.out.println("PLayer: " + playerScore());
		if (aiScore() < playerScore()) {
			sum = aiScore();
		}
		else{
			sum = 1;
		}

		score= sum;
		
		childrenLeft = level;
		propagateScore();
	}

	public int aiScore() {
		int sum = 0;
		for (int i = 0; i < hexgame.BSIZE; i++) {
			for (int j = 0; j < hexgame.BSIZE; j++) {
				if (hashMap.get(new Coordinate(i, j)).getOwner() == ai) {
					sum++;
				}
			}
		}
		return sum;
	}

	public int playerScore() {
		int sum = 0;
		for (int i = 0; i < hexgame.BSIZE; i++) {
			for (int j = 0; j < hexgame.BSIZE; j++) {
				if (hashMap.get(new Coordinate(i, j)).getOwner() == player) {
					sum++;
				}
			}
		}
		return sum;
	}

	public void propagateScore() {
		if (parentState != null && childrenLeft == 0)
			parentState.submit(this);
	}

	public void submit(State s) {
		System.out.println("-------------------------------");
		if (currentTurn == player) {
			score = Math.min(score, s.getScore());
		} else {
			score = Math.max(score, s.getScore());
		}
		
		System.out.println("Eto sum bes: " + score);

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

		System.out.println();

		for (int i = 0; i < hexgame.BSIZE; i++) {
			for (int j = 0; j < hexgame.BSIZE; j++) {
				System.out.print(hashMap.get(new Coordinate(i, j)).getValue());

				System.out.print(" | ");

			}
			System.out.println();
		}

		System.out.println();
		System.out.println();

	}

	public ArrayList<State> generateStates() {
		int magicNumberSlash = 1;
		int magicNumberBackSlash = 0;

		ArrayList<State> states = new ArrayList<State>();

		if (currentTurn == ai) {

			for (int row = 0; row < hexgame.BSIZE; row++) {
				for (int column = 0; column < hexgame.BSIZE; column++) {

					if (hashMap.get(new Coordinate(row, column)).getOwner() == ai) {

						// // left diagonal UP
						if (row % 2 == 1) {
							magicNumberSlash = 0;
						} else if (row % 2 == 0) {
							magicNumberSlash = 1;
						}

						for (int k = row - 1, l = column - magicNumberSlash; k > 0 && l > 0; k = k + 1 - 1) {

							if (k % 2 == 1) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k - 1, l)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												player, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);
									}
									break;
								} else {
									k--;
								}
							} else if (k % 2 == 0) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k - 1, l - 1)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												player, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);

									}
									break;

								} else {
									k--;
									l--;
								}

							} else {
								k--;
							}
						} // end of left diagonal up

						if (row % 2 == 1) {
							magicNumberSlash = 1;
						} else if (row % 2 == 0) {
							magicNumberSlash = 0;
						}

						// right diagonal down
						for (int k = row + 1, l = column +

								magicNumberSlash; k < hexgame.BSIZE && l < hexgame.BSIZE; k = k + 1 - 1) {

							if (k % 2 == 1) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k + 1, l + 1)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												player, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);
									}
									break;

								} else {
									k++;
									l++;
								}
							} else if (k % 2 == 0) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k + 1, l)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												player, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);

									}
									break;

								} else {
									k++;
								}

							}
						} // right diagonal down

						if (row % 2 == 1) {
							magicNumberBackSlash = 0;
						} else if (row % 2 == 0) {
							magicNumberBackSlash = 1;
						}

						// right diagonal up
						for (int k = row + 1, l = column - magicNumberBackSlash; k < hexgame.BSIZE
								&& l > 0; k = k + 1 - 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else

							if (k % 2 == 1) {
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

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												player, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);
									}
									break;

								} else {
									k++;

								}
							} else if (k % 2 == 0) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k + 1, l - 1)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												player, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);
									}
									break;

								} else {
									k++;
									l--;
								}

							}
						} // right diagonal up

						if (row % 2 == 1) {
							magicNumberBackSlash = 1;
						} else if (row % 2 == 0) {
							magicNumberBackSlash = 0;
						}

						// left diagonal down
						for (int k = row - 1, l = column + magicNumberBackSlash; k > 0
								&& l < hexgame.BSIZE; k = k + 1 - 1) {

							if (k % 2 == 1) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k - 1, l + 1)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												player, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);
									}
									break;

								} else {
									k--;
									l++;

								}
							} else if (k % 2 == 0) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k - 1, l)).getOwner() != free) {

									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												player, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);

									}
									break;

								} else {
									k--;
								}

							}
						} // left diagonal down

						// vertical up
						for (int k = row, l = column - 1; l > 0; k = k + 1 - 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k, l - 1)).getOwner() != free) {
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
								}
								break;

							} else {
								l--;

							}

						} // end of vertical up

						// vertical down
						for (int k = row, l = column + 1; l < hexgame.BSIZE; k = k + 1 - 1) {

							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k, l + 1)).getOwner() != free) {
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
								}
								break;

							} else {
								l++;
							}

						} // vertical down

					} // if loop kung AI hex sya

				} // for loop j closing
			} // for loop i closing
		}

		if (currentTurn == player) {

			for (int row = 0; row < hexgame.BSIZE; row++) {
				for (int column = 0; column < hexgame.BSIZE; column++) {

					if (hashMap.get(new Coordinate(row, column)).getOwner() == player) {

						// // left diagonal UP
						if (row % 2 == 1) {
							magicNumberSlash = 0;
						} else if (row % 2 == 0) {
							magicNumberSlash = 1;
						}

						for (int k = row - 1, l = column - magicNumberSlash; k > 0 && l > 0; k = k + 1 - 1) {

							if (k % 2 == 1) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k - 1, l)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												ai, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);
									}
									break;
								} else {
									k--;
								}
							} else if (k % 2 == 0) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k - 1, l - 1)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												ai, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);

									}
									break;

								} else {
									k--;
									l--;
								}

							} else {
								k--;
							}
						} // end of left diagonal up

						if (row % 2 == 1) {
							magicNumberSlash = 1;
						} else if (row % 2 == 0) {
							magicNumberSlash = 0;
						}

						// right diagonal down
						for (int k = row + 1, l = column +

								magicNumberSlash; k < hexgame.BSIZE && l < hexgame.BSIZE; k = k + 1 - 1) {

							if (k % 2 == 1) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k + 1, l + 1)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												ai, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);
									}
									break;

								} else {
									k++;
									l++;
								}
							} else if (k % 2 == 0) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k + 1, l)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												ai, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);

									}
									break;

								} else {
									k++;
								}

							}
						} // right diagonal down

						if (row % 2 == 1) {
							magicNumberBackSlash = 0;
						} else if (row % 2 == 0) {
							magicNumberBackSlash = 1;
						}

						// right diagonal up
						for (int k = row + 1, l = column - magicNumberBackSlash; k < hexgame.BSIZE
								&& l > 0; k = k + 1 - 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else

							if (k % 2 == 1) {
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

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												ai, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);
									}
									break;

								} else {
									k++;

								}
							} else if (k % 2 == 0) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k + 1, l - 1)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												ai, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);
									}
									break;

								} else {
									k++;
									l--;
								}

							}
						} // right diagonal up

						if (row % 2 == 1) {
							magicNumberBackSlash = 1;
						} else if (row % 2 == 0) {
							magicNumberBackSlash = 0;
						}

						// left diagonal down
						for (int k = row - 1, l = column + magicNumberBackSlash; k > 0
								&& l < hexgame.BSIZE; k = k + 1 - 1) {

							if (k % 2 == 1) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k - 1, l + 1)).getOwner() != free) {
									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												ai, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);
									}
									break;

								} else {
									k--;
									l++;

								}
							} else if (k % 2 == 0) {
								if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
									break;
								} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
										&& hashMap.get(new Coordinate(k - 1, l)).getOwner() != free) {

									for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
											- 1; transfer > 0; transfer--) {

										Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

										for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
											Coordinate key = new Coordinate(entry.getKey());
											GuiCell temp = new GuiCell(entry.getValue());

											hashMap2.put(key, temp);
										}

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this,
												ai, level + 1);

										newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
										newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
										newState.getHashMap().get(new Coordinate(row, column)).setValue(
												hashMap.get(new Coordinate(row, column)).getValue() - transfer);

										states.add(newState);

									}
									break;

								} else {
									k--;
								}

							}
						} // left diagonal down

						// vertical up
						for (int k = row, l = column - 1; l > 0; k = k + 1 - 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k, l - 1)).getOwner() != free) {
								for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
										- 1; transfer > 0; transfer--) {

									Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

									for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
										Coordinate key = new Coordinate(entry.getKey());
										GuiCell temp = new GuiCell(entry.getValue());

										hashMap2.put(key, temp);
									}

									State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, ai,
											level + 1);

									newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
									newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
									newState.getHashMap().get(new Coordinate(row, column))
											.setValue(hashMap.get(new Coordinate(row, column)).getValue() - transfer);

									states.add(newState);
								}
								break;

							} else {
								l--;

							}

						} // end of vertical up

						// vertical down
						for (int k = row, l = column + 1; l < hexgame.BSIZE; k = k + 1 - 1) {

							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k, l + 1)).getOwner() != free) {
								for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
										- 1; transfer > 0; transfer--) {

									Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

									for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
										Coordinate key = new Coordinate(entry.getKey());
										GuiCell temp = new GuiCell(entry.getValue());

										hashMap2.put(key, temp);
									}

									State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, ai,
											level + 1);

									newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
									newState.getHashMap().get(new Coordinate(k, l)).setValue(transfer);
									newState.getHashMap().get(new Coordinate(row, column))
											.setValue(hashMap.get(new Coordinate(row, column)).getValue() - transfer);

									states.add(newState);
								}
								break;

							} else {
								l++;
							}

						} // vertical down

					} // if loop kung player hex sya

				} // for loop j closing
			} // for loop i closing
		}
		return states;

	}

}
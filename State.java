import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

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

	ArrayList<Coordinate> decreasingAICells;

	public Coordinate getBiggestAIStack() {
		Coordinate biggestAIStack = null;

		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() > hashMap.get(biggestAIStack).getValue()
					&& hashMap.get(biggestAIStack).getValue() < 2) {
				biggestAIStack = c;
			}
		}
		return biggestAIStack;

	}

	public State(Map<Coordinate, GuiCell> hashMap, State parent, int nextTurn, int level) {

		this.hashMap = new HashMap(hashMap);
		this.parentState = parent;
		this.currentTurn = nextTurn;
		this.level = level;
		childrenLeft = 1;

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

	private ArrayList<Coordinate> getAICells() {
		ArrayList<Coordinate> aiCells = new ArrayList<>();
		for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
			if (entry.getValue().getOwner() == ai)
				aiCells.add(entry.getKey());
		}
		return aiCells;
	}

	private ArrayList<Coordinate> getPlayerCells() {
		ArrayList<Coordinate> playerCells = new ArrayList<>();
		for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
			if (entry.getValue().getOwner() == player)
				playerCells.add(entry.getKey());
		}
		return playerCells;
	}

	public void computeScore() {

		// int sum = 0;
		//
		// System.out.println("Compute score eto: ");
		// System.out.println("AI: " +aiScore());
		// System.out.println("PLayer: " + playerScore());
		// if (aiScore() < playerScore()) {
		// sum = aiScore();
		// }
		// else{
		// sum = 1;
		// }
		//
		// score= sum;
		//
		// childrenLeft = level;
		// propagateScore();

		// for (Coordinate c : playerCells) {
		// playerSurroundingCells = c.getSurroundingCoordinates();
		// for (Coordinate surroundingCell : playerSurroundingCells) {
		// System.out.println("surrounding: " + surroundingCell.getX() + " " +
		// surroundingCell.getY());
		// if (hashMap.get(surroundingCell).getOwner() == ai) {
		// System.out.println("Found stuff");
		// score++;
		// }
		// }
		// }

		//
		// ArrayList<Coordinate> aiCells = getAICells();
		// ArrayList<Coordinate> playerCells = getPlayerCells();
		// ArrayList<Coordinate> aiSurroundingCells = new ArrayList<>();
		// ArrayList<Coordinate> playerSurroundingCells = new ArrayList<>();
		// // check the 6 cell neighbors in each AI cell
		// for (Coordinate c : aiCells) {
		// aiSurroundingCells = c.getSurroundingCoordinates();
		// for (Coordinate surroundingCell : aiSurroundingCells) {
		// if (hashMap.get(surroundingCell).getOwner() == player &&
		// hashMap.get(c).getValue() > 1)
		// score--;// decrement the score per each neighboring rival
		// // hexagon is found
		// }
		// }

		// for(Coordinate c:playerCells){
		// playerSurroundingCells = c.getSurroundingCoordinates();
		// for(Coordinate surroundingCell: playerSurroundingCells){
		// System.out.println("surrounding: " + surroundingCell.getX() + " " +
		// surroundingCell.getY());
		// if(hashMap.get(surroundingCell).getOwner() == ai){
		// System.out.println("Found stuff");
		// score++;
		// }
		// }
		// }
		//
		// Coordinate biggestPlayerStack = playerCells.get(0);
		// for (Coordinate c : playerCells) {
		// if (hashMap.get(c).getValue() >
		// hashMap.get(biggestPlayerStack).getValue()) {
		// biggestPlayerStack = c;
		//
		// }
		// }
		// for (Coordinate c : playerCells) {
		// }
		//
		// // check for ai stack na pwede palibutan ang biggest stack ng player.
		//
		// // functioncall

		int score = 0;
		ArrayList<Coordinate> aiCells = getAICells();
		ArrayList<Coordinate> playerCells = getPlayerCells();
		ArrayList<Coordinate> aiSurroundingCells = new ArrayList<>();
		ArrayList<Coordinate> playerSurroundingCells = new ArrayList<>();

//		// score of AI
//		if (currentTurn == ai) {
//			for (Coordinate c : playerCells) {
//				for (Coordinate surroundingCell : aiSurroundingCells) {
//					score++;
//				}
//			}
//		}
//		// score of player
//		else {
//			for (Coordinate c : aiCells) {
//				for (Coordinate surroundingCell : playerSurroundingCells) {
//					score++;
//				}
//			}
//		}

		if (currentTurn == ai) {
			int magicNumberSlash = 1;
			int magicNumberBackSlash = 0;

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

							if (hashMap.get(new Coordinate(k, l)).getOwner() == ai) {
								score++;
								break;
							}

							break;
						}

						if (row % 2 == 1) {
							magicNumberSlash = 1;
						} else if (row % 2 == 0) {
							magicNumberSlash = 0;
						}

						// right diagonal down
						for (int k = row + 1, l = column +

								magicNumberSlash; k < hexgame.BSIZE && l < hexgame.BSIZE; k = k + 1 - 1) {

							if (hashMap.get(new Coordinate(k, l)).getOwner() == ai) {
								score++;
								break;
							}

							break;
						}
						// right diagonal down

						if (row % 2 == 1) {
							magicNumberBackSlash = 0;
						} else if (row % 2 == 0) {
							magicNumberBackSlash = 1;
						}

						// right diagonal up
						for (int k = row + 1, l = column - magicNumberBackSlash; k < hexgame.BSIZE
								&& l > 0; k = k + 1 - 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == ai) {
								score++;
								break;

							}
							break;

						}
						// right diagonal up

						if (row % 2 == 1) {
							magicNumberBackSlash = 1;
						} else if (row % 2 == 0) {
							magicNumberBackSlash = 0;
						}

						// left diagonal down
						for (int k = row - 1, l = column + magicNumberBackSlash; k > 0
								&& l < hexgame.BSIZE; k = k + 1 - 1) {

							if (hashMap.get(new Coordinate(k, l)).getOwner() == ai) {
								score++;
								break;
							}

							break;

						} // left diagonal down

						// vertical up
						for (int k = row, l = column - 1; l > 0; k = k + 1 - 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == ai) {
								score++;
								break;
							}

							break;

						} // end of vertical up

						// vertical down
						for (int k = row, l = column + 1; l < hexgame.BSIZE; k = k + 1 - 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == ai) {
								score++;
								break;
							}

							break;

						}

					}
				}
			}
		}
		// score of player
		if (currentTurn == player) {
			int magicNumberSlash = 1;
			int magicNumberBackSlash = 0;

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

							if (hashMap.get(new Coordinate(k, l)).getOwner() == player) {
								score++;
								break;
							}

							break;
						}

						if (row % 2 == 1) {
							magicNumberSlash = 1;
						} else if (row % 2 == 0) {
							magicNumberSlash = 0;
						}

						// right diagonal down
						for (int k = row + 1, l = column +

								magicNumberSlash; k < hexgame.BSIZE && l < hexgame.BSIZE; k = k + 1 - 1) {

							if (hashMap.get(new Coordinate(k, l)).getOwner() == player) {
								score++;
								break;
							}

							break;
						}
						// right diagonal down

						if (row % 2 == 1) {
							magicNumberBackSlash = 0;
						} else if (row % 2 == 0) {
							magicNumberBackSlash = 1;
						}

						// right diagonal up
						for (int k = row + 1, l = column - magicNumberBackSlash; k < hexgame.BSIZE
								&& l > 0; k = k + 1 - 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == player) {
								score++;
								break;

							}
							break;

						}
						// right diagonal up

						if (row % 2 == 1) {
							magicNumberBackSlash = 1;
						} else if (row % 2 == 0) {
							magicNumberBackSlash = 0;
						}

						// left diagonal down
						for (int k = row - 1, l = column + magicNumberBackSlash; k > 0
								&& l < hexgame.BSIZE; k = k + 1 - 1) {

							if (hashMap.get(new Coordinate(k, l)).getOwner() == player) {
								score++;
								break;
							}

							break;

						} // left diagonal down

						// vertical up
						for (int k = row, l = column - 1; l > 0; k = k + 1 - 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == player) {
								score++;
								break;
							}

							break;

						} // end of vertical up

						// vertical down
						for (int k = row, l = column + 1; l < hexgame.BSIZE; k = k + 1 - 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() == player) {
								score++;
								break;
							}

							break;

						}

					}
				}
			}
		}

		System.out.println("Score current: " + score);

		propagateScore();

	}

	public State getAttackingCoordinate() {

		int magicNumberSlash = 1;
		int magicNumberBackSlash = 0;

		for (int row = 0; row < hexgame.BSIZE; row++) {
			for (int column = 0; column < hexgame.BSIZE; column++) {

				if (hashMap.get(new Coordinate(row, column)).getOwner() == player
						&& hashMap.get(new Coordinate(row, column)).getValue() > 1) {

					// // // left diagonal UP
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
									&& (hashMap.get(new Coordinate(k - 1, l)).getOwner() == ai
											&& hashMap.get(new Coordinate(k - 1, l)).getValue() > 1)) {

								State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player,
										level + 1);
								newState.hashMap.get(new Coordinate(row - 1, column - magicNumberSlash)).setOwner(ai);
								newState.hashMap.get(new Coordinate(row - 1, column - magicNumberSlash)).setValue(1);
								newState.hashMap.get(new Coordinate(k - 1, l))
										.setValue(hashMap.get(new Coordinate(k - 1, l)).getValue() - 1);

								return newState;
							} else {
								k--;
							}
						} else if (k % 2 == 0) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& (hashMap.get(new Coordinate(k - 1, l - 1)).getOwner() == ai)
									&& hashMap.get(new Coordinate(k - 1, l - 1)).getValue() > 1) {

								State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player,
										level + 1);
								newState.hashMap.get(new Coordinate(row - 1, column - magicNumberSlash)).setOwner(ai);
								newState.hashMap.get(new Coordinate(row - 1, column - magicNumberSlash)).setValue(1);
								newState.hashMap.get(new Coordinate(k - 1, l - 1))
										.setValue(hashMap.get(new Coordinate(k - 1, l - 1)).getValue() - 1);

								return newState;
							} else {
								k--;
								l--;
							}

						} else {
							k--;
						}
					} // end of left diagonal up

					// // left diagonal down

					if (row % 2 == 1) {
						magicNumberSlash = 1;
					} else if (row % 2 == 0) {
						magicNumberSlash = 0;
					}

					// right diagonal down
					for (int k = row + 1, l = column + magicNumberSlash; k < hexgame.BSIZE
							&& l < hexgame.BSIZE; k = k + 1 - 1) {

						if (k % 2 == 1) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& (hashMap.get(new Coordinate(k + 1, l + 1)).getOwner() == ai
											&& hashMap.get(new Coordinate(k + 1, l + 1)).getValue() > 1)) {

								State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player,
										level + 1);
								newState.hashMap.get(new Coordinate(row + 1, column + magicNumberSlash)).setOwner(ai);
								newState.hashMap.get(new Coordinate(row + 1, column + magicNumberSlash)).setValue(1);
								newState.hashMap.get(new Coordinate(k + 1, l + 1))
										.setValue(hashMap.get(new Coordinate(k + 1, l + 1)).getValue() - 1);

								return newState;

							} else {
								k++;
								l++;
							}
						} else if (k % 2 == 0) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& (hashMap.get(new Coordinate(k + 1, l)).getOwner() == ai
											&& hashMap.get(new Coordinate(k + 1, l)).getValue() > 1)) {

								State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player,
										level + 1);
								newState.hashMap.get(new Coordinate(row + 1, column + magicNumberSlash)).setOwner(ai);
								newState.hashMap.get(new Coordinate(row + 1, column + magicNumberSlash)).setValue(1);
								newState.hashMap.get(new Coordinate(k + 1, l))
										.setValue(hashMap.get(new Coordinate(k + 1, l)).getValue() - 1);

								return newState;

							} else {
								k++;
							}

						}
					} // right diagonal down

					System.out.println("traval");

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
									&& (hashMap.get(new Coordinate(k + 1, l)).getOwner() == ai
											&& hashMap.get(new Coordinate(k + 1, l)).getValue() > 1)) {

								State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player,
										level + 1);
								newState.hashMap.get(new Coordinate(row + 1, column - magicNumberBackSlash))
										.setOwner(ai);
								newState.hashMap.get(new Coordinate(row + 1, column - magicNumberBackSlash))
										.setValue(1);
								newState.hashMap.get(new Coordinate(k + 1, l))
										.setValue(hashMap.get(new Coordinate(k + 1, l)).getValue() - 1);

								return newState;

							} else {
								k++;

							}
						} else if (k % 2 == 0) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& (hashMap.get(new Coordinate(k + 1, l - 1)).getOwner() == ai
											&& hashMap.get(new Coordinate(k + 1, l - 1)).getValue() > 1)) {

								State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player,
										level + 1);
								newState.hashMap.get(new Coordinate(row + 1, column - magicNumberBackSlash))
										.setOwner(ai);
								newState.hashMap.get(new Coordinate(row + 1, column - magicNumberBackSlash))
										.setValue(1);
								newState.hashMap.get(new Coordinate(k + 1, l - 1))
										.setValue(hashMap.get(new Coordinate(k + 1, l - 1)).getValue() - 1);

								return newState;

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
									&& (hashMap.get(new Coordinate(k - 1, l + 1)).getOwner() == ai
											&& hashMap.get(new Coordinate(k - 1, l + 1)).getValue() > 1)) {

								State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player,
										level + 1);
								newState.hashMap.get(new Coordinate(row - 1, column + magicNumberBackSlash))
										.setOwner(ai);
								newState.hashMap.get(new Coordinate(row - 1, column + magicNumberBackSlash))
										.setValue(1);
								newState.hashMap.get(new Coordinate(k - 1, l + 1))
										.setValue(hashMap.get(new Coordinate(k - 1, l + 1)).getValue() - 1);

								return newState;

							} else {
								k--;
								l++;

							}
						} else if (k % 2 == 0) {
							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& (hashMap.get(new Coordinate(k - 1, l)).getOwner() == ai
											&& hashMap.get(new Coordinate(k - 1, l)).getValue() > 1)) {

								State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player,
										level + 1);
								newState.hashMap.get(new Coordinate(row - 1, column + magicNumberBackSlash))
										.setOwner(ai);
								newState.hashMap.get(new Coordinate(row - 1, column + magicNumberBackSlash))
										.setValue(1);
								newState.hashMap.get(new Coordinate(k - 1, l))
										.setValue(hashMap.get(new Coordinate(k - 1, l)).getValue() - 1);

								return newState;

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
								&& (hashMap.get(new Coordinate(k, l - 1)).getOwner() == ai
										&& hashMap.get(new Coordinate(k, l - 1)).getValue() > 1)) {

							State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player,
									level + 1);
							newState.hashMap.get(new Coordinate(row, column - 1)).setOwner(ai);
							newState.hashMap.get(new Coordinate(row, column - 1)).setValue(1);
							newState.hashMap.get(new Coordinate(k, l - 1))
									.setValue(hashMap.get(new Coordinate(k, l - 1)).getValue() - 1);

							return newState;

						} else {
							l--;

						}

					} // end of vertical up

					// vertical down
					for (int k = row, l = column + 1; l < hexgame.BSIZE; k = k + 1 - 1) {

						if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
							break;
						} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
								&& (hashMap.get(new Coordinate(k, l + 1)).getOwner() == ai
										&& hashMap.get(new Coordinate(k, l + 1)).getValue() > 1)) {
							State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player,
									level + 1);
							newState.hashMap.get(new Coordinate(row, column + 1)).setOwner(ai);
							newState.hashMap.get(new Coordinate(row, column + 1)).setValue(1);
							newState.hashMap.get(new Coordinate(k, l + 1))
									.setValue(hashMap.get(new Coordinate(k, l + 1)).getValue() - 1);

							return newState;
						} else {
							l++;
						}

					} // vertical down

				}
			}

		}

		// return randomRightDiagonalUp();

		// insert random

		decreasingAICells = getAICells();

		ArrayList<Integer> visitedRandom = new ArrayList<Integer>();
		Random rand = new Random();

		int value = 1 + rand.nextInt((6 - 1) + 1);

		State randomState;

		Boolean isFound = false;
		while (!isFound && !visitedRandom.contains(value) && decreasingAICells.size() > 0) {

//			System.out.println("Random");
//			System.out.println("Decreasing size: " + decreasingAICells.size());
//			System.out.println("size of visited: " + visitedRandom.size());
//
//			System.out.println("Value:  " + value);

			switch (value) {
			case 1:
				randomState = randomLeftDiagonalUp();

				if (randomState != null) {
					return randomState;
				}

				visitedRandom.add(value);
//				System.out.println();
//				System.out.println("else sa: " + value);
//				System.out.println();
				break;

			case 2:
				randomState = randomRightDiagonalDown();

				if (randomState != null) {
					return randomState;
				}

				visitedRandom.add(value);
//				System.out.println();
//				System.out.println("else sa: " + value);
//				System.out.println();
				break;
			case 3:
				randomState = randomRightDiagonalUp();

				if (randomState != null) {
					return randomState;
				}

				visitedRandom.add(value);
//				System.out.println();
//				System.out.println("else sa: " + value);
//				System.out.println();
				break;
			case 4:
				randomState = randomLeftDiagonalDown();

				if (randomState != null) {
					return randomState;
				}

				visitedRandom.add(value);
//				System.out.println();
//				System.out.println("else sa: " + value);
//				System.out.println();
				break;

			case 5:
				randomState = randomUp();

				if (randomState != null) {
					return randomState;
				}
				visitedRandom.add(value);
//				System.out.println();
//				System.out.println("else sa: " + value);
//				System.out.println();
				break;
			case 6:
				randomState = randomDown();

				if (randomState != null) {
					return randomState;
				}

				visitedRandom.add(value);
//				System.out.println();
//				System.out.println("else sa: " + value);
//				System.out.println();
				break;
			}

			rand = new Random();

			if (visitedRandom.size() == 6) {
				decreasingAICells.remove(getBiggestAIStack());

				for (int i = 0; i < decreasingAICells.size(); i++) {
					if (hashMap.get(decreasingAICells.get(i)).getValue() == 1) {
						decreasingAICells.remove(i);
					}
				}
				visitedRandom = new ArrayList<Integer>();
			}
		}

		return null;

	}

	public State randomLeftDiagonalUp() {

		int magicNumberSlash = 1;
		int magicNumberBackSlash = 0;

		Coordinate biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() > hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		int row = biggestAIStack.getX();
		int column = biggestAIStack.getY();

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

					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;
				} else {
					k--;
				}
			} else if (k % 2 == 0) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
					break;
				} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k - 1, l - 1)).getOwner() != free) {

					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k--;
					l--;
				}

			} else {
				k--;
			}
		} // end of left diagonal up

		///////////////////////////////////////////////////

		magicNumberSlash = 1;
		magicNumberBackSlash = 0;

		biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() >= hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		row = biggestAIStack.getX();
		column = biggestAIStack.getY();

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

					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;
				} else {
					k--;
				}
			} else if (k % 2 == 0) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
					break;
				} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k - 1, l - 1)).getOwner() != free) {

					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k--;
					l--;
				}

			} else {
				k--;
			}
		} // end of left diagonal up

		return null;

	}

	public State randomRightDiagonalDown() {

		int magicNumberSlash = 1;
		int magicNumberBackSlash = 0;

		Coordinate biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() > hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		int row = biggestAIStack.getX();
		int column = biggestAIStack.getY();

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
					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k++;
					l++;
				}
			} else if (k % 2 == 0) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
					break;
				} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k + 1, l)).getOwner() != free) {
					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;
				} else {
					k++;
				}

			}
		} // right diagonal down

		/////////////////

		magicNumberSlash = 1;
		magicNumberBackSlash = 0;

		biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() >= hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		row = biggestAIStack.getX();
		column = biggestAIStack.getY();

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
					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k++;
					l++;
				}
			} else if (k % 2 == 0) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
					break;
				} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k + 1, l)).getOwner() != free) {
					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;
				} else {
					k++;
				}

			}
		} // right diagonal down

		return null;

	}

	public State randomRightDiagonalUp() {

		int magicNumberSlash = 1;
		int magicNumberBackSlash = 0;

		Coordinate biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() > hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		int row = biggestAIStack.getX();
		int column = biggestAIStack.getY();

		if (row % 2 == 1) {
			magicNumberBackSlash = 0;
		} else if (row % 2 == 0) {
			magicNumberBackSlash = 1;
		}

		// right diagonal up
		for (int k = row + 1, l = column - magicNumberBackSlash; k < hexgame.BSIZE && l > 0; k = k + 1 - 1) {
			if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
				break;
			} else

			if (k % 2 == 1) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k + 1, l)).getOwner() != free) {
					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k++;

				}
			} else if (k % 2 == 0) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
					break;
				} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k + 1, l - 1)).getOwner() != free) {
					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k++;
					l--;
				}

			}
		} // right diagonal up

		/////////////////////////////

		magicNumberSlash = 1;
		magicNumberBackSlash = 0;

		biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() >= hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		row = biggestAIStack.getX();
		column = biggestAIStack.getY();

		if (row % 2 == 1) {
			magicNumberBackSlash = 0;
		} else if (row % 2 == 0) {
			magicNumberBackSlash = 1;
		}

		// right diagonal up
		for (int k = row + 1, l = column - magicNumberBackSlash; k < hexgame.BSIZE && l > 0; k = k + 1 - 1) {
			if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
				break;
			} else

			if (k % 2 == 1) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k + 1, l)).getOwner() != free) {
					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k++;

				}
			} else if (k % 2 == 0) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
					break;
				} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k + 1, l - 1)).getOwner() != free) {
					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k++;
					l--;
				}

			}
		} // right diagonal up

		return null;
	}

	public State randomLeftDiagonalDown() {

		int magicNumberSlash = 1;
		int magicNumberBackSlash = 0;

		Coordinate biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() > hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		int row = biggestAIStack.getX();
		int column = biggestAIStack.getY();

		if (row % 2 == 1) {
			magicNumberBackSlash = 1;
		} else if (row % 2 == 0) {
			magicNumberBackSlash = 0;
		}

		// left diagonal down
		for (int k = row - 1, l = column + magicNumberBackSlash; k > 0 && l < hexgame.BSIZE; k = k + 1 - 1) {

			if (k % 2 == 1) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
					break;
				} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k - 1, l + 1)).getOwner() != free) {
					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k--;
					l++;

				}
			} else if (k % 2 == 0) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
					break;
				} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k - 1, l)).getOwner() != free) {

					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k--;
				}

			}
		} // left diagonal down

		///////////////////////

		magicNumberSlash = 1;
		magicNumberBackSlash = 0;

		biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() >= hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		row = biggestAIStack.getX();
		column = biggestAIStack.getY();

		if (row % 2 == 1) {
			magicNumberBackSlash = 1;
		} else if (row % 2 == 0) {
			magicNumberBackSlash = 0;
		}

		// left diagonal down
		for (int k = row - 1, l = column + magicNumberBackSlash; k > 0 && l < hexgame.BSIZE; k = k + 1 - 1) {

			if (k % 2 == 1) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
					break;
				} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k - 1, l + 1)).getOwner() != free) {
					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k--;
					l++;

				}
			} else if (k % 2 == 0) {
				if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
					break;
				} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
						&& hashMap.get(new Coordinate(k - 1, l)).getOwner() != free) {

					State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
					int temp = hashMap.get(new Coordinate(row, column)).getValue();
					newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
					newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
					newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

					return newState;

				} else {
					k--;
				}

			}
		} // left diagonal down

		return null;
	}

	public State randomUp() {

		Coordinate biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() > hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		int row = biggestAIStack.getX();
		int column = biggestAIStack.getY();

		// vertical up
		for (int k = row, l = column - 1; l > 0; k = k + 1 - 1) {
			if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
				break;
			} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
					&& hashMap.get(new Coordinate(k, l - 1)).getOwner() != free) {
				State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
				int temp = hashMap.get(new Coordinate(row, column)).getValue();
				newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
				newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
				newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

				return newState;

			} else {
				l--;

			}
			/*
			 * <<<<<<< HEAD } else if (k % 2 == 0) { if (hashMap.get(new
			 * Coordinate(k, l)).getOwner() == free && hashMap.get(new
			 * Coordinate(k - 1, l - 1)).getOwner() != free) { for (int transfer
			 * = hashMap.get(new Coordinate(row, column)).getValue() - 1;
			 * transfer > 0; transfer--) { =======
			 */

		} // end of vertical up

		//////////////////////

		biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() >= hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		row = biggestAIStack.getX();
		column = biggestAIStack.getY();

		// vertical up
		for (int k = row, l = column - 1; l > 0; k = k + 1 - 1) {
			if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
				break;
			} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
					&& hashMap.get(new Coordinate(k, l - 1)).getOwner() != free) {
				State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
				int temp = hashMap.get(new Coordinate(row, column)).getValue();
				newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
				newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
				newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

				return newState;

			} else {
				l--;

			}
		}
		return null;
	}

	public State randomDown() {

		Coordinate biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() > hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		int row = biggestAIStack.getX();
		int column = biggestAIStack.getY();

		// vertical down
		for (int k = row, l = column + 1; l < hexgame.BSIZE; k = k + 1 - 1) {

			if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
				break;
			} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
					&& hashMap.get(new Coordinate(k, l + 1)).getOwner() != free) {
				State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
				int temp = hashMap.get(new Coordinate(row, column)).getValue();
				newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
				newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
				newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

				return newState;

			} else {
				l++;
			}

		} // vertical down

		/////////////////////////////

		biggestAIStack = null;
		for (Coordinate c : decreasingAICells) {
			if (biggestAIStack == null)
				biggestAIStack = c;
			else if (hashMap.get(c).getValue() >= hashMap.get(biggestAIStack).getValue()) {
				biggestAIStack = c;
			}
		}

		row = biggestAIStack.getX();
		column = biggestAIStack.getY();

		// vertical down
		for (int k = row, l = column + 1; l < hexgame.BSIZE; k = k + 1 - 1) {

			if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
				break;
			} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
					&& hashMap.get(new Coordinate(k, l + 1)).getOwner() != free) {
				State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap), this, player, level + 1);
				int temp = hashMap.get(new Coordinate(row, column)).getValue();
				newState.getHashMap().get(new Coordinate(k, l)).setOwner(ai);
				newState.getHashMap().get(new Coordinate(k, l)).setValue(temp / 2);
				newState.getHashMap().get(new Coordinate(row, column)).setValue(temp - (temp / 2));

				return newState;

			} else {
				l++;
			}

		} // vertical down
		return null;
	}

	public void propagateScore() {
		// if (parentState != null && level== 2){
		// parentState.submit(this);
		// System.out.println("------------------------------PLEASE-----------------------------------------------------------------");
		//
		// }

		if (parentState != null) {
			parentState.submit(this);
		}
		
	}

	public void submit(State s) {
		if (currentTurn == player) {
			score = Math.min(score, s.getScore());
		} else {
			score = Math.max(score, s.getScore());
		}

//		System.out.println("Eto sum bes: " + score);

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

//		System.out.println();
//
//		for (int i = 0; i < hexgame.BSIZE; i++) {
//			for (int j = 0; j < hexgame.BSIZE; j++) {
//				System.out.print(hashMap.get(new Coordinate(i, j)).getValue());
//
//				System.out.print(" | ");
//
//			}
//			System.out.println();
//		}
//
//		System.out.println();
//		System.out.println();

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

										/*
										 * <<<<<<< HEAD for (int k = row - 1, l
										 * = column - 1; k >= 1 && l >=1; k = k
										 * + 1 - 1) { //
										 * System.out.println("K: " + k); if (k
										 * % 2 == 1) { if (hashMap.get(new
										 * Coordinate(k, l)).getOwner() == free
										 * && hashMap.get(new Coordinate(k - 1,
										 * l)).getOwner() != free) { for (int
										 * transfer = hashMap.get(new
										 * Coordinate(row, column)).getValue() -
										 * 1; transfer > 0; transfer--) {
										 * =======
										 */
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
									// >>>>>>> master

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
							/*
							 * <<<<<<< HEAD } else if (k % 2 == 0) { if
							 * (hashMap.get(new Coordinate(k, l)).getOwner() ==
							 * free && hashMap.get(new Coordinate(k - 1, l -
							 * 1)).getOwner() != free) { for (int transfer =
							 * hashMap.get(new Coordinate(row,
							 * column)).getValue() - 1; transfer > 0;
							 * transfer--) { =======
							 */

						} // end of vertical up

						// vertical down
						for (int k = row, l = column + 1; l < hexgame.BSIZE; k = k + 1 - 1) {

							if (hashMap.get(new Coordinate(k, l)).getOwner() != free) {
								break;
							} else if (hashMap.get(new Coordinate(k, l)).getOwner() == free
									&& hashMap.get(new Coordinate(k, l + 1)).getOwner() != free) {
								for (int transfer = hashMap.get(new Coordinate(row, column)).getValue()
										- 1; transfer > 0; transfer--) {
									// >>>>>>> master

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

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, ai,
												level + 1);

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

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, ai,
												level + 1);

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

						/*
						 * <<<<<<< HEAD } } =======
						 */
						// right diagonal down
						for (int k = row + 1, l = column +
						// >>>>>>> master

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

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, ai,
												level + 1);

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

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, ai,
												level + 1);

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

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, ai,
												level + 1);

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

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, ai,
												level + 1);

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

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, ai,
												level + 1);

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

										State newState = new State(new HashMap<Coordinate, GuiCell>(hashMap2), this, ai,
												level + 1);

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
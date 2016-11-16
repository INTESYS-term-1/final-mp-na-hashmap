import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.miginfocom.swing.MigLayout;

/**********************************
 * This is the main class of a Java program to play a game based on hexagonal
 * tiles. The mechanism of handling hexes is in the file hexmech.java.
 *
 * Written by: M.H. Date: December 2012
 *
 ***********************************/

public class hexgame {
	private hexgame() {
		initGame();
		createAndShowGUI();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new hexgame();
			}
		});
	}

	// constants and global variables
	final static Color COLOURBACK = Color.WHITE;
	final static Color COLOURCELL = Color.DARK_GRAY;
	final static Color COLOURGRID = Color.BLACK;
	final static Color COLOURONE = Color.green;
	final static Color COLOURONETXT = Color.WHITE;
	final static Color COLOURTWO = Color.RED;
	final static Color COLOURTWOTXT = Color.WHITE;

	final static Color COLOURTHREE = Color.lightGray;
	final static Color COLOURTHREETXT = Color.WHITE;

	final static Color COLOURFOUR = Color.BLACK;
	final static Color COLOURFOURTXT = Color.BLACK;

	// for cell direction
	final static int UP = 0;
	final static int DOWN = 1;
	final static int LEFT_UP = 2;
	final static int LEFT_DOWN = 3;
	final static int RIGHT_UP = 4;
	final static int RIGHT_DOWN = 5;




	// final static Color COLOURTWO = new Color(0,0,0,200);

	final static int EMPTY = 1;
	final static int BSIZE = 8; // board size.
	final static int HEXSIZE = 75; // hex size in pixels
	final static int BORDERS = 15;
	final static int SCRSIZE = HEXSIZE * (BSIZE + 1) + BORDERS * 3; // screen
	// size
	// (vertical
	// dimension).

	// variables natin
	JSplitPane splitPane;
	JPanel lblSheepsPerPlaye = new JPanel();

	int numberOfSheepsPerPlayer = 0;
	Boolean isHolding = false;
	int holding = 0;

	static int player = -1;
	static int free = 0;
	static int ai = 1;
	static int wall = -99;

	Map<Coordinate, GuiCell> hashMap = new HashMap<Coordinate, GuiCell>();

	static GuiCell[][] board = new GuiCell[BSIZE][BSIZE];
	private final JLabel lblBattlesheepDashboard = new JLabel("BattleSheep Dashboard");
	private final JLabel lblSheepsPlayer = new JLabel("Sheeps per player:");
	private final JLabel lblSheepsPerPlayer = new JLabel("n");
	private final JLabel lblSheepAtHand = new JLabel("Sheep at hand:");
	private final JLabel lblFromHexX = new JLabel("From hex x coord");
	private final JLabel lblFromHexY = new JLabel("From hex y coord");
	private final JLabel lblxcoord = new JLabel("New label");
	private final JLabel lblycoord = new JLabel("New label");
	private final JButton btnDone = new JButton("Done");

	void initGame() {

		hexmech.setXYasVertex(false); // RECOMMENDED: leave this as FALSE.

		hexmech.setHeight(HEXSIZE); // Either setHeight or setSize must be run
		// to initialize the hex
		hexmech.setBorders(BORDERS);

		for (int i = 0; i < BSIZE; i++) {
			for (int j = 0; j < BSIZE; j++) {
				if (i == 0) {
					GuiCell guiCell = new GuiCell(0, wall);
					Coordinate coordinate = new Coordinate(i, j);
					hashMap.put(coordinate, guiCell);
				} else if (j == 0) {
					GuiCell guiCell = new GuiCell(0, wall);
					Coordinate coordinate = new Coordinate(i, j);
					hashMap.put(coordinate, guiCell);
				}

				else if (j == BSIZE - 1) {
					GuiCell guiCell = new GuiCell(0, wall);
					Coordinate coordinate = new Coordinate(i, j);
					hashMap.put(coordinate, guiCell);

				} else if (i == BSIZE - 1) {
					GuiCell guiCell = new GuiCell(0, wall);
					Coordinate coordinate = new Coordinate(i, j);
					hashMap.put(coordinate, guiCell);

				} else {
					GuiCell guiCell = new GuiCell(0, free);
					Coordinate coordinate = new Coordinate(i, j);
					hashMap.put(coordinate, guiCell);
				}

			}
		}

		// for ( Map.Entry<String, Tab> entry : hash.entrySet()) {
		// String key = entry.getKey();
		// Tab tab = entry.getValue();
		// // do something with key and/or tab
		// }

		updateBoard();

		initializeSheeps();

		// initialize player's sheeps

		// set up board here
		// even x point of reference
		// board[0][1] = 'B';
		// board[1][0] = 'A';
		// board[1][1] = 'C';
		// board[2][1] = 'D';
		// //odd
		// board[1][2] = 'B';
		// board[2][3] = 'A';
		// board[2][2] = 'C';
		// board[3][2] = 'D';

		// displayBoardConsole();
		Map<Coordinate, GuiCell> hashMap2 = new HashMap<Coordinate, GuiCell>();

		for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
			Coordinate key = new Coordinate(entry.getKey());
			GuiCell temp = new GuiCell(entry.getValue());
			// Tab tab = entry.getValue();
			// do something with key and/or tab
			hashMap2.put(key, temp);
		}

		;
		State state = new State(new HashMap(hashMap2), null, ai, 0);
		ArrayList<State> states = state.generateStates();



//		System.out.println("----------------------------");
//		for (int i = 0; i < states.size(); i++) {
//			states.get(i).print();
//		}

//		hashMap = states.get(states.size()-1).getHashMap();
		updateBoard();
		// displayBoardConsole();
	}

	public void displayBoardConsole() {
		// Collection c = hashMap.values();
		// Iterator itr = c.iterator();
		//
		// for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
		//
		// entry.getKey().print();
		// entry.getValue().print();
		//
		// // Tab tab = entry.getValue();
		// // do something with key and/or tab
		// }

		for (int i = 0; i < hexgame.BSIZE; i++) {
			for (int j = 0; j < hexgame.BSIZE; j++) {
				System.out.print(hashMap.get(new Coordinate(i, j)).getValue());
				System.out.print(" | ");

				if (hashMap.get(new Coordinate(i, j)).getValue() == 6) {
					System.out.println(i);
					System.out.println(j);
				}
			}
			System.out.println();
		}

		System.out.println();
		System.out.println();
	}

	public void updateBoard() {
		Collection c = hashMap.values();
		Iterator itr = c.iterator();

		for (Map.Entry<Coordinate, GuiCell> entry : hashMap.entrySet()) {
			Coordinate key = entry.getKey();
			board[key.getX()][key.getY()] = entry.getValue();
			// Tab tab = entry.getValue();
			// do something with key and/or tab

		}

	}

	public void initializeSheeps() {
		// initialize sheep
		numberOfSheepsPerPlayer = Integer
				.parseInt(JOptionPane.showInputDialog(null, "Total number of sheeps per player", "Welcome", 2));
		lblSheepsPerPlayer.setText(Integer.toString(numberOfSheepsPerPlayer));

		initializeAISheeps();
		initializePlayerSheeps();
		updateBoard();

	}

	public void initializeAISheeps() {
		hashMap.put(new Coordinate(6,5), new GuiCell(numberOfSheepsPerPlayer, ai));
		hashMap.put(new Coordinate(1,2), new GuiCell(numberOfSheepsPerPlayer, ai));


	}

	public void initializePlayerSheeps() {
		int initialX = Integer.parseInt(
				JOptionPane.showInputDialog(null, "X coordinate:", "Where do you want to put all your sheep?", 2));
		int initialY = Integer.parseInt(
				JOptionPane.showInputDialog(null, "Y coordinate:", "Where do you want to put all your sheep?", 2));

		hashMap.put(new Coordinate(initialX, initialY), new GuiCell(numberOfSheepsPerPlayer, player));

	}

	private void createAndShowGUI() {
		DrawingPanel panel = new DrawingPanel();

		// JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Hex Testing 4");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(panel),
				new JScrollPane(lblSheepsPerPlaye));
		lblSheepsPerPlaye.setLayout(new MigLayout("", "[][][]", "[][][][][][][][][][]"));
		lblBattlesheepDashboard.setFont(new Font("Tahoma", Font.BOLD, 13));

		lblSheepsPerPlaye.add(lblBattlesheepDashboard, "cell 0 0");

		lblSheepsPerPlaye.add(lblSheepsPlayer, "cell 0 1");

		lblSheepsPerPlaye.add(lblSheepsPerPlayer, "cell 1 1");

		lblSheepsPerPlaye.add(lblSheepAtHand, "cell 0 3");

		lblSheepsPerPlaye.add(lblFromHexX, "cell 0 4");

		lblSheepsPerPlaye.add(lblxcoord, "cell 1 4");

		lblSheepsPerPlaye.add(lblFromHexY, "cell 0 5");

		lblSheepsPerPlaye.add(lblycoord, "cell 1 5,alignx left");

		lblSheepsPerPlaye.add(btnDone, "cell 0 9,alignx center");
		splitPane.setDividerLocation(550);

		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("click");
			}
		});

		Container content = frame.getContentPane();
		content.add(splitPane);
		// this.add(panel); -- cannot be done in a static context
		// for hexes in the FLAT orientation, the height of a 10x10 grid is
		// 1.1764 * the width. (from h / (s+t))
		frame.setSize((int) (SCRSIZE / .9), SCRSIZE);

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	class DrawingPanel extends JPanel {
		// mouse variables here
		// Point mPt = new Point(0,0);

		public DrawingPanel() {
			setBackground(COLOURBACK);

			MyMouseListener ml = new MyMouseListener();
			addMouseListener(ml);
		}

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setFont(new Font("Consolas", Font.BOLD, 20));
			super.paintComponent(g2);
			// draw grid
			for (int i = 0; i < BSIZE; i++) {
				for (int j = 0; j < BSIZE; j++) {
					hexmech.drawHex(i, j, g2);
				}
			}
			// fill in hexes
			for (int i = 0; i < BSIZE; i++) {
				for (int j = 0; j < BSIZE; j++) {
					// if (board[i][j] < 0)
					// hexmech.fillHex(i,j,COLOURONE,-board[i][j],g2);
					// if (board[i][j] > 0) hexmech.fillHex(i,j,COLOURTWO,
					// board[i][j],g2);

					hexmech.fillHex(i, j, Integer.toString(board[i][j].getValue())+ " "+ Integer.toString(i) + Integer.toString(j), g2);
				}
			}

			// g2.setColor(Color.RED);
			// g.drawLine(mPt.x,mPt.y, mPt.x,mPt.y);
		}

		class MyMouseListener extends MouseAdapter { // inner class inside
			// DrawingPanel
			private boolean isAtEnd(Coordinate oldPoint, Coordinate newPoint, int direction){
				switch(direction){
					case DOWN:
						do{ // down
							oldPoint.goDown();
							if(!hashMap.containsKey(oldPoint))
								break;
						}while(hashMap.get(oldPoint).getOwner() == free);
						oldPoint.goUp();
						if(oldPoint.equals(newPoint))
							return true;
						break;
					case UP:
						do{// going up
							oldPoint.goUp();
							if(!hashMap.containsKey(oldPoint))
								break;
						}while(hashMap.get(oldPoint).getOwner() ==  free);
						oldPoint.goDown();
						if(oldPoint.equals(newPoint))
							return true;
						break;
					case LEFT_DOWN:
						do{// lower left diagonal
							oldPoint.goLeftDown();
							if(!hashMap.containsKey(oldPoint))
								break;
						}while(hashMap.get(oldPoint).getOwner() ==  free);
						oldPoint.goRightUp();
						if(oldPoint.equals(newPoint))
							return true;
						break;
					case LEFT_UP:
						do{// upper left diagonal
							oldPoint.goLeftUp();
							if(!hashMap.containsKey(oldPoint))
								break;
						}while(hashMap.get(oldPoint).getOwner() ==  free);
						oldPoint.goRightDown();
						if(oldPoint.equals(newPoint))
							return true;
						break;
					case RIGHT_UP:
						do{//upper right diagonal
							oldPoint.goRightUp();
							if(!hashMap.containsKey(oldPoint))
								break;
						}while(hashMap.get(oldPoint).getOwner() ==  free);
						oldPoint.goLeftDown();

						if(oldPoint.equals(newPoint))
							return true;
						break;
					case RIGHT_DOWN:
						do{//lower right diagonal
							oldPoint.goRightDown();
							if(!hashMap.containsKey(oldPoint))
								break;
						}while(hashMap.get(oldPoint).getOwner() ==  free);
						oldPoint.goLeftUp();
						if(oldPoint.equals(newPoint))
							return true;
						break;
				}
				return false;
			}
			private boolean validPlace(Coordinate old, Coordinate newPoint){
				Coordinate oldPoint = new Coordinate(old.getX(), old.getY());

				do{ // down
					oldPoint.goDown();
					if(oldPoint.equals(newPoint) && isAtEnd(oldPoint, newPoint, DOWN)) {
						return true;
					}
					if(!hashMap.containsKey(oldPoint))
						break;
				}while(hashMap.get(oldPoint).getOwner() == free);

				oldPoint = new Coordinate(old.getX(), old.getY());

				do{//lower right diagonal
					oldPoint.goRightDown();
					if(oldPoint.equals(newPoint) && isAtEnd(oldPoint, newPoint, RIGHT_DOWN)) {
						return true;
					}
					if(!hashMap.containsKey(oldPoint))
						break;
				}while(hashMap.get(oldPoint).getOwner() ==  free);

				oldPoint = new Coordinate(old.getX(), old.getY());

				do{//upper right diagonal
					oldPoint.goRightUp();
					if(oldPoint.equals(newPoint) && isAtEnd(oldPoint, newPoint, RIGHT_UP)) {
						return true;
					}
					if(!hashMap.containsKey(oldPoint))
						break;
				}while(hashMap.get(oldPoint).getOwner() ==  free);

				oldPoint = new Coordinate(old.getX(), old.getY());

				do{// going up
					oldPoint.goUp();
					if(oldPoint.equals(newPoint) && isAtEnd(oldPoint, newPoint, UP)) {
						return true;
					}
					if(!hashMap.containsKey(oldPoint))
						break;
				}while(hashMap.get(oldPoint).getOwner() ==  free);

				oldPoint = new Coordinate(old.getX(), old.getY());

				do{// upper left diagonal
					oldPoint.goLeftUp();
					if(oldPoint.equals(newPoint) && isAtEnd(oldPoint, newPoint, LEFT_UP)){
						return true;
					}
					if(!hashMap.containsKey(oldPoint))
						break;
				}while(hashMap.get(oldPoint).getOwner() ==  free);

				oldPoint = new Coordinate(old.getX(), old.getY());

				do {// lower left diagonal
					oldPoint.goLeftDown();
					if(oldPoint.equals(newPoint) && isAtEnd(oldPoint, newPoint, LEFT_DOWN)){

						return true;
					}
					if(!hashMap.containsKey(oldPoint))
						break;
				}while (hashMap.get(oldPoint).getOwner() == free);
				JOptionPane.showMessageDialog(null, "Can't place sheep there.", "Sorry", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			public void mouseClicked(MouseEvent e) {
				// mPt.x = x;
				// mPt.y = y;
				Point p = new Point(hexmech.pxtoHex(e.getX(), e.getY()));
				if (p.x < 0 || p.y < 0 || p.x >= BSIZE || p.y >= BSIZE)
					return;

				// DEBUG: colour in the hex which is supposedly the one clicked
				// on
				// clear the whole screen first.
				/*
				 * for (int i=0;i<BSIZE;i++) { for (int j=0;j<BSIZE;j++) {
				 * board[i][j]=EMPTY; } }
				 */

				// What do you want to do when a hexagon is clicked?

				// //even x point of reference
				// board[0][1] = 'A';
				// board[1][0] = 'B';
				// board[1][1] = 'C';
				// board[2][1] = 'D';
				// //odd
				// board[1][2] = 'A';
				// board[2][3] = 'B';
				// board[2][2] = 'C';
				// board[3][2] = 'D';
				try {
					// if(p.x>BSIZE-2) {

					if (board[p.x][p.y].getOwner() == player && isHolding == false) {

						holding = Integer.parseInt(
								JOptionPane.showInputDialog("How many sheeps would you like to get from here?", "0"));

						if (board[p.x][p.y].getValue() - holding < 1) {
							JOptionPane.showMessageDialog(null, "You should leave at least one 1 sheep");
							holding = 0;
						} else {// take sheeps from player cell
							isHolding = true;
							lblxcoord.setText(Integer.toString(p.x));
							lblycoord.setText(Integer.toString(p.y));

							lblSheepAtHand.setText(Integer.toString(holding));

							System.out.println(hashMap.get(new Coordinate(p.x, p.y)).getValue());
							System.out.println("munis");
							System.out.println(holding);

							hashMap.put(new Coordinate(p.x, p.y),
									new GuiCell(hashMap.get(new Coordinate(p.x, p.y)).getValue() - holding, player));

							updateBoard();
						}

					} else if ((board[p.x][p.y].getOwner() == player || board[p.x][p.y].getOwner() == ai)
							&& isHolding == true) {
						JOptionPane.showMessageDialog(null, "Please select a hex that is free");
					} else if (isHolding == false) {
						JOptionPane.showMessageDialog(null, "Please select sheeps first");

					}

					else if (board[p.x][p.y].getOwner() == free && isHolding == true) {
						int oldX = Integer.parseInt(lblxcoord.getText());
						int oldY = Integer.parseInt(lblycoord.getText());
						Coordinate oldCoordinate =  new Coordinate(oldX, oldY);
						Coordinate newCoordinate =  new Coordinate(p.x, p.y);
						if (validPlace(oldCoordinate, newCoordinate)/*oldX - p.x == oldY - p.y || p.x - oldX == oldY - p.y || oldX - p.x == p.y - oldY
								|| p.x - oldX == p.y - oldY || oldX == p.x*/) {

							hashMap.replace(new Coordinate(p.x, p.y), new GuiCell(holding, player));

							isHolding = false;
							holding = 0;
							lblSheepAtHand.setText("none");
							lblxcoord.setText("none");
							lblycoord.setText("none");
							updateBoard();

							System.out.println("CANGED");
						}
					}

					else if (board[p.x][p.y].getOwner() == ai) {
						JOptionPane.showMessageDialog(null, "You cannot select sheeps that aren't yours");
					}

					else if (board[p.x][p.y].getOwner() == ai && isHolding == true) {
						JOptionPane.showMessageDialog(null, "You cannot add sheeps to hex that are not yours.");
					}

					// board[p.x][p.y] = "X";
					// board[p.x + 1][p.y] = "X";
					// board[p.x + 1][p.y - 1] = "X";
					// board[p.x + 2][p.y] = "X";
					// }

				} catch (IndexOutOfBoundsException e1) {
					System.out.println("caught exception at hexagon: " + p.x + " " + p.y + "value: " + board[p.x][p.y]);
				}

				repaint();
			}


		} // end of MyMouseListener class
	} // end of DrawingPanel class
}

public class GuiCell {
	private int value;
	private int owner; // 1 for AI, 0 for wala, -1 for player

	public GuiCell(int value, int owner) {
		super();
		this.value = value;
		this.owner = owner;
	}

	// public GuiCell(int x, int y, int value, int owner) {
	// super();
	// this.x = x;
	// this.y = y;
	// this.value = value;
	// this.owner = owner;
	// this.legitCell = true;
	// }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public void print() {

		System.out.println("Value: " + value);
		System.out.println("Owner: " + owner);
	}

}
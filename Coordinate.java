
public class Coordinate {

	private int x;
	private int y;

	public Coordinate(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		int tmp = (y + ((x + 1) / 2));
		return x + (tmp * tmp);
	}

	@Override
	public boolean equals(Object o) {
		Coordinate s = (Coordinate) o;
		if (x != s.getX()) {
			return false;
		}

		if (y != s.getY()) {
			return false;
		}

		return true;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void print() {
		System.out.print("X: " + x);
		System.out.println("Y: " + y);
	}

}

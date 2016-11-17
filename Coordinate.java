import java.util.ArrayList;

public class Coordinate {

	private int x;
	private int y;

	public Coordinate(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Coordinate(Coordinate key) {
		// TODO Auto-generated constructor stub
		x = key.x;
		y = key.y;
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

	public Coordinate goUp(){
		this.setY(this.getY()-1);
		return this;
	}
	public Coordinate goDown(){
		this.setY(this.getY()+1);
		return this;
	}
	public Coordinate goLeftUp(){
		if(this.getX()%2==0){
			this.setX(this.getX()-1);
			this.setY(this.getY()-1);
		}
		else{
			this.setX(this.getX()-1);
		}
		return this;
	}
	public Coordinate goLeftDown(){
		if(this.getX()%2==0){
			this.setX(this.getX()-1);
		}
		else{
			this.setX(this.getX()-1);
			this.setY(this.getY()+1);
		}
		return this;
	}
	public Coordinate goRightUp(){
		if(this.getX()%2==0){
			this.setX(this.getX()+1);
			this.setY(this.getY()-1);
		}
		else{
			this.setX(this.getX()+1);
		}
		return this;
	}
	public Coordinate goRightDown(){
		if(this.getX()%2==0){// old x cell even thus:
			this.setX(this.getX()+1);
		}
		else{// if old x cell is odd then:
			this.setX(this.getX()+1);
			this.setY(this.getY()+1);
		}
		return this;
	}
	public ArrayList<Coordinate> getSurroundingCoordinates(){
		ArrayList<Coordinate> surroundingCoordinates = new ArrayList<>();
		Coordinate newCoor = new Coordinate(this.getX(), this.getY());

		surroundingCoordinates.add(newCoor.goUp());
		newCoor = new Coordinate(this.getX(), this.getY());
//		this.goDown();
		surroundingCoordinates.add(newCoor.goDown());
		newCoor = new Coordinate(this.getX(), this.getY());
//		this.goUp();
		surroundingCoordinates.add(newCoor.goLeftUp());
		newCoor = new Coordinate(this.getX(), this.getY());
//		this.goRightDown();
		surroundingCoordinates.add(newCoor.goLeftDown());
		newCoor = new Coordinate(this.getX(), this.getY());
//		this.goRightUp();
		surroundingCoordinates.add(newCoor.goRightUp());
		newCoor = new Coordinate(this.getX(), this.getY());
//		this.goLeftDown();
		surroundingCoordinates.add(newCoor.goRightDown());

//		this.goLeftUp();

		return surroundingCoordinates;
	}

}

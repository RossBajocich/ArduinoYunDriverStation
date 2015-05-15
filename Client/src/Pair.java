
public class Pair {
	double left, right;

	public Pair(double left, double right) {
		this.left = left;
		this.right = right;
	}

	public double getLeft() {
		return left;
	}

	public double getRight() {
		return right;
	}

	public byte[] getBytes() {
		return ("L" + left + "R" + right).getBytes();
	}
}

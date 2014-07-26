package javascriptserver;

public class CircularArea extends Area {
	
	public int x, y, z, rad_sq;
	
	public CircularArea(int x, int y, int z, int radius) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.rad_sq = radius * radius;
	}
	
	public int radius() {
		return (int) Math.sqrt(rad_sq);
	}

	public boolean isInArea(int x, int y, int z) {
		int dx = this.x - x;
		int dy = this.y - y;
		int dz = this.z - z;
		return (dx * dx + dy * dy + dz * dz) <= rad_sq;
	}
	
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof CircularArea) {
			CircularArea area = (CircularArea) o;
			return area.x == this.x && area.y == this.y && area.z == this.z
					&& area.rad_sq == this.rad_sq;
		} else {
			return false;
		}
	}
}

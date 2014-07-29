package javascriptserver;

public class RectangularArea extends Area {

	public int x, y, z, width, height, depth;

	public RectangularArea(int x, int y, int z, int width, int height, int depth) {
		if (width < 0) {
			x += width;
			width = Math.abs(width);
		}
		if (height < 0) {
			y += height;
			height = Math.abs(height);
		}
		if (depth < 0) {
			z += depth;
			depth = Math.abs(depth);
		}
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	public boolean isInArea(int x, int y, int z) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height && z >= this.z && z <= this.z + this.depth;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof RectangularArea) {
			RectangularArea area = (RectangularArea) o;
			return area.x == this.x && area.y == this.y && area.z == this.z && area.width == this.width && area.height == this.height && area.depth == this.depth;
		} else {
			return false;
		}
	}
}

package mcjs;

public class RectangularArea extends Area
{

	public int x, y, z, width, height, depth;

	public RectangularArea(int x, int y, int z, int width, int height, int depth)
	{
		if (width < 0)
		{
			x += width;
			width = Math.abs(width);
		}
		if (height < 0)
		{
			y += height;
			height = Math.abs(height);
		}
		if (depth < 0)
		{
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

	public boolean isInArea(int x, int y, int z)
	{
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height && z >= this.z && z <= this.z + this.depth;
	}

	public boolean isArea(Area area)
	{
		if (area == null)
		{
			return false;
		}
		if (area instanceof RectangularArea)
		{
			RectangularArea a = (RectangularArea) area;
			return a.x == this.x && a.y == this.y && a.z == this.z && a.width == this.width && a.height == this.height && a.depth == this.depth;
		}
		else
		{
			return false;
		}
	}
}

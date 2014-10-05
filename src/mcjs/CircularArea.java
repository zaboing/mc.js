package mcjs;

public class CircularArea extends Area
{
	private static final long serialVersionUID = 280934956086758788L;
	
	public int x, y, z, rad_sq;

	public CircularArea(int x, int y, int z, int radius)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.rad_sq = radius * radius;
	}

	public int radius()
	{
		return (int) Math.sqrt(rad_sq);
	}

	public boolean isInArea(int x, int y, int z)
	{
		int dx = this.x - x;
		int dy = this.y - y;
		int dz = this.z - z;
		return (dx * dx + dy * dy + dz * dz) <= rad_sq;
	}

	public boolean isArea(Area area)
	{
		if (area == null)
		{
			return false;
		}
		if (area instanceof CircularArea)
		{
			CircularArea a = (CircularArea) area;
			return a.x == this.x && a.y == this.y && a.z == this.z && a.rad_sq == this.rad_sq;
		}
		else
		{
			return false;
		}
	}
}

package mcjs;

import java.util.ArrayList;
import java.util.List;

public class CompositeArea extends Area
{
	private static final long serialVersionUID = 1733830781132156702L;

	public final List<Area> areas = new ArrayList<Area>();

	public boolean isInArea(int x, int y, int z)
	{
		return areas.stream().filter(area -> area.isInArea(x, y, z)).count() > 0;
	}

	public boolean isArea(Area area)
	{
		return this == area;
	}

	public void include(Area area)
	{
		areas.add(area);
	}
}

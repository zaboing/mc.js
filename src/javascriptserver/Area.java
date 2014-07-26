package javascriptserver;

import jdk.nashorn.internal.runtime.ScriptObject;

public abstract class Area {
	public abstract boolean isInArea(int x, int y, int z);
	
	public abstract boolean equals(Object o);
	
	public boolean isArea(ScriptObject area) {
		return equals(convertFromJSObject(area));
	}
	
	public static Area convertFromJSObject(ScriptObject object) {
		Area area;
		if (object.has("radius")) {
			area = new CircularArea(object.getInt("x"), object.getInt("y"),
					object.getInt("z"), object.getInt("radius"));
		} else if (object.has("rad_sq")) {
			area = new CircularArea(object.getInt("x"), object.getInt("y"),
					object.getInt("z"),
					(int) Math.sqrt(object.getInt("radius")));
		} else {
			area = new RectangularArea(object.getInt("x"), object.getInt("y"),
					object.getInt("z"), object.getInt("width"),
					object.getInt("height"), object.getInt("depth"));
		}
		return area;
	}
}

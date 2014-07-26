package events;

import java.util.Arrays;

public enum EventType {
	AREA_ENTER("area.enter"), AREA_EXIT("area.exit"), AREA_MOVE("area.move");

	public String descriptor;

	EventType(String descriptor) {
		this.descriptor = descriptor;
	}

	public static EventType byDescriptor(String descriptor) {
		return Arrays.asList(EventType.values()).stream()
				.filter(type -> type.descriptor.equals(descriptor)).findFirst()
				.get();
	}
}
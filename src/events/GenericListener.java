package events;

import java.util.function.Consumer;

public class GenericListener {

	public Consumer<Event> callback;
	
	public GenericListener(Consumer<Event> callback) {
		this.callback = callback;
	}
	
	public void onEvent(Event event) {
		callback.accept(event);
	}
}

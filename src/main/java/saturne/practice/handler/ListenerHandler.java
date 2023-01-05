package saturne.practice.handler;

import saturne.practice.Main;
import saturne.practice.handler.listeners.PlayerListener;

public class ListenerHandler {
	
	public ListenerHandler(final Main main) {
		main.registerListeners(new PlayerListener());
	}

}
